package com.zkkj.gps.gateway.ccs.config;

import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * author : cyc
 * Date : 2019/7/23
 * 返回一个dozer实例bean，目的是做对象间属性值的赋值操作
 */

@Configuration
public class DozerMapperConfig {

    @Bean
    public DozerBeanMapperFactoryBean mapper(){
        return new DozerBeanMapperFactoryBean();
    }
}
