package com.matrixboot.validation.validator;

import cn.hutool.core.lang.Validator;
import com.matrixboot.validation.constraint.IPv6;

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
public class IPv6Validator implements ConstraintValidator<IPv6, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return Validator.isIpv6(s);
    }

}
