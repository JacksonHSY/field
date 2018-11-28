package com.yuminsoft.ams.system.vo.quality;

import lombok.Data;
import lombok.ToString;

/**
 * 质检查询_导出Vo
 * @author YM10174
 *
 */
@Data
@ToString
public class QualityControlQueryExportVo {
	
	/**
     * 完成日期
     */
    private String endDate;
	/**
     * 客户姓名
     */
    private String customerName;
    /**
     * 身份证号码
     */
    private String idNo;
    /**
     * 客户类型
     */
    private String customerType;
    /**
     * 进件营业部
     */
	private String owningBrance;
	/**
     * 申请来源
     */
    private String source;
    /**
     * 申请件编号
     */
	private String loanNo;
	/**
	 * 贷款类型
	 */
	private String productTypeName;
	/**
	 * 审批结果
	 */
	private String approvalStatus;
    /**
     * 初审姓名
     */
    private String checkPersonName;
    /**
     * 终审姓名
     */
    private String finalPersonName;
    /**
     * 审批日期
     */
    private String approveDate;
    /**
     * 拒绝原因
     */
    private String refuseReasonEmbed;
    /**
     * 差错类型
     */
    private String checkResult;
    /**
     * 质检人员
     */
    private String checkUser;
    
    
}
