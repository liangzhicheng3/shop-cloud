package com.liangzhicheng.shop.config.mvc.filter;

import com.liangzhicheng.shop.common.basic.ResponseResult;
import com.liangzhicheng.shop.common.utils.CookieUtil;
import com.liangzhicheng.shop.feign.IUserFeignApi;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @description 1.所有进入服务的请求都会先经过zuul网关
 *              2.用户请求到任意服务，都需要刷新redis和cookie中token的有效时间
 *              3.利用ZuulFilter来做token刷新处理
 *              4.利用ZuulFilter的后置拦截，完成token刷新
 *                4.1.校验请求中是否携带cookie(token)
 *                4.2.如果有携带token，则刷新redis和cookie的有效时间
 */
@Component
public class LoginTokenFilter extends ZuulFilter {

    @Resource
    private IUserFeignApi userFeignApi;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        //获取页面传来的token
        HttpServletRequest request = RequestContext
                .getCurrentContext()
                .getRequest();
        //如果取到token有值才进行过滤
        return !StringUtils.isEmpty(CookieUtil.getToken(request));
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        String token = CookieUtil.getToken(request);
        //更新redis中token
        ResponseResult<Boolean> responseResult = userFeignApi.refreshToken(token);
        if(responseResult.isError()){
            return null;
        }
        //更新cookie中token
        if(responseResult.getData()){
            CookieUtil.setToken(token, context.getResponse());
        }
        return null;
    }

}
