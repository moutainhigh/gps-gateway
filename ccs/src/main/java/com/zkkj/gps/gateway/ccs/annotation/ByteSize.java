package com.zkkj.gps.gateway.ccs.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * author : cyc
 * Date : 2019-07-05
 * 字节长度自定义长度校验注解
 */

@Target({FIELD, METHOD, PARAMETER, ANNOTATION_TYPE})
@Retention(RUNTIME)
//指定验证器
@Constraint(validatedBy = ByteSizeValidator.class)
@Documented
public @interface ByteSize {

    // 默认错误消息
    String message() default "{javax.validation.constraints.Size.message}";

    //分组
    Class<?>[] groups() default {};

    //负载
    Class<? extends Payload>[] payload() default {};

    //最大长度
    int max() default Integer.MAX_VALUE;

}
