package com.yuminsoft.ams.system.service.pms.impl;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections.Transformer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.ymkj.base.core.biz.api.message.PageResponse;
import com.ymkj.base.core.biz.api.message.Response;
import com.ymkj.pms.biz.api.enums.OrganizationEnum;
import com.ymkj.pms.biz.api.enums.RoleEnum;
import com.ymkj.pms.biz.api.service.ICalendarExecuter;
import com.ymkj.pms.biz.api.service.IEmployeeExecuter;
import com.ymkj.pms.biz.api.service.IOrganizationExecuter;
import com.ymkj.pms.biz.api.service.IRoleExecuter;
import com.ymkj.pms.biz.api.service.v1.IEmployeeExecuterV1;
import com.ymkj.pms.biz.api.service.v1.IOrganizationExecuterV1;
import com.ymkj.pms.biz.api.service.v1.IRoleExecuterV1;
import com.ymkj.pms.biz.api.vo.request.ReqCalendarVO;
import com.ymkj.pms.biz.api.vo.request.ReqEmployeeVO;
import com.ymkj.pms.biz.api.vo.request.ReqLevelVO;
import com.ymkj.pms.biz.api.vo.request.ReqOrganizationVO;
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
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.common.AmsCode;
import com.yuminsoft.ams.system.common.AmsConstants;
import com.yuminsoft.ams.system.common.EnumUtils;
import com.yuminsoft.ams.system.service.BaseService;
import com.yuminsoft.ams.system.service.pms.PmsApiService;
import com.yuminsoft.ams.system.util.CollectionUtils;
import com.yuminsoft.ams.system.util.RedisUtil;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.util.Result.Type;
import com.yuminsoft.ams.system.vo.apply.TaskNumber;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;

