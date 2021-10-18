package com.liangzhicheng.shop.config.mq;

import com.liangzhicheng.shop.common.constant.MQConstants;
import com.liangzhicheng.shop.common.utils.JSONUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;

@Slf4j
@Data
public class DefaultSeckillOrderSendCallback implements SendCallback {

    /**
     * 标识
     */
    private String flag;

    public DefaultSeckillOrderSendCallback(String flag){
        this.flag = flag;
    }

    @Override
    public void onSuccess(SendResult sendResult) {
        String content = JSONUtil.toJSONString(sendResult);
        switch(this.flag){
            case MQConstants.CREATE_ORDER_TAG:
                log.info("[异步下单] 发送创建秒杀订单消息：{}", content);
                break;
            case MQConstants.SECKILL_SUCCESS_TAG:
                log.info("[异步下单] 发送秒杀订单成功消息：{}", content);
                break;
            case MQConstants.SECKILL_FAIL_TAG:
                log.info("[异步下单] 发送秒杀订单失败消息：{}", content);
                break;
            case MQConstants.DELAY_SECKILL_ORDER_TAG:
                log.info("[延迟订单] 发送订单未支付延迟消息：{}", content);
                break;
            case MQConstants.CLEAR_STOCK_NUM_OVER_TAG:
                log.info("[广播消息->本地库存售完标识] 发送清除本地库存售完标识消息：{}", content);
                break;
        }
    }

    @Override
    public void onException(Throwable e) {
        log.error("[订单服务] 发送消息失败：{}", e);
    }

}
