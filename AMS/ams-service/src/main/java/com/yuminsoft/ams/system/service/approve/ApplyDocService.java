package com.yuminsoft.ams.system.service.approve;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ymkj.ams.api.vo.response.audit.ResBMSApplicationPartVO;
import com.ymkj.ams.api.vo.response.master.ResBMSOrgLimitChannelVO;
import com.ymkj.ams.api.vo.response.master.ResBMSProductAuditLimitVO;
import com.ymkj.base.core.biz.api.message.PageResponse;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.vo.apply.ApplyDoc;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;

public interface ApplyDocService {

    /**
     * 申请件列表信息查询
     * 
     * @param requestPage
     * @param applyDoc
     * @param statu
     * @return
     * @author JiaCX
     * @date 2017年4月13日 下午5:07:58
     */
    PageResponse<ResBMSApplicationPartVO> getApplicationPartList(String sysCode, RequestPage requestPage, ApplyDoc applyDoc, String statu);

    /**
     * 拒绝件修改
     * 
     * @param sysCode
     * @param applyDoc
     * @return
     * @author JiaCX
     * @date 2017年4月13日 下午5:40:28
     */
    Result<String> editRefusedApplicationPart(String sysCode, ApplyDoc applyDoc, HttpServletRequest request);

    /**
     * 通过件修改
     * 
     * @param sysCode
     * @param applyDoc
     * @param session
     * @return
     * @author JiaCX
     * @date 2017年4月13日 下午5:34:18
     */
    Result<String> editPassedApplicationPart(String sysCode, ApplyDoc applyDoc, HttpSession session, HttpServletRequest request);

    /**
     * 查询产品的审批期限
     * 
     * @param sysCode
     * @param applyDoc
     * @return
     * @author JiaCX
     * @date 2017年4月13日 下午5:28:11
     */
    List<ResBMSProductAuditLimitVO> getApprovalPeriod(String sysCode, ApplyDoc applyDoc);

    /**
     * 通过件拒绝
     * @param applyDoc
     * @return
     * @author JiaCX
     * @date 2017年4月13日 下午5:16:53
     */
    Result<String> refusePassedApplicationPart(String sysCode, ApplyDoc applyDoc, HttpServletRequest request);

    /**
     * 设置审批金额的上限和下限
     * 
     * @param applyDoc
     * @author JiaCX
     * @date 2017年6月24日 下午2:31:05
     */
    void setMaxAndMin(ApplyDoc applyDoc);

    /**
     * 根据审批额度获取该产品的审批期限列表
     * 
     * @param sysCode
     * @param loanNo
     * @param accLmt
     * @param request
     * @return
     * @author JiaCX
     * @date 2017年10月12日 下午6:05:44
     */
    List<ResBMSOrgLimitChannelVO> getApprovalPeriodList(String sysCode, String loanNo, String accLmt, HttpServletRequest request);

}
