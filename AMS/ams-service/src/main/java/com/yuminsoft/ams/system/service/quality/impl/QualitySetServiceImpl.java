package com.yuminsoft.ams.system.service.quality.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ymkj.ams.api.service.quality.QualityExecuter;
import com.ymkj.ams.api.vo.request.audit.ReqQualityInspectionSheetVO;
import com.ymkj.ams.api.vo.response.audit.ResQualityInspectionSheetResultVO;
import com.ymkj.ams.api.vo.response.audit.ResQualityInspectionSheetVO;
import com.ymkj.base.core.biz.api.message.PageResponse;
import com.yuminsoft.ams.system.common.AmsConstants;
import com.yuminsoft.ams.system.common.QualityEnum;
import com.yuminsoft.ams.system.dao.quality.*;
import com.yuminsoft.ams.system.domain.quality.*;
import com.yuminsoft.ams.system.exception.BusinessException;
import com.yuminsoft.ams.system.service.quality.CheckedPersonnelManagementService;
import com.yuminsoft.ams.system.service.quality.QualityCheckInfoService;
import com.yuminsoft.ams.system.service.quality.QualitySetService;
import com.yuminsoft.ams.system.util.CollectionUtils;
import com.yuminsoft.ams.system.util.DateUtils;
import com.yuminsoft.ams.system.util.Result;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * 质检抽检率设置Service
 *
 * @author sunlonggang
 */
@Service
public class QualitySetServiceImpl implements QualitySetService {
    private static final Logger LOGGER = LoggerFactory.getLogger(QualitySetServiceImpl.class);
    @Autowired
    private QualitySetInfoMapper qualitySetInfoMapper;
    @Autowired
    private QualityCheckInfoMapper qualityCheckInfoMapper;
    @Autowired
    private CheckedPersonnelManagementService checkedPersonnelManagementService;
    @Autowired
    private QualityTaskInfoMapper qualityTaskInfoMapper;
    @Autowired
    private QualityExecuter qualityExecuter;
    @Autowired
    private QualityLogMapper qualityLogMapper;
    @Autowired
    private QualityExtractCaseMapper qualityExtractCaseMapper;
    @Value("${sys.code}")
    private String sysCode;
    @Autowired
    private QualityCheckInfoService checkInfoService;

    /**
     * 抽检率设置
     */
    @Override
    public Result<String> samplingRateSave(QualitySetInfo samplingInfo) {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> nextmap = new HashMap<String, Object>();
        // 用当前设置条件去查询是否有记录，如果结果为空，则进行设置，否则代表已设置，给出提示信息
        map.put("isRegular", AmsConstants.ZERO);
        map.put("startDate", samplingInfo.getStartDate());
        map.put("endDate", samplingInfo.getEndDate());
        map.put("passRate", samplingInfo.getPassRate());
        map.put("rejectRate", samplingInfo.getRejectRate());
        // 设置下一个周期质检率
        nextmap.put("isRegular", AmsConstants.ZERO);
        nextmap.put("startDate", samplingInfo.getNextStartDate());
        nextmap.put("endDate", samplingInfo.getNextEndDate());
        nextmap.put("passRate", samplingInfo.getNextPassRate());
        nextmap.put("rejectRate", samplingInfo.getNextRejectRate());
        // 判断当前周期和下一个周期是否设置过
        List<QualitySetInfo> nextList = qualitySetInfoMapper.findAll(nextmap);
        List<QualitySetInfo> list = qualitySetInfoMapper.findAll(map);
        // 设置下一个周期对象
        QualitySetInfo next = new QualitySetInfo();
        next.setStartDate(samplingInfo.getNextStartDate());
        next.setEndDate(samplingInfo.getNextEndDate());
        next.setPassRate(samplingInfo.getNextPassRate());
        next.setRejectRate(samplingInfo.getNextRejectRate());
        next.setIsRegular(samplingInfo.getIsRegular());
        // 定义返回结果集
        Result<String> result = new Result<String>(Result.Type.SUCCESS);
        if (list.size() > 0 && nextList.size() == 0) {  //下一次抽检周期为设置
            LOGGER.info("修改下次抽检周期为start:{}end:{},通过抽检率为pass:{}，拒绝抽检率为reject:{},本次未做修改",next.getStartDate(),next.getEndDate(),next.getPassRate(),next.getRejectRate());
            updateNowRate(list.get(0));
            qualitySetInfoMapper.save(next);
            result.addMessage("质检抽检率设置成功");
        } else if (list.size() == 0 && nextList.size() > 0) { // 修改本次
            LOGGER.info("修改本次抽检周期为start:{}end:{},通过抽检率为pass:{}，拒绝抽检率为reject:{},下次未做修改",samplingInfo.getStartDate(),samplingInfo.getEndDate(),samplingInfo.getPassRate(),samplingInfo.getRejectRate());
            updateNowRate(nextList.get(0));
            qualitySetInfoMapper.save(samplingInfo);
            result.addMessage("质检抽检率设置成功");
        } else if (list.size() == 0 && nextList.size() == 0) { // 修改本次和下次
            LOGGER.info("修改本次抽检周期为start:{}end:{},通过抽检率为pass:{}，拒绝抽检率为reject:{}",samplingInfo.getStartDate(),samplingInfo.getEndDate(),samplingInfo.getPassRate(),samplingInfo.getRejectRate());
            LOGGER.info("修改下次抽检周期为start:{}end:{},通过抽检率为pass:{}，拒绝抽检率为reject:{}",next.getStartDate(),next.getEndDate(),next.getPassRate(),next.getRejectRate());
            getPeriodByStartOrEndDate(samplingInfo);
            qualitySetInfoMapper.save(samplingInfo);
            qualitySetInfoMapper.save(next);
            result.addMessage("质检抽检率设置成功");
        } else {
            result.setType(Result.Type.WARNING);
            result.addMessage("该周期已设置过抽检率");
        }
        return result;
    }

