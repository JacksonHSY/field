package com.yuminsoft.ams.system.common;


import java.util.Arrays;
import java.util.List;
/**
 * 质检枚举
 *
 * @author fuhongxing
 */
public class QualityEnum {

    /**
     * 质检状态
     */
    public enum CheckStatus{

        CHECK_COMPLETE("0", "已质检"),
        CHECK_WAIT("1", "待质检");

        private CheckStatus(String code, String value){
            this.code = code;
            this.value = value;
        }

        private String code;

        private String value;

        public String getCode(){
            return code;
        }

        public String getValue(){
            return value;
        }
    }

    /**
     * 质检意见类型
     * @desc 初审质检意见 or 终审质检意见 or 初审组长质检意见
     * @author wulinjie
     */
    public enum CheckPart {

        CHECK_PART("checkPart", "初审质检意见"),
        FINAL_PART("finalPart", "终审质检意见"),
        CHECK_LEADER_PART("checkLeaderPart", "初审组长质检意见");

        private CheckPart(String code, String value){
            this.code = code;
            this.value = value;
        }

        private String code;

        private String value;

        public String getCode(){
            return code;
        }

        public String getValue(){
            return value;
        }
    }

    /**
     * 差错类型
     */
    public enum CheckResult{

        SERIOUS_ERROR("E_000004","重大差错"),
        GENERAL_ERROR("E_000003","一般差错"),
        SUGGESTION("E_000002","建议"),
        WARN("E_000001","预警"),
        NONE_ERROR("E_000000","无差错");

        private CheckResult(String code, String value){
            this.code = code;
            this.value = value;
        }

        private String code;

        private String value;

        public String getCode(){
            return code;
        }

        public String getValue(){
            return value;
        }

    }

    public enum MenuFlag {

        /**
         * 待处理
         */
        质检待处理("todo", "质检待处理"),
        /**
         * 已完成
         */
        质检综合查询("dono", "质检综合查询"),
        /**
         * 已完成列表
         */
        质检已完成列表("doneList", "质检已完成列表"),
        /**
         * 质检申请复核-reCheck
         */
        质检申请复核("reCheck", "质检申请复核"),
        /**
         * 质检手工分派-manualDisPatch
         */
        质检手工分派("manualDisPatch", "质检手工分派"),
        /**
         * 质检手工改派-manualReform
         */
        质检手工改派("manualReform", "质检手工改派"),
        /**
         * 质检手工改派-manualReform
         */
        质检系统分派("sysDisPatch", "质检系统分派"),
        /**
         * 质检查询-query
         */
        质检查询("query", "质检查询"),
        /**
         * 组长/主管/经理
         */
        组长主管经理审核("leader-check", "组长/主管/经理审核");

        private MenuFlag(String code, String value){
            this.code = code;
            this.value = value;
        }

        private String code;

        private String value;

        public String getCode(){
            return code;
        }

        public String getValue(){
            return value;
        }
    }

    /**
     * 质检状态
     */
    public enum QualityStatus {

        QUALITY_COMPLETE ("quality_complete","完成质检"),
        QUALITY_RECHECK("quality_recheck","申请复核"),
        QUALITY_TEMPORARY_SAVE("quality_temporary_save","暂存"),
        QUALITY_RECHECK_WAIT("quality_recheck_wait","申请复核等待");

        private QualityStatus(String code, String value){
            this.code = code;
            this.value = value;
        }

        private String code;

        private String value;

        public String getCode(){
            return code;
        }

        public String getValue(){
            return value;
        }
    }

    /**
     * 质检结论初审反馈还是终审反馈
     */
    public enum QualityTaskDef {
        //质检结论 初审:apply-check
        // 终审:applyinfo-finalaudit
        // 组长/主管/经理：leader-chec
        APPLY_CHECK("apply-check", "初审"),
        APPLY_INFO_FINAL_AUDIT("applyinfo-finalaudit", "终审"),
        FEED_BACK_FLOW("feedbackFlow", "质检反馈工作流");

        private QualityTaskDef(String code, String value){
            this.code = code;
            this.value = value;
        }

        private String code;

        private String value;

        public String getCode(){
            return code;
        }

        public String getValue(){
            return value;
        }
    }

    /**
     * 质检反馈
     */
    public enum FeedbackResult {

        /**质检反馈*/
        CONFIRM("F_000000","确认"),
        CONFUSE("F_000001","争议"),
        ARBITRATION("F_000002","仲裁"),
        CONCLUSION("F_000003","定版");

        private FeedbackResult(String code, String value) {
            this.code = code;
            this.value = value;
        }

        private String code;

        private String value;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }

    /**
     * 质检抽单件状态
     * @desc 0-通过件，1-拒绝件，2-其他件
     * @author wulinjie
     */
    public enum ApprovalStatus {

