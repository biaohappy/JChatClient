package com.xiaobiao.chatclient.result;

import com.xiaobiao.chatclient.model.Friend;
import com.xiaobiao.chatclient.model.Group;
import com.xiaobiao.chatclient.model.Mine;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author wuxiaobiao
 * @create 2018-01-18 17:45
 * To change this template use File | Settings | Editor | File and Code Templates.
 **/
@Getter
@Setter
public class FriendResult {

    private Mine mine;

    private List<Friend> friend;

    private List<Group> group;
}
