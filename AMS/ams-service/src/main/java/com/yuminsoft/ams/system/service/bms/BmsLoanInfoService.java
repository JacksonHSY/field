package com.yuminsoft.ams.system.service.bms;


import com.alibaba.fastjson.JSONArray;
import com.ymkj.ams.api.vo.request.apply.*;
import com.ymkj.ams.api.vo.request.audit.*;
import com.ymkj.ams.api.vo.request.audit.first.ReqApplicationInfoVO;
import com.ymkj.ams.api.vo.request.audit.first.ReqAssetsInfoVO;
import com.ymkj.ams.api.vo.request.master.ReqBMSTMReasonVO;
import com.ymkj.ams.api.vo.request.task.PersonCodeAndRoleVo;
import com.ymkj.ams.api.vo.response.audit.*;
import com.ymkj.ams.api.vo.response.integrate.application.ResApplicationInfoVO;
import com.ymkj.ams.api.vo.response.integrate.application.ResDetailDifferenceVO;
import com.ymkj.ams.api.vo.response.master.ResBMSQueryLoanLogVO;
import com.ymkj.ams.api.vo.response.task.TaskNumberQueVo;
import com.ymkj.base.core.biz.api.message.PageResponse;
import com.ymkj.pms.biz.api.vo.response.ResOrganizationInfo;
import com.yuminsoft.ams.system.common.EnumUtils;
import com.yuminsoft.ams.system.domain.approve.MobileOnline;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.vo.apply.ApplyHistoryVO;
import com.yuminsoft.ams.system.vo.apply.ApprovalSaveVO;
import com.yuminsoft.ams.system.vo.apply.TaskNumber;
import com.yuminsoft.ams.system.vo.engine.RuleEngineVO;
import com.yuminsoft.ams.system.vo.firstApprove.CustomerContactInfoVO;
import com.yuminsoft.ams.system.vo.firstApprove.FirstTelephoneSummaryRelationInfoVo;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface BmsLoanInfoService {

    /**
     * 修改借款信息
     *
     * @param auditAmendEntryVO
     * @return
     * @author dmz
     * @date 2017年3月23日
     */
    public Result<String> updateLoanInfo(AuditAmendEntryVO auditAmendEntryVO);


    /**
     * 前前客户信息修改
     *
     * @param reqApplicationInfoVO
     * @return
     */
    Result<String> updateMoneyLoanInfo(ReqApplicationInfoVO reqApplicationInfoVO);


    /**
     * 修改初审借款单状态
     *
     * @param applyVo  审批历史表
     * @param loanInfo 申请件信息
     * @return
     * @author dmz
     * @date 2017年3月17日
     */
    public Result<String> updateFirstLoanNoStateService(ApplyHistoryVO applyVo, ReqInformationVO loanInfo);

    /**
     * 根据借款编号查询借款基本信息
     *
     * @param sessionId-回话id
     * @param loanNo-借款编号
     * @param flag-标记是刷新redis(true:直接从借款拿;false:从redis拿)
     * @return
     * @author dmz
     * @date 2017年3月18日
     */
    public ReqInformationVO getBMSLoanBasiceInfoByLoanNoService(String sessionId, String loanNo, boolean flag);

    /**
     * 根据借款编号查询借款信息
     *
     * @param loanNo
     * @return
     * @author dmz
     * @date 2017年3月15日
     */
    public ApplyEntryVO getBMSLoanInfoByLoanNoService(String loanNo);

    /**
     * 根据借款编号查询借款信息(客户信息标红)
     *
     * @param loanNo
     * @param type   1:初审，2：终审
     * @return
     * @author dmz
     * @date 2017年5月12日
     */
    public AuditApplyEntryVO queryAuditDifferences(String loanNo, String type);

    /**
     * 根据借款编号查询借款信息(前前客户信息标红)
     *
     * @param loanNo
     * @param type-1:初审，2：终审
     * @return
     */
    ResDetailDifferenceVO getDetailDifference(String loanNo, String type);

    /**
     * 添加联系人
     *
     * @param
     * @return
     * @author dmz
     * @date 2017年3月17日
     */
    public Result<String> insertContactInfoService(FirstTelephoneSummaryRelationInfoVo vo, String loanNo, String version);

    /**
     * 分页查询初审和终审工作台队列信息
     *
     * @param requestPage
     * @param taskType-队列类型
     * @param reqFlag-区分初审和终审
     * @return
     * @author dmz
     * @date 2017年3月21日
     */
    public ResponsePage<ResBMSAuditVo> getWorkbenchList(RequestPage requestPage, String taskType, String reqFlag);

    /**
     * 分页查询初审和终审工已完成队列信息
     *
     * @param request
     * @return
     * @author dmz
     * @date 2017年4月13日
     */
    public ResponsePage<ResOffTheStocksAuditVO> adultOffTheStocks(ReqBMSAdultOffTheStocksVo request, RequestPage requestPage);

    /**
     * 修改联系人联系方式
     *
     * @param vo
     * @return
     */
    public Result<String> updateRelationContactInfo(ReqContactInfoVO vo);

    /**
     * 修改联系人信息
     */
    public Result<String> updateContactInfoService(ReqContactInfoVO[] vo);

    /**
     * 添加联系人
     *
     * @param
     * @return
     * @author dmz
     * @date 2017年3月17日
     */
    public Result<String> insertContactInfoService(ReqContactInfoVO vo);

    /**
     * 电核汇总中修改用户基本信息
     *
     * @param vo
     * @return
     * @author dmz
     * @date 2017年6月16日
     */
    public Result<String> updatePersonalInformation(ReqPersonalInformation vo);

    /**
     * 审批修改产品相关信息
     *
     * @param approvalSaveVO
     * @author dmz
     * @date 2017年4月6日
     */
    public void updateProductInfo(ApprovalSaveVO approvalSaveVO);

    /**
     * 前前审批修改产品相关信息
     *
     * @param approvalSaveVO
     * @param saveAssetsVO
     * @author dmz
     * @date 2017年4月6日
     */
     void updateMoneyProductInfo(ApprovalSaveVO approvalSaveVO,ReqAssetsInfoVO saveAssetsVO);

    /**
     * 改派查询接口
     *
     * @param request
     * @return
     * @author dmz
     * @date 2017年4月6日
     */
    public ResponsePage<ResBMSReassignmentVo> getLoanNoListPage(ReqBMSReassignmentVo request, RequestPage requestPage);

    /**
     * 批量改派
     *
     * @param request
     * @return
     * @author dmz
     * @date 2017年4月8日
     */
    public Result<ResReassignmentUpdVO> updateReform(ReqBMSReassignmentBatchVo request);

    /**
     * 初审批量拒绝拆分新接口
     *
     * @return
     * @author dmz
     * @date 2017年4月16日
     */
    public Result<String> updateReturnOrRejectService(ReqCsRefusePlupdateStatusVO request, String type);

    /**
     * 借款日志查询
     *
     * @param ip
     * @return
     * @author dmz
     * @date 2017年4月14日
     */
    public List<ResBMSQueryLoanLogVO> queryLoanLog(String loanNo, String ip);

    /**
     * add by zw at 2017-04-24 根据借款编号查询只读借款详细信息
     *
     * @param loanNo
     * @return ApplyEntryVO
     */
    public ApplyEntryVO getBMSLoanInfoOnlyReadByLoanNoService(String loanNo);

    /**
     * 根据借款编号获取前前只读信息
     *
     * @param loanNo
     * @param needReplaceCustomerEnum-客户信息是否转换枚举
     * @param needReplaceAssetsEnum-资产信息是否转换枚举
     * @return
     */
    ResApplicationInfoVO getMoneyLoanInfoDetail(String loanNo, boolean needReplaceCustomerEnum, boolean needReplaceAssetsEnum);

    /**
     * 查询算话反欺诈需要的借款信息
     *
     * @param loanNo
     * @return
     */
    public ResBMSAduitPersonVo getBMSLoanInfoByloanNo(String loanNo);

    /**
     * 查询信审初审待办任务数
     *
     * @return
     * @author dmz
     * @date 2017年5月23日
     */
    public Result<Integer> getFirstTaskNumber();

    /**
     * 查询信审终审待办任务数
     *
     * @return
     * @author dmz
     * @date 2017年5月23日
     */
    public Result<Integer> getFinalTaskNumber();

    /**
     * 分页查询终审工已完成队列信息
     *
     * @param requestPage
     * @param offStartDate
     * @param offEndDate
     * @param request
     * @return
     * @author JiaCX
     * @date 2017年6月14日 上午11:44:54
     */
    public ResponsePage<ResOffTheStocksAuditVO> getCompletedTask(RequestPage requestPage, String offStartDate, String offEndDate, HttpServletRequest request);

    /**
     * 分页查询终审工作台队列信息
     *
     * @param requestPage
     * @param taskType
     * @param reqFlag
     * @return
     * @author JiaCX
     * @date 2017年6月14日 上午11:52:03
     */
    public ResponsePage<ResBMSAuditVo> getZsWorkbenchList(RequestPage requestPage, String taskType, String reqFlag);

    /**
     * 终审改派工作台列表信息
     *
     * @return
     * @author JiaCX
     * @date 2017年6月16日 上午10:28:08
     */
    public PageResponse<ResBMSReassignmentVo> getFinishReformList(ReqBMSReassignmentVo request, RequestPage requestPage);

    /**
     * 更新客户信息中的联系方式
     *
     * @return
     * @author zhouwen
     * @date 2017年6月20日
     */
    public Result<String> updateCustomerContactInfo(CustomerContactInfoVO customerContactInfoVO);

    /**
     * 根据借款编号校验客户信息
     *
     * @param loanNo
     * @return
     * @author dmz
     * @date 2017年6月23日
     */
    public Result<String> validateApplyInfo(String loanNo);

    /**
     * 根据借款编号校验前前客户信息
     *
     * @param loanNo
     * @return
     * @author dmz
     * @date 2017年6月23日
     */
     Result<String> validateMoneyApplyInfo(String loanNo);
    /**
     * 根据原因码获取原因(规则引擎)
     *
     * @param reasonCode
     * @return
     * @author dmz
     * @ate 2017年7月03日
     */
    public Result<ReasonVO> queryReason(String reasonCode);

    /**
     * 插入规则引擎返回值6个(规则引擎)
     *
     * @param ruleEngineVO
     * @return
     * @author dmz
     * @ate 2017年7月03日
     */
    public Result<String> auditUpdateRulesData(RuleEngineVO ruleEngineVO);

    /**
     * 根据借款编号查询最新规则引擎返回的值(规则引擎)
     *
     * @param loanNo
     * @return
     * @author dmz
     * @ate 2017年7月03日
     */
    public Result<AuditRulesVO> queryAuditRulesData(String loanNo);

    /**
     * 保存审批意见时重新更新央行报告重绑
     *
     * @param loanNo
     * @author zhouwen
     * @date 2017年7月29日
     */
    public void updateReportIdByLoanNo(String loanNo);

    /**
     * 查询在网时长和实名认证
     *
     * @return
     * @authro wulj
     */
    public List<MobileOnline> getCreditReportInfo(String loanNo);

    /**
     * 更新借款"入网时长"和"实名认证"信息
     *
     * @param loanNo
     * @param name
     * @param idNo
     * @param longOnlineIdJson
     * @param realNameAuthIdJson
     */
    public Result<String> syncHZReportID(String loanNo, String name, String idNo, String longOnlineIdJson, String realNameAuthIdJson);

    /**
     * 规则引擎执行拒绝(初审、终审派单)
     *
     * @param reqZsUpdVO
     * @return
     * @author dmz
     * @date 2017年9月4日
     */
    public boolean ruleEngineReject(ReqZsUpdVO reqZsUpdVO);

    /**
     * 获取初审待分派申请件
     *
     * @return 初审待分派申请件
     * @author wulj
     */
    public List<ResBMSAutomaticDispatchAttrVO> getFirstDispatchOrders();

    /**
     * 获取终审待分派申请件(XSCS-PASS、HIGH-PASS)
     *
     * @return 终审待分派申请件
     * @author wulj
     */
    public List<ResBMSAutomaticDispatchAttrVO> getFinalDispatchOrders();

    /**
     * 删除申请件客户信息中的联系人
     *
     * @param request
     * @return
     * @author zhouwen
     * @date 2017年9月7日
     */
    public Result<String> deleteContactInfo(ReqAuditDifferencesVO request);

    /**
     * 所有拒绝或者退回原因
     *
     * @param reqBMSTMReasonVO
     * @return
     * @author JiaCX
     * @date 2017年9月7日 下午1:50:38
     */
    public List<ReqBMSTMReasonVO> findReasonByOperType(ReqBMSTMReasonVO reqBMSTMReasonVO);


    /**
     * 判断用户和身份证是否有改变
     *
     * @param applyBasiceInfo
     * @return
     * @author dmz
     */
    Result<Boolean> judgeCustomerOrIDNOChange(ReqInformationVO applyBasiceInfo);

    /**
     * 获取借款信息备份版本
     *
     * @param loanNo-借款编号
     * @param flag-1:初审   2:终审
     * @author dmz
     */
    String getLoanInfoBackup(String loanNo, String flag);

    /**
     * 根据字段名称获取对应的历史信息
     *
     * @param key-字段名称
     * @param loanNo-借款编号
     * @param flag-1:初审   2:终审
     * @return
     */
    String getLoanInfoHistoryColunm(String loanNo, String flag, String key);

    /**
     * 到借款查询队列数
     *
     * @param list
     * @return
     * @author JiaCX
     * @date 2017年10月11日 上午9:09:38
     */
    void getQueenNum(List<TaskNumber> list);

    /**
     * 查询队列数
     *
     * @param params 员工列表
     * @return
     * @author wulj
     */
    List<TaskNumberQueVo> getTaskNumber(List<PersonCodeAndRoleVo> params);

    /**
     * 查询队列数
     *
     * @param staffCode 工号
     * @param taskDef   {@link EnumUtils.FirstOrFinalEnum}
     * @return
     * @author wulj
     */
    TaskNumberQueVo getTaskNumberByStaffCodeAndTaskDef(String staffCode, String taskDef);

    /**
     * 组装联系人信息列表(给电核汇总用)
     *
     * @param loanNo-借款编号
     * @param name-申请人姓名
     * @param org
     * @param type      1:初审，2：终审
     * @return
     * @author Jia CX
     * @date 2017年11月30日 下午4:23:37
     * @notes
     */
    public List<JSONArray> combineContactInfo(String loanNo,String name, ResOrganizationInfo org, int type);

    /**
     * 钱钱组装联系人信息列表(给电核汇总用)
     *
     * @param loanNo-借款编号
     * @param name-申请人姓名
     * @param org
     * @param type      1:初审，2：终审
     * @return
     * @author Jia CX
     * @date 2017年11月30日 下午4:23:37
     * @notes
     */
     List<JSONArray> moneyCombineContactInfo(String loanNo,String name, ResOrganizationInfo org, int type);

    /**
     * 根据借款编号和手机号查询联系人信息(包含本人)
     *
     * @param loanNo
     * @param phone
     * @return
     */
    public List<ContactInfoVO> queryContactInfoByLoanNo(String loanNo, String phone);
}

