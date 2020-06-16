package com.zkkj.gps.gateway.automation.scheduled;

import com.zkkj.gps.gateway.automation.service.TerminalOptionsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 设置终端参数定时任务类
 * @author suibozhuliu
 */
@Slf4j
@Configuration
@Component
@EnableScheduling // 该注解必须要加
public class TerminalOptionsScheduleTask {

    private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);

    @Autowired
    private TerminalOptionsService terminalOptionsService;

    /**
     * 设置终端IP和端口定时方法
     */
    public void setTerminalArgsScheduleRun() {

    }
}
