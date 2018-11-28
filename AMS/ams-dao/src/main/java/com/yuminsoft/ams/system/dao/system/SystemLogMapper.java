package com.yuminsoft.ams.system.dao.system;

import java.util.List;
import java.util.Map;

import com.yuminsoft.ams.system.domain.system.SystemLog;


public interface SystemLogMapper {
	
	public int save(SystemLog systemLog);

	public int delete(Long id);
    
    public int update(SystemLog systemLog);
    
    public SystemLog findById(Long id);
    
    public SystemLog findOne(Map<String, Object> map);
    
    public List<SystemLog> findAll(Map<String, Object> map);
}
