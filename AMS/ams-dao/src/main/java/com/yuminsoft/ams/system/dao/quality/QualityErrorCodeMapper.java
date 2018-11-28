package com.yuminsoft.ams.system.dao.quality;

import java.util.List;
import java.util.Map;

import com.yuminsoft.ams.system.domain.quality.QualityErrorCode;

public interface QualityErrorCodeMapper {
	public List<QualityErrorCode> findAll(QualityErrorCode qualityErrorCodeInfo);
	
	public int save(QualityErrorCode qualityErrorCodeInfo);
	
	public QualityErrorCode findById(Long id);
	
	public int update(QualityErrorCode qualityErrorCodeInfo);
	
	public int deletes(String[] ids);
	
	public QualityErrorCode findOne(Map<String,Object> map);
	
	public List<QualityErrorCode> findByName(String name);

	/**
	 * @Desc: 查询所有可用的差错代码
	 * @Author: phb
	 * @Date: 2017/5/14 14:00
	 */
	public List<QualityErrorCode> findAllUsableErrorCodes();

}
