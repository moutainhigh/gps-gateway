package com.zkkj.gps.gateway.gpsobtain.scheduled;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/*
 * @Author lx
 * @Description 定时执行访问中交兴路数据
 * @Date 9:28 2019/10/29
 * @Param
 * @return
 **/
@Component
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class RequestZJXLScheduled {
    //或直接指定时间间隔，例如：30秒（中交兴路返回数据30秒一次）
    @Scheduled(fixedRate = 30000)
    private void configureTasks() {
        /*Thread thread = new MyThread();
        thread.start();
        System.err.println("执行静态定时任务时间: " + LocalDateTime.now());*/
    }

}
