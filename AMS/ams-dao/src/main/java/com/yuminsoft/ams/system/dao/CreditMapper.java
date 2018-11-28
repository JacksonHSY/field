package com.yuminsoft.ams.system.dao;

import java.util.List;
import java.util.Map;

import com.yuminsoft.ams.system.domain.Credit;

public interface CreditMapper {
	
	public int save(Credit credit);
	
	public int delete(Long id);
    
    public int update(Credit credit);
    
    public Credit findById(Long id);
    
    public Credit findOne(Map<String, Object> map);
    
    public List<Credit> findAll(Map<String, Object> map);
	
	
}