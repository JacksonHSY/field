package com.yuminsoft.ams.system.service.quality;

import java.util.List;

import com.yuminsoft.ams.system.domain.quality.QualitySourceInfo;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;

public interface QualitySourceService {
	public ResponsePage<QualitySourceInfo> getPageList(RequestPage requestPage,QualitySourceInfo qualitySource);
	
	public String save(QualitySourceInfo qualitySource);
	
	public QualitySourceInfo findById(long id);
	
	public String update(QualitySourceInfo qualitySource,String oldSource);
	
	public String delete(String[] ids);
	
	public List<QualitySourceInfo> getAllSource();
	

}
