package com.xiaobiao.chatclient.mapper;

import com.xiaobiao.chatclient.param.ChatGroup;

import java.util.List;

public interface ChatGroupMapper {
    int deleteByPrimaryKey(String id);

    int insert(ChatGroup record);

    int insertSelective(ChatGroup record);

    ChatGroup selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ChatGroup record);

    int updateByPrimaryKey(ChatGroup record);

    List<ChatGroup> queryGroups(String id);
}