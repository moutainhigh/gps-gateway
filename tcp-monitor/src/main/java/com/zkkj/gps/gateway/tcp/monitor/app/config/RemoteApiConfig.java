package com.zkkj.gps.gateway.tcp.monitor.app.config;

import com.netflix.hystrix.HystrixCommandProperties;
import com.zkkj.gps.gateway.tcp.monitor.app.service.JtxApiService;
import com.zkkj.gps.gateway.tcp.monitor.app.service.JtxJt1ApiService;
import com.zkkj.gps.gateway.tcp.monitor.utils.LoggerUtils;
import feign.hystrix.HystrixFeign;
import feign.hystrix.SetterFactory;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 远程Api调用配置
 * @author suibozhuliu
 */

@Configuration
@Slf4j
public class RemoteApiConfig {

    /**
     * gps服务地址，洛阳大区，jt0
     */
    @Value("${jtx_webapi_url}")
    private String jtxWebApiUrl;

    /**
     * gps服务地址，铜川大区，jt1
     */
    @Value("${jtx_webapi_url_jt1}")
    private String jtxJt1WebApiUrl;

    @Bean
    JtxApiService gpsClient() {
        JtxApiService jtxApiService = null;
        try {
            jtxApiService = HystrixFeign.builder()
                    .decoder(new JacksonDecoder())
                    .encoder(new JacksonEncoder())
                    .setterFactory((target, method) ->
                            new SetterFactory.Default().create(target, method).
                                    andCommandPropertiesDefaults(HystrixCommandProperties.defaultSetter().
                                            withExecutionTimeoutInMilliseconds(1000 * 60)))
                    .target(JtxApiService.class, jtxWebApiUrl);
        } catch (Exception e) {
            LoggerUtils.error(log,"JtxApiService.gpsClient is error：" + e);
        }
        return jtxApiService;
    }

    @Bean
    JtxJt1ApiService gpsJt1Client() {
        JtxJt1ApiService jtxJt1ApiService = null;
        try {
            jtxJt1ApiService = HystrixFeign.builder()
                    .decoder(new JacksonDecoder())
                    .encoder(new JacksonEncoder())
                    .setterFactory((target, method) ->
                            new SetterFactory.Default().create(target, method).
                                    andCommandPropertiesDefaults(HystrixCommandProperties.defaultSetter().
                                            withExecutionTimeoutInMilliseconds(1000 * 60)))
                    .target(JtxJt1ApiService.class, jtxJt1WebApiUrl);
        } catch (Exception e) {
            LoggerUtils.error(log,"JtxJt1ApiService.gpsClient is error：" + e);
        }
        return jtxJt1ApiService;
    }

}
