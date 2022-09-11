package com.zhuo.seckill.rabbitmq;

import com.alibaba.fastjson2.JSONObject;
import com.zhuo.seckill.entity.Order;
import com.zhuo.seckill.entity.SeckillMessage;
import com.zhuo.seckill.entity.User;
import com.zhuo.seckill.service.GoodsService;
import com.zhuo.seckill.service.OrderService;
import com.zhuo.seckill.vo.SeckillGoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 消息消费者
 */
@Service
@Slf4j
public class MQReceiver {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;

    /**
     * 接收秒杀信息并下单
     */
    @RabbitListener(queues = "seckillQueue")
    public void receiveAndHandler(String message){
//        log.info("接收消息: " + message);
        SeckillMessage seckillMessage = JSONObject.parseObject(message, SeckillMessage.class);
        User user = seckillMessage.getUser();
        long goodsId = seckillMessage.getGoodsId();
        SeckillGoodsVo goods = goodsService.getSeckillGoodsById(goodsId);
        orderService.seckill(user, goods);
    }

}
