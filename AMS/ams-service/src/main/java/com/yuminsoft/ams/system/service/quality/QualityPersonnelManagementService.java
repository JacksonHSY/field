package com.yuminsoft.ams.system.service.quality;

import java.util.List;
import java.util.Map;

import com.ymkj.pms.biz.api.vo.response.ResEmployeeVO;
import com.ymkj.pms.biz.api.vo.response.ResOrganizationTreeVO;
import com.yuminsoft.ams.system.domain.quality.QualityTaskInfo;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;
import com.yuminsoft.ams.system.vo.quality.QualityTaskInfoVo;

/**
 * 质检人员管理
 * @author sunlonggang
 *
 */
/**
 * @author YM10174
 *
 */
public interface QualityPersonnelManagementService {
	
	public List<QualityTaskInfo> findAll(Map<String,Object> map);
	
	public QualityTaskInfo findOne(Map<String,Object> map);

	/**
	 * 新增质检员
	 * @param qualityTaskInfo
	 * @return
	 */
	public Result<String> save(QualityTaskInfo qualityTaskInfo);
	
	public int delete(Long id);

	/**
	 * 批量删除质检员
	 * @param ids
	 * @return
	 */
	public Result<String> deleteBatch(String[] ids);

	/**
	 * 更新质检人员队列配置
	 * @author wulinjie
	 * @param qualityTaskInfoVo 队列配置
	 * @param checkUsers 质检人员
	 * @return
	 */
	public void updateQualityTask(QualityTaskInfoVo qualityTaskInfoVo, List<ResOrganizationTreeVO> checkUsers);

	/**
	 * 分页查询质检员信息
	 * @param requestPage
	 * @param qualityTaskInfo
	 * @return
	 */
	public ResponsePage<QualityTaskInfo> getPageList(RequestPage requestPage,QualityTaskInfo qualityTaskInfo);
	
	public String getCheckUser(String userCode);

	/**
	 * 本人及其辖下人员，包括离职
	 */
	public List<ResEmployeeVO> getBranchPerson(String code);
	
	public List<ResEmployeeVO> getTwoCyclePerson(String code);
}
