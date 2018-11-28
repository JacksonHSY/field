package com.yuminsoft.ams.system.controller.sysservice;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ymkj.ams.api.vo.request.apply.ReqInformationVO;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.common.AmsConstants;
import com.yuminsoft.ams.system.controller.BaseController;
import com.yuminsoft.ams.system.service.bms.BmsLoanInfoService;
import com.yuminsoft.ams.system.service.creditzx.CreditzxService;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.util.Result.Type;
import com.yuminsoft.ams.system.util.Strings;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/***
 * 征信接口实现
 *
 * @author dmz
 * @date 2017年2月24日
 */
@Controller
@RequestMapping("/creditzx")
public class CreditzxController extends BaseController {

	@Autowired
	private CreditzxService creditzxService;

	@Autowired
	private BmsLoanInfoService bmsLoanInfoService;

	/**
	 * 学历查询
	 * @author dmz
	 * @date 2017年2月24日
	 * @param loanNo
	 * @param session
	 * @return
	 */
	@RequestMapping("/getEducationInfo")
	@ResponseBody
	@Deprecated
	public Result<JSONArray> getEducationInfo(String loanNo, HttpSession session) {
		ReqInformationVO applyBasiceInfo = bmsLoanInfoService.getBMSLoanBasiceInfoByLoanNoService(session.getId(), loanNo, false);
		Result<JSONArray> result = new Result<JSONArray>(Type.FAILURE);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("idCard", applyBasiceInfo.getIdNo());
		map.put("name", applyBasiceInfo.getName());
		map.put("isCheck", true);
		map.put("creatorId", ShiroUtils.getAccount());
		map.put("timestamp", String.valueOf(System.currentTimeMillis()));
		map.put("appNo", loanNo);
		try {
			result = creditzxService.getEducationInfoService(map);
		} catch (Exception e) {
			LOGGER.info("学历查询异常:{}", e);
			result.addMessage("查询异常");
		}
		return result;
	}

	/**
	 * 车辆信息查询
	 *
	 * @author dmz
	 * @date 2017年2月24日
	 * @param loanNo
	 * @param session
	 * @return
	 */
	@RequestMapping("/getCarInfo")
	@ResponseBody
	@Deprecated
	public Result<JSONArray> getCarInfo(String loanNo, HttpSession session) {
		Result<JSONArray> result = new Result<JSONArray>(Type.FAILURE);
		ReqInformationVO applyBasiceInfo = bmsLoanInfoService.getBMSLoanBasiceInfoByLoanNoService(session.getId(), loanNo, false);
		// 牌照 TODO
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("idCard", applyBasiceInfo.getIdNo());
		map.put("name", applyBasiceInfo.getName());
		map.put("isCheck", true);
		map.put("carNumber", "京L88999");// 车牌号
		map.put("creatorId", "00222223");
		map.put("timestamp", String.valueOf(System.currentTimeMillis()));
		map.put("appNo", loanNo);
		try {
			result = creditzxService.getCarInfoService(map);
		} catch (Exception e) {
			LOGGER.info("车辆信息查询异常:{}", e);
			result.addMessage("查询异常");
		}
		return result;
	}

	/**
	 * 上海资信
	 *
	 * @author dmz
	 * @date 2017年2月28日
	 * @return
	 */
	@RequestMapping("/queryCreditById/{loanNo}")
	@ResponseBody
	public Result<JSONObject> queryCreditById(@PathVariable String loanNo, HttpSession session) {
		Result<JSONObject> result = new Result<JSONObject>(Type.FAILURE);
		// 不能再改false了，报告ID是查询最新的
		ReqInformationVO applyBasicInfo = bmsLoanInfoService.getBMSLoanBasiceInfoByLoanNoService(session.getId(), loanNo, true);
		if (StringUtils.isNotEmpty(applyBasicInfo.getNfcsId())) {
			try {
				result = creditzxService.getNfcsCreditById(loanNo, applyBasicInfo.getNfcsId());
			} catch (Exception e) {
				LOGGER.error("上海资信查询异常", e);
				result.addMessage("查询异常");
			}
		} else {
			result.addMessage("无相关记录");
		}

		return result;
	}


	/**
	 * 获取征信初判信息
	 * @param type 1:央行征信，2：征信初判(需要查贷款记录)
	 * @param loanNo
	 * @param session
	 */
	@RequestMapping("/getCreditJudgment/{type}/{loanNo}")
	@ResponseBody
	public Result<JSONArray> getCreditJudgment(@PathVariable String type, @PathVariable String loanNo, HttpSession session) {
		Result<JSONArray> result = new Result<JSONArray>(Type.FAILURE);
		ReqInformationVO applyBasiceInfo = bmsLoanInfoService.getBMSLoanBasiceInfoByLoanNoService(session.getId(), loanNo, true);//不能再改false了，报告ID是查询最新的
		Result<JSONObject> reportInfo = new Result<JSONObject>();// 央行报告
		Result<JSONObject> loanLimitInfo = new Result<JSONObject>();// 自动填充
		try {
			// 调用央行征信
			JSONArray resultArray = new JSONArray();
			reportInfo = creditzxService.getCreditReportInfoService(applyBasiceInfo);// 信用卡报告
			if (reportInfo.getData() == null) {
				resultArray.add(reportInfo);
			} else {
				resultArray.add(reportInfo.getData());
			}
			if("2".equals(type)) {
				loanLimitInfo = creditzxService.getLoanInfoService(applyBasiceInfo);// 贷款记录
				if (loanLimitInfo.getData() == null) {
					resultArray.add(loanLimitInfo);
				} else {
					resultArray.add(loanLimitInfo.getData());
				}
			}
			result.setData(resultArray);
			result.setType(Type.SUCCESS);

		} catch (Exception e) {
			LOGGER.info("获取征信初判信息查询异常:", e);
			result.addMessage("查询异常");
		}
		return result;
	}

	/**
	 * 返回算话征信页面
	 * @param loanNo
	 * @return
	 */
	@RequestMapping("/getSuanHuaCreditWindow/{loanNo}")
	public String getSuanHuaCreditWindow(@PathVariable String loanNo, Model model,HttpSession session) {
		ReqInformationVO applyBasicInfo = bmsLoanInfoService.getBMSLoanBasiceInfoByLoanNoService(session.getId(), loanNo, false);
		model.addAttribute("loanNo",loanNo);
		model.addAttribute("name",applyBasicInfo.getName());
		return "/approve/first/suanHuaCredit";
	}

	/**
	 * 算话征信
	 * @return
	 */
	@RequestMapping("/getSuanHuaCredit/{loanNo}")
	@ResponseBody
	public Result<JSONObject> getSuanHuaCredit(@PathVariable String loanNo, HttpSession session) {
		ReqInformationVO applyBasiceInfo = bmsLoanInfoService.getBMSLoanBasiceInfoByLoanNoService(session.getId(), loanNo, true);
		Result<JSONObject> result = new Result<JSONObject>(Type.FAILURE);
		if (Strings.isNotEmpty(applyBasiceInfo.getShReportId())) {
			try {
				result = creditzxService.getSuanHuaCredit(applyBasiceInfo);
			} catch (Exception e) {
				LOGGER.error("算话征信调用异常:", e);
				result.addMessage(AmsConstants.DEFAULT_ERROR_MESSAGE);
			}
		}else {
			result.addMessage("查无结果");
		}
		return result;
	}

}
