package com.yuminsoft.ams.system.dao;

import java.util.List;
import java.util.Map;

import com.yuminsoft.ams.system.domain.ApplyMain;

public interface ApplyMainMapper {
    
	public int save(ApplyMain applyMain);
	
	public int delete(Long id);
    
    public int update(ApplyMain applyMain);
    
    public ApplyMain findById(Long id);
    
    public ApplyMain findOne(Map<String, Object> map);
    
    public List<ApplyMain> findAll(Map<String, Object> map);
    
    public int updateByLoanNo(ApplyMain applyMain);
}