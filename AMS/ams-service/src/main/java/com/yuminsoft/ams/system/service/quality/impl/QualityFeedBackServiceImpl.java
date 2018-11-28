package com.yuminsoft.ams.system.service.quality.impl;

import com.bstek.uflo.model.ProcessDefinition;
import com.bstek.uflo.model.task.Task;
import com.bstek.uflo.service.ProcessService;
import com.bstek.uflo.service.TaskService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ymkj.pms.biz.api.enums.OrganizationEnum;
import com.ymkj.pms.biz.api.enums.RoleEnum;
import com.ymkj.pms.biz.api.vo.request.ReqLevelVO;
import com.ymkj.pms.biz.api.vo.response.ResEmployeeVO;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.common.AmsConstants;
import com.yuminsoft.ams.system.common.QualityEnum;
import com.yuminsoft.ams.system.dao.quality.QualityCheckInfoMapper;
import com.yuminsoft.ams.system.dao.quality.QualityCheckResMapper;
import com.yuminsoft.ams.system.dao.quality.QualityFeedBackMapper;
import com.yuminsoft.ams.system.dao.quality.TaskMapper;
import com.yuminsoft.ams.system.domain.quality.QualityCheckInfo;
import com.yuminsoft.ams.system.domain.quality.QualityCheckResult;
import com.yuminsoft.ams.system.domain.quality.QualityFeedBack;
import com.yuminsoft.ams.system.domain.quality.QualityLog;
import com.yuminsoft.ams.system.service.pms.PmsApiService;
import com.yuminsoft.ams.system.service.quality.QualityControlDeskService;
import com.yuminsoft.ams.system.service.quality.QualityFeedBackService;
import com.yuminsoft.ams.system.service.quality.QualityLogService;
import com.yuminsoft.ams.system.util.DateUtils;
import com.yuminsoft.ams.system.util.ExcelUtils;
import com.yuminsoft.ams.system.util.HttpUtils;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.vo.pic.AttachmentVo;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;
import com.yuminsoft.ams.system.vo.quality.QualityCheckResVo;
import com.yuminsoft.ams.system.vo.quality.QualityFeedBackExportVo;
import com.yuminsoft.ams.system.vo.quality.QualityFeedBackVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static com.yuminsoft.ams.system.common.AmsConstants.*;

/**
 * Created by ZJY on 2017/2/27.
 */
