package com.yuminsoft.ams.system.service.pms;

import java.util.List;

import com.ymkj.base.core.biz.api.message.PageResponse;
import com.ymkj.pms.biz.api.enums.OrganizationEnum;
import com.ymkj.pms.biz.api.enums.RoleEnum;
import com.ymkj.pms.biz.api.vo.request.ReqLevelVO;
import com.ymkj.pms.biz.api.vo.request.ReqParamVO;
import com.ymkj.pms.biz.api.vo.request.ReqRoleVO;
import com.ymkj.pms.biz.api.vo.response.ResCalendarVO;
import com.ymkj.pms.biz.api.vo.response.ResEmpOrgVO;
import com.ymkj.pms.biz.api.vo.response.ResEmpRoleOrgVO;
import com.ymkj.pms.biz.api.vo.response.ResEmployeeVO;
import com.ymkj.pms.biz.api.vo.response.ResGroupVO;
import com.ymkj.pms.biz.api.vo.response.ResOrganizationInfo;
import com.ymkj.pms.biz.api.vo.response.ResOrganizationTreeVO;
import com.ymkj.pms.biz.api.vo.response.ResOrganizationVO;
import com.ymkj.pms.biz.api.vo.response.ResRoleVO;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.vo.apply.TaskNumber;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;

public interface PmsApiService {

	/**
	 * 返回审批组信息
	 * 
	 * @author dmz
	 * @date 2017年3月21日
	 * @return
	 */
	public List<ResOrganizationTreeVO> findOrgTreeByOrgCodes();

	/**
	 * 根据机构id查询员工
	 * 
	 * @author dmz
	 * @date 2017年3月21日
	 * @param orgId
	 * @return
	 */
	public List<ResEmployeeVO> findEmpsByOrgId(Long orgId);

	/**
	 * 根据机构编码,名字查询指定机构id下的子机构
	 * 
	 * @param orgId
	 * @param orgCode
	 * @param orgName
	 * @return
	 * @author YM10168
	 * @date 2017年3月30日 上午11:40:56
	 */
	public List<ResOrganizationVO> findOrgByCodeOrName(Long orgId, String orgCode, String orgName);

	/**
	 * 根据工号和名字查询指定机构ID下的人员
	 * 
	 * @param orgId
	 * @param userCode
	 * @param userName
	 * @return
	 * @author YM10168
	 * @date 2017年3月30日 下午1:30:28
	 */
	public List<ResEmployeeVO> findEmpByCodeOrName(Long orgId, String userCode, String userName, String orgTypeCode);

	/**
	 * 根据 工号和角色编码获取上级(默认取第一个)
	 * 
	 * @author dmz
	 * @date 2017年4月10日
	 * @param vo
	 * @return
	 */
//	public ResEmployeeVO findLeaderByAccount(ReqParamVO vo);

	/**
	 * 获取特定角色的领导(包含组长/主管/经理)
	 * @param userCode		用户工号
	 * @param leaderRole    角色
	 * @param orgCode
	 * @author wulinjie
	 * @return
	 */
	public String findLeaderByUserCodeAndRole(String userCode, RoleEnum leaderRole, OrganizationEnum.OrgCode orgCode);

	/**
	 * 查询所有营业部
	 * 
	 * @author dmz
	 * @date 2017年4月6日
	 * @return
	 */
	public List<ResOrganizationVO> findAllDepts();

	/**
	 * 根据工号获取所在机构及其子机构的成员
	 * 
	 * @author dmz
	 * @date 2017年4月6日
	 * @param usercode
	 * @return
	 */
//	public List<ResEmployeeVO> findEmpByUsercode(String usercode);

	/***
	 * 根据当前用户获取大组
	 * @author dmz
	 * @date 2017年4月8日
	 * @return
	 */
//	public List<ResOrganizationVO> findBigGroupByAccount();

	/**
	 * 根据机构ID，获取机构信息
	 * @param orgId
	 * @return
	 */
	public ResOrganizationVO findOrgById(Long orgId);

	/**
	 * 是否属于某个机构类型
	 * @param orgId
	 * @param orgCode
	 * @return
	 */
	public boolean hasOrgType(Long orgId, OrganizationEnum.OrgCode... orgCode);

