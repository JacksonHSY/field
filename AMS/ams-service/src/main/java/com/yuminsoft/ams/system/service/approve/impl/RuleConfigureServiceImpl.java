package com.yuminsoft.ams.system.service.approve.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ymkj.ams.api.enums.EnumConstants;
import com.ymkj.ams.api.vo.request.master.ReqBMSTMReasonVO;
import com.ymkj.pms.biz.api.enums.OrganizationEnum;
import com.ymkj.pms.biz.api.enums.RoleEnum;
import com.ymkj.pms.biz.api.vo.request.ReqLevelVO;
import com.ymkj.pms.biz.api.vo.request.ReqMessageVO;
import com.ymkj.pms.biz.api.vo.request.ReqParamVO;
import com.ymkj.pms.biz.api.vo.response.ResEmpOrgVO;
import com.ymkj.pms.biz.api.vo.response.ResEmployeeVO;
import com.ymkj.pms.biz.api.vo.response.ResGroupVO;
import com.ymkj.pms.biz.api.vo.response.ResOrganizationTreeVO;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.common.AmsConstants;
import com.yuminsoft.ams.system.dao.approve.AgenLeaderMapper;
import com.yuminsoft.ams.system.dao.approve.ProcessrulesCfgMapper;
import com.yuminsoft.ams.system.dao.approve.StaffOrderAbilityMapper;
import com.yuminsoft.ams.system.domain.approve.AgenLeader;
import com.yuminsoft.ams.system.domain.approve.ProcessrulesCfg;
import com.yuminsoft.ams.system.domain.approve.UserRule;
import com.yuminsoft.ams.system.domain.approve.UserRuleSub;
import com.yuminsoft.ams.system.service.approve.RuleConfigureService;
import com.yuminsoft.ams.system.service.bms.BmsLoanInfoService;
import com.yuminsoft.ams.system.service.pms.PmsApiService;
import com.yuminsoft.ams.system.service.websocket.MessageServer;
import com.yuminsoft.ams.system.util.BeanUtil;
import com.yuminsoft.ams.system.util.HttpUtils;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.util.Result.Type;
import com.yuminsoft.ams.system.vo.apply.*;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Executors;

/**
 * 复核确认
 * 
 * @author fusj
 *
 */
@Service
public class RuleConfigureServiceImpl implements RuleConfigureService {
	private static final Logger LOGGER = LoggerFactory.getLogger(RuleConfigureServiceImpl.class);

	@Autowired
	private ProcessrulesCfgMapper processrulesCfgMapper;
	
	@Autowired
	private AgenLeaderMapper agenLeaderMapper;

	@Autowired
	private StaffOrderAbilityMapper staffOrderAbilityMapper;

	@Autowired
	private PmsApiService pmsApiService;

	@Autowired
	private MessageServer messageServer;

	@Value("${sys.code}")
	private String sysCode;

	@Value("${ams.pms.url}")
	private String pmsUrl;
	
	@Autowired
	private BmsLoanInfoService bmsLoanInfoServiceImpl;

