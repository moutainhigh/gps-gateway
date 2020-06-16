package com.zkkj.gps.gateway.ccs.config;

import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.zkkj.gps.gateway.ccs.service.BaiDuWebApiService;
import com.zkkj.gps.gateway.ccs.service.GpsWebApiService;
import com.zkkj.gps.gateway.ccs.service.LocationWebApiService;
import com.zkkj.gps.gateway.ccs.service.WarnWebApiService;
import feign.hystrix.HystrixFeign;
import feign.hystrix.SetterFactory;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.netflix.hystrix.HystrixThreadPoolProperties.Setter;

/**
 * author : cyc
 * Date : 2019-06-12
 * 外部接口调用配置类
 */
@Configuration
public class WebApiConfig {

    private Logger logger = LoggerFactory.getLogger(WebApiConfig.class);

    /**
     * gps服务地址
     */
    @Value("${application.gpsWebApi.url}")
    private String gpsWebApiUrl;

    /**
     * 百度服务地址
     */
    @Value("${application.baiDuWebApi.url}")
    private String baiDuWebApiUrl;

    /**
     * 外部定位地址
     */
    @Value("${application.locationWebApi.url}")
    private String locationWebApi;

    @Autowired
    private HystrixClientFallback hystrixClientFallback;

    /**
     * 提醒服务地址
     */
    @Value("${application.warnWebApi.url}")
    private String warnWebApi;

    @Bean
    WarnWebApiService warnClient() {
        WarnWebApiService warnWebApiService = null;
        try {
            warnWebApiService = HystrixFeign.builder()
                    .decoder(new JacksonDecoder())
                    .encoder(new JacksonEncoder())
                    .setterFactory((target, method) ->
                            new SetterFactory.Default().create(target, method).
                                    andCommandPropertiesDefaults(HystrixCommandProperties.defaultSetter().
                                            withExecutionTimeoutInMilliseconds(1000 * 10)))
                    .target(WarnWebApiService.class, warnWebApi);
        } catch (Exception e) {
            logger.error("WebApiConfig.warnClient is error", e);
        }
        return warnWebApiService;
    }


    @Bean
    GpsWebApiService gpsClient() {
        GpsWebApiService gpsWebApiService = null;
        try {
            gpsWebApiService = HystrixFeign.builder()
                    .decoder(new JacksonDecoder())
                    .encoder(new JacksonEncoder())
                    .setterFactory((target, method) ->
                            new SetterFactory.Default().create(target, method).andThreadPoolPropertiesDefaults(Setter().withCoreSize(30)).
                                    andCommandPropertiesDefaults(HystrixCommandProperties.defaultSetter().
                                            withExecutionTimeoutInMilliseconds(1000 * 30)))
                    .target(GpsWebApiService.class, gpsWebApiUrl, hystrixClientFallback);
        } catch (Exception e) {
            logger.error("WebApiConfig.gpsClient is error", e);
        }
        return gpsWebApiService;
    }


    @Bean
    BaiDuWebApiService baiDuClient() {
        BaiDuWebApiService baiDuWebApiService = null;
        try {
            baiDuWebApiService = HystrixFeign.builder()
                    .decoder(new JacksonDecoder())
                    .encoder(new JacksonEncoder())
                    .setterFactory((target, method) ->
                            new SetterFactory.Default().create(target, method).
                                    andCommandPropertiesDefaults(HystrixCommandProperties.defaultSetter().
                                            withExecutionTimeoutInMilliseconds(1000 * 30)))
                    .target(BaiDuWebApiService.class, baiDuWebApiUrl);
        } catch (Exception e) {
            logger.error("WebApiConfig.baiDuClient is error", e);
        }
        return baiDuWebApiService;
    }

    @Bean
    LocationWebApiService locationClient() {
        LocationWebApiService locationWebApiService = null;
        try {
            locationWebApiService = HystrixFeign.builder()
                    .decoder(new JacksonDecoder())
                    .encoder(new JacksonEncoder())
                    .setterFactory((target, method) ->
                            new SetterFactory.Default().create(target, method).
                                    andCommandPropertiesDefaults(HystrixCommandProperties.defaultSetter().
                                            withExecutionTimeoutInMilliseconds(1000 * 30)))
                    .target(LocationWebApiService.class, locationWebApi);
        } catch (Exception e) {
            logger.error("WebApiConfig.locationClient is error", e);
        }
        return locationWebApiService;
    }
}
