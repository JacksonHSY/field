package com.yuminsoft.ams.system.service.quality;



import java.util.List;
import java.util.Map;

import com.yuminsoft.ams.system.domain.quality.QualityErrorCodeInfo;

public interface QualityErrorCodeInfoService {
	//根据loanNo查询对应的一级二级三级分类以及描述
	List<QualityErrorCodeInfo> queryQualityErrorCodeInfo(String loanNo);
	//根据标志查询所有一级二级三级
	List<Map<String, String>> queryErrorCodeByLevel(String mark,String parentId,String firstId);

}
