package com.zkkj.gps.gateway.gpsZjxl.config;

import com.netflix.hystrix.HystrixCommandProperties;
import com.zkkj.gps.gateway.gpsZjxl.fallback.HystrixClientFallback;
import com.zkkj.gps.gateway.gpsZjxl.service.LocationWebApiService;
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
     * 中交兴路服务地址
     */
    @Value("${application.locationWebApi.url}")
    private String zjxlLocationWebApi;

    @Autowired
    private HystrixClientFallback hystrixClientFallback;


    @Bean
    LocationWebApiService locationClient() {
        LocationWebApiService locationWebApiService = null;
        try {
            locationWebApiService = HystrixFeign.builder()
                    .decoder(new JacksonDecoder())
                    .encoder(new JacksonEncoder())
                    .setterFactory((target, method) ->
                            new SetterFactory.Default().create(target, method).
                                    andThreadPoolPropertiesDefaults(Setter().withCoreSize(20)).
                                    andCommandPropertiesDefaults(HystrixCommandProperties.defaultSetter().
                                            withExecutionTimeoutInMilliseconds(1000 * 10)))
                    .target(LocationWebApiService.class, zjxlLocationWebApi, hystrixClientFallback);
        } catch (Exception e) {
            logger.error("WebApiConfig.locationClient is error", e);
        }
        return locationWebApiService;
    }
}
