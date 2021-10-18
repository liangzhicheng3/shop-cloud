package com.liangzhicheng.shop.modules.controller;

import com.liangzhicheng.shop.common.constant.Constants;
import com.liangzhicheng.shop.common.enums.SeckillRedisKeyEnum;
import com.liangzhicheng.shop.common.enums.UserRedisKeyEnum;
import com.liangzhicheng.shop.common.utils.JSONUtil;
import com.liangzhicheng.shop.common.utils.VerifyCodeImgUtil;
import com.liangzhicheng.shop.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping(value = "/verify/code")
public class VerifyCodeController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RequestMapping
    public void generate(@CookieValue(name = Constants.USER_LOGIN_TOKEN) String token,
                         Long seckillId,
                         HttpServletResponse response) {
        //1.生成验证码表达式
        String exp = VerifyCodeImgUtil.generateVerifyCode();
        //2.计算验证码表达式的结果
        Integer result = VerifyCodeImgUtil.calc(exp);
        //3.将验证码结果存储在redis，以用户id+秒杀商品id作为key
        User user = this.getUserByToken(token);
        stringRedisTemplate.opsForValue().set(
                SeckillRedisKeyEnum.SECKILL_VERIFY_CODE.join(user.getId() + "", seckillId + ""),
                result + "",
                SeckillRedisKeyEnum.SECKILL_VERIFY_CODE.getExpireTime(),
                SeckillRedisKeyEnum.SECKILL_VERIFY_CODE.getTimeUnit()
        );
        //4.基于验证码表达式生成图片
        BufferedImage image = VerifyCodeImgUtil.createVerifyCodeImg(exp);
        //5.将图片通过响应流返回
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
        } catch (IOException e) {
            log.error("验证码输出异常：{}", e);
        }
    }

    private User getUserByToken(String token) {
        return JSONUtil.parseObject(stringRedisTemplate.opsForValue()
                .get(UserRedisKeyEnum.USER_LOGIN_TOKEN.join(token)), User.class);
    }

}