	/**
	 * 规则配置流程列表 组行
	 */
	@Override
	public ResponsePage<ProcessrulesCfg> findRuleAndUserList(RequestPage requestPage) {
		ResponsePage<ProcessrulesCfg> page = new ResponsePage<ProcessrulesCfg>();
		// 当前登录用户
		ResEmployeeVO currentUser = ShiroUtils.getCurrentUser();

		// 根据工号和机构代码查询当前登录用户下级
		ReqLevelVO vo = new ReqLevelVO();
		vo.setSysCode(sysCode);
		vo.setLoginUser(currentUser.getUsercode());
		vo.setRoleCode(RoleEnum.CHECK.getCode());
		vo.setLevelType(OrganizationEnum.OrgCode.CHECK.getCode());
		List<ResEmpOrgVO> response = pmsApiService.getLowerEmpByAccount(vo);
		List<String> userCodeList = new ArrayList<String>();
		List<ResEmpOrgVO> list = new ArrayList<ResEmpOrgVO>();
		if (null != response && CollectionUtils.isNotEmpty(response)){
		    list = response;
		    for (ResEmpOrgVO reo : list) {
		        userCodeList.add(reo.getUsercode());
		    }
		}

		List<ProcessrulesCfg> processlist = new ArrayList<ProcessrulesCfg>();
		if (CollectionUtils.isNotEmpty(userCodeList)) {
			PageHelper.startPage(requestPage.getPage(), requestPage.getRows());
			processlist = processrulesCfgMapper.findByUserCodeList(userCodeList);
			for (ProcessrulesCfg processrulesCfg : processlist) {
			    ResEmployeeVO rev = pmsApiService.findEmpByUserCode(processrulesCfg.getCreatedBy());//redis已经存储了，所以可以这样直接调用
			    processrulesCfg.setCreatedBy(rev.getName());
				for (ResEmpOrgVO resEmpOrgVO : list) {
					if (resEmpOrgVO.getUsercode().equals(processrulesCfg.getUserCode())) {
						processrulesCfg.setUserName(resEmpOrgVO.getName());
						processrulesCfg.setOrgName(resEmpOrgVO.getOrgName());
						processrulesCfg.setOrgId(String.valueOf(resEmpOrgVO.getOrgId()));
						processrulesCfg.setOrgPid(resEmpOrgVO.getOrgPid());
						processrulesCfg.setOrgPname(resEmpOrgVO.getOrgPname());
					}
				}
			}
			page.setRows(processlist);
			page.setTotal(((Page<ProcessrulesCfg>) processlist).getTotal());
		}
		return page;

	}

	/**
	 * 规则配置 修改保存
	 * 
	 * @param processrulesCfg
	 * @return
	 */
	@Override
	public Result<String> updateProcessRule(ProcessrulesCfg processrulesCfg) {
		Result<String> result = new Result<String>(Type.FAILURE);
		/*
		 * 1：如果是新增-->新增通过-->已存在-->提示规则已存在
		 *                        -->不存在-->直接新增
		 *             -->新增拒退 -->已存在-->判断，增量新增规则
		 *                        -->不存在-->直接新增
		 * 2：如果是修改 -->通过改为拒绝/退回，退回改为拒绝，拒绝改为退回-->新类型的记录不存在-->直接update
		 *                                                          -->新类型的记录已存在-->删除原记录，判断，增量新增规则
		 *             -->拒改为拒/退改为退-->如果是修改自己的-->直接更新
		 *                                -->如果是修改为别人的-->新类型的记录不存在-->直接update
         *                                                    -->新类型的记录已存在-->删除原记录，判断，增量新增规则
		 *             -->通过/拒绝/退回改为通过-->通过记录不存在-->直接update
		 *                                     -->通过记录已存在-->提示规则已存在
		 */
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userCode", processrulesCfg.getUserCode());
		map.put("ruleType", processrulesCfg.getRuleType());
		ProcessrulesCfg rule = processrulesCfgMapper.findOne(map);
		
		int num = 0;
		if(null == processrulesCfg.getId()){
		    //新增
		    if(EnumConstants.RtfNodeState.XSCSPASS.getValue().equals(processrulesCfg.getRuleType())){
		        if(null != rule){
		            result.addMessage("该规则已经存在!");
	                return result;
		        }else{
		            num = processrulesCfgMapper.save(processrulesCfg);
		        }
		    }else{
		        if(null != rule){
                    num = processrulesCfgMapper.update(reassembleRule(processrulesCfg, rule));
		        }else{
		            num = processrulesCfgMapper.save(processrulesCfg);
		        }
		    }
		}else{
		    //修改
		    ProcessrulesCfg oldrule = processrulesCfgMapper.findById(processrulesCfg.getId());
		    String oldRuleType = oldrule.getRuleType();
		    String newRuleType = processrulesCfg.getRuleType();
		    if((EnumConstants.RtfNodeState.XSCSPASS.getValue().equals(oldRuleType) && EnumConstants.RtfNodeState.XSCSREJECT.getValue().equals(newRuleType))
		                    || (EnumConstants.RtfNodeState.XSCSPASS.getValue().equals(oldRuleType) && EnumConstants.RtfNodeState.XSCSRETURN.getValue().equals(newRuleType))
		                    || (EnumConstants.RtfNodeState.XSCSRETURN.getValue().equals(oldRuleType) && EnumConstants.RtfNodeState.XSCSREJECT.getValue().equals(newRuleType))
		                    || (EnumConstants.RtfNodeState.XSCSREJECT.getValue().equals(oldRuleType) && EnumConstants.RtfNodeState.XSCSRETURN.getValue().equals(newRuleType))){
		        if(null != rule){
		            processrulesCfgMapper.delete(processrulesCfg.getId());
		            processrulesCfg = reassembleRule(processrulesCfg, rule);
		        }
		        num = processrulesCfgMapper.update(processrulesCfg);
		    }else if((EnumConstants.RtfNodeState.XSCSRETURN.getValue().equals(oldRuleType) && EnumConstants.RtfNodeState.XSCSRETURN.getValue().equals(newRuleType))
                            || (EnumConstants.RtfNodeState.XSCSREJECT.getValue().equals(oldRuleType) && EnumConstants.RtfNodeState.XSCSREJECT.getValue().equals(newRuleType))){
		        if(rule != null && rule.getId().equals(processrulesCfg.getId())){
		            processrulesCfgMapper.delete(processrulesCfg.getId());
		            processrulesCfg = reassembleRule(processrulesCfg, rule);
		        }
		        num = processrulesCfgMapper.update(processrulesCfg);
		    }else if((EnumConstants.RtfNodeState.XSCSRETURN.getValue().equals(oldRuleType) && EnumConstants.RtfNodeState.XSCSPASS.getValue().equals(newRuleType))
		                    || (EnumConstants.RtfNodeState.XSCSREJECT.getValue().equals(oldRuleType) && EnumConstants.RtfNodeState.XSCSPASS.getValue().equals(newRuleType))
		                    || (EnumConstants.RtfNodeState.XSCSPASS.getValue().equals(oldRuleType) && EnumConstants.RtfNodeState.XSCSPASS.getValue().equals(newRuleType))){
		        if(null != rule){
		            result.addMessage("该规则已经存在!");
		            return result;
		        }else{
		            num = processrulesCfgMapper.update(processrulesCfg);
		        }
		    }
		}
		result.addMessage((num > 0) ? "成功" : "失败");
        result.setType((num > 0) ? Type.SUCCESS : Type.FAILURE);
		return result;
	}

