package com.zkkj.gps.gateway.automation.config;

import com.netflix.hystrix.HystrixCommandProperties;
import com.zkkj.gps.gateway.automation.service.TerminalOptionsService;
import com.zkkj.gps.gateway.automation.utils.LoggerUtils;
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

    //通信中心Tcp服务地址
    @Value("${tcp_webapi_url}")
    private String terminalTcpWebApiUrl;

    @Bean
    TerminalOptionsService authlbsClient() {
        TerminalOptionsService optionsService = null;
        try {
            optionsService = HystrixFeign.builder()
                    .decoder(new JacksonDecoder())
                    .encoder(new JacksonEncoder())
                    .setterFactory((target, method) ->
                            new SetterFactory.Default().create(target, method).
                                    andCommandPropertiesDefaults(HystrixCommandProperties.defaultSetter().
                                            withExecutionTimeoutInMilliseconds(1000 * 60)))
                    .target(TerminalOptionsService.class, terminalTcpWebApiUrl);
        } catch (Exception e) {
            LoggerUtils.error(log,"TerminalOptionsService.optionsService is error：" + e);
        }
        return optionsService;
    }

}
