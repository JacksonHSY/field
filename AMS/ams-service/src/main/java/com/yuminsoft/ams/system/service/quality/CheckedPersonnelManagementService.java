package com.yuminsoft.ams.system.service.quality;

import java.util.List;
import java.util.Map;

import com.yuminsoft.ams.system.domain.quality.QualityRegularInfo;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;

/**
 * 被检人员管理
 * @author YM10105
 *
 */
public interface CheckedPersonnelManagementService {
	
	public List<QualityRegularInfo> findAll(Map<String,Object> map);
	
	public List<String> findUserId(Map<String,Object> map);
	
	public QualityRegularInfo findOne(Map<String,Object> map);
	
	public Result<String> save(QualityRegularInfo qualityRegularInfo);
	
	public int delete(Long id);
	
	public Result<String> deleteBatch(String[] ids);
	
	public String getPersonTree();
	/**
	 * 分页查询被检人员信息
	 * @param requestPage
	 * @param qualityRegularInfo
	 * @return
	 */
	public ResponsePage<QualityRegularInfo> getPageList(RequestPage requestPage,QualityRegularInfo qualityRegularInfo);
}
