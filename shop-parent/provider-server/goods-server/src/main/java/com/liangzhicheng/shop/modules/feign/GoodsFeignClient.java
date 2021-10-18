package com.liangzhicheng.shop.modules.feign;

import com.liangzhicheng.shop.common.basic.ResponseResult;
import com.liangzhicheng.shop.entity.Goods;
import com.liangzhicheng.shop.feign.IGoodsFeignApi;
import com.liangzhicheng.shop.modules.service.IGoodsService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@RestController
public class GoodsFeignClient implements IGoodsFeignApi {

    @Resource
    private IGoodsService goodsService;

    @Override
    public ResponseResult<List<Goods>> getByGoodsIds(Set<Long> goodsIds) {
        return ResponseResult.success(goodsService.getByGoodsIds(goodsIds));
    }

}