@Service
public class PmsApiServiceImpl extends BaseService implements PmsApiService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IOrganizationExecuter organizationExecuter;

    @Autowired
    private IEmployeeExecuter employeeExecuter;

    @Autowired
    private IOrganizationExecuterV1 organizationExecuterV1;

    @Autowired
    private IEmployeeExecuterV1 iEmployeeExecuterV1;
    
    @Autowired
	private ICalendarExecuter calendarExecuter;
    
    @Autowired
    private IRoleExecuterV1 roleExecuterV1;
    
    @Autowired
    private IRoleExecuter roleExecuter;

    /**
     * 接单能力配置显示组织机构数(返回审批组信息)
     *
     * @return
     */
    @Override
    public List<ResOrganizationTreeVO> findOrgTreeByOrgCodes() throws RuntimeException {
        ReqParamVO request = new ReqParamVO();
        request.setSysCode(sysCode);
        List<String> codes = Lists.newArrayList();
        codes.add(OrganizationEnum.OrgCode.CHECK.getCode());
        codes.add(OrganizationEnum.OrgCode.FINAL_CHECK.getCode());
        codes.add(OrganizationEnum.OrgCode.SALE_CHECK.getCode());
        request.setOrgTypeCodes(codes);
        request.setLoginUser(ShiroUtils.getAccount());
        Response<List<ResOrganizationTreeVO>> response = organizationExecuterV1.findOrgTreeByOrgTypeCodes(request);
        LOGGER.info("返回审批组信息  findOrgTreeByOrgTypeCodes params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
        if (null != response && response.isSuccess()) {
            return response.getData();
        }
        return null;
    }

    /**
     * 根据机构id查询员工
     *
     * @param orgId
     * @return
     */
    @Override
    public List<ResEmployeeVO> findEmpsByOrgId(Long orgId) {
        List<ResEmployeeVO> list = new ArrayList<ResEmployeeVO>();
        ReqParamVO request = new ReqParamVO();
        request.setSysCode(sysCode);
        request.setOrgId(orgId);
        Response<List<ResEmployeeVO>> response = employeeExecuter.findEmpsByOrgId(request);
        LOGGER.info("根据机构id查询员工  findEmpsByOrgId params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
        if (null != request && response.isSuccess()) {
            list = response.getData();
        }
        return list;
    }

    /**
     * 根据机构编码,名字查询指定机构id下的子机构
     *
     * @param orgId
     * @param orgCode
     * @param orgName
     * @return
     */
    @Override
    public List<ResOrganizationVO> findOrgByCodeOrName(Long orgId, String orgCode, String orgName) {
        List<ResOrganizationVO> list = new ArrayList<ResOrganizationVO>();
        ReqOrganizationVO request = new ReqOrganizationVO();
        request.setSysCode(sysCode);
        request.setId(orgId);
        request.setName(orgName);
        request.setOrgCode(orgCode);
        Response<List<ResOrganizationVO>> response = organizationExecuter.findOrgByCodeOrName(request);
        LOGGER.info("根据机构id查询子机构  findOrgByCodeOrName params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
        if (null != request && response.isSuccess()) {
            list = response.getData();
        }
        return list;
    }

    /**
     * 根据工号和名字查询指定机构ID下的人员
     *TODO 让平台修改接口，返回员工角色信息
     * @param orgId
     * @param userCode
     * @param userName
     * @return
     */
    @Override
    public List<ResEmployeeVO> findEmpByCodeOrName(Long orgId, String userCode, String userName, String orgTypeCode) {
        List<ResEmployeeVO> list = new ArrayList<ResEmployeeVO>();
        ReqEmployeeVO request = new ReqEmployeeVO();
        request.setSysCode(sysCode);
        request.setOrgId(orgId);
        request.setName(userName);
        request.setUsercode(userCode);
        request.setRoleCodes(orgTypeCode);
        request.setStatus(0);
        request.setInActive("t");
        Response<List<ResEmployeeVO>> response = employeeExecuter.findEmpByCodeOrName(request);
        LOGGER.info("根据机构id查询员工  findEmpByCodeOrName params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
        if (null != request && response.isSuccess()) {
            list = response.getData();
            Collections.sort(list, new Comparator<ResEmployeeVO>() {
                @Override
                public int compare(ResEmployeeVO o1, ResEmployeeVO o2) {
                    Collator com = Collator.getInstance(java.util.Locale.CHINA);
                    return com.getCollationKey(o1.getName()).compareTo(com.getCollationKey(o2.getName()));
                }
            });
        }
        return list;
    }

    /**
     * TODO 后期可以直接用IEmployeeExecuterV1.findLeaderByAccount 替换
     * 获取特定角色的领导(包含组长/主管/经理)
     *
     * @param userCode   用户工号
     * @param leaderRole 角色
     * @return
     */
    @Override
    public String findLeaderByUserCodeAndRole(String userCode, RoleEnum leaderRole, OrganizationEnum.OrgCode orgCode) {
        // 如果当前人员是初审经理，并且查找的角色也是初审经理，则直接反馈userCode
        if (hasRole(userCode, leaderRole.getCode())) {
            return userCode;
        }
        // 查询上级领导
        ReqLevelVO reqVo = new ReqLevelVO();
        reqVo.setSysCode(sysCode);
        reqVo.setLoginUser(userCode);
        reqVo.setInActive(AmsConstants.T);
        reqVo.setStatus(AmsConstants.ZERO);
        reqVo.setLevelType(orgCode.getCode());
        ResEmployeeVO leader = getLeaderByCode(reqVo);
        String leaderUserCode = null;
        if (leader != null) {
            leaderUserCode = leader.getUsercode();
            if (leaderUserCode.equals(userCode)) {
                if (hasRole(leaderUserCode, leaderRole.getCode())) {
                    return userCode;  // 如果上级领导是自己
                }
                return null;
            }
            // 不是该角色，则继续查找上级，直到找到为止
            if (!hasRole(leaderUserCode, leaderRole.getCode())) {
                leaderUserCode = findLeaderByUserCodeAndRole(leaderUserCode, leaderRole, orgCode);
            }
        }
        return leaderUserCode;
    }

    /**
     * 查询所有营业部
     *
     * @return
     */
    @Override
    public List<ResOrganizationVO> findAllDepts() {
        List<ResOrganizationVO> list = new ArrayList<ResOrganizationVO>();
        ReqOrganizationVO request = new ReqOrganizationVO();
        request.setSysCode(sysCode);
        Response<List<ResOrganizationVO>> response = organizationExecuter.findAllDepts(request);
        LOGGER.debug("查询所有营业部 params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
        if (null != response && response.isSuccess()) {
            list = response.getData();
        }
        return list;
    }

    /**
     * 根据机构ID，获取机构信息
     *
     * @param orgId 机构ID
     * @return
     */
    @Override
    public ResOrganizationVO findOrgById(Long orgId) {
        ReqOrganizationVO reqParamVO = new ReqOrganizationVO();
        reqParamVO.setSysCode(sysCode);
        reqParamVO.setId(orgId);
        Response<ResOrganizationVO> response = organizationExecuter.findById(reqParamVO);
        LOGGER.debug("根据机构ID，获取机构信息 params:{} resulst:{}", JSON.toJSONString(reqParamVO), JSON.toJSONString(response));
        if (null != response && response.isSuccess()) {
            return response.getData();
        }
        return null;
    }

    /**
     * 是否属于某个机构类型
     *
     * @param orgId   机构ID
     * @param orgCode 机构类型
     * @return true-是，false-否
     */
    @Override
    public boolean hasOrgType(Long orgId, OrganizationEnum.OrgCode... orgCode) {
        ResOrganizationVO orgVo = findOrgById(orgId);
        boolean result = true;
        if (orgVo != null) {
            for (OrganizationEnum.OrgCode code : orgCode) {
                if (!code.getCode().equals(orgVo.getOrgCode())) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 根据用户查询所在的大组小组(用于批量改派根据用户判断是否可编辑大组小组)
     *
     * @return
     */
    @Override
    public Result<ResGroupVO> findGroupInfoByAccount(ReqParamVO reqParamVO) {
        Result<ResGroupVO> result = new Result<ResGroupVO>(Type.FAILURE);
        reqParamVO.setSysCode(sysCode);
        Response<ResGroupVO> response = organizationExecuterV1.findGroupInfoByAccount(reqParamVO);
        LOGGER.info("根据用户查询所在的大组小组(用于批量改派根据用户判断是否可编辑大组小组) params:{} resulst:{}", JSON.toJSONString(reqParamVO), JSON.toJSONString(response));
        if (null != response && response.isSuccess()) {
            result.setType(Type.SUCCESS);
            if (response.getData().isAuth()) {
                result.setData(response.getData());
            }
        } else {
            result.addMessage(response.getRepMsg());
        }
        return result;
    }

    /**
     * 根据机构代码获取组织结构下指定角色的员工
     *
     * @return
     */
    @Override
    public List<ResEmployeeVO> findEmpByOrgCode(ReqParamVO reqParamVO) {
        LOGGER.info("根据机构编码查询员工信息请求参数：{}", JSON.toJSONString(reqParamVO));
        Response<List<ResEmployeeVO>> response = employeeExecuter.findByOrgTypeCodeAndRole(reqParamVO);
        LOGGER.info("根据机构编码查询员工信息返回参数：{}", JSON.toJSONString(response));
        if (null != response && response.isSuccess()) {
            return CollectionUtils.removeDuplicateByProperty(response.getData(), "usercode");
        }
        return new ArrayList<ResEmployeeVO>();
    }

    /**
     * 根据工号查询员工信息
     *
     * @param userCode
     * @return
     */
    @Override
    public ResEmployeeVO findEmpByUserCode(String userCode) {
        ReqEmployeeVO reqEmployeeVO = new ReqEmployeeVO();
        reqEmployeeVO.setSysCode(sysCode);
        reqEmployeeVO.setUsercode(userCode);
        String employeeUserCode = "EmployeeVO-" + userCode;
        if (redisUtil.exists(employeeUserCode)) {
            return (ResEmployeeVO) redisUtil.get(employeeUserCode);
        }
        try {
            Response<ResEmployeeVO> response = employeeExecuter.findByAccount(reqEmployeeVO);
            // 判断是否查询成功
            LOGGER.info("根据员工编码查询员工信息返回结果：{}", JSON.toJSONString(response));
            if (null != response && response.isSuccess()) {
                redisUtil.set(employeeUserCode, response.getData(), 3600L);
                return response.getData();
            }
        } catch (Exception e) {
            LOGGER.info("根据员工编码查询员工信息异常:" + e.getMessage());
        }
        return null;
    }

	/**
	 * 获取所有组长
	 * 1.经理以上角色获取所有初审和直通车组长,初审经理
	 * 2.初审经理、主管、组长或者初审机构下所有组长
	 * 3.直通车经理、主管、组长获取直通车机构下所有组长
	 *
	 * @return
	 */
	@Override
	public List<ResEmployeeVO> getAllFirstApprovalLeaders() {
		ReqParamVO vo = new ReqParamVO();
		vo.setSysCode(sysCode);
		vo.setLoginUser(ShiroUtils.getCurrentUser().getUsercode());
		List<String> orgTypeCodeList = new ArrayList<String>();
		orgTypeCodeList.add(OrganizationEnum.OrgCode.CHECK.getCode());
		orgTypeCodeList.add(OrganizationEnum.OrgCode.SALE_CHECK.getCode());
		vo.setOrgTypeCodes(orgTypeCodeList);
		vo.setStatus(0);
		vo.setInActive(AmsConstants.T);
		Response<List<ResEmployeeVO>> response = iEmployeeExecuterV1.findGroupLeadersByAccount(vo);
		LOGGER.info("获取所有初审组组长返回结果 params:{} result:{}",JSON.toJSONString(vo), JSON.toJSONString(response));
		if (null != response && response.isSuccess()) {
			return response.getData();
		}
		return null;
	}

    /**
     * @Desc: 根据机构代码获取机构ID
     * @Author: phb
     * @Date: 2017/4/25 18:13
     */
    @Override
    public Long findOrgIdByOrgTypeCode(String code) {
        ReqParamVO reqParamVO = new ReqParamVO();
        reqParamVO.setSysCode(sysCode);
        reqParamVO.setOrgTypeCode(code);
        Response<Long> response = organizationExecuter.findOrgIdByOrgTypeCode(reqParamVO);
        LOGGER.info("根据机构代码获取机构ID params:{} resulst:{}", JSON.toJSONString(reqParamVO), JSON.toJSONString(response));
        if (null != response && response.isSuccess()) {
            return response.getData();
        }
        return null;
    }

    /**
     * add by zw at 2017-05-09 获取所有机构区域
     *
     * @return List
     */
    @Override
    public List<ResOrganizationVO> findAllOrgAreas() {
        List<ResOrganizationVO> list = new ArrayList<ResOrganizationVO>();
        ReqOrganizationVO request = new ReqOrganizationVO();
        request.setSysCode(sysCode);
        Response<List<ResOrganizationVO>> response = organizationExecuter.findAllOrgAreas(request);
        LOGGER.info("所有机构区域查询 params:{} resulst:{}", JSON.toJSONString(request), JSON.toJSONString(response));
        if (null != response && response.isSuccess()) {
            if (!CollectionUtils.isEmpty(response.getData())) {
                list = response.getData();
            }
        }
        return list;
    }

    /**
     * @param typeStatus-默认获取所有,0:表示有效用户,1:表示禁用用户
     */
    @Override
    public List<ResEmployeeVO> findByOrgTypeCodeAndRole(List<String> roleCodes, String orgTypeCode, int typeStatus) {
        ReqParamVO reqParamVO = new ReqParamVO();
        reqParamVO.setSysCode(sysCode);
        reqParamVO.setOrgTypeCode(orgTypeCode);
        reqParamVO.setRoleCodes(roleCodes);
        reqParamVO.setStatus(typeStatus);
        Response<List<ResEmployeeVO>> response = employeeExecuter.findByOrgTypeCodeAndRole(reqParamVO);
        LOGGER.info("根据机构代码获取组织结构下指定角色的员工 params:{} resulst:{}", JSON.toJSONString(reqParamVO), JSON.toJSONString(response));
        if (null != response && response.isSuccess()) {
            return CollectionUtils.removeDuplicateByProperty(response.getData(), "usercode");
        }
        return null;
    }

    /**
     * @Desc: 查询用户拥有的角色
     * @Author: phb
     * @Date: 2017/5/13 21:56
     */
    @Override
    public List<ResRoleVO> findRolesByAccount(String loginUser) {
        ReqEmployeeVO reqEmployeeVO = new ReqEmployeeVO();
        reqEmployeeVO.setSysCode(sysCode);
        reqEmployeeVO.setUsercode(loginUser);
        Response<List<ResRoleVO>> response = employeeExecuter.findRolesByAccount(reqEmployeeVO);
        LOGGER.info("查询用户拥有的角色列表 params:{} resulst:{}", JSON.toJSONString(reqEmployeeVO), JSON.toJSONString(response));
        if (null != response && EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(response.getRepCode())) {
            return response.getData();
        }
        return null;
    }

    /**
     * 获取用户的角色编码
     *
     * @param account
     * @return
     */
    @Override
    public List<String> findRoleCodeListByAccount(String account) {
        List<ResRoleVO> roleVoList = findRolesByAccount(account);
        return (List<String>) CollectionUtils.collect(roleVoList, new Transformer() {
            @Override
            public Object transform(Object o) {
                return ((ResRoleVO) o).getCode();
            }
        });
    }

    /**
     * 判断用户是否有指定角色(true-拥有 false-没有)
     *
     * @param loginUser 用户工号
     * @param roleCode  角色编号
     */
    @Override
    public Boolean hasRole(String loginUser, String roleCode) {
        ReqParamVO reqParamVO = new ReqParamVO();
        reqParamVO.setLoginUser(loginUser);
        reqParamVO.setRoleCode(roleCode);
        reqParamVO.setSysCode(sysCode);
        Response<Boolean> response = employeeExecuter.hasRole(reqParamVO);
        LOGGER.info("判断用户是否有指定角色 params:{} resulst:{}", JSON.toJSONString(reqParamVO), JSON.toJSONString(response));
        if (null != response && response.isSuccess()) {
            return response.getData();
        }
        return null;
    }

    /**
     * 判断用户是否含有指定角色列表(含有任意一个角色，则true，否则false)
     *
     * @param account
     * @param roleCodeList
     * @return
     */
    @Override
    public Boolean hasAnyRoles(String account, List<String> roleCodeList) {
        List<String> userRoleCodeList = findRoleCodeListByAccount(account);    // 获取用户所有角色编码
        if (!CollectionUtils.isEmpty(userRoleCodeList) && !CollectionUtils.isEmpty(roleCodeList)) {
            // 判断是否有交集
            List<String> intersection = (List<String>) CollectionUtils.intersection(userRoleCodeList, roleCodeList);
            if (!CollectionUtils.isEmpty(intersection)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @Desc: 根据工号查询员工基本信息
     * @Author: phb
     * @Date: 2017/5/9 15:20
     */
    @Override
    public ResEmployeeVO findByAccount(String account) {
        ReqEmployeeVO reqEmployeeVO = new ReqEmployeeVO();
        reqEmployeeVO.setSysCode(sysCode);
        reqEmployeeVO.setUsercode(account);
        Response<ResEmployeeVO> response = employeeExecuter.findByAccount(reqEmployeeVO);
        LOGGER.info("根据机构代码获取组织结构下指定角色的员工 params:{} resulst:{}", JSON.toJSONString(reqEmployeeVO), JSON.toJSONString(response));
        if (null != response && response.isSuccess()) {
            return response.getData();
        }
        return null;
    }

    /**
     * @param account 工号
     * @param orgCode 机构类型
     */
    @Override
    public List<String> findLowerStaffByAccountAndOrgTypeCode(String account, OrganizationEnum.OrgCode orgCode) {
        List<String> userCodes = new ArrayList<>();
        ReqParamVO reqParamVO = new ReqParamVO();
        reqParamVO.setSysCode(sysCode);
        reqParamVO.setLoginUser(account);
        reqParamVO.setPage(false);
        reqParamVO.setOrgTypeCode(orgCode.getCode());
        Response<List<ResEmployeeVO>> response = employeeExecuter.findLower(reqParamVO);
        LOGGER.info("根据工号和机构代码查询当前登录用户下级 params:{} resulst:{}", JSON.toJSONString(reqParamVO), JSON.toJSONString(response));
        if (null != response && response.isSuccess()) {
            List<ResEmployeeVO> data = response.getData();
            if (!CollectionUtils.isEmpty(data)) {
                for (ResEmployeeVO resEmpOrgVO : data) {
                    userCodes.add(resEmpOrgVO.getUsercode());
                }
            }
        }
        return userCodes;
    }

    /**
     * @param account 工号
     * @param orgCode 机构类型
     */
    @Override
    public List<String> findLowerByAccountAndOrgTypeCode(String account, OrganizationEnum.OrgCode orgCode) {
        List<String> userCodes = new ArrayList<>();
        ReqParamVO reqParamVO = new ReqParamVO();
        reqParamVO.setSysCode(sysCode);
        reqParamVO.setLoginUser(account);
        reqParamVO.setPage(false);
        reqParamVO.setOrgTypeCode(orgCode.getCode());
        reqParamVO.setInActive("t");
        Response<List<ResEmployeeVO>> response = employeeExecuter.findLower(reqParamVO);
        LOGGER.info("根据工号和机构代码查询当前登录用户下级 params:{} resulst:{}", JSON.toJSONString(reqParamVO), JSON.toJSONString(response));
        if (null != response && response.isSuccess()) {
            List<ResEmployeeVO> data = response.getData();
            if (!CollectionUtils.isEmpty(data)) {
                for (ResEmployeeVO resEmpOrgVO : data) {
                    userCodes.add(resEmpOrgVO.getUsercode());
                }
            }
        }
        return userCodes;
    }

    /**
     * 获取上级领导(质检使用)(如果经理及经理以上返回自己,如果终审有初审角色的返回终审上级)
     *
     * @param userCode
     * @return
     */
    @Override
    public ResEmployeeVO findLeader(String userCode) {
        ResEmployeeVO revo = null;
        ReqParamVO request = new ReqParamVO();
        request.setSysCode(sysCode);
        request.setLoginUser(userCode);
        request.setStatus(0);// 默认全部;0:优先用户;1:禁用用户
        Response<List<ResEmployeeVO>> response = employeeExecuter.findLeader(request);
        LOGGER.info("获取上级领导(质检使用) params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
        if (null != response && response.isSuccess()) {
            if (!CollectionUtils.isEmpty(response.getData())) {
                revo = response.getData().get(0);
            }
        }
        return revo;
    }

    @Override
    public boolean isManagerAbove(ReqParamVO reqParamVO) {
        Response<Boolean> response = employeeExecuter.isManagerAbove(reqParamVO);
        LOGGER.info("判断登录用户 是否是经理上级别 params:{} result:{}", JSON.toJSONString(reqParamVO), JSON.toJSONString(response));
        if (null != response && AmsCode.RESULT_SUCCESS.equals(response.getRepCode())) {
            return response.getData();
        }
        return false;
    }

    /**
     * 根据当前用户查询或机构id或用户名(拼音查询)
     *
     * @param orgId-机构id
     * @param roleCode-角色编码
     * @param userName-用户名
     * @return
     */
    @Override
    public List<ResEmployeeVO> getUserInfoByLikeUserName(Long orgId, String roleCode, String userName) {
        List<ResEmployeeVO> list = new ArrayList<ResEmployeeVO>();
        ReqParamVO reqParamVO = new ReqParamVO();
        reqParamVO.setSysCode(sysCode);
        reqParamVO.setLoginUser(ShiroUtils.getAccount());
        reqParamVO.setOrgId(orgId);
        reqParamVO.setRoleCode(roleCode);
        reqParamVO.setUserName(userName);
        reqParamVO.setStatus(0); //默认全部0:可以用;1: 禁用
        reqParamVO.setInActive("t");//默认全部t:在职;f:不在职
        Response<List<ResEmployeeVO>> response = iEmployeeExecuterV1.findEmpsByAccount(reqParamVO);
        LOGGER.info("根据大组或小组或用户(拼音)查询用户 params:{} result:{}", JSON.toJSONString(reqParamVO), JSON.toJSONString(response));
        if (null != response && response.isSuccess()) {
            list = response.getData();
        }
        return list;
    }


    /**
     * 根据工号获取小组列表及列表下员工信息(初审,如果是经理以上角色是返回所有初审组包括直通车初审)
     */
    @Override
    public List<ResGroupVO> getGroupsByAccount() {
        ReqParamVO reqParamVO = new ReqParamVO();
        reqParamVO.setSysCode(sysCode);
        reqParamVO.setLoginUser(ShiroUtils.getAccount());
        reqParamVO.setStatus(AmsConstants.ZERO); //可用
        reqParamVO.setInActive(AmsConstants.T);    //在职
        Response<List<ResGroupVO>> response = organizationExecuterV1.findGroupsByAccount(reqParamVO);
        LOGGER.info("返回借平台机构ID查找所有员工 params:{} result:{}", JSON.toJSONString(reqParamVO), JSON.toJSONString(response));
        List<ResGroupVO> list = Lists.newArrayList();
        if (null != response && AmsCode.RESULT_SUCCESS.equals(response.getRepCode())) {
            list = response.getData();
        }
        return list;
    }


    /**
     * 根据工号查询当前登录用户上级,默认查找直属上级, 只查一级(查找范围为当前机构和所有父机构)
     *
     * @param reqLevelVO
     * @return
     */
    @Override
    public ResEmployeeVO getLeaderByCode(ReqLevelVO reqLevelVO) {
        reqLevelVO.setSysCode(sysCode);
        Response<List<ResEmployeeVO>> response = iEmployeeExecuterV1.findLeaderByAccount(reqLevelVO);
        LOGGER.info("根据工号查询当前登录用户上级 params:{} result:{}", JSON.toJSONString(reqLevelVO), JSON.toJSONString(response));
        if (null != response && response.isSuccess() && !CollectionUtils.isEmpty(response.getData())) {
            return response.getData().get(0);
        }
        return null;
    }

    /**
     * 根据工号和角色编码查询下级
     */
    @Override
    public List<ResEmpOrgVO> getLowerEmpByAccount(ReqLevelVO reqLevelVO) {
        reqLevelVO.setSysCode(sysCode);
        if (StringUtils.isEmpty(reqLevelVO.getLoginUser())) {
            reqLevelVO.setLoginUser(ShiroUtils.getAccount());
        }
        reqLevelVO.setStatus(AmsConstants.ZERO);
        reqLevelVO.setInActive(AmsConstants.T);
        reqLevelVO.setAllLevel(true);
        Response<List<ResEmpOrgVO>> response = iEmployeeExecuterV1.findLowerByAccount(reqLevelVO);
        LOGGER.info("根据工号和角色编码查询下级,返回平台查询 params:{} result:{}", JSON.toJSONString(reqLevelVO), JSON.toJSONString(response));
        if (null != response && response.isSuccess() && !CollectionUtils.isEmpty(response.getData())) {
            List<ResEmpOrgVO> returnlist = response.getData();
            Collections.sort(returnlist, new Comparator<ResEmpOrgVO>() {
                @Override
                public int compare(ResEmpOrgVO o1, ResEmpOrgVO o2) {
                    Collator com = Collator.getInstance(java.util.Locale.CHINA);
                    return com.getCollationKey(o1.getName()).compareTo(com.getCollationKey(o2.getName()));
                }
            });
            return returnlist;

        } else {
            return null;
        }
    }

    /**
     * 根据登录工号和角色获取所有小组(只查初审)
     * @param orgId
     * @return
     */
    @Override
    public List<ResOrganizationVO> getSmallGroupByAccount(Long orgId) {
        List<ResOrganizationVO> list = new ArrayList<ResOrganizationVO>();
        ReqParamVO reqParamVO = new ReqParamVO();
        reqParamVO.setOrgId(orgId);
        reqParamVO.setSysCode(sysCode);
        reqParamVO.setLoginUser(ShiroUtils.getAccount());
        List<String> orgCodeList = new ArrayList<String>();
        orgCodeList.add(OrganizationEnum.OrgCode.CHECK.getCode());
        orgCodeList.add(OrganizationEnum.OrgCode.SALE_CHECK.getCode());
        reqParamVO.setOrgTypeCodes(orgCodeList);
        reqParamVO.setRoleCode(RoleEnum.CHECK.getCode());
        Response<List<ResOrganizationVO>> response = organizationExecuterV1.findSmallGroupByAccount(reqParamVO);
        LOGGER.info("根据登录工号和角色获取所有小组(只查初审),返回平台查询 params:{} result:{}", JSON.toJSONString(reqParamVO), JSON.toJSONString(response));
        if (null != response && response.isSuccess() && !CollectionUtils.isEmpty(response.getData())) {
           list = response.getData();
        }
        return list;
    }

    /**
     * 根据登录用户和角色获取所有大组
     *
     * @return
     */
    @Override
    public List<ResOrganizationVO> getBigGroupByAccount() {
        List<ResOrganizationVO> list = new ArrayList<ResOrganizationVO>();
        ReqParamVO reqParamVO = new ReqParamVO();
        reqParamVO.setSysCode(sysCode);
        reqParamVO.setLoginUser(ShiroUtils.getAccount());
        List<String> orgList = new ArrayList<String>();
        orgList.add(OrganizationEnum.OrgCode.CHECK.getCode());
        orgList.add(OrganizationEnum.OrgCode.SALE_CHECK.getCode());
        reqParamVO.setOrgTypeCodes(orgList);
        reqParamVO.setRoleCode(RoleEnum.CHECK.getCode());
        Response<List<ResOrganizationVO>> response = organizationExecuterV1.findBigGroupByAccount(reqParamVO);
        LOGGER.info("根据登录用户和角色获取所有大组,返回平台查询 params:{} result:{}", JSON.toJSONString(reqParamVO), JSON.toJSONString(response));
        if (null != response && response.isSuccess() && !CollectionUtils.isEmpty(response.getData())) {
            list = response.getData();
        }
        return list;
    }

    /**
     * 根据机构代码列表获取机构及其机构下的人员树
     *
     * @return
     */
    @Override
    public List<ResOrganizationTreeVO> getOrgTreeAndEmployees(ReqParamVO reqParamVO) {
        reqParamVO.setSysCode(sysCode);
        Response<List<ResOrganizationTreeVO>> response = organizationExecuterV1.findOrgTreeAndEmployees(reqParamVO);
        LOGGER.info("根据机构代码列表获取机构及其机构下的人员树,返回平台查询 params:{} result:{}", JSON.toJSONString(reqParamVO), JSON.toJSONString(response));
        if (null != response && response.isSuccess() && !CollectionUtils.isEmpty(response.getData())) {
            return response.getData();
        }
        return null;
    }


    /**
     * 根据登录用户获取大组小组信息
     *
     * @param reqParamVO
     * @return
     */
    @Override
    public Result<ResGroupVO> getGroupInfoByAccount(ReqParamVO reqParamVO) {
        Result<ResGroupVO> result = new Result<ResGroupVO>(Type.FAILURE);
        reqParamVO.setSysCode(sysCode);
        Response<ResGroupVO> response = organizationExecuterV1.findGroupInfoByAccount(reqParamVO);
        LOGGER.info("根据用户查询所在的大组小组,返回平台查询 params:{} result:{}", JSON.toJSONString(reqParamVO), JSON.toJSONString(response));
        if (null != response && response.isSuccess()) {
            result.setType(Type.SUCCESS);
            result.setData(response.getData());
            return result;
        }
        return null;

    }

    /**
     * 根据工号获取所在机构及其子机构的成员
     *
     * @param reqParamVO
     * @return
     */
    @Override
    public List<ResEmployeeVO> getEmpsByAccount(ReqParamVO reqParamVO) {
        reqParamVO.setSysCode(sysCode);
        Response<List<ResEmployeeVO>> response = iEmployeeExecuterV1.findEmpsByAccount(reqParamVO);
        LOGGER.info("根据工号获取所在机构及其子机构的成员,返回平台查询 params:{} result:{}", JSON.toJSONString(reqParamVO), JSON.toJSONString(response));
        if (null != response && response.isSuccess()) {
            return response.getData();
        }
        return null;
    }

    /**
     * 查询所有初审角色的初审员(用于初审改派有经理以上角色和同步初审员工)
     *
     * @return
     */
    @Override
    public List<ResEmployeeVO> findByRoleCode() {
        List<ResEmployeeVO> staffList = new ArrayList<ResEmployeeVO>();
        ReqParamVO reqParamVO = new ReqParamVO();
        reqParamVO.setSysCode(sysCode);
        reqParamVO.setRoleCode(RoleEnum.CHECK.getCode());
        reqParamVO.setStatus(AmsConstants.ZERO);
        reqParamVO.setInActive(AmsConstants.T);
        Response<List<ResEmployeeVO>> response = employeeExecuter.findByRoleCode(reqParamVO);
        LOGGER.debug("查询所有初审角色的初审员(用于初审改派有经理及经理以上角色和同步初审员工) params:{} result:{}", JSON.toJSONString(reqParamVO), JSON.toJSONString(response));
        if (null != response && response.isSuccess()) {
            if (!CollectionUtils.isEmpty(response.getData())) {
                staffList = response.getData();
            }
        }
        return staffList;
    }

    /**
     * 根据用户判断是否是经理或者是经理及以上角色
     *
     * @param userCode-用户code
     * @param notManager-默认判断是否有经理角色true 表示不判断
     * @return
     */
    @Override
    public boolean isManagerAbove(String userCode, boolean notManager) {
        ReqParamVO reqParamVO = new ReqParamVO();
        reqParamVO.setLoginUser(userCode);
        reqParamVO.setNotManager(notManager);
        reqParamVO.setSysCode(sysCode);
        Response<Boolean> response = employeeExecuter.isManagerAbove(reqParamVO);
        LOGGER.info("根据用户判断是否是经理或者是经理及以上角色 params:{} result:{}", JSON.toJSONString(reqParamVO), JSON.toJSONString(response));
        if (null != response && response.isSuccess()) {
            return response.getData();
        }
        return false;
    }

    @Override
    public ResOrganizationInfo findOrgInfoById(Long id) {
        ReqOrganizationVO reqParamVO = new ReqOrganizationVO();
        reqParamVO.setSysCode(sysCode);
        reqParamVO.setId(id);
        Response<ResOrganizationInfo> response = organizationExecuter.getOrgInfoByID(reqParamVO);
        LOGGER.debug("根据机构ID，获取机构信息 params:{} resulst:{}", JSON.toJSONString(reqParamVO), JSON.toJSONString(response));
        if (null != response && response.isSuccess()) {
            return response.getData();
        }
        return null;
    }

    @Override
    public List<ResEmployeeVO> findByDeptAndRole(Long orgId, List<String> roleCodes) {
        ReqParamVO request = new ReqParamVO();
        request.setSysCode(sysCode);
        request.setOrgId(orgId);
        request.setRoleCodes(roleCodes);
        Response<List<ResEmployeeVO>> response = employeeExecuter.findByDeptAndRole(request);
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("根据机构ID和角色，查询员工信息 request:{}, response:{}", JSONObject.toJSONString(request), JSONObject.toJSONString(response));
        }

        if(response.isSuccess()){
            return response.getData();
        }

        return null;
    }

	@Override
	public List<ResEmpRoleOrgVO> getAllEmps() {
        ReqParamVO reqParamVO = new ReqParamVO();
        reqParamVO.setSysCode(sysCode);
        String[] roleCodes = {RoleEnum.CHECK.getCode(),RoleEnum.FINAL_CHECK.getCode(),RoleEnum.FINAL_CHECK_L1.getCode(),RoleEnum.FINAL_CHECK_L2.getCode(),RoleEnum.FINAL_CHECK_L3.getCode(),RoleEnum.FINAL_CHECK_L4.getCode()};
        reqParamVO.setRoleCodes(Arrays.asList(roleCodes));
        reqParamVO.setInActive(AmsConstants.T);
        Response<List<ResEmpRoleOrgVO>> response = employeeExecuter.findByRoleCodes(reqParamVO);
        LOGGER.info("查询所有在职的初审员或者终审员 params:{} result:{}", JSON.toJSONString(reqParamVO), JSON.toJSONString(response));
        if (null != response && response.isSuccess()) {
            return response.getData();
        }
        return null;
    }


    /**
     * 根据工号和角色编码查询下级(根据数据权限获取)
     */
    @Override
    public List<ResEmpOrgVO> findEmpOrgByAccount(ReqParamVO reqParamVO) {
        reqParamVO.setSysCode(sysCode);
        reqParamVO.setStatus(AmsConstants.ZERO);
        reqParamVO.setInActive(AmsConstants.T);
        Response<List<ResEmpOrgVO>> response = iEmployeeExecuterV1.findEmpOrgByAccount(reqParamVO);
        LOGGER.info("根据工号和角色编码查询数据权限下级人员,返回平台查询 params:{} result:{}", JSON.toJSONString(reqParamVO), JSON.toJSONString(response));
        if (null != response && response.isSuccess() && !CollectionUtils.isEmpty(response.getData())) {
            List<ResEmpOrgVO> returnlist = response.getData();
            Collections.sort(returnlist, new Comparator<ResEmpOrgVO>() {
                @Override
                public int compare(ResEmpOrgVO o1, ResEmpOrgVO o2) {
                    Collator com = Collator.getInstance(java.util.Locale.CHINA);
                    return com.getCollationKey(o1.getName()).compareTo(com.getCollationKey(o2.getName()));
                }
            });
            return returnlist;

        } else {
            return null;
        }
    }

    public PageResponse<ResEmpOrgVO> getEmployeesAndGroupLeaders(RequestPage requestPage, TaskNumber tasknum) {
		ReqParamVO req = new ReqParamVO();
		req.setSysCode(sysCode);
		req.setLoginUser(ShiroUtils.getCurrentUser().getUsercode());
		
		// 判断要查询的角色列表
		List<String> rolecodes = new ArrayList<String>();
		if(StringUtils.isNotEmpty(tasknum.getTaskDefId())) {
			rolecodes.add(tasknum.getTaskDefId());
		} else {
			if (hasStaffWhoSpecifiedRole(ShiroUtils.getAccount(), new String[]{RoleEnum.CHECK_GROUP_LEADER.getCode()})) {
				LOGGER.info("用户[{}]数据权限内有[{}]角色的员工", ShiroUtils.getAccount(), RoleEnum.CHECK_GROUP_LEADER.getCode());
				rolecodes.add(RoleEnum.CHECK_GROUP_LEADER.getCode());// 如果数据权限内有初审组长角色
			}
		    if(hasStaffWhoSpecifiedRole(ShiroUtils.getAccount(), new String[]{RoleEnum.CHECK.getCode()})) {
		    	LOGGER.info("用户[{}]数据权限内有[{}]角色的员工", ShiroUtils.getAccount(), RoleEnum.CHECK.getCode());
		    	rolecodes.add(RoleEnum.CHECK.getCode());//如果数据权限内有初审角色
		    }
		    if(hasStaffWhoSpecifiedRole(ShiroUtils.getAccount(), new String[]{RoleEnum.FINAL_CHECK.getCode()})) {
		    	LOGGER.info("用户[{}]数据权限内有[{}]角色的员工", ShiroUtils.getAccount(), RoleEnum.FINAL_CHECK.getCode());
		    	rolecodes.add(RoleEnum.FINAL_CHECK.getCode());//如果数据权限内有终审角色
		    }
		}
		req.setRoleCodes(rolecodes);
		
		req.setUsercode(StringUtils.isEmpty(tasknum.getStaffCode()) ? null : tasknum.getStaffCode());
		req.setUserName(StringUtils.isEmpty(tasknum.getStaffName()) ? null : tasknum.getStaffName());
		req.setPage(true);
		req.setPageNum(requestPage.getPage());
		req.setPageSize(requestPage.getRows());
		req.setInActive("t");
		//处理排序参数
		if (StringUtils.isNotEmpty(requestPage.getSort())){
			StringBuilder sb = new StringBuilder("");
			String[] sort = requestPage.getSort().split(",");
			String[] order = requestPage.getOrder().split(",");
			for (int i = 0; i < sort.length; i++) {
				String s = "";
				if("taskDefId".equals(sort[i])) {
					s = "CONVERT(pr.name USING gbk)";
				}else if("parentOrgName".equals(sort[i])) {
					s = "CONVERT(o2.name USING gbk)";
				}else if("orgName".equals(sort[i])) {
					s = "CONVERT(o1.name USING gbk)";
				}
				sb.append(s + " " + order[i] + ",");
			}
			req.setSortType(sb.toString().substring(0, sb.toString().length()-1));
		}
		
		PageResponse<ResEmpOrgVO> res = iEmployeeExecuterV1.findEmpOrgsPageByAccountAndRoles(req);
		LOGGER.info("获取当前登录人数据权限内，初审人员，终审人员，初审组长列表,返回平台查询 params:{} result:{}", JSON.toJSONString(req), JSON.toJSONString(res));
		if(res.isSuccess()) {
			return res;
		}
		return new PageResponse<ResEmpOrgVO>();
	}

	/* (non-Javadoc)
	 * @see com.yuminsoft.ams.system.service.pms.PmsApiService#hasStaffWhoSpecifiedRole(java.lang.String, java.util.List)
	 */
	@Override
	public boolean hasStaffWhoSpecifiedRole(String userCode, String[] roleCodes) {
		ReqParamVO req = new ReqParamVO();
		req.setLoginUser(userCode);
		req.setRoleCodes(Arrays.asList(roleCodes));
		req.setSysCode(sysCode);
		Response<List<ResEmployeeVO>> res = iEmployeeExecuterV1.findEmpsByAccountRolesInDataOrgId(req);
		if(res.isSuccess() && !CollectionUtils.isEmpty(res.getData())) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isWeekday(String date) {
		ReqCalendarVO vo = new ReqCalendarVO();
		vo.setSysCode(sysCode);
		vo.setBizType("ams");
		vo.setSomeDay(date);
		Response<Boolean> response = calendarExecuter.isWorkday(vo);
		LOGGER.info("查询指定日期是否是工作日 params:{} result:{}", JSON.toJSONString(vo), JSON.toJSONString(response));
		if(null != response && response.isSuccess()) {
			return response.getData();
		}
		return false;
	}

	@Override
	public List<ResRoleVO> getRoleByAccount() {
		ReqParamVO vo = new ReqParamVO();
		vo.setSysCode(sysCode);
		vo.setLoginUser(ShiroUtils.getAccount());
		Response<List<ResRoleVO>> res = roleExecuterV1.findRoleByAccount(vo);
		LOGGER.info("获取当前登陆用户组织机构内的员工的角色列表 params:{} result:{}", JSON.toJSONString(vo), JSON.toJSONString(res));
		if(null != res && res.isSuccess()) {
			return res.getData();
		}
		return new ArrayList<ResRoleVO>();
	}

	@Override
	public List<ResCalendarVO> getCalendar(String day) {
		ReqCalendarVO req = new ReqCalendarVO();
		req.setSysCode(sysCode);
		req.setSomeDay(day);
		Response<List<ResCalendarVO>> res = calendarExecuter.monthBySomeday(req);
		LOGGER.info("获取日历 params:{} result:{}", JSON.toJSONString(req), JSON.toJSONString(res));
		if(null != res && res.isSuccess()) {
			return res.getData();
		}
		return new ArrayList<ResCalendarVO>();
	}

	@Override
	public ResRoleVO findByCode(ReqRoleVO reqRoleVO) {
		reqRoleVO.setSysCode(sysCode);
		Response<ResRoleVO> res = roleExecuter.findByCode(reqRoleVO);
		LOGGER.info("根据编码获取角色 params:{} result:{}", JSON.toJSONString(reqRoleVO), JSON.toJSONString(res));
		if(null != res && res.isSuccess()) {
			return res.getData();
		}
		return new ResRoleVO();
	}

	@Override
	public List<ResOrganizationTreeVO> getOrgTreeAndEmployeesWithoutDataPermission(ReqParamVO reqParamVO) {
        reqParamVO.setSysCode(sysCode);
        Response<List<ResOrganizationTreeVO>> response = organizationExecuter.findOrgAndEmpTreeByAccount(reqParamVO);
        LOGGER.info("根据机构代码列表获取机构及其机构下的人员树,返回平台查询 params:{} result:{}", JSON.toJSONString(reqParamVO), JSON.toJSONString(response));
        if (null != response && response.isSuccess() && !CollectionUtils.isEmpty(response.getData())) {
            return response.getData();
        }
        return new ArrayList<ResOrganizationTreeVO>();
    }

	@Override
	public List<ResEmployeeVO> findAllReviewRoleEmp() {
		ReqParamVO req = new ReqParamVO();
		req.setSysCode(sysCode);
		req.setInActive("t");
		Response<List<ResEmployeeVO>> res = employeeExecuter.findAllReviewRoleEmp(req);
		LOGGER.info("查找所有拥有复议角色的员工信息,返回平台查询 params:{} result:{}", JSON.toJSONString(req), JSON.toJSONString(res));
		if(res.isSuccess() && !CollectionUtils.isEmpty(res.getData())) {
			return res.getData();
		}
		return new ArrayList<ResEmployeeVO>();
	}
	
}