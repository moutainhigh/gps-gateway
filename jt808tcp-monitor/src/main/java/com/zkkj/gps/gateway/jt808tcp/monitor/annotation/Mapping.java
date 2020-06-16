package com.zkkj.gps.gateway.jt808tcp.monitor.annotation;

import java.lang.annotation.*;

/**
 * @author suibozhuliu
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Mapping {

    int[] types();

    String desc() default "";

}