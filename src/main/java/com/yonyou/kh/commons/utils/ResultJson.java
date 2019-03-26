package com.yonyou.kh.commons.utils;

import java.io.Serializable;

import com.github.pagehelper.PageInfo;

/**
 * ResultJson 统一restful接口返回
 * 
 * @author wangjian
 *
 */
public class ResultJson implements Serializable {

	private static final long serialVersionUID = -5425361471916352020L;

	/**
	 * 状态
	 */
	private Integer flag;
	/**
	 * 消息
	 */
	private String msg;
	/**
	 * 数据
	 */
	private Object data;
	/**
	 * 总条数
	 */
	private Long count;
	/**
	 * 页码
	 */
	private Integer pageIndex;
	/**
	 * 每页显示条数
	 */
	private Integer pageSize;

	/**
	 * 构造函数
	 * 
	 * @param code
	 * @param msg
	 * @param data
	 * @param pageIndex
	 * @param pageSize
	 * @param count
	 */
	public ResultJson(Integer flag, String msg, Object data, Long count, Integer pageIndex, Integer pageSize) {
		
		this.setFlag(flag);
		this.msg = msg;
		this.data = data;
		this.count = count;
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
	}

	/**
	 * 操作成功返回
	 * 
	 * @param msg
	 * @return
	 */
	public static ResultJson buildSuccess(String msg) {
		return new ResultJson(RequestStatusEnum.SUCCESS.getStatus(), msg, null, null,null, null);
	}

	/**
	 * 操作成功返回
	 * 
	 * @return
	 */
	public static ResultJson buildSuccess() {
		return new ResultJson(RequestStatusEnum.SUCCESS.getStatus(), "", null, null,null, null);
	}

	/**
	 * 操作成功返回
	 * 
	 * @param msg
	 * @param data
	 * @return
	 */
	public static ResultJson buildSuccess(String msg, Object data) {
		return new ResultJson(RequestStatusEnum.SUCCESS.getStatus(), "", data, null,null, null);
	}

	/**
	 * 操作成功返回
	 * 
	 * @param msg
	 * @param pageInfo
	 * @return
	 */
	public static ResultJson buildSuccess(String msg, PageInfo<?> pageInfo) {
		if (pageInfo == null || pageInfo.getList().isEmpty()) {
			return new ResultJson(RequestStatusEnum.SUCCESS.getStatus(), "", null, null,pageInfo.getPageNum(), pageInfo.getPageSize());
		}

		return new ResultJson(RequestStatusEnum.SUCCESS.getStatus(), "",pageInfo.getList(), pageInfo.getTotal(),pageInfo.getPageNum(), pageInfo.getPageSize());
	}

	/**
	 * 操作失败返回
	 * 
	 * @param msg
	 * @return
	 */
	public static ResultJson buildError(String msg) {
		return new ResultJson(RequestStatusEnum.FAIL_FIELD.getStatus(), msg, null, null,null, null);
	}

	/**
	 * 操作失败返回
	 * 
	 * @param msg
	 * @param data
	 * @return
	 */
	public static ResultJson buildError(String msg, Object data) {
		return new ResultJson(RequestStatusEnum.FAIL_FIELD.getStatus(), msg, data, null,null, null);
	}

	/**
	 * 操作失败,参数异常返回
	 * 
	 * @param msg
	 * @return
	 */
	public static ResultJson buildParamError(String msg) {
		return new ResultJson(RequestStatusEnum.FAIL_PARAM.getStatus(), msg, null, null,null, null);
	}

	/**
	 * 身份认证失败，权限不足
	 * 
	 * @return
	 */
	public static ResultJson buildUnauthorized(String msg) {
		return new ResultJson(RequestStatusEnum.UAUTHORIZED.getStatus(), msg, null, null,null, null);
	}

	/**
	 * 登录session超时
	 * 
	 * @param msg
	 * @return
	 */
	public static ResultJson buildSessionOut(String msg) {
		return new ResultJson(RequestStatusEnum.SESSION_OUT.getStatus(), msg, null, null,null, null);
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

}
