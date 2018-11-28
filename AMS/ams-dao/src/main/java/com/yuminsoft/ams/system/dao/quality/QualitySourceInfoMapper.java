package com.yuminsoft.ams.system.dao.quality;

import java.util.List;
import java.util.Map;

import com.yuminsoft.ams.system.domain.quality.QualitySourceInfo;

public interface QualitySourceInfoMapper {
	
//	public int save(QualitySourceInfo qualitySourceInfo);
//	
//	public int delete(Long id);
//    
//    public int update(QualitySourceInfo QualitySourceInfo);
//    
//    public QualitySourceInfo findById(Long id);
//    
//    
//    public List<QualitySourceInfo> findAll(Map<String, Object> map);
	public List<QualitySourceInfo> findAll(QualitySourceInfo qualitySource);
	
	public int save(QualitySourceInfo qualitySource);
	
	public QualitySourceInfo findById(Long id);
	
	public int update(QualitySourceInfo qualitySource);
	
	public int deletes(String[] ids);
	
	public QualitySourceInfo findOne(String code);
	
	public QualitySourceInfo findOne(Map<String, Object> map);
	
	public QualitySourceInfo findUse(Map<String, Object> map);
	
	public List<QualitySourceInfo> findInUse(QualitySourceInfo qualitySource);
    
    
}