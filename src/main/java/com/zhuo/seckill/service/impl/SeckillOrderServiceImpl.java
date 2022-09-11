package com.zhuo.seckill.service.impl;

import com.zhuo.seckill.entity.SeckillOrder;
import com.zhuo.seckill.mapper.SeckillOrderMapper;
import com.zhuo.seckill.service.SeckillOrderService;
import com.zhuo.seckill.utils.MD5Util;
import com.zhuo.seckill.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @Override
    public String createSeckillPathForUser(String userId, Long goodsId) {
        String path = MD5Util.md5(UUIDUtil.uuid()); // 随机生成秒杀接口路径
        // 将接口地址存入redis
        redisTemplate.opsForValue().set("seckillPath:"+userId +":"+goodsId, path, 60, TimeUnit.SECONDS);
        return path;
    }

}
