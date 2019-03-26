package com.yonyou.kh.remote.util;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public class RemoteJson {

	private RemoteJson(){}
	
	public static JSONObject buildJson(List<Map<String, Object>> conditions, Map<String, Integer> pager, List<Map<String, Object>> orders, List<Map<String, Object>> fields){
		JSONObject json = new JSONObject();
		json.put("conditions", conditions);
		json.put("pager", pager);
		json.put("orders", orders);
		json.put("fields", fields);
		return json;
	}
	
}
