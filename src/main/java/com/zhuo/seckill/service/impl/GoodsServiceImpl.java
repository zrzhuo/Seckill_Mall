package com.zhuo.seckill.service.impl;

import com.zhuo.seckill.mapper.GoodsMapper;
import com.zhuo.seckill.service.GoodsService;
import com.zhuo.seckill.vo.SeckillGoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public List<SeckillGoodsVo> getSeckillGoods() {
        return goodsMapper.selectAllSeckillGoods();
    }

    @Override
    public SeckillGoodsVo getSeckillGoodsById(Long goodsId) {
        return goodsMapper.selectSeckillGoodsById(goodsId);
    }
}
