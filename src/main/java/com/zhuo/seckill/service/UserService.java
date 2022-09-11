package com.zhuo.seckill.service;

import com.zhuo.seckill.entity.User;
import com.zhuo.seckill.vo.LoginVo;
import com.zhuo.seckill.vo.RegisterVo;
import com.zhuo.seckill.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserService {
    /**
     * 用户注册
     */
    RespBean register(RegisterVo registerVo, HttpServletRequest request, HttpServletResponse response);
    /**
     * 用户登录
     */
    RespBean login(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);

    /**
     * 从redis中获取已登录用户
     */
    User getUserFromRedis(String ticket, HttpServletRequest request, HttpServletResponse response);

}
