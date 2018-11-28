package com.yuminsoft.ams.system.service.quality.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ymkj.ams.api.enums.EnumConstants;
import com.ymkj.ams.api.service.approve.integrate.IntegrateSearchExecuter;
import com.ymkj.ams.api.vo.request.integratedsearch.ReqQueryLoanLogVO;
import com.ymkj.ams.api.vo.response.master.ResBMSQueryLoanLogVO;
import com.ymkj.base.core.biz.api.message.Response;
import com.ymkj.pms.biz.api.enums.OrganizationEnum;
import com.ymkj.pms.biz.api.enums.RoleEnum;
import com.ymkj.pms.biz.api.service.IEmployeeExecuter;
import com.ymkj.pms.biz.api.vo.request.ReqParamVO;
import com.ymkj.pms.biz.api.vo.response.ResEmpOrgVO;
import com.ymkj.pms.biz.api.vo.response.ResEmployeeVO;
import com.ymkj.pms.biz.api.vo.response.ResRoleVO;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.common.AmsConstants;
import com.yuminsoft.ams.system.common.EnumUtils;
import com.yuminsoft.ams.system.common.QualityEnum;
import com.yuminsoft.ams.system.dao.approve.ApprovalHistoryMapper;
import com.yuminsoft.ams.system.dao.quality.QualityCheckInfoMapper;
import com.yuminsoft.ams.system.dao.quality.QualityCheckResMapper;
import com.yuminsoft.ams.system.dao.quality.QualityControlDeskMapper;
import com.yuminsoft.ams.system.dao.quality.QualityErrorCodeMapper;
import com.yuminsoft.ams.system.domain.approve.ApplyHistory;
import com.yuminsoft.ams.system.domain.approve.ApprovalHistory;
import com.yuminsoft.ams.system.domain.quality.*;
import com.yuminsoft.ams.system.service.approve.ApplyHistoryService;
import com.yuminsoft.ams.system.service.pms.PmsApiService;
import com.yuminsoft.ams.system.service.quality.*;
import com.yuminsoft.ams.system.util.*;
import com.yuminsoft.ams.system.vo.firstApprove.ApprovalHistoryVO;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;
import com.yuminsoft.ams.system.vo.quality.QualityControlDeskToDoExportVo;
import com.yuminsoft.ams.system.vo.quality.QualityControlDeskVo;
import com.yuminsoft.ams.system.vo.quality.QualityControlQueryExportVo;
import com.yuminsoft.ams.system.vo.quality.QualityLogVo;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class QualityControlDeskServiceImpl implements QualityControlDeskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QualityControlDeskServiceImpl.class);

    @Autowired
    private QualityControlDeskMapper qualityControlDeskMapper;

    @Autowired
    private ApprovalHistoryMapper approvalHistoryMapper;

    @Autowired
    private QualityCheckInfoMapper qualityCheckInfoMapper;

    @Autowired
    private QualityCheckResMapper qualityCheckResMapper;

    @Autowired
    private QualityErrorCodeMapper errorCodeMapper;

    @Autowired
    private IntegrateSearchExecuter integrateSearchExecuter;

    @Autowired
    private PmsApiService pmsApiService;

    @Autowired
    private QualityCheckInfoService qualityCheckInfoService;

    @Autowired
    private QualityCheckResService qualityCheckResService;

    @Autowired
    private ApplyHistoryService applyHistoryService;

    @Autowired
    private QualityTaskInfoService taskInfoService;

    @Autowired
    private QualityLogService qualityLogService;

    @Autowired
    private IEmployeeExecuter iEmployeeExecuter;

    @Autowired
    private QualityInspectionProcessService qualityInspectionProcessService;

    @Value("${sys.code}")
    public String sysCode;

    @Override
    public ResponsePage<QualityControlDeskVo> getPageList(RequestPage requestPage, QualityControlDeskVo qualityControlDeskVo, String type) {
        //结束时间默认传入的是当天的00点00分00秒，可以加1天后小于即可
        Date assignDateEnd = qualityControlDeskVo.getAssignDateEnd();
        if (assignDateEnd != null) {
            qualityControlDeskVo.setAssignDateEnd(DateUtils.addDate(assignDateEnd, 1));
        }
        //查询质检工作台完成日期终止时间默认查询的是00点00分00秒，可以加1天后小于即可
        Date endDateEnd = qualityControlDeskVo.getEndDateEnd();
        if (endDateEnd != null) {
            qualityControlDeskVo.setEndDateEnd(DateUtils.addDate(endDateEnd, 1));
        }
        //综合查询页面质检员
        String checkuser = qualityControlDeskVo.getCheckUser();
        ResponsePage<QualityControlDeskVo> rp = new ResponsePage<QualityControlDeskVo>();
        PageHelper.startPage(requestPage.getPage(), requestPage.getRows());
        List<QualityControlDeskVo> list = new ArrayList<QualityControlDeskVo>();
        String userCode = ShiroUtils.getCurrentUser().getUsercode();
        String userForm = qualityControlDeskVo.getCheckUser();
        qualityControlDeskVo.setCheckUser(userCode);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //查询辖下人员
        List<String> codeList = pmsApiService.findLowerStaffByAccountAndOrgTypeCode(userCode, OrganizationEnum.OrgCode.QUALITY_CHECK);
        codeList.add(0, userCode);//把自己添加到查询人员列表里


        Map<String, Object> map = new HashMap<String, Object>();
        map.put("checkUser", userCode);
        map.put("codeList", codeList);
        map.put("qualityControlDeskVo", qualityControlDeskVo);

        //质检综合查询营业部多选
        if (qualityControlDeskVo.getOwningBranceId() != null) {
            String[] ownid = qualityControlDeskVo.getOwningBranceId().split(",");
            List<String> listown = new ArrayList<String>();
            Collections.addAll(listown, ownid);
            if (ownid.length >= 1) {
                map.put("owningBranceId", listown);
            }
        }

        //质检工作台待处理列表查询
        if (QualityEnum.MenuFlag.质检待处理.getCode().equals(type)) {
            map.put("checkStatus", AmsConstants.ZERO);
            map.put("flag", QualityEnum.MenuFlag.质检待处理.getCode());
            codeList.remove(0);
            map.put("codeList", codeList);
            if (codeList.size() <= 1) {
                map.remove("codeList");
            }
            map.put("Closed", String.valueOf(AmsConstants.ONE));
            map.put("qualityStatus", QualityEnum.QualityStatus.QUALITY_TEMPORARY_SAVE.getCode());

            LOGGER.info("质检工作台待处理列表查询，param：<{}>", JSON.toJSONString(map));
            List<QualityControlDeskVo> listVo = qualityControlDeskMapper.findQualityByUser(map);
            list = changeIdNo(listVo);

        } else if (QualityEnum.MenuFlag.质检综合查询.getCode().equals(type)) { //质检综合查询已完成信息查询
            map.put("checkStatus", "0");
            map.put("flag", QualityEnum.MenuFlag.质检综合查询.getCode());
            map.put("isClosed", AmsConstants.ONE);
            if (StringUtils.isNotEmpty(checkuser)) {
                map.put("user", checkuser);
            }
            if (StringUtils.isNotEmpty(qualityControlDeskVo.getCheckResult())) {
                String[] Result = qualityControlDeskVo.getCheckResult().split(",");
                List<String> checkResult = new ArrayList<String>();
                Collections.addAll(checkResult, Result);
                map.put("checkResult", checkResult);
            }
            LOGGER.info("质检综合查询列表查询，param：<{}>", JSON.toJSONString(map));
            list = qualityControlDeskMapper.findToDoByUser(map);
            if (list.size() > 0) {
                for (QualityControlDeskVo desk : list) {
                    if (StringUtils.isNotEmpty(desk.getCheckPerson())) {
                        desk.setCheckPerson(qualityCheckInfoService.getNameByCode(desk.getCheckPerson()));
                    }
                    if (StringUtils.isNotEmpty(desk.getFinalPerson())) {
                        desk.setFinalPerson(qualityCheckInfoService.getNameByCode(desk.getFinalPerson()));
                    }
                    if (StringUtils.isNotEmpty(desk.getCheckUser())) {
                        desk.setCheckUser(qualityCheckInfoService.getNameByCode(desk.getCheckUser()));
                    }

                }
            }
        } else if (QualityEnum.MenuFlag.质检手工分派.getCode().equals(type)) {//质检手工分派信息查询
            map.put("flag", QualityEnum.MenuFlag.质检手工分派.getCode());
            map.put("users", checkuser);
            map.put("checkStatus", AmsConstants.ONE);
            LOGGER.info("质检手工分派列表查询，param：<{}>", JSON.toJSONString(map));
            List<QualityControlDeskVo> listVo = qualityControlDeskMapper.findForAssign(map);
            list = changeIdNo(listVo);
            if (list.size() > 0) {
                for (QualityControlDeskVo desk : list) {
                    if (StringUtils.isNotEmpty(desk.getCheckPerson())) {
                        desk.setCheckPerson(qualityCheckInfoService.getNameByCode(desk.getCheckPerson()));
                    }
                    if (StringUtils.isNotEmpty(desk.getFinalPerson())) {
                        desk.setFinalPerson(qualityCheckInfoService.getNameByCode(desk.getFinalPerson()));
                    }
                    if (StringUtils.isNotEmpty(desk.getCheckUser())) {
                        desk.setCheckUser(qualityCheckInfoService.getNameByCode(desk.getCheckUser()));
                    }

                }
            }
        } else if (QualityEnum.MenuFlag.质检已完成列表.getCode().equals(type)) { //质检工作台已完成列表
            Date date = new Date();
            Date tenDays = DateUtils.addDate(date, -10);
            map.put("tenDays", sdf.format(tenDays));
            map.put("checkUser", userCode);
            map.put("checkStatus", AmsConstants.ZERO);
            map.put("Closed", AmsConstants.ONE);
            //质检完成，申请复核，申请复核等待三种转台的数据会在质检工作台显示
            map.put("qualityStatus", QualityEnum.QualityStatus.QUALITY_TEMPORARY_SAVE.getCode());
            LOGGER.info("质检工作台已完成列表查询，param：<{}>", JSON.toJSONString(map));
            List<QualityControlDeskVo> listVo = qualityControlDeskMapper.findQualityDoneByUser(map);
            List<QualityControlDeskVo> voList = qualityInspectionProcessService.buildQueryResult(listVo);
            list = changeIdNo(voList);
        }
        rp.setRows(list);
        rp.setTotal(((Page<QualityControlDeskVo>) list).getTotal());
        return rp;
    }

    /**
     * 身份证号前几位替换为*
     * @param listVo
     * @return
     */
    private List<QualityControlDeskVo> changeIdNo(List<QualityControlDeskVo> listVo) {
        List<QualityControlDeskVo> list;
        if (null != listVo && listVo.size() > 0) {
            for (QualityControlDeskVo deskVo : listVo) {
                String idNo = deskVo.getIdNo();
                if (idNo != null) {
                    deskVo.setIdNo("*" + idNo.substring(14));
                }
            }
        }
        list = listVo;
        return list;
    }

    @Override
    @SneakyThrows
    public void exportManualDispatchList(QualityControlDeskVo qualityControlDeskVo, HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("质检待分派列表Excel导出, Param:{}", qualityControlDeskVo.toString());

        //查询出当前登录用户及其下属质检员
        List<String> lowerByAccount = pmsApiService.findLowerStaffByAccountAndOrgTypeCode(ShiroUtils.getAccount(), OrganizationEnum.OrgCode.QUALITY_CHECK);
        List<String> codeList = qualityCheckInfoService.getStuffList(ShiroUtils.getAccount());
        Map<String, Object> param = new HashMap<>();
        param.put("codeList", codeList);
        param.put("qualityControlDeskVo", qualityControlDeskVo);
        param.put("flag", QualityEnum.MenuFlag.质检手工分派.getCode());
        param.put("users", qualityControlDeskVo.getCheckUser());
        lowerByAccount.add(ShiroUtils.getAccount());
        param.put("checkUsers", lowerByAccount);
        param.put("checkStatus", AmsConstants.ONE);

        //进件营业部id
        String owningBranceId = qualityControlDeskVo.getOwningBranceId();
        if (owningBranceId != null) {
            String[] owningBranceIds = owningBranceId.split(",");
            param.put("owningBranceIds", owningBranceIds);
        }

        String[] properties = {"customerName", "idNo", "customerType", "loanNo", "owningBrance", "applyDate", "productTypeName", "checkPersonName", "finalPersonName",
                "approveDate", "approvalStatusText", "refuseReasonEmbed", "sourceText", "checkUserName"};

        List<QualityControlDeskVo> list = qualityControlDeskMapper.findForAssign(param);
        if (!CollectionUtils.isEmpty(list)) {
            for (QualityControlDeskVo vo : list) {
                String checkPersonName = qualityCheckInfoService.getNameByCode(vo.getCheckPerson());
                vo.setCheckPersonName(checkPersonName);
                String finalPersonName = qualityCheckInfoService.getNameByCode(vo.getFinalPerson());
                vo.setFinalPersonName(finalPersonName);//
                String checkUserName = qualityCheckInfoService.getNameByCode(vo.getCheckUser());
                vo.setCheckUserName(checkUserName);
            }
        }

        ExcelUtils.exportExcel("质检待分派列表_" + DateUtils.dateToString(new Date(), "yy-MM-dd") + ".xlsx", properties, list, request, response, DateUtils.FORMAT_DATE_YYYY_MM_DD, QualityControlDeskVo.class);
    }

    @Override
    public List<QualityControlDeskVo> getQualityControlDeskVos(QualityControlDeskVo qualityControlDeskVo) {

        List<QualityControlDeskVo> list = new ArrayList<QualityControlDeskVo>();
        list = qualityControlDeskMapper.findToDo(qualityControlDeskVo);
        return list;
    }

    @Override
    public Map<String, Object> findApproveHistory(Map<String, Object> map) {
        Map<String, Object> reMap = new HashMap<String, Object>();
        List<Object> listCS = new ArrayList<Object>();
        List<Object> listZS = new ArrayList<Object>();
        List<Object> listLeader = new ArrayList<Object>();
        QualityCheckInfo info = qualityCheckInfoMapper.findOne(map);
        if (null != info) {
            map.put("samplingDate", info.getCreatedDate());
            List<ApprovalHistory> approveHistoryList = approvalHistoryMapper.findForQuality(map);//查询历史终审信息
            for (ApprovalHistory approvalHistory : approveHistoryList) {
                if (approvalHistory.getLastModifiedDate().getTime() > info.getCreatedDate().getTime()) {
                    continue;
                }
                //筛选初审历史信息
                if (null != approvalHistory.getRtfState()) {
                    if ("XSCS".equals(approvalHistory.getRtfState())) {
                        String approvalPerson = approvalHistory.getCurrentRole();//获取当初审核时候的角色code
                        String leaderGroup = approvalHistory.getCurrentGroup();//获取当初审核时候的小组code
                        if (null != approvalPerson && approvalPerson != "") {
                            String[] roleArry = approvalPerson.split(",");
                            String checkRole = null;
                            for (String role : roleArry) {
                                if (role.equals(RoleEnum.CHECK_GROUP_LEADER.getCode()) || role.equals(RoleEnum.CHECK_MANAGER.getCode()) || role.equals(RoleEnum.CHECK_DIRECTOR.getCode())) {
                                    listLeader.add(approvalHistory.getId());
                                    checkRole = null;
                                    break;
                                }
                                if (role.equals(RoleEnum.CHECK.getCode())) {
                                    checkRole = role;
                                }
                                ;
                            }
                            if (null != checkRole && checkRole != "") {
                                listZS.add(approvalHistory.getId());
                            }
                        }
                    }
                    //筛选终审历史信息
                    if ("XSZS".equals(approvalHistory.getRtfState())) {
                        listZS.add(approvalHistory.getId());
                    }
                }
            }
        }

        reMap.put("listCS", listCS);
        reMap.put("listZS", listZS);
        reMap.put("listLeader", listLeader);
        return reMap;
    }

    @Override
    public int update(QualityControlDeskVo controlDeskVo) {

        return 0;
    }

    @Transactional
    @Override
    public Result<String> completeQuality(List<QualityCheckResult> resList, String loanNo, String flag, String checkUser) {
        Result<String> result = new Result<>(Result.Type.SUCCESS);
        QualityCheckInfo info = qualityCheckInfoService.findQualityCheckInfoByLoanNo(loanNo);
        if (null == info) {
            result.setType(Result.Type.FAILURE);
            result.addMessage("该案件已被删除！！");
            return result;
        }

        if ("complete".equals(flag)) {//如果是点击的“完成质检”提交操作，验证是否被改派
            if (null != checkUser && !checkUser.equals(info.getCheckUser())) {
                result.setType(Result.Type.FAILURE);
                result.addMessage("该案件已改派！！");
                return result;
            }
        }

        for (QualityCheckResult checkRes : resList) {
            checkRes.setQualityCheckId(info.getId());
            checkRes.setStatus(QualityEnum.QualityStatus.QUALITY_COMPLETE.getCode());
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("createdBy", ShiroUtils.getCurrentUser().getUsercode());
            map.put("qualityCheckId", info.getId());
            if (!checkRes.getCheckPart().equals(QualityEnum.CheckPart.CHECK_LEADER_PART.getCode())) {
                map.put("approvePerson", checkRes.getApprovePerson());
            }
            map.put("checkError", checkRes.getCheckError());
            map.put("checkPart", checkRes.getCheckPart());
            int count = 0;
            QualityCheckResult findResVo = qualityCheckResService.findOne(map);//根据单号、创建人（本人）、审核人、质检模块查询质检结论
            if (null != findResVo) {//如果有，更新，没有就保存
                checkRes.setId(findResVo.getId());
                count = qualityCheckResService.update(checkRes);
            } else {
                count = qualityCheckResService.save(checkRes);
            }
            if (count <= 0) {
                result.setType(Result.Type.FAILURE);
                result.addMessage("保存失败");
                return result;
            }
        }

        //更新质检件信息
        if (QualityEnum.CheckStatus.CHECK_WAIT.getCode().equals(info.getCheckStatus())) {//如果如果质检件为未完成案件
            info.setEndDate(new Date());
        }
        info.setCheckStatus("0");
        info.setLastModifiedBy(ShiroUtils.getCurrentUser().getUsercode());
        info.setLastModifiedDate(new Date());
        int updateCount = qualityCheckInfoService.update(info);
        if (updateCount < 1) {
            result.setType(Result.Type.FAILURE);
            result.addMessage("保存失败");
            LOGGER.info("更新质检件：{}异常！！", JSON.toJSONString(info));
            return result;
        }
        //添加操作日志
        QualityLog log = null;
        if ("complete".equals(flag)) {
            // 质检完成日志
            log = new QualityLog(loanNo, null, QualityEnum.QualityLogLink.QUALITY_CHECK.getCode(), QualityEnum.QualityLogOperation.QUALITY_COMPLETE.getCode());
        } else {
            // 质检编辑日志
            log = new QualityLog(loanNo, null, QualityEnum.QualityLogLink.QUALITY_CHECK.getCode(), QualityEnum.QualityLogOperation.EDIT.getCode());
        }
        int save = qualityLogService.save(log);
        if (save > 0) {
            result.addMessage("保存成功");
        } else {
            result.addMessage("保存失败");
        }
        return result;
    }

    @Transactional
    @Override
    public Result<String> pauseQuality(List<QualityCheckResult> resList, String loanNo, String checkUser) {
        Result<String> result = new Result<>(Result.Type.SUCCESS);
        QualityCheckInfo info = qualityCheckInfoService.findQualityCheckInfoByLoanNo(loanNo);
        if (null == info) {
            result.setType(Result.Type.FAILURE);
            result.addMessage("该案件已被删除！！");
            return result;
        }
        if (!checkUser.equals(info.getCheckUser())) {
            result.setType(Result.Type.FAILURE);
            result.addMessage("该案件已改派！！");
            return result;
        }

        for (QualityCheckResult checkRes : resList) {
            checkRes.setQualityCheckId(info.getId());
            checkRes.setStatus(QualityEnum.QualityStatus.QUALITY_TEMPORARY_SAVE.getCode());
            checkRes.setLastModifiedBy(ShiroUtils.getAccount());
            checkRes.setLastModifiedDate(new Date());

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("createdBy", ShiroUtils.getCurrentUser().getUsercode());
            map.put("qualityCheckId", info.getId());
            if (!checkRes.getCheckPart().equals(QualityEnum.CheckPart.CHECK_LEADER_PART.getCode())) {
                map.put("approvePerson", checkRes.getApprovePerson());
            }
            map.put("checkError", checkRes.getCheckError());
            map.put("checkPart", checkRes.getCheckPart());
            int count = 0;
            QualityCheckResult findResVo = qualityCheckResService.findOne(map);//根据单号、创建人（本人）、审核人、质检模块查询质检结论
            if (null != findResVo) {//如果有，更新，没有就保存
                checkRes.setId(findResVo.getId());
                count = qualityCheckResService.update(checkRes);
            } else {
                count = qualityCheckResService.save(checkRes);
            }

            if (count <= 0) {
                result.setType(Result.Type.FAILURE);
                result.addMessage("保存失败");
                return result;
            }
        }
        //插入操作日志,去掉备注，保持一致
        QualityLog log = new QualityLog(loanNo, null,
                QualityEnum.QualityLogLink.QUALITY_CHECK.getCode(),
                QualityEnum.QualityLogOperation.TEMPORARY.getCode());
        int save = qualityLogService.save(log);
        if (save > 0) {
            result.addMessage("保存成功");
        } else {
            result.addMessage("保存失败");
        }
        return result;
    }

    /**
     * lihm 申请复核
     *
     * @param resList
     * @param loanNo
     * @return
     */
    @Override
    public Result<String> reCheck(HttpServletRequest request, List<QualityCheckResult> resList, String loanNo, String checkUser) {
        Result<String> result = new Result<>(Result.Type.SUCCESS);
/*		String roleCode = request.getSession().getAttribute("roleCode").toString();
        if(RoleEnum.QUALITY_CHECK_MANAGER.getCode().equals(roleCode)){
			result.setType(Result.Type.FAILURE);
			result.addMessage("质检经理无法申请复核");
			return result;
		}*/

        QualityCheckInfo info = qualityCheckInfoService.findQualityCheckInfoByLoanNo(loanNo);
        if (null == info) {
            result.setType(Result.Type.FAILURE);
            result.addMessage("该案件已被删除！！");
            return result;
        }
        if (!checkUser.equals(info.getCheckUser())) {
            result.setType(Result.Type.FAILURE);
            result.addMessage("该案件已改派！！");
            return result;
        }
        QualityTaskInfo taskInfo = getRecheckLeader(ShiroUtils.getCurrentUser().getUsercode());//获取申请复核对象
        for (QualityCheckResult checkResult : resList) {
            checkResult.setQualityCheckId(info.getId());
            if (null != taskInfo) {
                result.addMessage("申请复核成功！");
                checkResult.setStatus(QualityEnum.QualityStatus.QUALITY_RECHECK.getCode());
                checkResult.setRecheckPerson(taskInfo.getCheckUser());
            } else {
                result.addMessage("recheck_wait");
                checkResult.setStatus(QualityEnum.QualityStatus.QUALITY_RECHECK_WAIT.getCode());
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("createdBy", ShiroUtils.getCurrentUser().getUsercode());
            map.put("qualityCheckId", info.getId());
            if (!checkResult.getCheckPart().equals(QualityEnum.CheckPart.CHECK_LEADER_PART.getCode())) {
                map.put("approvePerson", checkResult.getApprovePerson());
            }
            map.put("checkPart", checkResult.getCheckPart());
            int count = 0;
            QualityCheckResult resVo = qualityCheckResService.findOne(map);//根据单号、创建人（本人）、审核人、错误类型查询质检结论
            if (null != resVo) {//如果有，更新，没有就保存
                checkResult.setId(resVo.getId());
                count = qualityCheckResService.update(checkResult);
            } else {
                count = qualityCheckResService.save(checkResult);
            }
            if (count <= 0) {
                result.setType(Result.Type.FAILURE);
                result.addMessage("申请复核失败");
                return result;
            }

            info.setLastModifiedBy(ShiroUtils.getCurrentUser().getUsercode());
            info.setEndDate(new Date());
            info.setLastModifiedDate(new Date());
            int updateCount = qualityCheckInfoService.update(info);
            if (updateCount < 1) {
                result.setType(Result.Type.FAILURE);
                result.addMessage("申请复核失败");
                LOGGER.info("更新质检件：{}异常！！", JSON.toJSONString(info));
                return result;
            }

        }
        //插入操作日志，增加质检环节
        QualityLog log = new QualityLog(loanNo, null,
                QualityEnum.QualityLogLink.QUALITY_CHECK.getCode(),
                QualityEnum.QualityLogOperation.RECHECK.getCode());
        int save = qualityLogService.save(log);
        if (save <= 0) {
            result.setType(Result.Type.FAILURE);
            result.addMessage("申请复核失败");
            return result;
        }
        return result;
    }

    /**
     * TODO 需要优化
     * 获取当前人员的质检申请复核人
     *
     * @author lihuimeng
     * @date 2017年6月10日 下午3:17:44
     */
    private QualityTaskInfo getRecheckLeader(String userCode) {
        ReqParamVO reqParamVO = new ReqParamVO();
        reqParamVO.setSysCode(sysCode);
        reqParamVO.setLoginUser(userCode);
        reqParamVO.setOrgTypeCode("qualityCheck");
        Response<List<ResEmployeeVO>> response = iEmployeeExecuter.findLeaderByAccount(reqParamVO);
        if (response != null && response.isSuccess()) {
            List<ResEmployeeVO> data = response.getData();
            if (!data.isEmpty()) {
                String leaderCode = data.get(0).getUsercode();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("checkUser", leaderCode);
                map.put("ifApplyCheck", "0");
                map.put("ifAccept", "2");
                QualityTaskInfo taskInfo = taskInfoService.findOne(map);
                if (null != taskInfo) {
                    return taskInfo;
                } else {
                    return getRecheckLeader(leaderCode);//如果该领导关闭申请复核接单，则递归继续查询
                }
            }
        }
        return null;
    }

    @Override
    public ResponsePage<QualityCheckResult> getOpoinPageList(RequestPage requestPage, QualityCheckResult checkRes) {
        ResponsePage<QualityCheckResult> rp = new ResponsePage<QualityCheckResult>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("qualityCheckId", checkRes.getQualityCheckId());
        List<QualityCheckResult> checkResList = new ArrayList<QualityCheckResult>();
        checkResList = qualityCheckResMapper.findAll(map);
        rp.setRows(checkResList);
        rp.setTotal(checkResList.size());
        return rp;
    }

    @Override
    public List<QualityErrorCode> findByName(String name) {
        return errorCodeMapper.findByName(name);
    }

    @Override
    public void exportQueryList(HttpServletRequest request, HttpServletResponse response, QualityControlDeskVo qualityControlDeskVo) {
        /**综合查询页面质检员*/
        String checkuser = qualityControlDeskVo.getCheckUser();
        String usercode = ShiroUtils.getCurrentUser().getUsercode();
        List<String> codeList = qualityCheckInfoService.getStuffList(usercode);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String[] headers = {"完成日期", "客户姓名", "身份证号", "客户类型", "进件营业部", "申请来源", "申请件编号", "贷款类型", "审批结果", "初审姓名", "终审姓名", "审批日期", "拒绝原因", "差错类型", "质检人员"};
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("checkUser", usercode);
        map.put("codeList", codeList);
        map.put("qualityControlDeskVo", qualityControlDeskVo);
        map.put("checkStatus", String.valueOf(AmsConstants.ZERO));
        map.put("flag", QualityEnum.MenuFlag.质检综合查询.getCode());
        map.put("isClosed", AmsConstants.ONE);
        if (StringUtils.isNotEmpty(checkuser)) {
            map.put("user", checkuser);
        }
        if (StringUtils.isNotEmpty(qualityControlDeskVo.getCheckResult())) {
            String[] Result = qualityControlDeskVo.getCheckResult().split(",");
            List<String> checkResult = new ArrayList<String>();
            Collections.addAll(checkResult, Result);
            map.put("checkResult", checkResult);
        }
        if (StringUtils.isEmpty(qualityControlDeskVo.getSource())) {
            qualityControlDeskVo.setSource(null);
        }
        List<QualityControlDeskVo> list = qualityControlDeskMapper.findToDoByUser(map);
        List<QualityControlQueryExportVo> exportList = new ArrayList<QualityControlQueryExportVo>();
        for (QualityControlDeskVo svo : list) {
            QualityControlQueryExportVo exportVo = new QualityControlQueryExportVo();
            try {
                ConvertUtils.register(new DateConverter(null), java.util.Date.class);
                BeanUtils.copyProperties(exportVo, svo);
                exportVo.setApproveDate(sdf.format(svo.getApproveDate()));
                if (svo.getApprovalStatus().equals(AmsConstants.ZERO)) {
                    exportVo.setApprovalStatus("通过");
                } else if (svo.getApprovalStatus().equals(AmsConstants.ONE)) {
                    exportVo.setApprovalStatus("拒绝");
                } else {
                    exportVo.setApprovalStatus("其他");
                }
                //时间处理
                if (svo.getEndDate() != null) {
                    exportVo.setEndDate(sdf.format(svo.getEndDate()));
                }
                //获取初审，终审人员name
                String checkPersonName = "";
                String finalPersonName = "";
                if (StringUtils.isNotEmpty(svo.getCheckPerson())) {
                    checkPersonName = qualityCheckInfoService.getNameByCode(svo.getCheckPerson());
                }
                if (StringUtils.isNotEmpty(svo.getFinalPerson())) {
                    finalPersonName = qualityCheckInfoService.getNameByCode(svo.getFinalPerson());
                }
                if (StringUtils.isNotEmpty(svo.getCheckUser())) {
                    exportVo.setCheckUser(qualityCheckInfoService.getNameByCode(svo.getCheckUser()));
                }
                if ("E_000000".equals(svo.getCheckResult())) {
                    exportVo.setCheckResult("无差错");
                }
                if ("E_000001".equals(svo.getCheckResult())) {
                    exportVo.setCheckResult("预警");
                }
                if ("E_000002".equals(svo.getCheckResult())) {
                    exportVo.setCheckResult("建议");
                }
                if ("E_000003".equals(svo.getCheckResult())) {
                    exportVo.setCheckResult("一般差错");
                }
                if ("E_000004".equals(svo.getCheckResult())) {
                    exportVo.setCheckResult("重大差错");
                }
                exportVo.setCheckPersonName(checkPersonName);
                exportVo.setFinalPersonName(finalPersonName);
                exportList.add(exportVo);
            } catch (Exception e) {
                LOGGER.error("质检查询列表导出异常", e);
            }
        }
        try {
            ExcelUtils.exportExcel(sdf.format(date) + "质检综合查询列表.xlsx", headers, exportList, request, response, DateUtils.FORMAT_DATE_YYYY_MM_DD);
        } catch (Exception e) {
            LOGGER.error("质检综合查询列表导出异常", e);
        }
    }

    /**
     * TODO 接口更新iEmployeeExecuter.findLowerByAccount(vo) 用getLowerEmpByAccount替换
     *
     * @param request
     * @param response
     * @param controlDeskVo
     * @param type
     * @return
     */
    @Override
    public Result<String> exportQualityList(HttpServletRequest request, HttpServletResponse response, QualityControlDeskVo controlDeskVo, String type) {
        Result<String> result = new Result<>(Result.Type.SUCCESS);
        String[] headers = {"分派日期", "客户姓名", "身份证号", "进件营业部", "申请来源", "申请件编号", "申请日期", "贷款类型", "审批结果", "初审工号", "初审人员", "终审工号", "终审人员", "审批日期", "质检人员"};

        String userCode = ShiroUtils.getCurrentUser().getUsercode();
        controlDeskVo.setCheckUser(userCode);
        ReqParamVO vo = new ReqParamVO();
        vo.setLoginUser(userCode);
        vo.setSysCode(sysCode);
        vo.setOrgTypeCode(OrganizationEnum.OrgCode.QUALITY_CHECK.getCode());
        Response<List<ResEmpOrgVO>> resVO = iEmployeeExecuter.findLowerByAccount(vo);
        List<String> codeList = new ArrayList<String>();
        codeList.add(userCode);//把自己添加到查询人员列表里
        if (null != resVO && resVO.isSuccess()) {
            List<ResEmpOrgVO> data = resVO.getData();
            for (ResEmpOrgVO resEmpOrgVO : data) {
                codeList.add(resEmpOrgVO.getUsercode());
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("checkUser", userCode);
        map.put("codeList", codeList);
        map.put("qualityControlDeskVo", controlDeskVo);

        if (null != type && type.equals(QualityEnum.MenuFlag.质检待处理.getCode())) {
            map.put("checkStatus", "1");
            map.put("flag", QualityEnum.MenuFlag.质检待处理.getCode());
            if (codeList.size() <= 1) {
                map.remove("codeList");
            }
            List<QualityControlDeskVo> listVo = qualityControlDeskMapper.findQualityByUser(map);
            List<QualityControlDeskToDoExportVo> exportList = new ArrayList<QualityControlDeskToDoExportVo>();
            for (QualityControlDeskVo svo : listVo) {
                QualityControlDeskToDoExportVo exportVo = new QualityControlDeskToDoExportVo();
                try {
                    ConvertUtils.register(new DateConverter(null), java.util.Date.class);
                    BeanUtils.copyProperties(exportVo, svo);
                    exportList.add(exportVo);
                } catch (Exception e) {
                    result.setType(Result.Type.FAILURE);
                    result.addMessage(0, "质检工作台导出异常！！");
                    LOGGER.info("质检工作台导出有误！！");
                }
            }
            try {
                ExcelUtils.exportExcel("质检工作台_待处理列表.xlsx", headers, exportList, request, response, DateUtils.FORMAT_DATA_YYYY_MM_DD_HH_MM_SS);
                result.addMessage(0, "导出成功！");
            } catch (Exception e) {
                result.setType(Result.Type.FAILURE);
                result.addMessage(0, "生成excle文件失败！！");
                LOGGER.info("质检工作台数据导出 生成文件异常！！");
            }
        }
        if (null != type && type.equals(QualityEnum.MenuFlag.质检已完成列表.getCode())) {
            map.put("checkUser", userCode);
            List<QualityControlDeskVo> listVo = qualityControlDeskMapper.findQualityDoneByUser(map);

            List<QualityControlDeskToDoExportVo> exportList = new ArrayList<QualityControlDeskToDoExportVo>();
            for (QualityControlDeskVo svo : listVo) {
                QualityControlDeskToDoExportVo exportVo = new QualityControlDeskToDoExportVo();
                try {
                    ConvertUtils.register(new DateConverter(null), java.util.Date.class);
                    BeanUtils.copyProperties(exportVo, svo);
                    exportList.add(exportVo);
                } catch (Exception e) {
                    result.setType(Result.Type.FAILURE);
                    result.addMessage(0, "质检工作台导出异常！！");
                    LOGGER.info("质检工作台已完成导出有误！！");
                }
            }
            try {
                ExcelUtils.exportExcel("质检工作台_待处理列表.xlsx", headers, exportList, request, response, DateUtils.FORMAT_DATA_YYYY_MM_DD_HH_MM_SS);
                result.addMessage(0, "导出成功！");
            } catch (Exception e) {
                result.setType(Result.Type.FAILURE);
                result.addMessage(0, "生成excle文件失败！！");
                LOGGER.info("质检工作台已完成数据导出 生成文件异常！！");
            }
        }
        return result;
    }

    @Override
    public Map<String, String> getQualityRolesInfo(String targetCode) {
        Map<String, String> map = new HashMap<String, String>();
        int isLeader = 0;

        //根据传来的参数进行查询角色信息列表
        String roleCode = null;
        int level = 0;//角色等级  质检经理：3    质检主管：2     质检组长 ：1   质检员：0
        List<ResRoleVO> roleList = pmsApiService.findRolesByAccount(targetCode);
        if (null != roleList) {
            for (ResRoleVO resVo : roleList) {
                if (null != resVo.getCode()) {
                    if (RoleEnum.QUALITY_CHECK_MANAGER.getCode().equals(resVo.getCode())) {
                        level = 3;
                        roleCode = RoleEnum.QUALITY_CHECK_MANAGER.getCode();
                    }

                    if (RoleEnum.QUALITY_CHECK_DIRECTOR.getCode().equals(resVo.getCode()) && level < 3) {
                        level = 2;
                        roleCode = RoleEnum.QUALITY_CHECK_DIRECTOR.getCode();
                    }

                    if (RoleEnum.QUALITY_CHECK_GROUP_LEADER.getCode().equals(resVo.getCode()) && level < 2) {
                        level = 1;
                        roleCode = RoleEnum.QUALITY_CHECK_GROUP_LEADER.getCode();
                    }

                    if (RoleEnum.QUALITY_CHECK.getCode().equals(resVo.getCode()) && level < 1) {
                        level = 0;
                        roleCode = RoleEnum.QUALITY_CHECK.getCode();
                    }
                }
            }
        }

        //查询当登录前角色列表
        String userRoleCode = null;
        int logerLevel = 0;
        List<ResRoleVO> roleVOList = ShiroUtils.getShiroUser().getRoles();
        if (null != roleVOList && roleVOList.size() > 0) {
            for (ResRoleVO resVo : roleVOList) {
                if (null != resVo.getCode()) {
                    if (RoleEnum.QUALITY_CHECK_MANAGER.getCode().equals(resVo.getCode())) {
                        logerLevel = 3;
                        userRoleCode = RoleEnum.QUALITY_CHECK_MANAGER.getCode();
                    }

                    if (RoleEnum.QUALITY_CHECK_DIRECTOR.getCode().equals(resVo.getCode()) && level < 3) {
                        logerLevel = 2;
                        userRoleCode = RoleEnum.QUALITY_CHECK_DIRECTOR.getCode();
                    }

                    if (RoleEnum.QUALITY_CHECK_GROUP_LEADER.getCode().equals(resVo.getCode()) && level < 2) {
                        logerLevel = 1;
                        userRoleCode = RoleEnum.QUALITY_CHECK_GROUP_LEADER.getCode();
                    }

                    if (RoleEnum.QUALITY_CHECK.getCode().equals(resVo.getCode()) && level < 1) {
                        logerLevel = 0;
                        userRoleCode = RoleEnum.QUALITY_CHECK.getCode();
                    }
                }
            }
        }

        if (logerLevel < level) {//是领导
            isLeader = 0;
        } else {
            isLeader = 1;
        }
        map.put("roleCode", roleCode);
        map.put("userRoleCode", userRoleCode);
        map.put("isLeader", isLeader + "");
        return map;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getApprovalName(String loanNo, String roleCode) {
        //获取带出领导
        String leaderCode = getFirstAppLeader(loanNo);
        List<String> firstList = (List<String>) getAppLog(loanNo).get("firstList");
        List<String> finishList = (List<String>) getAppLog(loanNo).get("finishList");
        Map<String, Object> map = new HashMap<String, Object>();
        if (RoleEnum.QUALITY_CHECK.getCode().equals(roleCode)) {
            for (int i = 0; i < firstList.size(); i++) {
                map.put(EnumConstants.RtfState.XSCS.getValue() + firstList.get(i), "初审" + (i + 1));
            }
            for (int i = 0; i < finishList.size(); i++) {
                map.put(EnumConstants.RtfState.XSZS.getValue() + finishList.get(i), "终审" + (i + 1));
            }
            if (StringUtils.isNotEmpty(leaderCode)) {
                map.put(leaderCode, "组长");
            }
        } else {
            for (int i = 0; i < firstList.size(); i++) {
                map.put(EnumConstants.RtfState.XSCS.getValue() + firstList.get(i), pmsApiService.findEmpByUserCode(firstList.get(i)).getName());
            }
            for (int i = 0; i < finishList.size(); i++) {
                map.put(EnumConstants.RtfState.XSZS.getValue() + finishList.get(i), pmsApiService.findEmpByUserCode(finishList.get(i)).getName());
            }
            if (StringUtils.isNotEmpty(leaderCode)) {
                map.put(leaderCode, pmsApiService.findEmpByUserCode(leaderCode));
            }
        }
        map.put("firstList", firstList);
        map.put("finishList", finishList);
        return map;
    }

    private String getFirstAppLeader(String loanNo) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("loanNo", loanNo);
        ApplyHistory applyHistory = applyHistoryService.getDoCheck(map);
        if (null != applyHistory && StringUtils.isNotEmpty(applyHistory.getApprovalLeader())) {
            return applyHistory.getApprovalLeader();
        }
        return null;
    }

    /**
     * 获取借款审核日志
     *
     * @author lihuimeng
     * @date 2017年6月7日 下午3:23:39
     */
    private Map<String, Object> getAppLog(String loanNo) {
        List<String> firstList = new ArrayList<>();
        List<String> finishList = new ArrayList<>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("loanNo", loanNo);
        List<ApplyHistory> appHistoryList = applyHistoryService.findAll(map);//查询此单子在审核系统的所有操作
        if (null != appHistoryList && !appHistoryList.isEmpty()) {
            for (ApplyHistory applyHistory : appHistoryList) {
                if (!StringUtils.isEmpty(applyHistory.getCheckPerson())) {//如果派单给初审人员
                    firstList.add(applyHistory.getCheckPerson());
                }
                if (!StringUtils.isEmpty(applyHistory.getFinalPerson())) {//如果派单给终审人员
                    finishList.add(applyHistory.getFinalPerson());
                }
                if (!StringUtils.isEmpty(applyHistory.getApprovalPerson())) {//如果派单给协审人员
                    finishList.add(applyHistory.getApprovalPerson());
                }
            }
        }
        map.put("firstList", removeDuplicate(firstList));
        map.put("finishList", removeDuplicate(finishList));
        return map;
    }

    @Override
    public List<QualityCheckResult> getCheckResForShow(String loanNo) {
        Map<String, Object> map = new HashMap<String, Object>();
        QualityCheckInfo checkInfo = qualityCheckInfoService.findQualityCheckInfoByLoanNo(loanNo);
        List<QualityCheckResult> result = new ArrayList<QualityCheckResult>();
        if (null == checkInfo) {
            return result;
        }
        Long qualityCheckId = checkInfo.getId();
        map.put("qualityCheckId", qualityCheckId);
        Response<List<QualityCheckResult>> response = qualityCheckResService.findAll(map);
        if (null != response && response.isSuccess()) {
            result = response.getData();
        }
        return result;
    }

    /**
     * list去重
     *
     * @author lihuimeng
     * @date 2017年6月7日 下午6:10:17
     */
    private List<String> removeDuplicate(List<String> list) {
        Set<String> set = new HashSet<String>();
        List<String> newList = new ArrayList<String>();
        for (String str : list) {
            if (set.add(str)) {
                newList.add(str);
            }
        }
        return newList;
    }


    @Override
    public List<ResBMSQueryLoanLogVO> queryLoanLog(String loanNo, String ip, Map<String, Object> map) {
        List<ResBMSQueryLoanLogVO> result = new ArrayList<ResBMSQueryLoanLogVO>();
        ReqQueryLoanLogVO request = new ReqQueryLoanLogVO();
        request.setIp(ip);
        request.setLoanNo(loanNo);
        request.setServiceCode(ShiroUtils.getAccount());
        request.setServiceName(ShiroUtils.getCurrentUser().getName());
        request.setSysCode(sysCode);
        Response<List<ResBMSQueryLoanLogVO>> response = integrateSearchExecuter.queryLoanLog(request);
        LOGGER.info("借款日志查询 params:{} result:{}", JSON.toJSONString(request), JSON.toJSONString(response));
        //  将操作人中的code转换成名字
        if (null != response && EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(response.getRepCode())) {
            result = response.getData();
            for (ResBMSQueryLoanLogVO logVo : result) {
                if (logVo.getOperationModule().contains("初审")) {
                    String userCode = logVo.getOperatorCode();
                    if (map.containsKey("XSCS" + userCode) && null != map.get("XSCS" + userCode)) {
                        logVo.setOperator(map.get("XSCS" + userCode).toString());
                    }
                }
                if (logVo.getOperationModule().contains("终审")) {
                    String userCode = logVo.getOperatorCode();
                    if (map.containsKey("XSZS" + userCode) && null != map.get("XSZS" + userCode)) {
                        logVo.setOperator(map.get("XSZS" + userCode).toString());
                    }
                }

                if (logVo.getOperationType().contains("改派") || logVo.getOperationType().contains("分派")) {//如果是改派操作，操作人修改为“管理员”，屏蔽掉备注
                    logVo.setRemark("");
                    logVo.setOperator("管理员");
                }
                if (logVo.getOperationType().contains("自动派单")) {//如果是自动派单，屏蔽掉备注
                    logVo.setRemark("");
                }
                if (logVo.getOperationType().contains("申请件维护")) {//如果是申请件维护操作，操作人修改为“管理员”
                    logVo.setOperator("管理员");
                }
                if (logVo.getOperationType().contains("申请件维护")) {//如果是申请件维护操作，操作人修改为“管理员”
                    logVo.setOperator("管理员");
                }
                if (logVo.getOperationType().contains("复核同意") || logVo.getOperationType().contains("复核不同意")) {//如果是复核同意或不同意操作，操作人修改为“管理员”
                    logVo.setOperator("管理员");
                }
            }
        }
        return result;
    }

    @Override
    public ResponsePage<QualityLogVo> queryQualityLog(RequestPage requestPage, String loanNo) {
        ResponsePage<QualityLogVo> rp = new ResponsePage<QualityLogVo>();
        PageHelper.startPage(requestPage.getPage(), requestPage.getRows());
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("loanNo", loanNo);
        //返回所有需要展示的操作
        List<String> showOperations = QualityEnum.QualityLogOperation.queryShowOperations();
        //返回所有需要展示的环节
        List<String> showLinks = QualityEnum.QualityLogLink.queryShowLinks();
        param.put("showOperations", showOperations);
        param.put("showLinks", showLinks);
        List<QualityLogVo> qualityLogList = qualityLogService.findAll(param);
        for (QualityLogVo vo : qualityLogList) {
            String linkText = EnumerationUtils.getValueByCode(QualityEnum.QualityLogLink.class, vo.getLink());
            String operationText = EnumerationUtils.getValueByCode(QualityEnum.QualityLogOperation.class, vo.getOperation());
            vo.setLinkText(linkText);
            vo.setOperationText(operationText);
            //修正系统自动场景显示处理人不正确问题
            if (!sysCode.equals(vo.getCreatedBy())) {
                ResEmployeeVO creatorVo = pmsApiService.findEmpByUserCode(vo.getCreatedBy());
                if (null != creatorVo) {
                    String creatorName = creatorVo.getName();
                    vo.setCreatorName(creatorName);
                } else {
                    vo.setCreatorName(vo.getCreatedBy());
                }
            } else {
                vo.setCreatorName("系统自动");
            }
            vo.getCreatedBy();
        }

        rp.setRows(qualityLogList);
        rp.setTotal(((Page<QualityLogVo>) qualityLogList).getTotal());
        return rp;
    }

    /**
     * 获取审核组长/主管/经理列表
     *
     * @return 审核 组长/主管/经理列表
     * @author lihm
     */
    @Override
    public List<ResEmployeeVO> getQualityLeader() {
        List<String> roleCodes = Lists.newArrayList();
        roleCodes.add(RoleEnum.CHECK_GROUP_LEADER.getCode());
        roleCodes.add(RoleEnum.CHECK_DIRECTOR.getCode());
        roleCodes.add(RoleEnum.CHECK_MANAGER.getCode());
        roleCodes.add(RoleEnum.FINAL_CHECK_GROUP_LEADER.getCode());
        roleCodes.add(RoleEnum.FINAL_CHECK_DIRECTOR.getCode());
        roleCodes.add(RoleEnum.FINAL_CHECK_MANAGER.getCode());
        List<ResEmployeeVO> checkList = pmsApiService.findByOrgTypeCodeAndRole(roleCodes, OrganizationEnum.OrgCode.CHECK.getCode(), 0);
        List<ResEmployeeVO> finalCheckList = pmsApiService.findByOrgTypeCodeAndRole(roleCodes, OrganizationEnum.OrgCode.FINAL_CHECK.getCode(), 0);
        List<ResEmployeeVO> leaderList = new ArrayList<ResEmployeeVO>();
        leaderList.addAll(checkList);
        leaderList.addAll(finalCheckList);
        Set<String> set = new HashSet<String>();
        for (int i = 0; i < leaderList.size(); i++) {
            if (!set.add(leaderList.get(i).getUsercode())) {
                leaderList.remove(i);
            }
        }
        return leaderList;
    }

    @Override
    public List<QualityControlDeskVo> findQualityDoneByUser(Map<String, Object> map) {
        return qualityControlDeskMapper.findQualityDoneByUser(map);
    }

    @Override
    public List<ApprovalHistory> setApprovalPersonName(List<ApprovalHistory> approvalHistoryList, String loanNo, HttpSession session) {
        List<ApprovalHistory> historyList = new ArrayList<ApprovalHistory>();
        for (ApprovalHistory appHistory : approvalHistoryList) {
            Map<String, Object> map = (Map<String, Object>) session.getAttribute("approvalMap");//获取session里面的审核信息
            Map<String, Object> approvalMap = (Map<String, Object>) map.get(loanNo);//获取本案件的审核信息
            String rtfState = appHistory.getRtfState();
            String approvalPerson = appHistory.getApprovalPerson();
            String approvalPersonName = approvalMap.get(rtfState + approvalPerson).toString();
            ApprovalHistoryVO ahv = BeanUtil.copyProperties(appHistory, ApprovalHistoryVO.class);
            ahv.setCreatedDate(appHistory.getCreatedDate());
            ahv.setApprovalPersonName(approvalPersonName);
            historyList.add(ahv);
        }
        return historyList;
    }

    @Override
    public String getApprovalLeader(String loanNo, String rtfState) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("loanNo", loanNo);
        params.put("rtfState", rtfState);
        ApplyHistory applyHistory = applyHistoryService.getDoCheck(params);
        if (null != applyHistory) {
            if (StringUtils.isNotEmpty(applyHistory.getApprovalLeader())) {//如果有组长，就带出组长
                return applyHistory.getApprovalLeader();
            }
            if (StringUtils.isNotEmpty(applyHistory.getApprovalDirector())) {//如果有主管，就带出主管
                return applyHistory.getApprovalDirector();
            }
            if (StringUtils.isNotEmpty(applyHistory.getApprovalManager())) {//如果有经理，就带出经理
                return applyHistory.getApprovalManager();
            }
        }
        return null;
    }
}
