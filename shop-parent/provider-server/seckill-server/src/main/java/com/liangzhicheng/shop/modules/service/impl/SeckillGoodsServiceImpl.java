package com.liangzhicheng.shop.modules.service.impl;

import com.liangzhicheng.shop.common.basic.ResponseResult;
import com.liangzhicheng.shop.common.enums.SeckillRedisKeyEnum;
import com.liangzhicheng.shop.common.exception.CustomizeException;
import com.liangzhicheng.shop.common.message.SeckillCodeMessage;
import com.liangzhicheng.shop.common.utils.JSONUtil;
import com.liangzhicheng.shop.entity.Goods;
import com.liangzhicheng.shop.entity.SeckillGoods;
import com.liangzhicheng.shop.feign.IGoodsFeignApi;
import com.liangzhicheng.shop.modules.dao.ISeckillGoodsDao;
import com.liangzhicheng.shop.modules.service.ISeckillGoodsService;
import com.liangzhicheng.shop.vo.SeckillGoodsVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SeckillGoodsServiceImpl implements ISeckillGoodsService {

    @Resource
    private ISeckillGoodsDao seckillGoodsDao;
    @Resource
    private IGoodsFeignApi goodsFeignApi;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public List<SeckillGoodsVO> list() {
        //1.获取所有秒杀商品列表
        List<SeckillGoods> seckillGoodsList = seckillGoodsDao.list();
        //2.遍历秒杀商品列表，得到一个商品id列表（备注：一个商品可以存在多个秒杀活动，需要去重）
//        Set<Long> goodsIds = seckillGoodsList.stream().map(SeckillGoods::getGoodsId).collect(Collectors.toSet());
        Set<Long> goodsIds = new HashSet<>(seckillGoodsList.size());
        for(Iterator<SeckillGoods> it = seckillGoodsList.iterator(); it.hasNext();){
            goodsIds.add(it.next().getGoodsId());
        }
        //3.调用远程商品服务，以商品id列表作为参数获取商品列表
        ResponseResult<List<Goods>> responseResult = goodsFeignApi.getByGoodsIds(goodsIds);
        if(responseResult == null || responseResult.isError()){
            throw new CustomizeException(SeckillCodeMessage.SECKILL_SERVER_BUSY);
        }
        List<Goods> goodsList = responseResult.getData();
        //4.遍历商品列表，构建map，以商品id为key，商品对象为value
        Map<Long, Goods> map = new HashMap<>(goodsList.size());
        for(Iterator<Goods> it = goodsList.iterator(); it.hasNext();){
            Goods goods = it.next();
            map.put(goods.getId(), goods);
        }
        //5.遍历秒杀商品列表，根据秒杀商品的商品id，从map中获取到对应的商品对象
        List<SeckillGoodsVO> seckillGoodsVOList = new ArrayList<>(seckillGoodsList.size());
        for(Iterator<SeckillGoods> it = seckillGoodsList.iterator(); it.hasNext();){
            SeckillGoods seckillGoods = it.next();
            //6.使用BeanUtils.copyProperties()方法进行拷贝属性并返回
            SeckillGoodsVO seckillGoodsVO = new SeckillGoodsVO();
            Goods goods = map.get(seckillGoods.getGoodsId());
            if(goods != null){
                BeanUtils.copyProperties(goods, seckillGoodsVO);
            }
            BeanUtils.copyProperties(seckillGoods, seckillGoodsVO);
            seckillGoodsVOList.add(seckillGoodsVO);
        }
        return seckillGoodsVOList;
    }

    @Override
    public List<SeckillGoodsVO> listByCache() {
        List<Object> list = stringRedisTemplate.opsForHash().values(SeckillRedisKeyEnum.SECKILL_GOODS_HASH.join());
        List<SeckillGoodsVO> seckillGoodsVOList = new ArrayList<>(list.size());
        if(!CollectionUtils.isEmpty(list)){
            for(Object object : list) {
                String goodsInfo = (String) object;
                if(!StringUtils.isEmpty(goodsInfo)){
                    SeckillGoodsVO seckillGoodsVO = JSONUtil.parseObject(goodsInfo, SeckillGoodsVO.class);
                    if(seckillGoodsVO != null){
                        String seckillNumStr = (String) stringRedisTemplate.opsForHash().get(
                                SeckillRedisKeyEnum.SECKILL_GOODS_STOCK_HASH.join(), seckillGoodsVO.getId() + "");
                        if(!StringUtils.isEmpty(seckillNumStr)){
                            int seckillNum = Integer.parseInt(seckillNumStr);
                            seckillGoodsVO.setStockNum(seckillNum <= 0 ? 0 : seckillNum);
                        }
                    }
                    seckillGoodsVOList.add(seckillGoodsVO);
                }
            }
        }
        return seckillGoodsVOList;
    }

    @Override
    public SeckillGoodsVO get(Long id) {
        //1.根据id获取秒杀商品对象
        SeckillGoods seckillGoods = seckillGoodsDao.get(id);
        //2.根据秒杀商品对象的商品id调用远程服务获取商品对象
        ResponseResult<List<Goods>> responseResult =
                goodsFeignApi.getByGoodsIds(Collections.singleton(seckillGoods.getGoodsId())); //单个参数构建一个集合对象
        if(responseResult == null && responseResult.isError()){
            return null;
        }
        //3.使用BeanUtils.copyProperties()方法进行对秒杀商品对象和商品对象拷贝属性并返回
        SeckillGoodsVO seckillGoodsVO = new SeckillGoodsVO();
        List<Goods> goodsList = responseResult.getData();
        if(!CollectionUtils.isEmpty(goodsList)){
            BeanUtils.copyProperties(goodsList.get(0), seckillGoodsVO);
        }
        BeanUtils.copyProperties(seckillGoods, seckillGoodsVO);
        return seckillGoodsVO;
    }

    @Override
    public SeckillGoodsVO getByCache(Long id) {
        String goodsInfo = (String) stringRedisTemplate.opsForHash().get(
                SeckillRedisKeyEnum.SECKILL_GOODS_HASH.join(), id + ""
        );
        if(StringUtils.isEmpty(goodsInfo)){
           this.newObject();
        }
        SeckillGoodsVO seckillGoodsVO = JSONUtil.parseObject(goodsInfo, SeckillGoodsVO.class);
        String seckillNumStr = (String) stringRedisTemplate.opsForHash().get(
                SeckillRedisKeyEnum.SECKILL_GOODS_STOCK_HASH.join(), id + ""
        );
        if(seckillGoodsVO == null || StringUtils.isEmpty(seckillNumStr)){
            this.newObject();
        }
        int seckillNum = Integer.parseInt(seckillNumStr);
        seckillGoodsVO.setStockNum(seckillNum <= 0 ? 0 : seckillNum);
        return seckillGoodsVO;
    }

    @Override
    public int subStockNum(Long seckillId) {
        return seckillGoodsDao.subStockNum(seckillId);
    }

    @Override
    public void rollbackStockNum(Long seckillId) {
        seckillGoodsDao.rollbackStockNum(seckillId);
    }

    /**
     * @description 返回空值对象
     * @return SeckillGoodsVO
     */
    private SeckillGoodsVO newObject(){
        return new SeckillGoodsVO();
    }

}
