package com.zhuo.seckill.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
public class SeckillGoods implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long id;

    private Long goodsId;

    private BigDecimal seckillPrice;

    private Integer stockCount;

    private Date startDate;

    private Date endDate;


}
