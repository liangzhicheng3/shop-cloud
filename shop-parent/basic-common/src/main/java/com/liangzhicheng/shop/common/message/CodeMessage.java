package com.liangzhicheng.shop.common.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodeMessage {

    public static final CodeMessage DEFAULT_ERROR_MESSAGE = new CodeMessage(500000, "服务器繁忙，请稍候再试");
    public static final CodeMessage PARAM_ERROR_MESSAGE = new CodeMessage(500003, "参数发生异常，请检查");
    public static final CodeMessage REQUEST_BUSY = new CodeMessage(500429, "请求过于频繁");

    /**
     * 返回状态码
     */
    private Integer code;

    /**
     * 返回信息
     */
    private String message;

}
