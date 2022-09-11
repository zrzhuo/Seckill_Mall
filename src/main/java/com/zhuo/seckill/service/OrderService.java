package com.zhuo.seckill.service;

import com.zhuo.seckill.entity.Order;
import com.zhuo.seckill.entity.User;
import com.zhuo.seckill.vo.SeckillGoodsVo;
import com.zhuo.seckill.vo.OrderDetailVo;

public interface OrderService {

    /**
     * 秒杀
     */
    Order seckill(User user, SeckillGoodsVo goods);

    /**
     * 通过id查询订单详情
     */
    OrderDetailVo getOderDetailById(Long orderId);
}
