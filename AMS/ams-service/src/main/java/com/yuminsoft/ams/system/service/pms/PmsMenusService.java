package com.yuminsoft.ams.system.service.pms;


import java.util.List;

import com.ymkj.pms.biz.api.vo.request.ReqEmployeeVO;
import com.ymkj.pms.biz.api.vo.response.ResResourceVO;
import com.yuminsoft.ams.system.vo.pluginVo.Tree;

public interface PmsMenusService {
	/**
	 * 获取对应人员菜单
	 * @param reqEmployeeVO
	 * @return
	 */
	public List<ResResourceVO> getMenusByAccount(ReqEmployeeVO reqEmployeeVO);
	/**
	 * 获取人员菜单树
	 * @param vo
	 * @return
	 */
	public Tree findMenuTree(ReqEmployeeVO reqEmployeeVO);
}
