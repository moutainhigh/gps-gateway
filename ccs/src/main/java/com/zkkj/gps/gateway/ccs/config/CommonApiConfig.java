package com.zkkj.gps.gateway.ccs.config;


import com.netflix.hystrix.HystrixCommandProperties;
import com.zkkj.gps.gateway.ccs.service.CommonService;
import feign.hystrix.HystrixFeign;
import feign.hystrix.SetterFactory;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonApiConfig {

    @Value("${urlForCommon}")
    private String urlForCommon;

    /**
     * @param
     * @return
     * @Description 访问公共管理平台地址
     * @author liuxin
     * @date 2018/11/21 15:19
     **/
    @Bean
    CommonService clockClientCommon() throws Exception {
        return HystrixFeign.builder()
                .decoder(new JacksonDecoder())
                .encoder(new JacksonEncoder())
                .setterFactory((target, method) ->
                        new SetterFactory.Default().create(target, method).
                                andCommandPropertiesDefaults(HystrixCommandProperties.defaultSetter().
                                        withExecutionTimeoutInMilliseconds(1000 * 60)))
                .target(CommonService.class, this.urlForCommon);
    }
}
