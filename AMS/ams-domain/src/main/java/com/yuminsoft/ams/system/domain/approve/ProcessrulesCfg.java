package com.yuminsoft.ams.system.domain.approve;

import com.yuminsoft.ams.system.domain.AbstractEntity;

/**
 * 规则配置
 * 
 * @author fusj
 *
 */
public class ProcessrulesCfg extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	private String userCode;// 用户编码
	private String productCode;// 产品编码 
	private String productName;// 产品名称
	private String ruleType;// 收回权限类型(通过:PASS;拒绝:REFUSE;退回:RETURN)
	private String reasonCode;// 原因 code
	private String reasonName;// 原因 名称
//	private String reasonCodeB;//原因 2级code
//	private String reasonNameB;//原因 2级名称
	private Long orgPid;// 大组
	private String orgPname;// 大组名称
	private String orgId;//小组
	private String orgName;//小组名称
	private String applyType;//申请类型(新申请:NEW;追加贷款:TOPUP; 结清再贷:RELOAN)----此字段不用了
	private String userName;// 用户名称

	/*
	 * private List<ProcessrulesCfg> list ; public List<ProcessrulesCfg> getList() { return list; } public void setList(List<ProcessrulesCfg> list) { this.list = list; }
	 */
	public String getReasonName() {
		return reasonName;
	}

	public void setReasonName(String reasonName) {
		this.reasonName = reasonName;
	}

	public Long getOrgPid() {
		return orgPid;
	}

	public void setOrgPid(Long orgPid) {
		this.orgPid = orgPid;
	}

	public String getOrgPname() {
		return orgPname;
	}

	public void setOrgPname(String orgPname) {
		this.orgPname = orgPname;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getApplyType() {
		return applyType;
	}

	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getRuleType() {
		return ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}

	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}


}
