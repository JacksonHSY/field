package com.yuminsoft.ams.system.dao.system;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.yuminsoft.ams.system.domain.system.SysParamDefine;

public interface SysParamDefineMapper {

	public int save(SysParamDefine sysParamDefine);

	public int delete(Long id);

	public int deletes(String[] ids);

	public int update(SysParamDefine sysParamDefine);

	public SysParamDefine findById(Long id);

	public SysParamDefine findOne(Map<String, Object> map);

	public List<SysParamDefine> findAll(Map<String, Object> map);

	public List<SysParamDefine> findAllConditions(SysParamDefine sysParamDefine);

	public List<SysParamDefine> findByParamDefine(SysParamDefine sysParamDefine);

	public List<SysParamDefine> findByParamType(String paramType);

	/**
	 * 查询所有终审角色额度权限
	 * 
	 * @author shipf
	 * @return
	 */
	public List<SysParamDefine> findAllFinalParam();

	/**
	 * 根据审批金额查询终审对应的审批层级
	 * 
	 * @param accLmt
	 * @return
	 */
	public String findFinalAuditLevel(BigDecimal accLmt);

	/**
	 * 查找出出终审人员的正常队列上限最大值
	 * 
	 * @author zhouwen
	 * @date 2017年6月16日
	 * @return
	 */
	public String findMaxNormalQueue();

	/**
	 * 查找出出终审人员的挂起队列上限最大值
	 * 
	 * @author zhouwen
	 * @date 2017年6月16日
	 * @return
	 */
	public String findMaxHangQueue();
}