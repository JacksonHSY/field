package com.yuminsoft.ams.system.service.quality.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yuminsoft.ams.system.common.AmsConstants;
import com.yuminsoft.ams.system.dao.quality.QualitySourceInfoMapper;
import com.yuminsoft.ams.system.domain.quality.QualitySourceInfo;
import com.yuminsoft.ams.system.service.quality.QualityCheckInfoService;
import com.yuminsoft.ams.system.service.quality.QualitySourceService;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class QualitySourceServiceImpl implements QualitySourceService{
	@Autowired
	private QualitySourceInfoMapper qualitySourceInfoMapper;
	@Autowired
	private QualityCheckInfoService qualityCheckInfoService;

	@Override
	public ResponsePage<QualitySourceInfo> getPageList(RequestPage requestPage,QualitySourceInfo qualitySourceInfo) {
		ResponsePage<QualitySourceInfo> rp = new ResponsePage<QualitySourceInfo>();
		PageHelper.startPage(requestPage.getPage(), requestPage.getRows());
		qualitySourceInfo.setStatus(String.valueOf(AmsConstants.TWO));
		List<QualitySourceInfo> list = qualitySourceInfoMapper.findInUse(qualitySourceInfo);
		if(list.size()>0){
			for(QualitySourceInfo info:list){
				info.setLastModifiedBy(qualityCheckInfoService.getNameByCode(info.getLastModifiedBy()));
			}
		}
		rp.setRows(list);
		rp.setTotal(((Page<QualitySourceInfo>) list).getTotal());
		return rp;
		
	}

	/**
	 * @author wangzx
	 * @version 2017年6月3日
	 * @param qualitySourceInfo
	 * @return
	 */
	@Override
	public String save(QualitySourceInfo qualitySourceInfo) {
				Map<String, Object> map = new HashMap<String, Object>();
				Map<String,Object> findMap = new HashMap<String,Object>();
				findMap.put("qualitySource", qualitySourceInfo.getQualitySource());
				findMap.put("status", AmsConstants.TWO);
				QualitySourceInfo exist = qualitySourceInfoMapper.findOne(findMap);
				int status =0;
				if(exist==null){
					 status = qualitySourceInfoMapper.save(qualitySourceInfo);
					 if(status==1){
						 map.put("status", "true");
					 }else{
						 map.put("status", "false");
					 }
				}else{
					map.put("status", "repeated");
					
				}
				return JSONObject.toJSONString(map);
	}
	
	@Override
	public QualitySourceInfo findById(long id) {
		return qualitySourceInfoMapper.findById(id);
	}

	@Override
	public String update(QualitySourceInfo qualitySourceInfo,String oldSource) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String,Object> findMap = new HashMap<String,Object>();
		//判断更新前后质检来源是否一致，如果一致，则不校验是否在当前系统存在
		if(oldSource.equals(qualitySourceInfo.getQualitySource())){
			int sum = qualitySourceInfoMapper.update(qualitySourceInfo);
			if(sum==1){
				map.put("status", "true");
			}else{
				map.put("status", "false");
			}
			
		}else{
			//根据修改后的qualitySource判断该质检来源是否已存在
			findMap.put("qualitySource", qualitySourceInfo.getQualitySource());
			findMap.put("status", AmsConstants.TWO);
			QualitySourceInfo qinfo = qualitySourceInfoMapper.findOne(findMap);
			if(qinfo!=null){
				map.put("status", "repeated");
			}else{
				int sum = qualitySourceInfoMapper.update(qualitySourceInfo);
				if(sum==1){
					map.put("status", "true");
				}else{
					map.put("status", "false");
				}
			}
		}
		return JSONObject.toJSONString(map);
	}

	@Override
	public String delete(String[] ids) {
		Map<String, Object> map = new HashMap<String, Object>();
		int deletedId = qualitySourceInfoMapper.deletes(ids);
		if (deletedId > 0) {
			map.put("status", "true");
			map.put("deletedId", deletedId);
		}
		return JSONObject.toJSONString(map);
	}
	//得到所有质检来源信息
	@Override
	public List<QualitySourceInfo> getAllSource() {
		return qualitySourceInfoMapper.findAll(new QualitySourceInfo());
	}
}
