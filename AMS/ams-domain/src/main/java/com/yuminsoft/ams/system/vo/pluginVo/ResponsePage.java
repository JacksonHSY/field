package com.yuminsoft.ams.system.vo.pluginVo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * easyui datagrid 响应
 * @author dongmingzhi
 * @date 2016年12月12日
 */
public class ResponsePage<T> implements Serializable {
	private static final long serialVersionUID = -6200098538119986014L;
	// Fields
	public long total;
	public List<T> rows =new ArrayList<T>();

	// Constructors
	/*** default constructor **/
	public ResponsePage() {

	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		if(rows != null && rows.size() > 0){
			this.rows = rows;
		}
	}
}
