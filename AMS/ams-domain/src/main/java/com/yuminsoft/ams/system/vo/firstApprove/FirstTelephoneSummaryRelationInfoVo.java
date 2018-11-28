package com.yuminsoft.ams.system.vo.firstApprove;

import com.yuminsoft.ams.system.domain.AbstractEntity;

public class FirstTelephoneSummaryRelationInfoVo extends AbstractEntity {

	/**
	 * 电核总汇 联系人信息
	 * 
	 * @author luting
	 * @date 2017年2月17日 上午9:24:11
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @author  luting
	 * @date    2017年2月20日 上午9:53:58
	 */
	public FirstTelephoneSummaryRelationInfoVo() {
		super();
	}

	private String name;// 姓名

	private String relationship;// 与申请人关系

	private String isKnow;// 是否知晓贷款

	private String telRelationType;// 接听人关系

	private String companyName;// 公司名称

	private String mobileNumber;// 手机

	private String job;// 职务

	private String telephoneNumber;// 公司电话

	/**
	 * @author  luting
	 * @date    2017年2月20日 上午9:54:15
	 */
	public FirstTelephoneSummaryRelationInfoVo(String name, String relationship, String isKnow, String telRelationType,
			String companyName, String mobileNumber, String job, String telephoneNumber) {
		super();
		this.name = name;
		this.relationship = relationship;
		this.isKnow = isKnow;
		this.telRelationType = telRelationType;
		this.companyName = companyName;
		this.mobileNumber = mobileNumber;
		this.job = job;
		this.telephoneNumber = telephoneNumber;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the relationship
	 */
	public String getRelationship() {
		return relationship;
	}

	/**
	 * @param relationship
	 *            the relationship to set
	 */
	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	/**
	 * @return the isKnow
	 */
	public String getIsKnow() {
		return isKnow;
	}

	/**
	 * @param isKnow
	 *            the isKnow to set
	 */
	public void setIsKnow(String isKnow) {
		this.isKnow = isKnow;
	}

	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @param companyName
	 *            the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * @return the telRelationType
	 */
	public String getTelRelationType() {
		return telRelationType;
	}

	/**
	 * @param telRelationType
	 *            the telRelationType to set
	 */
	public void setTelRelationType(String telRelationType) {
		this.telRelationType = telRelationType;
	}

	/**
	 * @return the mobileNumber
	 */
	public String getMobileNumber() {
		return mobileNumber;
	}

	/**
	 * @param mobileNumber
	 *            the mobileNumber to set
	 */
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	/**
	 * @return the job
	 */
	public String getJob() {
		return job;
	}

	/**
	 * @param job
	 *            the job to set
	 */
	public void setJob(String job) {
		this.job = job;
	}

	/**
	 * @return the telephoneNumber
	 */
	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	/**
	 * @param telephoneNumber
	 *            the telephoneNumber to set
	 */
	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

}
