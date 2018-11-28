package com.yuminsoft.ams.system.service.system.impl;

import java.text.Collator;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.ymkj.pms.biz.api.enums.OrganizationEnum;
import com.ymkj.pms.biz.api.enums.RoleEnum;
import com.ymkj.pms.biz.api.vo.request.ReqParamVO;
import com.ymkj.pms.biz.api.vo.request.ReqRoleVO;
import com.ymkj.pms.biz.api.vo.response.ResCalendarVO;
import com.ymkj.pms.biz.api.vo.response.ResEmpOrgVO;
import com.ymkj.pms.biz.api.vo.response.ResEmployeeVO;
import com.ymkj.pms.biz.api.vo.response.ResOrganizationTreeVO;
import com.ymkj.pms.biz.api.vo.response.ResRoleVO;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.common.AmsConstants;
import com.yuminsoft.ams.system.dao.system.TimeManageMapper;
import com.yuminsoft.ams.system.domain.system.TimeManagement;
import com.yuminsoft.ams.system.exception.BusinessException;
import com.yuminsoft.ams.system.service.pms.PmsApiService;
import com.yuminsoft.ams.system.service.system.TimeManageService;
import com.yuminsoft.ams.system.util.BeanUtil;
import com.yuminsoft.ams.system.util.DateUtils;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.util.Result.Type;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;
import com.yuminsoft.ams.system.vo.system.TimeManageAddParamIn;
import com.yuminsoft.ams.system.vo.system.TimeManageCheckUserParamIn;
import com.yuminsoft.ams.system.vo.system.TimeManageCheckUserParamOut;
import com.yuminsoft.ams.system.vo.system.TimeManageListParamIn;
import com.yuminsoft.ams.system.vo.system.TimeManageListParamOut;
import com.yuminsoft.ams.system.vo.system.TimeManageRemainVO;

