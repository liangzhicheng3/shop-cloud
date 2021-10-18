package com.liangzhicheng.shop.vo;

import com.liangzhicheng.shop.entity.SeckillGoods;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SeckillGoodsVO extends SeckillGoods {

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品介绍
     */
    private String goodsIntro;

    /**
     * 商品图片
     */
    private String goodsImage;

    /**
     * 商品明细
     */
    private String goodsDetail;

    /**
     * 商品价格
     */
    private BigDecimal goodsPrice;

    /**
     * 商品库存
     */
    private Integer goodsStock;

}
