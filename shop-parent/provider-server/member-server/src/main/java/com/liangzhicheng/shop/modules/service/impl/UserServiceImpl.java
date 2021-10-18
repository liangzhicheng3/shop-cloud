package com.liangzhicheng.shop.modules.service.impl;

import com.liangzhicheng.shop.common.enums.UserRedisKeyEnum;
import com.liangzhicheng.shop.common.message.MemberCodeMessage;
import com.liangzhicheng.shop.common.utils.AssertUtil;
import com.liangzhicheng.shop.common.utils.JSONUtil;
import com.liangzhicheng.shop.entity.User;
import com.liangzhicheng.shop.modules.dao.IUserDao;
import com.liangzhicheng.shop.modules.service.IUserService;
import com.liangzhicheng.shop.modules.dto.LoginUserDTO;
import com.liangzhicheng.shop.utils.MD5Util;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private IUserDao userDao;

    @Override
    public String login(LoginUserDTO loginUserDTO) {
        Long phone = Long.valueOf(loginUserDTO.getPhone());
        //参数校验
        AssertUtil.notNull(loginUserDTO.getPhone(), MemberCodeMessage.PHONE_PARAM_IS_NULL);
        //根据手机号码获取用户
        User user = userDao.getById(phone);
        //判断用户是否存在
        AssertUtil.notNull(user, MemberCodeMessage.PHONE_NOT_EXIST);
        //比较密码是否一致，需要对密码再加密(encryptAgain)
        String encryptPassword = MD5Util.encryptAgain(loginUserDTO.getPassword(), user.getSalt());
        AssertUtil.isMatch(user.getPassword(), encryptPassword, MemberCodeMessage.PASSWORD_ERROR);
        //生成token
        String token = UUID.randomUUID().toString().replace("-", "");
        //存在redis中
        user.setPassword("");
        stringRedisTemplate.opsForValue().set(
                UserRedisKeyEnum.USER_LOGIN_TOKEN.join(token),
                JSONUtil.toJSONString(user),
                UserRedisKeyEnum.USER_LOGIN_TOKEN.getExpireTime(),
                UserRedisKeyEnum.USER_LOGIN_TOKEN.getTimeUnit()
        );
        return token;
    }

    @Override
    public Boolean refreshToken(String token) {
        return stringRedisTemplate.expire(
                UserRedisKeyEnum.USER_LOGIN_TOKEN.join(token),
                UserRedisKeyEnum.USER_LOGIN_TOKEN.getExpireTime(),
                UserRedisKeyEnum.USER_LOGIN_TOKEN.getTimeUnit()
        );
    }

    @Override
    public User getByToken(String token) {
        if(token == null){
            return null;
        }
        return JSONUtil.parseObject(stringRedisTemplate.opsForValue()
                .get(UserRedisKeyEnum.USER_LOGIN_TOKEN.join(token)),
                User.class);
    }

}
