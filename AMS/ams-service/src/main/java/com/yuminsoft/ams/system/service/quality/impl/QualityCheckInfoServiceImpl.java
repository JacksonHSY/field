package com.yuminsoft.ams.system.service.quality.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.StringUtil;
import com.ymkj.ams.api.service.quality.QualityExecuter;
import com.ymkj.ams.api.vo.request.audit.ReqLoanNumberVO;
import com.ymkj.ams.api.vo.response.audit.ResQualityInspectionSheetResultVO;
import com.ymkj.base.core.biz.api.message.Response;
import com.ymkj.pms.biz.api.enums.OrganizationEnum;
import com.ymkj.pms.biz.api.service.IEmployeeExecuter;
import com.ymkj.pms.biz.api.vo.request.ReqEmployeeVO;
import com.ymkj.pms.biz.api.vo.request.ReqParamVO;
import com.ymkj.pms.biz.api.vo.response.ResEmployeeVO;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.common.AmsCode;
import com.yuminsoft.ams.system.common.AmsConstants;
import com.yuminsoft.ams.system.common.QualityEnum;
import com.yuminsoft.ams.system.dao.quality.QualityCheckInfoMapper;
import com.yuminsoft.ams.system.dao.quality.QualityLogMapper;
import com.yuminsoft.ams.system.dao.quality.QualitySourceInfoMapper;
import com.yuminsoft.ams.system.domain.quality.QualityCheckInfo;
import com.yuminsoft.ams.system.domain.quality.QualityLog;
import com.yuminsoft.ams.system.domain.quality.QualitySourceInfo;
import com.yuminsoft.ams.system.excel.ExcelTemplet;
import com.yuminsoft.ams.system.service.pms.PmsApiService;
import com.yuminsoft.ams.system.service.quality.QualityCheckInfoService;
import com.yuminsoft.ams.system.service.quality.QualityLogService;
import com.yuminsoft.ams.system.service.quality.QualityTaskInfoService;
import com.yuminsoft.ams.system.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 质检信息
 *
 * @author YM10105
 * @author YM10174
 */
/**
 * @author YM10174
 *
 */

/**
 * @author YM10174
 *
 */
