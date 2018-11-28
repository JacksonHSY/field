package com.yuminsoft.ams.system.service.quality;

import java.util.List;

import com.yuminsoft.ams.system.domain.quality.QualityErrorCode;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;

public interface QualityErrorCodeService {
	public ResponsePage<QualityErrorCode> getPageList(RequestPage requestPage,QualityErrorCode qualityErrorCodeInfo);
	
	public String save(QualityErrorCode qualityErrorCodeInfo);
	
	public QualityErrorCode findById(long id);
	
	public String update(QualityErrorCode qualityErrorCodeInfo,String oldCode);
	
	public String delete(String[] ids);

	/**
	 * @Desc:  查询所有可用的差错代码
	 * @Author: phb
	 * @Date: 2017/5/14 14:05
	 */
	public List<QualityErrorCode> findAllUsableErrorCodes();

}
