package com.zhuo.seckill.entity;


import lombok.Data;

import java.io.Serializable;


@Data
public class SeckillOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String userId;

    private Long goodsId;

    private Long orderId;

}
