package com.yuminsoft.ams.system.exception;

import java.text.MessageFormat;

import org.apache.commons.lang3.StringUtils;

import com.yuminsoft.ams.system.common.FileEnum;

/**
 * 文件异常
 * @author fuhongxing
 */
public class FileException extends RuntimeException {

	private static final long serialVersionUID = -8058289372452353570L;
	/** 业务类型代码 */
	private String msg;
	private FileEnum fileEnum;
	private Object[] arguments;
	

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public FileEnum getFileEnum() {
		return fileEnum;
	}

	public void setFileEnum(FileEnum fileEnum) {
		this.fileEnum = fileEnum;
	}
	
	public Object[] getArguments() {
		return arguments;
	}

	public void setArguments(Object[] arguments) {
		this.arguments = arguments;
	}

	public FileException(FileEnum fileEnum, Object... arguments) {
		super();
		this.fileEnum = fileEnum;
		this.arguments = arguments;
	}

	public FileException(Throwable e) {
		super(e);
	}

	public FileException(String msg) {
		super(msg);
		this.msg = msg;
	}

	public FileException(String msg, Throwable cause) {
		super(msg, cause);
		this.msg = msg;
	}

	@Override
	public String getMessage() {
		String returnMsg = "";
		if (StringUtils.isNotEmpty(msg)) {
			returnMsg = msg;
		} else {
			if (fileEnum != null) {
				returnMsg = fileEnum.getDesc();
			}
		}
		if (StringUtils.isEmpty(returnMsg)) {
			returnMsg = "not message";
		}
		return MessageFormat.format(returnMsg, arguments);
	}
	
}