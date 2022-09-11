package com.zhuo.seckill.config;

import com.zhuo.seckill.entity.User;
import com.zhuo.seckill.service.UserService;
import com.zhuo.seckill.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 为User类设置"处理器方法参数解析器"
 */
@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private UserService userService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> clazz = parameter.getParameterType();
//        System.out.println("参数类型为: " +  clazz);
        return clazz == User.class; // User类型的参数
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
//        System.out.println("通过UserArgumentResolver处理的User对象");
        String ticket = CookieUtil.getCookieValue(request, "userTicket");// 从Cookie中获取用户ticket
        if(ticket == null ||ticket.isEmpty()){
            return null;
        }
        return userService.getUserFromRedis(ticket, request, response); // 根据ticket从redis中获取User对象, 赋值到相应的控制器方法参数
    }
}
