package com.yuminsoft.ams.system.dao.approve;

import com.github.pagehelper.Page;
import com.yuminsoft.ams.system.dao.GenericCrudMapper;
import com.yuminsoft.ams.system.domain.approve.StaffOrderTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface StaffOrderTaskMapper extends GenericCrudMapper<StaffOrderTask, Long> {

	/**
	 * 修改员工队列数
	 * 
	 * @author dmz
	 * @date 2017年2月20日
	 * @param staffOrderTask
	 * @return
	 */
	int updateTaskNum(StaffOrderTask staffOrderTask);

	/**
	 * 查询原初审是否有能力接单(用于优先级分派给原初审)
	 * @param map
	 * @return
	 */
	StaffOrderTask findOriginalApprover(Map<String, Object> map);

	/**
	 * 查询初审待派单员工信息(优先队列规则)
	 * 
	 * @author dmz
	 * @date 2017年2月21日
	 * @param map
	 * @return
	 */
	StaffOrderTask findPriorityTaskOrder(Map<String, Object> map);

	/**
	 *  初审复议派单查询原初审是否可以接单且未超过正常队列上线
	 * @param map
	 * @return
	 */
	StaffOrderTask findReconsiderActiviesTask(Map<String, Object> map);

	/**
	 * 查询初审待派单员工信息(正常队列规则)
	 * 
	 * @author dmz
	 * @date 2017年2月20日
	 * @param map
	 * @return
	 */
	StaffOrderTask findActiviesTaskOrder(Map<String, Object> map);

	/**
	 * 判断初审待派员工信息(挂起队列规则)
	 * 
	 * @author dmz
	 * @date 2017年5月18日
	 * @param map
	 * @return
	 */
	StaffOrderTask findAurrInactiveTaskNum(Map<String, Object> map);

	@Override
    StaffOrderTask findById(Long id);

	StaffOrderTask findOne(Map<String, Object> map);

	List<StaffOrderTask> findAll(Map<String, Object> map);

	/**
	 * 查询终审员(优先队列规则)
	 * @param params 查询参数
	 * @author wulinjie
	 * @return
	 */
	StaffOrderTask findPriorityStaffForFinal(Map<String, Object> params);

	/**
	 * 查询终审员(正常队列规则)
	 * @param params 查询参数
	 * @author wulinjie
	 * @return
	 */
	StaffOrderTask findActivityStaffForFinal(Map<String, Object> params);

	/**
	 * 查询终审待派单员工信息(原协审人员接单)
	 * @author shipf
	 * @date
	 * @param map
	 * @return
	 */
	List<StaffOrderTask> findApprovalStaff(Map<String, Object> map);

	StaffOrderTask findByCodes(@Param("userCode") String userCode, @Param("approveType") String approveType);

	/**
	 * 根据级别查出大于等于该级别的终审人员
	 * 
	 * @param level
	 * @return
	 * @author JiaCX
	 * @date 2017年4月19日 下午12:03:47
	 */
	List<StaffOrderTask> findStaffCodesByLevel(@Param("level") String level);

	/**
	 * 查询终审员或初审员信息(同步员工时使用)
	 * 
	 * @param map
	 * @return
	 */
	List<StaffOrderTask> findByStaffCode(Map<String, Object> map);

	/**
	 * 查询当前终审员工挂起队列是否超过终审挂起队列上限
	 * 
	 * @author shipf
	 * @date
	 * @param map
	 * @return
	 */
	String checkInactiveTask(Map<String, Object> map);

	/**
	 * 根据员工code和审核类型查出接单能力列表
	 * 
	 * @param map
	 * @return
	 * @author JiaCX
	 * @date 2017年5月11日 上午10:39:18
	 */
	Page<StaffOrderTask> findByStaffCodeListAndApprovalType(Map<String, Object> map);
	
	/**
	 * 根据给定的员工code列表查出比其中级别最高的还高(或者相等)的终审人员列表
	 * 
	 * @return
	 * @author JiaCX
	 * @date 2017年6月17日 下午3:59:04
	 */
	List<StaffOrderTask> findListAboveStaffCodes(List<String> list);
	
	/**
	 * 根据给定的员工code列表查出其中最高的终审级别
	 * 
	 * @param staffCodes
	 * @return
	 * @author JiaCX
	 * @date 2017年6月28日 下午2:17:14
	 */
	String findMaxLevelByStaffCodes(String[] staffCodes);

	/**
	 * 批量插入员工接单任务表
	 * 
	 * @author Jia CX
	 * @date 2017年12月26日 下午2:09:48
	 * @notes
	 * 
	 * @param list
	 */
	void batchInsert(List<StaffOrderTask> list);

	/**
	 * 批量更新员工接单任务表
	 * 
	 * @author Jia CX
	 * @date 2017年12月26日 下午2:09:55
	 * @notes
	 * 
	 * @param list
	 */
	void batchUpdate(List<StaffOrderTask> list);

	/**
	 * 批量删除员工接单任务表
	 * 
	 * @author Jia CX
	 * @date 2017年12月26日 下午2:10:00
	 * @notes
	 * 
	 * @param list
	 */
	void batchDelete(List<StaffOrderTask> list);

	/**
	 * 批量禁用员工
	 * 
	 * @author Jia CX
	 * <p>2018年2月1日 上午9:37:36</p>
	 * 
	 * @param needDelete
	 */
	void batchForbidden(List<StaffOrderTask> list);
}
