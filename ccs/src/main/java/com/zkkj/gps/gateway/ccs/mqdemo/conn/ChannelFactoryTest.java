package com.zkkj.gps.gateway.ccs.mqdemo.conn;//package com.zkkj.gps.gateway.ccs.mqdemo.conn;
//
//import com.rabbitmq.client.Connection;
//import com.rabbitmq.client.ConnectionFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
///**
// * 信道创建工厂
// * @Auther: zkkjgs
// * @Description:
// * @Date: 2019-06-21 下午 2:36
// */
//@Component
//public class ChannelFactoryTest {
//
//    @Value("${spring.rabbitmq.host}")
//    private String amqp_host;
//    @Value("${spring.rabbitmq.port}")
//    private int amqp_port;
//    @Value("${rabitmq_virtualHost}")
//    private String amqp_virtualhost;
//
//    /**
//     * 获取链接
//     * @param connectionDesc
//     * @param amqpUserName
//     * @param amqpPassWord
//     * @return
//     */
//    public Connection getConnection(String connectionDesc,String amqpUserName,String amqpPassWord){
//        try {
//            ConnectionFactory connectionFactory = getConnectionFactory(amqpUserName,amqpPassWord);
//            Connection connection = connectionFactory.newConnection(connectionDesc);
//            return connection;
//        } catch (Exception e){
//            throw new RuntimeException("获取Channel连接失败");
//        }
//    }
//
//    private ConnectionFactory getConnectionFactory(String amqpUserName,String amqpPassWord) {
//        ConnectionFactory connectionFactory = new ConnectionFactory();
//
//        // 配置连接信息
//        connectionFactory.setHost(amqp_host);
//        connectionFactory.setPort(amqp_port);
//        connectionFactory.setVirtualHost(amqp_virtualhost);
//        connectionFactory.setUsername(amqpUserName);
//        connectionFactory.setPassword(amqpPassWord);
//        // 网络异常自动连接恢复
//        connectionFactory.setAutomaticRecoveryEnabled(true);
//        // 每10秒尝试重试连接一次
//        connectionFactory.setNetworkRecoveryInterval(10000);
//        return connectionFactory;
//    }
//
//}
