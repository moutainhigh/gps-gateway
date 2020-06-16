package com.zkkj.gps.gateway.ccs.websocket;


import com.google.common.collect.Maps;
import com.zkkj.gps.gateway.ccs.config.RedisDao;
import com.zkkj.gps.gateway.ccs.dto.common.ResultVo;
import com.zkkj.gps.gateway.ccs.dto.token.TokenUser;
import com.zkkj.gps.gateway.ccs.dto.token.TruckAndTerminal;
import com.zkkj.gps.gateway.ccs.dto.websocketDto.GPSPositionDto;
import com.zkkj.gps.gateway.ccs.enume.WebSocketTypeEnum;
import com.zkkj.gps.gateway.ccs.exception.ParamException;
import com.zkkj.gps.gateway.ccs.exception.TokenException;
import com.zkkj.gps.gateway.ccs.utils.GPSPositionUtils;
import com.zkkj.gps.gateway.ccs.websocket.bean.Client;
import com.zkkj.gps.gateway.common.utils.DateTimeUtils;
import com.zkkj.gps.gateway.common.utils.FastJsonUtils;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BaseGPSPositionDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BasicPositionDto;
import com.zkkj.gps.gateway.terminal.monitor.task.GpsMonitorSingle;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Auther: zkkjgs
 * @Description:位置信息socket
 * @Date: 2019-05-09 上午 10:16
 */
@Component
@ServerEndpoint(value = "/webSocketPosition/{token}")
public class WebSocketPosition {

    private static Logger logger = LoggerFactory.getLogger(WebSocketPosition.class);

    //在线用户数
    private static AtomicInteger onlineCount = new AtomicInteger();

    private static CopyOnWriteArraySet<Client> clients = new CopyOnWriteArraySet<>();

    private final static String SYS_USERNAME = "随波逐流";

    private Session session;

    //定义token和session关系
    private static Map<Session, String> map = Maps.newConcurrentMap();

    private static ApplicationContext applicationContext;

    //要注入的service或者dao
    private static RedisDao redisDao;

    //存储当前窗口下对应token下的设备信息
    private static Map<Session, Map<String, TruckAndTerminal>> tokenTruckAndTerminalMap = Maps.newConcurrentMap();

    //session和登录传入设备编号绑定关系
    private static Map<Session, String> terminalsMap = Maps.newConcurrentMap();

    public static void setApplicationContext(ApplicationContext applicationContext) {
        WebSocketPosition.applicationContext = applicationContext;
    }

    /**
     * 1.建立连接
     * 2.添加session和redis的对应关系
     *
     * @param session
     * @param token
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "token") String token) {
        if (StringUtils.isNotBlank(token)) {
            try {
                //第一次加载判断
                if (applicationContext != null) {
                    redisDao = applicationContext.getBean(RedisDao.class);
                }
                //添加session和redis的对应关系
                logger.error("WebSocketPosition.onOpen session" + session + ";token:" + token);
                map.put(session, token);
                clients.add(new Client(session));
                onlineCount.incrementAndGet();
                this.session = session;
                logger.info("WebSocketPosition: current online count:" + onlineCount.get());
            } catch (Exception e) {
                logger.error("WebSocketPosition.onOpen is error", e);
            }
        }
    }


    @OnClose
    public void onClose(Session session) {
        try {
            map.remove(session);
            clients.remove(new Client(session));
            onlineCount.decrementAndGet();    //在线数减1
        } catch (Exception ex) {
            logger.error("WebSocketPosition.onClose is error", ex);
        }

    }

    @OnMessage
    public void onMessage(String terminalNos, Session session) {
        ResultVo<Map<String, GPSPositionDto>> resultVo = new ResultVo<>();
        try {
            if (StringUtils.isBlank(terminalNos)) {
                throw new ParamException("设备号不能为空");
            }
            if (session == null) {
                throw new ParamException("当前会话已关闭");
            }
            logger.error("WebSocketPosition.onMessage session" + session + ";terminalNos:" + terminalNos);
            terminalsMap.put(session, terminalNos);
            Map<String, GPSPositionDto> gpsPositionMap = getGPSPosition(session);
            if (MapUtils.isEmpty(gpsPositionMap)) {
                resultVo.resultFail("未获取到定位信息");
            } else {
                resultVo.resultSuccess(gpsPositionMap);
            }
        } catch (ParamException e) {
            logger.error("WebSocketPosition.onMessage is error", e);
            resultVo.resultFail(e.getMessage());
        } catch (TokenException e) {
            logger.error("WebSocketPosition.onMessage is error", e);
            resultVo.resultFail(e.getMessage());
        } catch (Exception ex) {
            logger.error("WebSocketPosition.onMessage is error", ex);
            resultVo.resultFail("系统异常");
        }
        sendMsg(FastJsonUtils.toJSONString(resultVo), this.session);
    }

    /**
     * 建立连接获取所有登录条件对应的所有设备的定位信息
     *
     * @param session
     */
    private Map<String, GPSPositionDto> getGPSPosition(Session session) {
        Map<String, GPSPositionDto> curGPSPositionMap = getCurGPSPositionMap(session, map, terminalsMap, redisDao, tokenTruckAndTerminalMap);
        return curGPSPositionMap;
    }

