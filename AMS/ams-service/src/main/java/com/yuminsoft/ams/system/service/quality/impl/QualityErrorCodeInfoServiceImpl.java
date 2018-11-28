package com.yuminsoft.ams.system.service.quality.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuminsoft.ams.system.dao.quality.QualityErrorCodeInfoMapper;
import com.yuminsoft.ams.system.domain.quality.QualityErrorCodeInfo;
import com.yuminsoft.ams.system.service.quality.QualityErrorCodeInfoService;
@Service
public class QualityErrorCodeInfoServiceImpl implements QualityErrorCodeInfoService{
	
    private static final Logger LOGGER = LoggerFactory.getLogger(QualityErrorCodeInfoServiceImpl.class);
    @Autowired
    private QualityErrorCodeInfoMapper qualityErrorCodeInfoMapper;

	@Override
	public List<QualityErrorCodeInfo> queryQualityErrorCodeInfo(String loanNo) {
		// TODO Auto-generated method stub
		return qualityErrorCodeInfoMapper.queryErrorCodeInfoByLoanNo(loanNo);
	}

	@Override
	public List<Map<String,String>> queryErrorCodeByLevel(String mark,String parentId,String firstId) {
		// TODO Auto-generated method stub
		if (mark == "0") {// 查询所有一级
			return qualityErrorCodeInfoMapper.queryAllParentInfo();
		} else if (mark == "1") {// 所有二级
			return qualityErrorCodeInfoMapper.queryFirstInfoByParent(parentId);
		} else if (mark == "2") {// 所有三级
			return qualityErrorCodeInfoMapper.querySecondInfoByParentFirst(parentId, firstId);
		}
		return null;
	}

}
