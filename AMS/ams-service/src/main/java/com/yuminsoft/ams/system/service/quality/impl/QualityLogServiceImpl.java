package com.yuminsoft.ams.system.service.quality.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.yuminsoft.ams.system.dao.quality.QualityLogMapper;
import com.yuminsoft.ams.system.domain.quality.QualityLog;
import com.yuminsoft.ams.system.service.quality.QualityLogService;
import com.yuminsoft.ams.system.vo.quality.QualityLogVo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 质检信息
 *
 * @author YM10105
 */
@Service
public class QualityLogServiceImpl implements QualityLogService {

	private static final Logger LOGGER = LoggerFactory.getLogger(QualityLogServiceImpl.class);

	@Autowired
	private QualityLogMapper qualityLogMapper;
	
	@Override
	public int update(QualityLog qualityLog) {
		return qualityLogMapper.update(qualityLog);
	}

	@Override
	public int save(QualityLog qualityLog) {
		return qualityLogMapper.save(qualityLog);
	}

	/**
	 * @Desc: 查询质检日志
	 * @Author: phb
	 * @Date: 2017/5/16 16:28
	 */
	@Override
    public List<QualityLogVo> findAll(Map<String, Object> param) {
		LOGGER.info("=======查询质检日志，参数:{}=========", JSON.toJSONString(param));
        return qualityLogMapper.findAll(param);
    }
}
