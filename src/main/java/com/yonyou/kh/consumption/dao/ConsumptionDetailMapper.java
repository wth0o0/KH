package com.yonyou.kh.consumption.dao;

import java.util.List;

import com.yonyou.kh.consumption.entity.ConsumptionDetail;

public interface ConsumptionDetailMapper {
    int deleteByPrimaryKey(String id);

    int insert(ConsumptionDetail record);

    int insertSelective(ConsumptionDetail record);

    ConsumptionDetail selectByPrimaryKey(String id);
    //类型余额
    ConsumptionDetail selectByType(Integer TransactionType,String uId);
    
    List<ConsumptionDetail> selectByTypeAndUids(List<String> TransactionType,List<String> uId);

    int updateByPrimaryKeySelective(ConsumptionDetail record);

    int updateByPrimaryKey(ConsumptionDetail record);
}