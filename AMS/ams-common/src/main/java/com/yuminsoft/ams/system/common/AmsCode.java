package com.yuminsoft.ams.system.common;

/**
 * 状态码
 * @author fuhongxing
 */
public class AmsCode {
	
	/**成功状态码*/
	public static final String RESULT_SUCCESS = "000000";
	/**
	 * 结果
	 */
	public enum STATUS{
		SUCCESS, WARNING, FAIL
	}
	
	
	/**核心接口成功状态码*/
	public static int CORN_RESULT_SUCCESS = 200;
	
	/**
	 * 
	 * @author fuhongxing
	 */
	public enum PHONEENUM{
		常用手机, 备用手机, 宅电, 单位电话1, 单位电话2,
	}
}
