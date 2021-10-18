package com.liangzhicheng.shop.modules.service;

public interface ISeckillOrderService {

    /**
     * @description 根据用户id和秒杀商品id获取已经下单的秒杀订单
     * @param userId
     * @param seckillId
     * @return Long
     */
    Long getCountByUserIdAndSeckillId(Long userId, Long seckillId);

    /**
     * @description 创建秒杀订单
     * @param orderNo
     * @param userId
     * @param seckillId
     */
    void insertSeckillOrder(String orderNo, Long userId, Long seckillId);

    /**
     * @description 删除秒杀订单
     * @param orderNo
     */
    void deleteSeckillOrder(String orderNo);

}