    /**
     * 重新组装规则
     * 
     * @param processrulesCfg
     * @param rule
     * @author JiaCX
     * @date 2017年8月7日 下午1:57:47
     */
    private ProcessrulesCfg reassembleRule(ProcessrulesCfg processrulesCfg, ProcessrulesCfg rule)
    {
        String reasonCode = combine(processrulesCfg, rule, "1");
        String reasonName = combine(processrulesCfg, rule, "2");
        processrulesCfg.setReasonCode(reasonCode);
        processrulesCfg.setReasonName(reasonName);
        processrulesCfg.setId(rule.getId());
        return processrulesCfg;
    }

    /**
     * 把新code中存在，但是老code中不存在的，添加到老的code中
     * 
     * @param newRule
     * @param oldRule
     * @param type 1:取code，2：取name
     * @return
     * @author JiaCX
     * @date 2017年8月5日 下午6:08:19
     */
    private String combine(ProcessrulesCfg newRule, ProcessrulesCfg oldRule, String type){
        String reason = "";
        List<String> news = new ArrayList<String>();
        List<String> olds = new ArrayList<String>();
        if("1".equals(type)){
            news = Arrays.asList(newRule.getReasonCode().split("、"));
            olds = Arrays.asList(oldRule.getReasonCode().split("、"));
        }else{
            news = Arrays.asList(newRule.getReasonName().split("、"));
            olds = Arrays.asList(oldRule.getReasonName().split("、"));
        }
        List<String> list = new ArrayList<String>();
        for(String str: olds){
            list.add(str);
        }
        for(String str: news){
            if(!olds.contains(str)){
                list.add(str);
            }
        }
        //olds.addAll(temp);//Arrays.asList()出来的list 不能对其进行add，remove，set操作
        reason = StringUtils.join(list.toArray(new String[list.size()]), "、");
        LOGGER.info("-----------"+reason);
        return reason;
    }

