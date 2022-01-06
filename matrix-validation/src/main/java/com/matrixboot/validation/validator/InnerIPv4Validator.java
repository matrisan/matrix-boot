package com.matrixboot.validation.validator;

import cn.hutool.core.net.Ipv4Util;
import com.matrixboot.validation.constraint.InnerIPv4;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * TODO
 * <p>
 * create in 2021/11/30 3:50 下午
 *
 * @author shishaodong
 * @version 0.0.1
 */
public class InnerIPv4Validator implements ConstraintValidator<InnerIPv4, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return Ipv4Util.isInnerIP(s);
    }
}
