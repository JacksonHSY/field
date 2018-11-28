package com.yuminsoft.ams.system.domain.quality;

import com.yuminsoft.ams.system.domain.AbstractEntity;

/**
 * 质检来源信息表
 * @author YM10174
 */
public class QualitySourceInfo extends AbstractEntity {

	private static final long serialVersionUID = -7580526541305271927L;
	
	/**
     * 申请来源
     */
	private String qualitySource;
	/**
     * 是否开启 Y N
     */
    private String status;
	public String getQualitySource() {
		return qualitySource;
	}
	public void setQualitySource(String qualitySource) {
		this.qualitySource = qualitySource;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    

}