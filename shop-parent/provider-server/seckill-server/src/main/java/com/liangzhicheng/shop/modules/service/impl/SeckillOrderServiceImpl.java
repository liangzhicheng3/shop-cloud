package com.liangzhicheng.shop.modules.service.impl;

import com.liangzhicheng.shop.entity.SeckillOrder;
import com.liangzhicheng.shop.modules.dao.ISeckillOrderDao;
import com.liangzhicheng.shop.modules.service.ISeckillOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import javax.annotation.Resource;

@Service
public class SeckillOrderServiceImpl implements ISeckillOrderService {

    @Resource
    private ISeckillOrderDao seckillOrderDao;

    @Override
    public Long getCountByUserIdAndSeckillId(Long userId, Long seckillId) {
        return seckillOrderDao.getCountByUserIdAndSeckillId(userId, seckillId);
    }

    @Override
    public void insertSeckillOrder(String orderNo, Long userId, Long seckillId) {
        seckillOrderDao.insert(new SeckillOrder(orderNo, userId, seckillId));
    }

    @Override
    public void deleteSeckillOrder(String orderNo) {
        seckillOrderDao.delete(orderNo);
    }

}
