package com.yonyou.kh.commons.utils;

/**
 * rest 返回参数枚举
 * @author wangjian
 */
public enum RequestStatusEnum {
	/**
	 * 0.失败，系统错误
	 * 1.成功
	 * 2.失败，参数异常
	 * 3.身份认证失败，权限不足
	 * 4.session过期
	 */
	FAIL_FIELD(0),
	SUCCESS(1),
	FAIL_PARAM(2),
	UAUTHORIZED(3),
	SESSION_OUT(4);
	
	private Integer status;

	private RequestStatusEnum(int status) {
		this.status=status;
	}

	public Integer getStatus() {
		return status;
	}
	
	
}
