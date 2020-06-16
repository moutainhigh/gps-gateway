package com.zkkj.gps.gateway.ccs.websocket;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.zkkj.gps.gateway.ccs.config.RedisDao;
import com.zkkj.gps.gateway.ccs.dto.token.TokenUser;
import com.zkkj.gps.gateway.ccs.dto.websocketDto.AlarmInfoSocket;
import com.zkkj.gps.gateway.ccs.utils.JSonUtils;
import com.zkkj.gps.gateway.ccs.websocket.bean.Client;
import com.zkkj.gps.gateway.common.utils.FastJsonUtils;
import com.zkkj.gps.gateway.terminal.monitor.constant.Constant;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmInfoDto.TerminalAlarmInfoDto;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @Auther: zkkjgs
 * @Description:报警信息socket
 * @Date: 2019-05-09 上午 10:16
 */
@Component
@ServerEndpoint(value = "/webSocketAlarmInfo/{token}")
public class WebSocketAlarmInfo {
    private static Logger logger = LoggerFactory.getLogger(WebSocketAlarmInfo.class);

    //在线用户数
    private static AtomicInteger onlineCount = new AtomicInteger();
    private static CopyOnWriteArraySet<Client> clients = new CopyOnWriteArraySet<>();

    private final static String SYS_USERNAME = "随波逐流";

    private Session session;
    //定义token和session关系
    private static Map<Session, String> map = new HashMap<Session, String>();

