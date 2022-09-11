package com.zhuo.seckill.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String userId;

    private Long goodsId;

    private String deliveryAddressId;

    private String goodsName;

    private Integer goodsCount;

    private BigDecimal goodsPrice;

    /**
     * 1为pc, 2为android，3为ios
     */
    private Integer orderChannel;

    /**
     * 订单状态：0未支付，1已支付, 2已发货, 3已收货, 4已退款, 5已完成
     */
    private Integer status;

    private Date createDate;

    private Date payDate;
}