    /**
     * 从设备定位缓存中获取缓存设备的定位信息
     *
     * @param terminalMap
     * @param truckAndTerminalList
     * @param session
     * @param tokenTruckAndTerminalMap
     * @return
     */
    public static Map<String, GPSPositionDto> sendGPSPositionMap(Map<Session, String> terminalMap, List<TruckAndTerminal> truckAndTerminalList, Session session, Map<Session, Map<String, TruckAndTerminal>> tokenTruckAndTerminalMap) {
        String terminalNos = terminalMap.get(session);
        Map<String, TruckAndTerminal> truckAndTerminalMap = new HashMap<>();
        Map<String, GPSPositionDto> concurrentMap = Maps.newConcurrentMap();
        logger.error("sendGPSPositionMap:session" + session + ";terminalNos:" + terminalNos);
        if (StringUtils.isNotBlank(terminalNos)) {
            Map<String, BaseGPSPositionDto> mapLatestAvailablePosition = GpsMonitorSingle.getInstance().getMapLatestAvailablePosition();
            if ("*".equals(terminalNos)) {
                for (TruckAndTerminal terminal : truckAndTerminalList) {
                    GPSPositionDto gpsPositionDto = GPSPositionUtils.getGPSPositionDto(mapLatestAvailablePosition.get(terminal.getTerminalNo()));
                    if (gpsPositionDto != null) {
                        gpsPositionDto.setTruckNo(terminal.getTruckNo());
                        concurrentMap.put(gpsPositionDto.getSimId(), gpsPositionDto);
                        truckAndTerminalMap.put(gpsPositionDto.getSimId(), terminal);
                    }
                }
            } else {
                String[] terminalNoArr = terminalNos.split(",");
                if (terminalNoArr != null && terminalNoArr.length > 0) {
                    for (String terminal : terminalNoArr) {
                        for (TruckAndTerminal terminal1 : truckAndTerminalList) {
                            if (terminal.equals(terminal1.getTerminalNo())) {
                                GPSPositionDto gpsPositionDto = GPSPositionUtils.getGPSPositionDto(mapLatestAvailablePosition.get(terminal));
                                if (gpsPositionDto != null) {
                                    gpsPositionDto.setTruckNo(terminal1.getTruckNo());
                                    concurrentMap.put(gpsPositionDto.getSimId(), gpsPositionDto);
                                    truckAndTerminalMap.put(gpsPositionDto.getSimId(), terminal1);
                                    continue;
                                }
                            }
                        }
                    }
                }
            }
            tokenTruckAndTerminalMap.put(session, truckAndTerminalMap);
        }
        logger.error("concurrentMap.size:" + concurrentMap.size());
        return concurrentMap;
    }

