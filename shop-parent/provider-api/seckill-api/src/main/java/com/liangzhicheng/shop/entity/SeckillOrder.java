package com.liangzhicheng.shop.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SeckillOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 秒杀订单id
     */
    private Long id;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 秒杀商品id
     */
    private Long seckillId;

    public SeckillOrder(String orderNo, Long userId, Long seckillId) {
        this.orderNo = orderNo;
        this.userId = userId;
        this.seckillId = seckillId;
    }

}