    /**
     * 未修改日期，只修改了本次的抽检率
     */
    public void updateNowRate(QualitySetInfo info) {
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("isRegular", AmsConstants.ZERO);
        map.put("endDate", DateUtils.dateToString(DateUtils.addDate(DateUtils.stringToDate(info.getStartDate(), DateUtils.FORMAT_DATE_YYYY_MM_DD), -1), DateUtils.FORMAT_DATE_YYYY_MM_DD));
        List<QualitySetInfo> list = qualitySetInfoMapper.findPeriodByStartOrEndDate(map);
        if (list.size() > 0) {
            for (QualitySetInfo qsi : list) {
                qualitySetInfoMapper.updateSetInfoNotUse(qsi.getId());
            }
        }

    }

    /**
     * 根据当前需要保存的周期查询上一个周期，并置为无效
     *
     * @ author wangzx
     * @ date 2017-10-24
     */
    public void getPeriodByStartOrEndDate(QualitySetInfo info) {
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("isRegular", AmsConstants.ZERO);
        String date = null;
        date = info.getStartDate();
        map.put("startDate", date);
        List<QualitySetInfo> listPeriod = qualitySetInfoMapper.findPeriodByStartOrEndDate(map);
        if (listPeriod.size() > 0) { // 未过期的情况
            for (QualitySetInfo setInfo : listPeriod) {
                map.put("endDate", null);
                map.put("startDate",
                        DateUtils.dateToString(DateUtils.addDate(DateUtils.stringToDate(setInfo.getEndDate(), DateUtils.FORMAT_DATE_YYYY_MM_DD), 1), DateUtils.FORMAT_DATE_YYYY_MM_DD));
                qualitySetInfoMapper.updateSetInfoNotUse(setInfo.getId());
                List<QualitySetInfo> nextPeriod = qualitySetInfoMapper.findPeriodByStartOrEndDate(map);
                if (nextPeriod.size() > 0) {
                    for (QualitySetInfo qsi : nextPeriod) {
                        qualitySetInfoMapper.updateSetInfoNotUse(qsi.getId());
                    }
                }
                map.put("startDate",null);
                updateNowRate(info);
            }
        } else if (listPeriod.size() == 0) { // 过期，本次的开始日期就是上次结束日期的下一天
            date = DateUtils.dateToString(DateUtils.addDate(DateUtils.stringToDate(info.getStartDate(), DateUtils.FORMAT_DATE_YYYY_MM_DD), -1), DateUtils.FORMAT_DATE_YYYY_MM_DD);
            map.put("startDate", null);
            map.put("endDate", date);
            listPeriod = qualitySetInfoMapper.findPeriodByStartOrEndDate(map);
            if (listPeriod.size() > 0) {
                for (QualitySetInfo qsInfo : listPeriod) {
                    qualitySetInfoMapper.updateSetInfoNotUse(qsInfo.getId()); //设置抽检周期为无效
                    map.put("startDate", null);
                    map.put("endDate", DateUtils.dateToString(DateUtils.addDate(DateUtils.stringToDate(qsInfo.getStartDate(), DateUtils.FORMAT_DATE_YYYY_MM_DD), -1), DateUtils.FORMAT_DATE_YYYY_MM_DD));
                    List<QualitySetInfo> now = qualitySetInfoMapper.findPeriodByStartOrEndDate(map);
                    if (now.size() > 0) {
                        for (QualitySetInfo qs : now) {
                            qualitySetInfoMapper.updateSetInfoNotUse(qs.getId());
                        }

                    }

                }
            }
        }

    }

