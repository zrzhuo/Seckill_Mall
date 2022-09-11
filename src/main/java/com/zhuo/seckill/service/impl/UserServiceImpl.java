package com.zhuo.seckill.service.impl;

import com.zhuo.seckill.entity.User;
import com.zhuo.seckill.exception.GlobalException;
import com.zhuo.seckill.mapper.UserMapper;
import com.zhuo.seckill.service.UserService;
import com.zhuo.seckill.utils.CookieUtil;
import com.zhuo.seckill.utils.MD5Util;
import com.zhuo.seckill.utils.UUIDUtil;
import com.zhuo.seckill.vo.LoginVo;
import com.zhuo.seckill.vo.RegisterVo;
import com.zhuo.seckill.vo.RespBean;
import com.zhuo.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @Override
    public RespBean register(RegisterVo registerVo, HttpServletRequest request, HttpServletResponse response) {
        String mobile = registerVo.getMobile();
        // 用户已存在
        if(userMapper.selectUserById(mobile) != null){
            throw new GlobalException(RespBeanEnum.REPEAT_MOBILE);
        }
        String password = registerVo.getPassword();
        String nickname = registerVo.getNickname();
        User user = new User();
        user.setId(mobile);
        user.setNickname(nickname);
        user.setSlat("12345678"); // todo:随机生成salt
        user.setPassword(MD5Util.fromPassToDBPass(password, user.getSlat()));
        user.setRegisterDate(new Date());
        user.setLoginCount(0);
        boolean res = userMapper.insertUser(user);
        // insert失败
        if(!res){
            throw new GlobalException(RespBeanEnum.REGISTER_ERROR);
        }
        // 注册成功自动登录
        return login(new LoginVo(mobile, password), request, response);
    }

    @Override
    public RespBean login(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        User user = userMapper.selectUserById(mobile);
        // 不存在该用户
        if(user == null){
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        // 密码不正确
        if(!MD5Util.fromPassToDBPass(password, user.getSlat()).equals(user.getPassword())){
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        String ticket = UUIDUtil.uuid(); // 为用户随机生成一个uuid作为ticket
//        request.getSession().setAttribute(ticket, user); // 将用户添加在session中
        redisTemplate.opsForValue().set("user:" + ticket, user); // 将用户储存在redis中
        CookieUtil.setCookie(request, response, "userTicket", ticket); // 将ticket添加到Cookie中
        return RespBean.success(); // 登录成功
    }

    @Override
    public User getUserFromRedis(String ticket, HttpServletRequest request, HttpServletResponse response) {
        if(ticket.isEmpty()){
            return null;
        }
        // 根据ticket从redis中获取用户
        User user = (User)redisTemplate.opsForValue().get("user:" + ticket);
//        if(user != null){
//            // 将用户ticket添加到cookie中
//            CookieUtil.setCookie(request, response, "userTicker", ticket);
//        }
        return user;
    }
}
