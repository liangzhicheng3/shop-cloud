package com.liangzhicheng.shop.mq.message;

import com.liangzhicheng.shop.common.message.CodeMessage;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeckillFailMessage {

    /**
     * 返回消息对象
     */
    private CodeMessage codeMessage;

    /**
     * 用户随机id
     */
    private String uuid;

}
