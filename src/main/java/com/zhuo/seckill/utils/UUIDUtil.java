package com.zhuo.seckill.utils;


import java.util.UUID;

/**
 * 生成一个随机的UUID
 **/
public class UUIDUtil {

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
