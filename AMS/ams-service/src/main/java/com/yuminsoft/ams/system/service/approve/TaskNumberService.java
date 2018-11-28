package com.yuminsoft.ams.system.service.approve;

import java.util.List;

import com.ymkj.pms.biz.api.vo.response.ResEmpOrgVO;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.vo.apply.TaskNumber;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;

/**
 * @author jiacx
 */
public interface TaskNumberService {
    /**
     * 人员接单状态修改
     * 
     * @param userCode
     * @param isaccept
     * @param personRole
     * @return
     * @author JiaCX
     * @date 2017年5月16日 下午5:42:44
     */
    Result<String> updateTaskNumber(String userCode, String isaccept, String personRole);

    /**
     * 按条件查出人员接单能力列表
     * 
     * @param requestPage
     * @param firstGroup
     * @param finalGroup
     * @param taskNumber
     * @return
     * @author JiaCX
     * @date 2017年5月11日 上午10:24:52
     */
    ResponsePage<TaskNumber> getOrderTaskList(RequestPage requestPage, List<ResEmpOrgVO> firstGroup, List<ResEmpOrgVO> finalGroup, TaskNumber taskNumber);

    /**
     * 获取当前用户角色类型
     * 
     * @return 0:初审或者终审经理以上级别；1：初审经理或者主管或者组长；2：终审经理或者主管或者组长
     * @author JiaCX
     * @date 2017年6月23日 下午3:59:40
     */
    String getRoleTypeOfCurrentUser();

    /**
     * 同步员工任务数
     * @authro wulj
     */
    void syncTaskNumber();

	/**
	 * 获取任务数列表
	 * 
	 * @author Jia CX
	 * <p>2018年4月11日 上午10:16:01</p>
	 * 
	 * @param requestPage
	 * @param tasknum
	 * @return
	 */
	ResponsePage<TaskNumber> getTaskList(RequestPage requestPage, TaskNumber tasknum);
}
