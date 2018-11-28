package com.yuminsoft.ams.system.service.system;

import java.util.List;

import com.yuminsoft.ams.system.domain.system.SystemLog;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;

public interface SystemLogService {
	public int save(SystemLog systemLog);
	
	public int update(SystemLog systemLog);
	
	public int delete(Long id);
	
	public List<SystemLog> findAll(int currentPage, int pageSize);
	
	public ResponsePage<SystemLog> getPageList(RequestPage requestPage);
}
