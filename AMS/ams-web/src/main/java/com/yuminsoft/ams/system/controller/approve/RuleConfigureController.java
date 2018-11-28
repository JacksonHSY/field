package com.yuminsoft.ams.system.controller.approve;

import com.ymkj.pms.biz.api.enums.OrganizationEnum;
import com.ymkj.pms.biz.api.service.IEmployeeExecuter;
import com.ymkj.pms.biz.api.vo.request.ReqParamVO;
import com.ymkj.pms.biz.api.vo.response.ResGroupVO;
import com.yuminsoft.ams.system.annotation.UserLogs;
import com.yuminsoft.ams.system.domain.approve.AgenLeader;
import com.yuminsoft.ams.system.service.approve.RuleConfigureService;
import com.yuminsoft.ams.system.service.pms.PmsApiService;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.util.Result.Type;
import com.yuminsoft.ams.system.vo.apply.GroupVO;
import com.yuminsoft.ams.system.vo.apply.UserRuleParamIn;
import com.yuminsoft.ams.system.vo.apply.UserRuleVO;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/ruleconfig")
public class RuleConfigureController {
	private static final Logger LOGGER = LoggerFactory.getLogger(RuleConfigureController.class);

	@Value("${sys.code}")
	public String sysCode;

	@Autowired
	public RuleConfigureService ruleConfigureServiceImpl;

	@Autowired
	public IEmployeeExecuter iEmployeeExecuter;

	@Autowired
	private PmsApiService pmsApiService;

	@RequestMapping("/groupLeaderReview")
	public String groupLeaderReview() {
		return "/apply/ruleConfigReview";
	}

	/**
	 * 返回当前登录人所在组下，组长及组员列表和所有初审组长
	 * 
	 * @return
	 */
	@RequestMapping("/agentLeaderReview")
	public String agentLeaderReview(Model model) {
		List<ResGroupVO> list = pmsApiService.getGroupsByAccount();
		if(!list.isEmpty()) {
			// 回显员工的代理组长
			List<GroupVO> groupList = ruleConfigureServiceImpl.combineEmployee(list);
			model.addAttribute("agentlist", groupList);
		}
		model.addAttribute("leaderList", pmsApiService.getAllFirstApprovalLeaders());
		return "/apply/agentLerader_Dialog";
	}

	/**
	 * 代理组长保存
	 */
	@RequestMapping("/saveProxyUser")
	@ResponseBody
	@UserLogs(link = "规则配置", operation = "代理组长保存")
	public Result<String> saveProxyUser(@RequestBody List<AgenLeader> list) {
		List<String> message = new ArrayList<String>();
		Result<String> result = new Result<String>();
		for (AgenLeader agenLeader : list) {
			try {
				if (ruleConfigureServiceImpl.saveProxyUser(agenLeader) != 1) {
					message.add("为" + agenLeader.getUserCode() + "设置代理组长失败");
				}
			} catch (Exception e) {
				LOGGER.error("为" + agenLeader.getUserCode() + "设置代理组长失败", e);
				message.add("为" + agenLeader.getUserCode() + "设置代理组长失败");
			}
		}
		if (message.size() == list.size()) {
			result.setType(Type.FAILURE);// 全部都设置失败的时候才认为代理组长设置失败
		} else if (message.size() == 0) {
			result.setType(Type.SUCCESS);
			result.addMessage("设置成功");
		} else {
			result.setType(Type.SUCCESS);
		}
		if (message.size() > 0) {
			for (String string : message) {
				result.addMessage(string);
			}
		}
		return result;

	}
	
	/**
	 * 获取规则配置中被收回权限的用户规则列表
	 * 
	 * @param requestPage
	 * @return
	 * @author JiaCX
	 * @date 2017年9月4日 下午5:42:17
	 */
	@RequestMapping("/userRuleList")
	public @ResponseBody ResponsePage<UserRuleVO> getUserRuleList(RequestPage requestPage){
	    return ruleConfigureServiceImpl.getUserRuleList(requestPage);
	}
	
