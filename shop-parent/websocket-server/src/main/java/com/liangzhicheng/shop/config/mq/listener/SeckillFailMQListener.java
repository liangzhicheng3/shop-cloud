package com.liangzhicheng.shop.config.mq.listener;

import com.liangzhicheng.shop.common.basic.ResponseResult;
import com.liangzhicheng.shop.common.constant.MQConstants;
import com.liangzhicheng.shop.common.utils.JSONUtil;
import com.liangzhicheng.shop.mq.message.SeckillFailMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Slf4j
@RocketMQMessageListener(
        topic = MQConstants.SECKILL_ORDER_TOPIC,
        selectorExpression = MQConstants.SECKILL_FAIL_TAG,
        consumerGroup = MQConstants.SECKILL_FAIL_CONSUMER_GROUP
)
@Component
public class SeckillFailMQListener extends CommonMQListener implements RocketMQListener<SeckillFailMessage> {

    private String logTag = "[秒杀失败监听器]";

    @Override
    public void onMessage(SeckillFailMessage message) {
        log.info("{} 接收到秒杀订单失败消息：{}", logTag, JSONUtil.toJSONString(message));
        super.sendMessage(message.getUuid(), ResponseResult.error(message.getCodeMessage()));
    }

}
