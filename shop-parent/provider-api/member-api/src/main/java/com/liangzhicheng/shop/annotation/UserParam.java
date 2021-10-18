package com.liangzhicheng.shop.annotation;

import java.lang.annotation.*;

@Documented
@Inherited
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserParam {

}
