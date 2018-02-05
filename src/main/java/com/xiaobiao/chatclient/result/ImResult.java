package com.xiaobiao.chatclient.result;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wuxiaobiao
 * @create 2018-01-19 13:22
 * To change this template use File | Settings | Editor | File and Code Templates.
 **/
@Getter
@Setter
public class ImResult {

    private String code;

    private String msg;

    private FriendResult data;
}
