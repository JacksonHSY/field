package com.yuminsoft.ams.system.service.quality;

import com.yuminsoft.ams.system.domain.quality.QualityTaskInfo;

import java.util.List;
import java.util.Map;

/**
 * @author lihm
 * @data 2017年6月6日上午11:51:29
 * 质检人员
 */
public interface QualityTaskInfoService {
	
	/**
     * 查询待分派的接单质检员
     * @param map
     * @return
     */
    public List<QualityTaskInfo> findForAssign(Map<String, Object> map);
    
    /**
     * 查找当前人员及辖下接单人员
     * @author lihuimeng
     * @date 2017年6月8日 下午4:40:54
     */
    public List<QualityTaskInfo> findQualityUser(String sysCode, String userCode); 
    
    public QualityTaskInfo findOne(Map<String, Object> map);
    
    /**
     * 查找当前人员及辖下所有未失效的人员
     */
    public List<QualityTaskInfo> findForManual(Map<String,Object> map);
}
