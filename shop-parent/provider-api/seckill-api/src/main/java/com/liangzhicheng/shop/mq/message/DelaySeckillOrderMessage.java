package com.liangzhicheng.shop.mq.message;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DelaySeckillOrderMessage {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 秒杀商品id
     */
    private Long seckillId;

}
