package com.yuminsoft.ams.system.service.creditzx.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.StringUtil;
import com.ymkj.ams.api.vo.request.apply.ReqInformationVO;
import com.ymkj.ams.api.vo.response.audit.ResBMSAduitPersonVo;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.common.EnumUtils;
import com.yuminsoft.ams.system.common.PhoneUtil;
import com.yuminsoft.ams.system.service.bms.BmsLoanInfoService;
import com.yuminsoft.ams.system.service.creditzx.CreditzxService;
import com.yuminsoft.ams.system.util.HttpUtils;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.util.Result.Type;
import com.yuminsoft.ams.system.util.Strings;
import com.yuminsoft.ams.system.vo.creditzx.huazheng.HzRequest;
import com.yuminsoft.ams.system.vo.creditzx.huazheng.HzResponse;
import com.yuminsoft.ams.system.vo.creditzx.huazheng.IdCardCheckInfoVo;
import com.yuminsoft.ams.system.vo.creditzx.huazheng.MobileOnlineInfoVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.HashMap;
import java.util.Map;

@Service
public class CreditzxServiceImpl implements CreditzxService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CreditzxServiceImpl.class);

	@Autowired
	private BmsLoanInfoService bmsLoanInfoService;

	@Value("${sys.houseFeeRate:0.00408}")
	private Double houseFeeRate; // 房贷费率
	@Value("${sys.carFeeRate:0.00625}")
	private Double carFeeRate;   // 车贷费率

	@Value("${sys.credit.zx}")
	private String sysCreditZX;

	@Value("${sys.code}")
	private String sysCode;

	/**
	 * 信用不良查询
	 * 备注:只有当放回000000是data才会有值,其他情况data为空
	 * @author dmz
	 * @date 2017年3月28日
	 * @param reqInformationVO
	 * @return
	 */
	@Override
	public Result<JSONObject> getCreditReportInfoService(ReqInformationVO reqInformationVO) {
		StopWatch stopWatch = new StopWatch();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("customerIdCard", reqInformationVO.getIdNo());				// 省份证"310111111111111112"
		map.put("customerName", reqInformationVO.getName());					// 姓名"测试人员"
		map.put("reportId", reqInformationVO.getReportId());					// 报告id"100033"
		map.put("queryDate", reqInformationVO.getAuditEndTime());				// 首次录入复核时间newDate()
		map.put("timestamp", String.valueOf(System.currentTimeMillis()));
		map.put("appNo", reqInformationVO.getLoanNo());
		Result<JSONObject> result = new Result<JSONObject>();
		Map<String, String> params = new HashMap<String, String>();
		String mapStr = JSON.toJSONString(map);
		params.put("param", mapStr);
		stopWatch.start("信用不良查询");
		String resultStr = HttpUtils.doPost(sysCreditZX + "creditReport/getCreditReportInfo", params, null);
		stopWatch.stop();
		LOGGER.info("信用不良查询  params:{} result:{}", mapStr, resultStr);
		stopWatch.start("信用不良查询结果解析");
		if (null != resultStr) {
			JSONObject json = JSONObject.parseObject(resultStr);
			if (json.containsKey("code")) { // 判断code 是否存在
				String code = json.getString("code"); // 结果代码:000000为有记录;000001未查询到记录;000002报告过期;000003 请求时间与当前时间相差过长;999999缺少参数或异常
				if (null != code && EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(code)) {
					Integer alreadyUseMoney = (Integer) json.get("alreadyUseMoney"); // 已用额度
					if (alreadyUseMoney != null) { // 计算信用卡负债信息
						Float f = Float.valueOf(alreadyUseMoney) / 10;
						json.put("debt", Math.round(f));// 保存信用卡负债
					}
					result.setType(Type.SUCCESS);
					result.setData(json);
				}  else {
					result.addMessage(json.get("messages").toString());
				}
			} else {
				result.addMessage("信用不良查询异常未找到code代码");
			}
		} else {
			result.addMessage("信用不良查询异常");
		}
		stopWatch.stop();
		LOGGER.info(stopWatch.prettyPrint());
		return result;
	}

	/**
	 * 信用贷款自动填充
	 *
	 * @author dmz
	 * @date 2017年3月28日
	 * @param map
	 * @return
	 */
	@Override
	public Result<JSONArray> getLoanLimitInfoService(Map<String, Object> map) {
		StopWatch stopWatch = new StopWatch();
		Result<JSONArray> result = new Result<JSONArray>(Type.FAILURE);
		Map<String, String> params = new HashMap<String, String>();
		String mapStr = JSON.toJSONString(map);
		params.put("param", mapStr);
		stopWatch.start("贷款自动填充");
		String debt = HttpUtils.doPost(sysCreditZX + "creditReport/getLoanLimitInfo", params, null);
		stopWatch.stop();
		LOGGER.info("贷款自动填充 params:{} result:{}", mapStr, debt);
		stopWatch.start("贷款自动填充解析");
		if (null != debt) {
			JSONObject json = JSONObject.parseObject(debt);
			if (json.containsKey("code")) { // 判断code
				String code = json.getString("code");//000000正常返回结果;000001未查询到记录;000002有报告无贷款记录;999999缺少参数或异常
				if (null != code && EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(code)) {
					String loan = json.getString("loan");
					JSONArray debtArray = JSONArray.parseArray(loan);
					for (int i = 0; i < debtArray.size(); i++) {
						JSONObject job = debtArray.getJSONObject(i);
						if (null != job.get("grantMoney") && null != job.get("repayPeriods")) {
							double grantMoney = job.getDoubleValue("grantMoney");// 额度
							int repayPeriods = job.getIntValue("repayPeriods");// 期限
							if (grantMoney > 0 && repayPeriods > 0) {
								// 额度/期限 + 额度 * 2.3%，四舍五入保留两位小数，额度和期限填写完成时，自动计算
								int debtMoney = (int) (grantMoney / repayPeriods + grantMoney * 0.023);
								job.put("debtMoney", debtMoney);
							}
						}
					}
					result.setType(Type.SUCCESS);
					result.setData(debtArray);
				} else {
					result.addMessage(json.get("messages").toString());
				}
			} else {
				result.addMessage("信用不良查询异常未找到code代码");
			}
		} else {
			result.addMessage("信用不良查询异常");
		}
		stopWatch.stop();
		LOGGER.info(stopWatch.prettyPrint());
		return result;
	}

	/**
	 * 其他贷款自动填充(信用贷款、房贷、车贷、其他)
	 * 注意:其他贷款自动填充中的信用贷款不要使用，请重新调用信用贷款自动填充接口
	 * @author dmz
	 * @date 2017年7月21日
	 * @param reqInformationVO
	 * @return
	 */
	@Override
	public Result<JSONObject> getLoanInfoService(ReqInformationVO reqInformationVO) {
		StopWatch stopWatch = new StopWatch();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("customerIdCard", reqInformationVO.getIdNo());				// 省份证"310111111111111112"
		map.put("customerName", reqInformationVO.getName());					// 姓名"测试人员"
		map.put("reportId", reqInformationVO.getReportId());					// 报告id"100033"
		map.put("queryDate", reqInformationVO.getAuditEndTime());				// 首次录入复核时间newDate()
		map.put("timestamp", String.valueOf(System.currentTimeMillis()));
		map.put("appNo", reqInformationVO.getLoanNo());
		Result<JSONObject> result = new Result<JSONObject>(Type.FAILURE);
		Map<String, String> params = new HashMap<String, String>();
		String mapStr = JSON.toJSONString(map);
		params.put("param", mapStr);
		stopWatch.start("其他贷款自动填充(信用贷款、房贷、车贷、其他)");
		String debt = HttpUtils.doPost(sysCreditZX + "creditReport/getLoanInfo", params, null);
		stopWatch.stop();
		LOGGER.info("其他贷款自动填充(信用贷款、房贷、车贷、其他) params:{} result:{}", mapStr, debt);
		stopWatch.start("其他贷款自动填充(信用贷款、房贷、车贷、其他)");
		if (null != debt) {
			JSONObject json = JSONObject.parseObject(debt);
			if (json.containsKey("code")) { // 判断code
				String code = json.getString("code");//000000正常返回结果;000001未查询到记录;000002有报告无贷款记录;999999缺少参数或异常
				if (null != code && EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(code)) {
					String data = json.getString("data");
					JSONObject debtArray = JSONObject.parseObject(data);
					// TODO 临时解决计算房贷和车贷
					// 房贷
					JSONArray houseArray = debtArray.getJSONArray("house");
					for (int i = 0; i < houseArray.size(); i++) {
						JSONObject houseObj = houseArray.getJSONObject(i);
						if (null != houseObj.get("grantMoney") && null != houseObj.get("repayPeriods")) {
							double grantMoney = houseObj.getDoubleValue("grantMoney");// 额度
							int repayPeriods = houseObj.getIntValue("repayPeriods");// 期限
							if (grantMoney > 0 && repayPeriods > 0) {
								// 负债 = 额度/期限 + 额度*费率，四舍五入保留两位小数，额度和期限填写完成时，自动计算
								int debtMoney = (int) (grantMoney / repayPeriods + grantMoney * houseFeeRate);
								houseObj.put("debtMoney", debtMoney);
							} else {
								houseObj.put("debtMoney", "");
							}
						}
					}
					// 车贷
					JSONArray carArray = debtArray.getJSONArray("car");
					for (int i = 0; i < carArray.size(); i++) {
						JSONObject carObj = carArray.getJSONObject(i);
						if (null != carObj.get("grantMoney") && null != carObj.get("repayPeriods")) {
							double grantMoney = carObj.getDoubleValue("grantMoney");// 额度
							int repayPeriods = carObj.getIntValue("repayPeriods");// 期限
							if (grantMoney > 0 && repayPeriods > 0) {
								// 负债 = 额度/期限 + 额度*费率，四舍五入保留两位小数，额度和期限填写完成时，自动计算
								int debtMoney = (int) (grantMoney / repayPeriods + grantMoney * carFeeRate);
								carObj.put("debtMoney", debtMoney);
							} else {
								carObj.put("debtMoney", "");
							}
						}
					}
					result.setType(Type.SUCCESS);
					result.setData(debtArray);
				}else {
					result.addMessage(json.get("messages").toString());
				}
			} else {
				result.addMessage("其他贷款自动填充(信用贷款、房贷、车贷、其他)查询异常");
			}
		} else {
			result.addMessage("其他贷款自动填充(信用贷款、房贷、车贷、其他)查询异常");
		}
		// 调用信用贷款
		Result<JSONArray> resultArray = getLoanLimitInfoService(map);
		if (resultArray.success()) {
			if (null != result.getData()){
					result.getData().put("loan", resultArray.getData());
			} else {
				JSONObject jsonObjectAdd = new JSONObject();
				jsonObjectAdd.put("loan",resultArray.getData());
				result.setData(jsonObjectAdd);
			}
		}
		stopWatch.stop();
		LOGGER.info(stopWatch.prettyPrint());
		return result;
	}

	/**
	 * 查询手机在网时长
	 * @param hzRequest
	 * @author dmz
	 * @date 2017年2月23日
	 */
	@Override
	public HzResponse<MobileOnlineInfoVo> getMobileOnlineInfoService(HzRequest hzRequest) {
		StopWatch stopWatch = new StopWatch();
		HzResponse<MobileOnlineInfoVo> hzResponse = new HzResponse<MobileOnlineInfoVo>();
		//hzRequest.setMobileService(getMobileType(hzRequest.getMobile()).getCode());	// 手机运营商:1：移动，2：联通，3：电信
		hzRequest.setMobileService(PhoneUtil.getCarrierType(hzRequest.getMobile()));
		Map<String, String> params = new HashMap<String, String>();
		params.put("param", JSON.toJSONString(hzRequest));
		stopWatch.start("查询手机在网时长信息");
		String result = HttpUtils.doPost(sysCreditZX + "hz/getMobileOnlineInfo", params, null);	// 调用征信系统获取在网时长信息
		stopWatch.stop();
		LOGGER.info("查询手机在网时长信息  params:{} result:{}", JSON.toJSONString(hzRequest), result);
		JSONObject creditZxJson = JSONObject.parseObject(result);
		hzResponse.setCode(creditZxJson.getString("code"));
		hzResponse.setMessages(creditZxJson.getString("messages"));
		if(hzResponse.success()){
			JSONObject creditZxData = creditZxJson.getJSONArray("result").getJSONObject(0);
			hzResponse.setMetaCode(creditZxData.getString("metaCode"));
			hzResponse.setStatus(creditZxData.getInteger("status"));
			MobileOnlineInfoVo data = new MobileOnlineInfoVo();
			data.setId(creditZxJson.getLong("id"));
			data.setAppNo(creditZxJson.getString("appNo"));
			data.setIdCard(creditZxJson.getString("idCard"));
			data.setName(creditZxJson.getString("name"));
			data.setMobile(creditZxJson.getString("mobile"));
			data.setMobileService(creditZxJson.getInteger("mobileService"));
			data.setCreatorId(creditZxJson.getString("creatorId"));
			data.setTimestamp(creditZxJson.getLong("timestamp"));
			data.setIsCheck(creditZxJson.getBoolean("isCheck"));
			data.setTimes(creditZxData.getJSONArray("data").getJSONObject(0).getString("times"));
			hzResponse.setData(data);
		}
		LOGGER.info(stopWatch.prettyPrint());
		return hzResponse;
	}

	/**
	 * 查询手机三要素实名认证信息
	 * @param hzRequest
	 * @author dmz
	 * @date 2017年2月23日
	 * @return-Map<String,Object>
	 */
	@Override
	public HzResponse<IdCardCheckInfoVo> getIdCardCheckInfo(HzRequest hzRequest) {
		HzResponse<IdCardCheckInfoVo> hzResponse = new HzResponse<IdCardCheckInfoVo>();
		StopWatch stopWatch = new StopWatch();
		//hzRequest.setMobileService(getMobileType(hzRequest.getMobile()).getCode());
		hzRequest.setMobileService(PhoneUtil.getCarrierType(hzRequest.getMobile()));
		Map<String, String> params = new HashMap<String, String>();
		params.put("param", JSON.toJSONString(hzRequest));
		stopWatch.start("查询手机实名认证");
		String result = HttpUtils.doPost(sysCreditZX + "hz/getIdCardCheckInfo", params, null);	// 调用征信系统获取实名认证信息
		stopWatch.stop();
		LOGGER.info("查询手机实名认证  params:{} result:{}", JSON.toJSONString(hzRequest), result);

		JSONObject creditZxJson = JSONObject.parseObject(result);
		hzResponse.setCode(creditZxJson.getString("code"));
		hzResponse.setMessages(creditZxJson.getString("messages"));
		if(hzResponse.success()){
			JSONObject creditZxData = creditZxJson.getJSONArray("result").getJSONObject(0);

			hzResponse.setMetaCode(creditZxData.getString("metaCode"));
			hzResponse.setStatus(creditZxData.getInteger("status"));

			IdCardCheckInfoVo data = new IdCardCheckInfoVo();
			data.setId(creditZxJson.getLong("id"));
			data.setAppNo(creditZxJson.getString("appNo"));
			data.setIdCard(creditZxJson.getString("idCard"));
			data.setName(creditZxJson.getString("name"));
			data.setMobile(creditZxJson.getString("mobile"));
			data.setCreatorId(creditZxJson.getString("creatorId"));
			data.setTimestamp(creditZxJson.getLong("timestamp"));
			data.setIsCheck(creditZxJson.getBoolean("isCheck"));
			data.setSucceed(creditZxData.getJSONArray("data").getJSONObject(0).getString("succeed"));
			data.setMsg(creditZxData.getJSONArray("data").getJSONObject(0).getString("msg"));
			hzResponse.setData(data);
		}
		LOGGER.info(stopWatch.prettyPrint());
		return hzResponse;
	}

	/**
	 * 学历查询
	 *
	 * @author dmz
	 * @date 2017年2月24日
	 * @param map
	 * @return
	 */
	@Override
	@Deprecated
	public Result<JSONArray> getEducationInfoService(Map<String, Object> map) {
		Map<String, String> params = new HashMap<String, String>();
		String mapStr = JSON.toJSONString(map);
		params.put("param", mapStr);
		String result = HttpUtils.doPost(sysCreditZX + "hz/getEducationInfo", params, null);
		LOGGER.info("调用征信查询学历信息 params:{}", mapStr);
		return getStringToMap(result, "data");
	}

	/**
	 * 车辆信息查询
	 *
	 * @author dmz
	 * @date 2017年2月24日
	 * @param map
	 * @return
	 */
	@Override
	public Result<JSONArray> getCarInfoService(Map<String, Object> map) {
		StopWatch stopWatch = new StopWatch();
		Map<String, String> params = new HashMap<String, String>();
		String mapStr = JSON.toJSONString(map);
		params.put("param", mapStr);
		stopWatch.start("车辆信息查询 ");
		String result = HttpUtils.doPost(sysCreditZX + "hz/getCarInfo", params, null);
		stopWatch.stop();
		LOGGER.info("车辆信息查询  params:{} result:{}", mapStr, result);
		LOGGER.info(stopWatch.prettyPrint());
		return getStringToMap(result, "data");
	}

	/**
	 * 上海资信
	 * @param loanNo 借款编号
	 * @param nfcsId 上海资信报告ID
	 * @author dmz
	 * @date 2017年2月28日
	 * @return
	 */
	@Override
	public Result<JSONObject> getNfcsCreditById(String loanNo, String nfcsId) {
		StopWatch stopWatch = new StopWatch();
		Result<JSONObject> result = new Result<JSONObject>(Type.FAILURE);
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", nfcsId);
		map.put("sources", sysCode);
		map.put("appNo", loanNo);

		String resultStr = HttpUtils.doPost(sysCreditZX + "shCredit/queryCreditById", map, null);
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("上海资信信息查询 params:{} result:{}", JSON.toJSONString(map), resultStr);
		}

		if (null != resultStr) {
			JSONObject json = JSONObject.parseObject(resultStr);
			if (json != null && json.containsKey("code")) {
				String code = json.getString("code");
				if (null != code && EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(code)) {
					result.setType(Type.SUCCESS);
					JSONObject data = JSONObject.parseObject(json.getString("responseJson"));
					data.getJSONObject("个人身份信息").put("证件号码", data.getJSONObject("个人身份信息").get("证件号码").toString());
					result.setData(data);
				} else {
					result.addMessage("未找到记录");
				}
			} else {
				result.addMessage("未找到记录");
			}

		} else {
			result.addMessage("未找到记录");
		}
		LOGGER.info(stopWatch.prettyPrint());
		return result;
	}

	/**
	 * 字符转map
	 *
	 * @author dmz
	 * @date 2017年2月24日
	 * @param resultStr
	 * @param dataStr
	 * @return
	 */
	@Deprecated
	private Result<JSONArray> getStringToMap(String resultStr, String dataStr) {
		Result<JSONArray> result = new Result<JSONArray>(Type.FAILURE);
		if (null != resultStr) {
			JSONObject json = JSONObject.parseObject(resultStr);
			// 000000为有记录;000001未查询到记录;000003 请求时间与当前时间相差过长;999999缺少参数或异常
			if (json != null && json.containsKey("code")) {
				String code = json.getString("code");
				if (null != code && EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(code)) {
					result.setType(Type.SUCCESS);
					String resultJSON = json.getString("result");
					JSONObject resultObject = JSONObject.parseArray(resultJSON).getJSONObject(0);
					String status = resultObject.getString("status");
					// 0：查询成功无数据、1：查询成功有数据、10：查询失败
					if ("1".equals(status)) {
						result.setData(JSONObject.parseArray(resultObject.getString(dataStr)));
					} else {
						LOGGER.info("查询数据状态 :{},对应描述:{0：查询成功无数据、1：查询成功有数据、10：查询失败}", status);
						result.addMessage("未找到记录");
					}
				} else if ("000001".equals(code)) {
					result.addMessage("未找到记录");
				} else if ("000003".equals(code)) {
					LOGGER.info("请求时间与当前时间相差过长");
					result.addMessage("未找到记录");
				} else if ("999999".equals(code)) {
					LOGGER.info("缺少参数或异常:{}", json.getString("messages"));
					result.addMessage("未找到记录");
				}
			} else {
				result.addMessage("未找到记录");
			}
		} else {
			result.addMessage("未找到记录");
		}
		return result;
	}

	@Override
	public Result<JSONObject> getSuanHuaAntifraud(String loanNo) {
		Result<JSONObject> result = new Result<JSONObject>(Result.Type.FAILURE);
		StopWatch stopWatch = new StopWatch();
		// 获取待反欺诈评分数据
		ResBMSAduitPersonVo vo = bmsLoanInfoService.getBMSLoanInfoByloanNo(loanNo);
		// AntifraudReqNewVo antifraudvo = new AntifraudReqNewVo();
		// antifraudvo.setAppNo(loanNo);
		// antifraudvo.setIdNo(vo.getIdNum());
		// antifraudvo.setName(vo.getName());
		// antifraudvo.setCellphone(vo.getCellphone());
		// antifraudvo.setAddress(vo.getAddress());
		// antifraudvo.setHomePhone(vo.getHomeTel());
		// antifraudvo.setCompanyName(vo.getCompanyName());
		// antifraudvo.setCompanyAddress(vo.getCompanyAddress());
		// antifraudvo.setCompanyPhone(vo.getCompanyPhone());
		// antifraudvo.setContactPhone1(vo.getContactPhone1());

		Map<String, String> map = new HashMap<String, String>();
		map.put("creatorId", ShiroUtils.getCurrentUser().getUsercode());
		map.put("sources", "AMS");
		map.put("appNo", loanNo);
		map.put("idCard", vo.getName());
		map.put("name", vo.getIdNum());
		map.put("cellphone", vo.getCellphone());
		map.put("address", vo.getAddress());
		map.put("homePhone", vo.getHomeTel());
		map.put("companyName", vo.getCompanyName());
		map.put("companyAddress", vo.getCompanyAddress());
		map.put("companyPhone", vo.getCompanyPhone());
		map.put("contactPhone1", vo.getContactPhone1());
		map.put("timestamp", String.valueOf(System.currentTimeMillis()));
		map.put("appNo", loanNo);
		Map<String, String> req = new HashMap<String, String>();
		req.put("param", JSON.toJSONString(map));
		// 调用征信系统获取评分结果
		// String antifraud = HttpUtils.postForJson(sysCreditZX +
		// "creditReport/getAntifraudInfo", JSON.toJSONString(antifraudvo),
		// String.class);
		stopWatch.start("反欺诈评分结果");
		String antifraud = HttpUtils.doPost(sysCreditZX + "creditReport/getAntifraudInfo", req, null);
		stopWatch.stop();
		LOGGER.info("反欺诈评分结果：{}", antifraud);
		// edit by zw at 2017-05-06 反欺诈评分结果为空判断
		if (StringUtil.isNotEmpty(antifraud)) {
			JSONObject responseResult = JSONObject.parseObject(antifraud);
			// 判断是否为查询成功
			if (responseResult.containsKey("code") && EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(responseResult.getString("code"))) {
				result.setType(Result.Type.SUCCESS);
			}
			// 反欺诈结果信息
			result.setData(responseResult.getJSONObject("data") == null ? new JSONObject() : responseResult.getJSONObject("data"));
		}
		LOGGER.info(stopWatch.prettyPrint());
		// end at 2017-05-06
		return result;
	}

	/**
	 * 算话征信报告
	 *
	 * @param applyBasiceInfo
	 * @return
	 */
	@Override
	public Result<JSONObject> getSuanHuaCredit(ReqInformationVO applyBasiceInfo) {
		/*姓名  证件类型  证件号码
		测试数据  9  8eedadb09e892063
		测试数据二  9  172b8091c7d5b330
		reportId 99 100
		*/
		StopWatch stopWatch = new StopWatch();
		Result<JSONObject> result = new Result<JSONObject>(Type.FAILURE);
		Map<String,Object> mapParams = new HashMap<String,Object>();
		mapParams.put("appNo", applyBasiceInfo.getLoanNo());// 借款编号
		mapParams.put("name",applyBasiceInfo.getName());// 用户
		mapParams.put("idCard",applyBasiceInfo.getIdNo());// 身份证号
		mapParams.put("idType", 9);
		mapParams.put("reportId",applyBasiceInfo.getShReportId());
		mapParams.put("creatorId", ShiroUtils.getAccount());// 操作用户
		mapParams.put("souces", sysCode);// 系统代码
		mapParams.put("timestamp", System.currentTimeMillis());// 时间戳
		Map<String,String> map = new HashMap<String,String>();
		map.put("param",JSON.toJSONString(mapParams));
		stopWatch.start("算话征信报告LoanNo:" + applyBasiceInfo.getLoanNo());
		String response = HttpUtils.doPost(sysCreditZX + "suanhua/getReportInfo", map, null);
		stopWatch.stop();
		LOGGER.info("算话征信报告 params:{} result:{}", JSON.toJSONString(mapParams), response);
		if (Strings.isNotEmpty(response)) {//000000为有记录;000001查询失败无算话征信报告结果;000003 请求时间与当前时间相差过长;	999999缺少参数或异常
			JSONObject json =  JSON.parseObject(response);
			String code = json.getString("code");
			if (EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(code)) {
			    JSONObject jsonObject = new JSONObject();
			    jsonObject.put("data",json.getJSONObject("data"));
			    jsonObject.put("queryRecordData",json.getJSONObject("queryRecordData"));
				result.setType(Type.SUCCESS);
				result.setData(jsonObject);
			} else {
				result.addMessage("查无结果");
			}
		} else {
			result.addMessage("查询失败");
		}
		LOGGER.info(stopWatch.prettyPrint());
		return result;
	}
}
