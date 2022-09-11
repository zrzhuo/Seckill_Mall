package com.zhuo.seckill.mapper;

import com.zhuo.seckill.entity.SeckillOrder;
import org.apache.ibatis.annotations.Param;

public interface SeckillOrderMapper {

    /**
     * 通过用户id和商品ID查询指定订单
     */
    SeckillOrder selectOrder(@Param("userId") String userId, @Param("goodsId") Long goodsId);

    /**
     * 插入秒杀订单并获取该订单的id
     */
    void insertSeckillOrder(SeckillOrder seckillOrder);
}
