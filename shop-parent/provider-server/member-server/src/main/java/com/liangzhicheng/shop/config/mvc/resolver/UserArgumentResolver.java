package com.liangzhicheng.shop.config.mvc.resolver;

import com.liangzhicheng.shop.annotation.UserParam;
import com.liangzhicheng.shop.common.utils.CookieUtil;
import com.liangzhicheng.shop.entity.User;
import com.liangzhicheng.shop.modules.service.IUserService;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @description 用户参数解析器
 */
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Resource
    private IUserService userService;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(UserParam.class)
                && methodParameter.getParameterType().equals(User.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {
        return userService.getByToken(
                CookieUtil.getToken(nativeWebRequest.getNativeRequest(HttpServletRequest.class))
        );
    }

}
