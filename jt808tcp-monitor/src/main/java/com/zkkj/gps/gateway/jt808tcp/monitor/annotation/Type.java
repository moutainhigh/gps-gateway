package com.zkkj.gps.gateway.jt808tcp.monitor.annotation;

import java.lang.annotation.*;

/**
 * @author suibozhuliu
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Type {

    int[] value();

    String desc() default "";

}