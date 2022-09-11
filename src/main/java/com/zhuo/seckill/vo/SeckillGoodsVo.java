package com.zhuo.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeckillGoodsVo {
    private Long id;

    private String goodsName;

    private String goodsImg;

    private BigDecimal goodsPrice;

    private BigDecimal seckillPrice;

    private Integer stockCount;

    private String goodsDetail;

    private Date startDate;

    private Date endDate;
}
