package com.yonyou.kh.remote.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yonyou.kh.commons.utils.ResultJson;
import com.yonyou.kh.remote.entity.Push2VO;
import com.yonyou.kh.remote.service.api.IRemoteUVipService;
import com.yonyou.kh.userinfo.entity.UserInfo;

@RestController
@RequestMapping("/ru")
public class RemoteUVipController {

	@Autowired
	private IRemoteUVipService isVip;
	
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public ResultJson getBalance(String memberid,String phone,String uName){
			return isVip.getBalance(memberid,uName,phone);
	}
	/**
	 * 康养基金
	 * param 
	 * return 
	 * */
	@RequestMapping(value = "/Push2Vo" , method = RequestMethod.POST)
	public ResultJson pushData(@RequestBody List<Push2VO> vo){
		return isVip.handleData(vo);
	}
	/**
	 * 卡卷核销报表
	 * 
	 * */
	@RequestMapping(value = "/getConsume" ,method= RequestMethod.POST)
	public ResultJson pullByConsume (String phone ,String beginDate ,String endDate){
		if(phone==null && beginDate==null && endDate==null){
		}
		UserInfo userinfo =(UserInfo)isVip.checkMemberID(null,null,phone).getData();
		isVip.pullByConsume(userinfo.getMid(), beginDate, endDate);
		return null;
	}
	/**
	 * 获取会员信息
	 * param 会员id，电话号码，名字
	 * return identity_num（身份证号），realname，phone
	 * */
	@RequestMapping(value = "/getMemberInfo", method = RequestMethod.GET)
	public ResultJson getMemberInfo(String memberid,String phone,String uName){
		return isVip.checkMemberID(memberid,uName,phone);
	}

}
