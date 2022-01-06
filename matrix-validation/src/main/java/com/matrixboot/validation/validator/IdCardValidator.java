package com.matrixboot.validation.validator;

import cn.hutool.core.util.IdcardUtil;
import com.matrixboot.validation.constraint.IdCard;

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
public class IdCardValidator implements ConstraintValidator<IdCard, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return IdcardUtil.isValidCard(s);
    }
}
