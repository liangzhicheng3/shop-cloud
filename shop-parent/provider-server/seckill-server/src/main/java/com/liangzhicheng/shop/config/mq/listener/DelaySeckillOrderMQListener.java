package com.liangzhicheng.shop.config.mq.listener;

import com.liangzhicheng.shop.common.constant.MQConstants;
import com.liangzhicheng.shop.common.utils.JSONUtil;
import com.liangzhicheng.shop.modules.service.IOrderInfoService;
import com.liangzhicheng.shop.mq.message.DelaySeckillOrderMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@RocketMQMessageListener(
        topic = MQConstants.SECKILL_ORDER_TOPIC,
        selectorExpression = MQConstants.DELAY_SECKILL_ORDER_TAG,
        consumerGroup = MQConstants.DELAY_SECKILL_ORDER_CONSUMER_GROUP
)
@Component
public class DelaySeckillOrderMQListener implements RocketMQListener<DelaySeckillOrderMessage> {

    @Resource
    private IOrderInfoService orderInfoService;

    @Override
    public void onMessage(DelaySeckillOrderMessage message) {
        log.info("[延迟订单] 接收到订单未支付延迟消息：{}", JSONUtil.toJSONString(message));
        //接收延迟消息，检查订单是否超时未支付
        orderInfoService.checkPayTimeout(message.getUserId(), message.getOrderNo(), message.getSeckillId());
    }

}
