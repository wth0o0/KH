package com.yonyou.kh.remote.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.New;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.yonyou.kh.commons.utils.ResultJson;
import com.yonyou.kh.consumption.dao.ConsumptionDetailMapper;
import com.yonyou.kh.consumption.entity.ConsumptionDetail;
import com.yonyou.kh.recharge.dao.RechargeDetailMapper;
import com.yonyou.kh.recharge.entity.RechargeDetail;
import com.yonyou.kh.remote.dao.RechargeMapper;
import com.yonyou.kh.remote.entity.Push2VO;
import com.yonyou.kh.remote.service.api.IRemoteUVipService;
import com.yonyou.kh.remote.util.IdGenerator;
import com.yonyou.kh.remote.util.RemoteJson;
import com.yonyou.kh.remote.util.RemoteMethod;
import com.yonyou.kh.userinfo.dao.UserInfoMapper;
import com.yonyou.kh.userinfo.entity.UserInfo;

@Service
public class RemoteUVipServiceImpl implements IRemoteUVipService {

	@Value("${secret.key}")
	private String secretKey;
	@Value("${access.token}")
	private String accessToken;
	@Value("${ky.life}")
	private Double life;
	@Value("${ky.healthy}")
	private Double healthy;
	@Value("${ky.fun}")
	private Double fun;
	@Autowired
	private UserInfoMapper userMapper;
	@Autowired
	private ConsumptionDetailMapper consumptionMapper;
	@Autowired
	private RechargeDetailMapper remoteMapper;
	
	@Override
	public ResultJson getBalance(String memberId,String uName,String phone) {
		List<UserInfo> userInfos = userMapper.selectByPrimaryKey(memberId,uName,phone);
		if(userInfos==null){
			return ResultJson.buildError("未查询到用户");
		}
		List<Map<String,Object>> resultList=  new ArrayList<>();
		for(UserInfo userInfo:userInfos) {
		Map <String,Object> map = new HashMap<>();
		if(userInfo.getFunFund()!=null) {
			map.put("funFund", userInfo.getFunFund());
		}else {
			map.put("funFund", BigDecimal.ZERO);
		}
		if(userInfo.getHealthyFund()!=null) {
			map.put("healthyFund", userInfo.getHealthyFund());
		}else {
			map.put("healthyFund", BigDecimal.ZERO);
		}
		if(userInfo.getLifeFund()!=null) {
			map.put("lifeFund", userInfo.getLifeFund());
		}else {
			map.put("lifeFund", BigDecimal.ZERO);
		}
		map.put("cyAmount", userInfo.getCyAmount());
		map.put("currencyAmount", userInfo.getCurrencyAmount());
		map.put("Mid", userInfo.getMid());
		map.put("card", userInfo.getCard());
		map.put("kyAmount", userInfo.getKyAmount());
		map.put("careteTime", userInfo.getCreateTime());
		map.put("lastmodefiedTime", userInfo.getLastModifiedTime());
		map.put("phone", userInfo.getPhone());
		map.put("uName", userInfo.getuName());
		map.put("id", userInfo.getId());
		map.put("lifeAmount", userInfo.getKyAmount().multiply(BigDecimal.valueOf(life)).setScale(2,BigDecimal.ROUND_HALF_UP));
		map.put("funAmount", userInfo.getKyAmount().multiply(BigDecimal.valueOf(fun)).setScale(2,BigDecimal.ROUND_HALF_UP));
		map.put("HealthAmount",userInfo.getKyAmount().subtract(userInfo.getKyAmount().multiply(BigDecimal.valueOf(fun)).setScale(2,BigDecimal.ROUND_HALF_UP))
				.subtract(userInfo.getKyAmount().multiply(BigDecimal.valueOf(life)).setScale(2,BigDecimal.ROUND_HALF_UP)) );
		resultList.add(map);
		}
		return ResultJson.buildSuccess("查询成功", resultList);
	}

