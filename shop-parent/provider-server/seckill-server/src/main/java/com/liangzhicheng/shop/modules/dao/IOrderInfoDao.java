package com.liangzhicheng.shop.modules.dao;

import com.liangzhicheng.shop.entity.OrderInfo;
import org.apache.ibatis.annotations.*;

@Mapper
public interface IOrderInfoDao {

    @Insert("INSERT INTO t_order_info " +
            "(order_no, " +
            "user_id, " +
            "goods_id, " +
            "delivery_addr_id, " +
            "goods_name, " +
            "goods_image, " +
            "goods_num, " +
            "goods_price, " +
            "seckill_price, " +
            "status, " +
            "pay_time, " +
            "create_time) " +
            "VALUES " +
            "(#{orderNo}, " +
            "#{userId}, " +
            "#{goodsId}, " +
            "#{deliveryAddrId}, " +
            "#{goodsName}, " +
            "#{goodsImage}, " +
            "#{goodsNum}, " +
            "#{goodsPrice}, " +
            "#{seckillPrice}, " +
            "#{status}, " +
            "#{payTime}, " +
            "#{createTime})")
    void insert(OrderInfo orderInfo);

    @Select("SELECT * FROM t_order_info WHERE user_id = #{userId} AND order_no = #{orderNo}")
    OrderInfo get(@Param("userId") Long userId, @Param("orderNo") String orderNo);

    @Update("UPDATE t_order_info SET status = 3 WHERE order_no = #{orderNo} AND status = 0")
    int updateOrderStatus(@Param("orderNo") String orderNo);

    @Update("UPDATE t_order_info SET status = 1, pay_time = SYSDATE() WHERE order_no = #{out_trade_no} and status = 0")
    void updatePayStatus(@Param("out_trade_no") String out_trade_no, @Param("trade_no") String trade_no);

}
