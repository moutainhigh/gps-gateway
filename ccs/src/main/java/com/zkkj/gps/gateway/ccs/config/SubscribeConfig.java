package com.zkkj.gps.gateway.ccs.config;

import com.zkkj.gps.gateway.terminal.monitor.mrsubscribe.gpssubscribe.GpsFilterInfoAlarmSubscribe;
import com.zkkj.gps.gateway.terminal.monitor.mrsubscribe.gpssubscribe.GpsListInfoAlarmSubscribe;
import com.zkkj.gps.gateway.terminal.monitor.mrsubscribe.gpssubscribe.GpsSpecialListInfoAlarmSubscribe;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * author : cyc
 * Date : 2019/9/10
 * 订阅对象配置类
 */

@Configuration
public class SubscribeConfig {

    @Bean
    public GpsFilterInfoAlarmSubscribe gpsFilterInfoAlarmSubscribe() {
        return new GpsFilterInfoAlarmSubscribe();
    }

    @Bean
    public GpsListInfoAlarmSubscribe gpsListInfoAlarmSubscribe() {
        return new GpsListInfoAlarmSubscribe();
    }

    @Bean
    public GpsSpecialListInfoAlarmSubscribe gpsSpecialListInfoAlarmSubscribe() {
        return new GpsSpecialListInfoAlarmSubscribe();
    }
}
