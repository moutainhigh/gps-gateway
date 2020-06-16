package com.zkkj.gps.gateway.ccs.websocket;

import com.google.common.collect.Maps;
import com.zkkj.gps.gateway.ccs.config.RedisDao;
import com.zkkj.gps.gateway.ccs.dto.common.ResultVo;
import com.zkkj.gps.gateway.ccs.dto.dispatch.DispatchInfoDto;
import com.zkkj.gps.gateway.ccs.dto.token.TruckAndTerminal;
import com.zkkj.gps.gateway.ccs.dto.websocketDto.GPSPositionDto;
import com.zkkj.gps.gateway.ccs.dto.websocketDto.TaskMonitorPositionDto;
import com.zkkj.gps.gateway.ccs.dto.websocketDto.WebSocketDispatchInfo;
import com.zkkj.gps.gateway.ccs.dto.websocketDto.WebSocketLoginDto;
import com.zkkj.gps.gateway.ccs.exception.ParamException;
import com.zkkj.gps.gateway.ccs.exception.TokenException;
import com.zkkj.gps.gateway.ccs.monitorconfig.MonitorConfigCache;
import com.zkkj.gps.gateway.ccs.service.IGenerator;
import com.zkkj.gps.gateway.ccs.service.impl.Generator;
import com.zkkj.gps.gateway.ccs.utils.BeanValidate;
import com.zkkj.gps.gateway.ccs.utils.GPSPositionUtils;
import com.zkkj.gps.gateway.ccs.websocket.bean.Client;
import com.zkkj.gps.gateway.common.utils.DateTimeUtils;
import com.zkkj.gps.gateway.common.utils.FastJsonUtils;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BaseGPSPositionDto;
import com.zkkj.gps.gateway.terminal.monitor.task.GpsMonitorSingle;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * author : cyc
 * Date : 2020-05-10
 * 任务监控车辆定位webSocket通讯
 */
@Component
@ServerEndpoint("/webSocketMonitorPosition/{token}")
public class WebSocketMonitorPosition {

    private static Logger logger = LoggerFactory.getLogger(WebSocketPosition.class);

    //在线用户数
    private static AtomicInteger onlineCount = new AtomicInteger();

    private static CopyOnWriteArraySet<Client> clients = new CopyOnWriteArraySet<>();

    private Session session;

    //缓存当前连接session和token和关系
    private static Map<Session, String> tokenSessionMap = Maps.newConcurrentMap();

    private static IGenerator iGenerator;

    //缓存登录传入车辆
    private static Map<Session, WebSocketLoginDto> webSocketLoginDtoHashMap = Maps.newConcurrentMap();

    //缓存当前连接session和设备terminal
    private static Map<Session, String> terminalSessionMap = Maps.newConcurrentMap();

    //存储当前窗口下对应token下的设备信息
    private static Map<Session, Map<String, TruckAndTerminal>> tokenTruckAndTerminalMap = Maps.newConcurrentMap();