	/**
	 * 代理组长
	 * 
	 * @param agenLeader
	 * @return
	 */
	@Override
	public int saveProxyUser(AgenLeader agenLeader) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userCode", agenLeader.getUserCode());
		AgenLeader proxyLeader = agenLeaderMapper.findOne(map);
		int a = 0;
		if (proxyLeader != null) {
			if ("-1".equals(agenLeader.getProxyUser())) {// 页面选的“请选择”
				if (proxyLeader.getStatus() != null && "-1".equals(proxyLeader.getStatus())) {
					return 1;
				}
				agenLeader.setStatus("-1");// 代理组长失效
			} else {
				if (proxyLeader.getStatus() != null && "1".equals(proxyLeader.getStatus()) && proxyLeader.getProxyUser().equals(agenLeader.getProxyUser())) {
					return 1;
				}
				agenLeader.setStatus("1");// 代理组长有效
			}
			a = agenLeaderMapper.update(agenLeader);
		} else {
			if ("-1".equals(agenLeader.getProxyUser())) {// 页面选的“请选择”
				return 1;
			}
			a = agenLeaderMapper.save(agenLeader);
			// 发送站内消息
		}
		
		/*
		 * 发送站内消息
		 * 此处重开线程异步发送，
		 * 因为发送站内消息(也可能是发送邮件，短信，等一些对接口响应无关紧要的操作)这件事可能会耗时影响原接口的响应，
		 * 而发送站内消息成功与否按道理来说不应该影响代理组长的修改
		 */
		final String uc = agenLeader.getUserCode();
		final String pu = agenLeader.getProxyUser();
		Executors.newCachedThreadPool().submit(new Runnable() {
			@Override
			public void run() {
				sendStationMessage(uc, pu);
			}
		});
		return a;

	}

	/**
	 * 发送站内消息
	 * 
	 * @param auditor  被设置代理组长的组员
	 * @param proxyLeader  该组员的代理组长
	 * @return
	 * @author JiaCX
	 * @date 2017年5月9日 下午4:27:38
	 */
	public boolean sendStationMessage(String auditor, String proxyLeader) {
		boolean flag = false;
		String userCode = ShiroUtils.getCurrentUser().getUsercode();
		String content = "";
		if ("-1".equals(proxyLeader)) {
			content = "组长【" + userCode + "】取消了本组初审员【" + auditor + "】的代理组长!!!";
		} else {
			content = "组长【" + userCode + "】设置本组初审员【" + auditor + "】的代理组长为【" + proxyLeader + "】!!!";
		}
		proxyLeader = "-1".equals(proxyLeader) ? "空" : proxyLeader;
		List<ResEmployeeVO> empList = pmsApiService.getAllFirstApprovalLeaders();
		for (ResEmployeeVO resEmployeeVO : empList) {
			if (userCode.equals(resEmployeeVO.getUsercode())) {// 当前登录用户是一个初审组长
				// 查找他的主管
				ReqLevelVO reqVo = new ReqLevelVO();
				reqVo.setSysCode(sysCode);
				reqVo.setLoginUser(ShiroUtils.getAccount());
				reqVo.setRoleCode(RoleEnum.CHECK_DIRECTOR.getCode());
				reqVo.setInActive(AmsConstants.T);
				reqVo.setStatus(AmsConstants.ZERO);
				reqVo.setLevelType(ShiroUtils.getShiroUser().getFuncCode());
				ResEmployeeVO emp = pmsApiService.getLeaderByCode(reqVo);
				if (emp != null) {
					// 发送站内消息
					ReqMessageVO vo = new ReqMessageVO();
					vo.setSysCode(sysCode);
					vo.setTitle("设置代理组长");
					vo.setContent(content);
					vo.setReceiver(emp.getUsercode());// 站内消息接收人(当前登录组长的主管)
					// 发送站内消息
					flag = messageServer.sendMessages(vo);
					LOGGER.info("发送站内消息成功！！！！");
					if (flag) {
						// 发送消息数量
						Map<String, String> param = new HashMap<String, String>();
						param.put("account", ShiroUtils.getAccount());
						String sendMessage = HttpUtils.doPost(pmsUrl + "api/message/sendCount", param);
						LOGGER.debug("通知{}未读站内消息数量，返回结果:{}", ShiroUtils.getAccount(), sendMessage);
						// messageServer.sendUnreadCountToEmployees(null);
					}
				}
			}
		}
		return flag;
	}

	/**
	 * 查出为该用户分配的产品code列表
	 * 
	 * @param userCode
	 * @return
	 * @author JiaCX
	 * @date 2017年4月6日 下午3:29:52
	 */
	@Override
	public List<String> findAllProductCodeByUserCode(String userCode) {
		return staffOrderAbilityMapper.findAllProductCodeByUserCode(userCode);
	}

	/**
	 * 重新组装小组信息和组员信息
	 * 
	 * @param list
	 * @return
	 * @author JiaCX
	 * @date 2017年4月10日 下午2:24:38
	 */
	@Override
	public List<GroupVO> combineEmployee(List<ResGroupVO> list) {
		Map<String, String> map = new HashMap<String, String>();
		for (ResGroupVO resGroupVO : list) {
			map.put(resGroupVO.getGroupUsercode(), resGroupVO.getGroupLeader());
		}
		List<GroupVO> groupVO = new ArrayList<GroupVO>();
		List<AgenLeader> all = agenLeaderMapper.findAll(null);
		for (ResGroupVO resGroupVO : list) {
			GroupVO gvo = new GroupVO();
			gvo.setGroupId(resGroupVO.getGroupId());
			gvo.setGroupLeader(resGroupVO.getGroupLeader());
			gvo.setGroupName(resGroupVO.getGroupLeader());
			gvo.setGroupUsercode(resGroupVO.getGroupUsercode());
			List<ResEmployeeVO> resEmpVO = resGroupVO.getEmployees();
			List<EmployeeVO> empVO = new ArrayList<EmployeeVO>();
			for (ResEmployeeVO resEmployeeVO : resEmpVO) {
				EmployeeVO emp = BeanUtil.copyProperties(resEmployeeVO, EmployeeVO.class);
				for (AgenLeader agenLeader : all) {
					if (agenLeader.getUserCode().equals(emp.getUsercode())) {
						emp.setProxyUserCode(agenLeader.getProxyUser());
						emp.setProxyUserName(map.containsKey(agenLeader.getProxyUser()) ? map.get(agenLeader.getProxyUser()) : "");
					}
				}
				empVO.add(emp);
			}
			gvo.setEmployees(empVO);
			groupVO.add(gvo);
		}
		return groupVO;
	}

	/**
	 * 规则配置，删除配置
	 * 
	 * @param id
	 * @return
	 * @author JiaCX
	 * @date 2017年4月12日 下午1:38:39
	 */
	@Override
	public Result<String> deleteProcessRule(long id) {
		Result<String> result = new Result<String>();
		int n = processrulesCfgMapper.delete(id);
		result.addMessage((n > 0) ? "成功" : "失败");
		result.setType((n > 0) ? Type.SUCCESS : Type.FAILURE);
		return result;
	}

	@Override
    public ResponsePage<UserRuleVO> getUserRuleList(RequestPage requestPage)
    {
        ResponsePage<UserRuleVO> page = new ResponsePage<UserRuleVO>();
        
        // 根据工号和机构代码查询当前登录用户下级初审员列表
        List<String> userCodeList = new ArrayList<String>();
		ReqLevelVO reqLevelVO = new ReqLevelVO();
		reqLevelVO.setLevelType(OrganizationEnum.OrgCode.CHECK.getCode());
		reqLevelVO.setRoleCode(RoleEnum.CHECK.getCode());
        List<ResEmpOrgVO> empList = pmsApiService.getLowerEmpByAccount(reqLevelVO);
        if (CollectionUtils.isNotEmpty(empList)){
            for (ResEmpOrgVO reo : empList) {
                userCodeList.add(reo.getUsercode());
            }
        }
        
        
        //转换用户名，所在组名，被收回或者拒绝的原因数量
        if (CollectionUtils.isNotEmpty(userCodeList)) {
            List<UserRuleVO> list = new ArrayList<UserRuleVO>();
            PageHelper.startPage(requestPage.getPage(), requestPage.getRows());
            list = processrulesCfgMapper.getUserRuleList(userCodeList);
            for (UserRuleVO ur : list) {
                ResEmployeeVO rev = pmsApiService.findEmpByUserCode(ur.getUpdatePerson());//redis已经存储了，所以可以这样直接调用
                ur.setUpdatePerson(rev.getName());
                for (ResEmpOrgVO resEmpOrgVO : empList) {
                    if (resEmpOrgVO.getUsercode().equals(ur.getUserCode())) {
                        ur.setUserName(resEmpOrgVO.getName());
                        ur.setGroupName(resEmpOrgVO.getOrgName());
                    }
                }
            }
            page.setRows(list);
            page.setTotal(((Page<UserRuleVO>) list).getTotal());
        }
        
        return page;
    }

	@Override
    public Result<String> addUserRule(List<UserRuleParamIn> list)
    {
        Result<String> res = new Result<String>(Type.SUCCESS);
        try
        {
            if(CollectionUtils.isNotEmpty(list)){
                List<ReqBMSTMReasonVO> rejectList = getAllReason("XSCS", "reject");
                List<ReqBMSTMReasonVO> returnList = getAllReason("XSCS", "return");
                List<UserRuleSub> subList = new ArrayList<UserRuleSub>();
                for(UserRuleParamIn userRuleParamIn: list)
                {
                    UserRule ur = new UserRule();
                    ur.setUserId(userRuleParamIn.getUserId());
                    ur.setUserCode(userRuleParamIn.getUserCode());
                    ResEmployeeVO curUser = ShiroUtils.getCurrentUser();
                    ur.setCreatedBy(curUser.getUsercode());
                    ur.setCreatedDate(new Date());
                    ur.setLastModifiedBy(curUser.getUsercode());
                    ur.setLastModifiedDate(new Date());
                    processrulesCfgMapper.insertUserRule(ur);
                    
                    if(CollectionUtils.isNotEmpty(userRuleParamIn.getSubList())){
                        for(UserRuleSubParamIn sub: userRuleParamIn.getSubList())
                        {
                            UserRuleSub urs = new UserRuleSub();
                            urs.setUserRuleId(new Long(ur.getId()));
                            urs.setRuleType(sub.getRuleType());
                            urs.setReasonNum(getReasonNum(sub.getReasonIds(), sub.getRuleType(), rejectList, returnList));
                            urs.setReasonId(sub.getReasonIds());
                            urs.setReasonCode(sub.getReasonCodes());
                            urs.setCreateTime(new Date());
                            urs.setUpdateTime(new Date());
                            subList.add(urs);
                        }
                    }
                }
                processrulesCfgMapper.batchInsertUserRuleSub(subList);
            }
        }
        catch(Exception e)
        {
            res.addMessage("新增失败");
            res.setType(Type.FAILURE);
            LOGGER.info(e.toString());
        }
        return res;
    }
    
    /**
     * 计算原因数量
     * 
     * @param reasonIds
     * @param ruleType
     * @param rejectList
     * @param returnList
     * @return
     * @author JiaCX
     * @date 2017年9月8日 下午2:30:53
     */
    private String getReasonNum(String reasonIds, String ruleType, List<ReqBMSTMReasonVO> rejectList, List<ReqBMSTMReasonVO> returnList)
    {
        List<String> rejectReasonA = new ArrayList<String>();//拒绝原因(没有二级原因)的一级原因id
        List<String> rejectReasonB = new ArrayList<String>();//拒绝原因二级原因id
        List<String> returnReasonA = new ArrayList<String>();//退回原因(没有二级原因的)一级原因id
        List<String> returnReasonB = new ArrayList<String>();//退回原因二级原因id
        
        if(CollectionUtils.isNotEmpty(rejectList) && CollectionUtils.isNotEmpty(rejectList.get(0).getChildren())){
            for(ReqBMSTMReasonVO reject: rejectList.get(0).getChildren()){
                if(CollectionUtils.isNotEmpty(reject.getChildren())){
                    for(ReqBMSTMReasonVO child: reject.getChildren()){
                        rejectReasonB.add(String.valueOf(child.getId()));
                    }
                }else{
                    rejectReasonA.add(String.valueOf(reject.getId()));
                }
            }
        }
        
        if(CollectionUtils.isNotEmpty(returnList) && CollectionUtils.isNotEmpty(returnList.get(0).getChildren())){
            for(ReqBMSTMReasonVO ret: returnList.get(0).getChildren()){
                if(CollectionUtils.isNotEmpty(ret.getChildren())){
                    for(ReqBMSTMReasonVO child: ret.getChildren()){
                        returnReasonB.add(String.valueOf(child.getId()));
                    }
                }else{
                    returnReasonA.add(String.valueOf(ret.getId()));
                }
            }
        }
        
        int num = 0;
        if("XSCS-REJECT".equalsIgnoreCase(ruleType)){
            List<String> rej = Arrays.asList(reasonIds.split(","));
            for(String str: rej){
                if(rejectReasonA.contains(str) || rejectReasonB.contains(str)){
                    num = num + 1;
                }
            }
            if(num == (rejectReasonA.size() + rejectReasonB.size())){
                return "ALL";
            }
        }else if("XSCS-RETURN".equalsIgnoreCase(ruleType)){
            List<String> rej = Arrays.asList(reasonIds.split(","));
            for(String str: rej){
                if(returnReasonA.contains(str) || returnReasonB.contains(str)){
                    num = num + 1;
                }
            }
            if(num == (returnReasonA.size() + returnReasonB.size())){
                return "ALL";
            }
        }else if("XSCS-PASS".equalsIgnoreCase(ruleType)){
            return "";
        }
        return String.valueOf(num);
    }

    @Override
    public Result<String> editUserRule(List<UserRuleParamIn> list)
    {
        /*
         * 1-更新主表的修改时间和修改人
         * 2-删除子表数据，重新插入
         */
        Result<String> res = new Result<String>(Type.SUCCESS);
        try
        {
            if(CollectionUtils.isNotEmpty(list)){
                List<ReqBMSTMReasonVO> rejectList = getAllReason("XSCS", "reject");
                List<ReqBMSTMReasonVO> returnList = getAllReason("XSCS", "return");
                for(UserRuleParamIn userRuleParamIn: list){
                    UserRule ur = new UserRule();
                    ur.setId(userRuleParamIn.getId());
                    ur.setLastModifiedBy(ShiroUtils.getCurrentUser().getUsercode());
                    ur.setLastModifiedDate(new Date());
                    processrulesCfgMapper.updateUserRule(ur);
                    
                    processrulesCfgMapper.deleteUserRuleSubByUserRuleId(userRuleParamIn.getId());
                    if(CollectionUtils.isNotEmpty(userRuleParamIn.getSubList())){
                        List<UserRuleSub> subList = new ArrayList<UserRuleSub>();
                        for(UserRuleSubParamIn sub: userRuleParamIn.getSubList())
                        {
                            UserRuleSub urs = new UserRuleSub();
                            urs.setUserRuleId(new Long(userRuleParamIn.getId()));
                            urs.setRuleType(sub.getRuleType());
                            urs.setReasonNum(getReasonNum(sub.getReasonIds(), sub.getRuleType(), rejectList, returnList));
                            urs.setReasonId(sub.getReasonIds());
                            urs.setReasonCode(sub.getReasonCodes());
                            urs.setCreateTime(new Date());
                            urs.setUpdateTime(new Date());
                            subList.add(urs);
                        }
                        processrulesCfgMapper.batchInsertUserRuleSub(subList);
                    }
                }
            }
        }
        catch(Exception e)
        {
            res.addMessage("修改失败");
            res.setType(Type.FAILURE);
            LOGGER.info(e.toString());
        }
        return res;
    }

    @Override
    public Result<String> deleteUserRule(Long id)
    {
        Result<String> res = new Result<String>(Type.SUCCESS);
        try{
            processrulesCfgMapper.deleteUserRuleByUserRuleId(id);
            processrulesCfgMapper.deleteUserRuleSubByUserRuleId(id);
        }catch(Exception e){
            res.addMessage("删除失败");
            res.setType(Type.FAILURE);
            LOGGER.info(e.toString());
        }
        return res;
    }

    @Override
    public List<UserRuleVO> getUserRuleByUserCode(String userCode)
    {
        List<String> list = new ArrayList<String>();
        list.add(userCode);
        return processrulesCfgMapper.getUserRuleList(list);
    }

    @Override
    public int ifAllCollected() throws Exception
    {
		ReqParamVO reqParamVO = new ReqParamVO();
		List<String> codes = new ArrayList<String>();
		codes.add(OrganizationEnum.OrgCode.CHECK.getCode());
		codes.add(OrganizationEnum.OrgCode.SALE_CHECK.getCode());
		reqParamVO.setLoginUser(ShiroUtils.getAccount());
		reqParamVO.setOrgTypeCodes(codes);
		reqParamVO.setRoleCode(RoleEnum.CHECK.getCode());
        List<ResOrganizationTreeVO> list = pmsApiService.getOrgTreeAndEmployees(reqParamVO);
        List<String> userCodes = new ArrayList<String>();
        getUserCodes(list, userCodes);
        List<String> collectedUsers = processrulesCfgMapper.getTokenBackUsers();
        if(CollectionUtils.isEmpty(userCodes)){
            return 1;//权限内没有初审员可以操作
        }
        
        if(CollectionUtils.isNotEmpty(collectedUsers)){
            if(collectedUsers.containsAll(userCodes)){
                return 2;//权限内所有初审员都被收回了权限
            }
        }
        return 0;//可以操作
    }

    @SuppressWarnings("unchecked")
    private void getUserCodes(List<ResOrganizationTreeVO> list, List<String> userCodes)
    {
        if(CollectionUtils.isNotEmpty(list)){
            for(ResOrganizationTreeVO treevo: list){
                if(treevo.getAttributes() != null){
                    Object obj = treevo.getAttributes();
                    Map<String,Object> map = (Map<String, Object>)obj;
                    if(null != map.get("usercode")){
                        userCodes.add(map.get("usercode").toString());
                    }
                }
                if(CollectionUtils.isNotEmpty(treevo.getChildren())){
                    getUserCodes(treevo.getChildren(), userCodes);
                }
            }
        }
    }

    @Override
    public List<String> getAllCollectedUserIds()
    {
        List<String> ids = new ArrayList<String>();
        
        // 根据工号和机构代码查询当前登录用户下级初审员列表
        List<String> userCodeList = new ArrayList<String>();
		ReqLevelVO reqLevelVO = new ReqLevelVO();
		reqLevelVO.setLevelType(OrganizationEnum.OrgCode.CHECK.getCode());
		reqLevelVO.setRoleCode(RoleEnum.CHECK.getCode());
        List<ResEmpOrgVO> empList = pmsApiService.getLowerEmpByAccount(reqLevelVO);
        if (CollectionUtils.isNotEmpty(empList)){
            for (ResEmpOrgVO reo : empList) {
                userCodeList.add(reo.getUsercode());
            }
        }
        
        if (CollectionUtils.isNotEmpty(userCodeList)) {
            List<UserRuleVO> list = new ArrayList<UserRuleVO>();
            list = processrulesCfgMapper.getUserRuleList(userCodeList);
            for (UserRuleVO ur : list) {
                ids.add(ur.getUserId());
            }
        }
        return ids;
    }
    
    /**
     * 获取所有拒绝或者退回原因
     * 
     * @param type
     * @param module
     * @return
     * @author JiaCX
     * @date 2017年9月8日 下午2:27:04
     */
    private List<ReqBMSTMReasonVO> getAllReason(String module, String type){
        ReqBMSTMReasonVO req = new ReqBMSTMReasonVO();
        req.setOperationType(type);
        req.setOperationModule(module);
        req.setSysCode(sysCode);
        return bmsLoanInfoServiceImpl.findReasonByOperType(req);
    }

}
