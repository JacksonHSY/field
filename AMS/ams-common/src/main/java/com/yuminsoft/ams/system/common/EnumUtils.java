package com.yuminsoft.ams.system.common;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.concurrent.TimeUnit;

public class EnumUtils {

	/**
	 * 响应代码
	 * 
	 * @date 2016年10月27日
	 */
	public enum ResponseCodeEnum {
		/** 成功 **/
		SUCCESS("000000"),
		/** 校验状态 **/
		CHECK("000001"),
		/****校验状态 配合 前前新定义**/
		CHECKPARAM("000003"),
		/** 当前版本不是最新 **/
		PARAMMISSING("100001"),
		/** 请求参数缺失 **/
		VERSIONERR("400002"),
		/** 请求参数为空 **/
		PARAMEMPLY("100002"),
		/** 请求参数类型错误 **/
		PARAMERROR("100003"),
		/** 请求参数值不合法 **/
		PARAMNOTVALID("100004"),
		/** 系统忙 **/
		SYSTEM("100011"),
		/** 失败 **/
		FAILURE("111111"),
		/** 请求IP没有访问权限 **/
		IPNOTPERMISSION("200001"),
		/** 请求系统不存在 **/
		SYSTEMNOTFIND("200011"),
		/** 请求系统已关闭 **/
		SYSTEMCLOSE("200012"),
		/** 处理中 **/
		EXECUTE("222222"),
		/** 异常 **/
		EXCEPTION("333333");
		private String value;

		private ResponseCodeEnum(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return getValue();
		}
	}

	/**
	 * Y或者N
	 * 
	 * @author dmz
	 * @date 2017年3月14日
	 */
	public enum YOrNEnum {
		/** Y **/
		Y("Y"),
		/** N **/
		N("N");
		private String value;

		private YOrNEnum(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return getValue();
		}
	}

	/**
	 * 启用禁用
	 * 
	 * @date 2016年10月27日
	 * @describe 0:启用;1:禁用
	 */
	public enum DisplayEnum {
		/*** 启用 ***/
		ENABLE("0"),
		/** 禁用 ***/
		DISABLE("1");
		private String value;

		private DisplayEnum(String value) {
			this.value = value;
		}

