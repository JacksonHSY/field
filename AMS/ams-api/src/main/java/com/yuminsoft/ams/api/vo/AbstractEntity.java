package com.yuminsoft.ams.api.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 基类
 * 
 * @author fuhongxing
 */
public abstract class AbstractEntity implements Serializable {

	private static final long serialVersionUID = 5583035582769043365L;

	private Long id;// 主键id
	
	private Date createdDate = new Date();// 创建时间

	private Date lastModifiedDate = new Date();// 修改时间
	
	private String createdBy;// 创建人

	private String lastModifiedBy;// 修改人

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

}