	/**
	 * 根据登录用户和大组id获取小组
	 * 
	 * @author dmz
	 * @date 2017年4月8日
	 * @param orgId
	 * @return
	 */
//	public List<ResOrganizationVO> findTeamByAccountAndOrgId(Long orgId);

	/**
	 * 根据员工号和机构类型(初审终审质检)获取小组
	 * @author dmz
	 * @param userCode
	 * @param orgType
	 * @return
	 */
//	List<ResOrganizationVO> findSmallGroupByAccount(String userCode, String orgType);

	/**
	 * 根据用户查询所在的大组小组(用于批量改派根据用户判断是否可编辑大组小组)
	 * 
	 * @author dmz
	 * @date 2017年4月12日
	 * @return
	 */
	public Result<ResGroupVO> findGroupInfoByAccount(ReqParamVO reqParamVO);

	/**
	 * 根据机构代码获取组织结构下指定角色的员工
	 * 
	 * @author fhx
	 * @date 2017年4月12日
	 * @return
	 */
	public List<ResEmployeeVO> findEmpByOrgCode(ReqParamVO reqParamVO);

	/**
	 * 根据工号查询员工信息
	 *
	 * @param userCode
	 * @return
	 */
	public ResEmployeeVO findEmpByUserCode(String userCode);

	/**
	 * 获取所有组长
	 * 1.经理以上角色获取所有初审和直通车组长,初审经理
	 * 2.初审经理、主管、组长或者初审机构下所有组长
	 * 3.直通车经理、主管、组长获取直通车机构下所有组长
	 * @author JiaCX
	 * @date 2017年5月6日 上午9:42:16
	 */
	public List<ResEmployeeVO> getAllFirstApprovalLeaders();

	/**
	 * @Desc: 根据机构代码获取机构ID
	 * @Author: phb
	 * @Date: 2017/4/25 17:59
	 */
	public Long findOrgIdByOrgTypeCode(String code);

	/**
	 * @Desc: 根据机构代码获取组织结构下指定角色的员工
	 * @Author: phb
	 * @Date: 2017/5/2 16:39
	 * @param typeStatus-默认获取所有,0:表示有效用户,1:表示禁用用户
	 */
	public List<ResEmployeeVO> findByOrgTypeCodeAndRole(List<String> roleCodes, String orgTypeCode, int typeStatus);

	/*
	 * add by zw at 2017-05-09 获取所有机构区域
	 *
	 * @return
	 */
	public List<ResOrganizationVO> findAllOrgAreas();

	/**
	 * @Desc: 根据工号查询员工基本信息
	 * @Author: phb
	 * @Date: 2017/5/9 15:19
	 */
	public ResEmployeeVO findByAccount(String account);

	/**
	 * @Desc: 根据工号和机构代码查询当前登录用户下级
	 * @param account 工号
	 * @param orgCode 机构类型
	 * @Author: phb
	 * @Date: 2017/5/10 9:59
	 */
	public List<String> findLowerStaffByAccountAndOrgTypeCode(String account, OrganizationEnum.OrgCode orgCode);

	/**
	 * add by zw at 2017-05-10
	 *
	 * @param reqParamVO
	 * @return
	 */
//	public Result<ResGroupVO> findGroupInfoByAccountNotRight(ReqParamVO reqParamVO);

	/**
	 * @Desc: 查询用户拥有的角色
	 * @Author: phb
	 * @Date: 2017/5/13 21:55
	 */
	public List<ResRoleVO> findRolesByAccount(String loginUser);

	/**
	 * 获取用户的角色编码
	 * @author wulj
	 * @param account
	 * @return
	 */
	public List<String> findRoleCodeListByAccount(String account);

	/**
	 * @Desc: 判断用户是否有指定角色(true：拥有 false：没有)
	 * @Author: phb
	 * @Date: 2017/5/19 10:34
	 */
	public Boolean hasRole(String loginUser, String roleCode);

	/**
	 * 判断用户是否含有指定角色列表(含有任意一个角色，则true，否则false)
	 * @param account
	 * @param roleCodeList
	 * @return
	 */
	public Boolean hasAnyRoles(String account, List<String> roleCodeList);

	/**
	 * 根据工号和角色编码查询下级
	 * 
	 * @param vo
	 * @return
	 * @author JiaCX
	 * @date 2017年6月2日 上午10:50:02
	 */
//	public List<ResEmpOrgVO> findLowerEmpByAccount(ReqParamVO vo);

