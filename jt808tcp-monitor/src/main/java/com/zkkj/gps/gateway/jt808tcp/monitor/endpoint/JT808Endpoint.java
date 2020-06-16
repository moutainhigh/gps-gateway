package com.zkkj.gps.gateway.jt808tcp.monitor.endpoint;

import com.zkkj.gps.gateway.jt808tcp.monitor.annotation.Endpoint;
import com.zkkj.gps.gateway.jt808tcp.monitor.annotation.Mapping;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.entity.CommonResult;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.entity.Message;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.entity.TerminalBaseDto;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.entity.attribute.TerminalAttributeDto;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.entity.params.TerminalParamsDto;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol.*;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol.hhdpro.dispatch.ElecDispatchInfo;
import com.zkkj.gps.gateway.jt808tcp.monitor.session.MessageManager;
import com.zkkj.gps.gateway.jt808tcp.monitor.session.Session;
import com.zkkj.gps.gateway.jt808tcp.monitor.session.SessionManager;
import com.zkkj.gps.gateway.jt808tcp.monitor.utils.LoggerUtils;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static com.zkkj.gps.gateway.jt808tcp.monitor.commons.Constants.terminalCacheList;
import static com.zkkj.gps.gateway.jt808tcp.monitor.jt808.common.MessageId.*;

/**
 * 消息发送与接收服务
 * @author suibozhuliu
 */
@Slf4j
@Endpoint
@Component
public class JT808Endpoint {

    private SessionManager sessionManager = SessionManager.getInstance();

    private MessageManager messageManager = MessageManager.INSTANCE;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${terminal_service}")
    private String terminalService;

    /**
     * 同步锁集合
     */
    private Map<String,List<TerminalBaseDto>> syncLockMap = new Hashtable<>();