    /**
     * 系统抽单_常规队列 定时任务，每天凌晨1点执行
     * @param  date 手动触发的时候才有
     */
    @Override
    public QualityExtractCase systemSamplingRegular(String date) {
        LOGGER.info("******************质检抽单开始*********************");
        // 查询被检初审人员
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ifRegular", AmsConstants.ZERO);
        List<String> listUser = getCheckedUser(map); // 获取被检人员code
        map.put("isRegular", AmsConstants.ZERO);
        if(StringUtils.isEmpty(date)){
            map.put("date", DateUtils.dateToString(new Date(), DateUtils.FORMAT_DATE_YYYY_MM_DD));
        }else{
            map.put("date", date);
        }
        QualityExtractCase qualityExtractCase = new QualityExtractCase();

        QualitySetInfo info = qualitySetInfoMapper.findByDate(map); //获取质检抽检率
        if (listUser.size() == 0 || info == null) {
            LOGGER.info("质检系统没有设置被抽检人或未找到对应周期的抽检率");
        } else {
            qualityExtractCase = systemPass(AmsConstants.ZERO, info, listUser ,date);  // 系统常规通过件
            qualityExtractCase = systemReject(AmsConstants.ZERO, qualityExtractCase, info, listUser, date);// 系统常规拒绝件
            LOGGER.info("========================质检系统抽单完成=================");
        }
        return qualityExtractCase;
    }

    /**
     * 系统自动派单
     */
    public void systemAssign() {
        // 1.查询所有质检人员为空的单子
        List<QualityCheckInfo> appList = qualityCheckInfoMapper.findApps();
        // 2.派单
        for (QualityCheckInfo checkInfo : appList) {
            // 实时查询已分派单子最少的质检员
            String checkUser = getCheckUser();
            checkInfo.setCheckUser(checkUser);
            qualityCheckInfoMapper.update(checkInfo);
        }
    }

    /**
     * 实时查询已分派单子最少的质检员
     */
    public String getCheckUser() {
        List<QualityTaskInfo> userList = qualityTaskInfoMapper.findCheckUser();
        String checkUser = "";
        if (!userList.isEmpty()) {
            checkUser = String.valueOf(userList.get(0).getId());
        }
        return checkUser;
    }

    /**
     * 查询被质检人员
     *
     * @param map
     * @return
     */
    public List<String> getCheckedUser(Map<String, Object> map) {
        List<String> listUser = checkedPersonnelManagementService.findUserId(map);
        return listUser;
    }


