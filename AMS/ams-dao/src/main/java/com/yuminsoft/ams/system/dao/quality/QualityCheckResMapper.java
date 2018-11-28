package com.yuminsoft.ams.system.dao.quality;

import com.yuminsoft.ams.system.domain.quality.QualityCheckResult;
import com.yuminsoft.ams.system.vo.quality.QualityCheckResVo;

import java.util.List;
import java.util.Map;

public interface QualityCheckResMapper {

    int save(QualityCheckResult qualityCheckRes);

    int delete(Long id);

    int update(QualityCheckResult qualityCheckRes);

    QualityCheckResult findById(Long id);

    QualityCheckResult findOne(Map<String, Object> map);

    List<QualityCheckResult> findAll(Map<String, Object> map);

    List<QualityCheckResVo> getQualityHistoryById(Long id);

    List<String> getResIds(Long id);

    List<QualityCheckResVo> getInProcessInfo(Map<String, Object> param);
    
    List<QualityCheckResult> getQualityCheckOpinion(long qualityCheckId);

    /**
     * 获取质检复核等待的申请件
     * @param list 工号列表
     * @author wulinjie
     * @return
     */
    public List<QualityCheckResult> findQualityRecheckWait(List<String> list);
    
}