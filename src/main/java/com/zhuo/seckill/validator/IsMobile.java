package com.zhuo.seckill.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 手机号验证
 */

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {IsMobileValidator.class}) // 指定校验类
public @interface IsMobile  {
    boolean required() default true; // 是否必填

    String message() default "手机号格式错误"; // 默认信息

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
