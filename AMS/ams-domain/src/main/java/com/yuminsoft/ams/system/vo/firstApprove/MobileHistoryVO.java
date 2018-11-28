package com.yuminsoft.ams.system.vo.firstApprove;

import com.yuminsoft.ams.system.domain.AbstractEntity;

/**
 * 电话调查信息表(电核汇总)
 * 
 * @author zhouwen 2017-06-26
 */
public class MobileHistoryVO extends AbstractEntity {
	// public static final String TABLE_NAME = "AMS_Mobile_HISTORY";
	// public static final String TABLE_NAME = "TM_APP_HISTORY_DETAIL";//对应征审

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String loanNo;// 申请件编号

	private String name;// 姓名

	private String pri;// 优先级

	private String threeSource;// 第三方来源

	private String telRelationType;// 接听人关系

	private String telPhoneType;// 电话类型

	private String telPhone;// 致电电话

	private String telDate;// 核查时间

	private String askTypeOne;// 问题1

	private String askTypeTwo;// 问题2

	private String askContent;// 电核备注

	private String nameTitle;// 关系描述

	private String telPhoneTypeCn;// 电话类型描述

	private String relationTypeSort;// 联系人排序标识

	private boolean flag = true;// 操作标识,修改删除

	private String createdBy;// 创建人

	private String createdByCode;// 创建人工号


	/**

	 * <p>
	 * 申请件编号
	 * </p>
	 * <p>
	 * 申请件编号
	 * </p>
	 */
	public String getLoanNo() {
		return loanNo;
	}

	/**
	 * <p>
	 * 申请件编号
	 * </p>
	 * <p>
	 * 申请件编号
	 * </p>
	 */
	public void setLoanNo(String loanNo) {
		this.loanNo = loanNo;
	}

	/**
	 * <p>
	 * 姓名
	 * </p>
	 */
	public String getName() {
		return name;
	}

	/**
	 * <p>
	 * 姓名
	 * </p>
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * <p>
	 * 优先级
	 * </p>
	 */
	public String getPri() {
		return pri;
	}

	/**
	 * <p>
	 * 优先级
	 * </p>
	 */
	public void setPri(String pri) {
		this.pri = pri;
	}

	/**
	 * <p>
	 * 第三方来源
	 * </p>
	 */
	public String getThreeSource() {
		return threeSource;
	}

	/**
	 * <p>
	 * 第三方来源
	 * </p>
	 */
	public void setThreeSource(String threeSource) {
		this.threeSource = threeSource;
	}

	/**
	 * <p>
	 * 接听人关系
	 * </p>
	 */
	public String getTelRelationType() {
		return telRelationType;
	}

	/**
	 * <p>
	 * 接听人关系
	 * </p>
	 */
	public void setTelRelationType(String telRelationType) {
		this.telRelationType = telRelationType;
	}

	/**
	 * <p>
	 * 电话类型
	 * </p>
	 */
	public String getTelPhoneType() {
		return telPhoneType;
	}

	/**
	 * <p>
	 * 电话类型
	 * </p>
	 */
	public void setTelPhoneType(String telPhoneType) {
		this.telPhoneType = telPhoneType;
	}

	/**
	 * <p>
	 * 致电电话
	 * </p>
	 */
	public String getTelPhone() {
		return telPhone;
	}

	/**
	 * <p>
	 * 致电电话
	 * </p>
	 */
	public void setTelPhone(String telPhone) {
		this.telPhone = telPhone;
	}

	/**
	 * <p>
	 * 核查时间
	 * </p>
	 */
	public String getTelDate() {
		return telDate;
	}

	/**
	 * <p>
	 * 核查时间
	 * </p>
	 */
	public void setTelDate(String telDate) {
		this.telDate = telDate;
	}

	/**
	 * <p>
	 * 问题1
	 * </p>
	 */
	public String getAskTypeOne() {
		return askTypeOne;
	}

	/**
	 * <p>
	 * 问题1
	 * </p>
	 */
	public void setAskTypeOne(String askTypeOne) {
		this.askTypeOne = askTypeOne;
	}

	/**
	 * <p>
	 * 问题2
	 * </p>
	 */
	public String getAskTypeTwo() {
		return askTypeTwo;
	}

	/**
	 * <p>
	 * 问题2
	 * </p>
	 */
	public void setAskTypeTwo(String askTypeTwo) {
		this.askTypeTwo = askTypeTwo;
	}

	/**
	 * <p>
	 * 电核备注
	 * </p>
	 */
	public String getAskContent() {
		return askContent;
	}

	/**
	 * <p>
	 * 电核备注
	 * </p>
	 */
	public void setAskContent(String askContent) {
		this.askContent = askContent;
	}

	/**
	 * <p>
	 * 关系描述
	 * </p>
	 * <p>
	 * 关系描述
	 * </p>
	 */
	public String getNameTitle() {
		return nameTitle;
	}

	/**
	 * <p>
	 * 关系描述
	 * </p>
	 * <p>
	 * 关系描述
	 * </p>
	 */
	public void setNameTitle(String nameTitle) {
		this.nameTitle = nameTitle;
	}

	/**
	 * <p>
	 * 电话类型描述
	 * </p>
	 * <p>
	 * 电话类型描述
	 * </p>
	 */
	public String getTelPhoneTypeCn() {
		return telPhoneTypeCn;
	}

	/**
	 * <p>
	 * 电话类型描述
	 * </p>
	 * <p>
	 * 电话类型描述
	 * </p>
	 */
	public void setTelPhoneTypeCn(String telPhoneTypeCn) {
		this.telPhoneTypeCn = telPhoneTypeCn;
	}

	/**
	 * <p>
	 * 联系人排序标识
	 * </p>
	 * <p>
	 * 联系人排序标识
	 * </p>
	 */
	public String getRelationTypeSort() {
		return relationTypeSort;
	}

	/**
	 * <p>
	 * 联系人排序标识
	 * </p>
	 * <p>
	 * 联系人排序标识
	 * </p>
	 */
	public void setRelationTypeSort(String relationTypeSort) {
		this.relationTypeSort = relationTypeSort;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	@Override
	public String getCreatedBy() {
		return createdBy;
	}

	@Override
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedByCode() {
		return createdByCode;
	}

	public void setCreatedByCode(String createdByCode) {
		this.createdByCode = createdByCode;
	}
}
