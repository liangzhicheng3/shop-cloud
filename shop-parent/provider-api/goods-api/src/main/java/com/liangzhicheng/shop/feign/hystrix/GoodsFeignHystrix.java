package com.liangzhicheng.shop.feign.hystrix;

import com.liangzhicheng.shop.common.basic.ResponseResult;
import com.liangzhicheng.shop.entity.Goods;
import com.liangzhicheng.shop.feign.IGoodsFeignApi;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class GoodsFeignHystrix implements IGoodsFeignApi {

    @Override
    public ResponseResult<List<Goods>> getByGoodsIds(Set<Long> goodsIds) {
        return null;
    }

}
