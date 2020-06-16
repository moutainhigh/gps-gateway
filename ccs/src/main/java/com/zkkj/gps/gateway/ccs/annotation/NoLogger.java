package com.zkkj.gps.gateway.ccs.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * author : cyc
 * Date : 2020/1/6
 * 不记录日志的注解(日志文件和日志表均不记录)
 */

@Target({ElementType.METHOD})
@Retention(RUNTIME)
public @interface NoLogger {
}