@Service
public class QualityFeedBackServiceImpl implements QualityFeedBackService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QualityControlDeskService.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @Autowired
    private QualityFeedBackMapper qualityFeedBackMapper;
    @Autowired
    private QualityCheckResMapper qualityCheckResMapper;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private PmsApiService pmsApiService;
    @Autowired
    private QualityCheckInfoMapper qualityCheckInfoMapper;
    
	@Autowired
	private QualityLogService qualityLogService;
    
    @Resource(name = ProcessService.BEAN_ID)
    private ProcessService processService;

    @Resource(name = TaskService.BEAN_ID)
    private TaskService taskService;

    @Value("${sys.code}")
    private String sysCode;


    /**
     * 检反馈待处理列表
     *
     * @param requestPage
     * @param qualityFeedBack
     * @return
     */

    @Override
    public ResponsePage<QualityFeedBackVo> getPageListToDo(RequestPage requestPage, QualityFeedBackVo qualityFeedBack) {
        LOGGER.info("获取质检反馈待处理列表=========================>>>");
        List<QualityFeedBackVo> list = new ArrayList<>();
        ProcessDefinition processByName = processService.getProcessByName(QualityEnum.QualityTaskDef.FEED_BACK_FLOW.getCode());
        //查询出当前登录用户拥有的任务
        List<String> businessIdByLoginUser = taskMapper.findBusinessIdByLoginUser(qualityFeedBack.getLoginUser(), processByName.getId());
        //启用分页
        ResponsePage<QualityFeedBackVo> rp = new ResponsePage<>();
        PageHelper.startPage(requestPage.getPage(), requestPage.getRows());
        if (!CollectionUtils.isEmpty(businessIdByLoginUser)) {
            List<Long> ids = new ArrayList<>();
            for (String checkResId : businessIdByLoginUser) {
                ids.add(Long.parseLong(checkResId));
            }
            qualityFeedBack.setCheckResIds(ids);
            list.addAll(qualityFeedBackMapper.findToDo(qualityFeedBack));
        }
        PageInfo<QualityFeedBackVo> pageInfo = new PageInfo<>(list);

        /***********查询出初审小组组长****************/
        if (!CollectionUtils.isEmpty(list)) {
            for (QualityFeedBackVo qualityFeedBackVo : list) {
                String checkPerson = qualityFeedBackVo.getCheckPerson();
                if (checkPerson != null) {
                    ReqLevelVO reqVo = new ReqLevelVO();
                    reqVo.setSysCode(sysCode);
                    reqVo.setLoginUser(checkPerson);
                    reqVo.setInActive(AmsConstants.T);
                    reqVo.setStatus(AmsConstants.ZERO);
                    reqVo.setLevelType(OrganizationEnum.OrgCode.CHECK.getCode());
                    ResEmployeeVO leaderByAccount = pmsApiService.getLeaderByCode(reqVo);
                    if (leaderByAccount != null) {
                        qualityFeedBackVo.setCheckLeader(leaderByAccount.getUsercode());
                        qualityFeedBackVo.setCheckLeaderName(leaderByAccount.getName());
                    }
                }
            }
        }
        rp.setRows(list);
        rp.setTotal(pageInfo.getTotal());
        return rp;
    }

    /**
     * 质检反馈已完成列表
     *
     * @param requestPage
     * @param qualityFeedBack
     * @return
     */
    @Override
    public ResponsePage<QualityFeedBackVo> getPageListDone(RequestPage requestPage, QualityFeedBackVo qualityFeedBack) {
        LOGGER.info("获取质检反馈已完成列表=========================>>>");
        ResponsePage<QualityFeedBackVo> rp = new ResponsePage<>();
        PageHelper.startPage(requestPage.getPage(), requestPage.getRows());
        List<QualityFeedBackVo> list = qualityFeedBackMapper.findDone(qualityFeedBack);
        PageInfo<QualityFeedBackVo> pageInfo = new PageInfo<>(list);
        rp.setRows(list);
        rp.setTotal(pageInfo.getTotal());
        return rp;
    }

    /**
     * @Desc: 查询质检历史记录
     * @Author: phb
     * @Date: 2017/5/17 9:40
     */
    @Override
    public List<QualityCheckResVo> getQualityHistoryById(Long id) {
        return qualityCheckResMapper.getQualityHistoryById(id);
    }

    /**
     * @Desc: 查询质检反馈历史记录
     * @Author: phb
     * @Date: 2017/5/11 10:17
     */
    @Override
    public List<QualityFeedBack> getFeedBackHistoryByCheckResId(Long checkResId) {
        LOGGER.info("获得质检反馈历史结果相关数据=========================>>>");
        return qualityFeedBackMapper.getFeedBackHistoryByCheckResId(checkResId);
    }

    /**
     * @Desc: 任务处理人完成对应的质检反馈任务
     * @Author: phb
     * @Date: 2017/5/14 16:44
     */
    @Override
    public void finishFeedbackTask(String checkResIds) {
        String[] checkResIdStr = checkResIds.split(",");
        for (String checkResId : checkResIdStr) {
            long taskId = taskMapper.findTaskIdByBusinessId(checkResId); //UFLO_TASK表中查到taskId
            Task task = taskService.getTask(taskId);
            //开始处理一个任务
            taskService.start(taskId);
            String nodeName = task.getNodeName();//任务节点名
            LOGGER.info("======质检反馈流程：{}======", nodeName);
            QualityFeedBack latestFeedBackByChekResId = qualityFeedBackMapper.getLatestFeedBackByChekResId(Long.parseLong(checkResId));

            //查询出该申请件的质检员
            Map<String,Object> param=new HashMap<>();
            param.put("id",Long.parseLong(checkResId));
            QualityCheckResult checkResVo = qualityCheckResMapper.findOne(param);
            QualityCheckInfo qualityCheckInfo = qualityCheckInfoMapper.findById(checkResVo.getQualityCheckId());

            /***************指定下一次任务处理人,需要判断进入的是初审反馈还是终审反馈*************************/
            pickNextHandler(nodeName, latestFeedBackByChekResId.getType(), task, qualityCheckInfo.getCheckUser(), latestFeedBackByChekResId.getCheckType());
            String type = latestFeedBackByChekResId.getType();
            //确定工作流节点flow值
            String flowName = null;
            switch (type) {
                case CONFIRM:
                    flowName = QualityEnum.FeedbackResult.CONFIRM.getValue();
                    break;
                case CONFUSE:
                    flowName = QualityEnum.FeedbackResult.CONFUSE.getValue();
                    break;
                case ARBITRATION:
                    flowName = QualityEnum.FeedbackResult.ARBITRATION.getValue();
                    break;
                default:
                    flowName = QualityEnum.FeedbackResult.CONCLUSION.getValue();
            }
            //完成指定ID的任务
            taskService.complete(taskId, flowName);
        }
    }

    /**
     * 查找下一个任务的执行者
     *
     * @param nodeName
     * @param type
     * @param task
     */
    private void pickNextHandler(String nodeName, String type, Task task, String checkUser, String checkType) {
        String roleCode = null;
        Map<String, Object> variable = new HashMap<>();
        switch (nodeName) {
            case checkTeamLeaderCheck:   //质检组长核对
                if (type.equals(QualityEnum.FeedbackResult.CONFIRM.getCode())) {
                    roleCode = RoleEnum.QUALITY_CHECK_MANAGER.getCode();//质检经理
                } else {

                    if (QualityEnum.QualityTaskDef.APPLY_CHECK.getCode().equals(checkType)) {//信审组长
                        //初审反馈
                        roleCode = RoleEnum.CHECK_GROUP_LEADER.getCode();
                    } else if (QualityEnum.QualityTaskDef.APPLY_INFO_FINAL_AUDIT.getCode().equals(checkType)) {
                        //终审反馈
                        roleCode = RoleEnum.FINAL_CHECK_MANAGER.getCode();
                    }
                }
                break;
            case infoTeamLeaderCheck:  //信审组长核对（一次反馈）
                if (type.equals(QualityEnum.FeedbackResult.CONFIRM.getCode())) {
                    break;
                } else {
                    //信审组长一次反馈同时给质检员和质检主管
                    long pid = task.getProcessInstanceId();
                    List<String> approveUserList = new ArrayList<String>();
                    approveUserList.add(checkUser);//质检员
                    approveUserList.add(this.getManager(RoleEnum.QUALITY_CHECK_DIRECTOR.getCode()));//主管
                    variable.put("approveUserList", approveUserList);
                    processService.saveProcessVariables(pid, variable);
                    roleCode = null;
                }
                break;
            case checkDirectorCheck://质检主管审批核对:
                long pid = task.getProcessInstanceId();
                if (type.equals(QualityEnum.FeedbackResult.CONFIRM.getCode())) {//确认
                    variable.put("flowName", QualityEnum.FeedbackResult.CONFIRM.getCode());
                    roleCode = RoleEnum.QUALITY_CHECK_MANAGER.getCode();//质检经理
                } else {
                    variable.put("flowName", QualityEnum.FeedbackResult.CONFUSE.getCode());//争议
                    if (QualityEnum.QualityTaskDef.APPLY_INFO_FINAL_AUDIT.getCode().equals(checkType)) {//信审组长
                        //终审反馈
                        roleCode = RoleEnum.FINAL_CHECK_MANAGER.getCode();
                    } else if (QualityEnum.QualityTaskDef.APPLY_CHECK.getCode().equals(checkType)) {
                        //初审反馈
                        roleCode = RoleEnum.CHECK_GROUP_LEADER.getCode();
                    }
                }
                processService.saveProcessVariables(pid, variable);
                break;
            case infoTeamLeaderRecheck: //信审组长复核(二次反馈)
                if (type.equals(QualityEnum.FeedbackResult.CONFIRM.getCode())) {
                    break;
                } else {
                    if (QualityEnum.QualityTaskDef.APPLY_INFO_FINAL_AUDIT.getCode().equals(checkType)) {//信审主管
                        //终审反馈
                        roleCode = RoleEnum.FINAL_CHECK_MANAGER.getCode();
                    } else if (QualityEnum.QualityTaskDef.APPLY_CHECK.getCode().equals(checkType)) {
                        //初审反馈
                        roleCode = RoleEnum.CHECK_DIRECTOR.getCode();
                    }
                }
                break;
            case infoDirectorCheck: //信审主管核对（一次反馈）
                if (type.equals(QualityEnum.FeedbackResult.CONFIRM.getCode())) {
                    break;
                } else {
                    roleCode = RoleEnum.QUALITY_CHECK_MANAGER.getCode();//质检经理
                }
                break;
            case checkManagerCheck:// 质检经理核对
                if (type.equals(QualityEnum.FeedbackResult.CONFIRM.getCode())) {
                    break;
                } else {
                    if (QualityEnum.QualityTaskDef.APPLY_CHECK.getCode().equals(checkType)) {//信审主管核对
                        //初审反馈
                        roleCode = RoleEnum.CHECK_DIRECTOR.getCode();
                    } else if (QualityEnum.QualityTaskDef.APPLY_INFO_FINAL_AUDIT.getCode().equals(checkType)) {
                        //终审反馈
                        roleCode = RoleEnum.FINAL_CHECK_MANAGER.getCode();
                    }
                }
                break;
            case infoDirectorRecheck: //信审主管复核（二次反馈）
                if (type.equals(QualityEnum.FeedbackResult.CONFIRM.getCode())) {
                    break;
                } else {
                    if (QualityEnum.QualityTaskDef.APPLY_CHECK.getCode().equals(checkType)) {//信审主管复核
                        //初审反馈
                        roleCode = RoleEnum.CHECK_MANAGER.getCode();
                    } else if (QualityEnum.QualityTaskDef.APPLY_INFO_FINAL_AUDIT.getCode().equals(checkType)) {
                        //终审反馈
                        roleCode = RoleEnum.FINAL_CHECK_MANAGER.getCode();
                    }
                }
                break;
            case checkManagerArb: //质检经理仲裁定版:
                break;
            case infoManagerArb: //信审经理申请仲裁:
                roleCode = RoleEnum.QUALITY_CHECK_MANAGER.getCode();//质检经理
                break;
        }
        if (roleCode != null) {
            //=null表示流程结束
            saveHandler(task, roleCode);
        }
    }

    /**
     * @Desc: 保存下一个任务处理人
     * @Author: phb
     * @Date: 2017/5/2 19:25
     */
    private void saveHandler(Task task, String roleCode) {
        List<String> roleCodes = new ArrayList<>();
        roleCodes.add(roleCode);
        String orgTypeCode = null;
        if (roleCode.startsWith(OrganizationEnum.OrgCode.QUALITY_CHECK.getCode())) {//质检组
            orgTypeCode = OrganizationEnum.OrgCode.QUALITY_CHECK.getCode();
        } else if (roleCode.startsWith(OrganizationEnum.OrgCode.CHECK.getCode())) {//初审组
            orgTypeCode = OrganizationEnum.OrgCode.CHECK.getCode();
        } else if (roleCode.startsWith(OrganizationEnum.OrgCode.FINAL_CHECK.getCode())) {//终审组
            orgTypeCode = OrganizationEnum.OrgCode.FINAL_CHECK.getCode();
        }
        List<ResEmployeeVO> list = pmsApiService.findByOrgTypeCodeAndRole(roleCodes, orgTypeCode, 0);
        if (!CollectionUtils.isEmpty(list)) {
            String nextHandler = list.get(0).getUsercode();//获得员工工号
            long pid = task.getProcessInstanceId();
            Map<String, Object> variable = new HashMap<>();
            variable.put("handler", nextHandler);
            processService.saveProcessVariables(pid, variable);
        }
    }

    /**
     * @Desc: 返回主管
     * @Author: phb
     * @Date: 2017/5/2 19:25
     */
    private String getManager(String roleCode) {
        List<String> roleCodes = new ArrayList<>();
        roleCodes.add(roleCode);
        String orgTypeCode = null;
        if (roleCode.startsWith(OrganizationEnum.OrgCode.QUALITY_CHECK.getCode())) {//质检组
            orgTypeCode = OrganizationEnum.OrgCode.QUALITY_CHECK.getCode();
        } else if (roleCode.startsWith(OrganizationEnum.OrgCode.CHECK.getCode())) {//初审组
            orgTypeCode = OrganizationEnum.OrgCode.CHECK.getCode();
        } else if (roleCode.startsWith(OrganizationEnum.OrgCode.FINAL_CHECK.getCode())) {//终审组
            orgTypeCode = OrganizationEnum.OrgCode.FINAL_CHECK.getCode();
        }
        List<ResEmployeeVO> list = pmsApiService.findByOrgTypeCodeAndRole(roleCodes, orgTypeCode, 0);
        if (!CollectionUtils.isEmpty(list)) {
            return list.get(0).getUsercode();//获得员工工号
        }
        return null;
    }

    /**
     * @Desc: 查询质检反馈的附件
     * @Author: phb
     * @Date: 2017/4/27 11:33
     */
    @Override
    public List<AttachmentVo> findFeedBackAttachmentList(String loanNo, String picImgUrl) {
        LOGGER.info("======查询质检反馈的附件======");
        List<AttachmentVo> resultList = new ArrayList<>();
        try {
            Map<String, String> params = new HashMap<>();
            params.put("subclass_sort", "PIC3");
            params.put("appNo", loanNo);
            params.put("operator", ShiroUtils.getCurrentUser().getName());
            params.put("jobNumber", ShiroUtils.getAccount());
            //若果是多个文件夹则需要确定是哪个文件夹下的
            String doPost = HttpUtils.doPost(picImgUrl + "/api/picture/list", params, "UTF-8");
            JsonNode jsonNode = MAPPER.readTree(doPost);//把整个json转换成jsonnode对象
            if (jsonNode.get("isFail").asBoolean()) {
                LOGGER.error("查询申请编号为：{}的质检反馈附件失败.", loanNo);
                return resultList;
            } else {
                ArrayNode result = (ArrayNode) jsonNode.get("result");//通过key获取某一个json数据
                for (JsonNode node : result) {
                    AttachmentVo vo = new AttachmentVo();
                    vo.setId(node.get("id").asLong());
                    vo.setImgName(node.get("imgname").asText());
                    vo.setSaveName(node.get("savename").asText());
                    vo.setSubclassSort(node.get("subclassSort").asText());
                    vo.setUptime(node.get("uptime").asText());
                    vo.setAppNo(node.get("appNo").asText());
                    vo.setSysName(node.get("sysName").asText());
                    vo.setUrl(node.get("url").asText());
                    vo.setIfWaste(node.get("ifWaste").asText());
                    vo.setIfPatchBolt(node.get("ifPatchBolt").asText());
                    vo.setCreator(node.get("creator").asText());
                    vo.setCreateJobnum(node.get("createJobnum").asText());
                    vo.setCreateTime(node.get("createTime").asText());
                    vo.setModifier(node.get("modifier").asText());
                    vo.setModifiedJobnum(node.get("modifiedJobnum").asText());
                    vo.setModifierTime(node.get("modifierTime").asText());
                    resultList.add(vo);
                }
            }
        } catch (IOException e) {
            LOGGER.error("查询申请编号为：{}的质检反馈附件失败:{}", loanNo, e);
        }
        return resultList;
    }

    /**
     * @Desc: 根据id删除反馈附件
     * @Author: phb
     * @Date: 2017/5/6 9:19
     */
    @Override
    public Result<String> deleteAttachmentById(Long id, String jobNumber, String operator, String picImgUrl,String loanNo) {
        Result<String> result = new Result<String>(Result.Type.SUCCESS);
        try {
            Map<String, String> params = new HashMap<>();
            String ids = id.toString();
            params.put("operator", ShiroUtils.getCurrentUser().getName());
            params.put("jobNumber", ShiroUtils.getAccount());
            params.put("ids", ids);
            String doPost = HttpUtils.doPost(picImgUrl + "/api/picture/delete", params, "UTF-8");
            JsonNode jsonNode = MAPPER.readTree(doPost);
            String errorcode = jsonNode.get("errorcode").asText();
            if ("000000".equals(errorcode)) {
                result.addMessage("删除附件成功！");
                LOGGER.info("删除id为：{}的附件成功!", id);
            } else {
                result.setType(Result.Type.FAILURE);
                result.addMessage("删除附件失败！");
                LOGGER.info("删除id为：{}的附件失败!", id);
                return result;
            }
        } catch (IOException e) {
            LOGGER.info("删除id为：{}的附件失败，失败信息：{}!", id, e);
        }
		//添加操作日志
		QualityLog log = new QualityLog(loanNo, "删除附件，附件id为:"+id, QualityEnum.QualityLogLink.QUALITY_CHECK.getCode(),
				QualityEnum.QualityLogOperation.DELETE_ATTACHMENT.getCode());
		int save = qualityLogService.save(log);
		if(save == 0){
			result.setType(Result.Type.FAILURE);
			result.addMessage("删除附件失败！");
		}
        return result;
    }

    /**
     * @Desc: 导出已完成和待处理列表
     * @Author: phb
     * @Date: 2017/5/6 14:07
     */
    @Override
    public void exportToDoExcel(QualityFeedBackVo qualityFeedBackVo, HttpServletRequest req, HttpServletResponse res) {
        List<QualityFeedBackVo> toDo = qualityFeedBackMapper.findToDo(qualityFeedBackVo);
        doExport(toDo, req, res);
    }

    /**
     * @Desc: 导出已完成列表
     * @Author: phb
     * @Date: 2017/5/6 14:07
     */
    @Override
    public void exportDoneList(QualityFeedBackVo qualityFeedBackVo, HttpServletRequest req, HttpServletResponse res) {
        List<QualityFeedBackVo> done = qualityFeedBackMapper.findDone(qualityFeedBackVo);
        doExport(done, req, res);
    }

    @Override
    public QualityCheckResVo getLatestQualityHistoryById(Long qualityCheckId) {
        LOGGER.info("=========查询出id为{}最新的一条质检结论=====", qualityCheckId);
        List<QualityCheckResVo> list = qualityCheckResMapper.getQualityHistoryById(qualityCheckId);
        if (!CollectionUtils.isEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    /**
     * @Desc: 查询出最新的一条质检反馈结果
     * @Author: phb
     * @Date: 2017/5/13 20:55
     */
    @Override
    public QualityFeedBack getLatestFeedBackByChekResId(Long checkResId) {
        return qualityFeedBackMapper.getLatestFeedBackByChekResId(checkResId);
    }

    /**
     * @Desc: 保存当前操作人的一条质检反馈记录
     * @Author: phb
     * @Date: 2017/5/18 17:28
     */
    @Override
    public Result<String> saveFeedBackRecord(QualityFeedBack qualityFeedBack) {
        LOGGER.info("=============保存当前操作人的一条反馈记录=============");
        // 信审人员一旦确认流程直接结束，记录相关数据----质检经理一旦定版流程直接结束，记录相关数据
        if (qualityFeedBack.getType().equals(QualityEnum.FeedbackResult.CONCLUSION.getCode()) || qualityFeedBack.getType().equals(QualityEnum.FeedbackResult.CONFIRM.getCode())
                && qualityFeedBack.getNodeName().matches(".*信审.*")) {
        //查询出最新的一条反馈记录
        qualityFeedBackMapper.getLatestFeedBackByChekResId(qualityFeedBack.getCheckResId());
        //为了做报表分析，需要保存质检结论历史记录
        QualityCheckResult newCheckResult = new QualityCheckResult();
        Map<String, Object> param = new HashMap<>();
        param.put("id", qualityFeedBack.getCheckResId());
            QualityCheckResult oldCheckResult = qualityCheckResMapper.findOne(param);
        BeanUtils.copyProperties(oldCheckResult, newCheckResult);
//        oldCheckResult.setStatus(1L);
        oldCheckResult.setFeedbackCode(2L);
        oldCheckResult.setCreatedBy(qualityFeedBack.getNodeName().substring(0, 4));
        oldCheckResult.setLastModifiedBy(ShiroUtils.getAccount());

        //同时记录一条新的质检结论
        newCheckResult.setId(null);
        newCheckResult.setCheckError(qualityFeedBack.getCheckType());//质检结论 初审:apply-check   终审:applyinfo-finalaudit
        newCheckResult.setCheckResult(qualityFeedBack.getCheckError());//质检差错（重大差错:E_000004  一般差错:E_000003   建议:E_000002  预警:E_000001  无差错:E_000000）
        newCheckResult.setCheckView(qualityFeedBack.getOpinion());
        newCheckResult.setFeedbackCode(2L);//0：未进入反馈流程  1：进入流程  2：流程结束
        newCheckResult.setCreatedBy(qualityFeedBack.getNodeName().substring(0, 4));
        newCheckResult.setLastModifiedBy(ShiroUtils.getAccount());
        newCheckResult.setLastModifiedDate(new Date());
        QualityCheckResult qualityCheckResult = new QualityCheckResult();

        BeanUtils.copyProperties(oldCheckResult, qualityCheckResult);
        qualityCheckResMapper.update(qualityCheckResult);
        qualityCheckResMapper.save(newCheckResult);
        }
        Result<String> result = new Result<String>(Result.Type.SUCCESS);
        int back = qualityFeedBackMapper.saveFeedBackRecord(qualityFeedBack);
        if (back > 0) {
            result.addMessage("保存反馈成功！");
            return result;
        } else {
            result.setType(Result.Type.FAILURE);
            result.addMessage("保存反馈失败！");
            return result;
        }
    }

    /**
     * @Desc: 导出表格
     * @Author: phb
     * @Date: 2017/5/6 16:06
     */
    public void doExport(List<QualityFeedBackVo> rows, HttpServletRequest req, HttpServletResponse res) {
        try {
            LOGGER.info("=========导出质检反馈列表=====");
            List<QualityFeedBackExportVo> resultList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(rows)) {
                for (QualityFeedBackVo row : rows) {
                    QualityFeedBackExportVo exportVo = new QualityFeedBackExportVo();
                    //查询出初审组长
                    ReqLevelVO reqVo = new ReqLevelVO();
                    reqVo.setSysCode(sysCode);
                    reqVo.setLoginUser(row.getCheckPerson());
                    reqVo.setInActive(AmsConstants.T);
                    reqVo.setStatus(AmsConstants.ZERO);
                    reqVo.setLevelType(OrganizationEnum.OrgCode.CHECK.getCode());
                    ResEmployeeVO leaderByAccount = pmsApiService.getLeaderByCode(reqVo);
                    if (!StringUtils.isEmpty(leaderByAccount)) {
                        exportVo.setCheckGroupLeader(leaderByAccount.getName());
                    }
                    BeanUtils.copyProperties(row, exportVo);
                    resultList.add(exportVo);
                }
            }
            //TODO需要根据角色判断导出
            List<String> roleCodes = ShiroUtils.getCurrentUser().getRoleCodes();
            String fileName = "质检反馈情况导出列表数据.xlsx";
            String[] headers = {"申请件编号", "客户姓名", "身份证号", "初审姓名", "终审姓名", "初审小组", "差错等级", "质检意见"};
            ExcelUtils.exportExcel(fileName, headers, resultList, req, res, DateUtils.FORMAT_DATA_YYYY_MM_DD_HH_MM_SS);
        } catch (Exception e) {
            LOGGER.info("=========导出质检反馈列表失败：{}=====", e);
        }
    }

}
