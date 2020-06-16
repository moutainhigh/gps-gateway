package com.zkkj.gps.gateway.ccs.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import com.zkkj.gps.gateway.ccs.enume.RedisChannelEnum;
import com.zkkj.gps.gateway.ccs.listener.RedisReceiverListener;

/**
 * author : cyc
 * Date : 2020/3/19
 * redis监听配置类
 */
@Configuration
public class RedisSubListenerConfig {

    @Autowired
    private Jackson2JsonRedisSerializer serializer;


    /**
     * 消息监听器容器
     *
     * @param connectionFactory
     * @param listenerAdapter
     * @return
     */
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        // 可以添加多个 messageListener，配置不同的交换机
        container.addMessageListener(listenerAdapter, new PatternTopic(RedisChannelEnum.SYSLOG.name()));
        return container;
    }

    /**
     * 消息监听器适配器，绑定消息处理器，利用反射技术调用消息处理器的业务方法
     *
     * @param redisReceiverListener
     * @return
     */
    @Bean
    MessageListenerAdapter listenerAdapter(RedisReceiverListener redisReceiverListener) {
        MessageListenerAdapter receiveListenerMessage = new MessageListenerAdapter(redisReceiverListener, "receiveListenerMessage");
        //消息序列化，使用原生的序列化，订阅到的消息对象是json数组
        receiveListenerMessage.setSerializer(serializer);
        return receiveListenerMessage;
    }

}
