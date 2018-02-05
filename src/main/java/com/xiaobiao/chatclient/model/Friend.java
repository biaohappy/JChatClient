package com.xiaobiao.chatclient.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 朋友
 * @author wuxiaobiao
 * @create 2018-01-16 15:05
 * To change this template use File | Settings | Editor | File and Code Templates.
 **/
@Getter
@Setter
public class Friend {

    /**
     * 分组名称
     */
    private String groupname;
    /**
     * 组id
     */
    private String id;
    /**
     * 在线个数
     */
    private String online;
    /**
     * 朋友资料
     */
    private List<Mine> list;

}
