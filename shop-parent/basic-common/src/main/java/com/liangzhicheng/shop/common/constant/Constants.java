package com.liangzhicheng.shop.common.constant;

public abstract class Constants {

    /**
     * 用户登录token
     */
    public static final String USER_LOGIN_TOKEN = "user_login_token";
    /**
     * 登录token有效时间
     */
    public static final int LOGIN_TOKEN_EXPIRE_TIME = 1800;

    /**
     * 域
     */
    public static final String COOKIE_DOMAIN = "localhost";
    /**
     * 路径
     */
    public static final String COOKIE_PATH = "/";

    /**
     * 加密/解密 盐
     */
    public static final String SALT = "1a2c3d4e";

    /**
     * 用户秒杀商品记录
     */
    public static final String USER_SECKILL_RECORD = "user_seckill_record";
    /**
     * 秒杀商品信息哈希
     */
    public static final String SECKILL_GOODS_HASH = "seckill_goods_hash";
    /**
     * 秒杀商品库存哈希
     */
    public static final String SECKILL_GOODS_STOCK_HASH = "seckill_goods_stock_hash";
    /**
     * 秒杀访问地址随机
     */
    public static final String SECKILL_RANDOM_PATH = "seckill_random_path";
    /**
     * 秒杀访问验证码
     */
    public static final String SECKILL_VERIFY_CODE = "seckill_verify_code";

}
