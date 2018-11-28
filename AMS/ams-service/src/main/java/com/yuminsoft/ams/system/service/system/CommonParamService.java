package com.yuminsoft.ams.system.service.system;

import com.yuminsoft.ams.system.domain.system.SysParamDefine;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;

import java.util.List;
import java.util.Map;

public interface CommonParamService {
	/**
	 * 分页查询
	 * 
	 * @param requestPage
	 * @param sysParamDefine
	 * @return
	 */
	ResponsePage<SysParamDefine> getPageList(RequestPage requestPage, SysParamDefine sysParamDefine);

	/**
	 * 通过id查询
	 * 
	 * @param id
	 * @return
	 */
	SysParamDefine findById(String id);

	/**
	 * 修改
	 * 
	 * @param sysParamDefine
	 * @return
	 */
	String update(SysParamDefine sysParamDefine);

	/**
	 * 保存
	 * 
	 * @param sysParamDefine
	 */
	String save(SysParamDefine sysParamDefine);

	/**
	 * 删除
	 * 
	 * @author luting
	 * @date 2017年3月16日 上午10:36:21
	 */
	String deletes(String[] ids);

	/**
	 * @Desc: 根据参数类型查询
	 * @Author: phb
	 * @Date: 2017/5/13 17:40
	 */
	List<SysParamDefine> findByParamType(SysParamDefine sysParamDefine);

	/**
	 * 根据参数类型查询
	 * 
	 * @param paramType
	 * @author wulinjie
	 * @return
	 */
	List<SysParamDefine> findByParamType(String paramType);

	/**
	 * 根据参数唯一标识查询参数类型
	 * 
	 * @author zhouwen
	 * @date 2017年6月21日
	 * @param map
	 * @return
	 */
	SysParamDefine findOne(Map<String, Object> map);

	/**
	 * 根据参数key，查找通用参数
	 * @param paramKey
	 * @param paramType
	 * @return
	 */
	SysParamDefine getByParamKeyAndParamType(String paramKey, String paramType);
}
