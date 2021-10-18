//package com.liangzhicheng.shop.config.mvc.filter;
//
//import com.liangzhicheng.shop.common.basic.ResponseResult;
//import com.liangzhicheng.shop.common.message.CodeMessage;
//import com.liangzhicheng.shop.common.utils.JSONUtil;
//import com.netflix.zuul.ZuulFilter;
//import com.netflix.zuul.context.RequestContext;
//import com.netflix.zuul.exception.ZuulException;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
//import org.springframework.stereotype.Component;
//import org.springframework.util.ReflectionUtils;
//import org.springframework.util.StringUtils;
//
//import javax.servlet.http.HttpServletResponse;
//
//@Slf4j
//@Component
//public class CustomizeSendErrorFilter extends ZuulFilter {
//
//    private static final String SEND_ERROR_FILTER_RAN = "sendErrorFilter.ran";
//
//    @Override
//    public String filterType() {
//        return FilterConstants.ERROR_TYPE;
//    }
//
//    @Override
//    public int filterOrder() {
//        return FilterConstants.SEND_ERROR_FILTER_ORDER;
//    }
//
//    @Override
//    public boolean shouldFilter() {
//        RequestContext requestContext = RequestContext.getCurrentContext();
//        return requestContext.getThrowable() != null
//                && !requestContext.getBoolean(SEND_ERROR_FILTER_RAN, false);
//    }
//
//    @Override
//    public Object run() {
//        RequestContext requestContext = RequestContext.getCurrentContext();
//        Throwable throwable = requestContext.getThrowable();
//        log.warn("Error during filtering", throwable);
//        try {
//            CodeMessage codeMessage = CodeMessage.DEFAULT_ERROR_MESSAGE;
//            String rateLimitExceeded = (String) requestContext.get("rateLimitExceeded");
//            if(!StringUtils.isEmpty(rateLimitExceeded)
//                    && rateLimitExceeded.equalsIgnoreCase(Boolean.TRUE.toString())){
//                codeMessage = CodeMessage.REQUEST_BUSY;
//            }
//            HttpServletResponse response = requestContext.getResponse();
//            response.setContentType("application/json;charset=utf-8");
//            response.getWriter().write(JSONUtil.toJSONString(ResponseResult.error(codeMessage)));
//        } catch (Exception e) {
//            ReflectionUtils.rethrowRuntimeException(e);
//        }
//        return null;
//    }
//
//}
