package com.liangzhicheng.shop.modules.service;

import com.liangzhicheng.shop.entity.User;
import com.liangzhicheng.shop.modules.dto.LoginUserDTO;

public interface IUserService {

    /**
     * @description 登录验证
     * @param loginVo
     * @return String
     */
    String login(LoginUserDTO loginVo);

    /**
     * @description 更新redis中token
     * @param token
     * @return Boolean
     */
    Boolean refreshToken(String token);

    /**
     * @description 从redis缓存中根据token获取用户
     * @param token
     * @return User
     */
    User getByToken(String token);

}
