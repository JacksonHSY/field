package com.yuminsoft.ams.system.domain.quality;

import com.yuminsoft.ams.system.domain.AbstractEntity;

import lombok.Data;
import lombok.ToString;


/**
 * 质检差错代码配置表
 * @author YM10174
 *
 */
@Data
@ToString
public class QualityErrorList extends AbstractEntity{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	
	private String level;

}
