package com.zhuo.seckill.validator;

import com.zhuo.seckill.utils.MobileValidateUtil;
import com.zhuo.seckill.validator.IsMobile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 手机号码校验类
 */

public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {

    private boolean required = false;

    @Override
    public void initialize(IsMobile constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(required){
            return MobileValidateUtil.isMobile(value); // 调用手机号码校验规则
        }
        else{
            if(value.isEmpty()){
                return false;
            }else{
                return MobileValidateUtil.isMobile(value); // 调用手机号码校验规则
            }
        }
    }
}