    //使用ApplicationContext注入对象，因为websocket是多例的与spring单例相违背，故spring不会为websocket创建的对象而依赖注入
    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        WebSocketMonitorPosition.applicationContext = applicationContext;
    }

    //要注入的service或者dao
    private static RedisDao redisDao;


    /**
     * webSocket建立连接
     *
     * @param session
     * @param token
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "token") String token) {
        try {
            if (applicationContext != null) {
                redisDao = applicationContext.getBean(RedisDao.class);
                iGenerator = applicationContext.getBean(Generator.class);
            }
            logger.error("WebSocketMonitorPosition.onOpen.session:" + session + ";token:" + token);
            tokenSessionMap.put(session, token);
            clients.add(new Client(session));
            this.session = session;
            onlineCount.incrementAndGet();
            logger.info("WebSocketMonitorPosition: current online count:" + onlineCount.get());
        } catch (Exception e) {
            logger.error("WebSocketMonitorPosition.onOpen is error", e);
        }
    }

    /**
     * 关闭session
     *
     * @param session
     */
    @OnClose
    public void onClose(Session session) {
        try {
            tokenSessionMap.remove(session);
            logger.error("WebSocketMonitorPosition.onClose tokenSessionMap.remove" + session);
            clients.remove(new Client(session));
            onlineCount.decrementAndGet();    //在线数减1
            webSocketLoginDtoHashMap.remove(session);
        } catch (Exception ex) {
            logger.error("WebSocketMonitorPosition.onClose is error", ex);
        }
    }


    /**
     * 发送信息
     *
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) {

        logger.error("WebSocketMonitorPosition.onMessage message:" + message + ";session:" + session);
        ResultVo<Map<String, TaskMonitorPositionDto>> resultVo = new ResultVo<>();
        try {
            WebSocketLoginDto webSocketLoginDto = FastJsonUtils.toBean(message, WebSocketLoginDto.class);
            BeanValidate.checkParam(webSocketLoginDto);
            if (session == null) {
                throw new ParamException("当前会话已关闭");
            }
            //获取当前session下的车辆定位信息
            webSocketLoginDtoHashMap.put(session, webSocketLoginDto);
            terminalSessionMap.put(session, webSocketLoginDto.getTerminalIds());
            //获取当前session下的
            Map<String, GPSPositionDto> curGPSPositionMap = WebSocketPosition.getCurGPSPositionMap(session, tokenSessionMap, terminalSessionMap, redisDao, tokenTruckAndTerminalMap);
            //获取运单信息
            Map<String, DispatchInfoDto> dispatchInfoDtoMap = MonitorConfigCache.getInstance().getDispatchInfoDtoMap();
            if (MapUtils.isEmpty(curGPSPositionMap) || MapUtils.isEmpty(dispatchInfoDtoMap)) return;
            Map<Session, Map<String, TaskMonitorPositionDto>> sessionTaskMonitorMap = Maps.newHashMap();
            Map<String, TaskMonitorPositionDto> taskMonitorPositionDtoMap = Maps.newHashMap();
            //获取当前identity下的运单
            for (Map.Entry<String, DispatchInfoDto> entry : dispatchInfoDtoMap.entrySet()) {
                if (StringUtils.isNotBlank(entry.getKey()) && entry.getValue() != null && StringUtils.isNotBlank(entry.getValue().getIdentity())
                        && curGPSPositionMap.get(entry.getKey()) != null) {
                    String[] identityArr = entry.getValue().getIdentity().split(",");
                    if (identityArr != null && identityArr.length > 0) {
                        for (String identity : identityArr) {
                            if (webSocketLoginDto.getIdentity().equals(identity)) {
                                TaskMonitorPositionDto taskMonitorPositionDto = getTaskMonitorPositionDto(entry.getValue(), curGPSPositionMap.get(entry.getKey()));
                                taskMonitorPositionDtoMap.put(entry.getKey(), taskMonitorPositionDto);
                                sessionTaskMonitorMap.put(session, taskMonitorPositionDtoMap);
                                break;
                            }
                        }
                    }
                }
            }
            if (MapUtils.isNotEmpty(taskMonitorPositionDtoMap)) {
                resultVo.resultSuccess(taskMonitorPositionDtoMap);
            } else {
                resultVo.resultFail("未获取到定位信息");
            }
        } catch (ParamException e) {
            logger.error("WebSocketMonitorPosition.onMessage is error", e);
            resultVo.resultFail(e.getMessage());
        } catch (TokenException e) {
            logger.error("WebSocketMonitorPosition.onMessage is error", e);
            resultVo.resultFail(e.getMessage());
        } catch (Exception e) {
            logger.error("WebSocketMonitorPosition.onMessage is error", e);
            resultVo.resultFail("系统异常");
        }
        sendMsg(FastJsonUtils.toJSONString(resultVo), session);
    }

    /**
     * 获取任务监控定位对象
     *
     * @param dispatchInfoDto
     * @param gpsPositionDto
     * @return
     */
    public TaskMonitorPositionDto getTaskMonitorPositionDto(DispatchInfoDto dispatchInfoDto, GPSPositionDto gpsPositionDto) {
        TaskMonitorPositionDto taskMonitorPositionDto = iGenerator.convert(gpsPositionDto, TaskMonitorPositionDto.class);
        BeanUtils.copyProperties(dispatchInfoDto, taskMonitorPositionDto);
        taskMonitorPositionDto.setTruckNo(dispatchInfoDto.getCarNumber());
        taskMonitorPositionDto.setCreateTime(DateTimeUtils.dateToStr(dispatchInfoDto.getCreateTime()));
        return taskMonitorPositionDto;
    }

    public synchronized void sendMessage(WebSocketDispatchInfo webSocketDispatchInfo) {
        if (redisDao == null || MapUtils.isEmpty(webSocketLoginDtoHashMap)
                || MapUtils.isEmpty(tokenSessionMap) || MapUtils.isEmpty(tokenTruckAndTerminalMap)
                || webSocketDispatchInfo.getDispatchInfoDto() == null) {
            return;
        }
        for (Map.Entry<Session, WebSocketLoginDto> entry : webSocketLoginDtoHashMap.entrySet()) {
            ResultVo<Map<String, Map<String, TaskMonitorPositionDto>>> resultVo = new ResultVo<>();
            try {
                if (entry.getValue() == null) {
                    continue;
                }
                WebSocketLoginDto webSocketLoginDto = entry.getValue();
                String token = tokenSessionMap.get(entry.getKey());
                if (token != null) {
                    if (StringUtils.isBlank(redisDao.getValue(token))) {
                        logger.error("WebSocketMonitorPosition.sendMessage.token:" + redisDao.getValue(token));
                        logger.error("WebSocketMonitorPosition.sendMessage tokenSessionMap.remove" + entry.getKey());
                        tokenSessionMap.remove(entry.getKey());
                        throw new TokenException("令牌失效");
                    }
                    Map<String, TruckAndTerminal> truckAndTerminalMap = tokenTruckAndTerminalMap.get(entry.getKey());
                    if (MapUtils.isEmpty(truckAndTerminalMap) || truckAndTerminalMap.get(webSocketDispatchInfo.getDispatchInfoDto().getTerminalNo()) == null) {
                        continue;
                    }
                    if (StringUtils.isNotBlank(webSocketDispatchInfo.getDispatchInfoDto().getIdentity())) {
                        String[] identityArr = webSocketDispatchInfo.getDispatchInfoDto().getIdentity().split(",");
                        if (identityArr != null && identityArr.length > 0) {
                            for (String identity : identityArr) {
                                if (identity.equals(webSocketLoginDto.getIdentity())) {
                                    //任务车辆位置对象 key为任务类型
                                    Map<String, Map<String, TaskMonitorPositionDto>> taskMonitorPositionMap = Maps.newHashMap();
                                    //任务对象
                                    Map<String, TaskMonitorPositionDto> taskMonitorMap = Maps.newHashMap();
                                    Map<String, BaseGPSPositionDto> mapLatestAvailablePosition = GpsMonitorSingle.getInstance().getMapLatestAvailablePosition();
                                    GPSPositionDto gpsPositionDto = GPSPositionUtils.getGPSPositionDto(mapLatestAvailablePosition.get(webSocketDispatchInfo.getDispatchInfoDto().getTerminalNo()));
                                    DispatchInfoDto dispatchInfoDto = webSocketDispatchInfo.getDispatchInfoDto();
                                    TaskMonitorPositionDto taskMonitorPositionDto = getTaskMonitorPositionDto(dispatchInfoDto, gpsPositionDto);
                                    taskMonitorMap.put(webSocketDispatchInfo.getDispatchInfoDto().getTerminalNo(), taskMonitorPositionDto);
                                    taskMonitorPositionMap.put(webSocketDispatchInfo.getWebSocketDispatchType(), taskMonitorMap);
                                    if (MapUtils.isNotEmpty(taskMonitorPositionMap)) {
                                        resultVo.resultSuccess(taskMonitorPositionMap);
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            } catch (ParamException e) {
                logger.error("WebSocketMonitorPosition.sendMessage is error", e);
                resultVo.resultFail(e.getMessage());
            } catch (TokenException e) {
                logger.error("WebSocketMonitorPosition.sendMessage is error", e);
                resultVo.resultFail(e.getMessage());
            } catch (Exception e) {
                logger.error("WebSocketMonitorPosition.sendMessage is error", e);
                resultVo.resultFail("系统异常");
            }
            if ((resultVo.isSuccess() && resultVo.getData() != null) || !resultVo.isSuccess()) {
                sendMsg(FastJsonUtils.toJSONString(resultVo), entry.getKey());
            }
        }
    }

    /**
     * 发送消息
     *
     * @param message
     * @param session
     */
    public void sendMsg(String message, Session session) {
        clients.forEach(client -> {
            synchronized (client.getSession()) {
                if (session != null && session.isOpen() && session.equals(client.getSession())) {
                    try {
                        client.getSession().getBasicRemote().sendText(message);
                    } catch (IOException e) {
                        logger.error("WebSocketMonitorPosition.sendMsg is error", e);
                    }
                }
            }
        });
    }

}
