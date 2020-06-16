package com.zkkj.gps.gateway.ccs.mq.producer;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.zkkj.gps.gateway.ccs.dto.amqp.AmqpVerifyDto;
import com.zkkj.gps.gateway.ccs.dto.amqp.RcvMonitorDto;
import com.zkkj.gps.gateway.ccs.mq.MqConstants;
import com.zkkj.gps.gateway.ccs.mq.conn.ChannelFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * 消息发布者订阅
 * @Auther: zkkjgs
 * @Description:
 * @Date: 2019-06-21 下午 12:11
 */
@Component
public class ProducerSubscribe {

    /**
     * 日志对象
     */
    private Logger logger = LoggerFactory.getLogger(ProducerSubscribe.class);

    @Autowired
    private ChannelFactory factory;

    @Value("${spring.rabbitmq.username}")
    private String amqp_username;
    @Value("${spring.rabbitmq.password}")
    private String amqp_password;

    /**
     * 消息发布
     * @param appKey
     * @param rcvMonitorDto
     * @return
     */
    public boolean produceMessage(String appKey,RcvMonitorDto rcvMonitorDto){
        try {
            if (StringUtils.isEmpty(appKey) || ObjectUtils.isEmpty(rcvMonitorDto)){
                logger.info("消息发布参数异常：appKey：【" + appKey + "】；rcvMonitorDto：【" + rcvMonitorDto + "】 \n");
                return false;
            }
            logger.info("消息发布参数：appKey：【" + appKey + "】；rcvMonitorDto：【" + rcvMonitorDto.toString() + "】 \n");
            String appSecret = null;
            String thirdPartyUrl = null;
            if (MqConstants.appKeyUrlMap != null && MqConstants.appKeyUrlMap.size() > 0){
                AmqpVerifyDto amqpVerifyDto = MqConstants.appKeyUrlMap.get(appKey);
                if (ObjectUtils.isEmpty(amqpVerifyDto)){
                    logger.info("消息发布数据异常，内存中存储的键为" + appKey + "的订阅者参数为空 \n");
                    return false;
                }
                appSecret = amqpVerifyDto.getAppSecret();
                thirdPartyUrl = amqpVerifyDto.getThirdPartyUrl();
            } else {
                logger.info("消息发布数据异常，内存中存储的订阅者注册参数集合为空 \n");
            }
            if (StringUtils.isEmpty(appSecret) || StringUtils.isEmpty(thirdPartyUrl)){
                logger.info("消息发布数据异常，内存中存储的键为" + appKey +
                        "的订阅者的appSecret或thirdPartyUrl为空：appSecret【" + appSecret + "】；thirdPartyUrl【" + thirdPartyUrl + "】 \n");
                return false;
            }
            rcvMonitorDto.setAppKey(appKey);
            rcvMonitorDto.setAppSecret(appSecret);
            String msgContent = JSONObject.toJSONString(rcvMonitorDto);
            Connection connection = factory.getConnection("MQProducer",amqp_username,amqp_password);
            Channel channel = connection.createChannel();
            String routingKey = appKey;
            // 声明交换机 (交换机名, 交换机类型, 是否持久化, 是否自动删除, 是否是内部交换机, 交换机属性);
            channel.exchangeDeclare(MqConstants.EXCHANGE_NAME, BuiltinExchangeType.DIRECT, true, false, false, new HashMap<>());
            // 创建一个持久化，非排他，非自动删除的队列
            channel.queueDeclare(appKey, true, false, false, null);
            //交换器与队列通过路由键绑定
            channel.queueBind(appKey,MqConstants.EXCHANGE_NAME,routingKey);
            /**
             * #contentHeader<basic>(content-type=UTF-8, content-encoding=null, headers=null, delivery-mode=2, priority=null, correlation-id=null,
             * reply-to=null, expiration=null, message-id=null,  timestamp=null, type=null, user-id=null, app-id=null, cluster-id=null)
             */
            AMQP.BasicProperties basicProperties = new AMQP.BasicProperties()
                    .builder()
                    .deliveryMode(2)
                    .priority(1)
                    .contentType("text/plain")
                    .contentEncoding("UTF-8")
                    .build();
            // 设置消息属性 发布消息 (交换机名, Routing key, 可靠消息相关属性 后续会介绍, 消息属性, 消息体);
            channel.basicPublish(MqConstants.EXCHANGE_NAME, routingKey, false, basicProperties, (msgContent).getBytes("UTF-8"));
            // 关闭频道和连接
            channel.close();
            connection.close();
            return true;
        } catch (Exception e){
            logger.error("消息发布异常：",e);
        }
        return false;
    }

}
