package com.zkkj.gps.gateway.automation.config;

import com.zkkj.gps.gateway.automation.scheduled.TerminalOptionsScheduleTask;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;

import java.util.concurrent.Executors;

@Slf4j
//@Configuration
public class ScheduleConfig implements SchedulingConfigurer {

    @Value("${set.terminal.args.time}")
    private String setTerminalArgsTime;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(Executors.newScheduledThreadPool(10));
    }

    // 配置中设定了
    // ① targetMethod: 指定需要定时执行DisconnectScheduleTask中的disconnectScheduleTask()方法
    // ② concurrent：对于相同的JobDetail，当指定多个Trigger时, 很可能第一个job完成之前，
    // 第二个job就开始了。指定concurrent设为false，多个job不会并发运行，第二个job将不会在第一个job完成之前开始。
    // ③ cronExpression：0/10 * * * * ?表示每10秒执行一次，具体可参考附表。
    // ④ triggers：通过再添加其他的ref元素可在list中放置多个触发器。 scheduleInfoAction中的simpleJobTest()方法
    // 设置终端IP和端口定时任务
    @Bean(name = "setTerminalArgs")
    public MethodInvokingJobDetailFactoryBean setTerminalArgsTrigger(TerminalOptionsScheduleTask terminalOptionsScheduleTask) {
        MethodInvokingJobDetailFactoryBean bean = new MethodInvokingJobDetailFactoryBean();
        bean.setTargetObject(terminalOptionsScheduleTask);
        bean.setTargetMethod("setTerminalArgsScheduleRun");
        // 设置任务名称
        bean.setName("setTerminalArgsScheduler");
        // 设置任务分组
        bean.setGroup("setTerminalArgsScheduler_group");
        bean.setConcurrent(true);
        return bean;
    }

    @Bean(name = "setTerminalArgsTrigger")
    public CronTriggerFactoryBean setTerminalArgsTrigger(JobDetail setTerminalArgsTrigger) {
        CronTriggerFactoryBean disconnectTrigger = new CronTriggerFactoryBean();
        disconnectTrigger.setJobDetail(setTerminalArgsTrigger);
        try {
            disconnectTrigger.setCronExpression(setTerminalArgsTime);
        } catch (Exception e) {
            log.error("定时任务异常：【" + e + "】");
        }
        return disconnectTrigger;
    }

}
