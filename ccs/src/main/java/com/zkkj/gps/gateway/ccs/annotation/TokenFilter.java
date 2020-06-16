package com.zkkj.gps.gateway.ccs.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * author : cyc
 * Date : 2020/3/23
 * token过滤器注解，方法上添加此注解则不需要token校验
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TokenFilter {
}