        PASS(0, "通过"),
        REJECT(1, "拒绝"),
        OTHER(2, "其他");

        private ApprovalStatus(Integer code, String value) {
            this.code = code;
            this.value = value;
        }

        private Integer code;

        private String value;

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
    
    /**
     * 质检日志环节
     * @author lihm
     * @data 2017年6月16日下午3:08:23
     */
    public enum QualityLogLink {

    	SAMPLING ("sampling","人工派单"),
    	ASSIGN ("assign","质检分派"),
        QUALITY_CHECK("quality_check", "质检"),
        
    	QUALITY_TASK_MANGER ("quality_task_manger","质检人员管理"),
    	APPROVE_PERSON_MANGER ("approve_person_manger","被检人员管理"),
    	ERRORCODE_MANGER ("errorCode_manger","差错代码管理"),
    	SOURCE_MANGER ("source_manger","申请来源管理");
    	
        private QualityLogLink(String code, String value) {
			this.code = code;
			this.value = value;
		}

		private String code;

        private String value;

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
		
		//返回所有需要展示的环节
		public static List<String> queryShowLinks(){
			return Arrays.asList(SAMPLING.getCode(),//人工派单
								 ASSIGN.getCode(),//质检分派
								 QUALITY_CHECK.getCode()//质检
								);
		}
    	
    }
    
    /**
     * 质检日志操作
     * @author lihm
     * @data 2017年6月16日下午3:08:23
     */
    public enum QualityLogOperation {
        // 环节-人工派单
        SAMPLING ("sampling","自动抽检"),
        IMPORT_CASE("import_case","导入案件"),
        SAMPLING_RATE_SET ("sampling_rate_set","设置抽检率"),

        // 环节-质检分派
        AUTO_ASSIGN("auto_assign","自动分派"),
        HAND_ASSIGN("hand_assign","手动分派"),
        IMPORT_DELETE ("import_delete","导入删除"),

        // 环节-质检
        QUALITY_COMPLETE ("quality_complete","质检完成"),//“完成质检”修正为“质检完成”
        RECHECK ("recheck","申请复核"),
    	EDIT("edit","编辑"),
        TEMPORARY ("temporary","暂存"),
        EXPORT ("export","导出"),
        CLOSE ("close","关闭"),
        DELETE_ATTACHMENT("delete_attachment","删除附件"),

        // 公共
        ADD ("add","添加"),
        DELETE ("delete","删除");

        private QualityLogOperation(String code, String value) {
			this.code = code;
			this.value = value;
		}

		private String code;

        private String value;

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
    	
		/**
		 * 返回页面需要展示的质检日志操作code集合
		 * @return
		 */
		public static List<String> queryShowOperations(){
			return Arrays.asList(IMPORT_CASE.getCode(),//导入案件
					             SAMPLING.getCode(),//自动抽检
					             AUTO_ASSIGN.getCode(),//自动分派
					             HAND_ASSIGN.getCode(),//手动分派
					             QUALITY_COMPLETE.getCode(),//质检完成
					             RECHECK.getCode(),//申请复核
					             EDIT.getCode()//编辑
					             );
		}
    }

    /**
     * 派单类型
     * @lihm
     */
    public enum QualityAssignType {
    	AUTO_ASSIGN ("0","系统派单"),
    	MANUAL_ASSIGN ("1","手工派单");
    	
    	private QualityAssignType(String code, String value) {
 			this.code = code;
 			this.value = value;
 		}

 		private String code;

         private String value;

 		public String getCode() {
 			return code;
 		}

 		public void setCode(String code) {
 			this.code = code;
 		}

 		public String getValue() {
 			return value;
 		}

 		public void setValue(String value) {
 			this.value = value;
 		}
    }
    
    /**
     * @author lihm
     * 质检作业页面跳转标识	
     *@data 2017年6月20日下午3:32:18
     */
    public enum QualityFlag {
    	TODO ("todo","质检自己的申请件"),
    	TODOOTHERS ("todoOthers","质检辖下的申请件"),
    	DONE ("done","查看详情"),
    	UPDATEDONE ("doneUpdate","编辑已完成质检件"),
    	SAVEDONE ("savedone","编辑保存质检件");
    	private QualityFlag(String code, String value) {
 			this.code = code;
 			this.value = value;
 		}

 		private String code;

        private String value;

 		public String getCode() {
 			return code;
 		}

 		public void setCode(String code) {
 			this.code = code;
 		}

 		public String getValue() {
 			return value;
 		}

 		public void setValue(String value) {
 			this.value = value;
 		}
    }

    /**
     * 质检是否接单
     * @author wulinjie
     */
    public enum IfAccept {

        YES("0", "是"),
        NO("1", "否"),
        DELETE("2", "已删除");

        private IfAccept(String code, String value){
            this.code = code;
            this.value = value;
        }

        private String code;

        private String value;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}


