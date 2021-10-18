package com.liangzhicheng.shop.feign;

import com.liangzhicheng.shop.common.basic.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "member-server")
public interface IUserFeignApi {

    /**
     * 更新redis中token
     * @param token
     * @return ResponseResult<Boolean>
     */
    @PostMapping(value = "/refreshToken")
    ResponseResult<Boolean> refreshToken(@RequestParam("token") String token);

}
