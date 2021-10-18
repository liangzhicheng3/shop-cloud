package com.liangzhicheng.shop.modules.controller;

import com.liangzhicheng.shop.common.basic.ResponseResult;
import com.liangzhicheng.shop.common.constant.Constants;
import com.liangzhicheng.shop.common.enums.SeckillRedisKeyEnum;
import com.liangzhicheng.shop.common.enums.UserRedisKeyEnum;
import com.liangzhicheng.shop.common.message.SeckillCodeMessage;
import com.liangzhicheng.shop.common.utils.AssertUtil;
import com.liangzhicheng.shop.common.utils.JSONUtil;
import com.liangzhicheng.shop.entity.User;
import com.liangzhicheng.shop.modules.service.ISeckillGoodsService;
import com.liangzhicheng.shop.vo.SeckillGoodsVO;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping(value = "/path")
public class PathController {

    @Resource
    private ISeckillGoodsService seckillGoodsService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping(value = "/{seckillId}/{verifyCode}")
    public ResponseResult<String> getPath(@CookieValue(name = Constants.USER_LOGIN_TOKEN) String token,
                                          @PathVariable("seckillId") Long seckillId,
                                          @PathVariable("verifyCode") String verifyCode){
        User user = this.getUserByToken(token);
        //验证码校验
        String verifyCodeByCache = stringRedisTemplate.opsForValue().get(
                SeckillRedisKeyEnum.SECKILL_VERIFY_CODE.join(user.getId() + "", seckillId + "")
        );
        AssertUtil.isFalse(!verifyCode.equals(verifyCodeByCache), SeckillCodeMessage.VERIFY_CODE_ERROR);
        //当验证码通过后删除验证码
        stringRedisTemplate.delete(SeckillRedisKeyEnum.SECKILL_VERIFY_CODE.join(user.getId() + "", seckillId + ""));
        SeckillGoodsVO seckillGoodsVO = seckillGoodsService.getByCache(seckillId);
        //1.判断秒杀活动是否开启
        Date currentTime = new Date();
        //如果当前时间小于活动开始时间成立，提示活动尚未开始
        AssertUtil.isFalse(currentTime.compareTo(seckillGoodsVO.getStartTime()) < 0, SeckillCodeMessage.ACTIVITY_NOT_START);
        //如果当前时间大于活动结束时间成立，提示活动已结束
        AssertUtil.isFalse(currentTime.compareTo(seckillGoodsVO.getEndTime()) > 0, SeckillCodeMessage.ACTIVITY_OVER);
        //2.生成uuid随机值
        String random = UUID.randomUUID().toString().replaceAll("-", "");
        //3.以用户id+秒杀商品id作为key，将uuid存储到redis
        stringRedisTemplate.opsForValue().set(
                SeckillRedisKeyEnum.SECKILL_RANDOM_PATH.join(user.getId() + "", seckillId + ""),
                random,
                SeckillRedisKeyEnum.SECKILL_RANDOM_PATH.getExpireTime(),
                SeckillRedisKeyEnum.SECKILL_RANDOM_PATH.getTimeUnit()
        );
        //4.返回生成uuid
        return ResponseResult.success(random);
    }

    private User getUserByToken(String token) {
        return JSONUtil.parseObject(stringRedisTemplate.opsForValue()
                .get(UserRedisKeyEnum.USER_LOGIN_TOKEN.join(token)), User.class);
    }

}
