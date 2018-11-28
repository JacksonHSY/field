package com.yuminsoft.ams.system.common;

/**
 * 公共常量
 * @author fuhongxing
 */
public class AmsConstants {
	/**
	 * 序列ID属性名
	 */
	public static final String SERIAL_VERSION_UID="serialVersionUID";
	
	public static final String YES="Y";
	
	public static final String NO="N";
	
	public static final int ZERO=0;
	
	public static final int ONE=1;
	
	public static final int TWO=2;

	public static final String T = "t"; //在职

	public static final String F = "f"; //不在职
	/**错误提示*/
	public static final String DEFAULT_ERROR_MESSAGE = "系统忙，请稍后再试";
	/**反馈工作流节点常量*/
	public static final String CONFIRM = "F_000000";//确认
	public static final String CONFUSE = "F_000001";//争议
	public static final String ARBITRATION = "F_000002";//仲裁
	public static final String CONCLUSION = "F_000003";//定版

	//任务节点名
//	public static final String checkerSuggest = "质检员反馈";
	public static final String checkTeamLeaderCheck = "质检组长反馈";
	public static final String checkDirectorCheck = "质检主管核对";
	public static final String checkManagerCheck = "质检经理定版";
	public static final String infoTeamLeaderCheck = "信审组长一次反馈";
	public static final String infoTeamLeaderRecheck = "信审组长二次反馈";
	public static final String infoDirectorCheck = "信审主管一次反馈";
	public static final String infoDirectorRecheck = "信审主管二次反馈";
	public static final String infoManagerArb = "信审经理申请仲裁";
	public static final String checkManagerArb = "质检经理申请仲裁定版";

	/*终审审批级别*/
	public static final String L1 = "L1";
	public static final String L2 = "L2";
	public static final String L3 = "L3";
	public static final String L4 = "L4";
	
	public static final String SQJWH = "申请件维护";
	public static final String SQJWH_PASSEDDOC_REFUSE = "通过件_拒绝";
	public static final String SQJWH_PASSEDDOC_EDIT = "通过件_修改";
	public static final String SQJWH_REFUSEDDOC_EDIT = "拒绝件_修改";
	
	/*工作流改变-注意这里的文字请和按照工作流流程图保持一致*/
	public static final String TO_START = "to开始";
	public static final String TO_FINAL = "to终审";
	public static final String TO_FIRST_CHECK = "to初审复核";
	public static final String TO_FIRST_START = "to 开始";
	public static final String TO_FIRST_APPROVAL = "to 初审";
	public static final String TO_FINAL_APPROVAL = "to 终审";
    public static final String TO_APPROVAL = "to 协审";
	
	/**终审虚拟处理人*/
	public static final String FINAL_VIRTUAL_PERSON = "finalAutoDispatch";
	
	/**终审当前审批意见状态*/
	public static final String FINAL_CURRENT_OPINION_STATE = "XSZS-CURRENT";
	
	/**终审改派导出文件名*/
	public static final String FINAL_REFORM_EXPORT_FILENAME = "终审改派工作台导出数据.xlsx";
	
	/**终审改派到出文件每一列的名字*/
	public static final String[] FINAL_REFORM_EXPORT_COLUMN = {"案件标识", "借款单号", "提交时间", "申请人姓名", "身份证号码", "借款产品", "营业部", "营业部属性", "处理人", "申请件层级", "初审姓名", "初审小组", "提交额度", "所在队列"};
	
	/**终审操作类型--已分派申请件正常操作*/
	public static final String FINAL_APPROVE_TYPE_DISPATCHED_NORMAL = "1";
	
	/**终审操作类型--已分派申请件批量操作*/
	public static final String FINAL_APPROVE_TYPE_DISPATCHED_BATCH = "2";
	
	/**终审操作类型--未分派申请件正常操作(现在还没有这个操作)*/
	public static final String FINAL_APPROVE_TYPE_UNDISPATCH_NORMAL = "3";
	
	/**终审操作类型--未分派申请件批量操作*/
	public static final String FINAL_APPROVE_TYPE_UNDISPATCH_BATCH = "4";

	public static final String CHANNEL_ZENDAI = "00001";	// 借款渠道-证大P2P
}