    /**
     * 防止数据量过大，分多次请求获取数据
     *
     * @author wangzhenxing
     * @date 2017-11-27
     */
    private Map<String, Object> getPssOrRejectList(ReqQualityInspectionSheetVO req, List<ResQualityInspectionSheetResultVO> list) {
        //存放通过件或拒绝件集合
        PageResponse<ResQualityInspectionSheetVO> page = qualityExecuter.getExtractList(req);
        LOGGER.debug("抽单查询接口 params:{} result:{}",JSON.toJSONString(req), JSON.toJSONString(page));
        HashMap<String, Object> map = Maps.newHashMap();
        if (null != page && page.isSuccess()) {
            if (page.getData() != null) {
                if ("reject".equals(req.getFlag())) {
                    map.put("passRejectCount", page.getData().getCountZsJj());  // 通过拒绝件数量
                }
                list.addAll(page.getData().getList());
                int times = (int) Math.ceil(((Integer)page.getTotalCount()).doubleValue() / req.getPageSize());             //获取总条数，判断要发几次请求
                if (req.getPageNum() < times) {
                    req.setPageNum(req.getPageNum() + 1);
                    getPssOrRejectList(req, list);
                }
            }
        } else {
            LOGGER.error("系统抽单异常 params:{} result:{} ", JSON.toJSONString(req), JSON.toJSONString(page));
            throw new BusinessException("系统抽单异常");
        }
        map.put("list", list);
        return map;
    }

    /**
     * 系统自动抽单，获取通过件或拒绝件
     *
     * @param checkType-pass:通过;reject:拒绝
     * @param listUser-被抽检人列表
     * @return
     * @throws Exception
     */
    private Map<String, Object> getAllLoanInfo(String checkType, List<String> listUser,String date) {
        ReqQualityInspectionSheetVO ris = new ReqQualityInspectionSheetVO();
        List<ResQualityInspectionSheetResultVO> getlist = Lists.newArrayList();
        ris.setSysCode(sysCode);
        if(StringUtils.isEmpty(date)){
            ris.setCurttenDate(DateUtils.addDate(new Date(), -1));
        }else{
            ris.setCurttenDate(DateUtils.stringToDate(date,DateUtils.FORMAT_DATE_YYYY_MM_DD));

        }
        ris.setUserCode(listUser);
        ris.setFlag(checkType);  //抽检类型(通过 pass;拒绝 reject)
        ris.setPageNum(AmsConstants.ONE); //从第一页开始查
        ris.setPageSize(500);    // 每次查500条
        Map<String, Object> map = Maps.newHashMap();
        try {
            map = getPssOrRejectList(ris, getlist);
        } catch (Exception e) {
            LOGGER.error("质检抽单查询(抽单类型:{}) 异常:{}", checkType, e);
        }
        return map;
    }


