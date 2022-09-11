package com.zhuo.seckill.utils;


import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {

    private static final String salt = "1a2b3c4d";

    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    /**
     * 对用户输入密码进行第一次加密
     */
    public static String inputPassToFromPass(String inputPass){
        String str = "" + salt.charAt(0) + salt.charAt(1)+ inputPass + salt.charAt(2) + salt.charAt(3);
        return md5(str);
    }

    /**
     * 对第一次加密后的密码进行第二次加密
     */
    public static String fromPassToDBPass(String fromPass, String salt){
        String str = "" + salt.charAt(3) + salt.charAt(2)+ fromPass + salt.charAt(1) + salt.charAt(0);
        return md5(str);
    }

    /**
     * 两次md5加密
     */
    public static String inputPassToDBPass(String inputPass, String salt){
        return fromPassToDBPass(inputPassToFromPass(inputPass), salt);
    }

    public static void main(String[] args) {
        System.out.println(inputPassToFromPass("123456"));
        System.out.println(inputPassToDBPass("123456", "12345678"));
    }
}
