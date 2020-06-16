package com.zkkj.gps.gateway.gpsZjxl.config;

import com.zkkj.gps.gateway.gpsZjxl.scheduled.FgmgzPositionScheduleTask;
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

    @Value("${fgmgz.zjxl.position.time}")
    private String fgmgzZjxlPositionTime;


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

    //针对府谷煤管站从中交信路获取定位信息定时任务
    @Bean(name = "getPositionFromZjxlDetail")
    public MethodInvokingJobDetailFactoryBean getPositionFromZjxlDetail(FgmgzPositionScheduleTask fgmgzPositionScheduleTask) {
        MethodInvokingJobDetailFactoryBean bean = new MethodInvokingJobDetailFactoryBean();
        bean.setTargetObject(fgmgzPositionScheduleTask);
        bean.setTargetMethod("fgmgzPositionScheduleRun");
        bean.setGroup("fgmgzPositionScheduler_group");// 设置任务的分组，这些属性都可以存储在数据库中，在多任务的时候使用
        bean.setName("fgmgzPositionScheduler");// 设置任务的名字
        bean.setConcurrent(true);
        return bean;
    }

    @Bean(name = "getPositionFromZjxlTrigger")
    public CronTriggerFactoryBean getPositionFromZjxlTrigger(JobDetail getPositionFromZjxlDetail) {
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        cronTriggerFactoryBean.setJobDetail(getPositionFromZjxlDetail);
        try {
            cronTriggerFactoryBean.setCronExpression(fgmgzZjxlPositionTime);
        } catch (Exception e) {
            logger.error("定时任务时间表达式有误", e);
        }
        return cronTriggerFactoryBean;
    }


    @Bean(name = "scheduler")
    public SchedulerFactoryBean schedulerFactory(Trigger getPositionFromZjxlTrigger) {
        SchedulerFactoryBean bean = new SchedulerFactoryBean();
        //设置是否任意一个已定义的Job会覆盖现在的Job。默认为false，即已定义的Job不会覆盖现有的Job。
        bean.setOverwriteExistingJobs(true);
        // 延时启动，应用启动5秒后  ，定时器才开始启动
        bean.setStartupDelay(5);
        // 注册定时触发器
        bean.setTriggers(getPositionFromZjxlTrigger);
        return bean;
    }

}
