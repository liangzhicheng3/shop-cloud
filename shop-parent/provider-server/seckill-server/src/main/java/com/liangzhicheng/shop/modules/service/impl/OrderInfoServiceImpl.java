package com.liangzhicheng.shop.modules.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.liangzhicheng.shop.common.constant.MQConstants;
import com.liangzhicheng.shop.common.enums.SeckillRedisKeyEnum;
import com.liangzhicheng.shop.common.enums.UserRedisKeyEnum;
import com.liangzhicheng.shop.common.exception.CustomizeException;
import com.liangzhicheng.shop.common.message.SeckillCodeMessage;
import com.liangzhicheng.shop.common.utils.AssertUtil;
import com.liangzhicheng.shop.common.utils.JSONUtil;
import com.liangzhicheng.shop.common.utils.SnowFlakeUtil;
import com.liangzhicheng.shop.config.alipay.AlipayProperties;
import com.liangzhicheng.shop.config.mq.DefaultSeckillOrderSendCallback;
import com.liangzhicheng.shop.entity.OrderInfo;
import com.liangzhicheng.shop.entity.User;
import com.liangzhicheng.shop.modules.dao.IOrderInfoDao;
import com.liangzhicheng.shop.modules.service.IOrderInfoService;
import com.liangzhicheng.shop.modules.service.ISeckillGoodsService;
import com.liangzhicheng.shop.modules.service.ISeckillOrderService;
import com.liangzhicheng.shop.mq.message.CreateSeckillOrderMessage;
import com.liangzhicheng.shop.vo.SeckillGoodsVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class OrderInfoServiceImpl implements IOrderInfoService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private ISeckillGoodsService seckillGoodsService;
    @Resource
    private ISeckillOrderService seckillOrderService;
    @Resource
    private IOrderInfoDao orderInfoDao;
    @Resource
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 本地库存售完标识map
     */
    public static final ConcurrentHashMap<Long, Boolean> stockOverMap = new ConcurrentHashMap<>();

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doSeckill(String token, String random, Long seckillId, String uuid) {
        //查询售完标识
        Boolean flag = stockOverMap.get(seckillId);
        AssertUtil.isFalse(flag != null && flag, SeckillCodeMessage.UNDERSTOCK_ERROR);
        //1.判断用户是否登录
        User user = this.getUserByToken(token);
        AssertUtil.notNull(user, SeckillCodeMessage.NOT_LOGIN);
        //校验请求url合法性
        String randomByCache = stringRedisTemplate.opsForValue().get(
                SeckillRedisKeyEnum.SECKILL_RANDOM_PATH.join(user.getId() + "", seckillId + "")
        );
        AssertUtil.isFalse(!random.equals(randomByCache), SeckillCodeMessage.OPERATION_ERROR);
        //2.根据秒杀商品id获取秒杀商品信息
//        SeckillGoodsVO seckillGoodsVO = seckillGoodsService.get(seckillId);
        SeckillGoodsVO seckillGoodsVO = seckillGoodsService.getByCache(seckillId);
        AssertUtil.notNull(seckillGoodsVO, SeckillCodeMessage.SECKILL_SERVER_BUSY);
        this.validate(user.getId(), seckillGoodsVO);
        //将MySQL插入操作抽离，修改为异步下单，发送MQ消息
        rocketMQTemplate.asyncSend(
                MQConstants.CREATE_SECKILL_DEST,
                new CreateSeckillOrderMessage(user.getId(), seckillId, uuid),
                new DefaultSeckillOrderSendCallback(MQConstants.CREATE_ORDER_TAG)
        );
    }

    @Override
    public OrderInfo get(String token, String orderNo) {
        User user = this.getUserByToken(token);
        AssertUtil.notNull(user, SeckillCodeMessage.NOT_LOGIN);
        return orderInfoDao.get(user.getId(), orderNo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String asynOrder(Long userId, Long seckillId){
        //2.根据秒杀商品id获取秒杀商品信息
//        SeckillGoodsVO seckillGoodsVO = seckillGoodsService.get(seckillId);
        SeckillGoodsVO seckillGoodsVO = seckillGoodsService.getByCache(seckillId);
        AssertUtil.notNull(seckillGoodsVO, SeckillCodeMessage.SECKILL_SERVER_BUSY);
        //6.扣减库存，再次判断库存是否足够
        int rows = seckillGoodsService.subStockNum(seckillId);
        AssertUtil.isFalse(rows <= 0, SeckillCodeMessage.UNDERSTOCK_ERROR);
        //7.创建订单
        String orderNo = this.insertOrder(userId, seckillGoodsVO);
        //8.创建秒杀订单
        seckillOrderService.insertSeckillOrder(orderNo, userId, seckillId);
        return orderNo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkPayTimeout(Long userId, String orderNo, Long seckillId) {
        //1.检查订单状态，如果未支付，则修改订单状态
        int rows = orderInfoDao.updateOrderStatus(orderNo);
        if(rows > 0){
            //2.如果未支付，回补数据库库存
            seckillGoodsService.rollbackStockNum(seckillId);
            //3.数据库秒杀商品与库存同步到redis中
            SeckillGoodsVO seckillGoodsVO = seckillGoodsService.get(seckillId);
            stringRedisTemplate.opsForHash().put(
                    SeckillRedisKeyEnum.SECKILL_GOODS_HASH.join(),
                    seckillGoodsVO.getId() + "",
                    JSONUtil.toJSONString(seckillGoodsVO)
            );
            stringRedisTemplate.opsForHash().put(
                    SeckillRedisKeyEnum.SECKILL_GOODS_STOCK_HASH.join(),
                    seckillId + "",
                    seckillGoodsVO.getStockNum() + ""
            );
            //4.删除秒杀订单
            seckillOrderService.deleteSeckillOrder(orderNo);
            //5.清除用户已下单标识
            stringRedisTemplate.delete(SeckillRedisKeyEnum.USER_SECKILL_RECORD.join(seckillId + "", userId + ""));
            //6.发送广播消息，清除本地售完标识（可多个消费者消费，集群秒杀服务，多个实例，解决分布式缓存同步问题）
            rocketMQTemplate.asyncSend(
                    MQConstants.CLEAR_STOCK_NUM_OVER_DEST,
                    seckillId,
                    new DefaultSeckillOrderSendCallback(MQConstants.CLEAR_STOCK_NUM_OVER_TAG)
            );
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleFail(Long userId, Long seckillId) {
        SeckillGoodsVO seckillGoodsVO = seckillGoodsService.get(seckillId);
        stringRedisTemplate.opsForHash().put(
                SeckillRedisKeyEnum.SECKILL_GOODS_HASH.join(),
                seckillGoodsVO.getId() + "",
                JSONUtil.toJSONString(seckillGoodsVO)
        );
        stringRedisTemplate.opsForHash().put(
                SeckillRedisKeyEnum.SECKILL_GOODS_STOCK_HASH.join(),
                seckillId + "",
                seckillGoodsVO.getStockNum() + ""
        );
        stringRedisTemplate.delete(SeckillRedisKeyEnum.USER_SECKILL_RECORD.join(seckillGoodsVO.getId() + "", userId + ""));
        rocketMQTemplate.asyncSend(
                MQConstants.CLEAR_STOCK_NUM_OVER_DEST,
                seckillId,
                new DefaultSeckillOrderSendCallback(MQConstants.CLEAR_STOCK_NUM_OVER_TAG)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void paySuccess(String out_trade_no, String trade_no) {
        orderInfoDao.updatePayStatus(out_trade_no, trade_no);
    }

    /**
     * @description 从redis缓存中根据token获取用户（不用调用远程服务获取用户，为了提高秒杀功能性能）
     * @param token
     * @return User
     */
    private User getUserByToken(String token) {
        return JSONUtil.parseObject(stringRedisTemplate.opsForValue()
                .get(UserRedisKeyEnum.USER_LOGIN_TOKEN.join(token)), User.class);
    }

    /**
     * @description 执行秒杀前的前置参数校验
     * @param userId
     * @param seckillGoodsVO
     */
    private void validate(Long userId, SeckillGoodsVO seckillGoodsVO){
        //3.判断时间
        Date currentTime = new Date();
        //如果当前时间小于活动开始时间成立，提示活动尚未开始
        AssertUtil.isFalse(currentTime.compareTo(seckillGoodsVO.getStartTime()) < 0, SeckillCodeMessage.ACTIVITY_NOT_START);
        //如果当前时间大于活动结束时间成立，提示活动已结束
        AssertUtil.isFalse(currentTime.compareTo(seckillGoodsVO.getEndTime()) > 0, SeckillCodeMessage.ACTIVITY_OVER);
        //4.判断用户是否重复下单
//        Long count = seckillOrderService.getCountByUserIdAndSeckillId(userId, seckillGoodsVO.getId());
//        AssertUtil.isFalse(count.intValue() > 0, SeckillCodeMessage.REPEAT_ORDER_ERROR); //存在并发重复下单
        Boolean isExist = stringRedisTemplate.opsForValue().setIfAbsent(
                SeckillRedisKeyEnum.USER_SECKILL_RECORD.join(seckillGoodsVO.getId() + "", userId + ""), "1");
        AssertUtil.isTrue(isExist == null || isExist, SeckillCodeMessage.REPEAT_ORDER_ERROR);
        //5.判断库存是否足够
//        AssertUtil.isFalse(seckillGoodsVO.getStockNum() <= 0, SeckillCodeMessage.UNDERSTOCK_ERROR); //存在并发超卖
        Long num = stringRedisTemplate.opsForHash().increment(
                SeckillRedisKeyEnum.SECKILL_GOODS_STOCK_HASH.join(), seckillGoodsVO.getId() + "", -1);
        if(num.intValue() < 0){
            //清除用户已下单标识
            stringRedisTemplate.delete(SeckillRedisKeyEnum.USER_SECKILL_RECORD.join(seckillGoodsVO.getId() + "", userId + ""));
            //新增售完标识
            stockOverMap.put(seckillGoodsVO.getId(), true);
            throw new CustomizeException(SeckillCodeMessage.UNDERSTOCK_ERROR);
        }
    }

    /**
     * @description 创建订单
     * @param userId
     * @param seckillGoodsVO
     * @return String
     */
    private String insertOrder(Long userId, SeckillGoodsVO seckillGoodsVO) {
        OrderInfo orderInfo = new OrderInfo();
        BeanUtils.copyProperties(seckillGoodsVO, orderInfo);
        String orderNo = SnowFlakeUtil.get().nextId() + "";
        orderInfo.setOrderNo(orderNo)
                .setUserId(userId)
                .setGoodsNum(1)
                .setCreateTime(new Date());
        orderInfoDao.insert(orderInfo);
        return orderNo;
    }

}
