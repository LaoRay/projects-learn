package com.zhengtoon.framework.annualticketrecharge.common.verify;

import com.zhengtoon.framework.annualticketrecharge.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueFieldsValidator implements ConstraintValidator<UniqueFields, String> {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void initialize(UniqueFields constraintAnnotation) {

    }

    /**
     * 如果数据库中已有该用户，则提示用户已存在
     *
     * @param value   请求值
     * @param context context环境
     * @return true 通过
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return userMapper.selectByUserName(value) == null;
    }
}
