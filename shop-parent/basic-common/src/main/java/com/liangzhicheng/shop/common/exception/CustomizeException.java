package com.liangzhicheng.shop.common.exception;

import com.liangzhicheng.shop.common.message.CodeMessage;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomizeException extends RuntimeException {

    /**
     * 消息实体
     */
    private CodeMessage codeMessage;

    public CustomizeException(CodeMessage codeMessage){
        this.codeMessage = codeMessage;
    }

}
