package com.liangzhicheng.shop.config.advice;

import com.liangzhicheng.shop.common.message.SeckillCodeMessage;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice(basePackages = {"com.liangzhicheng"})
public class SeckillExceptionAdvice extends CommonExceptionAdvice {

    public SeckillExceptionAdvice(){
        super(SeckillCodeMessage.SECKILL_SERVER_BUSY, SeckillCodeMessage.PARAM_VERIFY_ERROR);
    }

}
