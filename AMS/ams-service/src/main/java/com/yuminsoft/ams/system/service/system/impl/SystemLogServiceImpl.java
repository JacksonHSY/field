package com.yuminsoft.ams.system.service.system.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yuminsoft.ams.system.dao.system.SystemLogMapper;
import com.yuminsoft.ams.system.domain.system.SystemLog;
import com.yuminsoft.ams.system.service.system.SystemLogService;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;

@Service
public class SystemLogServiceImpl implements SystemLogService {

	@Autowired
	private SystemLogMapper systemLogMapper;

	@Override
	public List<SystemLog> findAll(int currentPage, int pageSize) {
		PageHelper.startPage(currentPage, pageSize);
		return systemLogMapper.findAll(new HashMap<String, Object>());
	}

	@Override
	public int save(SystemLog systemLog) {
		return systemLogMapper.save(systemLog);
	}

	@Override
	public int update(SystemLog systemLog) {
		return systemLogMapper.update(systemLog);
	}

	@Override
	public int delete(Long id) {
		return systemLogMapper.delete(id);
	}

	/***
	 * 分页查询
	 * 
	 * @author dongmingzhi
	 * @date 2016年12月12日
	 * @param requestPage
	 * @return
	 */
	@Override
	public ResponsePage<SystemLog> getPageList(RequestPage requestPage) {
		ResponsePage<SystemLog> rp = new ResponsePage<SystemLog>();
		PageHelper.startPage(requestPage.getPage(), requestPage.getRows());
		List<SystemLog> list = systemLogMapper.findAll(null);
		rp.setRows(list);
		rp.setTotal(((Page<SystemLog>) list).getTotal());
		return rp;
	}

}