	/**
	 * 跳转到新增收回权限页面
	 * 
	 * @return
	 * @author JiaCX
	 * @date 2017年9月6日 下午3:09:20
	 */
	@RequestMapping("/addUserRuleWindow")
    public String addUserRuleWindow() {
        return "/apply/addUserRuleWindow";
    }
	
	/**
	 * 新增收回权限
	 * 
	 * @param list
	 * @return
	 * @author JiaCX
	 * @date 2017年9月5日 上午10:14:01
	 */
	@RequestMapping("/addUserRule")
	public @ResponseBody Result<String> addUserRule(@RequestBody List<UserRuleParamIn> list){
	    return ruleConfigureServiceImpl.addUserRule(list);
	}
	
	/**
	 * 跳转到修改回权限页面
	 * 
	 * @return
	 * @author JiaCX
	 * @date 2017年9月7日 上午9:11:09
	 */
	@RequestMapping("/editUserRuleWindow/{userCode}")
    public String editUserRuleWindow(@PathVariable String userCode, Model model) {
		ReqParamVO reqParamVO = new ReqParamVO();
		ResGroupVO vo = null;
		reqParamVO.setLoginUser(userCode);
		reqParamVO.setOrgTypeCode(OrganizationEnum.OrgCode.CHECK.getCode());
		Result<ResGroupVO> result= pmsApiService.getGroupInfoByAccount(reqParamVO);
		if(null != result){
			vo = result.getData();
		}
		model.addAttribute("group", vo);
	    model.addAttribute("userInfo", pmsApiService.findEmpByUserCode(userCode));
        return "/apply/editUserRuleWindow";
    }
	
	/**
	 * 修改收回权限
	 * 
	 * @param list
	 * @return
	 * @author JiaCX
	 * @date 2017年9月5日 上午10:16:10
	 */
	@RequestMapping("/editUserRule")
	public @ResponseBody Result<String> editUserRule(@RequestBody List<UserRuleParamIn> list){
	    return ruleConfigureServiceImpl.editUserRule(list);
	}
	
	/**
	 * 删除某用户被收回的权限
	 * 
	 * @param id
	 * @return
	 * @author JiaCX
	 * @date 2017年9月5日 上午10:18:58
	 */
	@RequestMapping("/deleteUserRule/{id}")
	public @ResponseBody Result<String> deleteUserRule(@PathVariable Long id){
	    return ruleConfigureServiceImpl.deleteUserRule(id);
	}
	
	/**
	 * 根据userCode获取该用户被收回的权限信息
	 * 
	 * @param userCode
	 * @return
	 * @author JiaCX
	 * @date 2017年9月7日 上午10:32:58
	 */
	@RequestMapping("/getUserRuleByUserCode/{userCode}")
	public @ResponseBody List<UserRuleVO> getUserRuleByUserCode(@PathVariable String userCode){
	    return ruleConfigureServiceImpl.getUserRuleByUserCode(userCode);
	}
	
	/**
	 * 判断辖下初审员是否均已存在记录
	 * 0 可以操作
	 * 1 权限内没有初审员可以操作
	 * 2 权限内所有初审员都被收回了权限
	 * 
	 * @author JiaCX
	 * @throws Exception 
	 * @date 2017年9月8日 上午10:39:28
	 */
	@RequestMapping("/ifAllCollected")
	public @ResponseBody int ifAllCollected() throws Exception{
	    return ruleConfigureServiceImpl.ifAllCollected();
	}
	
	/**
	 * 获取当前登录人权限内被收回权限的用户id
	 * 
	 * @return
	 * @author JiaCX
	 * @date 2017年9月8日 下午12:46:21
	 */
	@RequestMapping("/getAllCollectedUserIds")
	public @ResponseBody List<String> getAllCollectedUserIds(){
	    return ruleConfigureServiceImpl.getAllCollectedUserIds();
	}
}
