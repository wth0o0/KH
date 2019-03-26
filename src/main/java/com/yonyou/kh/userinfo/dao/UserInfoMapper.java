package com.yonyou.kh.userinfo.dao;

import java.util.ArrayList;
import java.util.List;

import com.yonyou.kh.userinfo.entity.UserInfo;

public interface UserInfoMapper {
    int deleteByPrimaryKey(String id);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    List<UserInfo> selectByPrimaryKey(String memberId,String uName,String phone);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);
}