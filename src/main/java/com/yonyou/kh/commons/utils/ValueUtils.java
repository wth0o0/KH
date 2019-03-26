package com.yonyou.kh.commons.utils;

public class ValueUtils {
	private ValueUtils(){}
	
	/**
	 * 判断Value是否为空，返回默认值
	 * @param value
	 * @return
	 */
	public static String getValue(String value){
		return value == null ? null : value.trim();
	}
	
	
}
