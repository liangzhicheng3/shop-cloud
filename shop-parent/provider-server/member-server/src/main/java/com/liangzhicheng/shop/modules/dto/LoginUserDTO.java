package com.liangzhicheng.shop.modules.dto;

import lombok.*;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserDTO implements Serializable {

    @Pattern(regexp = "^((13[0-9])" +
            "|(14[5,7,9])" +
            "|(15([0-3]|[5-9]))" +
            "|(166)" +
            "|(17[0,1,3,5,6,7,8])" +
            "|(18[0-9])" +
            "|(19[8|9]))\\d{8}$",
            message = "手机格式有误")
    private String phone;

    private String password;

}
