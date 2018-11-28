package com.yuminsoft.ams.api.util;


import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * return result
 * 
 * @author fuhongxing
 */
public class ResponseDatagram<T extends Serializable> implements Serializable {

	private static final long serialVersionUID = -8508268287523566997L;

	/** 类型 */
	private Type type;
	
	/** 消息 */
	private List<String> messages;
	
	/** 数据 */
	private T data;
	
	/** 当前页 */
	private int currentPage; 
	
	/**每页显示多少条数据*/
	private int pageSize; 
	
	/** 构造函数 */
	public ResponseDatagram() {
		messages = new LinkedList<String>();
	}
	
	/**
	 * 构造函数
	 *
	 * @param type
	 */
	public ResponseDatagram(Type type) {
		this();
		this.type = type;
	}
	
	/**
	 * 构造函数
	 *
	 * @param type
	 * @param message
	 */
	public ResponseDatagram(Type type, String message) {
		this(type);
		addMessage(message);
	}
	
	/**
	 * 构造函数
	 *
	 * @param type
	 * @param message
	 * @param data
	 */
	public ResponseDatagram(Type type, String message, T data) {
		this(type, message);
		this.data = data;
	}
	
	/**
	 * 是否成功
	 * 
	 * @return
	 */
	public boolean success() {
		return Type.SUCCESS.equals(this.type);
	}

	/**
	 * 是否警告
	 * 
	 * @return
	 */
	public boolean warning() {
		return Type.WARNING.equals(this.type);
	}

	/**
	 * 是否失败
	 * 
	 * @return
	 */
	public boolean failure() {
		return Type.FAILURE.equals(this.type);
	}

	/**
	 * 读取类型
	 * 
	 * @return
	 */
	public Type getType() {
		return type;
	}
	
	/**
	 * 设置类型
	 * 
	 * @param type
	 * @return
	 */
	public ResponseDatagram<T> setType(Type type) {
		this.type = type;
		return this;
	}
	
	/**
	 * 读取消息
	 * 
	 * @param key
	 * @return
	 */
	public List<String> getMessages() {
		return messages;
	}
	
	/**
	 * 读取第一条消息
	 * 
	 * @return
	 */
	public String getFirstMessage() {
		return !messages.isEmpty() ? messages.get(0) : null;
	}
	
	/**
	 * 添加消息
	 * 
	 * @param message
	 * @param args
	 * @return
	 */
	public ResponseDatagram<T> addMessage(String message) {
		if (message != null && !"".equals(message)){  
			this.messages.add(message);
		}
		return this;
	}
	
	/**
	 * 添加消息
	 * 
	 * @param index
	 * @param message
	 * @param args
	 * @return
	 */
	public ResponseDatagram<T> addMessage(int index, String message) {
		if (message != null && !"".equals(message)){ 
			this.messages.add(index, message);
		}
		return this;
	}
	
	/**
	 * 读取数据
	 * 
	 * @return
	 */
	public T getData() {
		return this.data;
	}
	
	/**
	 * 设置数据
	 * 
	 * @param data
	 * @return
	 */
	public ResponseDatagram<T> setData(T data) {
		this.data = data;
		return this;
	}
	
	/**
	 * @return
	 */
	public boolean isResult() {
		return true;
	}
	
	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 类型
	 * 
	 * @author ultrafrog
	 * @version 1.0, 2012-10-18
	 * @since 1.0
	 */
	public enum Type {
		
		/** 成功 */
		SUCCESS, 
		
		/** 警告 */
		WARNING, 
		
		/** 失败 */
		FAILURE;
	}
	
	public boolean getSuccess(){
		return success();
	}
}
