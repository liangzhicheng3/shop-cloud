package com.liangzhicheng.shop.config.mq.listener;

import com.liangzhicheng.shop.common.constant.MQConstants;
import com.liangzhicheng.shop.modules.service.impl.OrderInfoServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Slf4j
@RocketMQMessageListener(
        topic = MQConstants.SECKILL_ORDER_TOPIC,
        selectorExpression = MQConstants.CLEAR_STOCK_NUM_OVER_TAG,
        consumerGroup = MQConstants.CLEAR_STOCK_NUM_OVER_CONSUMER_GROUP,
        messageModel = MessageModel.BROADCASTING //广播模式
)
@Component
public class ClearStockNumOverMQListener implements RocketMQListener<Long> {

    @Override
    public void onMessage(Long seckillId) {
        log.info("[广播消息->本地库存售完标识] 接收到清除本地库存售完标识消息，商品秒杀id为{}", seckillId);
        //将本地售完标识设置为false
        OrderInfoServiceImpl.stockOverMap.put(seckillId,false);
    }

}
