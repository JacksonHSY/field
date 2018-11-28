/**
 * 
 */
package com.yuminsoft.ams.system.service.approve.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.ymkj.pms.biz.api.vo.response.ResEmployeeVO;
import com.yuminsoft.ams.system.dao.approve.ReconsiderStaffMapper;
import com.yuminsoft.ams.system.domain.approve.ReconsiderStaff;
import com.yuminsoft.ams.system.service.approve.ReconsiderSyncService;
import com.yuminsoft.ams.system.service.pms.PmsApiService;
import com.yuminsoft.ams.system.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 同步复议员工
 * 
 * @author Jia CX
 * <p>2018年6月12日 下午2:22:55</p>
 * 
 */
@Service
@Transactional
public class ReconsiderSyncServiceImpl implements ReconsiderSyncService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ReconsiderSyncServiceImpl.class);

	@Autowired
	private ReconsiderStaffMapper reconsiderMapper;
	
	@Autowired
	private PmsApiService pmsService;
	
	@Override
	public void synchronizeEmpInfo() {
		/*
		 * 平台有，本地有，如果有改变的属性，就更新
		 * 平台有，本地没有，就插入
		 * 平台没有，本地有(而且状态是可用的)，更新为不可用
		 */
		List<ReconsiderStaff> needInsert = new ArrayList<ReconsiderStaff>();
		List<ReconsiderStaff> needUpdate = new ArrayList<ReconsiderStaff>();
		
		List<ReconsiderStaff> localEmps = reconsiderMapper.findAll(new HashMap<String,Object>());
		List<ResEmployeeVO> remoteEmps = pmsService.findAllReviewRoleEmp();
		
		Map<String, Object> localMap = getLocalUsers(localEmps);
		Map<String, Object> remoteMap = new HashMap<String, Object>();
		
		if(CollectionUtils.isNotEmpty(remoteEmps)) {
			for (ResEmployeeVO remoute : remoteEmps) {
				if(localMap.containsKey(remoute.getUsercode())) {
					if(chaged(remoute, (ReconsiderStaff)localMap.get(remoute.getUsercode()))) {
						needUpdate.add(convertUpdateStaff(localMap, remoute));
					}
				} else {
					needInsert.add(convertInsertStaff(remoute));
				}
				remoteMap.put(remoute.getUsercode(), remoute);
			}
		}
		
		if(CollectionUtils.isNotEmpty(localEmps)) {
			for (ReconsiderStaff emp : localEmps) {
				if(!remoteMap.containsKey(emp.getStaffCode()) && "0".equals(emp.getStatus())) {
					emp.setStatus("1");
					emp.setLastModifiedBy("anonymous");
					needUpdate.add(emp);
				}
			}
		}
		
		LOGGER.info("同步复议员工信息，有【" + needInsert.size() + "】条记录需要插入");
		if(CollectionUtils.isNotEmpty(needInsert)) {
			reconsiderMapper.batchInsert(needInsert);
		}
		
		LOGGER.info("同步复议员工信息，有【" + needUpdate.size() + "】条记录需要更新");
		if(CollectionUtils.isNotEmpty(needUpdate)) {
			reconsiderMapper.batchUpdate(needUpdate);
		}
	}

	private ReconsiderStaff convertUpdateStaff(Map<String, Object> localMap, ResEmployeeVO remoute) {
		ReconsiderStaff staff = (ReconsiderStaff) localMap.get(remoute.getUsercode());
		staff.setStaffName(remoute.getName());
		staff.setStaffEmail(remoute.getEmail());
		staff.setRuleLevel(Strings.join(",", remoute.getRoleCodes().toArray()));
		staff.setStatus(null == remoute.getStatus() ? null : remoute.getStatus().toString());
		staff.setLastModifiedBy("anonymous");
		return staff;
	}

	/**
	 * 判断pms和ams的员工信息是否不一致
	 * 
	 * @author Jia CX
	 * <p>2018年6月12日 下午6:20:18</p>
	 * 
	 * @param remoute
	 * @param local
	 * @return true表示不一致
	 */
	private boolean chaged(ResEmployeeVO remoute, ReconsiderStaff local) {
		return Strings.isDiff(local.getStaffName(), remoute.getName()) 
				|| Strings.isDiff(local.getStaffEmail(), remoute.getEmail()) 
				|| Strings.isDiff(local.getRuleLevel(), Strings.join(",", remoute.getRoleCodes().toArray()))
				|| Strings.isDiff(local.getStatus(), remoute.getStatus());
	}

	private ReconsiderStaff convertInsertStaff(ResEmployeeVO remoute) {
		ReconsiderStaff staff = new ReconsiderStaff();
		staff.setStaffCode(remoute.getUsercode());
		staff.setStaffName(remoute.getName());
		staff.setStaffEmail(remoute.getEmail());
		staff.setIfAccept("Y");
		staff.setRuleLevel(Strings.join(",", remoute.getRoleCodes().toArray()));
		staff.setStatus(null == remoute.getStatus() ? null : remoute.getStatus().toString());
		staff.setCreatedBy("anonymous");
		staff.setLastModifiedBy("anonymous");
		return staff;
	}

	/**
	 * 返回key：userCode，value：ReconsiderStaff
	 * 
	 * @author Jia CX
	 * <p>2018年6月12日 下午5:54:33</p>
	 * 
	 * @param localEmps
	 * @return
	 */
	private Map<String, Object> getLocalUsers(List<ReconsiderStaff> localEmps) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(CollectionUtils.isNotEmpty(localEmps)) {
			for (ReconsiderStaff emp : localEmps) {
				map.put(emp.getStaffCode(), emp);
			}
		}
		return map;
	}

}
