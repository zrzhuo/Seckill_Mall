package com.zhuo.seckill.vo;

import com.zhuo.seckill.entity.Goods;
import com.zhuo.seckill.entity.Order;
import com.zhuo.seckill.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailVo {
    private Order order;
    private User user;
    private Goods goods;
}
