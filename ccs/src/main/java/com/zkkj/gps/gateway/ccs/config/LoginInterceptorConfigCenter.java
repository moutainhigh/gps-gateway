package com.zkkj.gps.gateway.ccs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class LoginInterceptorConfigCenter extends WebMvcConfigurerAdapter {
    public LoginInterceptorConfigCenter() {

    }

    @Bean
    public LoginInterceptor securityInterceptor() {
        return new LoginInterceptor();
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.securityInterceptor()).addPathPatterns(new String[]{"/**"}).excludePathPatterns(
                new String[]{
                        "/swagger**",
                        "/v2/**",
                        "/images/**",
                        "/",
                        "/**/*.css",
                        "/**/*.js",
                        "/**/*.png",
                        "/**/*.jpg",
                        "/**/*.jpeg",
                        "/rest/api/**",
                        "/rabbitMQ/**",
                        "/login/login",
                        "/login/loginByTruckNum",
                        "/login/loginByTruckNumAndKey",
                        "/hello/**",
                        "/outGps/getTerminalInfoByTerminalId",
                        "/object/getObjectByClassPath"
                });
    }
}