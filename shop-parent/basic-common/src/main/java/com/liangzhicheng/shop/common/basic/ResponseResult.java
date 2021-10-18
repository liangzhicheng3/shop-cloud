package com.liangzhicheng.shop.common.basic;

import com.liangzhicheng.shop.common.constant.ApiConstant;
import com.liangzhicheng.shop.common.message.CodeMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseResult<T> {

	/**
	 * 状态码
	 */
	private int code;

	/**
	 * 消息
	 */
	private String message;

	/**
	 * 结果集
	 */
	private T data;

	public static <T> ResponseResult<T> success(){
		return success(null);
	}

	public static <T> ResponseResult<T> success(T data){
		return new ResponseResult(
				ApiConstant.BASE_SUCCESS_CODE, ApiConstant.getMessage(ApiConstant.BASE_SUCCESS_CODE), data);
	}

	public static <T> ResponseResult<T> success(String message, T data){
		return new ResponseResult(ApiConstant.BASE_SUCCESS_CODE, message, data);
	}

	public static ResponseResult<CodeMessage> error(CodeMessage codeMessage){
		return new ResponseResult(codeMessage.getCode(), codeMessage.getMessage(), null);
	}

	public static ResponseResult defaultError() {
		return new ResponseResult(
				ApiConstant.BASE_FAIL_CODE, ApiConstant.getMessage(ApiConstant.BASE_FAIL_CODE), null);
	}

	/**
	 * @description 判断result是否有误，true为错误
	 * @return
	 */
	public boolean isError(){
		return this == null || this.code != ApiConstant.BASE_SUCCESS_CODE;
	}

}
