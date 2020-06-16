package com.zkkj.gps.gateway.jt808tcp.monitor.annotation;

import com.zkkj.gps.gateway.jt808tcp.monitor.enums.DataType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author suibozhuliu
 */
@Target(ElementType.METHOD)//方法声明
@Retention(RetentionPolicy.RUNTIME)//VM将在运行期间保留注解，因此可以通过反射机制读取注解的信息
public @interface Property {

    int index() default -1;

    String[] indexOffsetName() default "";

    int length() default -1;

    String lengthName() default "";

    DataType type() default DataType.BYTE;

    String charset() default "GBK";

    byte pad() default 0;

    String desc() default "";

}