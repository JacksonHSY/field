package com.yuminsoft.ams.system.controller.quality;

import com.alibaba.fastjson.JSON;
import com.bstek.uflo.model.task.Task;
import com.bstek.uflo.service.TaskService;
import com.ymkj.pms.biz.api.enums.RoleEnum;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.domain.quality.QualityErrorCode;
import com.yuminsoft.ams.system.domain.quality.QualityFeedBack;
import com.yuminsoft.ams.system.domain.quality.QualityLog;
import com.yuminsoft.ams.system.service.pms.PmsApiService;
import com.yuminsoft.ams.system.service.quality.*;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;
import com.yuminsoft.ams.system.vo.pic.AttachmentVo;
import com.yuminsoft.ams.system.vo.quality.QualityCheckResVo;
import com.yuminsoft.ams.system.vo.quality.QualityFeedBackVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.yuminsoft.ams.system.common.AmsConstants.*;

/**
 * @Desc: 质检反馈conroller
 * @Author: phb
 * @Date: 2017/5/10 17:17
 */
@Controller
@RequestMapping("/qualityFeedBack")
public class QualityFeedBackController {
    private static final Logger LOGGER = LoggerFactory.getLogger(QualityFeedBackController.class);
    @Autowired
    private QualityFeedBackService qualityFeedBackService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private QualityTaskService qualityTaskService;
    @Autowired
    private QualityErrorCodeService qualityErrorCodeService;
    @Autowired
    private QualityCheckResService qualityCheckResService;
    /**
     * 系统编码
     */
    @Value("${sys.code}")
    private String sysCode;
    /**
     * pic文件操作地址
     */
    @Value("${ams.pic.image.url}")
    private String picImgUrl;
    /**
     * 当前任务节点的名字
     */
    private String nodeName;
    private String loanNo;
    private Long qualityCheckInfoId;
    @Autowired
    private QualityLogService qualityLogService;
    @Autowired
    private PmsApiService pmsApiService;

    /**
     * @Desc: 质检反馈主页面
     * @Author: phb
     * @Date: 2017/4/27 11:47
     */
    @RequestMapping("/mainTable")
    public String mainTable() {
        return "quality/qualityFeedBack/mainTable";
    }

    /**
     * @Desc: 跳转到质检反馈页面
     * @Author: phb
     * @Date: 2017/5/3 9:27
     */
    @RequestMapping("/feedBackTableView")
    public String feedBackTableView(Model model, Long checkResId, Long qualityCheckId, String loanNo) {
        model.addAttribute("qualityCheckId", qualityCheckId);
        model.addAttribute("checkResId", checkResId);
        model.addAttribute("loanNo", loanNo);
        model.addAttribute("picImgUrl", picImgUrl + "/api/filedata");
        model.addAttribute("nodeKey", "qualityFeedBack");
        model.addAttribute("sysName", sysCode);
        model.addAttribute("operator", ShiroUtils.getCurrentUser().getName());
        model.addAttribute("jobNumber", ShiroUtils.getAccount());
        //根据质检结论ID查询出当前任务的节点名称
        Long taskId = qualityTaskService.getTaskIdByBusinessId(checkResId + "");
        Task task = taskService.getTask(taskId);
        this.loanNo=loanNo;
        nodeName = task.getNodeName();
        qualityCheckInfoId=qualityCheckId;

        //查询当前登录用户是否有指定角色（这里判断质检员）
        Boolean hasRole = pmsApiService.hasRole(ShiroUtils.getAccount(), RoleEnum.CHECK.getCode());

        /***************************根据不同工作流节点进入不同的反馈页面********************************************/
        if (checkDirectorCheck.equals(nodeName) && hasRole) {//质检员反馈页面
            return "quality/qualityFeedBack/qualityUserFeedBack";
        } else if (checkDirectorCheck.equals(nodeName)) {//质检主管反馈页面(质检员和质检主管反馈)
            return "quality/qualityFeedBack/qualityDirectorFeedBack";
        } else if (checkManagerCheck.equals(nodeName)) {//质检经理确认定版
            return "quality/qualityFeedBack/qualityManagerFeedBack";
        } else if (infoTeamLeaderCheck.equals(nodeName) || infoDirectorCheck.equals(nodeName)) {//信审一次反馈页面（信审组长和主管）
            //查询出最新的一次质检反馈记录
            return "quality/qualityFeedBack/applyCheckFirstFeedBack";
        } else if (infoTeamLeaderRecheck.equals(nodeName) || infoDirectorRecheck.equals(nodeName)) {//信审二次反馈页面（信审组长和主管）
            return "quality/qualityFeedBack/applyCheckSecondFeedBack";
        } else if (infoManagerArb.equals(nodeName)) {//信审经理申请仲裁
            return "quality/qualityFeedBack/applyCheckManagerFeedBack";
        } else if (checkManagerArb.equals(nodeName)) {//质检经理申请仲裁定版
            return "quality/qualityFeedBack/qualityManagerArbitrationFeedBack";
        } else {//最终定版页面，反馈流程结束
            return "quality/qualityFeedBack/finalFeedBackTable";
        }
    }

