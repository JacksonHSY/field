package com.yuminsoft.ams.system.service.approve.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ymkj.pms.biz.api.vo.response.ResEmployeeVO;
import com.yuminsoft.ams.system.common.EnumUtils;
import com.yuminsoft.ams.system.common.PhoneUtil;
import com.yuminsoft.ams.system.dao.approve.ApplyHistoryMapper;
import com.yuminsoft.ams.system.dao.approve.MobileHistoryMapper;
import com.yuminsoft.ams.system.dao.system.SysParamDefineMapper;
import com.yuminsoft.ams.system.domain.approve.ApplyHistory;
import com.yuminsoft.ams.system.domain.approve.MobileHistory;
import com.yuminsoft.ams.system.domain.system.SysParamDefine;
import com.yuminsoft.ams.system.service.approve.MobileHistoryService;
import com.yuminsoft.ams.system.service.pms.PmsApiService;
import com.yuminsoft.ams.system.vo.firstApprove.MobileHistoryVO;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;
import com.yuminsoft.ams.system.vo.pluginVo.RestResponse;
import com.yuminsoft.ams.system.vo.webapi.TelephoneCheckParamIn;

/**
 * @author YM10165
 */
@Service
public class MobileHistoryServiceImpl implements MobileHistoryService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MobileHistoryServiceImpl.class);

	/**
	 * mobileHistoryMapper
	 */
	@Autowired
	private MobileHistoryMapper mobileHistoryMapper;
	@Autowired
	private SysParamDefineMapper sysParamDefineMapper;
	@Autowired
	private ApplyHistoryMapper applyHistoryMapper;
	@Autowired
	private PmsApiService pmsApiService;

	/**
	 * 根据借款号查询电核记录 其中不包含第三方联系人
	 */
	@Override
	public ResponsePage<MobileHistoryVO> queryMobileHisByLoanId(RequestPage requestPage, String loanNo, String userCode) {
		ResponsePage<MobileHistoryVO> rp = new ResponsePage<MobileHistoryVO>();
		PageHelper.startPage(requestPage.getPage(), requestPage.getRows());
		List<MobileHistoryVO> list = mobileHistoryMapper.queryMobileHis(loanNo);
		if (list != null) {
			ApplyHistory applyHistory = applyHistoryMapper.findLastByLoanNo(loanNo);
			Date lastModifiedDate = null;
			if (null != applyHistory) {
				lastModifiedDate = applyHistory.getLastModifiedDate();
			}
			for (int i = 0; i < list.size(); i++) {
				MobileHistoryVO mobileHistoryVO = list.get(i);
				mobileHistoryVO.setCreatedByCode(mobileHistoryVO.getCreatedBy());
				if (null != lastModifiedDate) {// 电核记录的最后修改时间与申请件最后提交退回改派操作时间进行对比
					Date mobileHistoryDate = mobileHistoryVO.getLastModifiedDate();
					if (mobileHistoryDate.before(lastModifiedDate)) { // 电核记录时间再最后一次审批意见提交前
						mobileHistoryVO.setFlag(false);
					}
				} else if (null != applyHistory) {
					mobileHistoryVO.setFlag(false);
				}
				// 查询员工
				if (null != mobileHistoryVO.getCreatedBy() && !mobileHistoryVO.getCreatedBy().isEmpty()) {
					ResEmployeeVO reVO = pmsApiService.findEmpByUserCode(mobileHistoryVO.getCreatedBy());
					if (null != reVO) {
						mobileHistoryVO.setCreatedBy(reVO.getName());
					}
				}
			}
		}

		rp.setRows(list);
		rp.setTotal(((Page<MobileHistoryVO>) list).getTotal());
		return rp;
	}

	/**
	 * 查询所有的电核记录
	 */
	@Override
	public List<MobileHistory> queryThridPartyByLoanId(String loanId) {

		return mobileHistoryMapper.queryMobileHisByLoanId(loanId);
	}

	/**
	 * 修改电核记录
	 */
	@Override
	public int updateMobileHisById(MobileHistory mobileHistory) {
		//设置手机归属地信息
		mobileHistory.setPhoneCity(PhoneUtil.getCity(mobileHistory.getTelPhone()));
		mobileHistory.setCarrier(PhoneUtil.getCarrier(mobileHistory.getTelPhone()));
		return mobileHistoryMapper.update(mobileHistory);
	}

	@Override
	public int delete(Long id) {
		return mobileHistoryMapper.delete(id);
	}

	@Override
	public int save(MobileHistory mobileHistory) {
		//设置手机归属地信息
		mobileHistory.setPhoneCity(PhoneUtil.getCity(mobileHistory.getTelPhone()));
		mobileHistory.setCarrier(PhoneUtil.getCarrier(mobileHistory.getTelPhone()));
		return mobileHistoryMapper.save(mobileHistory);
	}

	/**
	 * zjy 获取第三方来源列表
	 */
	@Override
	public List<SysParamDefine> getThridPartySource(Map<String, Object> map) {
		return sysParamDefineMapper.findAll(map);
	}

	/**
	 * 根据id查询单个电核记录
	 */
	@Override
	public MobileHistory findMobileHistoryById(Long id) {
		MobileHistory mobileHistory = mobileHistoryMapper.findById(id);
		return mobileHistory;
	}

	@Override
	public RestResponse<List<MobileHistory>> getTelephoneCheckList(TelephoneCheckParamIn param) {
		LOGGER.info("webapi--根据借款编号分页查询电核记录,入参{}",JSON.toJSONString(param));
		
		// loanNo为空，页码为0，页面大小为0都返回参数不合法
		if(StringUtils.isEmpty(param.getLoanNo()) || param.getPage() == 0 || param.getRows() == 0) {
			LOGGER.info("webapi--根据借款编号分页查询电核记录,请求参数不合法");
			return new RestResponse<List<MobileHistory>>(EnumUtils.ResponseCodeEnum.PARAMNOTVALID.getValue(),"请求参数不合法");
		}
		
		PageHelper.startPage(param.getPage(),param.getRows());
		Page<MobileHistory> list = mobileHistoryMapper.queryTelephoneCheckList(param.getLoanNo());
		return new RestResponse<List<MobileHistory>>(list.getResult(), list.getTotal());
	}

}
