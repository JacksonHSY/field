package com.yuminsoft.ams.system.service.system.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.yuminsoft.ams.system.common.EnumUtils;
import com.yuminsoft.ams.system.dao.system.SysParamDefineMapper;
import com.yuminsoft.ams.system.domain.system.SysParamDefine;
import com.yuminsoft.ams.system.service.system.CommonParamService;
import com.yuminsoft.ams.system.util.RedisUtil;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommonParamServiceImpl implements CommonParamService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommonParamServiceImpl.class);

	@Autowired
	private SysParamDefineMapper sysParamDefineMapper;

	@Autowired
	private RedisUtil redisUtil;

	@Override
	public ResponsePage<SysParamDefine> getPageList(RequestPage requestPage, SysParamDefine sysParamDefine) {
		ResponsePage<SysParamDefine> rp = new ResponsePage<SysParamDefine>();
		PageHelper.startPage(requestPage.getPage(), requestPage.getRows());
		List<SysParamDefine> list = sysParamDefineMapper.findAllConditions(sysParamDefine);
		rp.setRows(list);
		rp.setTotal(((Page<SysParamDefine>) list).getTotal());
		return rp;
	}

	@Override
	public SysParamDefine findById(String id) {
		return sysParamDefineMapper.findById(Long.parseLong(id));
	}

	@Override
	public String update(SysParamDefine sysParamDefine) {
		int updateRow = sysParamDefineMapper.update(sysParamDefine);
		Map<String, Object> map = new HashMap<String, Object>();
		if (updateRow > 0) {
			map.put("status", "true");
		}

		refreshCache();

		return JSONObject.toJSONString(map);
	}

	@Override
	public String save(SysParamDefine sysParamDefine) {
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		param.put("paramKey", sysParamDefine.getParamKey());
		SysParamDefine sysParamDefineResult = sysParamDefineMapper.findOne(param);
		if (sysParamDefineResult != null) {
			map.put("status", "repeat");
		} else {
			int updateRow = sysParamDefineMapper.save(sysParamDefine);
			if (updateRow > 0) {
				map.put("status", "true");
			}
		}

		refreshCache();

		return JSONObject.toJSONString(map);
	}

	/**
	 * 删除
	 */
	@Override
	public String deletes(String[] ids) {
		Map<String, Object> map = new HashMap<String, Object>();
		int deletedId = sysParamDefineMapper.deletes(ids);
		if (deletedId > 0) {
			map.put("status", "true");
			map.put("deletedId", deletedId);
		}

		refreshCache();

		return JSONObject.toJSONString(map);
	}

	/**
	 * @Desc: 根据参数类型查询
	 * @Author: phb
	 * @Date: 2017/5/13 17:41
	 */
	@Override
	public List<SysParamDefine> findByParamType(SysParamDefine sysParamDefine) {
		return sysParamDefineMapper.findByParamDefine(sysParamDefine);
	}

	/**
	 * 根据参数类型查询
	 * @param paramType
	 * @return
	 */
	@Override
	public List<SysParamDefine> findByParamType(String paramType) {
		return sysParamDefineMapper.findByParamType(paramType);
	}

	/**
	 * 刷新缓存
	 * @author wulinjie
	 */
	private void refreshCache(){
		LOGGER.info("更新系统参数缓存");

		redisUtil.removeByCacheType(EnumUtils.CacheType.SYSTEM);	// 删除所有系统参数缓存

		// 重新读取db，缓存系统参数
		List<SysParamDefine> params = sysParamDefineMapper.findAll(new HashMap<String, Object>());
		for (SysParamDefine param : params) {
			List<SysParamDefine> paramDefines = Lists.newArrayList();

			Object value = redisUtil.get(EnumUtils.CacheType.SYSTEM, param.getParamType());
			if(value != null){
				paramDefines = (List<SysParamDefine>)value;
				paramDefines.add(param);
			}else{
				paramDefines.add(param);
			}

			redisUtil.set(EnumUtils.CacheType.SYSTEM, param.getParamType(), paramDefines);
		}
	}

	@Override
	public SysParamDefine findOne(Map<String, Object> map) {
		 return sysParamDefineMapper.findOne(map);
	}

	@Override
	public SysParamDefine getByParamKeyAndParamType(String paramKey, String paramType){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("paramKey", paramKey);
		params.put("paramType", paramType);

		return findOne(params);
	}
}
