package com.yuminsoft.ams.system.dao.system;

import com.yuminsoft.ams.system.domain.system.UserLog;

import java.util.List;
import java.util.Map;

/**
 * 用户操作日志dao
 * @author fuhongxing
 */
public interface UserLogMapper {
	
	public int save(UserLog userLog);

	public int delete(Long id);
    
    public int update(UserLog systemLog);
    
    public UserLog findById(Long id);
    
    public UserLog findOne(Map<String, Object> map);
    
    public List<UserLog> findAll(Map<String, Object> map);
}
