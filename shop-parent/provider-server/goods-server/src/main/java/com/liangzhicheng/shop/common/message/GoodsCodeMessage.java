package com.liangzhicheng.shop.common.message;

public class GoodsCodeMessage extends CodeMessage {

    private GoodsCodeMessage(Integer code, String message){
        super(code, message);
    }

    public static final GoodsCodeMessage GOODS_SERVER_BUSY = new GoodsCodeMessage(500300,"商品服务繁忙");
    public static final GoodsCodeMessage PARAM_VERIFY_ERROR = new GoodsCodeMessage(500302,"参数校验异常");

}
