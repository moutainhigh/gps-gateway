package com.zkkj.gps.gateway.ccs.mqdemo.producer;//package com.zkkj.gps.gateway.ccs.mqdemo.producer;
//
//import com.alibaba.fastjson.JSONObject;
//import com.rabbitmq.client.AMQP;
//import com.rabbitmq.client.BuiltinExchangeType;
//import com.rabbitmq.client.Channel;
//import com.rabbitmq.client.Connection;
//import com.zkkj.gps.gateway.ccs.mqdemo.MqConstantsTest;
//import com.zkkj.gps.gateway.ccs.mqdemo.conn.ChannelFactoryTest;
//import com.zkkj.gps.gateway.ccs.mqdemo.entity.MsgMonitorDto;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.util.ObjectUtils;
//
//import java.util.HashMap;
//
///**
// * 消息发布者订阅
// * @Auther: zkkjgs
// * @Description:
// * @Date: 2019-06-21 下午 12:11
// */
//@Component
//public class ProducerSubscribeTest {
//
//    /**
//     * 日志对象
//     */
//    private Logger logger = LoggerFactory.getLogger(ProducerSubscribeTest.class);
//
//    @Autowired
//    private ChannelFactoryTest factory;
//
//    @Value("${spring.rabbitmq.username}")
//    private String amqp_username;
//    @Value("${spring.rabbitmq.password}")
//    private String amqp_password;
//
//    /**
//     * 消息发布
//     * @param appKey
//     * @param msgMonitorDto
//     * @return
//     */
//    public boolean produceMessageTest(String appKey,MsgMonitorDto msgMonitorDto){
//        try {
//            if (StringUtils.isEmpty(appKey) || ObjectUtils.isEmpty(msgMonitorDto) || ObjectUtils.isEmpty(msgMonitorDto.getData())){
//                logger.info("消息发布参数异常：appKey：【" + appKey + "】；rcvMonitorDto：【" + msgMonitorDto.toString() + "】 \n");
//                return false;
//            }
//            logger.info("消息发布参数：appKey：【" + appKey + "】；rcvMonitorDto：【" + msgMonitorDto.toString() + "】 \n");
//            msgMonitorDto.setAppKey(appKey);
//            String msgContent = JSONObject.toJSONString(msgMonitorDto);
//            Connection connection = factory.getConnection("MQProducer",amqp_username,amqp_password);
//            Channel channel = connection.createChannel();
//            String routingKey = appKey;
//            // 声明交换机 (交换机名, 交换机类型, 是否持久化, 是否自动删除, 是否是内部交换机, 交换机属性);
//            channel.exchangeDeclare(MqConstantsTest.EXCHANGE_NAME, BuiltinExchangeType.DIRECT, true, false, false, new HashMap<>());
//            // 创建一个持久化，非排他，非自动删除的队列
//            channel.queueDeclare(appKey, true, false, false, null);
//            //交换器与队列通过路由键绑定
//            channel.queueBind(appKey, MqConstantsTest.EXCHANGE_NAME,routingKey);
//            /**
//             * #contentHeader<basic>(content-type=UTF-8, content-encoding=null, headers=null, delivery-mode=2, priority=null, correlation-id=null,
//             * reply-to=null, expiration=null, message-id=null,  timestamp=null, type=null, user-id=null, app-id=null, cluster-id=null)
//             */
//            AMQP.BasicProperties basicProperties = new AMQP.BasicProperties()
//                    .builder()
//                    .deliveryMode(2)
//                    .priority(1)
//                    .contentType("text/plain")
//                    .contentEncoding("UTF-8")
//                    .build();
//            // 设置消息属性 发布消息 (交换机名, Routing key, 可靠消息相关属性 后续会介绍, 消息属性, 消息体);
//            channel.basicPublish(MqConstantsTest.EXCHANGE_NAME, routingKey, false, basicProperties, (msgContent).getBytes("UTF-8"));
//            // 关闭频道和连接
//            channel.close();
//            connection.close();
//            return true;
//        } catch (Exception e){
//            logger.error("消息发布异常：",e);
//        }
//        return false;
//    }
//
//}
