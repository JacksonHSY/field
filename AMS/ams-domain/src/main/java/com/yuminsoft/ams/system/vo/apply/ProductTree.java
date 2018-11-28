package com.yuminsoft.ams.system.vo.apply;

import java.util.List;

public class ProductTree {
	
	private String id;
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	private String pid;
	private String text;
	private List<ProductTree> children;
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		iconCls ="save";
		this.iconCls = iconCls;
	}
	private String iconCls;

	public List<ProductTree> getChildren() {
		return children;
	}
	public void setChildren(List<ProductTree> children) {
		this.children = children;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	
		
}
