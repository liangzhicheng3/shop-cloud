package com.liangzhicheng.shop.common.enums;

import com.liangzhicheng.shop.common.constant.Constants;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

@Getter
public enum SeckillRedisKeyEnum {

    /**
     * 存储用户秒杀商品对象的键值对
     */
   USER_SECKILL_RECORD(Constants.USER_SECKILL_RECORD),
    /**
     * 秒杀商品信息哈希
     */
    SECKILL_GOODS_HASH(Constants.SECKILL_GOODS_HASH),
    /**
     * 秒杀商品库存哈希
     */
   SECKILL_GOODS_STOCK_HASH(Constants.SECKILL_GOODS_STOCK_HASH),
    /**
     * 秒杀访问地址随机
     */
    SECKILL_RANDOM_PATH(Constants.SECKILL_RANDOM_PATH, 2, TimeUnit.SECONDS),
    /**
     * 秒杀访问验证码
     */
    SECKILL_VERIFY_CODE(Constants.SECKILL_VERIFY_CODE, 5, TimeUnit.MINUTES);

    private String prefix;
    private long expireTime;
    private TimeUnit timeUnit;

    SeckillRedisKeyEnum(String prefix){
        this(prefix, 0, null);
    }

    SeckillRedisKeyEnum(String prefix, long expireTime, TimeUnit timeUnit){
        this.prefix = prefix;
        this.expireTime = expireTime;
        this.timeUnit = timeUnit;
    }

    public String join(){
        return join(null);
    }

    public String join(String ... value){
        if(value == null || value.length == 0){
            return this.prefix;
        }
        StringBuilder sb = new StringBuilder(100);
        sb.append(this.prefix);
        for (String s : value) {
            sb.append(":").append(s);
        }
        return sb.toString();
    }

}
