package com.yuminsoft.ams.system.common;

/**
 * Created by ZJY on 2017/3/13.
 * 用于存储以下几个角色编码
 */
//TODO 用真正的编码代替双引号里的内容
public enum FeedbackEnum {
    //角色编码
    groupLeader("groupLeader"), //组长角色编码
    deptDirector("deptDirector"),//主管角色编码
    deptManager("deptManager"); //经理角色编码

    private String value;

    private FeedbackEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
