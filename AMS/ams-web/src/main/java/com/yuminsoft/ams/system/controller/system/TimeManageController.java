package com.yuminsoft.ams.system.controller.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ymkj.pms.biz.api.vo.response.ResCalendarVO;
import com.ymkj.pms.biz.api.vo.response.ResRoleVO;
import com.yuminsoft.ams.system.service.system.TimeManageService;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;
import com.yuminsoft.ams.system.vo.system.TimeManageAddParamIn;
import com.yuminsoft.ams.system.vo.system.TimeManageCheckUserParamIn;
import com.yuminsoft.ams.system.vo.system.TimeManageCheckUserParamOut;
import com.yuminsoft.ams.system.vo.system.TimeManageListParamIn;
import com.yuminsoft.ams.system.vo.system.TimeManageListParamOut;

/**
 * 系统使用时间管理控制器
 * 
 * @author Jia CX
 * @date 2017年12月11日 上午11:11:20
 * @notes
 * 
 */
@Controller
@RequestMapping("/timeManage")
public class TimeManageController {

	@Autowired
	private TimeManageService timeManageService;
	
	@RequestMapping("/index")
	public String index() {
		return "/system/timeManage/timeManage";
	}

	/**
	 * 查询角色时间限制列表
	 * 
	 * @author Jia CX
	 * @date 2017年12月11日 上午11:20:09
	 * @notes
	 * 
	 * @param requestPage
	 * @param paramIn
	 * @return
	 */
	@RequestMapping("/getList")
	public @ResponseBody ResponsePage<TimeManageListParamOut> getList( @RequestBody TimeManageListParamIn paramIn) {
		return timeManageService.getList(paramIn);
	}
	
	/**
	 * 新增角色时间限制记录
	 * 
	 * @author Jia CX
	 * @date 2017年12月11日 上午11:47:52
	 * @notes
	 * 
	 * @param paramIn
	 * @return
	 */
	@RequestMapping("/add")
	public @ResponseBody Result<String> add(@RequestBody TimeManageAddParamIn paramIn){
		return timeManageService.add(paramIn);
	}
	
	/**
	 * 修改角色时间限制记录
	 * 
	 * @author Jia CX
	 * @date 2017年12月11日 上午11:54:29
	 * @notes
	 * 
	 * @param paramIn
	 * @return
	 */
	@RequestMapping("/update")
	public @ResponseBody Result<String> update(TimeManageAddParamIn paramIn){
		return timeManageService.update(paramIn);
	}
	
	/**
	 * 批量修改
	 * 
	 * @author Jia CX
	 * <p>2018年5月15日 下午7:18:00</p>
	 * 
	 * @param paramIn
	 * @return
	 */
	@RequestMapping("/batchUpdate")
	public @ResponseBody Result<String> batchUpdate(TimeManageAddParamIn paramIn){
		return timeManageService.batchUpdate(paramIn);
	}
	
	/**
	 * 删除角色时间限制记录
	 * 
	 * @author Jia CX
	 * @date 2017年12月11日 上午11:54:46
	 * @notes
	 * 
	 * @param paramIn
	 * @return
	 */
	@RequestMapping("/delete")
	public @ResponseBody Result<String> delete(TimeManageAddParamIn paramIn){
		return timeManageService.delete(paramIn);
	}
	
	/**
	 * 批量删除
	 * 
	 * @author Jia CX
	 * <p>2018年5月15日 下午3:57:18</p>
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping("/batchDelete")
	public @ResponseBody Result<String> batchDelete(@RequestBody String[] ids){
		return timeManageService.batchDelete(ids);
	}
	
	
	/**
	 * 新增初审员时间限制弹框时，获取满足条件的初审员列表
	 * 
	 * @author Jia CX
	 * <p>2018年5月15日 上午10:04:50</p>
	 * 
	 * @return
	 */
	@RequestMapping("/getCheckUsers")
	public @ResponseBody List<TimeManageCheckUserParamOut> getCheckUsers(@RequestBody TimeManageCheckUserParamIn paramIn) {
		return timeManageService.getCheckUsers(paramIn);
	}
	
	/**
	 * 判断某条记录是否存在
	 * 
	 * @author Jia CX
	 * <p>2018年5月15日 下午1:53:16</p>
	 * 
	 * @param paramIn
	 * @return true 存在,false 不存在
	 */
	@RequestMapping("/hasRecord")
	public @ResponseBody boolean hasRecord(TimeManageAddParamIn paramIn) {
		return null != timeManageService.getRecord(paramIn.getRole(),paramIn.getTimeType(),paramIn.getDate(), null);
	}
	
	/**
	 * 获取当前登陆用户组织机构内的员工的角色列表
	 * 
	 * @author Jia CX
	 * <p>2018年5月16日 上午10:27:43</p>
	 * 
	 * @return
	 */
	@RequestMapping("/getRole")
	public @ResponseBody List<ResRoleVO> getRole(){
		return timeManageService.getRoleByAccount();
	}
	
	/**
	 * 获取日历信息
	 * 
	 * @author Jia CX
	 * <p>2018年5月21日 上午11:32:17</p>
	 * 
	 * @return
	 */
	@RequestMapping("/getCalendar")
	public @ResponseBody List<ResCalendarVO> getCalendar(String date){
		return timeManageService.getCalendar(date);
	} 
}
