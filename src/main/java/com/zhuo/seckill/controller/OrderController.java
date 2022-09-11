package com.zhuo.seckill.controller;



import com.zhuo.seckill.entity.User;
import com.zhuo.seckill.service.OrderService;
import com.zhuo.seckill.vo.OrderDetailVo;
import com.zhuo.seckill.vo.RespBean;
import com.zhuo.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;


    @RequestMapping("/detail")
    @ResponseBody
    public RespBean detail(User user, Long orderId){
        if (user == null) {
            // todo: 跳转到登录页面
            return RespBean.error(RespBeanEnum.ACCESS_LIMIT);
        }
        OrderDetailVo detail = orderService.getOderDetailById(orderId);
        return RespBean.success(detail);
    }

}
