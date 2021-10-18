package com.liangzhicheng.shop.modules.service;

import com.liangzhicheng.shop.entity.Goods;

import java.util.List;
import java.util.Set;

public interface IGoodsService {

    /**
     * @description 根据商品id列表获取商品信息
     * @param goodsIds
     * @return List<Goods>
     */
    List<Goods> getByGoodsIds(Set<Long> goodsIds);

}
