package com.yuminsoft.ams.system.service.system.impl;

import com.alibaba.fastjson.JSON;
import com.ymkj.base.core.biz.api.message.Response;
import com.ymkj.pms.biz.api.service.IEmployeeExecuter;
import com.ymkj.pms.biz.api.vo.request.ReqParamVO;
import com.ymkj.pms.biz.api.vo.response.ResEmployeeVO;
import com.yuminsoft.ams.system.common.EnumUtils;
import com.yuminsoft.ams.system.dao.approve.StaffOrderAbilityMapper;
import com.yuminsoft.ams.system.dao.approve.StaffOrderSetMapper;
import com.yuminsoft.ams.system.dao.approve.StaffOrderTaskMapper;
import com.yuminsoft.ams.system.dao.system.SysParamDefineMapper;
import com.yuminsoft.ams.system.domain.approve.StaffOrderAbility;
import com.yuminsoft.ams.system.domain.approve.StaffOrderSet;
import com.yuminsoft.ams.system.domain.approve.StaffOrderTask;
import com.yuminsoft.ams.system.domain.system.SysParamDefine;
import com.yuminsoft.ams.system.exception.BusinessException;
import com.yuminsoft.ams.system.service.BaseService;
import com.yuminsoft.ams.system.service.system.StaffAbilityService;
import com.yuminsoft.ams.system.util.BeanUtil;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 接单能力
 * 
 * @author fsj
 *
 */
@Service
public class StaffAbilityServiceImpl extends BaseService implements StaffAbilityService {

	@Autowired
	private StaffOrderAbilityMapper staffOrderAbilityMapper;
	@Autowired
	private SysParamDefineMapper sysParamDefineMapper;
	@Autowired
	private StaffOrderSetMapper staffOrderSetMapper;
	@Autowired
	private IEmployeeExecuter employeeExecuter;
	@Autowired
	private StaffOrderTaskMapper staffOrderTaskMapper;

	/**
	 * 获取能力列表
	 * 
	 * @author dmz
	 * @date 2017年3月29日
	 * @param map
	 * @return
	 */
	@Override
	public List<StaffOrderAbility> getStaffOrderAbility(Map<String, Object> map) {
		return staffOrderAbilityMapper.findAll(map);
	}

	/**
	 * 根据员工编号获取员队列配置上限
	 * 
	 * @author dmz
	 * @date 2017年4月10日
	 * @return
	 */
	@Override
	public StaffOrderSet getStaffOrderSetByStaffCodeAndTaskDefId(String staffCode, String taskDefId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("staffCode", staffCode);
		//map.put("taskDefId", taskDefId);
		StaffOrderSet sos = staffOrderSetMapper.findOne(map);

		return sos;
	}

	/**
	 * 初审组参数查询
	 */
	@Override
	public List<SysParamDefine> findByParamType(String paramType) {
		if (StringUtils.isEmpty(paramType)) {
			paramType = "first_org";
		}
		SysParamDefine dict = new SysParamDefine();
		dict.setParamType(paramType);
		return sysParamDefineMapper.findByParamDefine(dict);
	}

