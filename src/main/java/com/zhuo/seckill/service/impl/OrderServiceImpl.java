package com.zhuo.seckill.service.impl;

import com.zhuo.seckill.entity.Goods;
import com.zhuo.seckill.entity.Order;
import com.zhuo.seckill.entity.SeckillOrder;
import com.zhuo.seckill.entity.User;
import com.zhuo.seckill.mapper.*;
import com.zhuo.seckill.service.OrderService;
import com.zhuo.seckill.vo.SeckillGoodsVo;
import com.zhuo.seckill.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private SeckillOrderMapper seckillOrderMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    @Transactional
    public Order seckill(User user, SeckillGoodsVo goods) {
        // 秒杀商品列表减库存
        seckillGoodsMapper.decreaseStock(goods.getId());
        // 商品列表减库存
        goodsMapper.decreaseStock(goods.getId());
        // 生成订单并插入订单表
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goods.getId());
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(goods.getSeckillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        orderMapper.insertOrder(order);
        // 生成秒杀订单并插入秒杀订单表
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setUserId(user.getId());
        seckillOrder.setGoodsId(goods.getId());
        seckillOrder.setOrderId(order.getId());
        seckillOrderMapper.insertSeckillOrder(seckillOrder);
        // 将秒杀订单缓存入redis
        ValueOperations<String, Object> redisOperations = redisTemplate.opsForValue();
        redisOperations.set("seckillOrder:" + user.getId() + ":" + goods.getId(), seckillOrder);
        // 返回订单
        return order;
    }

    @Override
    public OrderDetailVo getOderDetailById(Long orderId) {
        Order order = orderMapper.selectOrderById(orderId);
        Goods goods = goodsMapper.selectGoodsById(order.getGoodsId());
        User user = userMapper.selectUserById(order.getUserId());
        return new OrderDetailVo(order, user, goods);
    }


}
