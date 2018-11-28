package com.yuminsoft.ams.system.job;

import com.alibaba.fastjson.JSON;
import com.ymkj.pms.biz.api.vo.response.ResEmployeeVO;
import com.yuminsoft.ams.system.common.EnumUtils;
import com.yuminsoft.ams.system.domain.approve.StaffOrderTask;
import com.yuminsoft.ams.system.service.approve.StaffOrderTaskService;
import com.yuminsoft.ams.system.service.pms.PmsApiService;
import org.quartz.DisallowConcurrentExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 初审同步用户信息
 * 
 * @author dmz
 * @date 2017年5月16日
 */
@Component
@DisallowConcurrentExecution
public class FirstSynchronizationUser {
	private final static Logger LOGGER = LoggerFactory.getLogger(FirstSynchronizationUser.class);
	@Autowired
	private PmsApiService pmsApiService;
	@Autowired
	private StaffOrderTaskService staffOrderTaskService;

	// 定时器
	public void initSynchronizationUser() {
		// 查询有初审角色的初审员工
		List<ResEmployeeVO> employeeVOList = pmsApiService.findByRoleCode();
		LOGGER.debug(" 查询有初审角色的初审员工 response:{}", JSON.toJSONString(employeeVOList));
		addOrUpdateStaff(EnumUtils.FirstOrFinalEnum.FIRST.getValue(), employeeVOList);
	}

	/**
	 *
	 * @param orgTypeCode- 初审或直通车初审
	 * @param employeeVOList-员工列表
	 */
	private void addOrUpdateStaff (String orgTypeCode,List<ResEmployeeVO> employeeVOList) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("taskDefId", orgTypeCode);
		List<StaffOrderTask> staffOrderTaskList = staffOrderTaskService.findAllService(map);
		LOGGER.debug("查询所用初审人员 response:{}", JSON.toJSONString(staffOrderTaskList));
		if (CollectionUtils.isEmpty(staffOrderTaskList) || CollectionUtils.isEmpty(employeeVOList)) {
			// 全部添加
			if (CollectionUtils.isEmpty(staffOrderTaskList) && !CollectionUtils.isEmpty(employeeVOList)) {
				for (ResEmployeeVO employeeVo : employeeVOList) {
					StaffOrderTask sot = new StaffOrderTask();
					sot.setCurrActivieTaskNum(0);
					sot.setCurrInactiveTaskNum(0);
					sot.setCurrPriorityNum(0);
					sot.setIfAccept(EnumUtils.YOrNEnum.Y.getValue());
					sot.setStaffCode(employeeVo.getUsercode());
					sot.setStatus(EnumUtils.DisplayEnum.ENABLE.getValue());
					sot.setTaskDefId(orgTypeCode);
					sot.setVersion(0);
					sot.setWaitTime(new Date());
					staffOrderTaskService.save(sot);
				}
			} else {// 全部禁用
				for (StaffOrderTask sot : staffOrderTaskList) {
					StaffOrderTask sotNew = new StaffOrderTask();
					sotNew.setId(sot.getId());
					sotNew.setStatus(EnumUtils.DisplayEnum.DISABLE.getValue());
					staffOrderTaskService.update(sotNew);
				}
			}
		}
		if (!CollectionUtils.isEmpty(staffOrderTaskList) && !CollectionUtils.isEmpty(employeeVOList)) {
			for (StaffOrderTask sot : staffOrderTaskList) {
				StaffOrderTask sotNew = new StaffOrderTask();
				boolean mark = true;
				for (ResEmployeeVO employeeVo : employeeVOList) {
					if (sot.getStaffCode().equals(employeeVo.getUsercode())) {
						mark = false;
						if (EnumUtils.DisplayEnum.DISABLE.getValue().equals(sot.getStatus())) {// 判断员工是禁用
							sotNew.setId(sot.getId());
							sotNew.setStatus(EnumUtils.DisplayEnum.ENABLE.getValue());
							staffOrderTaskService.update(sotNew);
						}
						employeeVOList.remove(employeeVo);
						break;
					}
				}
				if (mark && EnumUtils.DisplayEnum.ENABLE.getValue().equals(sot.getStatus())) {// 禁用
					sotNew.setId(sot.getId());
					sotNew.setStatus(EnumUtils.DisplayEnum.DISABLE.getValue());
					staffOrderTaskService.update(sotNew);
					mark = true;
				}
			}
			// 剩下的员工全部添加
			for (ResEmployeeVO employeeVo : employeeVOList) {
				StaffOrderTask sot = new StaffOrderTask();
				sot.setCurrActivieTaskNum(0);
				sot.setCurrInactiveTaskNum(0);
				sot.setCurrPriorityNum(0);
				sot.setIfAccept(EnumUtils.YOrNEnum.Y.getValue());
				sot.setStaffCode(employeeVo.getUsercode());
				sot.setStatus(EnumUtils.DisplayEnum.ENABLE.getValue());
				sot.setTaskDefId(orgTypeCode);
				sot.setVersion(0);
				sot.setWaitTime(new Date());
				staffOrderTaskService.save(sot);
			}
		}
	}
}
