package com.zkkj.gps.gateway.ccs.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.collections4.MapUtils;
import org.hibernate.validator.HibernateValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zkkj.gps.gateway.ccs.exception.ParamException;

/**
 * author : cyc
 * Date : 2019-06-15
 */
public class BeanValidate {

    private static Logger logger = LoggerFactory.getLogger(BeanValidate.class);

    /**
     * 会校验所有属性，然后返回所有的验证失败信息。
     */
    //private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    /**
     * failFast: true 快速失败返回模式，false 普通模式
     */
    private static ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
            .configure()
            .failFast(true)
            .buildValidatorFactory();

    public static <T> Map<String, String> validate(T t, Class... groups) {

        Validator validator = validatorFactory.getValidator();
        Set validateResult = validator.validate(t, groups);
        if (CollectionUtils.isEmpty(validateResult)) {
            return Collections.emptyMap();
        }
        LinkedHashMap errors = Maps.newLinkedHashMap();
        Iterator iterator = validateResult.iterator();
        while (iterator.hasNext()) {
            ConstraintViolation violation = (ConstraintViolation) iterator.next();
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return errors;
    }

    public static Map<String, String> validateList(Collection<?> collection) {
        Preconditions.checkNotNull(collection);
        Iterator<?> iterator = collection.iterator();
        Map errors;
        do {
            if (!iterator.hasNext()) {
                return Collections.emptyMap();
            }
            Object o = iterator.next();
            errors = validate(o, new Class[0]);
        } while (errors.isEmpty());
        return errors;
    }


    public static Map<String, String> validateObject(Object first, Object... objects) {
        if (objects != null && objects.length > 0) {
            return validateList(Lists.asList(first, objects));
        }
        return validate(first, new Class[0]);
    }

    /**
     * 校验参数
     *
     * @param param
     * @throws Exception
     */
    public static void checkParam(Object param) {
        Map<String, String> errorMap = validateObject(param);
        if (!MapUtils.isEmpty(errorMap)) {
            logger.error(param.toString());
            throw new ParamException(errorMap.toString());
        }
    }
}
