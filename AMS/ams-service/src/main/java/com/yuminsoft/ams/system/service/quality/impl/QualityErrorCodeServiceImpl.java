package com.yuminsoft.ams.system.service.quality.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yuminsoft.ams.system.common.AmsConstants;
import com.yuminsoft.ams.system.dao.quality.QualityErrorCodeMapper;
import com.yuminsoft.ams.system.domain.quality.QualityErrorCode;
import com.yuminsoft.ams.system.service.quality.QualityCheckInfoService;
import com.yuminsoft.ams.system.service.quality.QualityErrorCodeService;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class QualityErrorCodeServiceImpl implements QualityErrorCodeService{
	@Autowired
	private QualityErrorCodeMapper qualityErrorCodeMapper;
	@Autowired
	private QualityCheckInfoService qualityCheckInfoService;

	@Override
	public ResponsePage<QualityErrorCode> getPageList(RequestPage requestPage,QualityErrorCode qualityErrorCodeInfo) {
		ResponsePage<QualityErrorCode> rp = new ResponsePage<QualityErrorCode>();
		PageHelper.startPage(requestPage.getPage(), requestPage.getRows());
		qualityErrorCodeInfo.setStatus(String.valueOf(AmsConstants.TWO));
		List<QualityErrorCode> list = qualityErrorCodeMapper.findAll(qualityErrorCodeInfo);
		if(list.size()>0){
			for(QualityErrorCode info:list){
				info.setLastModifiedBy(qualityCheckInfoService.getNameByCode(info.getLastModifiedBy()));
			}
		}
		rp.setRows(list);
		rp.setTotal(((Page<QualityErrorCode>) list).getTotal());
		return rp;
		
	}

	@Override
	public String save(QualityErrorCode qualityErrorCodeInfo) {
				// 保存
				Map<String, Object> map = new HashMap<String, Object>();
				Map<String,Object> findMap = new HashMap<String,Object>();
				findMap.put("code", qualityErrorCodeInfo.getCode());
				findMap.put("status", AmsConstants.TWO);
				QualityErrorCode exist = qualityErrorCodeMapper.findOne(findMap);
				int status =0;
				if(exist==null){
					 status = qualityErrorCodeMapper.save(qualityErrorCodeInfo);
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
	public QualityErrorCode findById(long id) {
		return qualityErrorCodeMapper.findById(id);
	}

	@Override
	public String update(QualityErrorCode qualityErrorCodeInfo,String oldCode) {
		//如果修改前后差错代码一致，则不校验是否在数据库是否存在，反之则校验跟新的差错代码是否在数据库中存在
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String,Object> findMap = new HashMap<String,Object>();
		if(qualityErrorCodeInfo.getCode().equals(oldCode)){
			//更新差错代码
			int sum = qualityErrorCodeMapper.update(qualityErrorCodeInfo);
			if(sum==1){
				map.put("status", "true");
			}else{
				map.put("status", "false");
			}
			
		}else{
			//校验更新后的差错代码是否已经存在
			findMap.put("code", qualityErrorCodeInfo.getCode());
			findMap.put("status", AmsConstants.TWO);
			QualityErrorCode code = qualityErrorCodeMapper.findOne(findMap);
			if(code!=null){
				//差错代码已存在
				map.put("status", "repeated");
			}else{
				//更新差错代码
				int sum = qualityErrorCodeMapper.update(qualityErrorCodeInfo);
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
		int deletedId = qualityErrorCodeMapper.deletes(ids);
		if (deletedId > 0) {
			map.put("status", "true");
			map.put("deletedId", deletedId);
		}
		return JSONObject.toJSONString(map);
	}

	/**
	 * @Desc:  查询所有可用的差错代码
	 * @Author: phb
	 * @Date: 2017/5/14 14:05
	 */
	@Override
    public List<QualityErrorCode> findAllUsableErrorCodes() {
        return qualityErrorCodeMapper.findAllUsableErrorCodes();
    }
}
