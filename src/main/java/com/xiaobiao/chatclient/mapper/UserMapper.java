package com.xiaobiao.chatclient.mapper;


import com.xiaobiao.chatclient.param.UserParam;
import com.xiaobiao.chatclient.result.UserResult;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(UserParam record);

    UserParam selectUserInfo(Integer id);

    int updateByPrimaryKeySelective(UserParam record);

    List<UserResult> selectUserList(UserParam record);
}