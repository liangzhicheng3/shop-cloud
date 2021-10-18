package com.liangzhicheng.shop.common.message;

public class MemberCodeMessage extends CodeMessage {

    private MemberCodeMessage(Integer code, String message){
        super(code, message);
    }

    public static final MemberCodeMessage MEMBER_SERVER_BUSY = new MemberCodeMessage(500200,"会员服务繁忙");
    public static final MemberCodeMessage PARAM_VERIFY_ERROR = new MemberCodeMessage(500202,"参数校验异常");
    public static final MemberCodeMessage PHONE_PARAM_IS_NULL = new MemberCodeMessage(500203,"手机号码不能为空");
    public static final MemberCodeMessage PHONE_NOT_EXIST = new MemberCodeMessage(5002034,"手机号码不存在");
    public static final MemberCodeMessage PASSWORD_PARAM_IS_NULL = new MemberCodeMessage(500205,"密码不能为空");
    public static final MemberCodeMessage PASSWORD_ERROR = new MemberCodeMessage(500206,"用户名或密码错误");
    public static final MemberCodeMessage PARAM_PHONE_ERROR = new MemberCodeMessage(500209,"请填入正确的手机号码");

}
