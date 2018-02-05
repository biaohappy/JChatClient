package com.xiaobiao.chatclient.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wuxiaobiao
 * @create 2017-10-09 9:58
 * To change this template use File | Settings | Editor | File and Code Templates.
 **/
@Getter
@Setter
public class Music implements Serializable {
    private static final long serialVersionUID = -5856054685382997028L;

    private Integer id;
    private String musicName;
    private String url;
    private String userCode;
    private String deleteFlag;
    private Date createTime;

    @Override
    public String toString() {
        return "Music{" +
                "id=" + id +
                ", musicName='" + musicName + '\'' +
                ", url='" + url + '\'' +
                ", userCode='" + userCode + '\'' +
                ", deleteFlag='" + deleteFlag + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
