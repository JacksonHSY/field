package com.yuminsoft.ams.api.util;
/**
 * 枚举工具类
 * @author fuhongxing
 */
public class AmsEnumUtils {
	
	/**
	 * 响应代码
	 * @author fuhongxing
	 */
	public enum ResponseCodeEnum {
		/** 成功 **/
		SUCCESS("000000"),
		/** 请求参数缺失 **/
		PARAMMISSING("100001"),
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
		EXCEPTION("999999");
		private String value;
		
		private ResponseCodeEnum(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
	
}
