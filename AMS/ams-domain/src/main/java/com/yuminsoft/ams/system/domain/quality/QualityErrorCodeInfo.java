package com.yuminsoft.ams.system.domain.quality;

import com.yuminsoft.ams.system.domain.AbstractEntity;

import lombok.Data;
import lombok.ToString;


/**质检差错代码展示列表
 * @author YM10174
 *
 */
@Data
@ToString
public class QualityErrorCodeInfo extends AbstractEntity{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//编码
	private String code;
	//是否启用，0启用，1禁用，2删除
	private String isUse;
	//大类ID
	private int parentId;
	//大类描述
	private String parentDesc;
	//一级代码ID
	private int firstId;
	//一级代码描述
	private String firstDesc;
	//二级代码ID
	private int secondId;
	//二级代码描述
	private String secondDesc;
	
	

}
