package com.liangzhicheng.shop.config.advice;

import com.liangzhicheng.shop.common.basic.ResponseResult;
import com.liangzhicheng.shop.common.exception.CustomizeException;
import com.liangzhicheng.shop.common.message.CodeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice(basePackages = {"com.liangzhicheng"})
public class CommonExceptionAdvice {

    /**
     * 默认异常消息
     */
    private CodeMessage defaultErrorMessage;

    /**
     * 参数异常消息
     */
    private CodeMessage paramErrorMessage;

    /**
     * 默认输出异常消息
     */
    protected CommonExceptionAdvice(){
        this(CodeMessage.DEFAULT_ERROR_MESSAGE, CodeMessage.PARAM_ERROR_MESSAGE);
    }

    /**
     * @description 根据defaultErrorMessage，paramErrorMessage输出异常消息
     * @param defaultErrorMessage
     * @param paramErrorMessage
     */
    protected CommonExceptionAdvice(CodeMessage defaultErrorMessage,
                                    CodeMessage paramErrorMessage){
        this.defaultErrorMessage = defaultErrorMessage;
        this.paramErrorMessage = paramErrorMessage;
    }

    /**
     * @description 自定义异常信息
     * @param ex
     * @return ResponseResult
     */
    @ExceptionHandler(CustomizeException.class)
    @ResponseBody
    public ResponseResult handlerCustomizeException(CustomizeException ex){
        log.error("自定义异常输出：{}", ex.getCodeMessage().getMessage());
        return ResponseResult.error(ex.getCodeMessage());
    }

    /**
     * @description 约束异常信息
     * @param ex
     * @return ResponseResult
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ResponseResult handlerBindException(BindException ex){
        ObjectError objectError = ex.getAllErrors().get(0);
        String message = objectError.getDefaultMessage();
        return ResponseResult.error(
                new CodeMessage(paramErrorMessage.getCode(), message));
    }

    /**
     * @description 异常信息
     * @param ex
     * @return ResponseResult
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult handlerErrorException(Exception ex){
        log.error("其他异常输出：{}", ex);
        return ResponseResult.error(defaultErrorMessage);
    }

}