    /**
     * @Desc: 跳转到查询质检反馈详情页面
     * @Author: phb
     * @Date: 2017/5/6 11:43
     */
    @RequestMapping("/feedBackDetailsView")
    public String feedBackDetailsView(Model model, Long checkResId, Long qualityCheckId, String loanNo) {
        model.addAttribute("qualityCheckId", qualityCheckId);
        model.addAttribute("checkResId", checkResId);
        model.addAttribute("loanNo", loanNo);
        return "quality/qualityFeedBack/feedBackDetails";
    }

    /**
     * 待处理列表
     */
    @RequestMapping("/toDoPageList")
    @ResponseBody
    public ResponsePage<QualityFeedBackVo> getToDoPage(RequestPage requestPage, QualityFeedBackVo qualityFeedBack) {
        qualityFeedBack.setLoginUser(ShiroUtils.getAccount());
        return qualityFeedBackService.getPageListToDo(requestPage, qualityFeedBack);
    }

    /**
     * 已完成列表
     */
    @RequestMapping("/donePageList")
    @ResponseBody
    public ResponsePage<QualityFeedBackVo> getDonePage(RequestPage requestPage, QualityFeedBackVo qualityFeedBack) {
        qualityFeedBack.setLoginUser(ShiroUtils.getAccount());
        return qualityFeedBackService.getPageListDone(requestPage, qualityFeedBack);
    }

    /**
     * 质检历史结论
     * param:ams_quality_check_info表的ID
     */
    @RequestMapping("/qualityHistory")
    @ResponseBody
    public List<QualityCheckResVo> qualityHistory(Long qualityCheckId) {
        return qualityFeedBackService.getQualityHistoryById(qualityCheckId);
    }

    /**
     * @Desc: 根据checkResId查询质检反馈记录
     * @Author: phb
     * @Date: 2017/4/26 14:42
     */
    @RequestMapping("/getFeedBackHistoryByCheckResId")
    @ResponseBody
    public List<QualityFeedBack> getFeedBackHistoryByCheckResId(Long checkResId) {
        if (checkResId == null) {
            return null;
        } else {
            return qualityFeedBackService.getFeedBackHistoryByCheckResId(checkResId);
        }
    }

    /**
     * @Desc: 保存当前操作人的一条质检反馈记录
     * @Author: phb
     * @Date: 2017/5/18 17:26
     */
    @RequestMapping(value = "/saveFeedBackRecord", method = RequestMethod.POST)
    @ResponseBody
    public Result<String> saveFeedBackRecord(QualityFeedBack qualityFeedBack) {
        //查询出最新一条的质检反馈结果
        QualityFeedBack lastQualityFeedBack = qualityFeedBackService.getLatestFeedBackByChekResId(qualityFeedBack.getCheckResId());
        //如果质检反馈结果这三个字段为空则需要复制最近一条反馈记录的值
        if (qualityFeedBack.getCheckType() == null) {
            qualityFeedBack.setCheckType(lastQualityFeedBack.getCheckType());
        }
        if(qualityFeedBack.getCheckError()==null){
            qualityFeedBack.setCheckError(lastQualityFeedBack.getCheckError());
        }
        if(qualityFeedBack.getErrorCode()==null){
            qualityFeedBack.setErrorCode(lastQualityFeedBack.getErrorCode());
        }
        //设置当前操作人
        qualityFeedBack.setNodeName(nodeName);
        qualityFeedBack.setCreatedBy(ShiroUtils.getAccount());
        qualityFeedBack.setLastModifiedBy(ShiroUtils.getAccount());

        /***************************保存反馈操作日志**************************/
        QualityLog qualityLog = new QualityLog(loanNo, qualityFeedBack.getType(), "质检反馈", nodeName);
        qualityLog.setCreatedBy(ShiroUtils.getAccount());
        qualityLog.setLastModifiedBy(ShiroUtils.getAccount());
        qualityLogService.save(qualityLog);

        return qualityFeedBackService.saveFeedBackRecord(qualityFeedBack);
    }

