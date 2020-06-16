package com.zkkj.gps.gateway.ccs.scheduled;

import com.zkkj.gps.gateway.terminal.monitor.algorithm.DisconnectTerminalAlarmAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

/**
 * author : cyc
 * Date : 2019-05-27
 * 调信
 */
@Configuration
@Component
@EnableScheduling // 该注解必须要加
public class DisconnectScheduleTask {

    private Logger logger = LoggerFactory.getLogger(DisconnectScheduleTask.class);

    /**
     * 定时任务方法
     */
    public void disconnectScheduleRun() {
        try {
            DisconnectTerminalAlarmAlgorithm.pushDisconnectAlarm();
        } catch (Exception e) {
            logger.error("DisconnectScheduleTask.disconnectScheduleRun is error", e);
        }

    }
}
