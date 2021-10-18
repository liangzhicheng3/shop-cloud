package com.liangzhicheng.shop.config.advice;

import com.liangzhicheng.shop.common.message.MemberCodeMessage;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice(basePackages = {"com.liangzhicheng"})
public class MemberExceptionAdvice extends CommonExceptionAdvice {

    public MemberExceptionAdvice(){
        super(MemberCodeMessage.MEMBER_SERVER_BUSY, MemberCodeMessage.PARAM_VERIFY_ERROR);
    }

}
