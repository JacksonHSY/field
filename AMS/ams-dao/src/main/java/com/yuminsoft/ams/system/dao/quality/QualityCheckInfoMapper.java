package com.yuminsoft.ams.system.dao.quality;

import com.yuminsoft.ams.system.domain.quality.QualityCheckInfo;

import java.util.List;
import java.util.Map;

public interface QualityCheckInfoMapper {
    
    public int save(QualityCheckInfo qualityCheckInfo);
	
	public int delete(Long id);
    
    public int update(QualityCheckInfo qualityCheckInfo);
    
    public QualityCheckInfo findById(Long id);
    
    public QualityCheckInfo findOne(Map<String, Object> map);
    
    public List<QualityCheckInfo> findAll(Map<String, Object> map);
    
    public List<QualityCheckInfo> findApps();
    /**
     * 改派 /分派
     * @param map
     * @return
     */
    public int updateCheckUser(Map<String,Object> map);
    
    public int closes(String [] ids);
    
    public List<QualityCheckInfo> findByLoanId(String loanNo);
    /**
     * 系统分派查询待分派申请件
     * @param map
     * @return
     */
    public List<QualityCheckInfo> findForAssign(Map<String, Object> map);
    /**
     * 查询当前用户及其辖下人员未分配和质检未完成的进件
     */
    public QualityCheckInfo findByUser(Map<String,Object> map);
    
    /**
     * 根据周期获取周期内分派过单子的质检员的code
     */
    
    public List<String> getUserCode(Map<String,Object> map);

    /**
     * 派单专用
     */
    int updateStatus(QualityCheckInfo qualityCheckInfo);
}