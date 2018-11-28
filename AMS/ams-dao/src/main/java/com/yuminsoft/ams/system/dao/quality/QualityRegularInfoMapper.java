package com.yuminsoft.ams.system.dao.quality;

import java.util.List;
import java.util.Map;

import com.yuminsoft.ams.system.domain.quality.QualityRegularInfo;

public interface QualityRegularInfoMapper {
	
	public int save(QualityRegularInfo qualityRegularInfo);
	
	public int delete(Long id);
    
	public int deleteBatch(String[] ids);
	
    public int update(QualityRegularInfo qualityRegularInfo);
    
    public QualityRegularInfo findById(Long id);
    
    public QualityRegularInfo findOne(Map<String, Object> map);
    
    public List<QualityRegularInfo> findAll(Map<String, Object> map);
    
    public List<String> findUserId(Map<String, Object> map);

}