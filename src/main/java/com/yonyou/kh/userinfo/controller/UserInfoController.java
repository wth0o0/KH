package com.yonyou.kh.userinfo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.yonyou.kh.commons.utils.ResultJson;
import com.yonyou.kh.remote.util.RemoteJson;
import com.yonyou.kh.remote.util.RemoteMethod;

@RestController
@RequestMapping("/userInfo")
public class UserInfoController {
	
	@Value("${secret.key}")
	private String secretKey;
	@Value("${access.token}")
	private String accessToken;
	
	public ResultJson getUserInfo() {
		
		String url = "http://vip.cdkhms.com/open/mm/member/query/v1?access_token="+accessToken;
		
		List<Map<String,Object>> conditions = new ArrayList<>();
		
		Map<String, Object> map = new HashMap<>();
		map.put("name", "code");
		map.put("value1", "001");
		map.put("type", "string");
		map.put("op", "eq");
		
		conditions.add(map);
		
		Map<String,Integer> pager = new HashMap<>();
		pager.put("pageIndex", 1);
		pager.put("pageSize", 10);
		
		
		List<Map<String,Object>> orders = new ArrayList<>();
		Map<String,Object> map1 = new HashMap<>();
		map1.put("name", "province");
		map1.put("order", "desc");
		orders.add(map1);
		
		List<Map<String,Object>> fields = new ArrayList<>();
		Map<String, Object> m1 = new HashMap<>();
		m1.put("name", "id");
		fields.add(m1);
		
		Map<String, Object> m2 = new HashMap<>();
		m2.put("name", "name");
		fields.add(m2);
		
		Map<String, Object> m3 = new HashMap<>();
		m3.put("name", "contact");
		fields.add(m3);
		
		Map<String, Object> m4 = new HashMap<>();
		m4.put("name", "address");
		fields.add(m4);
		
		Map<String, Object> m5 = new HashMap<>();
		m5.put("name", "province");
		fields.add(m5);
		
		Map<String, Object> m6 = new HashMap<>();
		m6.put("name", "city");
		fields.add(m6);
		
		Map<String, Object> m7 = new HashMap<>();
		m7.put("name", "area");
		fields.add(m7);
		
		Map<String, Object> m8 = new HashMap<>();
		m8.put("name", "code");
		fields.add(m8);
		
		Map<String, Object> m9 = new HashMap<>();
		m9.put("name", "erp_code");
		fields.add(m9);
		
		
		
		
		JSONObject param = RemoteJson.buildJson(conditions, pager, orders, fields);
		
		try {
			ResultJson resultJson = RemoteMethod.connectionUVipFindStoreInfo(url, param, secretKey);
//			System.out.println(resultJson.toString());
			return resultJson;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	
}
