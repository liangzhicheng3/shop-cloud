package com.liangzhicheng.shop.common.message;

public class SeckillCodeMessage extends CodeMessage {

    private SeckillCodeMessage(Integer code, String message){
        super(code, message);
    }

    public static final SeckillCodeMessage SECKILL_SERVER_BUSY = new SeckillCodeMessage(500400,"秒杀服务繁忙");
    public static final SeckillCodeMessage NOT_LOGIN = new SeckillCodeMessage(500401,"未登录，请先登录后再进行秒杀");
    public static final SeckillCodeMessage PARAM_VERIFY_ERROR = new SeckillCodeMessage(500402,"参数校验异常");
    public static final SeckillCodeMessage ACTIVITY_NOT_START = new SeckillCodeMessage(500403,"活动尚未开始");
    public static final SeckillCodeMessage ACTIVITY_OVER = new SeckillCodeMessage(500404,"活动已结束");
    public static final SeckillCodeMessage ORDER_NOT_EXIST = new SeckillCodeMessage(500405,"订单记录不存在");
    public static final SeckillCodeMessage REPEAT_ORDER_ERROR = new SeckillCodeMessage(500406,"请勿重复下单");
    public static final SeckillCodeMessage UNDERSTOCK_ERROR = new SeckillCodeMessage(500407,"库存不足");
    public static final SeckillCodeMessage OPERATION_ERROR = new SeckillCodeMessage(500408,"非法操作");
    public static final SeckillCodeMessage VERIFY_CODE_ERROR = new SeckillCodeMessage(500409,"验证码输入有误");

}