@Service
public class TimeManageServiceImpl implements TimeManageService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TimeManageServiceImpl.class);
	
	@Autowired
	private TimeManageMapper timeManageMapper;
	
	@Autowired
	private PmsApiService pmsApiServiceImpl;
	
	@Override
	public ResponsePage<TimeManageListParamOut> getList(TimeManageListParamIn paramIn) {
		ResponsePage<TimeManageListParamOut> returnList = new ResponsePage<TimeManageListParamOut>();
		LOGGER.info("入参"+ JSON.toJSONString(paramIn));
		/*
		 * 1：如果选择的是其他角色节点-->直接在本地查询数据库除初审员之外的该记录
		 * 2：如果选择的是初审员节点-->查询平台当前登陆用户组织机构树内的初审员，而且在ams中被收回权限的记录
		 * 3：如果选择的是初审员树中的某个机构-->查询平台该机构下的初审员，而且在ams中被收回权限的记录
		 * 4：如果选择的是某个初审员-->查询ams中该初审员被收回的记录
		 */
		Map<String,String> nameMap = new HashMap<String,String>();
		Map<String,Object> queryMap = new HashMap<String,Object>();
		if(paramIn.getSearchType() == 1) {
			queryMap.put("roleCode", paramIn.getRoleCode());
			queryMap.put("timeType", paramIn.getTimeType());
		} else {
			queryMap.put("roleCode", RoleEnum.CHECK.getCode());
			queryMap.put("timeType", paramIn.getTimeType());
			if(paramIn.getSearchType() == 2) {
				List<ResEmpOrgVO> empList = getEmpsOfOrgTree(paramIn.getUserCode(), paramIn.getUserName());
				
				if(CollectionUtils.isEmpty(empList)) {
					return new ResponsePage<TimeManageListParamOut>();//如果平台查询不出员工列表，直接返回空给前台
				}
				
				for (ResEmpOrgVO resEmpOrgVO : empList) {
					nameMap.put(resEmpOrgVO.getUsercode(), resEmpOrgVO.getName());
				}
				queryMap.put("empList", empList);
			}else if(paramIn.getSearchType() == 3) {
				List<ResEmployeeVO> empList = pmsApiServiceImpl.findEmpByCodeOrName(paramIn.getOrgId(), paramIn.getUserCode(), paramIn.getUserName(), RoleEnum.CHECK.getCode());
				if(CollectionUtils.isEmpty(empList)) {
					return new ResponsePage<TimeManageListParamOut>();//如果平台查询不出员工列表，直接返回空给前台
				}
				
				for (ResEmployeeVO resEmpOrgVO : empList) {
					nameMap.put(resEmpOrgVO.getUsercode(), resEmpOrgVO.getName());
				}
				queryMap.put("empList", empList);
			}else if(paramIn.getSearchType() == 4) {
				ResEmployeeVO emp = pmsApiServiceImpl.findEmpByUserCode(paramIn.getCheckUserCode());
				if(null == emp) {
					return new ResponsePage<TimeManageListParamOut>();//如果平台查询不出员工列表，直接返回空给前台
				}
				
				nameMap.put(emp.getUsercode(), emp.getName());
				List<ResEmployeeVO> empList = new ArrayList<ResEmployeeVO>();
				empList.add(emp);
				queryMap.put("empList", empList);
			}
			
		}
		queryMap.put("startDate", paramIn.getStartDate());
		queryMap.put("endDate", paramIn.getEndDate());
		queryMap.put("searchType", paramIn.getSearchType());
		PageHelper.startPage(paramIn.getPage(),paramIn.getRows());
		Page<TimeManagement> manageList = timeManageMapper.findAll(queryMap);
		List<TimeManageListParamOut> tmlist = new ArrayList<TimeManageListParamOut>();
		if(manageList.getTotal() > 0) {
			for (TimeManagement tm : manageList) {
				TimeManageListParamOut tmo = BeanUtil.copyProperties(tm, TimeManageListParamOut.class);
				tmo.setUserName(nameMap.get(tmo.getUserCode()));
				String roleName = RoleEnum.getNameByCode(tmo.getRoleCode());
				if("未定义".equals(roleName)) {
					ReqRoleVO reqRoleVO = new ReqRoleVO();
					reqRoleVO.setCode(tmo.getRoleCode());
					roleName = pmsApiServiceImpl.findByCode(reqRoleVO).getName();
				}
				tmo.setRoleName(roleName);
				tmlist.add(tmo);
			}
			returnList.setRows(tmlist);
			returnList.setTotal(manageList.getTotal());
		}
		return returnList;
	}
	
	/**
	 * 根据当前登陆用户获取所在机构及其子机构树内的初审员
	 * 
	 * @author Jia CX
	 * <p>2018年5月29日 下午4:04:37</p>
	 * 
	 * @return
	 */
	private List<ResEmpOrgVO> getEmpsOfOrgTree(String userCode, String userName){
		ReqParamVO reqParamVO = new ReqParamVO();
		List<String> list = new ArrayList<String>();
		list.add(OrganizationEnum.OrgCode.CHECK.getCode());
		list.add(OrganizationEnum.OrgCode.SALE_CHECK.getCode());
		reqParamVO.setLoginUser(ShiroUtils.getAccount());
		reqParamVO.setOrgTypeCodes(list);
		reqParamVO.setRoleCode(RoleEnum.CHECK.getCode());
		reqParamVO.setStatus(AmsConstants.ZERO);
		reqParamVO.setInActive(AmsConstants.T);
		reqParamVO.setUsercode(userCode);
		reqParamVO.setUserName(userName);
	    List<ResOrganizationTreeVO> resList = pmsApiServiceImpl.getOrgTreeAndEmployeesWithoutDataPermission(reqParamVO);
		List<ResEmpOrgVO> empList = new ArrayList<ResEmpOrgVO>();
		getEmps(resList, empList);
		
		List<ResEmpOrgVO> returnList = new ArrayList<ResEmpOrgVO>();
		if(StringUtil.isEmpty(userCode) && StringUtil.isEmpty(userName)) {
			returnList = empList;
		}else {
			if(CollectionUtils.isNotEmpty(empList)) {
				for (ResEmpOrgVO vo : empList) {
					if(StringUtil.isNotEmpty(userCode) && StringUtil.isNotEmpty(userName)) {
						if(vo.getUsercode().equals(userCode) && vo.getName().contains(userName)) {
							returnList.add(vo);
						}
					} else if(StringUtil.isNotEmpty(userCode) && vo.getUsercode().equals(userCode)){
						returnList.add(vo);
					} else if(StringUtil.isNotEmpty(userName) && vo.getName().contains(userName)) {
						returnList.add(vo);
					}
				}
			}
		}
		
		return returnList;
	}
	
	@SuppressWarnings("unchecked")
	private void getEmps(List<ResOrganizationTreeVO> list, List<ResEmpOrgVO> empList){
        if(CollectionUtils.isNotEmpty(list)){
            for(ResOrganizationTreeVO treevo: list){
                if(treevo.getAttributes() != null){
                    Object obj = treevo.getAttributes();
                    Map<String,Object> map = (Map<String, Object>)obj;
                    if(null != map.get("usercode")){
                    	ResEmpOrgVO emp = new ResEmpOrgVO();
                    	emp.setUsercode(map.get("usercode").toString());
                    	emp.setName(treevo.getText());
                    	empList.add(emp);
                    }
                }
                if(CollectionUtils.isNotEmpty(treevo.getChildren())){
                	getEmps(treevo.getChildren(), empList);
                }
            }
        }
    }

	@Override
	public Result<String> add(TimeManageAddParamIn paramIn) {
		/*
		 * 如果是初审员新增(多个)
		 * 		--如果存在--跳过不处理
		 * 		--如果不存在--插入
		 * 如果是其他角色新增
		 * 		--如果已经存在--覆盖
		 * 		--如果不存在--插入
		 */
		int total = paramIn.getRole().equals(RoleEnum.CHECK.getCode()) ? paramIn.getUserCodes().length : 1;
		int fail = 0;
		List<String> users = new ArrayList<String>();
		List<TimeManagement> insertList = new ArrayList<TimeManagement>();
		
		if(paramIn.getRole().equals(RoleEnum.CHECK.getCode())) {
			String[] userCodes = paramIn.getUserCodes();
			for (String string : userCodes) {
				if(null != getRecord(paramIn.getRole(), paramIn.getTimeType(), paramIn.getDate(),string)) {
					fail = fail + 1;
					users.add(pmsApiServiceImpl.findEmpByUserCode(string).getName());
					continue;
				} else {
					TimeManagement t = new TimeManagement();
					t.setRoleCode(paramIn.getRole());
					t.setUserCode(string);
					t.setTimeType(paramIn.getTimeType());
					t.setDate(paramIn.getDate());
					t.setStartTime(paramIn.getStartTime());
					t.setEndTime(paramIn.getEndTime());
					t.setCreator(ShiroUtils.getCurrentUser().getUsercode());
					t.setCreatorName(ShiroUtils.getCurrentUser().getName());
					t.setCreateTime(new Date());
					t.setModifier(ShiroUtils.getCurrentUser().getUsercode());
					t.setModifierName(ShiroUtils.getCurrentUser().getName());
					t.setModifyTime(new Date());
					insertList.add(t);
				}
			}
		} else {
			TimeManagement tm = getRecord(paramIn.getRole(), paramIn.getTimeType(), paramIn.getDate(), null);
			if(null != tm) {
				tm.setDate(paramIn.getDate());
				tm.setStartTime(paramIn.getStartTime());
				tm.setEndTime(paramIn.getEndTime());
				tm.setModifier(ShiroUtils.getCurrentUser().getUsercode());
				tm.setModifierName(ShiroUtils.getCurrentUser().getName());
				tm.setModifyTime(new Date());
				timeManageMapper.update(tm);
			} else {
				TimeManagement t = new TimeManagement();
				t.setRoleCode(paramIn.getRole());
				t.setUserCode(null);
				t.setTimeType(paramIn.getTimeType());
				t.setDate(paramIn.getDate());
				t.setStartTime(paramIn.getStartTime());
				t.setEndTime(paramIn.getEndTime());
				t.setCreator(ShiroUtils.getCurrentUser().getUsercode());
				t.setCreatorName(ShiroUtils.getCurrentUser().getName());
				t.setCreateTime(new Date());
				t.setModifier(ShiroUtils.getCurrentUser().getUsercode());
				t.setModifierName(ShiroUtils.getCurrentUser().getName());
				t.setModifyTime(new Date());
				insertList.add(t);
			}
		}
		
		if(CollectionUtils.isNotEmpty(insertList)) {
			timeManageMapper.batchInsert(insertList);
		}
		
		String message = "新增成功" + (total - fail) + "条，失败" + fail + "条记录。";
		//如果是初审新增
		if(paramIn.getRole().equals(RoleEnum.CHECK.getCode()) && fail > 0) {
			String msg = "";
			for (String str : users) {
				msg = msg + "、" + str;
			}
			message = message + "<br>失败原因：" + msg.substring(1) + "的记录已经存在！";
		} 
		return new Result<String>(Type.SUCCESS, message);
	}

	@Override
	public Result<String> update(TimeManageAddParamIn paramIn) {
		TimeManagement t = new TimeManagement();
		t.setId(paramIn.getId());
		t.setStartTime(paramIn.getStartTime());
		t.setEndTime(paramIn.getEndTime());
		t.setModifier(ShiroUtils.getCurrentUser().getUsercode());
		t.setModifierName(ShiroUtils.getCurrentUser().getName());
		t.setModifyTime(new Date());
		timeManageMapper.update(t);
		
		return new Result<String>(Type.SUCCESS);
	}

	@Override
	public Result<String> delete(TimeManageAddParamIn paramIn) {
		timeManageMapper.delete(paramIn.getId());
		return new Result<String>(Type.SUCCESS);
	}

	@Override
	public boolean hasPermission() {
		/*
		 * 1：调用平台确定今天是节假日还是工作日
		 * 2：如果没有设置限制时间，返回true可以登录
		 * 3：如果设置了限制时间，看当前时间是否在限制时间内，如果在返回true可以登录，不在返回false
		 */
		boolean flag = false;//默认不可以登录
		List<TimeManagement> list = getTimeMangeListOfCurrentUser();
		
		if(CollectionUtils.isNotEmpty(list)) {
			for (TimeManagement t : list) {
				try {
					if("00:00:00".equals(t.getStartTime()) && "00:00:00".equals(t.getEndTime())) {
						continue;
					}
					boolean isBetween = DateUtils.isBetween(t.getStartTime(), t.getEndTime(), DateUtils.dateToString(new Date(), DateUtils.DEFAULT_TIME_FORMAT));
					if (isBetween) {
						flag = true;
						return flag;//只要有任何一个角色的限制时间包含当前时间，返回可以登录
					}
				} catch (ParseException e) {
					LOGGER.error("校验当前登录用户是否可以登录-----时间解析时出错", e);
					continue;
				}
			}
		} else {
			flag = true;
		}
		return flag;
	}

	/**
	 * 获取当前登录用户的时间限制列表(包含他所有的角色)
	 * 
	 * @author Jia CX
	 * @date 2017年12月13日 下午3:14:19
	 * @notes
	 * 
	 * @return
	 */
	private List<TimeManagement> getTimeMangeListOfCurrentUser() {
		List<TimeManagement> list = new ArrayList<TimeManagement>();
		boolean isWeekday = pmsApiServiceImpl.isWeekday(DateUtils.dateToString(new Date(), DateUtils.FORMAT_DATE_YYYY_MM_DD));
		List<ResRoleVO> roles = ShiroUtils.getShiroUser().getRoles();
		for (ResRoleVO resRoleVO : roles) {
			Map<String, Object> queryMap = new HashMap<String, Object>();
			queryMap.put("roleCode", resRoleVO.getCode());
			queryMap.put("timeType", isWeekday ? "0" : "1");
			queryMap.put("userCode", ShiroUtils.getAccount());
			TimeManagement tm = timeManageMapper.findOneOfToday(queryMap);
			if(null != tm) {
				list.add(tm);
			}
		}
		return list;
	}
	
	private List<TimeManagement> getTimeMangeListOfCurrentUser(String userCode) {
		List<TimeManagement> list = new ArrayList<TimeManagement>();
		boolean isWeekday = pmsApiServiceImpl.isWeekday(DateUtils.dateToString(new Date(), DateUtils.FORMAT_DATE_YYYY_MM_DD));
		List<String> roles = pmsApiServiceImpl.findRoleCodeListByAccount(userCode);
		for (String resRoleVO : roles) {
			Map<String, Object> queryMap = new HashMap<String, Object>();
			queryMap.put("roleCode",resRoleVO);
			queryMap.put("timeType", isWeekday ? "0" : "1");
			queryMap.put("userCode", userCode);
			TimeManagement tm = timeManageMapper.findOneOfToday(queryMap);
			if(null != tm) {
				list.add(tm);
			}
		}
		return list;
	}

	/**
	 * 组合某个用户的所有角色当天的有效登录时间段
	 * 
	 * @author Jia CX
	 * @date 2017年12月13日 上午10:31:20
	 * @notes
	 * 
	 * @param list
	 * @param li
	 */
	private void combinValidPeriod(List<TimeManagement> list, List<JSONObject> li) {
		if(list.size() > 1) {
			/*按开始时间升序排列*/
			Collections.sort(list, new Comparator<TimeManagement>() {
				@Override
				public int compare(TimeManagement o1, TimeManagement o2) {
					return o1.getStartTime().compareTo(o2.getStartTime());
				}
			});
			
			String st = list.get(0).getStartTime();
			String et = list.get(0).getEndTime();
			for (int i = 1; i < list.size(); i++) {
				if(list.get(i).getStartTime().compareTo(et) > 0) {
					JSONObject obj = new JSONObject();
					obj.put("startTime", st);
					obj.put("endTime", et);
					li.add(obj);
					
					st = list.get(i).getStartTime();
					et = list.get(i).getEndTime();
				} else {
					if(list.get(i).getEndTime().compareTo(et) > 0) {
						et = list.get(i).getEndTime();
					}
				}
				if(i == list.size() - 1) {
					JSONObject obj = new JSONObject();
					obj.put("startTime", st);
					obj.put("endTime", et);
					li.add(obj);
				}
			}
		} else {
			JSONObject obj = new JSONObject();
			obj.put("startTime", list.get(0).getStartTime());
			obj.put("endTime", list.get(0).getEndTime());
			li.add(obj);
		}
	}

	@Override
	public List<TimeManageCheckUserParamOut> getCheckUsers(TimeManageCheckUserParamIn paramIn) {
		List<TimeManageCheckUserParamOut> returnList = new ArrayList<TimeManageCheckUserParamOut>();
		if(paramIn.getSearchType() == 2) {
			List<ResEmpOrgVO> empList = getEmpsOfOrgTree(null, null);
			
			if(CollectionUtils.isEmpty(empList)) {
				return new ArrayList<TimeManageCheckUserParamOut>();//如果平台查询不出员工列表，直接返回空给前台
			} else {
				Collections.sort(empList, new Comparator<ResEmpOrgVO>() {
	                @Override
	                public int compare(ResEmpOrgVO o1, ResEmpOrgVO o2) {
	                    Collator com = Collator.getInstance(java.util.Locale.CHINA);
	                    return com.getCollationKey(o1.getName()).compareTo(com.getCollationKey(o2.getName()));
	                }
	            });
				
				for (ResEmpOrgVO resEmpOrgVO : empList) {
					TimeManageCheckUserParamOut out = new TimeManageCheckUserParamOut();
					out.setUserCode(resEmpOrgVO.getUsercode());
					out.setUserName(resEmpOrgVO.getName());
					returnList.add(out);
				}
			}
		}else if(paramIn.getSearchType() == 3) {
			List<ResEmployeeVO> empList = pmsApiServiceImpl.findEmpByCodeOrName(paramIn.getOrgId(), null, null, RoleEnum.CHECK.getCode());
			if(CollectionUtils.isEmpty(empList)) {
				return new ArrayList<TimeManageCheckUserParamOut>();//如果平台查询不出员工列表，直接返回空给前台
			} else {
				for (ResEmployeeVO resEmpOrgVO : empList) {
					TimeManageCheckUserParamOut out = new TimeManageCheckUserParamOut();
					out.setUserCode(resEmpOrgVO.getUsercode());
					out.setUserName(resEmpOrgVO.getName());
					returnList.add(out);
				}
			}
		}else if(paramIn.getSearchType() == 4) {
			ResEmployeeVO empList = pmsApiServiceImpl.findEmpByUserCode(paramIn.getCheckUserCode());
			if(null == empList) {
				return new ArrayList<TimeManageCheckUserParamOut>();//如果平台查询不出员工，直接返回空给前台
			} else {
				TimeManageCheckUserParamOut out = new TimeManageCheckUserParamOut();
				out.setUserCode(empList.getUsercode());
				out.setUserName(empList.getName());
				returnList.add(out);
			}
		}
			
		return returnList;
	}

	@Override
	public TimeManagement getRecord(String roleCode, String timeType, String date, String userCode) {
		Map<String,Object> queryMap = new HashMap<String,Object>();
		queryMap.put("roleCode", roleCode);
		queryMap.put("timeType", timeType);
		queryMap.put("date", date);
		if(StringUtils.isNotBlank(userCode)) {
			queryMap.put("userCode", userCode);
		}
		return timeManageMapper.findOne(queryMap);
	}

	@Override
	public Result<String> batchDelete(String[] ids) {
		timeManageMapper.batchDelete(ids);
		return new Result<String>(Type.SUCCESS);
	}

	@Override
	public Result<String> batchUpdate(TimeManageAddParamIn paramIn) {
		Map<String,Object> queryMap = new HashMap<String,Object>();
		queryMap.put("startTime", paramIn.getStartTime());
		queryMap.put("endTime", paramIn.getEndTime());
		queryMap.put("ids", paramIn.getIds().split("-"));
		queryMap.put("modifier", ShiroUtils.getCurrentUser().getUsercode());
		queryMap.put("modifierName", ShiroUtils.getCurrentUser().getName());
		queryMap.put("modifyTime", new Date());
		timeManageMapper.batchUpdate(queryMap);
		
		return new Result<String>(Type.SUCCESS);
	}

	@Override
	public List<ResRoleVO> getRoleByAccount() {
		return pmsApiServiceImpl.getRoleByAccount();
	}

	@Override
	public List<ResCalendarVO> getCalendar(String day) {
		return pmsApiServiceImpl.getCalendar(day);
	}

	@Override
	public int getRemainingTime(String userCode) {
		List<TimeManagement> list = getTimeMangeListOfCurrentUser(userCode);
		
		List<JSONObject> li = new ArrayList<JSONObject>();
		if(CollectionUtils.isNotEmpty(list)) {
			combinValidPeriod(list, li);
		} else {
			return Integer.MAX_VALUE;//无限制记录,返回一个很大的可用剩余时间
		}
		
		String now = DateUtils.dateToString(new Date(), DateUtils.DEFAULT_TIME_FORMAT);
		for (JSONObject obj : li) {
			try {
				if(DateUtils.isBetween(obj.getString("startTime"), obj.getString("endTime"), now)) {
					int diff = DateUtils.getSecondsBetween(now, obj.getString("endTime"));
					return diff;
				}
			} catch (ParseException e) {
				LOGGER.error("获取登录用户今天剩余有效登录时间-----时间解析时出错", e);
				continue;
			}
		}
		
		return 0;//已过今天系统可用时间,返回0s,系统自动登出
	}
	
	@Override
	public TimeManageRemainVO getRoleAndReaminTime(String userCode) {
		List<TimeManagement> list = getTimeMangeListOfCurrentUser(userCode);
		
		List<JSONObject> li = new ArrayList<JSONObject>();
		if(CollectionUtils.isNotEmpty(list)) {
			li = combinRolePeriod(list);
		} else {
			return new TimeManageRemainVO(null, Integer.MAX_VALUE, 0);//无限制记录,返回一个很大的可用剩余时间
		}
		
		String now = DateUtils.dateToString(new Date(), DateUtils.DEFAULT_TIME_FORMAT);
		for (JSONObject obj : li) {
			//不能在当前的角色限制记录时间段还没结束的时候去刷新页面
			//而应该在角色的上一条限制记录时间段刚刚结束10s之内去刷新页面
			//所以此方法是否刷新的判断实际上是判断距离上个角色限制记录时间段结束的时间
			try {
				if(DateUtils.isBetween(obj.getString("startTime"), obj.getString("endTime"), now)) {
					int remainRoleTime = DateUtils.getSecondsBetween(now, obj.getString("endTime"));
					int tem = DateUtils.getSecondsBetween(obj.getString("startTime"), now);
					int needRefresh = tem < 10 && tem >= 0 ? 1 : 0;
					return new TimeManageRemainVO(obj.getString("role"), remainRoleTime, needRefresh);
				}
			} catch (ParseException e) {
				LOGGER.error("获取当前时间登陆的角色的剩余时间-----时间解析时出错", e);
				continue;
			}
		}
		
		//有时间限制记录，但是当前时间不在任何角色的时间限制之内，返回0s，需要刷新页面（实际上退出操作已经把系统退出了）
		return new TimeManageRemainVO(null, 0, 1);
	}
	
	/**
	 * 组合时间段和登陆的角色
	 * 
	 * @author Jia CX
	 * <p>2018年9月29日 上午10:55:37</p>
	 * 
	 * @param list
	 * @param li
	 * @throws ParseException 
	 */
	private List<JSONObject> combinRolePeriod(List<TimeManagement> list) {
		List<JSONObject> li = new ArrayList<JSONObject>();
		if(list.size() > 1) {
			List<String> timeList = new ArrayList<String>();
			for (TimeManagement tm : list) {
				timeList.add(tm.getStartTime());
				timeList.add(tm.getEndTime());
			}
			
			timeList = new ArrayList<String>(new HashSet<String>(timeList));//去重
			Collections.sort(timeList);//升序
			
			Iterator<String> it = timeList.iterator();
			String st = it.next();
			while(it.hasNext()) {
				String role = "";
				String et = it.next();
				for (TimeManagement tm : list) {
					try {
						if(DateUtils.isBetween(tm.getStartTime(), tm.getEndTime(), st) && DateUtils.isBetween(tm.getStartTime(), tm.getEndTime(), et)) {
							role = StringUtils.isEmpty(role) ? role + tm.getRoleCode() : role + "," + tm.getRoleCode();
						}
					} catch (Exception e) {
						throw new BusinessException("获取登录用户角色剩余时间解析时出错", e);
					}
				}
				
				if(StringUtils.isNotEmpty(role)) {
					JSONObject obj = new JSONObject();
					obj.put("role", role);
					obj.put("startTime", st);
					obj.put("endTime", et);
					li.add(obj);
				}
				
				st = et;
			}
		} else {
			JSONObject obj = new JSONObject();
			obj.put("role", list.get(0).getRoleCode());
			obj.put("startTime", list.get(0).getStartTime());
			obj.put("endTime", list.get(0).getEndTime());
			li.add(obj);
		}
		return li;
	}


}
