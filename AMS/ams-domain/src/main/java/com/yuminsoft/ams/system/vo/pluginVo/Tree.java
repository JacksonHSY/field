package com.yuminsoft.ams.system.vo.pluginVo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

/**
 * tree 树形结构
 * 
 * @author dongmingzhi
 * @date 2016年12月12日
 */
public class Tree implements Serializable {
	private static final long serialVersionUID = -2812317987807805760L;
    
    /**
     * 资源编码
     */
    private String code ; 
    /**
     * 资源父编码
     */
    private String parentCode ; 
    /**
     * 资源名称
     */
    private String name ; 
    /**
     * 系统编码
     */
    private String systemCode ;
    /**
     * 资源类型
     */
    private String resType ; 
    /**
     * 资源描述
     */
    private String memo ;
    /**
     * 链接地址
     */
    private String url ;
    /**
     * 图标
     */
    private String icon ;
    /**
     * 是否展示(0: 能, 1:不 能)
     */
    private String isDisplay ;
    /**
     * 打开方式
     */
    private String openMode ;
    /**
     * 深度 0.根 1.一级，2.二级....
     */
    private String deep;
    /**
     * 是否含有子节点,true有，false无
     */
    private boolean hasChildren;
    
    private Object attr;// 自定义属性
    private JSONObject attributes = new JSONObject() ; 
    private List<Tree> children = new ArrayList<Tree>() ;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getParentCode() {
		return parentCode;
	}
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSystemCode() {
		return systemCode;
	}
	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}
	public String getResType() {
		return resType;
	}
	public void setResType(String resType) {
		this.resType = resType;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getIsDisplay() {
		return isDisplay;
	}
	public void setIsDisplay(String isDisplay) {
		this.isDisplay = isDisplay;
	}
	public String getOpenMode() {
		return openMode;
	}
	public void setOpenMode(String openMode) {
		this.openMode = openMode;
	}
	public String getDeep() {
		return deep;
	}
	public void setDeep(String deep) {
		this.deep = deep;
	}
	public Object getAttr() {
		return attr;
	}
	public void setAttr(Object attr) {
		this.attr = attr;
	}
	public JSONObject getAttributes() {
		return attributes;
	}
	public void setAttributes(JSONObject attributes) {
		this.attributes = attributes;
	}
	public List<Tree> getChildren() {
		return children;
	}
	public void setChildren(List<Tree> children) {
		this.children = children;
	}
	public boolean isHasChildren() {
		return hasChildren;
	}
	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}
	
	/**
	 * 菜单树生成
	 * @param treeDataList
	 * @return
	 */
	public static Tree formatMenuTree(List<Tree> treeDataList) {
		//根节点是否找到
		Tree root = null;
		if(treeDataList != null) {//空判断
			for(Tree menu : treeDataList) {//根节点查找
				if("0".equals(menu.getDeep()))  {
					root = menu;
					break;
				}
			}
			//下级节点查找
			if(root != null){
				getChildrenNodes(root, treeDataList);
			}
            
        }
        return root;
	}
	
	/**
	 * 通过回调生成菜单树
	 * @param root
	 * @param treeDataList
	 */
	private static void getChildrenNodes(Tree root, List<Tree> treeDataList) {
		List<Tree> menuList = new ArrayList<Tree>();
		for (Tree menu : treeDataList) {
			if(root.getCode().equals(menu.getParentCode())){//子节点判断
				//递归查找
				getChildrenNodes(menu, treeDataList);
				menuList.add(menu);
			}
		}
		if(!menuList.isEmpty()){
			root.setChildren(menuList);//子节点加入
			root.setHasChildren(true);//修改父级节点，含有子节点标识
		}
	}
}
