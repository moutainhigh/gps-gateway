package com.zkkj.gps.gateway.ccs.controller;

import com.zkkj.gps.gateway.ccs.config.GetIPAddress;
import com.zkkj.gps.gateway.ccs.dto.amqp.AmqpVerifyDto;
import com.zkkj.gps.gateway.ccs.dto.amqp.RcvMonitorDto;
import com.zkkj.gps.gateway.ccs.dto.common.ResultVo;
import com.zkkj.gps.gateway.ccs.dto.dispatch.DispatchEventDto;
import com.zkkj.gps.gateway.ccs.dto.websocketDto.AlarmInfoSocket;
import com.zkkj.gps.gateway.ccs.mq.MqConstants;
import com.zkkj.gps.gateway.ccs.mq.producer.ProducerSubscribe;
import com.zkkj.gps.gateway.ccs.mq.subscribe.ConsumerSubscribe;
import com.zkkj.gps.gateway.ccs.service.AmqpService;
import com.zkkj.gps.gateway.common.utils.DateTimeUtils;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmTypeEnum.AlarmResTypeEnum;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmTypeEnum.AlarmTypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.UUID;

/**
 * 消息队列相关接口
 *
 * @Auther: zkkjgs
 * @Description:
 * @Date: 2019-06-21 上午 11:45
 */
@RestController
@RequestMapping(value = "/rabbitMQ")
@CrossOrigin
@Api(value = "消息队列相关接口", description = "消息队列相关接口api")
public class AmqpController {
    private Logger logger = LoggerFactory.getLogger(AmqpController.class);

    @Autowired
    private ProducerSubscribe producerSubscribe;

    @Autowired
    private ConsumerSubscribe consumerSubscribe;

    @Autowired
    private AmqpService amqpService;

