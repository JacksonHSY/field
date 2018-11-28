package com.yuminsoft.ams.system.dao.quality;

import java.util.List;
import java.util.Map;

import com.yuminsoft.ams.system.domain.quality.QualityLog;
import com.yuminsoft.ams.system.vo.quality.QualityLogVo;

public interface QualityLogMapper {

	public int save(QualityLog qualityLog);
	
	public int delete(Long id);
    
    public int update(QualityLog qualityLog);
    
    public QualityLog findById(Long id);
    
    public QualityLog findOne(Map<String, Object> map);
    
    public List<QualityLogVo> findAll(Map<String, Object> map);

}