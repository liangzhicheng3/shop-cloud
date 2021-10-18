package com.liangzhicheng.shop.mq.message;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeckillSuccessMessage {

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 用户随机id
     */
    private String uuid;

}
