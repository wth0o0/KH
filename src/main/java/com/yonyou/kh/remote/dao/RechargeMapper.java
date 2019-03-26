package com.yonyou.kh.remote.dao;

import com.yonyou.kh.recharge.entity.RechargeDetail;

public interface RechargeMapper {
   
	RechargeDetail selectByMidAndType(String uid,Integer rechargeType);
	
	int insertSelective(RechargeDetail rechargeDetail);
}