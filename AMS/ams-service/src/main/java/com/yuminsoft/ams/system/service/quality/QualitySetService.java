package com.yuminsoft.ams.system.service.quality;

import com.yuminsoft.ams.system.domain.quality.QualityExtractCase;
import com.yuminsoft.ams.system.domain.quality.QualitySetInfo;
import com.yuminsoft.ams.system.util.Result;

import java.util.List;


/**
 * 质检抽检率设置Service
 *
 * @author sunlonggang
 */
public interface QualitySetService {

    //抽检率设置
    Result<String> samplingRateSave(QualitySetInfo samplingInfo);

    // 系统抽单_常规队列

    /**
     * 手动触发的时候会有值
     * @param date
     */
    QualityExtractCase systemSamplingRegular(String date);

    //获取最近两周期
    List<QualitySetInfo> getTwoSet();
}
