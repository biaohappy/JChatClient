package com.xiaobiao.chatclient.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wuxiaobiao
 * @create 2018-01-16 15:04
 * To change this template use File | Settings | Editor | File and Code Templates.
 **/
@Getter
@Setter
public class Mine {

    /**
     * 用户id
     */
    private String id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 是否在线
     */
    private String status;
    /**
     * 签名
     */
    private String sign;
    /**
     * 头像图片地址
     */
    private String avatar;
}
