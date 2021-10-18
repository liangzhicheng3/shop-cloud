package com.liangzhicheng.shop.modules.service;

import com.liangzhicheng.shop.entity.OrderInfo;

public interface IOrderInfoService {

    /**
     * @description 秒杀方法
     * @param token
     * @param seckillId
     * @param uuid
     * @return String
     */
    void doSeckill(String token, String random, Long seckillId, String uuid);

    /**
     * @description 异步下单
     * @param userId
     * @param seckillId
     * @return String
     */
    String asynOrder(Long userId, Long seckillId);

    /**
     * @description 根据订单编号查询订单信息
     * @param token
     * @param orderNo
     * @return OrderInfo
     */
    OrderInfo get(String token, String orderNo);

    /**
     * @description 检查订单是否超时未支付
     * @param userId
     * @param orderNo
     * @param seckillId
     */
    void checkPayTimeout(Long userId, String orderNo, Long seckillId);

    /**
     * @description 处理订单下单失败业务，回补redis中秒杀商品库存、清除用户下单标识、清除本地售完标识
     * @param userId
     * @param seckillId
     */
    void handleFail(Long userId, Long seckillId);

    /**
     * @description 支付成功业务逻辑处理
     * @param out_trade_no
     * @param trade_no
     */
    void paySuccess(String out_trade_no, String trade_no);

}
