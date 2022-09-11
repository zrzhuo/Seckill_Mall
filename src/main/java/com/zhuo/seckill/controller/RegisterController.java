package com.zhuo.seckill.controller;

import com.zhuo.seckill.entity.User;
import com.zhuo.seckill.service.UserService;
import com.zhuo.seckill.vo.RegisterVo;
import com.zhuo.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private UserService userService;

    @RequestMapping("/toRegister")
    public String toRegister(){
        return "register";
    }

    @RequestMapping("/doRegister")
    @ResponseBody
    public RespBean doRegister(@Valid RegisterVo registerVo, HttpServletRequest request, HttpServletResponse response){
        return  userService.register(registerVo, request, response);
    }

}