@Service
public class QualityCheckInfoServiceImpl implements QualityCheckInfoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(QualityCheckInfoServiceImpl.class);
    @Autowired
    private QualityCheckInfoMapper qualityCheckInfoMapper;
    @Autowired
    private QualityExecuter qualityExecuter;
    @Autowired
    private QualitySourceInfoMapper qualitySourceInfoMapper;
    @Autowired
    private QualityLogService qualityLogService;
    @Autowired
    private QualityTaskInfoService taskInfoService;
    @Autowired
    private PmsApiService pmsApiService;
    @Value("${sys.code}")
    public String sysCode;
    @Autowired
    private IEmployeeExecuter employeeExecuter;
    @Autowired
    private QualityLogMapper qualityLogMapper;

    /**
     * 手工质检申请派单_关闭
     * @param ids
     * @return
     */
    @Override
    public Result<String> closes(String[] ids) {
        LOGGER.info("质检手工分派关闭，param：<{}>", JSON.toJSONString(ids));
        Result<String> result = new Result<>(Result.Type.SUCCESS);
        try {
            int closeCount = qualityCheckInfoMapper.closes(ids);
            result.addMessage("成功关闭了" + closeCount + "条！！");
        } catch (Exception e) {
            LOGGER.error("批量关闭异常,exception:", e);
            result.setType(Result.Type.FAILURE);
        }
        for (String id : ids) {
            QualityCheckInfo info = findById(Long.parseLong(id));
            QualityLog log = new QualityLog(info.getLoanNo(), QualityEnum.QualityLogOperation.CLOSE.getCode(),
                    QualityEnum.QualityLogLink.ASSIGN.getCode(), QualityEnum.QualityLogOperation.CLOSE.getCode());
            LOGGER.info("质检关闭接单添加日志，param：<{}>",JSON.toJSONString(log));
            try {
                qualityLogService.save(log);
            } catch (Exception e) {
                LOGGER.error("添加日志异常！！,单号：<{}>,exception:", log.getLoanNo(),e);
                result.setType(Result.Type.FAILURE);
            }
        }
        return result;
    }

    //校验录入数据，并关联完整数据
    @Override
    public void importLoanInfo(List<Map<String, String>> sheetDataList) {
        String applyno = null;
        String customerName = null;
        String idCard = null;
        String applySource = null;
        String str = "失败：";
        for (Map<String, String> map : sheetDataList) {
            applyno = map.get("applyNo");
            customerName = map.get("customerName");
            idCard = map.get("idCard");
            applySource = map.get("applySource");
            try {
                //对必输项进行校验
                if (StringUtil.isEmpty(applyno) || 14 != applyno.length()) {
                    map.put(ExcelTemplet.FEED_BACK_MSG, str + "申请编号为空或不是新借款编号!");
                    continue;
                }
                if (StringUtil.isEmpty(customerName)) {
                    map.put(ExcelTemplet.FEED_BACK_MSG, str + "客户姓名为空!");
                    continue;
                }
                if (StringUtil.isEmpty(idCard)) {
                    map.put(ExcelTemplet.FEED_BACK_MSG, str + "身份证号码为空!");
                    continue;
                }
                if (StringUtil.isEmpty(applySource)) {
                    map.put(ExcelTemplet.FEED_BACK_MSG, str + "来源信息为空!");
                    continue;
                } else {
                    //如果信息来源不为空，判断信息来源是否在存在
                    Map<String, Object> maps = new HashMap<String, Object>();
                    maps.put("qualitySource", applySource);
                    maps.put("status", "0");
                    QualitySourceInfo qsinfo = qualitySourceInfoMapper.findUse(maps);
                    if (qsinfo == null) {
                        map.put(ExcelTemplet.FEED_BACK_MSG, str + "来源信息不存在或未开启!");
                        continue;
                    }
                }
                Map<String, Object> findMap = new HashMap<String, Object>();
                findMap.put("loanNo", applyno);
                QualityCheckInfo checkInfo = qualityCheckInfoMapper.findOne(findMap);
                if (checkInfo != null) {
                    map.put(ExcelTemplet.FEED_BACK_MSG, str + "当前系统已存在该笔数据！");
                    continue;
                }
                ReqLoanNumberVO vo = new ReqLoanNumberVO();
                //不为空的数据一次性传给借款系统
                List<ReqLoanNumberVO> queryBms = new ArrayList<ReqLoanNumberVO>();
                vo.setSysCode(sysCode);
                vo.setLoanNo(applyno);
                vo.setName(customerName);
                vo.setIdNo(idCard);
                queryBms.add(vo);
                Response<List<ResQualityInspectionSheetResultVO>> responseVo = qualityExecuter.getApplications(queryBms);
                List<ResQualityInspectionSheetResultVO> newList = responseVo.getData();
                if (newList.size() > 0) {
                    // 判断申请来源是否可用
                    Map<String, Object> sourceMap = new HashMap<String, Object>();
                    sourceMap.put("qualitySource", applySource);
                    sourceMap.put("status", "0");
                    QualitySourceInfo sourceInfo = qualitySourceInfoMapper.findUse(sourceMap);
                    QualityCheckInfo info = getQualityCheckInfo(newList.get(0));
                    info.setSource(sourceInfo.getId().toString());
                    qualityCheckInfoMapper.save(info);
                    // 插入日志
                    QualityLog log = new QualityLog(applyno, "人工导入",
                            QualityEnum.QualityLogLink.SAMPLING.getCode(),//导入案例属于人工派单
                            QualityEnum.QualityLogOperation.IMPORT_CASE.getCode());
                    qualityLogMapper.save(log);

                    map.put(ExcelTemplet.FEED_BACK_MSG, "成功！");
                    continue;
                } else {
                    map.put(ExcelTemplet.FEED_BACK_MSG, str + "数据不匹配！");
                    continue;
                }
            } catch (Exception e) {
                LOGGER.error("质检导入失败" + e);
            }
        }
    }

    /**
     * 返回质检处理对象
     */
    public QualityCheckInfo getQualityCheckInfo(ResQualityInspectionSheetResultVO res) {
        QualityCheckInfo info = new QualityCheckInfo();
        info.setLoanNo(res.getLoanNo());
        info.setCustomerName(res.getName());
        info.setIdNo(res.getIdNO());
        info.setOwningBrance(res.getOwningBranch());
        info.setOwningBranceId(res.getOwningBranchId().toString());
        info.setApplyDate(res.getApplyDate());
        info.setCustomerType(res.getApplyType());
        info.setApproveState(res.getRtfNodeState());
        info.setCheckPerson(res.getCheckPersonCode());
        info.setFinalPerson(res.getFinalPersonCode());
        info.setApproveDate(res.getAccDate());
        //未关闭
        info.setIsClosed(String.valueOf(AmsConstants.ONE));
        //待质检
        info.setCheckStatus(String.valueOf(AmsConstants.ONE));
        //qc.setIsChoiced(AmsConstants.YES);
        info.setProductTypeName(res.getProductCd());
        //非常规
        info.setIsRegular(String.valueOf(AmsConstants.ONE));
        //一级二级拒绝原因
        info.setRefuseCodeLevelOne(res.getFirstLevleReasonsCode());
        info.setRefuseLevelOneName(res.getFirstLevleReasons());
        info.setRefuseCodeLevelTwo(res.getTwoLevleReasonsCode());
        info.setRefuseLevelTwoName(res.getTwoLevleReasons());
        //通过件
        if (Integer.parseInt(res.getPassFile()) >= AmsConstants.ONE) {
            info.setApprovalStatus(AmsConstants.ZERO);
        }
        //拒绝件
        if (Integer.parseInt(res.getRejectFile()) >= AmsConstants.ONE) {
            info.setApprovalStatus(AmsConstants.ONE);
        }
        //非通过非拒绝件
        if (Integer.parseInt(res.getRejectFile()) < AmsConstants.ONE && Integer.parseInt(res.getPassFile()) < AmsConstants.ONE) {
            info.setApprovalStatus(AmsConstants.TWO);
        }
        //入库
        return info;
    }

    /**
     * 手工导入关闭
     */
    @Override
    public void importDeleteInfo(List<Map<String, String>> sheetDataList) {
        String applyno = null;
        String customerName = null;
        String idCard = null;
        String str = "删除失败：";
        //查找当前角色及辖下的人员
        try {
            String userCode = ShiroUtils.getCurrentUser().getUsercode();
            ReqParamVO vo = new ReqParamVO();
            vo.setLoginUser(userCode);
            vo.setSysCode(sysCode);
            vo.setOrgTypeCode(OrganizationEnum.OrgCode.QUALITY_CHECK.getCode());
            Response<List<ResEmployeeVO>> resVO = employeeExecuter.findLower(vo);
            List<String> codeList = new ArrayList<String>();
            codeList.add(userCode);//把自己添加到查询人员列表里
            if (null != resVO && resVO.isSuccess()) {
                List<ResEmployeeVO> data = resVO.getData();
                for (ResEmployeeVO resEmpOrgVO : data) {
                    codeList.add(resEmpOrgVO.getUsercode());
                }
            }
            //List<QualityCheckInfo> list =qualityCheckInfoMapper.findByUser(findMap);
            for (Map<String, String> map : sheetDataList) {
                applyno = map.get("applyNo");
                customerName = map.get("customerName");
                idCard = map.get("idCard");
                //对必输项进行校验
                if (StringUtil.isEmpty(applyno)) {
                    map.put(ExcelTemplet.FEED_BACK_MSG, str + "申请编号为空!");
                    continue;
                }
                if (StringUtil.isEmpty(customerName)) {
                    map.put(ExcelTemplet.FEED_BACK_MSG, str + "客户姓名为空!");
                    continue;
                }
                if (StringUtil.isEmpty(idCard)) {
                    map.put(ExcelTemplet.FEED_BACK_MSG, str + "身份证号码为空!");
                    continue;
                }
                Map<String, Object> mapSrc = new HashMap<String, Object>();
                mapSrc.put("loanNo", applyno);
                mapSrc.put("checkUser", codeList);
                mapSrc.put("customerName", customerName);
                mapSrc.put("idNo", idCard);
                mapSrc.put("isClosed", AmsConstants.ONE);
                mapSrc.put("checkStatus", AmsConstants.ZERO);
                QualityCheckInfo info = qualityCheckInfoMapper.findByUser(mapSrc);
                if (info != null) {
                    qualityCheckInfoMapper.delete(info.getId());

                    // 插入日志
                    QualityLog log = new QualityLog(applyno, "导入删除", QualityEnum.QualityLogLink.ASSIGN.getCode(), QualityEnum.QualityLogOperation.IMPORT_DELETE.getCode());
                    qualityLogMapper.save(log);

                    map.put(ExcelTemplet.FEED_BACK_MSG, "成功删除");
                } else {
                    map.put(ExcelTemplet.FEED_BACK_MSG, str + "数据不匹配或不在权限范围内");
                }
            }
            //根据客户姓名，身份证号码，申请编号去质检系统查询当前人员及其辖下待处理的进件

        } catch (Exception e) {
            LOGGER.error("质检导入删除调用dubbo接口查询辖下人员失败{}", e);
        }
    }

    /**
     * @Desc: 根据申请件编号查询质检申请件信息
     * @Author: phb
     * @Date: 2017/5/2 13:47
     */
    @Override
    public QualityCheckInfo findQualityCheckInfoByLoanNo(String loanNo) {
        Map<String, Object> param = new HashMap<>();
        param.put("loanNo", loanNo);
        param.put("isClosed", "1");
        return qualityCheckInfoMapper.findOne(param);
    }

    @Override
    public int update(QualityCheckInfo info) {
        return qualityCheckInfoMapper.update(info);
    }

    /**
     * 派单专用
     * @param info
     * @return
     */
    @Override
    public int updateStatus(QualityCheckInfo info) {
        return qualityCheckInfoMapper.updateStatus(info);
    }

    @Override
    public List<QualityCheckInfo> findForAssign(Map<String, Object> map) {
        return qualityCheckInfoMapper.findForAssign(map);
    }

    @Override
    public Result<String> updateCheckUser(Map<String, Object> map, String[] ids, String checkUser) {
        Result<String> result = new Result<>(Result.Type.SUCCESS);
        ResEmployeeVO vo = pmsApiService.findByAccount(checkUser);
        LOGGER.info("根据工号查询员工信息，工号：<{}>,result:<{}>", checkUser, JSON.toJSONString(vo));
        if (null == vo || "f".equals(vo.getInActive())) {
            result.setType(Result.Type.FAILURE);
            result.addMessage("该员工不在职！！");
            return result;
        }
        try {
            map.put("ids", ids);
            map.put("checkUser", checkUser);
            int assignCount = qualityCheckInfoMapper.updateCheckUser(map);
            result.addMessage("成功分派了" + assignCount + "条！！");
        } catch (Exception e) {
            LOGGER.info("质检分派失败！！质检件id：<{}>", JSON.toJSONString(ids));
            result.setType(Result.Type.FAILURE);
        }
        for (String id : ids) {
            QualityCheckInfo info = findById(Long.parseLong(id));
            //增加质检分派环节
            QualityLog log = new QualityLog(info.getLoanNo(), "手动分派给" + checkUser,
                    QualityEnum.QualityLogLink.ASSIGN.getCode(),
                    QualityEnum.QualityLogOperation.HAND_ASSIGN.getCode());
            int save = qualityLogService.save(log);
            if (save <= 0) {
                result.setType(Result.Type.FAILURE);
                return result;
            }
        }
        return result;
    }
    //获取当前人员的下级

    /**
     * @author wangzx
     * @version 2017年6月15日
     * @param usercode
     * @return
     */
    @Override
    public List<String> getStuffList(String usercode) {
        ReqParamVO vo = new ReqParamVO();
        vo.setLoginUser(usercode);
        vo.setSysCode(sysCode);
        vo.setOrgTypeCode(OrganizationEnum.OrgCode.QUALITY_CHECK.getCode());
        Response<List<ResEmployeeVO>> resVO = employeeExecuter.findLower(vo);
        List<String> codeList = new ArrayList<String>();
        codeList.add(0, usercode);//把自己添加到查询人员列表里
        if (null != resVO && resVO.isSuccess()) {
            List<ResEmployeeVO> data = resVO.getData();
            for (ResEmployeeVO resEmpOrgVO : data) {
                codeList.add(resEmpOrgVO.getUsercode());
            }
        }
        return codeList;
    }

    /**根据code获取name
     * @author wangzx
     * @version 2017年6月15日
     * @param code
     * @return
     */
    @Override
    public String getNameByCode(String code) {
        ReqEmployeeVO revo = new ReqEmployeeVO();
        revo.setSysCode(sysCode);
        revo.setUsercode(code);
        String name = "";
        Response<ResEmployeeVO> vo = employeeExecuter.findByAccount(revo);
        if (vo.getRepCode().equals(AmsCode.RESULT_SUCCESS) && vo.getData() != null) {
            name = vo.getData().getName();
        }
        return name;
    }

    @Override
    public QualityCheckInfo findById(Long id) {
        return qualityCheckInfoMapper.findById(id);
    }
}
