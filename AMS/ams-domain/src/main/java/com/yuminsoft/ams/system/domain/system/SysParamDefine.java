package com.yuminsoft.ams.system.domain.system;

import com.yuminsoft.ams.system.domain.AbstractEntity;

/**
 * 系统参数
 * @author fuhongxing
 */
public class SysParamDefine extends AbstractEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1112133414699477018L;

    private String paramKey;//参数key值

    private String paramType;//参数类型(JOB、系统参数)

    private String paramName;//参数中文名称

    private String paramValue;//参数value

    private String paramDesc;//描述
    
    private String paramStatus;//状态(参数是否有效0有效，1无效)
    
    private String memo;//备注

	public String getParamKey() {
		return paramKey;
	}

	public void setParamKey(String paramKey) {
		this.paramKey = paramKey;
	}

	public String getParamType() {
		return paramType;
	}

	public void setParamType(String paramType) {
		this.paramType = paramType;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	public String getParamDesc() {
		return paramDesc;
	}

	public void setParamDesc(String paramDesc) {
		this.paramDesc = paramDesc;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getParamStatus() {
		return paramStatus;
	}

	public void setParamStatus(String paramStatus) {
		this.paramStatus = paramStatus;
	}
    
}