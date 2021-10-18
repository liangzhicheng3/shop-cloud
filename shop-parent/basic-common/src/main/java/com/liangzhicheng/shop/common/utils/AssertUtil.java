package com.liangzhicheng.shop.common.utils;

import com.liangzhicheng.shop.common.exception.CustomizeException;
import com.liangzhicheng.shop.common.message.CodeMessage;
import org.springframework.util.StringUtils;

public class AssertUtil {

    /**
     * @description 判断空值
     * @param text
     * @param message
     */
    public static void isLength(String text, CodeMessage message) {
        if (!StringUtils.hasLength(text)) {
            throw new CustomizeException(message);
        }
    }

    /**
     * @description 判断输入密码是否一致
     * @param v1
     * @param v2
     * @param message
     */
    public static void isMatch(String v1, String v2, CodeMessage message) {
        boolean flag = false;
        if(v1 == null || v2 == null){
            flag = true;
        }
        if(!v1.equals(v2)){
            flag = true;
        }
        if(flag){
            throw new CustomizeException(message);
        }
    }

    /**
     * @description 需求判断参数不为空
     * @param object
     * @param message
     */
    public static void notNull(Object object, CodeMessage message) {
        if(object instanceof String){
            isLength((String) object, message);
            return;
        }
        if(object == null){
            throw new CustomizeException(message);
        }
    }

    /**
     * @description 需求判断参数为空
     * @param object
     * @param message
     */
    public static void isNull(Object object, CodeMessage message) {
        if(object != null){
            throw new CustomizeException(message);
        }
    }

    /**
     * @description 需求判断为false，true抛异常
     * @param result
     * @param message
     */
    public static void isFalse(boolean result, CodeMessage message){
        isTrue(!result, message);
    }

    /**
     * @description 需求判断为true，false抛异常
     * @param result
     * @param message
     */
    public static void isTrue(boolean result, CodeMessage message){
        if(!result){
            throw new CustomizeException(message);
        }
    }

}
