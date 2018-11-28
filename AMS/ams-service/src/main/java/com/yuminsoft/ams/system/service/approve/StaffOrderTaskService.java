package com.yuminsoft.ams.system.service.approve;

import com.yuminsoft.ams.system.common.EnumUtils;
import com.yuminsoft.ams.system.domain.approve.StaffOrderTask;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.vo.finalApprove.StaffOrderTaskVO;

import java.util.List;
import java.util.Map;

/**
 * @author dmz
 * @date 2017年2月17日
 */
public interface StaffOrderTaskService {

	/**
	 * 查询所有对象
	 * 
	 * @author dmz
	 * @date 2017年5月17日
	 * @param map
	 * @return
	 */
	List<StaffOrderTask> findAllService(Map<String, Object> map);

	/**
	 * 更新员工接单任务表
	 * 
	 * @param staffOrderTask
	 * @return
	 */
	boolean update(StaffOrderTask staffOrderTask);

	/**
	 * 添加员工接单任务表
	 * 
	 * @author dmz
	 * @date 2017年5月17日
	 * @param staffOrderTask
	 * @return
	 */
	boolean save(StaffOrderTask staffOrderTask);

	/**
	 * 查询单个对象
	 * 
	 * @param map
	 * @return
	 */
	StaffOrderTask findOneService(Map<String, Object> map);

	/**
	 * 删除
	 * 
	 * @param id
	 * @return
	 */
	Result<String> delete(Long id);

	/**
	 * 更新员工队列数
	 * @param staffCode 员工编号
	 * @param rtfNode   审批环节(初审or终审)
	 * @param calType	计算类型(加1 or 减1)
	 * @param staffTaskType 队列类型
	 */
	void updateStaffTaskNum(String staffCode, EnumUtils.FirstOrFinalEnum rtfNode,EnumUtils.CalType calType,EnumUtils.StaffTaskType staffTaskType);

	/**
	 * 查询原初审是否有能力接单(用于优先级分派给原初审)
	 * @param map
	 * @return
	 */
	StaffOrderTask getOriginalApprover(Map<String, Object> map);

	/**
	 * 查询初审待派单员工信息(优先队列规则)
	 * 
	 * @author dmz
	 * @date 2017年2月21日
	 * @param map
	 * @return
	 */
	StaffOrderTask findPriorityTaskOrderService(Map<String, Object> map);


	/**
	 *  初审复议派单查询原初审是否可以接单且未超过正常队列上线
	 * @param map
	 * @return
	 */
	StaffOrderTask getReconsiderActiviesTask(Map<String, Object> map);

	/**
	 * 查询初审待派单员工信息(正常队列规则)
	 * 
	 * @author dmz
	 * @date 2017年2月20日
	 * @param map
	 * @return
	 */
	StaffOrderTask findActiviesTaskOrderService(Map<String, Object> map);

	/**
	 * 判断初审挂起队列是否达到上限
	 * 
	 * @author dmz
	 * @date 2017年5月18日
	 * @param staffCode
	 * @return
	 */
	Result<String> findAurrInactiveTaskNumService(String staffCode);

	/**
	 * 设置员工是否接单(Y/N)
	 * 
	 * @author dmz
	 * @date 2017年3月7日
	 * @param ifAccept-标记Y或者N枚举
	 * @param taskDefId-标记初审终审枚举
	 * @param staffCode-账户编号
	 * @return
	 */
	Result<String> isAcceptService(String ifAccept, String taskDefId, String staffCode);

	/**
	 * 查询当前终审员工挂起队列是否超过终审挂起队列上限
	 * 
	 * @author shipf
	 * @date
	 * @return
	 */
	String checkInactiveTask();

	/**
	 * 终审改派时查询时，获取终审人员列表
	 * 
	 * @return
	 * @author JiaCX
	 * @date 2017年5月22日 上午11:10:22
	 */
	List<StaffOrderTaskVO> getFinalAcceptOrder();

	/**
	 * 查找出出终审人员的正常队列上限最大值
	 * 
	 * @author zhouwen
	 * @date 2017年6月16日
	 * @return
	 */
	String findMaxNormalQueue();

	/**
	 * 查找出出终审人员的挂起队列上限最大值
	 * 
	 * @author zhouwen
	 * @date 2017年6月16日
	 * @return
	 */
	String findMaxHangQueue();

	/**
	 * 从平台同步员工信息到信审系统
	 * 
	 * @author Jia CX
	 * @date 2017年12月26日 上午10:39:16
	 * @notes
	 * 
	 */
	void synchronizeEmpInfoNew();

}
