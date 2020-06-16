package com.zkkj.gps.gateway.ccs.mqdemo.subscribe;//package com.zkkj.gps.gateway.ccs.mqdemo.subscribe;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.alibaba.fastjson.TypeReference;
//import com.rabbitmq.client.*;
//import com.zkkj.gps.gateway.ccs.entity.amqp.AmqpVerifyDto;
//import com.zkkj.gps.gateway.ccs.entity.amqp.PushMsgDto;
//import com.zkkj.gps.gateway.ccs.entity.basebean.ResultVo;
//import com.zkkj.gps.gateway.ccs.mqdemo.MqConstantsTest;
//import com.zkkj.gps.gateway.ccs.mqdemo.conn.ChannelFactoryTest;
//import com.zkkj.gps.gateway.ccs.mqdemo.entity.MsgMonitorDto;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.converter.StringHttpMessageConverter;
//import org.springframework.stereotype.Component;
//import org.springframework.util.ObjectUtils;
//import org.springframework.util.StringUtils;
//import org.springframework.util.concurrent.ListenableFuture;
//import org.springframework.util.concurrent.ListenableFutureCallback;
//import org.springframework.web.client.AsyncRestTemplate;
//
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.util.HashMap;
//
///**
// * 消息订阅者订阅
// * @Auther: zkkjgs
// * @Description:
// * @Date: 2019-06-21 上午 11:52
// */
//@Component
//public class ConsumerSubscribeTest {
//
//    private Logger logger = LoggerFactory.getLogger(ConsumerSubscribeTest.class);
//
//    @Autowired
//    private ChannelFactoryTest factoryTest;
//
//    @Autowired
//    private AsyncRestTemplate restTemplate;
//
//    /**
//     * 订阅者订阅并从消息队列取消息
//     * @param amqpUserName
//     * @param amqpPassWord
//     * @throws Exception
//     */
//    public boolean consemerSubscribeTest(String amqpUserName,String amqpPassWord){
//        try {
//            logger.info("消息队列消费者：amqpUserName：【" + amqpUserName + "】；amqpPassWord：" + amqpPassWord + "】 \n");
//            Connection connection = factoryTest.getConnection("MQSubscriber", amqpUserName, amqpPassWord);
//            Channel channel = connection.createChannel();
//            // 声明队列 (队列名, 是否持久化, 是否排他, 是否自动删除, 队列属性);
//            AMQP.Queue.DeclareOk declareOk = channel.queueDeclare(amqpUserName, true, false, false, new HashMap<>());
//            // 声明交换机 (交换机名, 交换机类型, 是否持久化, 是否自动删除, 是否是内部交换机, 交换机属性);
//            channel.exchangeDeclare(MqConstantsTest.EXCHANGE_NAME, BuiltinExchangeType.DIRECT, true, false, false, new HashMap<>());
//            // Routing key
//            String routingKey = amqpUserName;
//            // 将队列Binding到交换机上 (队列名, 交换机名, Routing key, 绑定属性);
//            channel.queueBind(declareOk.getQueue(), MqConstantsTest.EXCHANGE_NAME, routingKey, new HashMap<>());
//            // 设置限流策略
//            // channel.basicQos(获取消息最大数[0-无限制], 依次获取数量, 作用域[true作用于整个channel，false作用于具体消费者]);
//            // channel.basicQos(0, 10, true);
//             channel.basicQos(1);
//            // 消费者订阅消息 监听如上声明的队列 (队列名, 是否自动应答(与消息可靠有关 后续会介绍), 消费者标签, 消费者)
//            DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
//                @Override
//                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
//                    try {
//                        if (body == null || body.length == 0) {
//                            logger.info("消息订阅者接收转换对象字符串为空：body");
//                            return;
//                        }
//                        logger.info("消息订阅者接收到的消息：【" +  new String(body,"UTF-8") + "】");
//                        String rcvMsgContent = new String(body,"UTF-8");
//                        if (!StringUtils.isEmpty(rcvMsgContent)){
//                            MsgMonitorDto msgMonitorDto = JSONObject.parseObject(rcvMsgContent,MsgMonitorDto.class);
//                            //RcvMonitorDto rcvMonitorDto = JSONObject.parseObject(rcvMsgContent,RcvMonitorDto.class);
//                            if (!ObjectUtils.isEmpty(msgMonitorDto)){
//                                logger.info("消息推送对象：rcvMonitorDto【" + msgMonitorDto.toString() + "】");
//                                String url = "";
//                                switch (msgMonitorDto.getFlag()){
//                                    case 1://事件
//                                        url = MqConstantsTest.consumerEventMsg;
//                                        break;
//                                    case 2://报警
//                                        url = MqConstantsTest.consumerAlarmMsg;
//                                        break;
//                                }
//                                if (!StringUtils.isEmpty(url)){
//                                    sendMsgToThirdParty(msgMonitorDto,url,channel,envelope);
//                                } else {
//                                    logger.info("消息推送第三方地址为空：url" + "\n");
//                                }
//                            } else {
//                                logger.info("消息推送转换对象为空：rcvMonitorDto" + "\n");
//                            }
//                        } else {
//                            logger.info("消息推送转换对象为空：rcvMsgContent" + "\n");
//                        }
//                    } catch (Exception e){
//                        logger.info("消息推送消息接收出现异常：" + e.toString() + "\n");
//                    }
//                }
//            };
//            boolean autoAck = false;
//            channel.basicConsume(declareOk.getQueue(), autoAck, "ReceiveDirect",defaultConsumer);
//            return true;
//        } catch (Exception e) {
//            logger.error("消息推送异常：",e);
//        }
//        return false;
//    }
//
//    /**
//     * 调用第三方消息接收接口发送消息
//     * @param msgMonitorDto
//     * @param implName
//     */
//    private void sendMsgToThirdParty(MsgMonitorDto msgMonitorDto,String implName,Channel channel,Envelope envelope){
//        String appKey = msgMonitorDto.getAppKey();
//        String appSecret = null;
//        String thirdPartyUrl = null;
//        if (!StringUtils.isEmpty(appKey)){
//            if (MqConstantsTest.appKeyUrlMap != null && MqConstantsTest.appKeyUrlMap.size() > 0){
//                AmqpVerifyDto amqpVerifyDto = MqConstantsTest.appKeyUrlMap.get(appKey);
//                if (!ObjectUtils.isEmpty(amqpVerifyDto)){
//                    appSecret = amqpVerifyDto.getAppSecret();
//                    thirdPartyUrl = amqpVerifyDto.getThirdPartyUrl();
//                } else {
//                    logger.info("消息推送内存集合为空：amqpVerifyDto\n");
//                }
//            } else {
//                logger.info("消息推送数据为空：MqConstantsTest.appKeyUrlMap\n");
//            }
//        } else {
//            logger.info("消息推送appKey为空：appKey\n");
//        }
//        if (ObjectUtils.isEmpty(msgMonitorDto.getData()) || StringUtils.isEmpty(appKey) ||
//                StringUtils.isEmpty(appSecret) || StringUtils.isEmpty(thirdPartyUrl)){
//            logger.info("消息推送相关数据为空：【" + msgMonitorDto.getData() + "；"+ appKey + "；" +appSecret + "；" + thirdPartyUrl + "】\n");
//            return;
//        }
//        PushMsgDto pushMsgDto = new PushMsgDto();
//        pushMsgDto.setAppKey(appKey);
//        pushMsgDto.setAppSecret(appSecret);
//        pushMsgDto.setData(msgMonitorDto.getData());
//        logger.info("推送消息对象字符串：【" + pushMsgDto.toString() + "】\n");
//        HttpHeaders headers = new HttpHeaders();//header参数
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<Object> entity = new HttpEntity<>(pushMsgDto,headers);
//        String urlRemote = thirdPartyUrl + "/" + implName;
//        logger.info("推送消息第三方接口地址：【" + urlRemote + "】\n");
//        //ResponseEntity<String> response = restTemplate.exchange(thirdPartyUrl + "/" + implName, HttpMethod.POST, entity, String.class);
//        restTemplate.getMessageConverters().set(1,new StringHttpMessageConverter(StandardCharsets.UTF_8));
//        ListenableFuture<ResponseEntity<String>> forEntity = restTemplate.postForEntity(urlRemote, entity, String.class);
//        forEntity.addCallback(new ListenableFutureCallback<ResponseEntity<String>>() {
//            @Override
//            public void onFailure(Throwable ex) {
//                logger.error(String.format("POST远程消息推送回调 %s 参数为 %s 请求失败，%s" + "\n", urlRemote, JSON.toJSONString(pushMsgDto), ex.toString()));
//                try {
//                    // 消息消费成功，消息消费者通知消息服务器删除消息
//                    //channel.basicAck(envelope.getDeliveryTag(), false);
//                    //true 表示将消息重新放入到队列中，false：表示直接从队列中删除，此时和basicAck(long deliveryTag, false)的效果一样
//                    channel.basicReject(envelope.getDeliveryTag(), true);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    logger.info("消息推送请求第三方接口响应失败，但消费者告诉消息服务器删除消息时出现异常：【" + e.toString() + "】 \n");
//                }
//            }
//
//            @Override
//            public void onSuccess(ResponseEntity<String> response) {
//                try {
//                    if (!ObjectUtils.isEmpty(response) && response.getStatusCodeValue() == 200){
//                        String str = response.getBody();
//                        ResultVo<Boolean> resultVo = JSONObject.parseObject(str,new TypeReference<ResultVo<Boolean>>(){});
//                        if (!ObjectUtils.isEmpty(resultVo)){
//                            logger.info("向第三方发送消息返回数据：【" + resultVo.toString( ) + "】\n");
//                        } else {
//                            logger.info("消息推送响应结果为空：resultVo\n");
//                        }
//                    } else {
//                        logger.info("消息推送响应异常\n");
//                    }
//                    // 消息消费成功，消息消费者通知消息服务器删除消息
//                    channel.basicAck(envelope.getDeliveryTag(), false);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    logger.info("消息推送请求第三方接口响应成功，但消费者告诉消息服务器删除消息时出现异常：【" + e.toString() + "】 \n");
//                }
//                logger.info(String.format("POST远程消息推送回调 %s 参数为 %s 请求成功，返回值为 %s" + "\n", urlRemote, JSON.toJSONString(pushMsgDto), JSON.toJSONString(response.getBody())));
//            }
//        });
//
//    }
//
//}