    /**
     * 通过件系统自动抽单
     *
     * @param isRegular-是否常规:0:常规;1:非常规
     * @param qualitySetInfo-质检抽检率设置
     * @param listUser-被抽检人列表
     * @return
     */
    public QualityExtractCase systemPass(int isRegular, QualitySetInfo qualitySetInfo, List<String> listUser,String date) {
        LOGGER.info("通过件入参 抽检率：{}，被检人员:{},手动指定的抽检日期：{}",JSON.toJSONString(qualitySetInfo),JSON.toJSONString(listUser),date);
        Map<String, Object> map = getAllLoanInfo("pass", listUser,date);// 调用bubbo接口从借款系统拿数据，获得当前人员名下所有申请件
        List<ResQualityInspectionSheetResultVO> list = (List<ResQualityInspectionSheetResultVO>) map.get("list");
        LOGGER.info("根据条件借款系统未找到前一个工作日的系统抽取通过件数据查询结果总条数:{}", list.size());
        QualityExtractCase qc = new QualityExtractCase(); // 用于存储抽检历史信息
        qc.setStartDate(DateUtils.stringToDate(qualitySetInfo.getStartDate(), DateUtils.FORMAT_DATE_YYYY_MM_DD));
        qc.setPassCount(list.size());
        if (!CollectionUtils.isEmpty(list)) {
            Map<String, Object> countMap = new HashMap<String, Object>();
            countMap.put("startDate", qualitySetInfo.getStartDate());
            Map<String, BigDecimal> counts = qualityExtractCaseMapper.getCount(countMap);
            double historyCount = counts.get("passCount") == null ? 0 : counts.get("passCount").intValue(); // 历史待抽检总数
            double needHistoryCount = counts.get("needPassCount") == null ? 0 : counts.get("needPassCount").intValue();  // 历史已抽出数
            int makrCount = 0; // 标记此次抽检总数
            int timeIntervalDay = DateUtils.daysOfTwo(new Date(), DateUtils.stringToDate(qualitySetInfo.getStartDate(), DateUtils.FORMAT_DATE_YYYYMMDD)); // 判断当前日期是否是首日抽取
            if (timeIntervalDay == 0 || historyCount == 0) { // 判断当前日期是否是首日如果是则直接按抽检率抽取，如果不是则需要判断计算抽检率与设置抽检率的大小
                makrCount = (int) Math.ceil(list.size() * Double.parseDouble(qualitySetInfo.getPassRate()) / 100); // 首日或者之前没有任何抽检数据
            } else { // 非首日
                double rate = needHistoryCount / (historyCount + list.size()); // 计算抽检率，如果计算出的抽检率大于等于当前设置的抽检率则不再抽取
                if (rate * 100 - Double.parseDouble(qualitySetInfo.getPassRate()) >= 0) {
                    LOGGER.info("质检当前抽检率为:{} 已满足设置抽检率:{} 无需抽检", rate, qualitySetInfo.getPassRate());
                    qc.setNeedPassCount(AmsConstants.ZERO);
                    return qc;
                } else {
                    //历史抽出标记质检的总数  计算本次需要抽取的件数，通过件应抽取件数 =（历史查询总数+T-1内查询总数）*抽检率- 减去以前抽出的单子总和
                    makrCount = (int) Math.ceil(((historyCount + list.size()) * Double.parseDouble(qualitySetInfo.getPassRate()) / 100 - needHistoryCount));
                }
            }
            if (makrCount > list.size()) {// 如果已抽
                makrCount = list.size();
            }
            int realCount = getNotRepeatData(makrCount, list, "pass"); // 实际入库数量
            qc.setNeedPassCount(realCount); // 保存抽检信息
        }
        LOGGER.info("质检抽单,符合条件的通过件有:{}件，实际抽:{}件",qc.getPassCount(),qc.getNeedPassCount());
        return qc;
    }

    /**
     * 根据抽检率随机取出所需要的数据
     *
     * @param count     计划抽检数
     * @param realCount 实际抽检数
     * @param list      待抽列表
     * @param type      案件类型，pass：通过件，reject :拒绝件
     */
    private int getNotRepeatData(int count, List<ResQualityInspectionSheetResultVO> list, String type) {
        Map<Integer,String>  numCount = Maps.newHashMap();
        int realCount = 0; //实际抽出总数
        while (count >0 && numCount.size() != list.size()) {
            int random = (int) (Math.random() * list.size());
            if (!numCount.containsKey(random)) {
                ResQualityInspectionSheetResultVO rqli = list.get(random);
                List<QualityCheckInfo> checkInfo = qualityCheckInfoMapper.findByLoanId(rqli.getLoanNo()); // 根据loanNo去判断本次抽出来的单子是否之前抽出来过，如果本地库存在并且质检过，则不插入该笔数据
                numCount.put(random,"");
                if (checkInfo.size() == 0) {
                   QualityCheckInfo qinfo = changeTemplete(rqli, AmsConstants.ZERO, type);
                   qualityCheckInfoMapper.save(qinfo); // 通过件入库
                   QualityLog log = new QualityLog(qinfo.getLoanNo(), null, QualityEnum.QualityLogLink.ASSIGN.getCode(), QualityEnum.QualityLogOperation.SAMPLING.getCode());
                   log.setCreatedBy(sysCode);
                   log.setLastModifiedBy(sysCode);
                   qualityLogMapper.save(log); // 插入日志
                   count--;                    // 计算抽取数减一
                   realCount++;
               }
            }
        }
        return realCount;
    }