	/**
	 * 根据小组编码查询下面的组员区分终审和初审(目前用于改派)
	 * 
	 * @author dmz
	 * @date 2017年6月12日
	 * @param reqParamVO
	 * @return
	 */
//	public List<ResEmployeeVO> findByOrgIdAndRoleCode(ReqParamVO reqParamVO);

	/**
	 * 根据小组机构id查询下属有初审权限的人员
	 * 
	 * @param orgId
	 * @return
	 * @author JiaCX
	 * @date 2017年6月15日 下午6:24:13
	 */
//	public List<ResEmployeeVO> findEmpsByGroupusculeOrgId(Long orgId);

	/**
	 * 获取上级领导(质检使用)(如果经理及经理以上返回自己,如果终审有初审角色的返回终审上级)
	 * 
	 * @author dmz
	 * @date 2017年6月22日
	 * @param userCode
	 * @return
	 */
	public ResEmployeeVO findLeader(String userCode);
	
	/**
     * 判断登录用户 是否是经理以上级别
     * 
     * @param reqParamVO
     * @return
     * @author JiaCX
     * @date 2017年6月23日 下午3:48:46
     */
	public boolean isManagerAbove(ReqParamVO reqParamVO);
    
    /**
     * 根据员工工号获取组长
     * 
     * @param req
     * @return
     * @author JiaCX
     * @date 2017年8月3日 下午4:00:09
     */
//	public ResEmployeeVO findGroupLeaderByUserCode(ReqParamVO req);

    /**
     * 获取当前登录用户权限内的机构人员树
     * 
     * @return
     * @author JiaCX
     * @date 2017年9月8日 上午9:08:36
     */
//    public List<ResOrganizationTreeVO> getUserOrgTree();

	/**
	 * 根据当前用户查询或机构id或用户名(拼音查询)
	 * @param orgId-机构id
	 * @param roleCode-角色编码
	 * @param userName-用户名
	 * @return
	 */
	List<ResEmployeeVO> getUserInfoByLikeUserName(Long orgId, String roleCode, String userName);

	/**
	 * 根据工号获取小组列表及列表下员工信息(初审,如果是经理以上角色是返回所有初审组包括直通车初审)
	 * @author wangzhenxing
	 * date 2017年11月13日
	 */
	List<ResGroupVO> getGroupsByAccount();

	/**
	 * 根据工号查询当前登录用户上级,默认查找直属上级, 只查一级(查找范围为当前机构和所有父机构)
	 * @author wangzhenxing
	 * @date 2017-11-14
	 * @param reqLevelVO
	 * @return
	 */
	ResEmployeeVO getLeaderByCode(ReqLevelVO reqLevelVO);

	/**
	 * 根据工号查询当前登录用户下级
	 * @author wangzhenxing
	 * @date 2017-11-14
	 * @param reqLevelVO
	 * @return
	 */
	List<ResEmpOrgVO> getLowerEmpByAccount(ReqLevelVO reqLevelVO);

	/**
	 * 根据登录工号和角色获取所有小组
	 * @author wangzhenxing
	 * @date 2017-11-14
	 * @param orgId
	 * @return
	 */
	List<ResOrganizationVO> getSmallGroupByAccount(Long orgId);

	/***
	 * 根据当前用户获取大组
	 * @author wangzhenxing
	 * @date 2017-11-14
	 * @return
	 */
	List<ResOrganizationVO> getBigGroupByAccount();

	/**
	 * 根据机构代码列表获取机构及其机构下的人员树
	 * @author wangzhenxing
	 * @date 2017-11-14
	 * @return
	 */
	List<ResOrganizationTreeVO> getOrgTreeAndEmployees(ReqParamVO reqParamVO);

	/**
	 * 根据登录用户获取大组小组信息
	 * @author wangzhenxing
	 * @date 2017-11-14
	 * @param reqParamVO
	 * @return
	 */
	Result<ResGroupVO> getGroupInfoByAccount(ReqParamVO reqParamVO);

	/**
	 * 根据工号获取所在机构及其子机构的成员
	 * @author wangzhenxing
	 * @date 2017-11-15
	 * @param reqParamVO
	 * @return
	 */
	List<ResEmployeeVO> getEmpsByAccount(ReqParamVO reqParamVO);

