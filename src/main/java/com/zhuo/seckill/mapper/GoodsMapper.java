package com.zhuo.seckill.mapper;

import com.zhuo.seckill.entity.Goods;
import com.zhuo.seckill.vo.SeckillGoodsVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsMapper {

    /**
     * 通过id查询商品
     */
    Goods selectGoodsById(@Param("id") Long goodsId);

    /**
     * 查询所有秒杀商品
     */
    List<SeckillGoodsVo> selectAllSeckillGoods();

    /**
     * 通过id查询秒杀商品
     */
    SeckillGoodsVo selectSeckillGoodsById(@Param("id") Long goodsId);

    /**
     * 指定id的商品减一单位库存
     */
    int decreaseStock(@Param("id") Long goodsId);


}
