package com.yuminsoft.ams.system.vo.pluginVo;

import java.io.Serializable;
/**
 * easyui datagrid 分页请求参数
 * @author dongmingzhi
 * @date 2016年12月12日
 */
public class RequestPage implements Serializable {
	private static final long serialVersionUID = 9021540362771448413L;
	// Fields
	private int rows;// 行数
	private int page;// 页数
	private String order = "desc";
	private String sort;// 排序列列名称
	// Constructors

	/*** default constructor **/
	public RequestPage() {

	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
}
