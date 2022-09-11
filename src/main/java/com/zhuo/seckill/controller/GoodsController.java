package com.zhuo.seckill.controller;

import com.zhuo.seckill.entity.User;
import com.zhuo.seckill.service.GoodsService;
import com.zhuo.seckill.service.UserService;
import com.zhuo.seckill.vo.SeckillGoodsDetailVo;
import com.zhuo.seckill.vo.SeckillGoodsVo;
import com.zhuo.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private UserService userService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;


    /**
     * 页面缓存
     */
    @RequestMapping(value = "/toList", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toList(Model model, User user, HttpServletRequest request, HttpServletResponse response){
        ValueOperations<String, Object> redisOperations = redisTemplate.opsForValue();
        String html = (String) redisOperations.get("seckillGoodsList"); // 尝试从redis中获取goodsList页面
        // 成功从redis中获取到页面, 直接返回
        if(StringUtils.hasText(html)){
            return html;
        }
        // 未能从redis中获取到页面, 则从数据库查询数据, 并进行手动渲染
        List<SeckillGoodsVo> seckillGoods = goodsService.getSeckillGoods(); // 从数据库查询数据
        model.addAttribute("seckillGoods", seckillGoods); // 数据存入model
        model.addAttribute("user", user);
        Map<String, Object> variables = model.asMap(); // model转为Map
        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale(), variables);
        html = thymeleafViewResolver.getTemplateEngine().process("seckillGoodsList", context); // 渲染页面
        if(StringUtils.hasText(html)){
            redisOperations.set("seckillGoodsList", html, 30, TimeUnit.SECONDS); // 页面存入redis, 并设置超时时间
        }
        return html;
    }

    /**
     * 未优化
     */
    @RequestMapping("/toDetail1/{goodsId}")
    public String toDetail1(Model model,  User user, @PathVariable Long goodsId){
        SeckillGoodsVo seckillGoods = goodsService.getSeckillGoodsById(goodsId);
        Date startDate = seckillGoods.getStartDate();
        Date endDate = seckillGoods.getEndDate();
        Date nowDate = new Date();
        int seckillStatus; // 当前秒杀商品的状态
        int remainSeconds; // 秒杀开始倒计时
        if(nowDate.before(startDate)){
            seckillStatus = 0; // 秒杀未开始
            remainSeconds = (int)((startDate.getTime() - nowDate.getTime()) / 1000);
        }else if(nowDate.after(endDate)){
            seckillStatus = 2; // 秒杀已结束
            remainSeconds = -1;
        }else {
            seckillStatus = 1; // 秒杀进行中
            remainSeconds = 0;
        }
        model.addAttribute("seckillGoods", seckillGoods);
        model.addAttribute("seckillStatus", seckillStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        model.addAttribute("user", user);
        return "seckillGoodsDetail";
    }

    /**
     * 页面缓存
     */
    @RequestMapping(value = "/toDetail2/{goodsId}", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toDetail2(Model model,  User user, @PathVariable Long goodsId, HttpServletRequest request, HttpServletResponse response){
        ValueOperations<String, Object> redisOperations = redisTemplate.opsForValue();
        String html = (String) redisOperations.get("seckillGoodsDetail:" + goodsId); // 尝试从redis中获取goodsDetail页面
        // 成功从redis中获取到页面, 直接返回
        if(StringUtils.hasText(html)){
            return html;
        }
        // 未能从redis中获取到页面, 则从数据库查询数据, 并进行手动渲染
        SeckillGoodsVo seckillGoods = goodsService.getSeckillGoodsById(goodsId);
        Date startDate = seckillGoods.getStartDate();
        Date endDate = seckillGoods.getEndDate();
        Date nowDate = new Date();
        int seckillStatus; // 当前秒杀商品的状态
        int remainSeconds; // 秒杀开始倒计时
        if(nowDate.before(startDate)){
            seckillStatus = 0; // 秒杀未开始
            remainSeconds = (int)((startDate.getTime() - nowDate.getTime()) / 1000);
        }else if(nowDate.after(endDate)){
            seckillStatus = 2; // 秒杀已结束
            remainSeconds = -1;
        }else {
            seckillStatus = 1; // 秒杀进行中
            remainSeconds = 0;
        }
        model.addAttribute("seckillGoods", seckillGoods);
        model.addAttribute("seckillStatus", seckillStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        model.addAttribute("user", user);
        Map<String, Object> variables = model.asMap(); // model转为Map
        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale(), variables);
        html = thymeleafViewResolver.getTemplateEngine().process("seckillGoodsDetail", context); // 渲染页面
        if(StringUtils.hasText(html)){
            redisOperations.set("seckillGoodsDetail:" + goodsId, html, 30, TimeUnit.SECONDS); // 页面存入redis, 并设置超时时间
        }
        return html;
    }


    /**
     * 对象缓存 + 页面静态化
     */
    @RequestMapping( "/toDetail3/{goodsId}")
    @ResponseBody
    public RespBean toDetail3(User user, @PathVariable Long goodsId){
        ValueOperations<String, Object> redisOperations = redisTemplate.opsForValue();
        // 尝试从redis中获取商品数据
        SeckillGoodsVo seckillGoods = (SeckillGoodsVo) redisOperations.get("seckillGoods:" + goodsId);
        if(seckillGoods == null){
            // 未能从redis中获取到商品数据, 则从数据库中查询数据
            seckillGoods = goodsService.getSeckillGoodsById(goodsId);
            // 将查询到商品数据缓存到redis中
            redisOperations.set("seckillGoods:" + goodsId, seckillGoods, 60, TimeUnit.SECONDS);
        }
        Date startDate = seckillGoods.getStartDate();
        Date endDate = seckillGoods.getEndDate();
        Date nowDate = new Date();
        int seckillStatus; // 当前秒杀商品的状态
        int remainSeconds; // 秒杀开始倒计时
        if(nowDate.before(startDate)){
            seckillStatus = 0; // 秒杀未开始
            remainSeconds = (int)((startDate.getTime() - nowDate.getTime()) / 1000);
        }else if(nowDate.after(endDate)){
            seckillStatus = 2; // 秒杀已结束
            remainSeconds = -1;
        }else {
            seckillStatus = 1; // 秒杀进行中
            remainSeconds = 0;
        }
        SeckillGoodsDetailVo seckillGoodsDetail = new SeckillGoodsDetailVo(user, seckillGoods, seckillStatus, remainSeconds);
        return RespBean.success(seckillGoodsDetail);
    }
}
