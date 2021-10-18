package com.liangzhicheng.shop.modules.service.impl;

import com.liangzhicheng.shop.entity.Goods;
import com.liangzhicheng.shop.modules.dao.IGoodsDao;
import com.liangzhicheng.shop.modules.service.IGoodsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@Service
public class GoodsServiceImpl implements IGoodsService {

    @Resource
    private IGoodsDao goodsDao;

    @Override
    public List<Goods> getByGoodsIds(Set<Long> goodsIds) {
        return goodsDao.getByGoodsIds(goodsIds);
    }

}
