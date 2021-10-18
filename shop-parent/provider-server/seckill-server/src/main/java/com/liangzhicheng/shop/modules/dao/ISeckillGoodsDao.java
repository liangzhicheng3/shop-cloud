package com.liangzhicheng.shop.modules.dao;

import com.liangzhicheng.shop.entity.SeckillGoods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ISeckillGoodsDao {

    @Select("SELECT * FROM t_seckill_goods")
    List<SeckillGoods> list();

    @Select("SELECT * FROM t_seckill_goods WHERE id = #{id}")
    SeckillGoods get(@Param("id") Long id);

    @Update("UPDATE t_seckill_goods SET stock_num = stock_num - 1 WHERE id = #{seckillId} AND stock_num > 0")
    int subStockNum(@Param("seckillId") Long seckillId);

    @Update("UPDATE t_seckill_goods SET stock_num = stock_num + 1 WHERE id = #{seckillId}")
    void rollbackStockNum(@Param("seckillId") Long seckillId);

}
