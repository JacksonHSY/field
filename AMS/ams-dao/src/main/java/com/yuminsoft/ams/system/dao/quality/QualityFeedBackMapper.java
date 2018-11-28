package com.yuminsoft.ams.system.dao.quality;

import com.yuminsoft.ams.system.domain.quality.QualityFeedBack;
import com.yuminsoft.ams.system.vo.quality.QualityFeedBackVo;

import java.util.List;
import java.util.Map;

/**
 * @Desc: 质检反馈
 * @Author: phb
 * @Date: 2017/5/10 14:47
 */
public interface QualityFeedBackMapper {
    /**
     * 查询待处理任务
     *
     * @param qualityFeedBack
     * @return
     */
    List<QualityFeedBackVo> findToDo(QualityFeedBackVo qualityFeedBack);

    /**
     * 查询完成任务
     *
     * @param qualityFeedBack
     * @return
     */
    List<QualityFeedBackVo> findDone(QualityFeedBackVo qualityFeedBack);

    /**
     * @Desc: 修改feedback_code字段标记申请件进入反馈流程/ 0未进入反馈流程 1：进入流程 2质检主管核对 完成 ：3流程结束
     * @Author: phb
     * @Date: 2017/5/10 15:46
     */
    void markCheckRes(List<Long> resList);

    /**
     * @Desc: 记录质检流程中每一次的反馈结果
     * @Author: phb
     * @Date: 2017/5/10 14:20
     */
    int saveFeedBackRecord(QualityFeedBack fbr);

    /**
     * @Desc: 查询质检反馈的历史结果
     * @Author: phb
     * @Date: 2017/5/10 14:28
     */
    List<QualityFeedBack> getFeedBackHistoryByCheckResId(Long checkResId);

    /**
     * @Desc: 查询一条反馈记录
     * @Author: phb
     * @Date: 2017/5/13 19:25
     */
    QualityFeedBack findOne(Map<String,Object> param);

    /**
     * @Desc: 查询出最新一条反馈记录
     * @Author: phb
     * @Date: 2017/5/13 20:56
     */
    QualityFeedBack getLatestFeedBackByChekResId(Long checkResId);
}
