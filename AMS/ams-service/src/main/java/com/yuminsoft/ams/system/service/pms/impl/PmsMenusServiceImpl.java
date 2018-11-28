package com.yuminsoft.ams.system.service.pms.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.ymkj.pms.biz.api.vo.request.ReqEmployeeVO;
import com.ymkj.pms.biz.api.vo.response.ResResourceVO;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.service.pms.PmsMenusFacade;
import com.yuminsoft.ams.system.service.pms.PmsMenusService;
import com.yuminsoft.ams.system.service.system.TimeManageService;
import com.yuminsoft.ams.system.util.RedisUtil;
import com.yuminsoft.ams.system.vo.pluginVo.Tree;
import com.yuminsoft.ams.system.vo.system.TimeManageRemainVO;
/**
 * 平台调用service
 * @author fuhongxing
 */
@Service
public class PmsMenusServiceImpl implements PmsMenusService {
	private static final Logger LOGGER = LoggerFactory.getLogger(PmsMenusServiceImpl.class);
	@Autowired
	private PmsMenusFacade pmsMenusFacade;
	
	@Autowired
	private TimeManageService timeManageService;
	
	@Autowired
	private RedisUtil redisUtil;
	@Override
	public List<ResResourceVO> getMenusByAccount(ReqEmployeeVO reqEmployeeVO) {
		return pmsMenusFacade.findMenusByAccount(reqEmployeeVO);
	}

	@Override
	public Tree findMenuTree(ReqEmployeeVO reqEmployeeVO) {
		LOGGER.info("========调用平台系统查询用户菜单信息=========");
		List<Tree> treeDataList = new ArrayList<Tree>();
		//List<ResResourceVO>封装成树List<TreeJson> 
		//设置根目录
		Tree rootData = new Tree();
		rootData.setCode("");
        rootData.setName("菜单树根");
        rootData.setDeep("0");
        treeDataList.add(rootData);
        Map<String, JSONObject> map = new HashMap<String, JSONObject>();
        Map<String, String> menuMap = new HashMap<String, String>();
        JSONObject json = null;
		//渠道
        //如果当前登陆角色有时间限制记录，则使用角色去查询菜单资列表
        List<ResResourceVO> resultList = new ArrayList<ResResourceVO>();
        TimeManageRemainVO time = timeManageService.getRoleAndReaminTime(reqEmployeeVO.getUsercode());
        if(StringUtils.isEmpty(time.getCurrentLoginRole())) {
        	resultList = this.getMenusByAccount(reqEmployeeVO);
        } else {
        	List<String> roles = new ArrayList<String>();
        	for (String role : time.getCurrentLoginRole().split(",")) {
				roles.add(role);
			}
        	reqEmployeeVO.setRoleCodeList(roles);
        	resultList = pmsMenusFacade.findMenusByRoleCodes(reqEmployeeVO);
        }
        
        for (ResResourceVO menu : resultList) {
        	Tree treeData = new Tree();
        	treeData.setCode(menu.getCode());//菜单code
        	treeData.setName(menu.getName());//菜单名称
            treeData.setParentCode(menu.getParentCode()==null ? "" : menu.getParentCode());//父菜单code,关联根节点
            treeData.setUrl(menu.getUrl());//访问地址
            treeData.setIcon(menu.getIcon());//显示图标
            treeData.setSystemCode(menu.getSystemCode());//系统编码
            treeDataList.add(treeData);
            json = new JSONObject();
            //写入用户菜单信息,系统日志自动读取菜单时使用
            if(StringUtils.isNotEmpty(menu.getParentCode())){
            	json.put("name", map.get(menu.getParentCode()).get("name")+"-->"+menu.getName());
            	json.put("url", menu.getUrl());
            	json.put("code", menu.getCode());
            	map.put(menu.getCode(), json);
            	menuMap.put(menu.getUrl(), map.get(menu.getParentCode()).get("name")+"-->"+menu.getName());
            }else{
            	json.put("name", menu.getName());
            	json.put("url", menu.getUrl());
            	json.put("code", menu.getCode());
            	map.put(menu.getCode(), json);
            	menuMap.put(menu.getUrl(), menu.getName());
            }
        }
        //生成树形菜单
        Tree tree = Tree.formatMenuTree(treeDataList);
        //将用户菜单权限写入redis
        if(!redisUtil.exists("menu-"+ShiroUtils.getCurrentUser().getUsercode())){
        	redisUtil.set("menu-"+ShiroUtils.getCurrentUser().getUsercode(), menuMap, 3600L);
        }
		return tree;
	}

}
