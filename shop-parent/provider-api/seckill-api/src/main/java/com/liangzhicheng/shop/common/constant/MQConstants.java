package com.liangzhicheng.shop.common.constant;

public abstract class MQConstants extends Constants {

    /**
     * mq:秒杀订单主题
     */
    public static final String SECKILL_ORDER_TOPIC = "seckill_order_topic";
    /**
     * mq:创建订单标签
     */
    public static final String CREATE_ORDER_TAG = "create_order_tag";
    /**
     * mq:创建订单消费者组
     */
    public static final String CREATE_ORDER_CONSUMER_GROUP = "create_order_consumer_group";
    /**
     * mq:创建订单消息目的地
     */
    public static final String CREATE_SECKILL_DEST = SECKILL_ORDER_TOPIC + ":" + CREATE_ORDER_TAG;
    /**
     * mq:秒杀成功标签
     */
    public static final String SECKILL_SUCCESS_TAG = "seckill_success_tag";
    /**
     * mq:秒杀成功消费者组
     */
    public static final String SECKILL_SUCCESS_CONSUMER_GROUP = "seckill_success_consumer_group";
    /**
     * mq:秒杀成功消息目的地
     */
    public static final String SECKILL_SUCCESS_DEST = SECKILL_ORDER_TOPIC + ":" + SECKILL_SUCCESS_TAG;
    /**
     * mq:秒杀失败标签
     */
    public static final String SECKILL_FAIL_TAG = "seckill_fail_tag";
    /**
     * mq:秒杀失败消费者组
     */
    public static final String SECKILL_FAIL_CONSUMER_GROUP = "seckill_fail_consumer_group";
    /**
     * mq:秒杀失败消息目的地
     */
    public static final String SECKILL_FAIL_DEST = SECKILL_ORDER_TOPIC + ":" + SECKILL_FAIL_TAG;
    /**
     * mq:延迟秒杀订单标签（订单支付延迟）
     */
    public static final String DELAY_SECKILL_ORDER_TAG = "delay_seckill_order_tag";
    /**
     * mq:延迟秒杀订单消费者组（订单支付延迟）
     */
    public static final String DELAY_SECKILL_ORDER_CONSUMER_GROUP = "delay_seckill_order_consumer_group";
    /**
     * mq:延迟秒杀订单消息目的地（订单支付延迟）
     */
    public static final String DELAY_SECKILL_ORDER_DEST = SECKILL_ORDER_TOPIC + ":" + DELAY_SECKILL_ORDER_TAG;
    /**
     * mq:清除本地秒杀订单售完标签
     */
    public static final String CLEAR_STOCK_NUM_OVER_TAG = "clear_stock_num_over_tag";
    /**
     * mq:清除本地秒杀订单售完消费者组
     */
    public static final String CLEAR_STOCK_NUM_OVER_CONSUMER_GROUP = "clear_stock_num_over_consumer_group";
    /**
     * mq:清除本地秒杀订单售完消息目的地
     */
    public static final String CLEAR_STOCK_NUM_OVER_DEST = SECKILL_ORDER_TOPIC + ":" + CLEAR_STOCK_NUM_OVER_TAG;

}
