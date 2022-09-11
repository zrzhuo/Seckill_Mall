package com.zhuo.seckill.vo;

import com.zhuo.seckill.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeckillGoodsDetailVo {
    private User user;
    private SeckillGoodsVo seckillGoodsVo;
    private int seckillStatus;
    private int remainSeconds;
}
