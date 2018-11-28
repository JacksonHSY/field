package com.yuminsoft.ams.system.common;

/**
 * 文件操作枚举
 * @author fuhongxing
 */
public enum FileEnum {

	/** 文件上传枚举 **/
	FILE_EMPTY_FILE("F000001","导入的文件为空，拒绝导入！"),
	FILE_SIZE_OUT_RANGE("F000002","导入的文件过大，文件大小不能超过 {0}！"),
	FILE_ERROR_TYPE("F000003","导入的文件类型错误，拒绝导入！"),
	FILE_ERROR_PARSE("F000004","解析文件出错：{0}！"),
	FILE_ERROR_WRITE("F000005","写入文件出错：{0}！"),
	FILE_ERROR_FORMAT_CONTENT("F000006","文件内容必须从第{0}行开始！"),
	FILE_NO_FILE("F000007", "所需文件未生成或已失效！");
	
	private final String code;
	private final String desc;

	private FileEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}
}
