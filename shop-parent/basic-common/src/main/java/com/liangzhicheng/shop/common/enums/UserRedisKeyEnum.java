package com.liangzhicheng.shop.common.enums;

import com.liangzhicheng.shop.common.constant.Constants;
import lombok.Getter;
import org.apache.tomcat.util.buf.StringUtils;

import java.util.concurrent.TimeUnit;

@Getter
public enum UserRedisKeyEnum {

    /**
     * 用户登录，存储user对象的键值对
     */
   USER_LOGIN_TOKEN(Constants.USER_LOGIN_TOKEN, Constants.LOGIN_TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);

    private String prefix;
    private long expireTime;
    private TimeUnit timeUnit;

    UserRedisKeyEnum(String prefix, long expireTime, TimeUnit timeUnit){
        this.prefix = prefix;
        this.expireTime = expireTime;
        this.timeUnit = timeUnit;
    }

    public String join(String ... value){
        StringBuilder sb = new StringBuilder(100);
        sb.append(this.prefix);
        for (String s : value) {
            sb.append(":").append(s);
        }
        return sb.toString();
    }

}
