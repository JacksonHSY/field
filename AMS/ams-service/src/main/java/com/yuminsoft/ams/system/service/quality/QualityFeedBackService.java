package com.yuminsoft.ams.system.service.quality;

import com.yuminsoft.ams.system.domain.quality.QualityFeedBack;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;
import com.yuminsoft.ams.system.vo.pic.AttachmentVo;
import com.yuminsoft.ams.system.vo.quality.QualityCheckResVo;
import com.yuminsoft.ams.system.vo.quality.QualityFeedBackVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by ZJY on 2017/2/27.
 */
public interface QualityFeedBackService {
    /**
     * @Desc: 查询反馈待处理列表
     * @Author: phb
     * @Date: 2017/5/11 14:07
     */
    ResponsePage<QualityFeedBackVo> getPageListToDo(RequestPage requestPage, QualityFeedBackVo qualityFeedBack);

    /**
     * @Desc: 查询反馈已完成列表
     * @Author: phb
     * @Date: 2017/5/11 14:08
     */
    ResponsePage<QualityFeedBackVo> getPageListDone(RequestPage requestPage, QualityFeedBackVo qualityFeedBack);

    /**
     * @Desc: 查询质检历史结论
     * @Author: phb
     * @Date: 2017/5/11 14:08
     */
    List<QualityCheckResVo> getQualityHistoryById(Long id);

    /**
     * @Desc: 查询质检反馈的历史记录
     * @Author: phb
     * @Date: 2017/5/10 14:41
     */
    List<QualityFeedBack> getFeedBackHistoryByCheckResId(Long checkResId);

    /**
     * @Desc: 完成反馈任务
     * @Author: phb
     * @Date: 2017/5/11 14:08
     */
    void finishFeedbackTask(String  checkResIds);

    /**
     * @Desc: 查询质检反馈的附件
     * @Author: phb
     * @Date: 2017/4/27 11:33
     */
    List<AttachmentVo> findFeedBackAttachmentList(String loanNo, String picImgUrl);

    /**
     * @param loanNo 
     * @Desc: 根据id删除反馈附件
     * @Author: phb
     * @Date: 2017/5/6 9:18
     */
    Result<String> deleteAttachmentById(Long id, String jobNumber, String operator, String picImgUrl, String loanNo);

    /**
     * @Desc: 导出待处理列表
     * @Author: phb
     * @Date: 2017/5/6 14:06
     */
    void exportToDoExcel(QualityFeedBackVo qualityFeedBackVo, HttpServletRequest req, HttpServletResponse res) throws Exception;

    /**
     * @Desc: 导出已完成列表
     * @Author: phb
     * @Date: 2017/5/6 14:06
     */
    void exportDoneList(QualityFeedBackVo qualityFeedBackVo,HttpServletRequest req, HttpServletResponse res) throws Exception;

    /**
     * @Desc: 根据qualityCheckId查询出最新的一条质检结论
     * @Author: phb
     * @Date: 2017/5/8 14:13
     */
    QualityCheckResVo getLatestQualityHistoryById(Long qualityCheckId);

    /**
     * @Desc: 查询出最新一条质检反馈结果
     * @Author: phb
     * @Date: 2017/5/13 20:54
     */
    QualityFeedBack getLatestFeedBackByChekResId(Long checkResId);

    /**
     * @Desc: 保存当前操作人的一条质检反馈记录
     * @Author: phb
     * @Date: 2017/5/18 17:28
     */
    Result<String> saveFeedBackRecord(QualityFeedBack qualityFeedBack);
}