    /**
     * @Desc: 根据工号和机构代码查询当前登录用户下级(只有在职的)
     * @param account 工号
     * @param orgCode 机构类型
     * @Author: phb
     * @Date: 2017/5/10 10:00
     */
    List<String> findLowerByAccountAndOrgTypeCode(String account, OrganizationEnum.OrgCode orgCode);

	/**
	 * 查询所有初审角色的初审员(用于初审改派有经理以上角色和同步初审员工)
	 * @return
	 */
	List<ResEmployeeVO> findByRoleCode();

	/**
	 * 根据用户判断是否是经理或者是经理及以上角色
	 * @param userCode-用户code
	 * @param notManager-默认判断是否有经理角色true 表示不判断
	 * @return
	 */
	boolean isManagerAbove(String userCode, boolean notManager);

	/**
	 * 根据机构id查询机构信息
	 * @param id 机构id
	 * @return
	 */
	ResOrganizationInfo findOrgInfoById(Long id);

	/**
	 * 根据机构ID查指定角色的员工
	 * @param  orgId		机构ID
	 * @param roleCodes		角色编码
	 */
	List<ResEmployeeVO> findByDeptAndRole(Long orgId, List<String> roleCodes);

	/**
	 * 获取所有员工信息(包含初审和终审)
	 * 
	 * @author Jia CX
	 * @date 2017年12月26日 上午10:44:56
	 * @notes
	 * 
	 * @return
	 */
	List<ResEmpRoleOrgVO> getAllEmps();

	/**
	 * 根据工号和角色编码查询下级(根据数据权限获取)
	 */
	List<ResEmpOrgVO> findEmpOrgByAccount(ReqParamVO reqParamVO);

	/**
	 * 获取当前登录人数据权限内，初审人员，终审人员，初审组长列表
	 * 
	 * @author Jia CX
	 * <p>2018年4月11日 上午10:27:54</p>
	 * @param tasknum 
	 * @param requestPage 
	 * @return 
	 * 
	 */
	PageResponse<ResEmpOrgVO> getEmployeesAndGroupLeaders(RequestPage requestPage, TaskNumber tasknum);
	
	/**
	 * 查询某个用户数据权限内是否有指定角色的员工(多个角色的话判断是否同时拥有)
	 * 
	 * @author Jia CX
	 * <p>2018年4月19日 下午4:25:16</p>
	 * 
	 * @param userCode
	 * @param roleCodes
	 * @return
	 */
	boolean hasStaffWhoSpecifiedRole(String userCode, String[] roleCodes);
	
	/**
	 * 查询指定日期是否是工作日
	 * 
	 * @author Jia CX
	 * @date 2017年12月12日 上午9:55:13
	 * @notes
	 * 
	 * @param date yy-MM-dd
	 * @return
	 */
	boolean isWeekday(String date);

	/**
	 * 获取当前登陆用户组织机构内的员工的角色列表
	 * 
	 * @author Jia CX
	 * <p>2018年5月16日 上午10:29:29</p>
	 * 
	 * @return
	 */
	List<ResRoleVO> getRoleByAccount();

	/**
	 * 获取日历信息
	 * 
	 * @author Jia CX
	 * <p>2018年5月21日 上午11:33:46</p>
	 * 
	 * @return
	 */
	List<ResCalendarVO> getCalendar(String day);
	
	/**
	 * 根据编码获取角色
	 * @param reqRoleVO
	 * @return
	 */
	ResRoleVO findByCode(ReqRoleVO reqRoleVO);

	/**
	 * 根据机构代码列表获取机构及其机构下的人员树(不根据数据权限)
	 * 
	 * @author Jia CX
	 * <p>2018年5月29日 下午2:26:40</p>
	 * 
	 * @param reqParamVO
	 * @return
	 */
	List<ResOrganizationTreeVO> getOrgTreeAndEmployeesWithoutDataPermission(ReqParamVO reqParamVO);
	
	/**
	 * 查找所有拥有复议角色的员工信息
	 * 
	 * @author Jia CX
	 * <p>2018年6月12日 下午5:34:38</p>
	 * 
	 * @return
	 */
	List<ResEmployeeVO> findAllReviewRoleEmp();
}
