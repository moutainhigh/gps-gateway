package com.zkkj.gps.gateway.celltrack.celltrackmonitor.config;

import com.netflix.hystrix.HystrixCommandProperties;
import com.zkkj.gps.gateway.celltrack.celltrackmonitor.service.AuthlbsRemoteService;
import com.zkkj.gps.gateway.celltrack.celltrackmonitor.utils.LoggerUtils;
import feign.hystrix.HystrixFeign;
import feign.hystrix.SetterFactory;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 熔断配置类
 * @author suibozhuliu
 */
@Component
@Slf4j
public class HystrixClientConfig {

    //神州基站定位服务地址
    @Value("${authlbs_webapi_url}")
    private String authlbsWebApiUrl;

    @Bean
    AuthlbsRemoteService authlbsClient() {
        AuthlbsRemoteService authlbsService = null;
        try {
            authlbsService = HystrixFeign.builder()
                    .decoder(new JacksonDecoder())
                    .encoder(new JacksonEncoder())
                    .setterFactory((target, method) ->
                            new SetterFactory.Default().create(target, method).
                                    andCommandPropertiesDefaults(HystrixCommandProperties.defaultSetter().
                                            withExecutionTimeoutInMilliseconds(1000 * 60)))
                    .target(AuthlbsRemoteService.class, authlbsWebApiUrl);
        } catch (Exception e) {
            LoggerUtils.error(log,"AuthlbsRemoteService.authlbsClient is error：" + e);
        }
        return authlbsService;
    }
}
