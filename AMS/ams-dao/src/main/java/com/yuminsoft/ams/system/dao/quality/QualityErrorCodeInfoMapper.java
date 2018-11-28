package com.yuminsoft.ams.system.dao.quality;

import java.util.List;
import java.util.Map;

import com.yuminsoft.ams.system.domain.quality.QualityErrorCode;
import com.yuminsoft.ams.system.domain.quality.QualityErrorCodeInfo;

/**差错代码页面显示的Mapper
 * @author YM10174
 *
 */
public interface QualityErrorCodeInfoMapper {
	//查询所有差错代码
	public List<QualityErrorCode> findAll(QualityErrorCode qualityErrorCodeInfo);
	//保存差错代码
	public int save(QualityErrorCode qualityErrorCodeInfo);
	//根据ID查找差错代码
	public QualityErrorCode findById(Long id);
	//更新差错代码
	public int update(QualityErrorCode qualityErrorCodeInfo);
	//删除差错代码
	public int deletes(String[] ids);
	//根据loan查询对应的差错信息
	public List<QualityErrorCodeInfo> queryErrorCodeInfoByLoanNo(String loan);
	//查询所有的大类
	public List<Map<String, String>> queryAllParentInfo();
	//根据大类查询一级分类
	public List<Map<String, String>> queryFirstInfoByParent(String parentId);
	//根据大类，一类分类查询二级分类
	public List<Map<String, String>> querySecondInfoByParentFirst(String parentId,String firstId);
	
}