    //此处是解决无法注入的关键
    private static ApplicationContext webSocketAlarmInfoContext;
    //要注入的service或者dao
    private RedisDao redisDao;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        WebSocketAlarmInfo.webSocketAlarmInfoContext = applicationContext;
    }


    public TokenUser getUserInfo(String token) {
        TokenUser tokenUser = new TokenUser();
        //将token进行解析
        if (webSocketAlarmInfoContext != null && StringUtils.isNotBlank(token)) {
            redisDao = webSocketAlarmInfoContext.getBean(RedisDao.class);
            String tokenStr = redisDao.getValue(token);
            if (!ObjectUtils.isEmpty(tokenStr)) {
                tokenUser = JSonUtils.readValue(tokenStr, TokenUser.class);
            }

        }
        return tokenUser;
    }


    @OnOpen
    public void onOpen(Session session, @PathParam(value = "token") String token) {
        try {
            TokenUser tokenUser = getUserInfo(token);
            //TokenUser tokenUser = HttpExternal.getTokenUserByToken(token);
            if (tokenUser != null && tokenUser.getAppkey() != null && tokenUser.getAppkey().length() > 0) {
                //添加对应绑定关系
                //map.put(session, JSON.toJSONString(tokenUser));
                map.put(session, FastJsonUtils.toJSONString(tokenUser));
                //新的方法
                this.session = session;
                clients.add(new Client(session));
                onlineCount.incrementAndGet();
                logger.info("WebSocketAlarmInfoconnect: current online count:" + onlineCount.get());
                Map<String, List<TerminalAlarmInfoDto>> terminalAlarmCache = Constant.terminalAlarmInfoCache;
                if (MapUtils.isNotEmpty(terminalAlarmCache) && tokenUser.getTruckAndTerminalList() != null &&
                        tokenUser.getTruckAndTerminalList().size() > 0 && !StringUtils.isEmpty(tokenUser.getIdentity())) {
                    //获取登录用户的群组信息
                    String userIdentity = tokenUser.getIdentity();
                    List<String> terminalNoList = tokenUser.getTruckAndTerminalList().stream().map(x -> x.getTerminalNo()).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(terminalNoList)){
                        for (Map.Entry<String, List<TerminalAlarmInfoDto>> entry : terminalAlarmCache.entrySet()) {
                            if (terminalNoList.contains(entry.getKey())) {
                                List<TerminalAlarmInfoDto> terminalAlarmInfoDTOList = entry.getValue().stream().filter(s -> s.getAlarmResType() != null).collect(Collectors.toList());
                                for (TerminalAlarmInfoDto ter : terminalAlarmInfoDTOList) {
                                    if (!ObjectUtils.isEmpty(ter) && !ObjectUtils.isEmpty(ter.getDispatchNo()) && !StringUtils.isEmpty(ter.getIdentity())) {
                                        String[] identityArr = ter.getIdentity().split(",");
                                        if (identityArr != null && identityArr.length > 0){
                                            List<String> identityLists = Lists.newArrayList(identityArr);
                                            if (!CollectionUtils.isEmpty(identityLists) && identityLists.contains(userIdentity)){
                                                AlarmInfoSocket alarmInfoSocket = new AlarmInfoSocket(ter.getAppKey(), ter.getAlarmGroupId(), ter.getAlarmResType(),
                                                        ter.getIdentity(), ter.getTerminalId(), ter.getCarNum(), ter.getAlarmTime(), ter.getLongitude(), ter.getLatitude(),
                                                        ter.getAlarmInfo(), ter.getAlarmType(), ter.getRemark());
                                                String msg = FastJsonUtils.toJSONString(alarmInfoSocket);
                                                if (session != null && session.isOpen()) {
                                                    sendMsg(msg, session.getId());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                /*if (MapUtils.isNotEmpty(terminalAlarmCache) && tokenUser.getTruckAndTerminalList() != null && tokenUser.getTruckAndTerminalList().size() > 0) {
                    List<String> terminalNoList = tokenUser.getTruckAndTerminalList().stream().map(x -> x.getTerminalNo()).collect(Collectors.toList());
                    for (Map.Entry<String, List<TerminalAlarmInfoDto>> entry : terminalAlarmCache.entrySet()) {
                        if (terminalNoList.contains(entry.getKey())) {
                            List<TerminalAlarmInfoDto> terminalAlarmInfoDTOList = entry.getValue().stream().filter(s -> s.getAlarmResType() != null).collect(Collectors.toList());
                            for (TerminalAlarmInfoDto ter : terminalAlarmInfoDTOList) {
                                if (!ObjectUtils.isEmpty(ter.getDispatchNo())) {
                                    AlarmInfoSocket alarmInfoSocket = new AlarmInfoSocket(ter.getAppKey(), ter.getAlarmGroupId(), ter.getAlarmResType(),
                                            ter.getIdentity(), ter.getTerminalId(), ter.getCarNum(), ter.getAlarmTime(), ter.getLongitude(), ter.getLatitude(),
                                            ter.getAlarmInfo(), ter.getAlarmType(), ter.getRemark());
                                    //String msg = JSON.toJSONString(alarmInfoSocket);
                                    String msg = FastJsonUtils.toJSONString(alarmInfoSocket);
                                    if (session != null && session.isOpen()) {
                                        sendMsg(msg, session.getId());
                                    }
                                }
                            }
                        }
                    }
                }*/
            }
        } catch (Exception ex) {
            logger.error("WebSocketAlarmInfo.onOpen is error", ex);
        }
    }

    @OnClose
    public void onClose() {
        try {
            map.remove(this);
            onlineCount.decrementAndGet();    //在线数减1
        } catch (Exception ex) {
            logger.error("WebSocketAlarmInfo.onClose is errro", ex);
        }
    }


    @OnMessage
    public void onMessage(String token, Session session) {
        try {
            TokenUser tokenUser = getUserInfo(token);
            if (tokenUser != null && tokenUser.getAppkey() != null && tokenUser.getAppkey().length() > 0) {
                //map.put(session, JSON.toJSONString(tokenUser));
                map.put(session, FastJsonUtils.toJSONString(tokenUser));
                Client client = clients.stream().filter(cli -> cli.getSession() == session)
                        .collect(Collectors.toList()).get(0);
            }
        } catch (Exception e) {
            logger.error("WebSocketAlarmInfo.onMessage is error", e);
        }

    }

    //发送给对应的公司报警信息
    public synchronized static void sendMessage(AlarmInfoSocket alarmInfoSocket) {
        //String msg = JSON.toJSONString(alarmInfoSocket);
        String msg = FastJsonUtils.toJSONString(alarmInfoSocket);
        List<Session> sessionIdList = sendMegBySessionList(alarmInfoSocket);
        for (Session session : sessionIdList) {
            if (session != null && session.isOpen()) {
                sendMsg(msg, session.getId());
            }
        }
    }

    //获取需要推送的session
    public static List<Session> sendMegBySessionList(AlarmInfoSocket websocketDto) {
        List<Session> sessionIdList = new ArrayList<>();
        for (Map.Entry<Session, String> entry : map.entrySet()) {
            TokenUser tokenUser = JSON.parseObject(entry.getValue(), TokenUser.class);
            if (tokenUser != null && tokenUser.getTruckAndTerminalList() != null &&
                    tokenUser.getTruckAndTerminalList().size() > 0 &&
                    !StringUtils.isEmpty(tokenUser.getIdentity())) {
                String userIdentity = tokenUser.getIdentity();
                String[] identityArr = userIdentity.split(",");
                if (identityArr != null && identityArr.length > 0) {
                    List<String> identityLists = Lists.newArrayList(identityArr);
                    if (!CollectionUtils.isEmpty(identityLists) &&
                            tokenUser.getTruckAndTerminalList().stream().anyMatch(x -> x.getTerminalNo().equals(websocketDto.getTerminalId())) &&
                            identityLists.contains(tokenUser.getIdentity())) {
                        sessionIdList.add(entry.getKey());
                    }
                }
            }
            /*if (tokenUser != null && tokenUser.getTruckAndTerminalList() != null && tokenUser.getTruckAndTerminalList().size() > 0) {
                if (tokenUser.getTruckAndTerminalList().stream().anyMatch(x -> x.getTerminalNo().equals(websocketDto.getTerminalId()))) {
                    sessionIdList.add(entry.getKey());
                }
            }*/
        }
        return sessionIdList;
    }


    public synchronized static void sendMsg(String message, String sessionID) {
        clients.forEach(client -> {
            if (sessionID.equals(client.getSession().getId())) {
                try {
                    client.getSession().getBasicRemote().sendText(message);
                } catch (IOException e) {
                    logger.error("WebSocketAlarmInfo.sendMsg is error", e);
                }
            }
        });
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
                        logger.error("WebSocketAlarmInfo.sendAll is error", e);
                    }
                });

    }


    @OnError
    public void onError(Session session, Throwable error) {
        try {
            clients.forEach(client -> {
                if (client.getSession().getId().equals(session.getId())) {
                    clients.remove(client);
                    onlineCount.decrementAndGet();
                }
            });
        } catch (Exception e) {
            logger.error("WebSocketAlarmInfo.onError is error", e);
        }
    }

}
