package com.yuminsoft.ams.system.domain.approve;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 复议员工表
 * 
 * @author Jia CX
 * <p>2018年6月12日 下午2:28:48</p>
 * 
 */
@Data
@ToString
public class ReconsiderStaff implements Serializable {
	
	private static final long serialVersionUID = -4479890070895479028L;
	
	private Long id;
	
	/** 员工编号*/
	private String staffCode;
	
	/** 员工邮箱*/
	private String staffEmail;
	
	/** 员工名称*/
	private String staffName;
	
	/** 是否接单(Y/N)*/
	private String ifAccept;
	
	/** 角色等级(多个用逗号隔开)*/
	private String ruleLevel;
	
	/** 员工状态（0-正常；1-非正常）*/
	private String status;
	
	/** 创建人*/
	private String createdBy;
	
	/** 创建时间*/
	private String createdDate;
	
	/** 修改人*/
	private String lastModifiedBy;
	
	/** 修改时间*/
	private String lastModifiedDate;
}
