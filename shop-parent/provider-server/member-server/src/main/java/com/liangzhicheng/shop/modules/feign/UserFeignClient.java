package com.liangzhicheng.shop.modules.feign;

import com.liangzhicheng.shop.common.basic.ResponseResult;
import com.liangzhicheng.shop.feign.IUserFeignApi;
import com.liangzhicheng.shop.modules.service.IUserService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class UserFeignClient implements IUserFeignApi {

    @Resource
    private IUserService userService;

    @Override
    public ResponseResult<Boolean> refreshToken(String token) {
        return ResponseResult.success(userService.refreshToken(token));
    }

}