    // 对象转换
    public QualityCheckInfo changeTemplete(ResQualityInspectionSheetResultVO rqli, int isRegular, String str) {
        QualityCheckInfo qualityCheckInfo = new QualityCheckInfo();
        if ("pass".equals(str)) {
            qualityCheckInfo.setApprovalStatus(AmsConstants.ZERO);
        } else if ("reject".equals(str)) {
            qualityCheckInfo.setApprovalStatus(AmsConstants.ONE);
        }
        qualityCheckInfo.setCheckStatus(String.valueOf(AmsConstants.ONE)); // 质检状态1待质检
        qualityCheckInfo.setIsRegular(String.valueOf(isRegular));
        qualityCheckInfo.setIsClosed(String.valueOf(AmsConstants.ONE));
        qualityCheckInfo.setLoanNo(rqli.getLoanNo()); // 借款编号
        qualityCheckInfo.setApproveState(rqli.getRtfNodeState()); // 审批结果
        qualityCheckInfo.setCustomerName(rqli.getName()); // 客户姓名
        qualityCheckInfo.setIdNo(rqli.getIdNO()); // 身份证ID
        qualityCheckInfo.setOwningBranceId(rqli.getOwningBranchId() == null ? "" : rqli.getOwningBranchId().toString()); // 进件门店ID
        qualityCheckInfo.setOwningBrance(rqli.getOwningBranch() == null ? "" : rqli.getOwningBranch().toString()); // 进件门店
        qualityCheckInfo.setApplyDate(rqli.getApplyDate()); // 申请日期
        qualityCheckInfo.setProductTypeName(rqli.getProductCd()); // 产品类型
        qualityCheckInfo.setCustomerType(rqli.getApplyType()); // 客户类型
        qualityCheckInfo.setCheckPerson(rqli.getCheckPersonCode()); // 初审工号
        qualityCheckInfo.setFinalPerson(rqli.getFinalPersonCode()); // 终审工号
        qualityCheckInfo.setApproveDate(rqli.getAccDate());// 审批日期
        qualityCheckInfo.setRefuseCodeLevelOne(rqli.getFirstLevleReasonsCode()); // 一级二级拒绝原因
        qualityCheckInfo.setRefuseCodeLevelTwo(rqli.getTwoLevleReasonsCode());
        qualityCheckInfo.setRefuseLevelOneName(rqli.getFirstLevleReasons());
        qualityCheckInfo.setRefuseLevelTwoName(rqli.getTwoLevleReasons());
        return qualityCheckInfo;
    }

    /**
     * 拒绝件系统自动抽单
     *
     * @param isRegular
     */
    public QualityExtractCase systemReject(int isRegular, QualityExtractCase qc, QualitySetInfo info, List<String> listUser,String date) {
        LOGGER.info("通过件入参 抽检率：{}，被检人员:{},手动指定的抽检日期：{}",JSON.toJSONString(info),JSON.toJSONString(listUser),date);
        Map<String, Object> map = getAllLoanInfo("reject", listUser,date);
        List<ResQualityInspectionSheetResultVO> list = (List<ResQualityInspectionSheetResultVO>) map.get("list"); // 调用bubbo接口从借款系统拿数据，获得当前人员名下所有申请件
        int passRejectCount = (int) map.get("passRejectCount"); // 通过拒绝件数量，不参与计算，只计算总数
        if (CollectionUtils.isEmpty(list)) { // 保存抽检信息
            qc.setNeedRejectCount(AmsConstants.ZERO);
            LOGGER.info("借款系统未找到前一个工作日的系统抽取拒绝件数据查询结果为空");
        } else {
            Map<String, Object> countMap = new HashMap<String, Object>();
            countMap.put("startDate", info.getStartDate());
            Map<String, BigDecimal> counts = qualityExtractCaseMapper.getCount(countMap); // 查询当前周期内抽检数，开始周期为startDate
            double historyCount = counts.get("rejectCount") == null ? 0 : counts.get("rejectCount").intValue(); // 历史待抽检总数
            double needHistoryCount = counts.get("needRejectCount") == null ? 0 : counts.get("needRejectCount").intValue(); // 历史已抽出数
            int count = 0;  //计算抽检数
            int timeIntervalDay = DateUtils.daysOfTwo(new Date(), DateUtils.stringToDate(info.getStartDate(), DateUtils.FORMAT_DATE_YYYYMMDD)); // 判断当前日期是否是首日抽取
            if (timeIntervalDay == 0 || historyCount == 0) { // 首日
                count = (int) Math.ceil((list.size() + passRejectCount) * Double.parseDouble(info.getRejectRate()) / 100);
            } else { // 非首日
                double rate = needHistoryCount / (historyCount + list.size() + passRejectCount);
                if (rate * 100 - Double.parseDouble(info.getRejectRate()) >= 0) { // 判断计算抽检率是否大于等于设置抽检率，如果大约等于则不抽取
                    count = 0;
                    LOGGER.info("质检当前抽检率已满足设置抽检率无需抽检");
                } else {
                    // 记录查询总数计算本次需要抽取的件数，通过件应抽取件数 =（历史查询总数+T-1内查询总数）*抽检率 -历史抽出总数
                    count = (int) Math.ceil(((historyCount + list.size() + passRejectCount) * Double.parseDouble(info.getRejectRate()) / 100 - needHistoryCount));
                }
            }
            if (count > list.size()) { // -历史抽出标记质检的总数
                count = list.size();
            }
            int realCount = getNotRepeatData(count, list, "reject"); // 实际入库数量
            qc.setNeedRejectCount(realCount);
        }
        qc.setRejectCount(list.size() + passRejectCount);
        qualityExtractCaseMapper.save(qc);  // 保存抽检信息
        LOGGER.info("质检抽单,符合条件的拒绝件有:{}件，实际抽:{}件",qc.getRejectCount(),qc.getNeedRejectCount());
        return qc;
    }

