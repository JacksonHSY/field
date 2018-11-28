package com.yuminsoft.ams.system.dao.system;

import java.util.List;
import java.util.Map;

import com.yuminsoft.ams.system.domain.system.Banks;
/**
 * 银行信息管理dao
 * @author zhouwq
 */
public interface BankInfoMapper {
    
    public int save(Banks banks);
	
	public int delete(Long id);
	
	public int deletes(String[] ids);
	
    public int update(Banks banks);
    
    public Banks findById(Long id);
    
    public Banks findOne(Map<String, Object> map);
    
    public List<Banks> findAll(Map<String, Object> map);
    
    public List<Banks> findAllConditions(Banks banks);
}