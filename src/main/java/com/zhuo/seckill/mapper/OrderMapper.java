package com.zhuo.seckill.mapper;

import com.zhuo.seckill.entity.Order;
import io.lettuce.core.dynamic.annotation.Param;

public interface OrderMapper {

    /**
     * 插入订单并获取该订单的id
     */
    void insertOrder(Order order);

    /**
     * 根据id查询订单
     */
    Order selectOrderById(@Param("id") Long orderId);
}
