package com.yuminsoft.ams.system.dao;

import java.util.List;
import java.util.Map;

import com.yuminsoft.ams.system.domain.DebtsInfo;
import com.yuminsoft.ams.system.domain.PersonInfo;

public interface PersonInfoMapper {
	
	public int save(PersonInfo personInfo);
	
	public int delete(Long id);
    
    public int update(DebtsInfo personInfo);
    
    public PersonInfo findById(Long id);
    
    public PersonInfo findOne(Map<String, Object> map);
    
    public List<PersonInfo> findAll(Map<String, Object> map);
}