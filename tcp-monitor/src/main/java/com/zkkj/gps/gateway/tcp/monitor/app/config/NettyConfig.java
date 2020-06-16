package com.zkkj.gps.gateway.tcp.monitor.app.config;

import com.zkkj.gps.gateway.tcp.monitor.socket.server.GPSSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zkkjgs
 * @date 2019-06-10
 */
@Configuration
public class NettyConfig implements ApplicationListener<ContextRefreshedEvent> {

    /**单线程化线程池，串行执行任务*/
    public static ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();

    @Autowired
    private GPSSocketService tcpServer;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        singleThreadPool.execute(() -> tcpServer.initTcpService());
    }

}