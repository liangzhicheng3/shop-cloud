package com.liangzhicheng.shop.config.advice;

import com.liangzhicheng.shop.common.message.GoodsCodeMessage;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice(basePackages = {"com.liangzhicheng"})
public class GoodsExceptionAdvice extends CommonExceptionAdvice {

    public GoodsExceptionAdvice(){
        super(GoodsCodeMessage.GOODS_SERVER_BUSY, GoodsCodeMessage.PARAM_VERIFY_ERROR);
    }

}
