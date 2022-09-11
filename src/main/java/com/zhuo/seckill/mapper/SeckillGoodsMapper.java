package com.zhuo.seckill.mapper;

import org.apache.ibatis.annotations.Param;

public interface SeckillGoodsMapper {

    /**
     * 指定id的商品减一单位库存
     */
    int decreaseStock(@Param("id") Long id);

}
