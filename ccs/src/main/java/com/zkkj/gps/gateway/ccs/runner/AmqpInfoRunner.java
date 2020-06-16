package com.zkkj.gps.gateway.ccs.runner;

import com.zkkj.gps.gateway.ccs.dto.amqp.AmqpVerifyDto;
import com.zkkj.gps.gateway.ccs.mq.MqConstants;
import com.zkkj.gps.gateway.ccs.mq.subscribe.ConsumerSubscribe;
import com.zkkj.gps.gateway.ccs.service.AmqpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 应用程序启动后将消息订阅用户从数据库加载到缓存中
 */
@Component
@Order(value = 1)
public class AmqpInfoRunner implements ApplicationRunner {

    private Logger logger = LoggerFactory.getLogger(AmqpInfoRunner.class);

    @Autowired
    private AmqpService amqpService;

    @Autowired
    private ConsumerSubscribe consumerSubscribe;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            List<AmqpVerifyDto> amqpUserLists = amqpService.getAmqpUser();
            if (MqConstants.appKeyUrlMap != null && !CollectionUtils.isEmpty(amqpUserLists)){
                MqConstants.appKeyUrlMap.clear();
                for (AmqpVerifyDto amqpVerifyDto : amqpUserLists){
                    if (ObjectUtils.isEmpty(amqpVerifyDto) || StringUtils.isEmpty(amqpVerifyDto.getAppKey()) ||
                            StringUtils.isEmpty(amqpVerifyDto.getAppSecret()) || StringUtils.isEmpty(amqpVerifyDto.getThirdPartyUrl())){
                        continue;
                    }
                    MqConstants.appKeyUrlMap.put(amqpVerifyDto.getAppKey(),amqpVerifyDto);
                }
                if (!CollectionUtils.isEmpty(MqConstants.appKeyUrlMap )){
                    registerAmqpUser();
                } else {
                    logger.info("消息订阅者注册用户列表为空，原因是可能出现异常");
                }
            } else {
                if (CollectionUtils.isEmpty(MqConstants.appKeyUrlMap))
                    logger.info("消息订阅者注册用户列表为空");
                if (CollectionUtils.isEmpty(amqpUserLists))
                    logger.info("数据库中存储的消息订阅者用户为空");
            }
        } catch (Exception e){
            logger.error("AmqpInfoRunner.run is error",e);
        }
    }

    /**
     * 注册消息订阅者
     */
    private void registerAmqpUser(){
        for (Map.Entry<String,AmqpVerifyDto> entry : MqConstants.appKeyUrlMap.entrySet()){
            try {
                if (entry == null) continue;
                String appKey = entry.getValue().getAppKey();//消息订阅者用户名
                String appSecret = entry.getValue().getAppSecret();//消息订阅者用户密码
                String thirdPartUrl = entry.getValue().getThirdPartyUrl();//消息订阅者接收消息根目录URL
                boolean isSubscribe = consumerSubscribe.consemerSubscribe(appKey, appSecret);
                logger.info("应用程序启动自动注册消息订阅者：appKey：【" + appKey + "】；appSecret：【" + appSecret + "】；thirdPartUrl：【" + thirdPartUrl + "】；isSubscribe：【" + isSubscribe + "】");
            } catch (Exception e){
                logger.error("应用程序启动注册消息订阅者出现异常",e);
            }
        }
    }

}
