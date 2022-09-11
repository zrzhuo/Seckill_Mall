package com.zhuo.seckill.service;

import com.zhuo.seckill.vo.SeckillGoodsVo;

import java.util.List;


public interface GoodsService {
    /**
     * 获取所有秒杀商品的列表
     */
    List<SeckillGoodsVo> getSeckillGoods();


    /**
     * 通过id获取秒杀商品详情
     */
    SeckillGoodsVo getSeckillGoodsById(Long goodsId);
}