    /**
     * 消息发送业务处理《平台----->终端》
     * @param message
     * @param hasReplyFlowIdId
     * @param reqProtocolId 请求协议ID
     * @return
     */
    public TerminalBaseDto sendMessage(Message message, boolean hasReplyFlowIdId, String reqProtocolId) {
        String mobileNumber = message.getMobileNumber();
        Session session = sessionManager.getByMobileNumber(mobileNumber);
        TerminalBaseDto terminalBaseDto = new TerminalBaseDto();
        if (ObjectUtils.isEmpty(session)){
            terminalBaseDto.setResultCode(1);
            terminalBaseDto.setResMsg("设备不在线或通道已关闭！");
            return terminalBaseDto;
        }
        message.setSerialNumber(session.currentFlowId());
        session.getChannel().writeAndFlush(message);
        ReferenceCountUtil.release(message);
        String key;
        if (hasReplyFlowIdId){
             key = mobileNumber + (hasReplyFlowIdId ? message.getSerialNumber() : "");
        } else {
            key = mobileNumber + reqProtocolId;
        }
        terminalBaseDto.setReqProtocolId(reqProtocolId);
        setSyncLockMap(key, terminalBaseDto);
        //System.out.println("指令发送的key值：【" + key + "】；message：【" + message.toString() + "】");
        synchronized (terminalBaseDto) {
            try {
                terminalBaseDto.wait(20 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (!CollectionUtils.isEmpty(syncLockMap.get(key))) {
            terminalBaseDto = syncLockMap.get(key).get(0);
            if (!ObjectUtils.isEmpty(terminalBaseDto) &&
                    !StringUtils.isEmpty(terminalBaseDto.getResProtocolId())) {
                StringBuffer resMsg = new StringBuffer();
                //默认失败
                Integer resCode = 1;
                switch (terminalBaseDto.getResProtocolId()) {
                    //终端通用应答（终端参数设置/设备车牌下发）
                    case "0001":
                        if (!StringUtils.isEmpty(terminalBaseDto.getReqProtocolId())) {
                            //设备车牌下发
                            if (terminalBaseDto.getReqProtocolId().equals("81030083")) {
                                resMsg.append("设备车牌下发");
                                //终端参数下发
                            } else if (terminalBaseDto.getReqProtocolId().equals("8103")) {
                                resMsg.append("终端参数下发");
                            } else {
                                return null;
                            }
                        }
                        resCode = terminalBaseDto.getResultCode();
                        switch (resCode) {
                            //成功/确认
                            case 0:
                                resMsg.append("操作成功！");
                                break;
                            //失败
                            case 1:
                                resMsg.append("操作失败，请重试！");
                                break;
                            //消息有误
                            case 2:
                                resMsg.append("操作失败，消息有误！");
                                break;
                            //不支持
                            case 3:
                                resMsg.append("操作失败，操作不支持！");
                                break;
                        }
                        break;
                    //读全参
                    case "0104":
                        if (!ObjectUtils.isEmpty(terminalBaseDto.getData()) &&
                                terminalBaseDto.getData() instanceof TerminalParamsDto) {
                            resCode = terminalBaseDto.getResultCode();
                        } else {
                            resCode = 1;
                        }
                        switch (resCode) {
                            case 0:
                                resMsg.append("终端参数读取成功！");
                                break;
                            case 1:
                                resMsg.append("终端参数读取失败，请重试！");
                                break;
                        }
                        break;
                    //读车牌
                    case "0083":
                        if (!ObjectUtils.isEmpty(terminalBaseDto.getData()) &&
                                terminalBaseDto.getData() instanceof String) {
                            resCode = terminalBaseDto.getResultCode();
                        } else {
                            resCode = 1;
                        }
                        switch (resCode) {
                            case 0:
                                resMsg.append("设备车牌读取成功！");
                                break;
                            case 1:
                                resMsg.append("设备车牌读取失败，请重试！");
                                break;
                        }
                        break;
                    //读终端属性
                    case "0107":
                        if (!ObjectUtils.isEmpty(terminalBaseDto.getData()) &&
                                terminalBaseDto.getData() instanceof TerminalAttributeDto) {
                            resCode = terminalBaseDto.getResultCode();
                        } else {
                            resCode = 1;
                        }
                        switch (resCode) {
                            case 0:
                                resMsg.append("终端属性读取成功！");
                                break;
                            case 1:
                                resMsg.append("终端属性读取失败，请重试！");
                                break;
                        }
                        break;
                    //下发电子运单结果
                    case "8182":
                        if (!ObjectUtils.isEmpty(terminalBaseDto.getData()) &&
                                terminalBaseDto.getData() instanceof Boolean) {
                            resCode = terminalBaseDto.getResultCode();
                        } else {
                            resCode = 1;
                        }
                        switch (resCode) {
                            case 0:
                                resMsg.append("电子运单下发成功！");
                                break;
                            case 1:
                                resMsg.append("电子运单下发失败，请重试！");
                                break;
                        }
                        break;
                    //读取电子运单结果
                    case "8183":
                        if (!ObjectUtils.isEmpty(terminalBaseDto.getData()) &&
                                terminalBaseDto.getData() instanceof ElecDispatchInfo) {
                            resCode = terminalBaseDto.getResultCode();
                        } else {
                            resCode = 1;
                        }
                        switch (resCode) {
                            case 0:
                                resMsg.append("电子运单读取成功！");
                                break;
                            case 1:
                                resMsg.append("电子运单读取失败，请重试！");
                                break;
                        }
                        break;
                    case "11EE":
                        if (!ObjectUtils.isEmpty(terminalBaseDto.getData()) &&
                                terminalBaseDto.getData() instanceof String) {
                            resCode = terminalBaseDto.getResultCode();
                        } else {
                            resCode = 1;
                        }
                        switch (resCode) {
                            case 0:
                                resMsg.append("电子车牌读取成功！");
                                break;
                            case 1:
                                resMsg.append("电子车牌读取失败，请重试！");
                                break;
                        }
                        break;
                    //电子车牌下发
                    case "11DD":
                        if (!ObjectUtils.isEmpty(terminalBaseDto.getData()) &&
                                terminalBaseDto.getData() instanceof Boolean) {
                            resCode = terminalBaseDto.getResultCode();
                        } else {
                            resCode = 1;
                        }
                        switch (resCode) {
                            case 0:
                                resMsg.append("电子车牌下发成功！");
                                break;
                            case 1:
                                resMsg.append("电子车牌下发失败，请重试！");
                                break;
                        }
                        break;
                    default:
                        return null;
                }
                terminalBaseDto.setResultCode(resCode);
                terminalBaseDto.setResMsg(resMsg.toString());
            }
        }
        syncLockMap.remove(key);
        return terminalBaseDto;
    }

    /**
     * 结果回调
     */
    public void resultCallback(TerminalBaseDto resBaseDto){
        if (!ObjectUtils.isEmpty(resBaseDto) && !StringUtils.isEmpty(resBaseDto.getResponseKey())){
            String responseKey = resBaseDto.getResponseKey();
            if (syncLockMap.containsKey(responseKey) && !ObjectUtils.isEmpty(syncLockMap.get(responseKey))){
                //获取协议下行时保存的请求对象
                List<TerminalBaseDto> reqBaseDtoList = syncLockMap.get(responseKey);
                cleanSyncLockMap(responseKey);
                if (!CollectionUtils.isEmpty(reqBaseDtoList)){
                    for (TerminalBaseDto reqBaseDto : reqBaseDtoList) {
                        resBaseDto.setReqProtocolId(reqBaseDto.getReqProtocolId());
                        setSyncLockMap(responseKey,resBaseDto);
                        synchronized (reqBaseDto){
                            reqBaseDto.notify();
                        }
                    }
                }
            }
        }
    }

    /**
     * 向同步锁集合设置同步对象
     * @param key
     * @param syncBean
     */
    private void setSyncLockMap (String key,TerminalBaseDto syncBean){
        if (syncLockMap.containsKey(key)){
            syncLockMap.get(key).add(syncBean);
        } else {
            List<TerminalBaseDto> syncBeans = new ArrayList<>();
            syncBeans.add(syncBean);
            syncLockMap.put(key,syncBeans);
        }
    }

    /**
     * 同步锁集合清空
     * @param key 同步锁集合键
     */
    private void cleanSyncLockMap(String key){
        if (syncLockMap.containsKey(key)){
            syncLockMap.remove(key);
        }
    }

    /**
     * 终端通用应答
     * @param message
     */
    @Mapping(types = 终端通用应答, desc = "终端通用应答")
    public void commonResponse(Message<CommonResult> message) {
        CommonResult body = message.getBody();
        String mobileNumber = message.getMobileNumber();
        Integer replyId = body.getReplyId();
        messageManager.put(mobileNumber + replyId, message);
    }

    /**
     * 查询终端参数应答
     * @param message
     */
    @Mapping(types = 查询终端参数应答, desc = "查询终端参数应答")
    public void getTerminalParameters(Message<JT_0104> message) {
        JT_0104 body = message.getBody();
        String mobileNumber = message.getMobileNumber();
        Integer replyId = message.getSerialNumber();
        messageManager.put(mobileNumber + replyId, message);
    }

    /**
     * 查询终端参数属性应答
     * @param message
     */
    @Mapping(types = 查询终端属性应答, desc = "查询终端属性应答")
    public void getTerminalProperty(Message<JT_0107> message) {
        JT_0107 body = message.getBody();
        String mobileNumber = message.getMobileNumber();
        messageManager.put(mobileNumber, message);
    }

    /**
     * 查询位置信息应答
     * @param message
     */
    @Mapping(types = {位置信息查询应答, 车辆控制应答}, desc = "位置信息查询应答/车辆控制应答")
    public void getPointResponse(Message<JT_0201> message) {
        JT_0201 body = message.getBody();
        String mobileNumber = message.getMobileNumber();
        Integer replyId = message.getSerialNumber();
        messageManager.put(mobileNumber + replyId, message);
    }

    /**
     * 终端心跳
     * @param message
     * @param session
     * @return
     */
    @Mapping(types = 终端心跳, desc = "终端心跳")
    public Message heartBeat(Message message, Session session) {
        CommonResult result = new CommonResult(终端心跳, message.getSerialNumber(), CommonResult.Success);
        return new Message(平台通用应答, session.currentFlowId(), message.getMobileNumber(), result);
    }

    /**
     * 补传分包请求
     * @param message
     * @param session
     * @return
     */
    @Mapping(types = 补传分包请求, desc = "补传分包请求")
    public Message replenishRequest(Message<JT_8003> message, Session session) {
        JT_8003 body = message.getBody();
        //TODO
        CommonResult result = new CommonResult(补传分包请求, message.getSerialNumber(), CommonResult.Success);
        return new Message(平台通用应答, session.currentFlowId(), message.getMobileNumber(), result);
    }

    /**
     * 终端注册
     * @param message
     * @param session
     * @return
     */
    @Mapping(types = 终端注册, desc = "终端注册")
    public Message<JT_8100> terminalRegister(Message<JT_0100> message, Session session) {
        JT_0100 body = message.getBody();
        //TODO
        sessionManager.put(Session.buildId(session.getChannel()), session);
        JT_8100 result = new JT_8100(message.getSerialNumber(), JT_8100.Success, "txzx_token");
        return new Message(终端注册应答, session.currentFlowId(), message.getMobileNumber(), result);
    }

    /**
     * 终端注销
     * @param message
     * @param session
     * @return
     */
    @Mapping(types = 终端注销, desc = "终端注销")
    public Message terminalUnregister(Message message, Session session) {
        //TODO
        CommonResult result = new CommonResult(终端注销, message.getSerialNumber(), CommonResult.Success);
        terminalCacheList.remove(message.getMobileNumber());
        return new Message(平台通用应答, session.currentFlowId(), message.getMobileNumber(), result);
    }

    @Mapping(types = 终端鉴权, desc = "终端鉴权")
    public Message authentication(Message<JT_0102> message, Session session) {
        //JT_0102 body = message.getBody();
        //TODO
        session.setTerminalId(message.getMobileNumber());
        if (!ObjectUtils.isEmpty(message) && !StringUtils.isEmpty(message.getMobileNumber())){
            String terminalId = message.getMobileNumber();
            try {
                if (!StringUtils.isEmpty(terminalId)){
                    redisTemplate.opsForValue().set(terminalId, terminalService);
                }
            } catch (Exception e){
                log.error("甲天行Redis存储异常：【" + e + "】");
            }
        }
        sessionManager.put(Session.buildId(session.getChannel()), session);
        CommonResult result = new CommonResult(终端鉴权, message.getSerialNumber(), CommonResult.Success);
        return new Message(平台通用应答, session.currentFlowId(), message.getMobileNumber(), result);
    }

    /**
     * 位置信息汇报
     * @param message
     * @param session
     * @return
     */
    @Mapping(types = 位置信息汇报, desc = "位置信息汇报")
    public Message pointUpload(Message<JT_0200> message, Session session) {
        JT_0200 body = message.getBody();
        //TODO
        CommonResult result = new CommonResult(位置信息汇报, message.getSerialNumber(), CommonResult.Success);
        return new Message(平台通用应答, session.currentFlowId(), message.getMobileNumber(), result);
    }

    /**
     * 电子运单上报
     * @param message
     * @param session
     * @return
     */
    @Mapping(types = 电子运单上报, desc = "电子运单上报")
    public Message elecDispatchUpLoad(Message message, Session session) {
        //TODO
        CommonResult result = new CommonResult(电子运单上报, message.getSerialNumber(), CommonResult.Success);
        return new Message(平台通用应答, session.currentFlowId(), message.getMobileNumber(), result);
    }

    /**
     * 定位数据批量上传
     * @param message
     * @param session
     * @return
     */
    @Mapping(types = 定位数据批量上传, desc = "定位数据批量上传")
    public Message pointBatchUpLoad(Message<JT_0704> message, Session session) {
        //JT_0704 body = message.getBody();
        //TODO
        CommonResult result = new CommonResult(定位数据批量上传, message.getSerialNumber(), CommonResult.Success);
        return new Message(平台通用应答, session.currentFlowId(), message.getMobileNumber(), result);
    }

    /**
     * 数据上行透传
     * @param message
     * @param session
     * @return
     */
    @Mapping(types = 数据上行透传, desc = "数据上行透传")
    public Message passthrough(Message<JT_0900> message, Session session) {
        JT_0900 body = message.getBody();
        //TODO
        CommonResult result = new CommonResult(数据上行透传, message.getSerialNumber(), CommonResult.Success);
        return new Message(平台通用应答, session.currentFlowId(), message.getMobileNumber(), result);
    }

}