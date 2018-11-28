package com.yuminsoft.ams.system.service.system.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yuminsoft.ams.system.dao.system.UserLogMapper;
import com.yuminsoft.ams.system.domain.system.UserLog;
import com.yuminsoft.ams.system.service.system.UserLogService;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户日志查询
 * @author luting
 */
@Service
public class UserLogImpl implements UserLogService {
	@Autowired
	private UserLogMapper userLogMapper;

	@Override
	public int save(UserLog userLog) {
		return userLogMapper.save(userLog);
	}

	@Override
	public List<UserLog> getByLoanNo(String loanNo) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("loanNo", loanNo);
		return userLogMapper.findAll(map);
	}
	
	@Override
	public ResponsePage<UserLog> getAll(int currentPage, int pageSize, Map<String, Object> map) {
		PageHelper.startPage(currentPage, pageSize);
		ResponsePage<UserLog> pageList = new ResponsePage<UserLog>();
		List<UserLog> list = userLogMapper.findAll(map);
		pageList.setRows(list);
		pageList.setTotal(((Page<UserLog>) list).getTotal());
		return pageList;
	}
	
	/**
	 * 分页查询
	 */
	@Override
	public ResponsePage<UserLog> getPageList(RequestPage requestPage) {
		ResponsePage<UserLog> rp = new ResponsePage<UserLog>();
		PageHelper.startPage(requestPage.getPage(), requestPage.getRows());
		List<UserLog> list = userLogMapper.findAll(null);
		rp.setRows(list);
		rp.setTotal(((Page<UserLog>) list).getTotal());
		return rp;
	}

}
