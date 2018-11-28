package com.yuminsoft.ams.api.vo.response;

import com.yuminsoft.ams.api.vo.AbstractEntity;

/**
 * 电话调查信息表(电核汇总)
 * @author fuhongxing
 */
public class ResMobileHistory extends AbstractEntity {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String loanNo;//申请件编号

    private String name;//姓名

    private String pri;//优先级

    private String threeSource;//第三方来源

    private String telRelationType;//接听人关系

    private String telPhoneType;//电话类型

    private String telPhone;//致电电话

    private String telDate;//核查时间

    private String askTypeOne;//问题1

    private String askTypeTwo;//问题2

    private String askContent;//电核备注

    private String nameTitle;//关系描述

    private String telPhoneTypeCn;//电话类型描述

    private String relationTypeSort;//联系人排序标识

	public String getLoanNo() {
		return loanNo;
	}

	public void setLoanNo(String loanNo) {
		this.loanNo = loanNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPri() {
		return pri;
	}

	public void setPri(String pri) {
		this.pri = pri;
	}

	public String getThreeSource() {
		return threeSource;
	}

	public void setThreeSource(String threeSource) {
		this.threeSource = threeSource;
	}

	public String getTelRelationType() {
		return telRelationType;
	}

	public void setTelRelationType(String telRelationType) {
		this.telRelationType = telRelationType;
	}

	public String getTelPhoneType() {
		return telPhoneType;
	}

	public void setTelPhoneType(String telPhoneType) {
		this.telPhoneType = telPhoneType;
	}

	public String getTelPhone() {
		return telPhone;
	}

	public void setTelPhone(String telPhone) {
		this.telPhone = telPhone;
	}

	public String getTelDate() {
		return telDate;
	}

	public void setTelDate(String telDate) {
		this.telDate = telDate;
	}

	public String getAskTypeOne() {
		return askTypeOne;
	}

	public void setAskTypeOne(String askTypeOne) {
		this.askTypeOne = askTypeOne;
	}

	public String getAskTypeTwo() {
		return askTypeTwo;
	}

	public void setAskTypeTwo(String askTypeTwo) {
		this.askTypeTwo = askTypeTwo;
	}

	public String getAskContent() {
		return askContent;
	}

	public void setAskContent(String askContent) {
		this.askContent = askContent;
	}

	public String getNameTitle() {
		return nameTitle;
	}

	public void setNameTitle(String nameTitle) {
		this.nameTitle = nameTitle;
	}

	public String getTelPhoneTypeCn() {
		return telPhoneTypeCn;
	}

	public void setTelPhoneTypeCn(String telPhoneTypeCn) {
		this.telPhoneTypeCn = telPhoneTypeCn;
	}

	public String getRelationTypeSort() {
		return relationTypeSort;
	}

	public void setRelationTypeSort(String relationTypeSort) {
		this.relationTypeSort = relationTypeSort;
	}

    

}