package com.yuminsoft.ams.system.service.system;

import java.util.List;

import com.ymkj.pms.biz.api.vo.response.ResCalendarVO;
import com.ymkj.pms.biz.api.vo.response.ResRoleVO;
import com.yuminsoft.ams.system.domain.system.TimeManagement;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;
import com.yuminsoft.ams.system.vo.system.TimeManageAddParamIn;
import com.yuminsoft.ams.system.vo.system.TimeManageCheckUserParamIn;
import com.yuminsoft.ams.system.vo.system.TimeManageCheckUserParamOut;
import com.yuminsoft.ams.system.vo.system.TimeManageListParamIn;
import com.yuminsoft.ams.system.vo.system.TimeManageListParamOut;
import com.yuminsoft.ams.system.vo.system.TimeManageRemainVO;

/**
 * 限制系统使用时间接口
 * 
 * @author Jia CX
 * @date 2017年12月11日 下午2:50:59
 * @notes
 * 
 */
public interface TimeManageService {

	/**
	 * 查询角色时间限制列表
	 * 
	 * @author Jia CX
	 * @date 2017年12月11日 上午11:20:55
	 * @notes
	 * 
	 * @param paramIn
	 * @return
	 */
	ResponsePage<TimeManageListParamOut> getList(TimeManageListParamIn paramIn);

	/**
	 * 新增角色时间限制记录
	 * 
	 * @author Jia CX
	 * @date 2017年12月11日 上午11:48:22
	 * @notes
	 * 
	 * @param paramIn
	 * @return
	 */
	Result<String> add(TimeManageAddParamIn paramIn);

	/**
	 * 修改角色时间限制记录
	 * 
	 * @author Jia CX
	 * @date 2017年12月11日 上午11:55:03
	 * @notes
	 * 
	 * @param paramIn
	 * @return
	 */
	Result<String> update(TimeManageAddParamIn paramIn);

	/**
	 * 删除角色时间限制记录
	 * 
	 * @author Jia CX
	 * @date 2017年12月11日 上午11:55:13
	 * @notes
	 * 
	 * @param paramIn
	 * @return
	 */
	Result<String> delete(TimeManageAddParamIn paramIn);

	/**
	 * 校验当前登录用户是否可以登录
	 * 
	 * @author Jia CX
	 * @date 2017年12月12日 上午9:11:15
	 * @notes
	 * 
	 * @return true可以登录，false不可以登录
	 */
	boolean hasPermission();

	/**
	 * 新增初审员时间限制弹框时，获取满足条件的初审员列表
	 * 
	 * @author Jia CX
	 * <p>2018年5月15日 上午10:14:17</p>
	 * 
	 * @param paramIn
	 * @return
	 */
	List<TimeManageCheckUserParamOut> getCheckUsers(TimeManageCheckUserParamIn paramIn);

	/**
	 * 判断某条记录是否存在
	 * 
	 * @author Jia CX
	 * <p>2018年5月15日 下午1:53:37</p>
	 * 
	 * @param roleCode
	 * @param timeType
	 * @param date
	 * @param
	 * @return TimeManagement
	 */
	TimeManagement getRecord(String roleCode, String timeType, String date, String userCode);

	/**
	 * 批量删除
	 * 
	 * @author Jia CX
	 * <p>2018年5月15日 下午3:57:42</p>
	 * 
	 * @param ids
	 * @return
	 */
	Result<String> batchDelete(String[] ids);

	/**
	 * 批量修改
	 * 
	 * @author Jia CX
	 * <p>2018年5月15日 下午7:20:09</p>
	 * 
	 * @param paramIn
	 * @return
	 */
	Result<String> batchUpdate(TimeManageAddParamIn paramIn);

	/**
	 * 获取当前登陆用户组织机构内的员工的角色列表
	 * 
	 * @author Jia CX
	 * <p>2018年5月16日 上午10:28:28</p>
	 * 
	 * @return
	 */
	List<ResRoleVO> getRoleByAccount();

	/**
	 * 获取日历信息
	 * 
	 * @author Jia CX
	 * <p>2018年5月21日 上午11:32:32</p>
	 * 
	 * @return
	 */
	List<ResCalendarVO> getCalendar(String day);
	
	/**
	 * 获取登录用户今天剩余有效登录时间
	 * 
	 * @author Jia CX
	 * @date 2017年12月12日 下午3:53:57
	 * @notes
	 * 
	 * @return
	 */
	int getRemainingTime(String userCode);
	
	/**
	 * 获取登陆用户当前时间
	 * <p>1：剩余登陆时间</P>
	 * <p>2：当前时间用户以什么角色登陆的系统</P>
	 * <p>3：当前时间用户登陆的角色的剩余时间</P>
	 * 
	 * @author Jia CX
	 * <p>2018年9月28日 下午4:59:03</p>
	 * 
	 * @return
	 */
	TimeManageRemainVO getRoleAndReaminTime(String userCode);

}
