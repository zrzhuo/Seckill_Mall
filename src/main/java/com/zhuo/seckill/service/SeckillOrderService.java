package com.zhuo.seckill.service;


public interface SeckillOrderService {

    /**
     * 为用户生成秒杀路径
     */
    String createSeckillPathForUser(String userId, Long goodsId);
}
