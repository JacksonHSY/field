package com.yuminsoft.ams.system.dao.quality;

import com.yuminsoft.ams.system.domain.quality.QualityTaskInfo;

import java.util.List;
import java.util.Map;

public interface QualityTaskInfoMapper {
	
	public int save(QualityTaskInfo qualityTaskInfo);
	
	public int delete(Long id);
    
	public int deleteBatch(String[] ids);
	
    public int update(QualityTaskInfo qualityTaskInfo);
    
    public QualityTaskInfo findById(Long id);
    
    public QualityTaskInfo findOne(Map<String, Object> map);

    public List<QualityTaskInfo> findAll(Map<String, Object> map);
    
    public List<QualityTaskInfo> findCheckUser();
    
    /**
     * 查询待分派的接单质检员
     * @param map
     * @return
     */
    public List<QualityTaskInfo> findForAssign(Map<String, Object> map);
    
    public int updateTaskNum(Map<String, Object> map);
    
    /**
     * 更新递减被改派的质检员目前任务数量
     * @param info
     * @return
     */
    public int reduceTaskNum(QualityTaskInfo info);

    /**
     * 更新递增被改派的质检员目前任务数量
     * @param info
     * @return
     */
    public int incrTaskNum(QualityTaskInfo info);

    /**
     * 查找当前人员及辖下接单人员
     */
    public List<QualityTaskInfo> findUse(Map<String,Object> map);
    
    /**
     * 查找当前人员及辖下所有未失效的人员
     */
    public List<QualityTaskInfo> findForManual(Map<String,Object> map);

}