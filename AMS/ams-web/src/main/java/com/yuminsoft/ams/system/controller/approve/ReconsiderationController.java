package com.yuminsoft.ams.system.controller.approve;

import com.alibaba.fastjson.JSON;
import com.ymkj.ams.api.enums.EnumConstants;
import com.ymkj.ams.api.vo.request.apply.ReqInformationVO;
import com.ymkj.ams.api.vo.request.reconsider.ReqReconsderReassignment;
import com.ymkj.ams.api.vo.request.reconsider.ReqReconsiderApprove;
import com.ymkj.ams.api.vo.request.reconsider.ReqReconsiderApproved;
import com.ymkj.ams.api.vo.response.reconsider.ResReconsiderApprove;
import com.ymkj.ams.api.vo.response.reconsider.ResReconsiderApproved;
import com.ymkj.ams.api.vo.response.reconsider.ResReconsiderReassignment;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.common.EnumUtils;
import com.yuminsoft.ams.system.controller.BaseController;
import com.yuminsoft.ams.system.domain.approve.ReconsiderHistory;
import com.yuminsoft.ams.system.domain.approve.ReconsiderStaff;
import com.yuminsoft.ams.system.service.approve.ReconsiderHistoryService;
import com.yuminsoft.ams.system.service.approve.ReconsiderStaffService;
import com.yuminsoft.ams.system.service.approve.ReconsiderationService;
import com.yuminsoft.ams.system.service.bms.BmsLoanInfoService;
import com.yuminsoft.ams.system.util.DateUtils;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.util.Strings;
import com.yuminsoft.ams.system.vo.apply.ReconsiderationVO;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;
import org.apache.ibatis.builder.BuilderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 复议控制层
 *
 * @author zhouwen
 * @date 2017年7月17日
 */
@Controller
@RequestMapping("/reconsideration")
public class ReconsiderationController extends BaseController {
    @Autowired
    private ReconsiderationService reconsiderationService;
    @Autowired
    private ReconsiderStaffService reconsiderStaffService;
    @Autowired
    private ReconsiderHistoryService reconsiderHistoryService;
    @Autowired
    private BmsLoanInfoService bmsLoanInfoService;
    /**
     * pic文件管理地址
     */
    @Value("${ams.pic.image.url}")
    private String picImageUrl;

    /**
     * pic复议权限
     */
    @Value("${ams.pic.image.picReconsideration}")
    private String picApproval;

    /**
     * 返回复议待办与已完成页面
     *
     * @return
     * @author zhouwen
     * @date 2017年7月17日
     */
    @RequestMapping("/reconsideration")
    public String customerService(Model moderl) {
        ReconsiderStaff reconsiderStaff = reconsiderStaffService.getReconsiderStaffByStaffCoder(ShiroUtils.getAccount());
        if (null != reconsiderStaff) {
            moderl.addAttribute("ifAccetp", reconsiderStaff.getIfAccept());
        }
        return "/apply/reconsideration";
    }


    /**
     * 查询复议待办任务
     *
     * @param req
     * @return
     */
    @RequestMapping("/getReconsiderationUnfinishedList")
    @ResponseBody
    public ResponsePage<ResReconsiderApprove> getReconsiderUnfinished(RequestPage requestPage, ReqReconsiderApprove req) {
        ResponsePage<ResReconsiderApprove> page = new ResponsePage<ResReconsiderApprove>();
        req.setPageNum(requestPage.getPage());
        req.setPageSize(requestPage.getRows());
        page = reconsiderationService.getReconsiderUnfinished(req);
        if (null != page && null != page.getRows()) {// 身份证号码隐藏
            for (ResReconsiderApprove resReconsiderApprove : page.getRows()) {
                resReconsiderApprove.setIdNo(Strings.hideIdCard(resReconsiderApprove.getIdNo()));
            }
        }
        return page;
    }

    /**
     * 查询复议已完成任务
     *
     * @return
     */
    @RequestMapping("/getReconsiderationFinishedList")
    @ResponseBody
    public ResponsePage<ResReconsiderApproved> getReconsiderFinished(RequestPage requestPage) {
        ResponsePage<ResReconsiderApproved> page = new ResponsePage<ResReconsiderApproved>();
        ReqReconsiderApproved req = new ReqReconsiderApproved();
        req.setPageNum(requestPage.getPage());
        req.setPageSize(requestPage.getRows());
        page = reconsiderationService.getReconsiderFinished(req);
        if (null != page && null != page.getRows()) {// 身份证号码隐藏
            for (ResReconsiderApproved resReconsiderApproved : page.getRows()) {
                resReconsiderApproved.setIdNo(Strings.hideIdCard(resReconsiderApproved.getIdNo()));
            }
        }
        return page;
    }

