package com.yuminsoft.ams.system.domain.quality;


import com.yuminsoft.ams.system.domain.AbstractEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Version;
import java.util.Date;

/**
 * 质检申请件信息表
 * @author fuhongxing
 */
@Data
@ToString
public class QualityCheckInfo extends AbstractEntity {

	private static final long serialVersionUID = -1744445565539869576L;

	/**
     * 借款编号
     */
	private String loanNo;
    /**
     * 分派时间，把抽取的申请件分派给质检员的时间
     */
    private Date assignDate;
    /**
     * 质检状态(0已完成，1未完成， 2申请复核)
     */
    private String checkStatus;
    /**
     * 完成时间
     */
    private Date endDate;
    /**
     * 质检员
     */
    private String checkUser;
    /**
     * 申请件状态 0：通过件，1拒绝件
     */
    private Integer approvalStatus;
    /**
     * 来源
     */
    private String source;
    /**
     * 是否冗余
     */
    private String redundant;
    /**
     * 是否关闭 0-是，1-否
     */
    private String isClosed;
    
    /**
     * 是否常规，0-是，1-否
     */
    private String isRegular;
    //下面字段存放在借款系统，通过loanNo关联获得
	/**
     * 客户姓名
     */
    private String customerName;

    /**
     * 客户信息明文
     */
    private String customerNameBak;
    /**
     * 身份证号码
     */
    private String idNo;

    /**
     * 身份证明文
     */
    private String idNoBak;
    /**
     * 进件营业部ID
     */
	private String owningBranceId;
	/**
     * 进件营业部
     */
	private String owningBrance;
	/**
	 * 申请日期
	 */
	private Date applyDate;
	/**
	 * 贷款类型
	 */
	private String productTypeName;
	/**
	 * 审批结果
	 */
	private String approveState;
	/**
	 * 初审工号
	 */
    private String checkPerson;
    /**
     * 终审工号
     */
    private String finalPerson;
    /**
     * 审批日期
     */
    private Date approveDate;
    /**
     * 派单类型
     */
    private String assignType;
    /**
     *客户类型
     * @return
     */
    private String customerType;
    /**
     * 二级拒绝原因code
     */
    private String refuseCodeLevelTwo;
    /**
     * 二级拒绝原因名称
     */
    private String refuseLevelTwoName;
    /**
     * 一级拒绝原因code
     */
    private String refuseCodeLevelOne;
    /**
     * 一级拒绝原因名称
     */
    private String refuseLevelOneName;
    /*
     * 更新版本号
     */
    @Version
    private Long version;

    public String getIdNoBak() {
        return this.idNo;
    }

    public String getCustomerNameBak() {
        return this.customerName;
    }
}