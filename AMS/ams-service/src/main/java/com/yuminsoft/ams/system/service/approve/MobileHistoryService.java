package com.yuminsoft.ams.system.service.approve;

import java.util.List;
import java.util.Map;

import com.yuminsoft.ams.system.domain.approve.MobileHistory;
import com.yuminsoft.ams.system.domain.system.SysParamDefine;
import com.yuminsoft.ams.system.vo.firstApprove.MobileHistoryVO;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;
import com.yuminsoft.ams.system.vo.pluginVo.RestResponse;
import com.yuminsoft.ams.system.vo.webapi.TelephoneCheckParamIn;

/**
 * @author YM10165
 */
public interface MobileHistoryService {
	/**
	 * 根据借款号查询电核记录
	 * 
	 * @author YM10165
	 * @date 2017年3月15日
	 * @param requestPage
	 * @param loanId
	 * @return
	 */
	ResponsePage<MobileHistoryVO> queryMobileHisByLoanId(RequestPage requestPage, String loanId,String userCode);

	/**
	 * 查询电核记录
	 * 
	 * @author luting
	 * @date 2017年5月3日 下午8:10:59
	 */
	List<MobileHistory> queryThridPartyByLoanId(String loanId);

	/**
	 * 修改电核记录
	 * 
	 * @author YM10165
	 * @date 2017年3月15日
	 * @param id
	 * @param telRelationType
	 * @param askContent
	 * @return
	 */
	int updateMobileHisById(MobileHistory record);

	/**
	 * @author CWJ
	 * @param id
	 * @return
	 */
	public int delete(Long id);

	/**
	 * @author CWJ
	 * @param mobileHistory
	 * @return
	 */
	public int save(MobileHistory mobileHistory);

	/**
	 * zjy 获取第三方来源列表
	 */
	List<SysParamDefine> getThridPartySource(Map<String, Object> map);

	/**
	 * 根据id查询单个电核记录
	 * 
	 * @author zhouwen
	 * @date 2017年6月19日
	 * @param id
	 * @return
	 */
	MobileHistory findMobileHistoryById(Long id);

	/**
	 * 根据借款编号分页查询电核记录
	 * 
	 * @author Jia CX
	 * <p>2018年3月27日 下午4:16:55</p>
	 * 
	 * @param param
	 * @return
	 */
	RestResponse<List<MobileHistory>> getTelephoneCheckList(TelephoneCheckParamIn param);
}
