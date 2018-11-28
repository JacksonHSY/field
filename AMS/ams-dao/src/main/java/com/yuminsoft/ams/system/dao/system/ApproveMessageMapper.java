package com.yuminsoft.ams.system.dao.system;

import com.yuminsoft.ams.system.domain.system.ApproveMessage;

import java.util.List;
import java.util.Map;

public interface ApproveMessageMapper {
	
	public int save(ApproveMessage approveMessage);
	
	public int delete(Long id);

    public ApproveMessage findById(Long id);
    
    public ApproveMessage findOne(Map<String, Object> map);
    
    public List<ApproveMessage> findAll(Map<String, Object> map);
	
	
}