	@Override
	public ResultJson handleData(List<Push2VO> pushvo) {
		System.out.println(pushvo.toString());
		if (pushvo != null) {
			for (Push2VO vo : pushvo) {
				ConsumptionDetail detail = new ConsumptionDetail();
				// 获取 消费/充值单号（确保数据库中不存在该单）
				String id = vo.getId();
				if (id.equals(null)) {
					ResultJson.buildError("id为空");
				}
				if (checkBillID(id)!=null) {
					return ResultJson.buildError("重复单据");
				}
				if(remoteMapper.selectByPrimaryKey(id)!=null){
					return ResultJson.buildError("重复单据");
				}

				detail.setId(IdGenerator.getUUID());
				detail.setSrcId(id);
				detail.setLastModifiedTime(new Date());

				// 获取用户（会员信息）
				String memberId = vo.getMid();// 获取会员id确保本地有该会员相关信息
				if (memberId == null) {
					return ResultJson.buildError("会员id为空");
				}

				List<UserInfo> userInfos = userMapper.selectByPrimaryKey(memberId,null,null);
				UserInfo userInfo= new UserInfo();
				if (userInfos.isEmpty()) {
					List<UserInfo> returnList  = MemberIDToUserInfo(checkMemberID(memberId,null,null));
					if(returnList!=null && returnList.size()>0){
						userMapper.insertSelective(returnList.get(0));
						userInfo=returnList.get(0);
					}else {
						return ResultJson.buildError("未查询到会员");
					}
				}else {
					userInfo=userInfos.get(0);
				}
				detail.setuId(userInfo.getMid());
				detail.setuName(userInfo.getuName());
				detail.setTransactionAmount(vo.getSum());
				// 两种充值
				if ((vo.getType().equals(7) || vo.getType().equals(8)) && vo.getPayment().equals("999")) {
					return top_ups(detail,vo,userInfo.getPhone());
				} else if (vo.getType().equals(1)||vo.getType().equals(4) || ((vo.getType().equals(7) || vo.getType().equals(8)) &&!vo.getPayment().equals("999"))){
					return top_ups(detail,vo,userInfo.getPhone());
				}

				// 消费
				// 获取店铺信息
				String store_Code = vo.getStore_code();
				if (store_Code!=null) {
					
					List<UserInfo> info=userMapper.selectByPrimaryKey(detail.getuId(),null,null);
					List<Map> storeInfo = (List<Map>) pullByStoreInfo(store_Code).getData();
					if (storeInfo != null) {
						Map Store = storeInfo.get(0);
						String erp_code = (String) Store.get("erp_code");
						String[] erp = erp_code.split("_");
						String code = erp[0];// 01生活类，02健康类，03快乐类
						detail.setTsStoreId(vo.getStore_employee_id());
						detail.setTsStoreName(vo.getStore_employee_name());
						detail.setTransactionAmount(vo.getSum());
						if (code.equals("01")) {
							UserInfo user = new UserInfo();
							user.setMid(detail.getuId());
							detail.setTransactionType(1);
							if (vo.getType() == 3) {
								consumptionMapper.insertSelective(detail);
								user.setLifeFund(detail.getTransactionAmount());
								userMapper.updateByPrimaryKey(userInfo);
							} else if (vo.getType() == 2) {
								if(info.get(0).getCurrencyAmount().compareTo(detail.getTransactionAmount().subtract(info.get(0).getLifeFund()))==-1){
									return ResultJson.buildError("余额不足");
								}
								if (info.get(0).getLifeFund().compareTo(detail.getTransactionAmount()) == -1) {// 需要从通用类型内扣钱
									detail.setTransactionAmount(detail.getTransactionAmount().negate());//取反数
									detail.setBalance(BigDecimal.ZERO);
									consumptionMapper.insertSelective(detail);
									UserInfo userInf = new UserInfo();
									userInf.setLifeFund(info.get(0).getLifeFund().negate());
									userInf.setCurrencyAmount(info.get(0).getLifeFund().add(detail.getTransactionAmount()));
									userInf.setMid(detail.getuId());
									userMapper.updateByPrimaryKey(userInf);
									return ResultJson.buildSuccess("消费成功(从通用类扣款"+info.get(0).getLifeFund().subtract(detail.getTransactionAmount()).toString()+"):"+pushvo.toString());
								} else {
									detail.setTransactionAmount(detail.getTransactionAmount().negate());
									detail.setBalance(BigDecimal.ZERO);
									consumptionMapper.insertSelective(detail);
									UserInfo userInf = new UserInfo();
									userInf.setLifeFund(detail.getTransactionAmount());
									userInf.setMid(detail.getuId());
									userMapper.updateByPrimaryKey(userInf);
									return ResultJson.buildSuccess("消费成功:"+pushvo.toString());
								}
							}
						} else if (code.equals("02")) {
							UserInfo user = new UserInfo();
							user.setMid(detail.getuId());
							detail.setTransactionType(3);
							if (vo.getType() == 3) {
								consumptionMapper.insertSelective(detail);
								userInfo.setHealthyFund(detail.getTransactionAmount());
								userMapper.updateByPrimaryKey(userInfo);
							} else if (vo.getType() == 2) {
								if(info.get(0).getCurrencyAmount().compareTo(detail.getTransactionAmount().subtract(info.get(0).getHealthyFund()))==-1){
									return ResultJson.buildError("余额不足");
								}
								if (info.get(0).getHealthyFund().compareTo(detail.getTransactionAmount()) == -1) {// 需要从通用类型内扣钱
									detail.setTransactionAmount(detail.getTransactionAmount().negate());//取反数
									detail.setBalance(BigDecimal.ZERO);
									consumptionMapper.insertSelective(detail);
									UserInfo userInf = new UserInfo();
									userInf.setHealthyFund(info.get(0).getHealthyFund().negate());
									userInf.setCurrencyAmount(info.get(0).getHealthyFund().add(detail.getTransactionAmount()));
									userInf.setMid(detail.getuId());
									userMapper.updateByPrimaryKey(userInf);
									return ResultJson.buildSuccess("消费成功(从通用类扣款"+info.get(0).getHealthyFund().subtract(detail.getTransactionAmount()).toString()+"):"+pushvo.toString());
									
								} else {
									detail.setTransactionAmount(detail.getTransactionAmount().negate());
									detail.setBalance(BigDecimal.ZERO);
									consumptionMapper.insertSelective(detail);
									UserInfo userInf = new UserInfo();
									userInf.setHealthyFund(detail.getTransactionAmount());
									userInf.setMid(detail.getuId());
									userMapper.updateByPrimaryKey(userInf);
									return ResultJson.buildSuccess("消费成功:"+pushvo.toString());
								}
							}
						} else if (code.equals("03")) {
							UserInfo user = new UserInfo();
							user.setMid(detail.getuId());
							detail.setTransactionType(2);
							if (vo.getType() == 3) {
								consumptionMapper.insertSelective(detail);
								userInfo.setFunFund(detail.getTransactionAmount());
								userMapper.updateByPrimaryKey(userInfo);
							} else if (vo.getType() == 2) {
								if(info.get(0).getCurrencyAmount().compareTo(detail.getTransactionAmount().subtract(info.get(0).getFunFund()))==-1){
									return ResultJson.buildError("余额不足");
								}
								if (info.get(0).getFunFund().compareTo(detail.getTransactionAmount()) == -1) {// 需要从通用类型内扣钱
									detail.setTransactionAmount(detail.getTransactionAmount().negate());//取反数
									detail.setBalance(BigDecimal.ZERO);
									consumptionMapper.insertSelective(detail);
									UserInfo userInf = new UserInfo();
									userInf.setFunFund(info.get(0).getFunFund().negate());
									userInf.setCurrencyAmount(info.get(0).getFunFund().add(detail.getTransactionAmount()));
									userInf.setMid(detail.getuId());
									userMapper.updateByPrimaryKey(userInf);
									return ResultJson.buildSuccess("消费成功(从通用类扣款"+info.get(0).getFunFund().subtract(detail.getTransactionAmount()).toString()+"):"+pushvo.toString());
								} else {
									detail.setTransactionAmount(detail.getTransactionAmount().negate());
									detail.setBalance(BigDecimal.ZERO);
									consumptionMapper.insertSelective(detail);
									UserInfo userInf = new UserInfo();
									userInf.setFunFund(detail.getTransactionAmount());
									userInf.setMid(detail.getuId());
									userMapper.updateByPrimaryKey(userInf);
									return ResultJson.buildSuccess("消费成功:"+pushvo.toString());
								}
							}
						}
					}else{
						return ResultJson.buildError("当前类型不为康养基金充值，且没有店铺信息");
					}
				}
			}
		}
			return ResultJson.buildError("获取推送数据失败");
	}

