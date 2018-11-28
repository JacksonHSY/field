package com.yuminsoft.ams.system.vo.apply;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 接单能力配置VO
 * 
 * @author dmz
 * @date 2017年3月29日
 */
@Data
@ToString
public class StaffAbilityVO implements Serializable {
	private static final long serialVersionUID = 4011935777389333130L;
	// Fiedes
	private String[] productCode;// 产品code
	private String[] abilityValue;// 能力值
	private String[] areaCode;// 地区code
	private String type;// 区分是机构还是员工(org/staff)
	private String code;// 机构Id或者是员工code
	private String taskDefId; //初审，直通车初审标识
	// Constructors

	/**
	 * default constructor
	 */
	public StaffAbilityVO() {

	}
}