    /**
     * @Desc: 任务处理人完成对应的质检反馈任务
     * @Author: phb
     * @Date: 2017/4/26 14:40
     */
    @RequestMapping(value = "/finishFeedbackTask", method = RequestMethod.POST)
    @ResponseBody
    public Result<String> finishFeedbackTask(String chekResIds) {
        LOGGER.info("======任务处理人:{}完成对应的质检反馈任务======", ShiroUtils.getAccount());
        Result<String> result = new Result<String>(Result.Type.SUCCESS);
        try {
            qualityFeedBackService.finishFeedbackTask(chekResIds);
            result.addMessage("质检反馈流程启动成功！");
        } catch (Exception e) {
            result.setType(Result.Type.FAILURE);
            result.addMessage("质检反馈流程启动失败！");
            LOGGER.info("======开启反馈流程失败！{}======", e);
        }
        return result;
    }

    /**
     * @Desc: 查询质检反馈的附件
     * @Author: phb
     * @Date: 2017/4/26 20:41
     */
    @RequestMapping(value = "/findFeedBackAttachmentList", method = RequestMethod.POST)
    @ResponseBody
    public List<AttachmentVo> findFeedBackAttachmentList(String loanNo) {
        return qualityFeedBackService.findFeedBackAttachmentList(loanNo, picImgUrl);
    }

    /**
     * @Desc: 根据ID删除反馈附件
     * @Author: phb
     * @Date: 2017/5/6 9:17
     */
    @RequestMapping(value = "/deleteAttachmentById", method = RequestMethod.POST)
    @ResponseBody
    public Result<String> deleteAttachmentById(Long id, String jobNumber, String operator,String loanNo) {
        return qualityFeedBackService.deleteAttachmentById(id, jobNumber, operator, picImgUrl,loanNo);
    }

    /**
     * @Desc: 导出待处理列表
     * @Author: phb
     * @Date: 2017/5/6 13:59
     */
    @RequestMapping("/exportToDoExcel/{queryParams}")
    public void exportToDoExcel(@PathVariable String queryParams, HttpServletRequest req, HttpServletResponse res) {
        try {
            QualityFeedBackVo qualityFeedBackVo = JSON.parseObject(queryParams, QualityFeedBackVo.class);
            qualityFeedBackService.exportToDoExcel(qualityFeedBackVo, req, res);
        } catch (Exception e) {
            LOGGER.error("质检反馈情况导出列表信息 异常:", e);
        }
    }

    /**
     * @Desc: 导出已完成列表
     * @Author: phb
     * @Date: 2017/5/6 13:59
     */
    @RequestMapping("/exportDoneExcel/{queryParams}")
    public void exportDoneExcel(@PathVariable String queryParams, HttpServletRequest req, HttpServletResponse res) {
        try {
            QualityFeedBackVo qualityFeedBackVo = JSON.parseObject(queryParams, QualityFeedBackVo.class);
            qualityFeedBackService.exportDoneList(qualityFeedBackVo, req, res);
        } catch (Exception e) {
            LOGGER.error("质检反馈情况导出列表信息 异常:", e);
        }
    }

    /**
     * @Desc: 查询所有可用的差错代码
     * @Author: phb
     * @Date: 2017/5/14 14:03
     */
    @RequestMapping("/findAllUsableErrorCodes")
    @ResponseBody
    public List<QualityErrorCode> findAllUsableErrorCodes() {
        return qualityErrorCodeService.findAllUsableErrorCodes();
    }
}


