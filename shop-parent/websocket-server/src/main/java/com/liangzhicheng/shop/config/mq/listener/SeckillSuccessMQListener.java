package com.liangzhicheng.shop.config.mq.listener;

import com.liangzhicheng.shop.common.basic.ResponseResult;
import com.liangzhicheng.shop.common.constant.MQConstants;
import com.liangzhicheng.shop.common.utils.JSONUtil;
import com.liangzhicheng.shop.mq.message.SeckillSuccessMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Slf4j
@RocketMQMessageListener(
        topic = MQConstants.SECKILL_ORDER_TOPIC,
        selectorExpression = MQConstants.SECKILL_SUCCESS_TAG,
        consumerGroup = MQConstants.SECKILL_SUCCESS_CONSUMER_GROUP
)
@Component
public class SeckillSuccessMQListener extends CommonMQListener implements RocketMQListener<SeckillSuccessMessage> {

    private String logTag = "[秒杀成功监听器]";

    @Override
    public void onMessage(SeckillSuccessMessage message) {
        log.info("{} 接收到秒杀订单成功消息：{}", logTag, JSONUtil.toJSONString(message));
        /**
         * 问题：1.点击秒杀按钮，因为创建订单和建立websocket连接是异步进行，会出现订单未创建，建立连接中获取客户端，获取为空
         *      2.进入秒杀界面就建立连接，此时websocket服务压力增大，频繁的建立，断开操作
         * 解决：使用重试机制
         */
        super.sendMessage(message.getUuid(), ResponseResult.success(message));
    }

}
