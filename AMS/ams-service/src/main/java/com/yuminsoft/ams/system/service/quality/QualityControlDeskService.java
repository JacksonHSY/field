
package com.yuminsoft.ams.system.service.quality;

import com.ymkj.ams.api.vo.response.master.ResBMSQueryLoanLogVO;
import com.ymkj.pms.biz.api.vo.response.ResEmployeeVO;
import com.yuminsoft.ams.system.domain.approve.ApprovalHistory;
import com.yuminsoft.ams.system.domain.quality.QualityCheckResult;
import com.yuminsoft.ams.system.domain.quality.QualityErrorCode;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;
import com.yuminsoft.ams.system.vo.quality.QualityControlDeskVo;
import com.yuminsoft.ams.system.vo.quality.QualityLogVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * 质检工作台
 *
 * @author lihm
 */
public interface QualityControlDeskService {
    /***
     * 分页查询质检信息
     * @param requestPage
     * @param vo
     * @param type
     * @return
     */
    ResponsePage<QualityControlDeskVo> getPageList(RequestPage requestPage, QualityControlDeskVo vo, String type);

    /**
     * 质检手工分派导出
     * @param qualityControlDeskVo
     * @param request
     * @param response
     */
    void exportManualDispatchList(QualityControlDeskVo qualityControlDeskVo, HttpServletRequest request, HttpServletResponse response);

    Result<String> reCheck(HttpServletRequest request, List<QualityCheckResult> resList, String loanNo, String checkUser);

    //根据条件查询质检件信息
    List<QualityControlDeskVo> getQualityControlDeskVos(QualityControlDeskVo qualityControlDeskVo);

    /**
     * 查询并筛选质检件审核人历史信息
     */
    public Map<String, Object> findApproveHistory(Map<String, Object> map);

    /**
     * update
     *
     * @param controlDeskVo
     * @return
     */
    public int update(QualityControlDeskVo controlDeskVo);

    /**
     * 完成质检
     *
     * @author lihuimeng
     * @date 2017年6月12日 下午2:55:59
     */
    public Result<String> completeQuality(List<QualityCheckResult> resList, String loanNo,String flag, String checkUser);

    /**
     * 暂存
     *
     * @author lihuimeng
     * @param checkUser 
     * @date 2017年6月12日 下午2:56:11
     */
    public Result<String> pauseQuality(List<QualityCheckResult> resList, String loanNo, String checkUser);

    /**
     * 分页查询已完成的质检意见信息
     *
     * @param requestPage
     * @param vo
     * @param type
     * @return
     */
    ResponsePage<QualityCheckResult> getOpoinPageList(RequestPage requestPage, QualityCheckResult checkRes);

    /**
     * 差错代码下拉框
     */
    List<QualityErrorCode> findByName(String name);

    /**
     * 质检导出
     */
    void exportQueryList(HttpServletRequest request, HttpServletResponse response, QualityControlDeskVo qualityControlDeskVo);

    /**
     * 质检待处理已完成导出
     *
     * @return auther lihm
     */
    public Result<String> exportQualityList(HttpServletRequest request, HttpServletResponse response, QualityControlDeskVo controlDeskVo, String type);

    /**
     * 获取当前登录人员和指定人员的上下级关系 和指定人员的角色信息
     *
     * @return
     */
    public Map<String, String> getQualityRolesInfo(String targetCode);

    /**
     * 审核日志查询
     *
     * @author lihuimeng
     * @date 2017年6月7日 下午2:22:57
     */
    Map<String, Object> getApprovalName(String loanNo, String roleCode);

    /**
     * 获取借款日志
     *
     * @author lihuimeng
     * @date 2017年6月9日 下午5:50:58
     */
    List<ResBMSQueryLoanLogVO> queryLoanLog(String loanNo, String ip, Map<String, Object> map);

    /**
     * 根据单号查询直接结论展示信息
     *
     * @author lihuimeng
     * @date 2017年6月8日 下午6:01:53
     */
    public List<QualityCheckResult> getCheckResForShow(String loanNo);

    /**
     * 查询质检日志
     *
     * @author lihuimeng
     * @date 2017年6月16日 上午11:35:32
     */
    ResponsePage<QualityLogVo> queryQualityLog(RequestPage requestPage, String loanNo);

    /**
     * 获取信审所有领导信息
     *
     * @author lihuimeng
     * @date 2017年6月17日 下午4:15:36
     */
    List<ResEmployeeVO> getQualityLeader();
    
    /**
     * 质检信息已质检查询
     */
    public List<QualityControlDeskVo> findQualityDoneByUser(Map<String, Object> map);
    
    /**
     * 设置审批人员别称
     * @author lihuimeng
     * @date 2017年7月10日 下午4:31:13
     */
    List<ApprovalHistory> setApprovalPersonName(List<ApprovalHistory> approvalHistoryList, String loanNo, HttpSession session);
    
    /**
     * 获取审核最后提交人领导
     * @author lihuimeng
     * @date 2017年7月12日 上午10:43:55
     */
    String getApprovalLeader(String loanNo, String rtfState);
}
