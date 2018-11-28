package com.yuminsoft.ams.system.service.quality;

import com.yuminsoft.ams.system.domain.quality.QualityLog;
import com.yuminsoft.ams.system.vo.quality.QualityLogVo;

import java.util.List;
import java.util.Map;

/**
 * 质检信息
 * @author YM10105
 *
 */
public interface QualityLogService {
	
	/**
	 * update
	 */
	int update(QualityLog qualityLog);
	
	/**
	 * save
	 */
	int save(QualityLog qualityLog);

	/**
	 * @Desc: 查询质检日志
	 * @Author: phb
	 * @Date: 2017/5/16 16:28
	 */
	List<QualityLogVo> findAll(Map<String, Object> param);
}
