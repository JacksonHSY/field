package com.yuminsoft.ams.system.dao.approve;

import com.yuminsoft.ams.system.domain.approve.StaffOrderSet;

import java.util.List;
import java.util.Map;

public interface StaffOrderSetMapper {
	
	public int save(StaffOrderSet staffOrderSet);
	
	public int delete(Long id);
    
	public int deleteByUserCode(String staffCode);
	
    public int update(StaffOrderSet staffOrderSet);
    
    public int updateUserCode(StaffOrderSet staffOrderSet);
    
    public StaffOrderSet findById(Long id);
    
    public StaffOrderSet findOne(Map<String, Object> map);
    
    public List<StaffOrderSet> findAll(Map<String, Object> map);

    /**
     * 修复员工接单正常队列上限，挂起队列上限，初终审标识
     */
    public int syncWithOrderTask();

}