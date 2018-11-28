package com.yuminsoft.ams.system.dao;

import java.util.List;
import java.util.Map;

import com.yuminsoft.ams.system.domain.CarInfo;

public interface CarInfoMapper {
	
	public int save(CarInfo carInfo);
	
	public int delete(Long id);
    
    public int update(CarInfo carInfo);
    
    public CarInfo findById(Long id);
    
    public CarInfo findOne(Map<String, Object> map);
    
    public List<CarInfo> findAll(Map<String, Object> map);
	
}