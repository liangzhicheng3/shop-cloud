package com.liangzhicheng.shop.config.alipay;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "alipay.config")
@Component
public class AlipayProperties {

    /**
     * appid，应用id
     */
    private String appId;

    /**
     * 商户密钥
     */
    private String merchantPrivateKey;

    /**
     * 支付宝公钥，查看地址：https://openhome.alipay.com/platform/keyManage.htm，对应appid下的支付宝公钥。
     */
    private String alipayPublicKey;

    /**
     * 服务器异步通知页面路径
     */
    private String notifyUrl;

    /**
     * 页面跳转同步通知页面路径
     */
    private String returnUrl;

    /**
     * 签名方式
     */
    private String signType;

    /**
     * 字符编码格式
     */
    private String charset;

    /**
     * 支付宝网关
     */
    private String gatewayUrl;

}
