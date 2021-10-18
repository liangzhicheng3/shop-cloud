package com.liangzhicheng.shop.feign;

import com.liangzhicheng.shop.common.basic.ResponseResult;
import com.liangzhicheng.shop.entity.Goods;
import com.liangzhicheng.shop.feign.hystrix.GoodsFeignHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@FeignClient(name = "goods-server", fallback = GoodsFeignHystrix.class) //fallback 降级
public interface IGoodsFeignApi {

    /**
     * @description 根据商品id列表获取商品信息
     * @param goodsIds
     * @return ResponseResult<List<Goods>>
     */
    @PostMapping(value = "getByGoodsIds")
    ResponseResult<List<Goods>> getByGoodsIds(@RequestParam("goodsIds") Set<Long> goodsIds);

}
