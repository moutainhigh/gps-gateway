package com.zkkj.gps.gateway.ccs.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Arrays;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * author : cyc
 * Date : 2020/3/20
 */

@Configuration
public class AsyncConfig implements AsyncConfigurer {

    
    private Logger logger = LoggerFactory.getLogger(AsyncConfig.class);

    /**
     * 针对异步任务创建线程池
     *
     * @return
     */
    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);        // 设置核心线程数
        executor.setMaxPoolSize(20);        // 设置最大线程数
        executor.setQueueCapacity(200);      // 设置队列容量
        executor.setKeepAliveSeconds(60);   // 设置线程活跃时间（秒）
        executor.setThreadNamePrefix("async-task-");  // 设置默认线程名称
        // 设置拒绝策略，线程池的默认拒绝策略，丢弃任务并抛出RejectedExecutionException异常。
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true); // 等待所有任务结束后再关闭线程池
        return executor;
    }

    /**
     * 自定义异步任务异常处理
     *
     * @return
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, objects) -> logger.error("method:" + method.getName() + "====objects:" + Arrays.toString(objects) + "====;throwable" + throwable.getMessage());
    }

}
