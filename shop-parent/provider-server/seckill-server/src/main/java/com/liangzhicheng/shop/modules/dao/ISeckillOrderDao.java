package com.liangzhicheng.shop.modules.dao;

import com.liangzhicheng.shop.entity.SeckillOrder;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ISeckillOrderDao {

    @Select("SELECT COUNT(*) FROM t_seckill_order WHERE user_id = #{userId} AND seckill_id = #{seckillId}")
    Long getCountByUserIdAndSeckillId(@Param("userId") Long userId, @Param("seckillId") Long seckillId);

    @Insert("INSERT INTO t_seckill_order (order_no, user_id, seckill_id) VALUES (#{orderNo}, #{userId}, #{seckillId})")
    void insert(SeckillOrder seckillOrder);

    @Delete("DELETE FROM t_seckill_order WHERE order_no = #{orderNo}")
    void delete(@Param("orderNo") String orderNo);

}
