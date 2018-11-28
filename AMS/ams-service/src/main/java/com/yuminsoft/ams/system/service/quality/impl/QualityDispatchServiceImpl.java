package com.yuminsoft.ams.system.service.quality.impl;

import com.alibaba.fastjson.JSON;
import com.ymkj.pms.biz.api.vo.response.ResEmployeeVO;
import com.yuminsoft.ams.system.common.QualityEnum;
import com.yuminsoft.ams.system.domain.quality.QualityCheckInfo;
import com.yuminsoft.ams.system.domain.quality.QualityLog;
import com.yuminsoft.ams.system.domain.quality.QualityTaskInfo;
import com.yuminsoft.ams.system.service.pms.PmsApiService;
import com.yuminsoft.ams.system.service.quality.QualityCheckInfoService;
import com.yuminsoft.ams.system.service.quality.QualityDispatchService;
import com.yuminsoft.ams.system.service.quality.QualityLogService;
import com.yuminsoft.ams.system.service.quality.QualityTaskInfoService;
import com.yuminsoft.ams.system.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 自动派单
 * @author lihm
 *@data 2017年5月4日上午9:17:22
 */
@Service
public class QualityDispatchServiceImpl implements QualityDispatchService {
	private final static Logger LOGGER = LoggerFactory.getLogger(QualityDispatchServiceImpl.class);
	
	@Autowired
	private QualityCheckInfoService qualityCheckInfoService;
	
	@Autowired
	private QualityTaskInfoService taskInfoService;
	
	@Autowired
	private QualityLogService qualityLogService;
	
	@Autowired
	private PmsApiService pmsApiService;
	
	@Value("${sys.code}")
	private String sysCode;
	
	@Transactional
	@Override
	public void dispatch() {
		LOGGER.info(" ************质检自动分单开始************");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("isClosed", "1");
		List<QualityCheckInfo> findForAssignList = new ArrayList<>();

		//获取待派单的质检单,按照 1、滞留批次 2、是否常规 3、通过件 4、拒绝件排序
		findForAssignList = qualityCheckInfoService.findForAssign(map);
		if(findForAssignList.isEmpty()){
			LOGGER.info("-------------当前无待分派质检单！！");
			LOGGER.info("*****************质检分派完成*****************");
			return;
		}
		
		for (QualityCheckInfo qualityCheckInfo: findForAssignList) {

			//获取当前接单的质检员
			QualityTaskInfo taskinfo = getUserForAssign();

			if (null == taskinfo) {
				LOGGER.info("-------------无合适的待分配质检人员！！");
				LOGGER.info("*****************质检分派完成*****************");
				return;
			}
			qualityCheckInfo.setCheckUser(taskinfo.getCheckUser());
			qualityCheckInfo.setAssignDate(new Date());
			qualityCheckInfo.setAssignType(QualityEnum.QualityAssignType.AUTO_ASSIGN.getCode());
			qualityCheckInfo.setLastModifiedDate(new Date());
			int updateCount = updateCheckInfo(qualityCheckInfo);//更新质检件
			if(updateCount == 0){
				LOGGER.info("无法分派：质检件已经被修改，无法分配！！质检件属性：{}", JSON.toJSONString(qualityCheckInfo));
				continue;
			}
			if(updateCount == -1){
				LOGGER.info("分派失败：质检件分派失败！！质检件属性：{}", JSON.toJSONString(qualityCheckInfo));
				continue;
			}
			saveToLog(qualityCheckInfo);//保存日志
			LOGGER.info("派单操作：质检件 [{}] 分派给了： {}", qualityCheckInfo.getLoanNo(),qualityCheckInfo.getCheckUser());
		}
		LOGGER.info("*****************质检分派完成*****************");
	}
	
	/**
	 * 保存操作日志
	 * @author lihuimeng
	 * @date 2017年6月6日 下午1:37:17
	 */
	private int saveToLog(QualityCheckInfo qualityCheckInfo) {
		//质检自动分配operation由value修正为code
		QualityLog log = new QualityLog(qualityCheckInfo.getLoanNo(), "系统自动分派给"+qualityCheckInfo.getCheckUser(), QualityEnum.QualityLogLink.ASSIGN.getCode(), QualityEnum.QualityLogOperation.AUTO_ASSIGN.getCode());
		log.setCreatedBy(sysCode);
		log.setLastModifiedBy(sysCode);
		int saveCount = 0;
		try {
			saveCount = qualityLogService.save(log);
		} catch (Exception e) {
			LOGGER.error("保存日志失败，单号：<{}>, exception:", qualityCheckInfo.getLoanNo(),e);
		}
		return saveCount;
	}

	/**
	 * 更新质检件
	 * @author lihuimeng
	 * @date 2017年6月6日 下午12:48:55
	 */
	private int updateCheckInfo(QualityCheckInfo qualityCheckInfo) {
		int updateCount = 0;
		try {
			updateCount = qualityCheckInfoService.updateStatus(qualityCheckInfo);
		} catch (Exception e) {
			updateCount = -1;
			LOGGER.error("质检件 {} 分配失败！！,exception", JSON.toJSONString(qualityCheckInfo),e);
		}
		return updateCount;
	}

	/**
	 * 查询待分派质检员
	 * @author lihuimeng
	 * @date 2017年6月6日 下午12:49:26
	 */
	private QualityTaskInfo getUserForAssign() {
		QualityTaskInfo qualityTaskInfo = null;
		Map<String, Object> map = new HashMap<>();
		map.put("ifAccept", "0");
		map.put("status",QualityEnum.QualityStatus.QUALITY_TEMPORARY_SAVE.getCode());

		//获取当前可以被派单的质检人员
		List<QualityTaskInfo> userList = taskInfoService.findForAssign(map);
		List<QualityTaskInfo> taskList = new ArrayList<QualityTaskInfo>();

		for(int i = 0; i<userList.size(); i++){
			ResEmployeeVO resEmployeeVO = pmsApiService.findByAccount(userList.get(i).getCheckUser());

			if(null != resEmployeeVO && "t".equals(resEmployeeVO.getInActive())){//如果该员工在职
				taskList.add(userList.get(i));
			} else {
				LOGGER.info("查询员工信息为不在职，工号：<{}>,result:<{}>", userList.get(i).getCheckUser(), JSON.toJSONString(resEmployeeVO));
			}
		}

		if(!CollectionUtils.isEmpty(taskList)){
			qualityTaskInfo = taskList.get(0);//获取当前任务量最少的质检员
		}
		return qualityTaskInfo;
	}

}