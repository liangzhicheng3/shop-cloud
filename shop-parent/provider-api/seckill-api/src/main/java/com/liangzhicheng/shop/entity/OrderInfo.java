package com.liangzhicheng.shop.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 快递地址id
     */
    private Long deliveryAddrId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品图片
     */
    private String goodsImage;

    /**
     * 商品数量
     */
    private Integer goodsNum;

    /**
     * 商品价格
     */
    private BigDecimal goodsPrice;

    /**
     * 秒杀价格
     */
    private BigDecimal seckillPrice;

    /**
     * 订单状态：0未付款，1已付款，2手动取消订单，4超时取消订单
     */
    private Integer status = STATUS_NOT_PAY;

    /**
     * 支付时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payTime;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    public static final Integer STATUS_NOT_PAY = 0; //未付款
    public static final Integer STATUS_PAY = 1; //已付款
    public static final Integer STATUS_CANCEL = 2; //手动取消订单
    public static final Integer STATUS_TIMEOUT = 3; //超时取消订单

}
