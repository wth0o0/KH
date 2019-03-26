package com.yonyou.kh.remote.entity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yonyou.kh.commons.utils.ResultJson;
import com.yonyou.kh.remote.service.api.IRemoteUVipService;

@RestController
@RequestMapping("/api")
public class RemoteUVipApi {
	
	@Autowired
	private IRemoteUVipService service;
	
	/**
	 * U会员往本地推送储值明细
	 * @param params
	 * @return
	 */
	@PostMapping("/remote/pushDetails")
	public ResultJson pushDetails(@RequestBody List<Push2VO> params){
		service.handleData(params);
		return ResultJson.buildSuccess("success");
	}
	
	/**
	 * 本地根据门店标识向U会员请求获取门店信息
	 * @return
	 */
	public ResultJson pullByStoreInfo(String store_Code){
		service.pullByStoreInfo(store_Code);
		return ResultJson.buildSuccess("success");
	}
	
	
}
