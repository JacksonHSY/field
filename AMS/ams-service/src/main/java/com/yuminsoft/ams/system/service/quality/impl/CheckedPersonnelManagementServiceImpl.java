package com.yuminsoft.ams.system.service.quality.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.gson.Gson;
import com.ymkj.ams.api.service.quality.QualityExecuter;
import com.ymkj.ams.api.vo.request.audit.ReqQualityPassParameterVO;
import com.ymkj.base.core.biz.api.message.Response;
import com.ymkj.pms.biz.api.enums.OrganizationEnum.OrgCode;
import com.ymkj.pms.biz.api.vo.request.ReqParamVO;
import com.ymkj.pms.biz.api.vo.response.ResOrganizationTreeVO;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.common.AmsConstants;
import com.yuminsoft.ams.system.dao.quality.QualityRegularInfoMapper;
import com.yuminsoft.ams.system.dao.quality.QualitySetInfoMapper;
import com.yuminsoft.ams.system.domain.quality.QualityRegularInfo;
import com.yuminsoft.ams.system.domain.quality.QualitySetInfo;
import com.yuminsoft.ams.system.service.BaseService;
import com.yuminsoft.ams.system.service.pms.PmsApiService;
import com.yuminsoft.ams.system.service.quality.CheckedPersonnelManagementService;
import com.yuminsoft.ams.system.util.DateUtils;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 被检人员service
 * @author fuhongxing
 */
@Service
public class CheckedPersonnelManagementServiceImpl extends BaseService implements CheckedPersonnelManagementService{
	@Autowired
	private QualityRegularInfoMapper qualityRegularInfoMapper;
	@Autowired
	private QualitySetInfoMapper QualitySetInfoMapper;
	@Autowired
	private QualityExecuter qualityExecuter;
	@Autowired
	private PmsApiService pmsApiService;

	@Override
	public List<QualityRegularInfo> findAll(Map<String, Object> map) {
		return qualityRegularInfoMapper.findAll(map);
	}

	@Override
	public QualityRegularInfo findOne(Map<String, Object> map) {
		return qualityRegularInfoMapper.findOne(map);
	}

	@Override
	public Result<String> save(QualityRegularInfo qualityRegularInfo) {
		Result<String> result = new Result<>(Result.Type.SUCCESS);
		try{
			qualityRegularInfoMapper.save(qualityRegularInfo);
		}catch (Exception e) {
			LOGGER.error("操作异常...", e);
			result.setType(Result.Type.FAILURE);
			result.addMessage("操作异常...!");
		}
		return result;
	}

	@Override
	public int delete(Long id) {
		return qualityRegularInfoMapper.delete(id);
	}

	/**
	 * 分页查询
	 */
	@Override
	public ResponsePage<QualityRegularInfo> getPageList(RequestPage requestPage,QualityRegularInfo qualityRegularInfo) {
		ResponsePage<QualityRegularInfo> rp = new ResponsePage<QualityRegularInfo>();
		PageHelper.startPage(requestPage.getPage(), requestPage.getRows());
		Map<String, Object> paraMap = new HashMap<String,Object>();
		paraMap.put("checkedUser", qualityRegularInfo.getCheckedUser());
		paraMap.put("checkedUserName", qualityRegularInfo.getCheckedUserName());
		if(qualityRegularInfo.getIfRegular()==null){
			paraMap.put("ifDelete", AmsConstants.TWO);
		}else{
			paraMap.put("ifRegular", qualityRegularInfo.getIfRegular());
		}
		List<QualityRegularInfo> list = qualityRegularInfoMapper.findAll(paraMap);
		rp.setRows(list);
		rp.setTotal(((Page<QualityRegularInfo>) list).getTotal());
		return rp;
	}

	@Override
	public Result<String> deleteBatch(String[] ids) {
		Result<String> result = new Result<>(Result.Type.SUCCESS);
		try {
		qualityRegularInfoMapper.deleteBatch(ids);
		result.addMessage("删除成功");
		} catch (Exception e) {
			LOGGER.error("操作异常...", e);
			result.setType(Result.Type.FAILURE);
			result.addMessage("操作异常...!");
		}
		return result;
	}
	
	/**
	 * 查询被检人员工号
	 */
	@Override
	public List<String> findUserId(Map<String, Object> map) {
		return qualityRegularInfoMapper.findUserId(map);
	}
	
	/**
	 * @author wangzx
	 * @version 2017年6月3日
	 * @return
	 */
	@Override
	public String getPersonTree() {
				//判断当前属于哪个周期
				Gson gson = new Gson();
				Map<String,Object> map = new HashMap<String,Object>();
				SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
				map.put("date",sdf.format(DateUtils.addDate(new Date(), 0)));
				List<ResOrganizationTreeVO> listTree = new ArrayList<ResOrganizationTreeVO>();
				try {
					//map.put("createdDate",DateUtils.format(DateUtils.addDate(date, -1)));
					QualitySetInfo  samplingInfo =  QualitySetInfoMapper.findByDate(map);
					if(null == samplingInfo){
							LOGGER.info("获取质检被检人员失败未设置对应质检周期");
					}else{
					//如果当前周期不会空，则获取前一个周期
					map.clear();
					map.put("endDate",DateUtils.addDate(DateUtils.stringToDate(samplingInfo.getStartDate(), "yyyy-MM-dd"), -1));
					String startDate = samplingInfo.getStartDate();
					QualitySetInfo before = QualitySetInfoMapper.findNearlyDate(map);
					if(before!=null){
						startDate = before.getStartDate();
					}
					//根据当前周期去得到当前周期内的初审人员，调用借款系统查询人员code
					ReqQualityPassParameterVO vo = new ReqQualityPassParameterVO();
					vo.setSysCode(sysCode);
					vo.setStartDate(startDate);
					vo.setEndDate(samplingInfo.getEndDate());
					Response<List<String>> roleList = qualityExecuter.getUserCodeByPeriod(vo);
					if(roleList.getData().size()==0){
						LOGGER.info("对应周期内未找到对应的初审人员");
					}else{
						//得到初审组织树,普通初审和直通车初审
						ReqParamVO req = new ReqParamVO();
						List<String> list = new ArrayList<String>(Arrays.asList(OrgCode.CHECK.getCode(),OrgCode.SALE_CHECK.getCode()));
						req.setUsercodes(roleList.getData());
						req.setOrgTypeCodes(list);
						req.setStatus(AmsConstants.ZERO);
						req.setInActive(AmsConstants.T);
						req.setLoginUser(ShiroUtils.getAccount());
						req.setAllPerms(true);
						List<ResOrganizationTreeVO> res = pmsApiService.getOrgTreeAndEmployees(req);
						listTree = res;
						}
					}
				} catch (Exception e) {
					LOGGER.info("获取周期内被检初审人员失败"+e);
				}
					return gson.toJson(listTree);
	}
}
