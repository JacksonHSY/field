package com.yuminsoft.ams.system.dao.quality;

import java.util.Map;

import antlr.collections.List;

import com.yuminsoft.ams.system.domain.quality.QualityErrorList;

/**质检差错代码配置Mapper
 * @author YM10174
 *
 */
public interface QualityErrorListMapper {
	//保存
	public int save(QualityErrorList qualityErrorListInfo);
	//查询
	public QualityErrorList findOne(Map<String,Object> map);


}
