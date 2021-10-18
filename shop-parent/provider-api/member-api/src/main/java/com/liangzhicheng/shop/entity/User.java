package com.liangzhicheng.shop.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 密码
     */
    @JsonIgnore //@JsonIgnore注解，敏感属性信息忽略后不返回
    private String password;

    /**
     * 盐
     */
    @JsonIgnore
    private String salt;

    /**
     * 头像
     */
    private String head;

    /**
     * 注册时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date registerDate;

    /**
     * 最后登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastLoginDate;

    /**
     * 登录次数
     */
    @JsonIgnore
    private Integer loginCount;

}
