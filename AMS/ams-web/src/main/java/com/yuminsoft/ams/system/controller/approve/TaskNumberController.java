package com.yuminsoft.ams.system.controller.approve;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.ymkj.pms.biz.api.enums.OrganizationEnum;
import com.ymkj.pms.biz.api.enums.RoleEnum;
import com.ymkj.pms.biz.api.vo.request.ReqLevelVO;
import com.ymkj.pms.biz.api.vo.response.ResEmpOrgVO;
import com.yuminsoft.ams.system.annotation.UserLogs;
import com.yuminsoft.ams.system.service.approve.TaskNumberService;
import com.yuminsoft.ams.system.service.pms.PmsApiService;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.util.Result.Type;
import com.yuminsoft.ams.system.vo.apply.TaskNumber;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;

/**
 * 任务数查询
 * 
 * @author fusj
 *
 */
@Controller
@RequestMapping("/taskNumber")
public class TaskNumberController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskNumberController.class);
    
	@Autowired
	private TaskNumberService taskNumberServiceImpl;

	@Autowired
	private PmsApiService pmsApiServiceImpl;
	
	@Value("${sys.code}")
	private String sysCode;
	
	/**
	 * 获取任务数列表
	 * 
	 * @author Jia CX
	 * <p>2018年4月11日 上午10:15:04</p>
	 * 
	 * @param requestPage
	 * @param tasknum
	 * @return
	 */
	@RequestMapping("/getTaskList")
	public @ResponseBody ResponsePage<TaskNumber> getTaskList(RequestPage requestPage,TaskNumber tasknum){
		return taskNumberServiceImpl.getTaskList(requestPage,tasknum);
	}
	
	/**
	 * 分页
	 * @author 
	 * @return
	 */
	@RequestMapping("/index")
	public @ResponseBody ResponsePage<TaskNumber> getPage(RequestPage requestPage,TaskNumber tasknum) {
		
		ReqLevelVO req = new ReqLevelVO();
		
		//当前登录用户的下级初审组列表
		req.setLevelType(OrganizationEnum.OrgCode.CHECK.getCode());
		req.setRoleCode(RoleEnum.CHECK.getCode());
		List<ResEmpOrgVO> firstGroup = pmsApiServiceImpl.getLowerEmpByAccount(req);
		
		//当前登录用户的下级终审组列表
		req.setLevelType(OrganizationEnum.OrgCode.FINAL_CHECK.getCode());
		req.setRoleCode(RoleEnum.FINAL_CHECK.getCode());
		List<ResEmpOrgVO> finalGroup = pmsApiServiceImpl.getLowerEmpByAccount(req);
		
		return taskNumberServiceImpl.getOrderTaskList(requestPage,firstGroup,finalGroup,tasknum);
	}
	

	
	/**
	 * 批量修改
	 * 是否接单
	 * @param userCode
	 * @return
	 */
	@RequestMapping("/batchUpdateTask")
	@ResponseBody
	@UserLogs(link = "是否接单", operation = "任务数查询批量开启关闭接单", type = com.yuminsoft.ams.system.domain.system.UserLog.Type.开启关闭接单)
	public Result<String> batchUpdateTask(String userCode,String isaccept,String personRole)
	{
	    LOGGER.info("需要更新的userCode params:{} ", JSON.toJSONString(userCode));
	    Result<String> result = new Result<String>();
        if (StringUtils.isNotEmpty(userCode)) {
            int failed = 0;
            String[] code = userCode.split(",");
            String[] type = personRole.split(",");
            
            for (int i = 0; i < code.length; i++) {
                String taskDefId = type[i];
                Result<String> re = taskNumberServiceImpl.updateTaskNumber(isaccept, taskDefId, code[i]);
                if (re.failure()) {
                    failed = failed + 1;
                    result.addMessage("为用户" + code[i] + "设置接单开关失败。");
                }
            }
            if(failed == code.length){//全部失败，就设认为是失败
                result.setType(Type.FAILURE);
            }else if(failed == 0){
                result.setType(Type.SUCCESS);
                result.addMessage("设置成功.");
            }else{
                result.setType(Type.SUCCESS);
            }
        }else{
            result.addMessage("设置失败,没有选择要设置的人员.");
            result.setType(Type.FAILURE);
        }
        return result;
	}
	
	/**
	 * 获取当前用户角色类型
	 * 
	 * @return
	 * @author JiaCX
	 * @date 2017年6月23日 下午3:59:05
	 */
	@RequestMapping("/roleType")
    public @ResponseBody String getRoleTypeOfCurrentUser() {
	    return taskNumberServiceImpl.getRoleTypeOfCurrentUser();
	}

}