    /**
     * 获取redis中该token对应的设备列表
     *
     * @param token
     * @return
     */
    public static List<TruckAndTerminal> getTruckAndTerminalList(String token, RedisDao redisDao) {
        if (redisDao == null) {
            logger.error("getTruckAndTerminalList:redisDao=null");
            return null;
        }
        TokenUser tokenUser = FastJsonUtils.toBean(redisDao.getValue(token), TokenUser.class);
        if (tokenUser == null || CollectionUtils.isEmpty(tokenUser.getTruckAndTerminalList())) {
            return null;
        }
        return tokenUser.getTruckAndTerminalList();
    }

    public synchronized void sendMessage(String terminalId) {
        Map<String, BaseGPSPositionDto> positionMap = GpsMonitorSingle.getInstance().getMapLatestAvailablePosition();
        if (MapUtils.isNotEmpty(map) && redisDao != null && positionMap.get(terminalId) != null && MapUtils.isNotEmpty(tokenTruckAndTerminalMap)) {
            //从缓存中获取当前设备的定位信息
            for (Map.Entry<Session, Map<String, TruckAndTerminal>> entry : tokenTruckAndTerminalMap.entrySet()) {
                ResultVo<Map<String, Map<String, GPSPositionDto>>> resultVo = new ResultVo<>();
                try {
                    if (entry.getKey() != null) {
                        if (map.get(entry.getKey()) != null) {
                            if (StringUtils.isBlank(redisDao.getValue(map.get(entry.getKey())))) {
                                map.remove(entry.getKey());
                                throw new TokenException("令牌失效");
                            }
                            if (MapUtils.isNotEmpty(entry.getValue()) && entry.getValue().get(terminalId) != null) {
                                //点位集合
                                Map<String, GPSPositionDto> curMap = new HashMap<>();
                                //返回前端该点位类型集合
                                GPSPositionDto gpsPosition = getGpsPosition(positionMap.get(terminalId));
                                gpsPosition.setTruckNo(entry.getValue().get(terminalId).getTruckNo());
                                curMap.put(terminalId, gpsPosition);
                                Map<String, Map<String, GPSPositionDto>> curGPSPositionMap = Maps.newHashMap();
                                curGPSPositionMap.put(WebSocketTypeEnum.ORDINARY.name(), curMap);
                                resultVo.setData(curGPSPositionMap);
                            }
                        }
                    }
                } catch (TokenException e) {
                    logger.error("WebSocketPosition.sendMessage is error", e);
                    resultVo.resultFail(e.getMessage());
                } catch (Exception e) {
                    logger.error("WebSocketPosition.sendMessage is error", e);
                    resultVo.resultFail("系统异常");
                }
                if ((resultVo.isSuccess() && MapUtils.isNotEmpty(resultVo.getData())) || !resultVo.isSuccess()) {
                    sendMsg(FastJsonUtils.toJSONString(resultVo), entry.getKey());
                }
            }
        }
    }

