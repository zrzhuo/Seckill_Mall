package com.zhuo.seckill.controller;

import com.alibaba.fastjson2.JSONObject;
import com.wf.captcha.SpecCaptcha;
import com.zhuo.seckill.entity.SeckillMessage;
import com.zhuo.seckill.entity.SeckillOrder;
import com.zhuo.seckill.entity.User;
import com.zhuo.seckill.rabbitmq.MQSender;
import com.zhuo.seckill.service.GoodsService;
import com.zhuo.seckill.service.OrderService;
import com.zhuo.seckill.service.SeckillOrderService;
import com.zhuo.seckill.vo.SeckillGoodsVo;
import com.zhuo.seckill.vo.RespBean;
import com.zhuo.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private SeckillOrderService seckillOrderService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private MQSender mqSender;
    private final Map<Long, Boolean> isEmptyStock = new HashMap<>(); // 记录商品库存是否为0


    /**
     * 获取某用户对某商品的秒杀路径
     */
    @RequestMapping(value = "/getSeckillPath", method = RequestMethod.GET)
    @ResponseBody
    public RespBean getSeckillPath(User user, Long goodsId, String captcha){
        // 用户未登录
        if(user == null){
            return RespBean.error(RespBeanEnum.ACCESS_LIMIT);
        }
        // 校验验证码
        String ans = (String) redisTemplate.opsForValue().get("seckillCaptcha:" + user.getId() + ":" + goodsId);
        if(!captcha.equalsIgnoreCase(ans)){
            return RespBean.error(RespBeanEnum.ERROR_CAPTCHA);
        }
        // 获取该用户的秒杀路径
        String path = seckillOrderService.createSeckillPathForUser(user.getId(), goodsId);
        return RespBean.success(path);
    }

    /**
     * 秒杀
     */
    @RequestMapping(value = "/doSeckill/{path}", method = RequestMethod.POST)
    @ResponseBody
    public RespBean doSeckill(User user, Long goodsId, @PathVariable String path){
        // 用户未登录
        if(user == null){
            return RespBean.error(RespBeanEnum.ACCESS_LIMIT);
            // todo: 未登录, 跳转到登录页面
        }
        // 判断path是否正确
        String realPath = (String) redisTemplate.opsForValue().get("seckillPath:"+user.getId()+":"+goodsId);
        if(path == null || !path.equals(realPath)){
            return RespBean.error(RespBeanEnum.ERROR_PATH);
        }
        // 在内存中(isEmptyStock)判断当前商品库存是否为0
        if(isEmptyStock.get(goodsId)){
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        ValueOperations<String, Object> redisOperations = redisTemplate.opsForValue();
        // 判断是否重复下单, 尝试从redis中获取秒杀订单, 若该订单已存在, 则为重复下单
        SeckillOrder seckillOrder = (SeckillOrder) redisOperations.get("seckillOrder:" + user.getId() + ":" + goodsId);
        if(seckillOrder != null){
            return RespBean.error(RespBeanEnum.REPEAT_ERROR);
        }
        // redis预减库存
        Long stock = redisOperations.decrement("seckillGoodsStock:" + goodsId); // 对redis中存放的库存减1, 然后返回当前库存
        // 判断库存是否充足
        if(stock != null && stock < 0){
            redisOperations.increment("seckillGoodsStock:" + goodsId); // 库存加1, 使其为0
            isEmptyStock.put(goodsId, true); // 在内存中记录当前商品库存已经为0
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        // 生成秒杀消息并发送
        SeckillMessage seckillMessage = new SeckillMessage(user, goodsId);
        mqSender.sendSeckillMessage(JSONObject.toJSONString(seckillMessage));
        return RespBean.success(0);
    }


    /**
     * 获取秒杀结果: 查询orderId, 存在则秒杀成功, 不存在且库存不足则秒杀失败
     */
    @RequestMapping(value = "/seckillResult", method = RequestMethod.GET)
    @ResponseBody
    public RespBean seckillResult(User user, Long goodsId){
        // 用户未登录
        if(user == null){
            return RespBean.error(RespBeanEnum.ACCESS_LIMIT);
            // todo: 未登录, 跳转到登录页面
        }
        ValueOperations<String, Object> redisOperations = redisTemplate.opsForValue();
        // 从redis中尝试获取订单
        SeckillOrder seckillOrder = (SeckillOrder)redisOperations.get("seckillOrder:" + user.getId() + ":" + goodsId);
        // 从redis中获取当前商品的库存
        Integer stock = (Integer)redisOperations.get("seckillGoodsStock:" + goodsId);
        if(seckillOrder != null){
            return RespBean.success(seckillOrder.getOrderId()); // 存在订单, 秒杀成功
        }
        else if(stock != null && stock < 1){
            return RespBean.success(-1); // 不存在订单, 且库存不足, 秒杀失败
        }
        else{
            return RespBean.success(0); // 不存在订单, 但仍有库存, 继续排队等待
        }
    }

    /**
     * 生成验证码
     */
    @RequestMapping(value = "/captcha", method = RequestMethod.GET)
    public void verifyCode(User user, Long goodsId, HttpServletResponse response) {
        // 设置请求头
        response.setContentType("image/jpg");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        // 生成验证码
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 32, 5);
        // 验证码存入redis
        redisTemplate.opsForValue().set("seckillCaptcha:" + user.getId() + ":" + goodsId, specCaptcha.text(), 60, TimeUnit.SECONDS);
//        System.out.println("seckillCaptcha:" + user.getId() + ":" + goodsId + "->" + specCaptcha.text());
        // 输出图片流
        try {
            specCaptcha.out(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 该方法会在所有Bean属性都装配完毕后自动调用, 可用来进行一些初始化操作
     */
    @Override
    public void afterPropertiesSet() {
        List<SeckillGoodsVo> seckillGoods = goodsService.getSeckillGoods();
        if(CollectionUtils.isEmpty(seckillGoods)){
            return;
        }
        for(SeckillGoodsVo seckillGood : seckillGoods) {
           redisTemplate.opsForValue().set("seckillGoodsStock:" + seckillGood.getId(), seckillGood.getStockCount());
           isEmptyStock.put(seckillGood.getId(), false);
        }
    }
}
