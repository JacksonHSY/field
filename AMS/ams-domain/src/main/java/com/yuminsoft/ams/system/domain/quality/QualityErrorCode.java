package com.yuminsoft.ams.system.domain.quality;

import com.yuminsoft.ams.system.domain.AbstractEntity;

/**
 * 质检差错代码
 * @author YM10174
 *
 */
public class QualityErrorCode extends AbstractEntity{
	private static final long serialVersionUID = 1L;
	/**
	 * 差错代码
	 */
	private String code;
	/**
	 * 差错代码名称
	 */
	private String name;
	/**
	 * 是否开启 Y，N
	 */
	private String status;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	
}