	/**
	 * 保存机构上限配置
	 * 
	 * @param staffOrderSet
	 * @param orgId 机构编码
	 * @return
	 */
	@Override
	public Boolean saveStaffAbility(StaffOrderSet staffOrderSet, Long orgId) {
		Boolean bol = false;
		if (orgId != null) {
			ReqParamVO request = new ReqParamVO();
			request.setSysCode(sysCode);
			request.setOrgId(orgId);
			Response<List<ResEmployeeVO>> response = employeeExecuter.findEmpsByOrgId(request);
			LOGGER.info("根据机构id查询全部员工  findEmpsByOrgId params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
			List<ResEmployeeVO> list = response.getData();
			if (null != response.getData()) {
				for (ResEmployeeVO vo : list) {
					staffOrderSetMapper.deleteByUserCode(vo.getUsercode());
					staffOrderSet.setStaffCode(vo.getUsercode());
					staffOrderSetMapper.save(staffOrderSet);
					bol = true;
				}
			}
		}
		return bol;
	}

	/**
	 * 批量保存用户队列上线设置
	 * 
	 * @param staffOrderSet
	 * @param userCode 		用户编码
	 * @return
	 */
	@Override
	public Boolean saveUserStaffAbility(StaffOrderSet staffOrderSet, String userCode) {
		boolean action = false;
		if (null != userCode && !userCode.isEmpty()) {//
			String[] userCodeArray = userCode.split(",");
			for (String code : userCodeArray) {
				if (null != code && !code.isEmpty()) {
					staffOrderSet.setStaffCode(code);
					// 初审终审共用一个队列上线
					StaffOrderSet queryStaffOrderSet = getStaffOrderSetByStaffCodeAndTaskDefId(code, staffOrderSet.getTaskDefId());
					int changeNum = -1;
					if (queryStaffOrderSet != null) {// 判断是否存在
						changeNum = staffOrderSetMapper.updateUserCode(staffOrderSet);
					} else {
						changeNum = staffOrderSetMapper.save(staffOrderSet);
					}
					if (1 != changeNum) {
						throw new BusinessException("批量保存用户队列上线设置失败,修改或保存条数:" + changeNum);
					} else {
						action = true;
					}
				}
			}

		}
		return action;
	}

	/**
	 * 添加或修改员工接单能力(只有初审有)
	 * 
	 * @author dmz
	 * @date 2017年3月29日
	 * @param userCodeList
	 * @param staffAbilityList
	 * @return
	 */
	@Override
	@SneakyThrows
	public void saveOrUpdateStaffAbilityService(List<String> userCodeList, List<StaffOrderAbility> staffAbilityList) {
		for (String userCode : userCodeList) {
			// 删除员工接单能力设置
			staffOrderAbilityMapper.deleteByStaffCode(userCode);
			// 重新添加员工接单能力
			for (StaffOrderAbility staffOrderAbility : staffAbilityList) {
				StaffOrderAbility soa = new StaffOrderAbility();
				BeanUtil.copyProperties(soa, staffOrderAbility);
				soa.setProductCode(staffOrderAbility.getProductCode() + "-" + staffOrderAbility.getType());
				soa.setStaffCode(userCode);
				staffOrderAbilityMapper.save(soa);
			}

			Map<String, Object> params = new HashMap<String, Object>();// 判断是否存在队列
			params.put("staffCode", userCode);
			params.put("taskDefId", EnumUtils.FirstOrFinalEnum.FIRST.getValue());
			StaffOrderTask staffOrderTask = staffOrderTaskMapper.findOne(params);
			if (null == staffOrderTask) {// 初始化员工接单队列
				staffOrderTask = new StaffOrderTask();
				staffOrderTask.setCurrActivieTaskNum(0);
				staffOrderTask.setCurrInactiveTaskNum(0);
				staffOrderTask.setCurrPriorityNum(0);
				staffOrderTask.setIfAccept(EnumUtils.YOrNEnum.Y.getValue());
				staffOrderTask.setStaffCode(userCode);
				staffOrderTask.setStatus(EnumUtils.DisplayEnum.ENABLE.getValue());
				staffOrderTask.setTaskDefId(EnumUtils.FirstOrFinalEnum.FIRST.getValue());
				staffOrderTask.setVersion(0);
				staffOrderTask.setWaitTime(new Date());
				staffOrderTaskMapper.insert(staffOrderTask);
			}
		}


	}

	//
	/**
	 * add by zw at 2017-04-28 根据ReformVOA 查询到员工的接单能力
	 * 
	 * @author zw
	 * @date 2017年04月28日
	 * @param map
	 * @return
	 */
	@Override
	public boolean getStaffOrderAbilityByMap(Map<String, Object> map) {
		StaffOrderAbility staffOrderAbility = staffOrderAbilityMapper.findOne(map);
		boolean flag = false;
		if (null != staffOrderAbility) {
			if (0 != staffOrderAbility.getValue()) {// 判断该用户的接单能力是否为0
				flag = true;
			}
		}
		return flag;
	}
	// end at 2017-04-28

	/**
	 * 同步初审员工队列表
	 */
	@Override
	public int syncStaffOrderSetAndTask() {
		return staffOrderSetMapper.syncWithOrderTask();
	}

	/**
	 * 根据用户编号查询接单能力更新时间(接单能力配置显示时间用)
	 * @param staffCode
	 * @return
	 */
	@Override
	public StaffOrderAbility getStaffOrderAbilityByStaffCode(String staffCode) {
		return staffOrderAbilityMapper.findStaffOrderAbilityByStaffCode(staffCode);
	}

	/**
	 * 查询任务数
	 * @return
	 */
	@Override
	public List<StaffOrderTask> getListStaffOrderTask(Map<String, Object> map) {
		return staffOrderTaskMapper.findAll(map);
	}


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
	@Override
	public List<StaffOrderAbility> getStaffOrderAbilityByUserCodes(List<String> list) {
		return staffOrderAbilityMapper.findByUserCodes(list);
	}

	@Override
	public List<StaffOrderAbility> getByMultiProduct(String staffCode, List<StaffOrderAbility> abilities) {

		return staffOrderAbilityMapper.findByMultiProductAndArea(staffCode, abilities);
	}

}
