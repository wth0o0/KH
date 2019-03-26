package com.yonyou.kh.remote.service.api;

import java.util.Date;
import java.util.List;

import com.yonyou.kh.commons.utils.ResultJson;
import com.yonyou.kh.remote.entity.Push2VO;

public interface IRemoteUVipService {

	/**
	 * 推送明细数据
	 * @return
	 */
	public ResultJson getBalance(String memberId,String uName,String phone);
	
	
	/**
	 * 根据门店Id查询门店信息
	 * @return
	 */
	public ResultJson queryUVipStoreInfo();

	/**
	 * 拉取门店信息根据门店标识
	 * @return 
	 */
	public ResultJson pullByStoreInfo(String store_Code);
	
	/**
	 * 接受推送单据数据
	 * @return 
	 */
	public ResultJson handleData(List<Push2VO> vo);
	
	
	public ResultJson checkMemberID(String id,String realname,String phone);

	public ResultJson pullByConsume(String mid, String beginDate, String endDate);

}
