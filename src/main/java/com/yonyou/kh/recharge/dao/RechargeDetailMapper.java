package com.yonyou.kh.recharge.dao;

import com.yonyou.kh.recharge.entity.RechargeDetail;

public interface RechargeDetailMapper {
    int deleteByPrimaryKey(String id);

    int insert(RechargeDetail record);

    int insertSelective(RechargeDetail record);

    RechargeDetail selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(RechargeDetail record);

    int updateByPrimaryKey(RechargeDetail record);
}