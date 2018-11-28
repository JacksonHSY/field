package com.yuminsoft.ams.system.service.quality;

import com.ymkj.base.core.biz.api.message.Response;
import com.yuminsoft.ams.system.domain.quality.QualityCheckResult;

import java.util.List;
import java.util.Map;

/**
 * 质检结论信息
 *
 * @author YM10105
 */
public interface QualityCheckResService {

    public Response<List<QualityCheckResult>> findAll(Map<String, Object> map);

    public QualityCheckResult findOne(Map<String, Object> map);

    public int save(QualityCheckResult qualityCheckRes);

    public int update(QualityCheckResult qualityCheckRes);

    public List<QualityCheckResult> getQualityCheckOpinion(long qualityCheckId);

    public List<QualityCheckResult> getQualityReCheckWaitList(String userCode);
}
