package com.yuminsoft.ams.system.controller.approve;

import com.alibaba.fastjson.JSON;
import com.ymkj.ams.api.vo.request.apply.ApplyEntryVO;
import com.ymkj.ams.api.vo.request.apply.ContactInfoVO;
import com.ymkj.ams.api.vo.request.apply.ReqContactInfoVO;
import com.ymkj.ams.api.vo.request.apply.ReqPersonalInformation;
import com.yuminsoft.ams.system.domain.approve.MobileHistory;
import com.yuminsoft.ams.system.domain.system.SysParamDefine;
import com.yuminsoft.ams.system.service.approve.MobileHistoryService;
import com.yuminsoft.ams.system.service.bds.BdsApiService;
import com.yuminsoft.ams.system.service.bms.BmsLoanInfoService;
import com.yuminsoft.ams.system.service.pms.PmsApiService;
import com.yuminsoft.ams.system.util.DateUtils;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.util.Result.Type;
import com.yuminsoft.ams.system.util.WebUtils;
import com.yuminsoft.ams.system.vo.firstApprove.CustomerContactInfoVO;
import com.yuminsoft.ams.system.vo.firstApprove.FirstTelephoneSummaryRelationInfoVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 初审 电核总汇
 */
@Controller
@RequestMapping("/firstTelephoneSummary")
public class FirstTelephoneSummaryController {
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FirstTelephoneSummaryController.class);
	@Autowired	
	private MobileHistoryService mobileHistoryService;
	@Autowired
	private BmsLoanInfoService bmsLoanInfoService;
	@Autowired
	private BdsApiService bdsApiService;
	@Autowired
	private PmsApiService pmsApiService;
	/**
	 * 保存新建联系人对话框
	 *
	 * @author luting
	 * @date 2017年2月16日 下午3:40:50
	 */
	@RequestMapping("/saveAddPeople/{loanNo}/{version}")
	@ResponseBody
	public Result<String> saveAddPeople(FirstTelephoneSummaryRelationInfoVo firstTelephoneSummaryRelationInfoVo, @PathVariable String loanNo, @PathVariable String version, HttpServletRequest request) {
		Result<String> result = bmsLoanInfoService.insertContactInfoService(firstTelephoneSummaryRelationInfoVo, loanNo, version);
		if (result.getSuccess()) {
			bmsLoanInfoService.getBMSLoanBasiceInfoByLoanNoService(request.getSession().getId(), loanNo, true); // 修改成功刷新redis
		}
		return result;
	}

	/**
	 * 修改联系人信息
	 *
	 * @author luting
	 * @date 2017年2月20日 上午10:14:58
	 */
	@RequestMapping("/updateRelationInfoList")
	@ResponseBody
	public Result<String> updateRelationInfoList(@RequestBody ReqContactInfoVO[] vo, HttpServletRequest request) {
		Result<String> result = bmsLoanInfoService.updateContactInfoService(vo);
		for (ReqContactInfoVO aVo : vo) {
			String loanNo = aVo.getLoanNo();
			if (result.getSuccess()) {
				bmsLoanInfoService.getBMSLoanBasiceInfoByLoanNoService(request.getSession().getId(), loanNo, true); // 修改成功刷新redis
			}
			if (StringUtils.isEmpty(loanNo)) {
				break;
			}
		}
		return result;
	}

	/**
	 * 电核汇总中修改用户基本信息
	 * 
	 * @author dmz
	 * @date 2017年6月16日
	 * @param vo
	 * @param request
	 * @return
	 */
	@RequestMapping("/updateBasicInfo")
	@ResponseBody
	public Result<String> updateBasicInfo(ReqPersonalInformation vo, HttpServletRequest request) {
		Result<String> result = new Result<String>(Type.FAILURE);
		try {
			vo.setIp(WebUtils.retrieveClientIp(request));
			result = bmsLoanInfoService.updatePersonalInformation(vo);
			// 重新保存借款基本信息
			if (result.success()) {
				bmsLoanInfoService.getBMSLoanBasiceInfoByLoanNoService(request.getSession().getId(), vo.getLoanNo(), result.success());
			}
		} catch (Exception e) {
			LOGGER.error("电核汇总中修改用户基本信息异常", e);
			result.addMessage("操作失败");
		}
		return result;
	}

	/**
	 * 新增电核记录
	 *
	 * @author luting
	 * @date 2017年2月23日 下午5:04:34
	 */
	@RequestMapping("/addTelephoneRecord")
	@ResponseBody
	public Result<MobileHistory> addTelephoneRecord(MobileHistory mobileHistory) {
		Result<MobileHistory> result = new Result<MobileHistory>(Type.SUCCESS);
		try {
			mobileHistoryService.save(mobileHistory);
		} catch (Exception e) {
			result.setType(Type.FAILURE);
			result.addMessage("新增电核记录异常");
			LOGGER.error("新增电核记录异常:{}", mobileHistory.getLoanNo(), e);
		}
		return result;
	}

	/**
	 * 第三方电话增加
	 */
	@RequestMapping("/addThirdPartTel")
	@ResponseBody
	public Result<MobileHistory> addThirdPartTel(MobileHistory mobileHistory, HttpServletRequest request) {
		LOGGER.info("=======第三方电话新增=========={}",JSON.toJSONString(mobileHistory));
		Result<MobileHistory> result = new Result<MobileHistory>(Type.SUCCESS);
		try {
			int count = mobileHistoryService.save(mobileHistory);
			if (count > 0) {
				//查询营业部信息
				result.setData(mobileHistory);
			} else {
				result.setType(Type.FAILURE);
				result.addMessage("添加失败！");
			}
		} catch (Exception e) {
			result.setType(Type.FAILURE);
			LOGGER.error("第三方电话增加异常:{}", mobileHistory.getLoanNo(), e);
		}
		return result;
	}

	/**
	 * 第三方电话/电核记录删除
	 * @param mobileHistryId 电话信息主键id
	 * @param loanNo 借款编号
	 * @return
	 */
	@RequestMapping("/deleteTelephoneRecord/{loanNo}")
	@ResponseBody
	public Result<String> deleteTelephoneRecord(Long mobileHistryId, @PathVariable String loanNo) {
		LOGGER.info("=====>第三方电话/电核记录删除:{}",loanNo);
		Result<String> result = new Result<String>(Type.SUCCESS);
		/** 删除电核记录 */
		try {
			mobileHistoryService.delete(mobileHistryId);
		} catch (Exception e) {
			result.setType(Type.FAILURE);
			LOGGER.error("电核信息删除异常:{}", loanNo, e);
		}
		/** 设置返回参数为成功 */
		
		return result;
	}

	/**
	 * 修改电核记录信息/第三方电话
	 * @param mobileHistory
	 * @return
	 * @author CWJ
	 */
	@RequestMapping("/updateTelephoneRecord")
	@ResponseBody
	public Result<MobileHistory> updateTelephoneRecord(MobileHistory mobileHistory) {
		LOGGER.info("========>修改电核记录信息/第三方电话:{},{}", mobileHistory.getLoanNo(), mobileHistory.getTelPhone());
		Result<MobileHistory> result = new Result<MobileHistory>(Type.SUCCESS);
		try {
			/** 更新电核记录 */
			mobileHistoryService.updateMobileHisById(mobileHistory);
			/** 设置返回参数为成功 */
			result.setData(mobileHistory);
		} catch (Exception e) {
			result.setType(Type.FAILURE);
			result.addMessage("修改失败");
			LOGGER.error("修改电核记录信息/第三方电话异常:{}", mobileHistory.getLoanNo(), e);
		}
		return result;
	}

	/**
	 * 修改联系人联系方式
	 */
	@RequestMapping("/updateRelationContactInfo")
	@ResponseBody
	public Result<String> updateRelationContactInfo(ReqContactInfoVO vo, HttpServletRequest request, String loanNo) {
		LOGGER.info("===========修改联系人联系方式：{}===========", vo.getLoanNo());
		vo.setLoanNo(loanNo);
		Result<String> result = bmsLoanInfoService.updateRelationContactInfo(vo);
		if (result.getSuccess()) {
			bmsLoanInfoService.getBMSLoanBasiceInfoByLoanNoService(request.getSession().getId(), loanNo, true); // 修改成功刷新redis
		}
		return result;
	}

	/**
	 * 获得第三方来源列表
	 */
	@RequestMapping("/getThridPartySource")
	@ResponseBody
	public List<SysParamDefine> getThridPartySource() {
		Map<String, Object> map = new HashMap<>();
		map.put("paramType", "thirdPartySource");
		List<SysParamDefine> vo = mobileHistoryService.getThridPartySource(map);
		return vo;
	}

	/**
	 * 返回新增电核记录页面
	 * 
	 * @author dongmingzhi
	 * @param
	 * @date 2017年1月4日(20171124优化修改电核记录)
	 * @return
	 */
	@RequestMapping("/addTelephoneRecordWin")
	public String addTelephoneRecordWin(MobileHistory mobileHistory, Model model, HttpSession session, HttpServletRequest request) {
		if (null != mobileHistory && null != mobileHistory.getId()) {
			mobileHistory = mobileHistoryService.findMobileHistoryById(mobileHistory.getId());
			model.addAttribute("title", "修改电核记录");
		} else{
			//内部匹配-->匹配号码-->新增电核记录 触发入口
			if (StringUtils.isEmpty(mobileHistory.getName())) {
				model.addAttribute("flag", "0");// 内匹添加电核记录标志
				//单位电话允许重复,内匹添加单位电话电核记录，需展示符合条件的多个联系人(与本次申请联系人对比)
				List<ContactInfoVO> list = bmsLoanInfoService.queryContactInfoByLoanNo(mobileHistory.getLoanNo(), mobileHistory.getTelPhone());
				model.addAttribute("contactList", list);
			}
			model.addAttribute("title", "新增电核记录");
			mobileHistory.setTelDate(DateUtils.dateToString(new Date(),DateUtils.FORMAT_DATA_YYYY_MM_DD_HH_MM_SS));
		}
		model.addAttribute("mobileHistory", mobileHistory);
		return "/approve/first/firstAddTelephoneRecord";
	}

	/**
	 * 修改联系人联系方式
	 */
	@RequestMapping("/updateCustomerContactInfo")
	@ResponseBody
	public Result<String> updateCustomerContactInfo(ContactInfoVO vo, HttpServletRequest request) {
		CustomerContactInfoVO customerContactInfoVO = new CustomerContactInfoVO();
		Result<String> result = new Result<String>(Result.Type.FAILURE);
		ApplyEntryVO applyInfo = bmsLoanInfoService.getBMSLoanInfoByLoanNoService(vo.getLoanNo());// 查询客户信息中的联系方式并赋值
		customerContactInfoVO.setLoanNo(vo.getLoanNo());
		customerContactInfoVO.setCellphone(applyInfo.getBasicInfoVO().getPersonInfoVO().getCellphone());
		customerContactInfoVO.setCellphoneSec(applyInfo.getBasicInfoVO().getPersonInfoVO().getCellphoneSec());
		customerContactInfoVO.setCorpPhone(applyInfo.getBasicInfoVO().getWorkInfoVO().getCorpPhone());
		customerContactInfoVO.setCorpPhoneSec(applyInfo.getBasicInfoVO().getWorkInfoVO().getCorpPhoneSec());
		if (null != vo.getContactCellPhone()) {// 页面传了哪个联系方式则修改已经赋值的联系方式
			customerContactInfoVO.setCellphone(vo.getContactCellPhone());
		} else if (null != vo.getContactCellPhone_1()) {
			if ("0".equals(vo.getContactCellPhone_1())) {// 备用手机可以删除
				customerContactInfoVO.setCellphoneSec("");
			} else {
				customerContactInfoVO.setCellphoneSec(vo.getContactCellPhone_1());
			}
		} else if (null != vo.getContactCorpPhone()) {
			customerContactInfoVO.setCorpPhone(vo.getContactCorpPhone());
		} else if (null != vo.getContactCorpPhone_1()) {
			if ("0".equals(vo.getContactCorpPhone_1())) {// 单电2可以删除
				customerContactInfoVO.setCorpPhoneSec("");
			} else {
				customerContactInfoVO.setCorpPhoneSec(vo.getContactCorpPhone_1());
			}
		}
		result = bmsLoanInfoService.updateCustomerContactInfo(customerContactInfoVO); // 修改成功
		return result;
	}
}
