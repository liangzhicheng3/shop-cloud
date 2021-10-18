package com.liangzhicheng.shop.modules.controller;

import com.liangzhicheng.shop.common.basic.ResponseResult;
import com.liangzhicheng.shop.common.enums.SeckillRedisKeyEnum;
import com.liangzhicheng.shop.common.utils.JSONUtil;
import com.liangzhicheng.shop.modules.service.ISeckillGoodsService;
import com.liangzhicheng.shop.vo.SeckillGoodsVO;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "/seckill/goods")
public class SeckillGoodsController {

    @Resource
    private ISeckillGoodsService seckillGoodsService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping(value = "/list")
    public ResponseResult<List<SeckillGoodsVO>> list(){
//        return ResponseResult.success(seckillGoodsService.list());
        return ResponseResult.success(seckillGoodsService.listByCache());
    }

    @GetMapping(value = "/{id}")
    public ResponseResult<SeckillGoodsVO> get(@PathVariable("id") Long id){
//        SeckillGoodsVO seckillGoodsVO = seckillGoodsService.get(id);
        SeckillGoodsVO seckillGoodsVO = seckillGoodsService.getByCache(id);
        return ResponseResult.success(seckillGoodsVO);
    }

    @RequestMapping(value = "/initData")
    public ResponseResult<?> initData(){
        List<SeckillGoodsVO> list = seckillGoodsService.list();
        if(list != null && !list.isEmpty()){
            for(SeckillGoodsVO seckillGoodsVO : list){
                //初始化将秒杀商品信息缓存，数据预热
                stringRedisTemplate.opsForHash().put(
                        SeckillRedisKeyEnum.SECKILL_GOODS_HASH.join(),
                        seckillGoodsVO.getId() + "",
                        JSONUtil.toJSONString(seckillGoodsVO)
                );
                //初始化将秒杀商品库存缓存，预减数据预热
                stringRedisTemplate.opsForHash().put(
                        SeckillRedisKeyEnum.SECKILL_GOODS_STOCK_HASH.join(),
                        seckillGoodsVO.getId() + "",
                        seckillGoodsVO.getStockNum() + ""
                );
            }
        }
        return ResponseResult.success();
    }

}
