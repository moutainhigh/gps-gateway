package com.zkkj.gps.gateway.ccs.config;

import com.zkkj.gps.gateway.ccs.scheduled.AlarmConfigScheduleTask;
import com.zkkj.gps.gateway.ccs.scheduled.DisconnectScheduleTask;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.concurrent.Executors;

@Configuration
public class ScheduleConfig implements SchedulingConfigurer {

    private Logger logger = LoggerFactory.getLogger(ScheduleConfig.class);

    @Value("${disconnect.time}")
    private String disconnectTime;

    @Value("${clear.alarmConfig.time}")
    private String clearAlarmConfigTime;

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

    //掉线定时任务
    @Bean(name = "disconnectDetail")
    public MethodInvokingJobDetailFactoryBean disconnectDetail(DisconnectScheduleTask disconnectScheduleTask) {
        MethodInvokingJobDetailFactoryBean bean = new MethodInvokingJobDetailFactoryBean();
        bean.setTargetObject(disconnectScheduleTask);
        bean.setTargetMethod("disconnectScheduleRun");
        bean.setName("disconnectScheduler");// 设置任务的名字
        bean.setGroup("disconnectScheduler_group");// 设置任务的分组，这些属性都可以存储在数据库中，在多任务的时候使用
        bean.setConcurrent(true);
        return bean;
    }

    @Bean(name = "disconnectTrigger")
    public CronTriggerFactoryBean disconnectTrigger(JobDetail disconnectDetail) {
        CronTriggerFactoryBean disconnectTrigger = new CronTriggerFactoryBean();
        disconnectTrigger.setJobDetail(disconnectDetail);
        try {
            disconnectTrigger.setCronExpression(disconnectTime);
        } catch (Exception e) {
            logger.error("定时任务时间表达式有误", e);
        }
        return disconnectTrigger;
    }


    //清空报警配置缓存定时任务
    @Bean(name = "clearAlarmConfigDetail")
    public MethodInvokingJobDetailFactoryBean clearAlarmConfigDetail(AlarmConfigScheduleTask alarmConfigScheduleTask) {
        MethodInvokingJobDetailFactoryBean bean = new MethodInvokingJobDetailFactoryBean();
        bean.setTargetObject(alarmConfigScheduleTask);
        bean.setTargetMethod("clearAlarmConfigScheduleRun");
        bean.setName("clearAlarmConfigScheduler");// 设置任务的名字
        bean.setGroup("clearAlarmConfigScheduler_group");// 设置任务的分组，这些属性都可以存储在数据库中，在多任务的时候使用
        bean.setConcurrent(true);
        return bean;
    }

    @Bean(name = "clearAlarmConfigTrigger")
    public CronTriggerFactoryBean clearAlarmConfigTrigger(JobDetail clearAlarmConfigDetail) {
        CronTriggerFactoryBean disconnectTrigger = new CronTriggerFactoryBean();
        disconnectTrigger.setJobDetail(clearAlarmConfigDetail);
        try {
            disconnectTrigger.setCronExpression(clearAlarmConfigTime);
        } catch (Exception e) {
            logger.error("定时任务时间表达式有误", e);
        }
        return disconnectTrigger;
    }

    @Bean(name = "scheduler")
    public SchedulerFactoryBean schedulerFactory(Trigger disconnectTrigger, Trigger clearAlarmConfigTrigger) {
        SchedulerFactoryBean bean = new SchedulerFactoryBean();
        //设置是否任意一个已定义的Job会覆盖现在的Job。默认为false，即已定义的Job不会覆盖现有的Job。
        bean.setOverwriteExistingJobs(true);
        // 延时启动，应用启动5秒后  ，定时器才开始启动
        bean.setStartupDelay(5);
        // 注册定时触发器
        bean.setTriggers(disconnectTrigger, clearAlarmConfigTrigger);
        return bean;
    }

}
