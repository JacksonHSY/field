package com.yuminsoft.ams.system.domain.quality;

import com.yuminsoft.ams.system.domain.AbstractEntity;

/**
 * 质检反馈记录表
 * @author fuhongxing
 */
public class QualityFeedBack extends AbstractEntity {


    private static final long serialVersionUID = 5270636150019712947L;

    private String type;    //质检反馈结论(确认F_000000、争议F_000001、仲裁F_000002、定版F_000003)
    private String opinion; //审批反馈意见
    private Long checkResId;  //对应的质检结论表ID
    private String nodeName;     //当前工作流节点名称
    private String checkType; 	//0初审 1终审
    private String checkError; //差错级别
    private String errorCode; //错误代码

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOpinion() {
		return opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

	public Long getCheckResId() {
		return checkResId;
	}

	public void setCheckResId(Long checkResId) {
		this.checkResId = checkResId;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getCheckType() {
		return checkType;
	}

	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}

	public String getCheckError() {
		return checkError;
	}

	public void setCheckError(String checkError) {
		this.checkError = checkError;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}
