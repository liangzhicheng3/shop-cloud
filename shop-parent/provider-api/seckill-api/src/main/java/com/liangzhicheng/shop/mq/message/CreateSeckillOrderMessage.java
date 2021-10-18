package com.liangzhicheng.shop.mq.message;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSeckillOrderMessage {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 秒杀商品id
     */
    private Long seckillId;

    /**
     * 用户随机id
     */
    private String uuid;

}