    /**
     * 获取websocket定位对象
     *
     * @param baseGPSPositionDto
     * @return
     */
    private GPSPositionDto getGpsPosition(BaseGPSPositionDto baseGPSPositionDto) {
        BasicPositionDto basicPositionDto = baseGPSPositionDto.getPoint();
        GPSPositionDto gpsPosition = new GPSPositionDto();
        gpsPosition.setCourse(basicPositionDto.getDirection() == null ? 0 : basicPositionDto.getDirection());
        gpsPosition.setGpsTime(DateTimeUtils.formatLocalDateTime(basicPositionDto.getDate()));
        gpsPosition.setRecTime(DateTimeUtils.formatLocalDateTime(baseGPSPositionDto.getRcvTime()));
        gpsPosition.setLatitude(basicPositionDto.getLatitude() == null ? 0.0 : basicPositionDto.getLatitude());
        gpsPosition.setLongitude(basicPositionDto.getLongitude() == null ? 0.0 : basicPositionDto.getLongitude());
        gpsPosition.setMilesKM(basicPositionDto.getMileage() == null ? 0.0 : Double.valueOf(basicPositionDto.getMileage()));
        gpsPosition.setSimId(baseGPSPositionDto.getTerminalId());
        gpsPosition.setSpeed(basicPositionDto.getSpeed() == null ? 0.0 : Double.valueOf(basicPositionDto.getSpeed()));
        gpsPosition.setPower(basicPositionDto.getPower());
        gpsPosition.setAlarmState(basicPositionDto.getAlarmState() == null ? 0 : basicPositionDto.getAlarmState());
        gpsPosition.setCarState(0);
        gpsPosition.setTerminalState(basicPositionDto.getTerminalState() == null ? 0 : basicPositionDto.getTerminalState());
        gpsPosition.setLoad(basicPositionDto.getLoadSensorValue());
        return gpsPosition;
    }

    public void sendMsg(String message, Session session) {
        clients.forEach(client -> {
            synchronized (client.getSession()) {
                if (session != null && session.isOpen() && session.equals(client.getSession())) {
                    try {
                        client.getSession().getBasicRemote().sendText(message);
                    } catch (IOException e) {
                        logger.error("WebSocketPositionconnect.sendMsg is error", e);
                    }
                }
            }
        });
    }


    /**
     * 获取当前session下设备定位信息
     * 1.获取当前session下的token，然后从token中获取设备信息
     * 2.获取当前session下的登录时缓存的设备，然后从设备定位缓存中获取缓存设备的定位信息
     *
     * @param session
     * @param tokenMap                 session对应的token缓存
     * @param terminalsMap             session对应登录传入的设备缓存
     * @param redisDao
     * @param tokenTruckAndTerminalMap
     * @return
     */
    public static Map<String, GPSPositionDto> getCurGPSPositionMap(Session session, Map<Session, String> tokenMap, Map<Session, String> terminalsMap, RedisDao redisDao, Map<Session, Map<String, TruckAndTerminal>> tokenTruckAndTerminalMap) {
        //取当前session下的token，然后从token中获取设备信息
        String token = tokenMap.get(session);
        if (redisDao == null) {
            logger.error("getCurGPSPositionMap.redisDao == null");
            return null;
        }
        if (StringUtils.isBlank(redisDao.getValue(token))) {
            map.remove(session);
            throw new TokenException("令牌失效");
        }
        List<TruckAndTerminal> truckAndTerminalList = getTruckAndTerminalList(token, redisDao);
        if (CollectionUtils.isEmpty(truckAndTerminalList)) {
            logger.error("truckAndTerminalList=null");
            return null;
        }
        //获取当前session下的登录时缓存的设备，然后从设备定位缓存中获取缓存设备的定位信息
        return sendGPSPositionMap(terminalsMap, truckAndTerminalList, session, tokenTruckAndTerminalMap);
    }

    /**
     * 信息群发，我们要排除服务端自己不接收到推送信息
     * 所以我们在发送的时候将服务端排除掉
     *
     * @param message
     */
    public synchronized static void sendAll(String message) {
        //群发，不能发送给服务端自己
        clients.stream().filter(cli -> !cli.getSession().getId().equals(SYS_USERNAME))
                .forEach(client -> {
                    try {
                        client.getSession().getBasicRemote().sendText(message);
                    } catch (IOException e) {
                        logger.error("WebSocketPosition.sendAll is error", e);
                    }
                });

    }


    @OnError
    public void onError(Session session, Throwable error) {
        clients.forEach(client -> {
            if (session != null && session.isOpen() && session.equals(client.getSession())) {
                try {
                    clients.remove(client);
                    onlineCount.decrementAndGet();
                } catch (Exception e) {
                    logger.error("WebSocketPosition.onError is error", e);
                }
            }
        });
    }

}