    /**
     * 返回复议办理页面
     *
     * @return
     * @author
     * @date
     */
    @RequestMapping("/handle/{loanNo}/{reconsiderNode}/{version}")
    public String handle(@PathVariable String loanNo, @PathVariable String reconsiderNode, Model model, @PathVariable Integer version, HttpSession session) {
        ReqInformationVO applyBasiceInfo = bmsLoanInfoService.getBMSLoanBasiceInfoByLoanNoService(session.getId(), loanNo, true);
        // 判断复议所处环节
        if (EnumUtils.isValidEnumName(EnumConstants.ReconsiderLink.class, reconsiderNode)) {
            if (EnumConstants.ReconsiderLink.F2.getCode().equals(reconsiderNode)) {
                ReconsiderHistory reconsiderHistory = reconsiderHistoryService.getReconsiderHistoryByLevelThreeHandle(loanNo);
                if (null != reconsiderHistory) {
                    model.addAttribute("reconsiderNodeStateThree", reconsiderHistory.getReconsiderNodeState());
                }
            }
        } else {
            LOGGER.error("复议办理所处环节错误 level:{}", reconsiderNode);
            throw new BuilderException("系统忙请稍后");
        }
        // 获取复议最后一个节点时间pic操作用
        ReconsiderHistory reconsiderHistory = reconsiderHistoryService.getLastNodeHistoryByLoanNo(loanNo);
        model.addAttribute("nodeCreateDate", DateUtils.dateToString(reconsiderHistory.getCreatedDate(),DateUtils.DEFAULT_DATE_YYYYMMDDHHMMSS));
        model.addAttribute("reconsiderNode", reconsiderNode);
        model.addAttribute("applyBasiceInfo", applyBasiceInfo);
        model.addAttribute("picImageUrl", picImageUrl);
        model.addAttribute("sysCode", sysCode);
        model.addAttribute("picApproval", picApproval);
        model.addAttribute("jobNumber", ShiroUtils.getCurrentUser().getUsercode());// 工号
        model.addAttribute("operator", ShiroUtils.getCurrentUser().getName());// 操作人姓名
        model.addAttribute("sysCreditZX", sysCreditZX);
        model.addAttribute("version", version);
        return "/apply/reconsiderationHandle";
    }

    /**
     * 复议办理
     *
     * @param reconsiderationVO
     * @return
     */
    @RequestMapping("/reconsiderHandle")
    @ResponseBody
    public Result<String> reconsiderHandle(ReconsiderationVO reconsiderationVO) {
        Result<String> result = new Result<String>(Result.Type.FAILURE);
        try {
            result = reconsiderationService.reconsiderHandle(reconsiderationVO);
        } catch (Exception e) {
            LOGGER.error("复议办理异常,params:{}", JSON.toJSONString(reconsiderationVO));
            result.addMessage("系统忙，请稍后");
        }
        return result;
    }

    /**
     * 复议改派页面
     *
     * @return
     */
    @RequestMapping("/reconsiderReform")
    public String reconsiderReform() {
        return "/apply/reconsiderReform";
    }

    /**
     * 复议改派列表查询
     *
     * @param requestPage
     * @param req
     * @return
     */
    @RequestMapping("/getReconsiderReformList")
    @ResponseBody
    public ResponsePage<ResReconsiderReassignment> getReconsiderReformList(RequestPage requestPage, ReqReconsderReassignment req) {
        ResponsePage<ResReconsiderReassignment> page = new ResponsePage<ResReconsiderReassignment>();
        req.setPageNum(requestPage.getPage());
        req.setPageSize(requestPage.getRows());
        page = reconsiderationService.getReconsiderReformList(req);
        if (null != page && null != page.getRows()) {// 身份证号码隐藏
            for (ResReconsiderReassignment resReconsiderReassignment : page.getRows()) {
                resReconsiderReassignment.setIdNo(Strings.hideIdCard(resReconsiderReassignment.getIdNo()));
            }
        }
        return page;
    }

    /**
     * 复议改派提交
     *
     * @return
     */
    @RequestMapping("/getReconsiderReformSubmit")
    @ResponseBody
    public Result<String> getReconsiderReformSubmit(@RequestBody List<ResReconsiderReassignment> list) {
        Result<String> result = new Result<String>(Result.Type.FAILURE);
        int count = 0;
        if (checkUserRule(list)) {
            for (ResReconsiderReassignment obj : list) {
                try {
                    reconsiderationService.getReconsiderReformSubmit(obj);
                    count++;
                } catch (Exception e) {
                    LOGGER.error("复议改派异常:", e);
                }
            }
            result.addMessage("复议改派成功:" + count + "条,失败:" + (list.size() - count) + "条");
            result.setType(Result.Type.SUCCESS);
        } else {
            result.addMessage("权限不足请重新改派");
        }
        return result;
    }

    /**
     * 提交时判断复议人员是否有权限
     * @param list
     * @return
     */
    private  boolean checkUserRule(List<ResReconsiderReassignment> list) {
        boolean action = false;
        Map<String,Object> map = new HashMap<String,Object>();
        StringBuffer sb = new StringBuffer();
        for(ResReconsiderReassignment obj : list) {
            // 判断当前层级
            if (Strings.isNotEmpty(obj.getReviewStatus())) {
                // 判断当前状态            //待办  退回
                if (EnumConstants.ReconsiderOperation.O0.getCode().equals(obj.getXsStatus()) || EnumConstants.ReconsiderOperation.O2.getCode().equals(obj.getXsStatus())) {
                    map.put(obj.getReviewStatus(),obj.getReviewStatus());
                    if (EnumConstants.ReconsiderOperation.O0.getCode().equals(obj.getXsStatus())) {
                        sb.append(obj.getHandlerCode()+",");
                    }
                    // 提交 (F1提交F3提交都找F2)
                }else if (EnumConstants.ReconsiderOperation.O1.getCode().equals(obj.getXsStatus())) {
                    map.put(EnumConstants.ReconsiderLink.F2.getCode(),EnumConstants.ReconsiderLink.F2.getCode());
                }
            } else {
                map.put(EnumConstants.ReconsiderLink.F1.getCode(),EnumConstants.ReconsiderLink.F1.getCode());
            }
        }
        if (sb.length()>0){
            map.put("filterStaff",sb.toString().split(","));
        }
        map.put("staffCode",list.get(0).getRejectPersonCode());
        List<ReconsiderStaff> reconsiderStaffList = reconsiderStaffService.getReconsiderReformHandler(map);
        if (!CollectionUtils.isEmpty(reconsiderStaffList)){
            action = true;
        }
        return action;
    }
}
