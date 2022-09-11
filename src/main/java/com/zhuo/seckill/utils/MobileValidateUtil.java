package com.zhuo.seckill.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 根据正则表达式对手机号进行校验
 */
public class MobileValidateUtil {
    private final static Pattern mobile_pattern = Pattern.compile("^1(3[0-9]|4[01456879]|5[0-35-9]|6[2567]|7[0-8]|8[0-9]|9[0-35-9])\\d{8}$");

    public static boolean isMobile(String mobile){
        if (mobile == null || mobile.isEmpty()) {
            return false;
        }
        Matcher matcher = mobile_pattern.matcher(mobile);
        return matcher.matches();
    }

}