	public ResultJson top_ups(ConsumptionDetail detail, Push2VO vo,String phone) {
		detail.setCreateTime(new Date());
		String messageReturn ="";
		Integer flagReturn = -1;
		 Object dataReturn = null;
		if ((vo.getType().equals(7)||vo.getType().equals(8) )&& vo.getPayment().equals("999")) {// 康养基金
			RechargeDetail rechargedetail = new RechargeDetail();
			if(vo.getType().equals(8)){
				rechargedetail.setAmount(detail.getTransactionAmount().negate());
			}else{
				rechargedetail.setAmount(detail.getTransactionAmount());
			}
			rechargedetail.setCreateTime(new Date());
			rechargedetail.setLastModifiedTime(new Date());
			rechargedetail.setRechargeType(1);
			rechargedetail.setuId(detail.getuId());
			rechargedetail.setuName(detail.getuName());
			rechargedetail.setSrcId(detail.getSrcId());
			rechargedetail.setId(detail.getId());
			remoteMapper.insertSelective(rechargedetail);//保存充值记录
			
			BigDecimal livings = rechargedetail.getAmount().multiply(BigDecimal.valueOf(life)).setScale(2,BigDecimal.ROUND_HALF_UP);
			BigDecimal Happy = rechargedetail.getAmount().multiply(BigDecimal.valueOf(fun)).setScale(2,BigDecimal.ROUND_HALF_UP);
			BigDecimal Health = rechargedetail.getAmount().subtract(Happy).subtract(livings);
			UserInfo userInfo = new UserInfo();
			userInfo.setHealthyFund(Health);
			userInfo.setLifeFund(livings);
			userInfo.setFunFund(Happy);
			userInfo.setMid(detail.getuId());
			userInfo.setLastModifiedTime(new Date());
			userInfo.setMid(vo.getMid());
			userInfo.setKyAmount(rechargedetail.getAmount());//设置初始额
			userMapper.updateByPrimaryKey(userInfo);
			
			
			Map<String,Object> submap = new HashMap<>();
			submap.put("membername",detail.getuName());
       	 	submap.put("moneytype", "康养基金");
       	 	submap.put("money",rechargedetail.getAmount());
       	 	submap.put("wname", "康和e家");
       	 	submap.put("companyphone", "028-87774858");
       	 	
       	 	JSONObject paramap = new JSONObject();
       	 
	  		paramap.put("tpl_params", submap);
	  		paramap.put("tplid", "memstoragenotice");
	  		paramap.put("phone",phone);
	  		
	  		List<JSONObject> params = new ArrayList<>();
	  		params.add(paramap);
	  		try {
	  			String url ="http://vip.cdkhms.com/open/ms/message/tplsend/v1?access_token="+accessToken;
				ResultJson resultJson =RemoteMethod.connectionUVipSendMessage(url,params, secretKey);
				if(resultJson.getFlag()!=1) {
					flagReturn = resultJson.getFlag();
					messageReturn=resultJson.getMsg();
					dataReturn= resultJson.getData();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (vo.getType().equals(1)||vo.getType().equals(4) || !vo.getPayment().equals("999")) {// 自行充值
			RechargeDetail rechargedetail = new RechargeDetail();
			if(vo.getType().equals(4)||vo.getType().equals(8)){
				rechargedetail.setAmount(detail.getTransactionAmount().negate());
			}else{
				rechargedetail.setAmount(detail.getTransactionAmount());
			}
			rechargedetail.setCreateTime(new Date());
			rechargedetail.setLastModifiedTime(new Date());
			rechargedetail.setRechargeType(2);
			rechargedetail.setuId(detail.getuId());
			rechargedetail.setuName(detail.getuName());
			rechargedetail.setSrcId(detail.getSrcId());
			rechargedetail.setId(detail.getId());
			remoteMapper.insertSelective(rechargedetail);//充值单
			
			UserInfo userInfo = new UserInfo();
			userInfo.setCurrencyAmount(rechargedetail.getAmount());
			userInfo.setCyAmount(rechargedetail.getAmount());
			userInfo.setMid(detail.getuId());
			userInfo.setLastModifiedTime(new Date());
			userMapper.updateByPrimaryKey(userInfo);//修改账户余额
			
			
			Map<String,Object> submap = new HashMap<>();
			submap.put("membername",detail.getuName());
       	 	submap.put("moneytype", "康养基金");
       	 	submap.put("money",rechargedetail.getAmount());
       	 	submap.put("wname", "康和e家");
       	 	submap.put("companyphone", "028-87774858");
       	 	
       	 	JSONObject paramap = new JSONObject();
       	 
	  		paramap.put("tpl_params", submap);
	  		paramap.put("tplid", "memstoragenotice");
	  		paramap.put("phone",phone);
	  		
	  		List<JSONObject> params = new ArrayList<>();
	  		params.add(paramap);
	  		//returnList.get(0).getPhone()
	  		try {
	  			String url ="http://vip.cdkhms.com/open/ms/message/tplsend/v1?access_token="+accessToken;
				ResultJson resultJson =RemoteMethod.connectionUVipSendMessage(url,params, secretKey);
				if(resultJson.getFlag()!=1) {
					flagReturn = resultJson.getFlag();
					messageReturn=resultJson.getMsg();
					dataReturn= resultJson.getData();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ResultJson.buildSuccess("充值完成:"+vo.toString()+"短信接口返回信息:"+"flag="+flagReturn+"msg="+messageReturn+"data="+dataReturn);
	}

	// 单据查重
	public ConsumptionDetail checkBillID(String id) {
		ConsumptionDetail detail = consumptionMapper.selectByPrimaryKey(id);
		return detail;
	}

	public List<UserInfo> MemberIDToUserInfo(ResultJson json) {
		List<Map<String, Object>> jsonMap = (List<Map<String, Object>>) json.getData();
		List<UserInfo> returnList = new ArrayList<>();
		for(Map<String, Object> map:jsonMap){
			UserInfo user = new UserInfo();
			user.setId(IdGenerator.getUUID());
			user.setPhone((String)map.get("phone"));
			user.setuName((String)map.get("realname"));
			user.setMid(String.valueOf(map.get("mid")));
			user.setFunFund(BigDecimal.valueOf(0));
			user.setLifeFund(BigDecimal.valueOf(0));
			user.setHealthyFund(BigDecimal.valueOf(0));
			user.setCurrencyAmount(BigDecimal.valueOf(0));
			user.setKyAmount(BigDecimal.valueOf(0));
			user.setCyAmount(BigDecimal.valueOf(0));
			user.setCard((String)map.get("card_no"));
			returnList.add(user);
		}
		return returnList;
	}
	// 确认会员存在
	public ResultJson checkMemberID(String id,String realname,String phone) {

		String url = "http://vip.cdkhms.com/open/mm/member/query/v1?access_token=" + accessToken;

		List<Map<String, Object>> conditions = new ArrayList<>();
		if(id!=null){
			Map<String, Object> midMap = new HashMap<>();
			midMap.put("name", "mid");
			midMap.put("value1", id);
			midMap.put("type", "string");
			midMap.put("op", "eq");
			conditions.add(midMap);
		}
		if(realname!=null){
			Map<String, Object> realnameMap = new HashMap<>();
			realnameMap.put("name", "realname");
			realnameMap.put("value1", realname);
			realnameMap.put("type", "string");
			realnameMap.put("op", "like");
			conditions.add(realnameMap);
		}
		if(phone!=null){
			Map<String, Object> phoneMap = new HashMap<>();
			phoneMap.put("name", "phone");
			phoneMap.put("value1", phone);
			phoneMap.put("type", "string");
			phoneMap.put("op", "like");
			conditions.add(phoneMap);
		}
		Map<String, Integer> pager = new HashMap<>();
		pager.put("pageIndex", 1);
		pager.put("pageSize", 10);

		List<Map<String, Object>> orders = new ArrayList<>();
		Map<String, Object> map1 = new HashMap<>();
		map1.put("name", "phone");
		map1.put("order", "desc");
		orders.add(map1);

		List<Map<String, Object>> fields = new ArrayList<>();
		Map<String, Object> m1 = new HashMap<>();
		m1.put("name", "phone");
		fields.add(m1);
		
		Map<String, Object> m3 = new HashMap<>();
		m3.put("name", "storage_balance");
		fields.add(m3);
			
		Map<String, Object> m4 = new HashMap<>();
		m4.put("name", "mid");
		fields.add(m4);
		
		Map<String, Object> m6 = new HashMap<>();
		m6.put("name", "card_no");
		fields.add(m6);
		
		Map<String, Object> m2 = new HashMap<>();
		m2.put("name", "realname");
		fields.add(m2);
		
		Map<String, Object> m5 = new HashMap<>();
		m5.put("name", "card");
		fields.add(m5);
		
		Map<String, Object> MapParam = new HashMap<>();
		MapParam.put("name","identity_num");
		fields.add(MapParam);
		
		JSONObject param = RemoteJson.buildJson(conditions, pager, orders, fields);

		try {
			ResultJson resultJson = RemoteMethod.connectionUVipFindStoreInfo(url, param, secretKey);
			return resultJson;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 获取店铺信息
	@Override
	public ResultJson pullByStoreInfo(String store_Code) {
		String url = "http://vip.cdkhms.com/open/mm/store/query/v1?access_token=" + accessToken;

		List<Map<String, Object>> conditions = new ArrayList<>();

		Map<String, Object> map = new HashMap<>();
		map.put("name", "code");
		map.put("value1", store_Code);
		map.put("type", "string");
		map.put("op", "eq");

		conditions.add(map);

		Map<String, Integer> pager = new HashMap<>();
		pager.put("pageIndex", 1);
		pager.put("pageSize", 10);

		List<Map<String, Object>> orders = new ArrayList<>();
		Map<String, Object> map1 = new HashMap<>();
		map1.put("name", "province");
		map1.put("order", "desc");
		orders.add(map1);

		List<Map<String, Object>> fields = new ArrayList<>();
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
			// System.out.println(resultJson.toString());
			return resultJson;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	@Override
	public ResultJson pullByConsume(String mid,String beginDate,String endDate) {
		String url = "http://vip.cdkhms.com/open/mm/coupon/mycoupon/v1?access_token=" + accessToken;
		
		List<Map<String, Object>> conditions = new ArrayList<>();
		Map<String, Object> midMap = new HashMap<>();
		midMap.put("name", "mid");
		midMap.put("value1", mid);
		midMap.put("type", "string");
		midMap.put("op", "eq");
		conditions.add(midMap);
		
		Map<String, Object> typeMap = new HashMap<>();
		typeMap.put("name", "type");
		typeMap.put("value1", 5);
		typeMap.put("type", "string");
		typeMap.put("op", "eq");
		typeMap.put("entity", "Coupon");
		conditions.add(typeMap);
		
		Map<String, Object> DateMap = new HashMap<>();
		DateMap.put("name", "effect_date");
		DateMap.put("value1", beginDate);
		DateMap.put("type", "datetime");
		DateMap.put("op", "eq");
		DateMap.put("entity", "Coupon");
		conditions.add(DateMap);
		
		Map<String, Integer> pager = new HashMap<>();
		pager.put("pageIndex", 1);
		pager.put("pageSize", 10);
		
		List<Map<String, Object>> orders = new ArrayList<>();
		Map<String, Object> ordersMap = new HashMap<>();
		ordersMap.put("name", "receive_date");
		ordersMap.put("order", "desc");
		orders.add(ordersMap);
		
		List<Map<String, Object>> fields = new ArrayList<>();
		Map<String, Object> returnMidMap = new HashMap<>();
        
        returnMidMap.put("name", "mid");
        fields.add(returnMidMap);
        
        Map<String, Object> returnStatusMap = new HashMap<>();
        returnStatusMap.put("name", "status");
        fields.add(returnStatusMap);
		
		Map<String, Object> returnSnMap = new HashMap<>();
		returnSnMap.put("name", "sn");
		fields.add(returnSnMap);
		
		Map<String, Object> returnTitleMap = new HashMap<>();
		returnTitleMap.put("name", "title");
		returnTitleMap.put("entity", "Coupon");
		fields.add(returnTitleMap);
		
		Map<String, Object> returnAmountMap = new HashMap<>();
		returnTitleMap.put("name", "amount");
		returnTitleMap.put("entity", "Coupon");
		fields.add(returnAmountMap);
		
		Map<String, Object> returnLogoMap = new HashMap<>();
		returnLogoMap.put("name", "logo_url");
		fields.add(returnLogoMap);
		Map<String, Object> returnRemainMap = new HashMap<>();
		returnRemainMap.put("name", "remain_times");
		fields.add(returnRemainMap);
		Map<String, Object> returnReceiveMap = new HashMap<>();
		returnReceiveMap.put("name", "receive_date");
		returnReceiveMap.put("format","Y-m-d H:i:s");
		fields.add(returnReceiveMap);
		Map<String, Object> returnEffectMap = new HashMap<>();
		returnEffectMap.put("name", "effect_date");
		returnEffectMap.put("format","Y-m-d H:i:s");
		fields.add(returnEffectMap);
		Map<String, Object> returnExpMap = new HashMap<>();
		returnExpMap.put("name", "exp_date");
		returnExpMap.put("format","Y-m-d H:i:s");
		fields.add(returnExpMap);
		
		JSONObject param = RemoteJson.buildJson(conditions, pager, orders, fields);
		
		try {
			ResultJson resultJson = RemoteMethod.connectionUVipFindStoreInfo(url,param, secretKey);
			return resultJson;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	
	@Override
	public ResultJson queryUVipStoreInfo() {
		// TODO Auto-generated method stub
		return null;
	}

}
