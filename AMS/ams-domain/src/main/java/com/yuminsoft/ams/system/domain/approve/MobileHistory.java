package com.yuminsoft.ams.system.domain.approve;

import com.yuminsoft.ams.system.domain.AbstractEntity;
import lombok.Data;
import lombok.ToString;

/**
 * 电话调查信息表(电核汇总)
 * @author fuhongxing
 */
@Data
@ToString
public class MobileHistory extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	private String loanNo;//申请件编号

    private String name;//姓名

    private String nameBak;// 姓名明文

    private String pri;//优先级

    private String threeSource;//第三方来源

    private String telRelationType;//接听人关系

    private String telPhoneType;//电话类型

    private String telPhone;//致电电话

    private String telPhoneBak;

    private String telDate;//核查时间

    private String askTypeOne;//问题1

    private String askTypeTwo;//问题2

    private String askContent;//电核备注

    private String nameTitle;//关系描述

    private String telPhoneTypeCn;//电话类型描述

    private String relationTypeSort;//联系人排序标识
    
    private String phoneCity;//手机归属地
    
    private String carrier; //运营商

    public String getTelPhoneBak() {
        return this.getTelPhone();
    }

    public String getNameBak() {
        return this.getName();
    }
}