    @PostMapping(value = "rabbitMQVerify")
    @ApiOperation(value = "根据appKey、appSecret验证RabbitMQ用户信息", notes = "RabbitMQ验证appKey、appSecret")
    public ResultVo<Boolean> rabbitMQVerify(@RequestBody @ApiParam(value = "消息队列初始化模型", required = true) AmqpVerifyDto amqpVerifyDto) {
        ResultVo<Boolean> resultVo = new ResultVo<>();
        resultVo.resultFail("消息注册失败！");
        resultVo.setData(false);
        if (ObjectUtils.isEmpty(amqpVerifyDto)) {
            logger.info("消息订阅者注册参数为空：amqpVerifyDto");
            resultVo.resultFail("参数异常！");
            return resultVo;
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String IP = GetIPAddress.getIpAddr(request);
        if (!IP.equals("59.110.17.54") || !IP.equals("60.205.164.169")) {
            logger.error("注册的IP地址有误---IP:" + IP);
        }
        logger.info("消息订阅者注册参数：【" + amqpVerifyDto.toString() + "】-----IP地址：" + IP);
        try {
            if (!StringUtils.isEmpty(amqpVerifyDto.getAppKey()) &&
                    !StringUtils.isEmpty(amqpVerifyDto.getAppSecret()) &&
                    !StringUtils.isEmpty(amqpVerifyDto.getThirdPartyUrl())) {
                boolean isSubscribe = consumerSubscribe.consemerSubscribe(amqpVerifyDto.getAppKey(), amqpVerifyDto.getAppSecret());
                if (isSubscribe) {//消息队列中存在并注册成功
                    if (MqConstants.appKeyUrlMap != null) {
                        if (!MqConstants.appKeyUrlMap.containsKey(amqpVerifyDto.getAppKey())) {//不包含则为新用户
                            Integer integer = amqpService.addAmqpUser(amqpVerifyDto);
                            if (integer > 0) {
                                logger.info("当前消息订阅者为新用户，数据新增成功");
                                MqConstants.appKeyUrlMap.put(amqpVerifyDto.getAppKey(), amqpVerifyDto);
                            } else {
                                logger.info("当前消息订阅者为新用户，数据新增失败，消息订阅失败");
                                return resultVo;
                            }
                        } else {
                            //更新订阅者
                            Integer updateUser = amqpService.updateAmqpUser(amqpVerifyDto);
                            if (updateUser > 0) {
                                logger.info("当前消息订阅者为老用户,数据更新成功");
                                MqConstants.appKeyUrlMap.put(amqpVerifyDto.getAppKey(), amqpVerifyDto);
                            } else if (updateUser == 0) {//数据库中删除了数据
                                Integer addUser = amqpService.addAmqpUser(amqpVerifyDto);
                                if (addUser > 0) {
                                    logger.info("数据库中删除了该用户,但内存中存在，数据新增成功");
                                    MqConstants.appKeyUrlMap.put(amqpVerifyDto.getAppKey(), amqpVerifyDto);
                                } else {
                                    logger.info("数据库中删除了该用户,但内存中存在，数据新增失败，消息订阅失败");
                                    return resultVo;
                                }
                            }
                            logger.info("当前内存中的消息订阅者存在");
                        }
                        //集合不为空，并且包含当前注册的用户则注册成功
                        if (!CollectionUtils.isEmpty(MqConstants.appKeyUrlMap) && MqConstants.appKeyUrlMap.containsKey(amqpVerifyDto.getAppKey())) {
                            for (Map.Entry<String, AmqpVerifyDto> entry : MqConstants.appKeyUrlMap.entrySet()) {
                                logger.info("内存中缓存的消息订阅用户的信息：appKey：【" + entry.getKey() + "】；thirdPartUrl：【" + entry.getValue().getThirdPartyUrl() + "】；【appSecret：" + entry.getValue().getAppSecret());
                            }
                            resultVo.setMsg("消息注册成功！");
                            resultVo.resultSuccess(isSubscribe);
                        } else {
                            logger.info("缓存消息订阅者的集合为空或缓存集合中不包含当前用户");
                        }
                    } else {
                        logger.info("缓存消息订阅者的集合为空");
                    }
                } else {
                    logger.info("消息订阅者注册失败");
                }
            } else {
                logger.info("消息订阅者注册参数异常");
                resultVo.resultFail("参数异常！");
                return resultVo;
            }
        } catch (Exception e) {
            logger.error("RabbitMQController.rabbitMQVerify is error", e);
            resultVo.resultFail("系统异常:验证RabbitMQ用户信息失败" );
        }
        logger.info("消息注册结果：【" + resultVo.toString() + "】");
        return resultVo;
    }


    @PostMapping(value = "sendAlarmMessage")
    @ApiOperation(value = "发布并推送报警消息", notes = "发布并推送报警消息Api")
    public ResultVo<Boolean> sendAlarmMessage(@RequestParam(value = "appKey") @ApiParam(value = "appKey", required = true) String appKey) {
        ResultVo<Boolean> resultVo = new ResultVo<>();
        resultVo.resultFail("报警消息推送失败！");
        resultVo.setData(false);
        try {
            if (!StringUtils.isEmpty(appKey)) {
                RcvMonitorDto<AlarmInfoSocket> rcvMonitorDto = new RcvMonitorDto<>();
                AlarmInfoSocket alarmInfoSocket = new AlarmInfoSocket();
                alarmInfoSocket.setDispatchNo(UUID.randomUUID().toString());
                alarmInfoSocket.setAlarmValue("12");
                alarmInfoSocket.setConfigValue("10");
                alarmInfoSocket.setCorpName("铜川");
                alarmInfoSocket.setAlarmCreateTime(DateTimeUtils.getCurrentDateTimeStr());
                alarmInfoSocket.setAlarmGroupId(UUID.randomUUID().toString());
                alarmInfoSocket.setAlarmInfo("于【2019-08-17 19:32:38】路线偏移,开始报警");
                alarmInfoSocket.setAlarmResType(AlarmResTypeEnum.start);
                alarmInfoSocket.setAlarmTime(DateTimeUtils.getCurrentDateTimeStr());
                alarmInfoSocket.setTerminalId("中C00001");
                alarmInfoSocket.setRemark("测试");
                alarmInfoSocket.setLongitude(108.4515454);
                alarmInfoSocket.setLatitude(35.454545);
                alarmInfoSocket.setCarNum("中C00001");
                alarmInfoSocket.setAppkey(appKey);
                alarmInfoSocket.setAlarmType(AlarmTypeEnum.VIOLATION_AREA);
                alarmInfoSocket.setIdentity("4968");
                rcvMonitorDto.setData(alarmInfoSocket);
                rcvMonitorDto.setFlag(2);
                boolean produceMessage = producerSubscribe.produceMessage(appKey, rcvMonitorDto);
                if (produceMessage) {
                    resultVo.setMsg("报警消息推送成功！");
                    resultVo.resultSuccess(true);
                }
            }
        } catch (Exception e) {
            logger.error("RabbitMQController.sendMessage is error", e);
            resultVo.resultFail("系统异常:推送报警消息失败");
        }
        return resultVo;
    }

    @PostMapping(value = "sendEventMessage")
    @ApiOperation(value = "发布并推送事件消息", notes = "发布并推送事件消息Api")
    public ResultVo<Boolean> sendEventMessage(@RequestParam(value = "appKey") @ApiParam(value = "appKey", required = true) String appKey,
                                              @RequestParam(value = "status") @ApiParam(value = "运单状态", required = true) Integer status) {
        ResultVo<Boolean> resultVo = new ResultVo<>();
        resultVo.resultFail("事件消息推送失败！");
        resultVo.setData(false);
        try {
            if (!StringUtils.isEmpty(appKey)) {
                if (status > 7 || status < 0) {
                    resultVo.resultFail("运单状态为0-7,请检查");
                    return resultVo;
                }
                RcvMonitorDto<DispatchEventDto> rcvMonitorDto = new RcvMonitorDto<>();
                DispatchEventDto dispatchEventDto = new DispatchEventDto();
                dispatchEventDto.setEventInfo("测试事件消息");
                dispatchEventDto.setEventType(1);
                dispatchEventDto.setAreaId("fjdis");
                dispatchEventDto.setAreaName("铜川电厂");
                dispatchEventDto.setTerminalId("095039631301");
                dispatchEventDto.setAppkey(appKey);
                dispatchEventDto.setCarNum("京A00000");
                dispatchEventDto.setConsignerName("西川一矿");
                dispatchEventDto.setDeductReason("矸大");
                dispatchEventDto.setDeductWeight(1.15);
                dispatchEventDto.setDispatchNo("NO124578");
                dispatchEventDto.setEventCreateTime(DateTimeUtils.getCurrentDateTimeStr());
                dispatchEventDto.setId("113657");
                dispatchEventDto.setIdentity("fhdusiaofhduijsalofh");
                dispatchEventDto.setLatitude(35.456465165);
                dispatchEventDto.setLongitude(100.4512156879);
                dispatchEventDto.setMileage(1000L);
                dispatchEventDto.setRcvGrossWeight(15.23);
                dispatchEventDto.setRcvTareWeight(14.48);
                dispatchEventDto.setSendGrossWeight(15.56);
                dispatchEventDto.setSendTareWeight(15.15);
                dispatchEventDto.setShipperName("擎天柱");
                dispatchEventDto.setStatus(status);
                dispatchEventDto.setCorpName("中矿科技");
                dispatchEventDto.setCreateBy("威震天");
                dispatchEventDto.setRemark("测试一下事件消息推送");
                dispatchEventDto.setReceiverName("铜川照金电厂");
                rcvMonitorDto.setData(dispatchEventDto);
                rcvMonitorDto.setFlag(1);
                boolean produceMessage = producerSubscribe.produceMessage(appKey, rcvMonitorDto);
                if (produceMessage) {
                    resultVo.setMsg("事件消息推送成功！");
                    resultVo.resultSuccess(true);
                }
            }
        } catch (Exception e) {
            logger.error("RabbitMQController.sendMessage is error", e);
            resultVo.resultFail("系统异常:推送事件消息失败");
        }
        return resultVo;
    }

}
