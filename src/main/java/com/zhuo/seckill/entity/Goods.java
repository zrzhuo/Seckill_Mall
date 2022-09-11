package com.zhuo.seckill.entity;


import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
public class Goods implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String goodsName;

    private String goodsTitle;

    private String goodsImg;

    private String goodsDetail;

    private BigDecimal goodsPrice;

    /**
     * 商品库存，默认为0，-1则代表没有限制
     */
    private Integer goodsStock;


}
