package com.yuminsoft.ams.system.service.quality.impl;

import com.bstek.uflo.service.ProcessService;
import com.bstek.uflo.service.StartProcessInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.ymkj.pms.biz.api.enums.OrganizationEnum;
import com.ymkj.pms.biz.api.enums.RoleEnum;
import com.ymkj.pms.biz.api.vo.request.ReqParamVO;
import com.ymkj.pms.biz.api.vo.response.ResEmployeeVO;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.common.QualityEnum;
import com.yuminsoft.ams.system.dao.quality.*;
import com.yuminsoft.ams.system.dao.system.SysParamDefineMapper;
import com.yuminsoft.ams.system.domain.quality.QualityCheckInfo;
import com.yuminsoft.ams.system.domain.quality.QualitySetInfo;
import com.yuminsoft.ams.system.domain.quality.QualitySourceInfo;
import com.yuminsoft.ams.system.domain.system.SysParamDefine;
import com.yuminsoft.ams.system.exception.BusinessException;
import com.yuminsoft.ams.system.service.pms.PmsApiService;
import com.yuminsoft.ams.system.service.quality.*;
import com.yuminsoft.ams.system.util.DateUtils;
import com.yuminsoft.ams.system.util.EnumerationUtils;
import com.yuminsoft.ams.system.util.ExcelUtils;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.vo.SortVo;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;
import com.yuminsoft.ams.system.vo.quality.QualityCheckResVo;
import com.yuminsoft.ams.system.vo.quality.QualityControlDeskVo;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class QualityInspectionProcessServiceImpl implements QualityInspectionProcessService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QualityInspectionProcessServiceImpl.class);

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private QualityControlDeskMapper qualityControlDeskMapper;

    @Autowired
    private QualityCheckInfoMapper qualityCheckInfoMapper;

    @Autowired
    private QualityCheckResMapper qualityCheckResMapper;

    @Autowired
    private QualityFeedBackMapper qualityFeedBackMapper;

    @Autowired
    private QualitySetInfoMapper qualitySetInfoMapper;

    @Autowired
    private QualitySourceService qualitySourceService;

    @Autowired
    private SysParamDefineMapper sysParamDefineMapper;

    @Autowired
    private PmsApiService pmsApiService;

    @Autowired
    private QualityCheckInfoService qualityCheckInfoService;
    
    @Autowired
    private QualityControlDeskService qualityControlDeskService;

    @Resource(name = ProcessService.BEAN_ID)
    private ProcessService processService;

    @Value("${sys.code}")
    public String sysCode;
    @Autowired
	private QualitySetService qualitySetService;

    /**
     * @Desc: 查询质检处理情况待处理列表
     * @Author: phb
     * @Date: 2017/5/17 17:52
     */
    @Override
    public ResponsePage<QualityControlDeskVo> getPageTodoList(RequestPage requestPage, QualityControlDeskVo qualityInfoVo) {
        LOGGER.info("查询质检处理情况待处理列表，{}", qualityInfoVo.toString());

        Map<String, Object> param = buildQueryParamsForTodo(qualityInfoVo);

        // 启用分页
        PageHelper.startPage(requestPage.getPage(), requestPage.getRows());
        List<QualityControlDeskVo> qualityInfoList = qualityControlDeskMapper.findInspectionTodoList(param);
        PageInfo<QualityControlDeskVo> pageInfo = new PageInfo<QualityControlDeskVo>(buildQueryResult(qualityInfoList));

        ResponsePage<QualityControlDeskVo> rp = new ResponsePage<QualityControlDeskVo>();
        rp.setRows(qualityInfoList);
        rp.setTotal(pageInfo.getTotal());

        return rp;
    }

    /**
     * @Desc: 查询质检处理情况已完成列表
     * @Author: phb
     * @Date: 2017/5/17 17:52
     */
    @Override
    public ResponsePage<QualityControlDeskVo> getPageDoneList(RequestPage requestPage, QualityControlDeskVo qualityInfoVo) {
        LOGGER.info("查询质检处理情况已完成列表，Params:{}", qualityInfoVo.toString());

        Map<String, Object> param = buildQueryParamsForDone(qualityInfoVo);

        // 启用分页
        PageHelper.startPage(requestPage.getPage(), requestPage.getRows());
        List<QualityControlDeskVo> qualityInfoList = qualityControlDeskMapper.findInspectionDoneList(param);
        PageInfo<QualityControlDeskVo> pageInfo = new PageInfo<QualityControlDeskVo>(buildQueryResult(qualityInfoList));

        ResponsePage<QualityControlDeskVo> responsePage = new ResponsePage<QualityControlDeskVo>();
        responsePage.setRows(qualityInfoList);
        responsePage.setTotal(pageInfo.getTotal());

        return responsePage;
    }

    /**
     * 构建查询条件(待处理)
     * @param qualityInfoVo
     * @return
     */
    private Map<String, Object> buildQueryParamsForTodo(QualityControlDeskVo qualityInfoVo){
        Map<String, Object> param = new HashMap<>();

        param.put("deskVo", qualityInfoVo);

        // 当前登录用户及其下属质检员
        List<String> lowerByAccount = pmsApiService.findLowerStaffByAccountAndOrgTypeCode(ShiroUtils.getAccount(), OrganizationEnum.OrgCode.QUALITY_CHECK);
        lowerByAccount.add(ShiroUtils.getAccount());
        param.put("checkUsers", lowerByAccount);

        param.put("currentUser", ShiroUtils.getAccount());

        // 进件营业部id
        if(StringUtils.isNotEmpty(qualityInfoVo.getOwningBranceId())){
            param.put("owningBranceIds", StringUtils.split(qualityInfoVo.getOwningBranceId(),",") );
        }

        // 分派完成日期 +1
        if(qualityInfoVo.getAssignDateEnd() != null){
            qualityInfoVo.setAssignDateEnd(DateUtils.addDate(qualityInfoVo.getAssignDateEnd(), 1));
        }

        // 排序字段
        List<SortVo.Order> sortList = Lists.newArrayList();
        sortList.add(new SortVo.Order(SortVo.Direction.DESC, "ifTop"));
        sortList.add(new SortVo.Order(SortVo.Direction.DESC, "d.status"));
        sortList.add(new SortVo.Order(SortVo.Direction.DESC, "d.assign_date"));
        param.put("sortList", sortList);

        return param;
    }

    /**
     * 构建查询条件(已完成)
     * @param qualityInfoVo
     * @return
     */
    private Map<String, Object> buildQueryParamsForDone(QualityControlDeskVo qualityInfoVo){
        Map<String, Object> param = new HashMap<>();

        // 查询出当前时间是否在质检周期内
        List<QualitySetInfo> currentAndLastDate = this.getCurrentAndLastDate();

        // 操作人员本人及辖下质检员已完成的所有（本周期是当前抽检设置的周期+上一次的抽检周期）历史申请件。过期情况下，处理已完成数据过期天数加上周期
        if(CollectionUtils.isEmpty(currentAndLastDate)){

            throw new BusinessException("查询质检处理情况列表失败,未找到" + new Date() + "对应的抽检率周期");

        }else if(currentAndLastDate.size()==1){

            Date startDate = DateUtils.stringToDate(currentAndLastDate.get(0).getStartDate(), "yyyy-MM-dd");
            qualityInfoVo.setLastModifiedDateStart(startDate);
            qualityInfoVo.setLastModifiedDateEnd(DateUtils.addDate(new Date(), 1));

        }else if(currentAndLastDate.size()==2){
        		Date startDate = DateUtils.stringToDate(currentAndLastDate.get(0).getStartDate(), "yyyy-MM-dd");
        		qualityInfoVo.setLastModifiedDateStart(startDate);
        		Date endDate = DateUtils.stringToDate(currentAndLastDate.get(1).getEndDate(), "yyyy-MM-dd");
        		qualityInfoVo.setLastModifiedDateEnd(DateUtils.addDate(endDate, 1));

        }

        // 完成日期
        if (qualityInfoVo.getEndDateEnd()!=null) {
            qualityInfoVo.setEndDateEnd(DateUtils.addDate(qualityInfoVo.getEndDateEnd(), 1));
        }

        //差错类型（无差错，一般差错，重大差错，预警，建议）
        String checkResult = qualityInfoVo.getCheckResult();
        if(StringUtils.isNotEmpty(checkResult)){
            checkResult = StringUtils.removePattern(checkResult, "[\\[\\]\"]");
            param.put("checkResults", StringUtils.split(checkResult, ","));
        }

        param.put("deskVo", qualityInfoVo);

        //查询出当前登录用户及其下属质检员
        List<String> lowerByAccount = pmsApiService.findLowerStaffByAccountAndOrgTypeCode(ShiroUtils.getAccount(), OrganizationEnum.OrgCode.QUALITY_CHECK);
        lowerByAccount.add(ShiroUtils.getAccount());
        param.put("checkUsers", lowerByAccount);
        
        param.put("currentUser", ShiroUtils.getAccount());

        // 进件营业部id
        String owningBranceId = qualityInfoVo.getOwningBranceId();
        if(owningBranceId!=null){
            String[] owningBranceIds = owningBranceId.split(",");
            param.put("owningBranceIds", owningBranceIds);
        }

        // 排序字段
        List<SortVo.Order> sortList = Lists.newArrayList(new SortVo.Order(SortVo.Direction.DESC, "result.status") ,new SortVo.Order(SortVo.Direction.DESC, "result.last_modified_date"));
        param.put("sortList", sortList);

        return param;
    }

    /**
     * 构造返回结果
     * @param qualityInfoList 质检查询结果
     * @return
     */
    @Override
    public List<QualityControlDeskVo> buildQueryResult(List<QualityControlDeskVo> qualityInfoList){
        String currentAccount = ShiroUtils.getAccount();
        //  填充剩余字段
        if(!CollectionUtils.isEmpty(qualityInfoList)){
            for (QualityControlDeskVo qualityInfo : qualityInfoList) {
                // 是否为本人暂存件
                if(QualityEnum.QualityStatus.QUALITY_TEMPORARY_SAVE.getCode().equals(qualityInfo.getStatus()) && currentAccount.equals(qualityInfo.getCheckUser())){
                    qualityInfo.setIfTemporal(true);
                }

                // 是否为质检复核件
                if(QualityEnum.QualityStatus.QUALITY_RECHECK.getCode().equals(qualityInfo.getStatus()) && currentAccount.equals(qualityInfo.getRecheckPerson())){
                    qualityInfo.setIfRecheck(true);
                }

                // 差错类型(文本)
                if(StringUtils.isNotEmpty(qualityInfo.getCheckResult())){
                    String value = EnumerationUtils.getValueByCode(QualityEnum.CheckResult.class, qualityInfo.getCheckResult());
                    qualityInfo.setCheckResultText(value);
                }

                // 初审姓名
                if(StringUtils.isNotEmpty(qualityInfo.getCheckPerson())){
                    String checkPersonName = qualityCheckInfoService.getNameByCode(qualityInfo.getCheckPerson());
                    qualityInfo.setCheckPersonName(checkPersonName);
                }

                // 终审姓名
                if(StringUtils.isNotEmpty(qualityInfo.getFinalPerson())){
                    String finalPersonName = qualityCheckInfoService.getNameByCode(qualityInfo.getFinalPerson());
                    qualityInfo.setFinalPersonName(finalPersonName);
                }

                // 质检员姓名
                if(StringUtils.isNotEmpty(qualityInfo.getCheckUser())){
                    String checkUserName = qualityCheckInfoService.getNameByCode(qualityInfo.getCheckUser());
                    qualityInfo.setCheckUserName(checkUserName);
                }

                // 申请来源
                if(StringUtils.isNotEmpty(qualityInfo.getSource())){
                    QualitySourceInfo qualitySourceInfo = qualitySourceService.findById(Integer.parseInt(qualityInfo.getSource()));
                    qualityInfo.setSourceText(qualitySourceInfo.getQualitySource());
                }
                
                // 是否可编辑
                if(StringUtils.isNotEmpty(qualityInfo.getLastModifiedBy())){
                	boolean ifEditor = true;
                	String isLeader = qualityControlDeskService.getQualityRolesInfo(qualityInfo.getLastModifiedBy()).get("isLeader");
                	if(qualityInfo.getCheckUser().equals(ShiroUtils.getCurrentUser().getUsercode())){//如果是本人的
                		if("0".equals(isLeader)){//最后修改人为当前用户领导
                			ifEditor = false;
                		}
                		if(null !=qualityInfo.getEndDate() && !isToday(qualityInfo.getEndDate())){
                			ifEditor = false;
                		}
                	}else{
                		if("0".equals(isLeader)){
                			ifEditor = false;
                		}
                	}
                	if(StringUtils.isNotEmpty(qualityInfo.getStatus())){
                		if(qualityInfo.getStatus().equals(QualityEnum.QualityStatus.QUALITY_RECHECK.getCode()) || qualityInfo.getStatus().equals(QualityEnum.QualityStatus.QUALITY_RECHECK_WAIT.getCode())){
                			ifEditor = false;
                		}
                	}
                	qualityInfo.setIfEditor(ifEditor);
                }
            }
        }

        return qualityInfoList;
    }

    /**
     * @Desc: 查询出当前质检周期上一次质检周期
     * @Author: phb
     * @Date: 2017/5/12 21:21
     */
    public List<QualitySetInfo> getCurrentAndLastDate() {
        try {
            List<QualitySetInfo> resultList = new ArrayList<>();

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("date", sdf.format(DateUtils.addDate(new Date(), 0)));
            map.put("isRegular", "0");
            QualitySetInfo byDate = qualitySetInfoMapper.findByDate(map);
            if (null == byDate) {//为null的话表示过期了，需要查询出上一次的加上过期天数
                //最近一次的质检周期
            	map.put("date",null);
                QualitySetInfo nearlyDate = qualitySetInfoMapper.findNearlyDate(map);
                if(nearlyDate!=null){
                    resultList.add(nearlyDate);
                }
                return resultList;
            } else {
                map.put("endDate", DateUtils.addDate(sdf.parse(byDate.getStartDate()), -1));
                //查找当前周期的上一周期
                QualitySetInfo lastDate = qualitySetInfoMapper.findLastDate(map);
                if(lastDate!=null){
                    resultList.add(lastDate);
                }
                resultList.add(byDate);
                return resultList;
            }
        } catch (ParseException e) {
            LOGGER.error("查询当前质检周期上一次质检周期失败！{}", e);
            return null;
        }
    }

    /**
     * @Desc: 导出待处理列表
     * @Author: phb
     * @Date: 2017/5/6 17:33
     */
    @Override
    @SneakyThrows
    public void exportToDoList(QualityControlDeskVo qualityControlDeskVo, HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("质检处理情况待处理列表Excel导出, Param:{}", qualityControlDeskVo.toString());

        Map<String, Object> param = buildQueryParamsForTodo(qualityControlDeskVo);

        String[] properties = {"assignDate", "customerName", "idNo", "customerType", "owningBrance", "sourceText", "loanNo", "applyDate", "productTypeName",
            "checkPersonName", "finalPersonName", "approveDate", "approvalStatusText","refuseReasonEmbed","checkUserName"};

        List<QualityControlDeskVo> list = qualityControlDeskMapper.findInspectionTodoList(param);

        ExcelUtils.exportExcel("质检处理情况待处理列表信息_"+ DateUtils.dateToString(new Date(),"yy-MM-dd") +".xlsx", properties, buildQueryResult(list), request, response, DateUtils.FORMAT_DATE_YYYY_MM_DD, QualityControlDeskVo.class);
    }

    /**
     * @Desc: 导出已完成列表
     * @Author: phb
     * @Date: 2017/5/6 17:33
     */
    @Override
    @SneakyThrows
    public void exportDoneExcel(QualityControlDeskVo qualityControlDeskVo, HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("质检处理情况已完成列表信息导出,Param:{}", qualityControlDeskVo.toString());

        Map<String, Object> param = buildQueryParamsForDone(qualityControlDeskVo);

        String[] properties = {"endDate", "customerName", "idNo", "customerType", "owningBrance", "sourceText", "loanNo", "applyDate", "productTypeName", "checkPersonName", "finalPersonName", "approveDate" , "approvalStatusText", "refuseReasonEmbed", "checkUserName", "checkResultText"};

        List<QualityControlDeskVo> list = qualityControlDeskMapper.findInspectionDoneList(param);

        ExcelUtils.exportExcel("质检处理情况已完成列表信息_"+ DateUtils.dateToString(new Date(),"yy-MM-dd") +" .xlsx",  properties, buildQueryResult(list), request, response, DateUtils.FORMAT_DATE_YYYY_MM_DD, QualityControlDeskVo.class);
    }

    /**
     * @Desc: 自动开启符合条件的反馈流程
     * @Author: phb
     * @Date: 2017/5/2 19:25
     */
    @Override
    public Result<String> qualityFeedbackJobExecute() {
        LOGGER.info("自动发起符合条件的反馈流程=========================>>>");

        Result<String> result = new Result<>(Result.Type.SUCCESS);
        /******************************查询出质检周期内满足条件的申请件********************************/
        List<QualitySetInfo> currentAndLastDate = this.getCurrentAndLastDate();
        Map<String, Object> param = new HashMap<>();
        if(CollectionUtils.isEmpty(currentAndLastDate)){
            result.setType(Result.Type.FAILURE);
            result.addMessage("发起反馈失败！");
            LOGGER.error("未找到" + new Date() + "对应的抽检率周期");
        }else if(currentAndLastDate.size()==1){
            param.put("startDate",currentAndLastDate.get(0).getStartDate());
            param.put("endDate",sdf.format(DateUtils.addDate(new Date(), 0)));
        }else if(currentAndLastDate.size()==2){
            param.put("startDate",currentAndLastDate.get(0).getStartDate());
            param.put("endDate",currentAndLastDate.get(1).getEndDate());
        }

        /*************查询出质检差错级别**********************/
        SysParamDefine sysParamDefine=new SysParamDefine();
        sysParamDefine.setParamType("check_result_code");
        List<SysParamDefine> byParamType = sysParamDefineMapper.findByParamDefine(sysParamDefine);
        List<String> checkResults = new ArrayList<>();
        checkResults.add("E_000004");
        checkResults.add("E_000003");
        checkResults.add("E_000002");
        checkResults.add("E_000001");
        param.put("checkResults",checkResults);
        List<QualityCheckResVo> qc = qualityCheckResMapper.getInProcessInfo(param); //找到需要进入流程的数据
        List<Long> resList = new ArrayList<>();
        for (QualityCheckResVo aqc : qc) {
            resList.add(aqc.getId());
        }
        if (resList.isEmpty()) {
            result.addMessage("没有需要进入反馈流程的申请件！");
            LOGGER.info("没有找到需要进入流程的数据=========================>>>");
            return result;
        }
        qualityFeedBackMapper.markCheckRes(resList);//通过修改feedback_code标注进入流程的数据

        /*****************************根据申请件的质检结论判断该申请件是进入初审反馈还是终审反馈*****************************************/
        StartProcessInfo info = new StartProcessInfo(ShiroUtils.getCurrentUser().getName());
        Map<String, Object> variables = new HashMap<>();//存放流程实例的变量
        for (QualityCheckResVo aqc : qc) {
            info.setBusinessId(aqc.getId().toString());   //resId作为流程的taskId
            ReqParamVO reqVO = new ReqParamVO();
            List<String> roleCodes = new ArrayList<>();
            List<ResEmployeeVO> list=null;//任务的处理人
            //质检管理人员发起反馈给信审组长
            if(QualityEnum.QualityTaskDef.APPLY_CHECK.getCode().equals(aqc.getCheckError())){
                //初审有差错发起初审反馈，反馈给初审组长
                roleCodes.add(RoleEnum.CHECK_GROUP_LEADER.getCode());
                reqVO.setRoleCodes(roleCodes);
                list = pmsApiService.findByOrgTypeCodeAndRole(roleCodes, OrganizationEnum.OrgCode.CHECK.getCode(), 0);
            }else if(QualityEnum.QualityTaskDef.APPLY_INFO_FINAL_AUDIT.getCode().equals(aqc.getCheckError())){
                //终审有差错发起终审反馈，反馈给终审组长
                roleCodes.add(RoleEnum.FINAL_CHECK.getCode());//TODO 终审组长平台还未提供，若果没有则找终审主管
                reqVO.setRoleCodes(roleCodes);
                list = pmsApiService.findByOrgTypeCodeAndRole(roleCodes, OrganizationEnum.OrgCode.CHECK.getCode(), 0);
            }
            //获取本次反馈任务的处理人
            if (!CollectionUtils.isEmpty(list)) {
                String nextHandler = list.get(0).getUsercode();
                variables.put("handler", nextHandler);
                info.setVariables(variables);
            } else {
                result.setType(Result.Type.FAILURE);
                result.addMessage("发起反馈失败！");
                LOGGER.error("调用findByOrgTypeCodeAndRole接口异常");
            }
            try {
                result.addMessage("发起反馈成功！");
                //启动反馈流程实例
                processService.startProcessByName(QualityEnum.QualityTaskDef.FEED_BACK_FLOW.getCode(), info);
            } catch (Exception e) {
                result.addMessage("发起反馈失败！");
                LOGGER.error("启动编号为：{}的反馈流程失败：{}", info.getBusinessId(), e);
            }
        }
        return result;
    }

    /**
     * @Desc: 修改质检库的信审人员
     * @Author: phb
     * @Date: 2017/5/9 14:18
     */
    @Override
    public Result<String> modifyApprovePerson(QualityCheckInfo qualityCheckInfo) {
        LOGGER.info("=======质检修改信审审批员========");
        Result<String> result = new Result<String>(Result.Type.SUCCESS);
        //设置初审姓名
        if (qualityCheckInfo.getCheckPerson() != null) {
            ResEmployeeVO byAccount = pmsApiService.findByAccount(qualityCheckInfo.getCheckPerson());
            if (byAccount != null) {
                qualityCheckInfo.setCheckPerson(byAccount.getUsercode());
            }
        }
        //设置终审姓名
        if (qualityCheckInfo.getFinalPerson() != null) {
            ResEmployeeVO byAccount = pmsApiService.findByAccount(qualityCheckInfo.getFinalPerson());
            if (byAccount != null) {
                qualityCheckInfo.setFinalPerson(byAccount.getUsercode());
            }
        }
        int update = qualityCheckInfoMapper.update(qualityCheckInfo);
        if (update > 0) {
            result.addMessage("修改审批员成功！");
        } else {
            result.setType(Result.Type.FAILURE);
            result.addMessage("修改审批员失败！");
            LOGGER.error("=======质检修改信审审批员失败id：{}========", qualityCheckInfo.getId());
        }
        return result;
    }
    
    private boolean isToday(Date date){
        SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd");
        if(fmt.format(date).toString().equals(fmt.format(new Date()).toString())){//格式化为相同格式
             return true;
         }else {
           return false;
         }
   }
}
