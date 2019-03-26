package com.yonyou.kh.userinfo.api;

import com.yonyou.kh.commons.utils.ResultJson;

/**
 * 拉取U会员的会员信息API
 * @author wangjian
 *
 */
public interface IPullUserInfo {

	/**
	 * 拉取会员信息
	 * @return
	 */
	public ResultJson userInfoPull();
	
	/**
	 * 推送调整单
	 * @return
	 */
	public ResultJson adjustmentOrderPush();
	
}
