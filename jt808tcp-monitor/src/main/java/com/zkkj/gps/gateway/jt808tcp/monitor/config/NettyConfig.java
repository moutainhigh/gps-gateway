package com.zkkj.gps.gateway.jt808tcp.monitor.config;

import com.zkkj.gps.gateway.jt808tcp.monitor.mapping.HandlerMapper;
import com.zkkj.gps.gateway.jt808tcp.monitor.server.tcp.TCPServer;
import com.zkkj.gps.gateway.jt808tcp.monitor.spring.SpringHandlerMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Netty配置
 * @author suibozhuliu
 */
@Configuration
@Slf4j
public class NettyConfig implements ApplicationListener<ContextRefreshedEvent> {

    /**单线程化线程池，串行执行任务*/
    public static ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();

    @Autowired
    private TCPServer tcpServer;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        singleThreadPool.execute(() -> initTcpService());
    }

    private void initTcpService(){
        /**以0x7e分隔，避免粘包和半包出现解析问题*/
        tcpServer.setParames((byte) 0x7e, handlerMapper());
        tcpServer.startService();
    }

    @Bean
    public HandlerMapper handlerMapper() {
        return new SpringHandlerMapper("com.zkkj.gps.gateway.jt808tcp.monitor.endpoint");
    }
}