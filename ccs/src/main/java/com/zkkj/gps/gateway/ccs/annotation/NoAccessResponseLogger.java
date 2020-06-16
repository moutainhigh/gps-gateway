package com.zkkj.gps.gateway.ccs.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * author : cyc
 * Date : 2020/1/6
 * 不需要记录结果日志
 */

@Target({ElementType.METHOD})
@Retention(RUNTIME)
public @interface NoAccessResponseLogger {
}