    // 获取本周起加下周期质检抽检率设置信息
    @Override
    public List<QualitySetInfo> getTwoSet() {
        List<QualitySetInfo> list = new ArrayList<QualitySetInfo>();
        // 获取本周起
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("date", DateUtils.dateToString(new Date(), DateUtils.FORMAT_DATE_YYYY_MM_DD));
        map.put("isRegular", AmsConstants.ZERO);
        QualitySetInfo now = qualitySetInfoMapper.findByDate(map);
        QualitySetInfo last = new QualitySetInfo();
        String startDate = null;
        if (now != null) {
            map.remove("date");
            list.add(now);
            // 获取下本周起的上一周期
            /** 得打本周起的结束日期+1，作为下一周期的开始日期 **/
            startDate = DateUtils.dateToString(DateUtils.addDate(DateUtils.stringToDate(now.getEndDate(), DateUtils.FORMAT_DATE_YYYY_MM_DD), 1), DateUtils.FORMAT_DATE_YYYY_MM_DD);
            map.put("startDate", startDate);
            last = qualitySetInfoMapper.getLast(map);
            if (last == null) {
                last = new QualitySetInfo();
                last.setStartDate(startDate);
            }
            list.add(last);

        } else {
            // 本次抽检周期也是空查询上周期的结束日期，作为本周起的开始日期
            map.clear();
            QualitySetInfo info = qualitySetInfoMapper.findByDate(map);
            if (info != null) {
                //如果获取抽检周期的起始日期大于当前日期
                Date date = DateUtils.stringToDate(info.getStartDate(), DateUtils.FORMAT_DATE_YYYY_MM_DD);
                int days = DateUtils.daysOfTwo(date, new Date());
                if (days > 0) {
                    //根据开始日期查找上一周期抽检率
                    map.put("endDate", DateUtils.addDate(date, -1));
                    QualitySetInfo nows = qualitySetInfoMapper.findNearlyDate(map);
                    list.add(nows);
                    list.add(info);
                } else {
                    now = new QualitySetInfo();
                    now.setStartDate(DateUtils.dateToString(DateUtils.addDate(DateUtils.stringToDate(info.getEndDate(), DateUtils.FORMAT_DATE_YYYY_MM_DD), 1), DateUtils.FORMAT_DATE_YYYY_MM_DD));
                    list.add(now);
                    list.add(last);
                }
            }
        }
        // 获取更新着的名字
        for (QualitySetInfo set : list) {
            if (StringUtils.isNotEmpty(set.getLastModifiedBy())) {
                set.setLastModifiedBy(checkInfoService.getNameByCode(set.getLastModifiedBy()));
            }
        }
        return list;
    }
}