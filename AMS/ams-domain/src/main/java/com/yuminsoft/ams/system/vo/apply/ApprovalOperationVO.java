package com.yuminsoft.ams.system.vo.apply;

import com.google.common.collect.Lists;
import com.yuminsoft.ams.system.domain.DebtsInfo;
import com.yuminsoft.ams.system.domain.approve.ApprovalHistory;
import com.yuminsoft.ams.system.domain.approve.ApproveCheckData;
import com.yuminsoft.ams.system.domain.approve.ApproveCheckInfo;
import com.yuminsoft.ams.system.domain.approve.ApproveCheckStatement;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 审批操作VO
 * 
 * @author dmz
 * @date 2017年3月31日
 */
@Data
public class ApprovalOperationVO implements Serializable {
	private static final long serialVersionUID = -4646613501314119729L;
	// 借款编号
	private String loanNo;
	// 审批意见-接收参数
	private ApprovalHistory approvalHistory ;
	// 审批意见-页面展示
	private List<ApprovalHistory> approvalHistoryList = Lists.newArrayList();
	// 资料核对
	private List<ApproveCheckData> approveCheckDataList;
	// 资料核对-汇总
	private ApproveCheckInfo approveCheckInfo;
	// 资料核对-明细
	private List<ApproveCheckStatement> approveCheckStatementList;
	// 负债信息
	private List<DebtsInfo> debtsInfoList;
	// 是否新生件
	private String ifNewLoanNo;
	//版本控制
	private Long version;
}
