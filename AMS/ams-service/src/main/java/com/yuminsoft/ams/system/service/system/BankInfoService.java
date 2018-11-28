package com.yuminsoft.ams.system.service.system;

import com.yuminsoft.ams.system.domain.system.Banks;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;

public interface BankInfoService {

	/**
	 * 分页查询
	 * @param requestPage
	 * @param banksVo
	 * @return
	 */
	ResponsePage<Banks> getPageList(RequestPage requestPage, Banks banks);
	
	/**
	 * 保存
	 * @param banks
	 */
	void save(Banks banks);
	
	/**
	 * 修改
	 * @param banks
	 */
	int update(Banks banks);
	
	/**
	 * 查询ById
	 * @param id
	 * @return
	 */
	Banks findById(long id);
	
	/**
	 * 删除
	 * @param ids
	 * @return
	 */
	int delete(String[] ids);

}