		@JsonValue
		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return getValue();
		}
	}

	/**
	 * 原因类型
	 * 
	 * @author dmz
	 * @date 2017年3月14日
	 */
	public enum ReasonKindEnum {
		/*** 取消 ***/
		CANCEL("CANCEL"),
		/** 拒绝 ***/
		REFUSE("REFUSE"),
		/** 挂起 **/
		HANG("HANG");
		private String value;

		private ReasonKindEnum(String value) {
			this.value = value;
		}

		@JsonValue
		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return getValue();
		}
	}

	/**
	 * 初审或终审
	 * 
	 * @author dmz
	 * @date 2017年3月14日
	 */
	public enum FirstOrFinalEnum {
		/*** 初审 ***/
		FIRST("apply-check"),
		/** 终审 ***/
		FINAL("applyinfo-finalaudit"),
		/**直通车**/
		SALE("sale-check");
		private String value;

		private FirstOrFinalEnum(String value) {
			this.value = value;
		}

		@JsonValue
		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return getValue();
		}
	}

	/**
	 * 终审层级
	 * 
	 * @author wulinjie
	 */
	public enum FinalLevel {
		L1, L2, L3, L4;
	}

	/**
	 * 缓存类型-redis
	 * 
	 * @author wulinjie
	 */
	public enum CacheType {
		SYSTEM("system", 24, TimeUnit.HOURS), // 系统参数
		SYS_BMS("bmsEnums", 24, TimeUnit.HOURS), // 借款枚举值
		RULE_ENGINE("ruleEngine", 1, TimeUnit.HOURS),// 规则引擎禁用
		SYS_CORE("core", 24, TimeUnit.HOURS);// 核心系统相关

		private final String prefix; // 缓存前缀
		private final long expire; // 缓存时间
		private TimeUnit timeUnit; // 缓存时间单位

		private CacheType(String key, long expire, TimeUnit timeUnit) {
			this.expire = expire;
			prefix = key;
			this.timeUnit = timeUnit;
		}

		public String getPrefix() {
			return prefix;
		}

		public long getExpire() {
			return expire;
		}

		public TimeUnit getTimeUnit() {
			return timeUnit;
		}
	}

	/**
	 * 规则引擎返回值类型
	 * 
	 * @author dmz
	 * @date 2017年7月10日
	 */
	public enum EngineType {
		/*** 通过 ***/
		PASS("通过", "PASS"),
		/*** 标识 ***/
		FLAG("标识", "FLAG"),
		/*** 提示 ***/
		HINT("提示", "HINT"),
		/*** 拒绝 ***/
		REJECT("拒绝", "REJECT"),
		/*** 限制 ***/
		LIMIT("限制", "LIMIT"),
		/*** 第三方调用 ***/
		CALLBACK("第三方调用", "CALLBACK");
		private String key;
		private String value;

		private EngineType(String key, String value) {
			this.value = value;
			this.key = key;
		}

		@JsonValue
		public String getValue() {
			return value;
		}

		@JsonValue
		public String getKey() {
			return key;
		}

		@Override
		public String toString() {
			return getValue();
		}
	}

	/**
	 * 申请件申请类型
	 * 
	 * @author zhouwen
	 * @date 2017年08月01日
	 */
	public enum ApplyTypeEnum {
		/*** 新申请 ***/
		NEW("NEW"),
		/** 追加贷款 ***/
		TOPUP("TOPUP"),
		/** 结清再贷 **/
		RELOAN("RELOAN");
		private String value;

		private ApplyTypeEnum(String value) {
			this.value = value;
		}

		@JsonValue
		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return getValue();
		}
	}

	/**
	 * 手机运营商
	 */
	public enum MobileCarrier{

		CMCC(1, "中国移动"),
		CUCC(1, "中国联通"),
		CTC(1, "中国电信");

		private MobileCarrier(Integer code, String value){
			this.code = code;
			this.value = value;
		}

		private Integer code;

		private String value;

		public Integer getCode(){
			return code;
		}

		public String getValue(){
			return value;
		}
	}

	/**
	 * 资料核对流水类型
	 */
	public enum CheckStatementType{
		B,
		C
	}

	/**
	 * 判断名字是不是给定枚举
	 * 
	 * @param enumClass
	 * @param enumName
	 * @return
	 */
	public static <E extends Enum<E>> boolean isValidEnumName(final Class<E> enumClass, final String enumName) {
		if (enumName == null) {
			return false;
		}
		try {
			Enum.valueOf(enumClass, enumName);
			return true;
		} catch (final IllegalArgumentException ex) {
			return false;
		}
	}
	
	/**
	 * 初审还是终审  1初审，2终审
	 * 
	 * @author JiaCX
	 * 2017年10月11日 上午9:35:57
	 *
	 */
	public enum approvalType{
	    /** 初审 */
        FIRST("1"),
        /** 终审 */
        FINAL("2");
	    
        private String value;

        private approvalType(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return getValue();
        }
	}

	/**
	 * 执行环节枚举类
	 */
	public enum ExecuteTypeEnum {
		/* 初审 点击办理 */
		XSCS01("XSCS"),
		/* 初审 点击提交 */
		XSCS02("XSCS"),
		/* 初审 审批意见表 点击系统初判 */
		XSCS03("XSCS"),
		/* 初审 审批意见表 点击保存 */
		XSCS04("XSCS"),
		/* 初审 客户信息 点击保存 */
		XSCS07("XSCS"),
		/* 终审 点击办理 */
		XSZS01("XSZS"),
		/* 终审 点击提交 */
		XSZS02("XSZS"),
		/* 终审 审批意见表 点击系统初判 */
		XSZS03("XSZS"),
		/* 终审 审批意见表 点击保存 */
		XSZS04("XSZS"),
		/* 初审 日终跑批 */
		XSCS05("XSZS"),
		/* 终审 日终跑批 */
		XSZS05("XSZS"),
		/* 初审 定时任务 */
		XSCS06("CSFP"),
		/* 终审 定时任务 */
		XSZS06("ZSFP");
		private String value;
		private ExecuteTypeEnum(String value) {
			this.value = value;
		}
		public String getValue() {
			return value;
		}
		@Override
		public String toString() {
			return getValue();
		}

	}

	/**
	 * 运算类型
	 */
	public enum CalType {
		ADD,	// 加
		MINUS   // 减
	}

	/**
	 * 员工队列类型
	 */
	public enum StaffTaskType {

		ACTIVITY("activity", "正常队列"),
		INACTIVITY("inactivity", "挂起队列"),
		PRIORITY("priority", "优先队列");

		private StaffTaskType(String code, String value){
			this.code = code;
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}

		public String getCode(){
			return this.code;
		}

		private String code;
		private String value;
	}

	/**
	 * 复议状态
	 * 1：提交；2：信审退回；3：同意复议；4：拒绝复议；5：发送信审；6：提交复核；
	*/
	public enum ReconsiderState{
		SUBMIT("1"),
		RETURN("2"),
		PASS("3"),
		REJECT("4"),
		SENDAPPROVE("5"),
		SUBMITREVIEW("6");
		private ReconsiderState(String value){
			this.value = value;
		}
		private String  value;
		public String getValue() {
			return this.value;
		}
	}
}
