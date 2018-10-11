package com.zhengtoon.framework.annualticketrecharge.common.verify;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = UniqueFieldsValidator.class)
public @interface UniqueFields {

    //默认错误消息
    String message() default "该用户已存在";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
