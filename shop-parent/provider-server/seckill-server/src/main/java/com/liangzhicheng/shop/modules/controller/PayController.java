package com.liangzhicheng.shop.modules.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.liangzhicheng.shop.common.constant.Constants;
import com.liangzhicheng.shop.common.message.SeckillCodeMessage;
import com.liangzhicheng.shop.common.utils.AssertUtil;
import com.liangzhicheng.shop.config.alipay.AlipayProperties;
import com.liangzhicheng.shop.entity.OrderInfo;
import com.liangzhicheng.shop.modules.service.IOrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping(value = "/seckill/pay")
public class PayController {

    @Resource
    private IOrderInfoService orderInfoService;
    @Resource
    private AlipayClient alipayClient; //获得初始化AlipayClient
    @Resource
    private AlipayProperties alipayProperties;

    /**
     * @description 发起支付请求
     * @param token
     * @param orderNo
     * @param response
     */
    @GetMapping(value = "/alipay")
    public void alipay(@CookieValue(name = Constants.USER_LOGIN_TOKEN) String token,
                       String orderNo,
                       HttpServletResponse response){
        OrderInfo orderInfo = orderInfoService.get(token, orderNo);
        AssertUtil.notNull(orderInfo, SeckillCodeMessage.ORDER_NOT_EXIST);
        AlipayTradePagePayRequest alipayRequest = null; //设置请求参数
        String out_trade_no = ""; //商户订单号，商户网站订单系统中唯一订单号，必填
        String total_amount = ""; //付款金额，必填
        String subject = ""; //订单名称，必填
        String body = ""; //商品描述，可空
        String result = "";
        PrintWriter out = null;
        try {
            alipayRequest = new AlipayTradePagePayRequest();
            alipayRequest.setReturnUrl(alipayProperties.getReturnUrl());
            alipayRequest.setNotifyUrl(alipayProperties.getNotifyUrl());
            out_trade_no = orderInfo.getOrderNo();
            total_amount = orderInfo.getSeckillPrice().toString();
            subject = "秒杀商品：" + orderInfo.getGoodsName();
            alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                    + "\"total_amount\":\""+ total_amount +"\","
                    + "\"subject\":\""+ subject +"\","
                    + "\"body\":\""+ body +"\","
                    + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
            //若想给BizContent增加其他可选请求参数，以增加自定义超时时间参数timeout_express来举例说明
            //alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
            //		+ "\"total_amount\":\""+ total_amount +"\","
            //		+ "\"subject\":\""+ subject +"\","
            //		+ "\"body\":\""+ body +"\","
            //		+ "\"timeout_express\":\"10m\","
            //		+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
            //请求
            result = alipayClient.pageExecute(alipayRequest).getBody();
            log.info("支付宝支付校验结果：{}", result);
            //输出
            response.setContentType("text/html;charset=utf-8");
            out = response.getWriter();
            out.println(result);
        } catch (AlipayApiException e) {
            log.error("支付宝支付失败：{}" , e);
        } catch (IOException e) {
            log.error("输出异常：{}" , e);
        }
    }

    /**
     * @description 支付宝异步通知回调接口，根据用户支付结果更新订单信息以及其他相关业务逻辑处理
     * @return String
     */
    @RequestMapping(value = "/notify_url")
    public String notifyUrl(@RequestParam HashMap<String, String> params){
        //验证签名
        boolean signVerified = false;
        String out_trade_no = ""; //商户订单号
        String trade_no = ""; //支付宝交易号
        String trade_status = ""; //交易状态
        try {
            signVerified = AlipaySignature.rsaCheckV1(
                    params,
                    alipayProperties.getAlipayPublicKey(),
                    alipayProperties.getCharset(),
                    alipayProperties.getSignType()
            );
            /* 实际验证过程建议商户务必添加以下校验：
            1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
            2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
            3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
            4、验证app_id是否为该商户本身。
            */
            if (signVerified) { //验证成功
                out_trade_no = params.get("out_trade_no");
                trade_no = params.get("trade_no");
                trade_status = params.get("trade_status");
                if (trade_status.equals("TRADE_FINISHED")) {
                    //判断该笔订单是否在商户网站中已经做过处理
                    //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                    //如果有做过处理，不执行商户的业务程序
                    //注意：
                    //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
                } else if (trade_status.equals("TRADE_SUCCESS")) {
                    //判断该笔订单是否在商户网站中已经做过处理
                    //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                    //如果有做过处理，不执行商户的业务程序
                    //处理订单信息以及其他相关业务逻辑
                    orderInfoService.paySuccess(out_trade_no, trade_no);
                    //注意：
                    //付款完成后，支付宝系统发送该交易状态通知
                }
                return "success";
            } else { //验证失败
                //调试用，写文本函数记录程序运行情况是否正常
                //String sWord = AlipaySignature.getSignCheckContentV1(params);
                //AlipayConfig.logResult(sWord);
            }
        } catch (AlipayApiException e) {
            log.error("支付宝异步通知回调失败：{}" , e);
        }
        return "fail";
    }

    /**
     * @description 支付宝同步通知回调接口，用于展示支付结果
     * @return String
     */
    @RequestMapping(value = "/return_url")
    public void returnUrl(@RequestParam HashMap<String, String> params,
                          HttpServletResponse response){
        //验证签名
        boolean signVerified = false;
        try {
            signVerified = AlipaySignature.rsaCheckV2(
                    params,
                    alipayProperties.getAlipayPublicKey(),
                    alipayProperties.getCharset(),
                    alipayProperties.getSignType()
            );
            log.info("signVerified 校验结果：" + signVerified);
            if (signVerified) {
                //验签成功，重定向到订单详情页
                response.sendRedirect("http://localhost/order_detail.html?orderNo=" + params.get("out_trade_no"));
            } else {
                //验签失败，重定向到失败页面
                response.sendRedirect("http://localhost/50x.html");
            }
        } catch (AlipayApiException e) {
            log.error("支付宝同步通知回调失败：{}" , e);
        } catch (IOException e) {
            log.error("输出异常：{}" , e);
        }
    }

}
