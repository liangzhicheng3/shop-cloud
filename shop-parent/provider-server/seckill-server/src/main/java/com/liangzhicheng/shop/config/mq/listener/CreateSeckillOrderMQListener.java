package com.liangzhicheng.shop.config.mq.listener;

import com.liangzhicheng.shop.common.constant.MQConstants;
import com.liangzhicheng.shop.common.exception.CustomizeException;
import com.liangzhicheng.shop.common.message.CodeMessage;
import com.liangzhicheng.shop.common.message.SeckillCodeMessage;
import com.liangzhicheng.shop.common.utils.JSONUtil;
import com.liangzhicheng.shop.config.mq.DefaultSeckillOrderSendCallback;
import com.liangzhicheng.shop.modules.service.IOrderInfoService;
import com.liangzhicheng.shop.mq.message.CreateSeckillOrderMessage;
import com.liangzhicheng.shop.mq.message.DelaySeckillOrderMessage;
import com.liangzhicheng.shop.mq.message.SeckillFailMessage;
import com.liangzhicheng.shop.mq.message.SeckillSuccessMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@RocketMQMessageListener(
        topic = MQConstants.SECKILL_ORDER_TOPIC,
        selectorExpression = MQConstants.CREATE_ORDER_TAG,
        consumerGroup = MQConstants.CREATE_ORDER_CONSUMER_GROUP
)
@Component
public class CreateSeckillOrderMQListener implements RocketMQListener<CreateSeckillOrderMessage> {

    @Resource
    private IOrderInfoService orderInfoService;
    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public void onMessage(CreateSeckillOrderMessage message) {
        log.info("[创建订单] 接收到创建秒杀订单消息：{}", JSONUtil.toJSONString(message));
        Long userId = message.getUserId(); //用户id
        Long seckillId = message.getSeckillId(); //秒杀商品id
        String uuid = message.getUuid(); //用户随机id
        try {
            //创建订单消息，进行真正的持久化操作
            String orderNo = orderInfoService.asynOrder(userId, seckillId);
            //订单创建成功，发送订单创建成功消息，通知客户端
            rocketMQTemplate.asyncSend(
                    MQConstants.SECKILL_SUCCESS_DEST,
                    new SeckillSuccessMessage(orderNo, uuid),
                    new DefaultSeckillOrderSendCallback(MQConstants.SECKILL_SUCCESS_TAG)
            );
            //发送延迟消息，检查订单是否超时未支付
            //delayLevel-> 1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
            rocketMQTemplate.asyncSend(
                    MQConstants.DELAY_SECKILL_ORDER_DEST,
                    new GenericMessage<>(new DelaySeckillOrderMessage(userId, orderNo, seckillId)),
                    new DefaultSeckillOrderSendCallback(MQConstants.DELAY_SECKILL_ORDER_TAG),
                    1000,
                    3
            );
        } catch (Exception e) {
            CustomizeException customizeException = null;
            CodeMessage errorMessage = SeckillCodeMessage.SECKILL_SERVER_BUSY;
            if(e instanceof CustomizeException){
                customizeException = (CustomizeException) e;
                errorMessage = customizeException.getCodeMessage();
            }
            //订单创建失败
            //通知用户
            rocketMQTemplate.asyncSend(MQConstants.SECKILL_FAIL_DEST,
                    new SeckillFailMessage(errorMessage, uuid),
                    new DefaultSeckillOrderSendCallback(MQConstants.SECKILL_FAIL_TAG)
            );
            //处理订单下单失败业务，回补redis中秒杀商品库存、清除用户下单标识、清除本地售完标识
            orderInfoService.handleFail(userId, seckillId);
        }
    }

}
