package com.yuminsoft.ams.system.service.system;

import com.yuminsoft.ams.system.domain.approve.StaffOrderAbility;
import com.yuminsoft.ams.system.domain.approve.StaffOrderSet;
import com.yuminsoft.ams.system.domain.approve.StaffOrderTask;
import com.yuminsoft.ams.system.domain.system.SysParamDefine;

import java.util.List;
import java.util.Map;

public interface StaffAbilityService {
	
	/**
	 * 根据员工工号获取能力列表
	 * 
	 * @author Jia CX
	 * @date 2017年12月19日 上午8:44:30
	 * @notes
	 * 
	 * @param list
	 * @return
	 */
	List<StaffOrderAbility> getStaffOrderAbilityByUserCodes(List<String> list);

	/**
	 * 获取能力列表
	 * 
	 * @author dmz
	 * @date 2017年3月29日
	 * @param map
	 * @return
	 */
	List<StaffOrderAbility> getStaffOrderAbility(Map<String, Object> map);

	/**
	 * 
	 * @param paramType
	 * @return
	 */

	List<SysParamDefine> findByParamType(String paramType);

	/**
	 * 根据员工编号和类型获取员队列配置上限
	 * 
	 * @author dmz
	 * @date 2017年4月10日
	 * @return
	 */
	StaffOrderSet getStaffOrderSetByStaffCodeAndTaskDefId(String staffCode, String taskDefId);

	/**
	 * 添加或修改员工接单能力
	 * 
	 * @author dmz
	 * @date 2017年3月29日
	 * @param userCodeList
	 * @param staffAbilityList
	 * @return
	 */
	 void saveOrUpdateStaffAbilityService(List<String> userCodeList, List<StaffOrderAbility> staffAbilityList);

	/**
	 * 保存机构上限配置
	 * 
	 * @param staffOrderSet
	 * @param orgId
	 *            机构编码
	 * @return
	 */
	Boolean saveStaffAbility(StaffOrderSet staffOrderSet, Long orgId);

	/**
	 * 保存用户上限配置
	 * 
	 * @param staffOrderSet
	 * @param userCode
	 *            用户编码
	 * @return
	 */
	Boolean saveUserStaffAbility(StaffOrderSet staffOrderSet, String userCode);

	// end at 2017-04-24
	/**
	 * add by zw at 2017-04-28 根据ReformVOA 查询到员工的接单能力
	 * 
	 * @param map
	 * @return
	 */
	boolean getStaffOrderAbilityByMap(Map<String, Object> map);
	// end at 2017-04-28

	/**
	 *
	 * @return
	 */
	int syncStaffOrderSetAndTask();

	/**
	 * 根据用户编号查询接单能力更新时间(接单能力配置显示时间用)
	 * @param staffCode
	 * @return
	 */
	StaffOrderAbility getStaffOrderAbilityByStaffCode(String staffCode);

	/**
	 * 查询任务数
	 * @param map
	 * @return
	 */
	List<StaffOrderTask> getListStaffOrderTask(Map<String, Object> map);

	/**
	 * 获取员工接单能力
	 * @param staffCode
	 * @param abilities
	 * @return 员工接单能力
	 */
	List<StaffOrderAbility> getByMultiProduct(String staffCode, List<StaffOrderAbility> abilities);
	
}
