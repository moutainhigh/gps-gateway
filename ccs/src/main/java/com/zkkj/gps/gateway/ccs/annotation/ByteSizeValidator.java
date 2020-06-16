package com.zkkj.gps.gateway.ccs.annotation;

import java.io.UnsupportedEncodingException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import com.zkkj.gps.gateway.common.constant.BaseConstant;

/**
 * author : cyc
 * Date : 2019-07-05
 * 创建验证的执行类
 */
public class ByteSizeValidator implements ConstraintValidator<ByteSize, String> {


    private ByteSize byteSize;

    @Override
    public void initialize(ByteSize constraintAnnotation) {
        this.byteSize = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        int length = 0;
        if (StringUtils.isNotBlank(value)) {
            try {
                length = value.getBytes(BaseConstant.CHAR_SET).length;
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        return byteSize.max() >= length;
    }
}
