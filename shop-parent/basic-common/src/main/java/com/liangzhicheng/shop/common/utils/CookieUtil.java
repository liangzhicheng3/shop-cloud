package com.liangzhicheng.shop.common.utils;

import com.liangzhicheng.shop.common.constant.Constants;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

    /**
     * @description 设置token到响应头
     * @param token
     * @param response
     */
    public static void setToken(String token, HttpServletResponse response) {
        Cookie cookie = new Cookie(Constants.USER_LOGIN_TOKEN, token);
        cookie.setDomain(Constants.COOKIE_DOMAIN);
        cookie.setMaxAge(Constants.LOGIN_TOKEN_EXPIRE_TIME);
        cookie.setPath(Constants.COOKIE_PATH);
        response.addCookie(cookie);
    }

    /**
     * @description 从请求头中获取token
     * @param request
     * @return String
     */
    public static String getToken(HttpServletRequest request) {
        if(request == null){
            return null;
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (Constants.USER_LOGIN_TOKEN.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}
