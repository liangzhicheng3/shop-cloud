package com.liangzhicheng.shop.modules.service;

import com.liangzhicheng.shop.vo.SeckillGoodsVO;

import java.util.List;

public interface ISeckillGoodsService {

    /**
     * @description 秒杀商品列表
     * @return List<SeckillGoodsVO>
     */
    List<SeckillGoodsVO> list();

    /**
     * @description 从缓存中获取秒杀商品列表
     * @return List<SeckillGoodsVO>
     */
    List<SeckillGoodsVO> listByCache();

    /**
     * @description 获取秒杀商品详情
     * @param id
     * @return SeckillGoodsVO
     */
    SeckillGoodsVO get(Long id);

    /**
     * @description 从缓存中获取秒杀商品信息
     * @return SeckillGoodsVO
     */
    SeckillGoodsVO getByCache(Long id);

    /**
     * @description 扣减库存
     * @param seckillId
     * @return int
     */
    int subStockNum(Long seckillId);

    /**
     * @description 增加库存
     * @param seckillId
     */
    void rollbackStockNum(Long seckillId);

}
