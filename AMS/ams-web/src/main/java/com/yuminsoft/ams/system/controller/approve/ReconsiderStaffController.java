package com.yuminsoft.ams.system.controller.approve;

import com.ymkj.ams.api.enums.EnumConstants;
import com.ymkj.ams.api.vo.response.reconsider.ResReconsiderReassignment;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.common.EnumUtils;
import com.yuminsoft.ams.system.controller.BaseController;
import com.yuminsoft.ams.system.domain.approve.ReconsiderStaff;
import com.yuminsoft.ams.system.service.approve.ReconsiderStaffService;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.Collator;
import java.util.*;

/**
 * Created by YM10106 on 2018/6/13.
 */
@Controller
@RequestMapping("/reconsiderStaff")
public class ReconsiderStaffController extends BaseController {

    @Autowired
    private ReconsiderStaffService reconsiderStaffService;

    /**
     * 复议开启或关闭接单
     *
     * @param ifAccept
     * @return
     */
    @RequestMapping("/updateReconsiderStaffAccept")
    @ResponseBody
    public Result<String> updateReconsiderStaffAccept(String ifAccept) {
        Result<String> result = new Result<>(Result.Type.FAILURE);
        try {
            result = reconsiderStaffService.updateResconsiderStaffAccept(ShiroUtils.getAccount(), ifAccept);
        } catch (Exception e) {
            result.addMessage("系统忙,请稍后");
            LOGGER.error("更新复议用户开启关闭接单异常", e);
        }
        return result;
    }

    /**
     * 根据级别查询出有效的复议员根据工号排序
     *
     * @return
     */
    @RequestMapping("/getReconsiderStaffByRuleLevel/{ruleLevel}")
    @ResponseBody
    public List<ReconsiderStaff> getReconsiderStaffByRuleLevel(@PathVariable String ruleLevel) {
        return reconsiderStaffService.getReconsiderStaffByRuleLevel(ruleLevel, EnumUtils.YOrNEnum.Y.getValue());
    }

    /**
     * 复议改派-查询所有有效的操作人按照拼音排序
     * @return
     */
    @RequestMapping("/getAllOrderByName")
    @ResponseBody
    public List<ReconsiderStaff> getAllOrderByName() {
        List<ReconsiderStaff> list = new ArrayList<ReconsiderStaff>();
        List<ReconsiderStaff> reconsiderStaffList = reconsiderStaffService.getAllReconsiderOperator();
        if (!CollectionUtils.isEmpty(reconsiderStaffList)) {
            Collections.sort(reconsiderStaffList, new Comparator<ReconsiderStaff>() {
                @Override
                public int compare(ReconsiderStaff o1, ReconsiderStaff o2) {
                    Collator com = Collator.getInstance(java.util.Locale.CHINA);
                    return com.getCollationKey(o1.getStaffName()).compareTo(com.getCollationKey(o2.getStaffName()));
                }
            });
            list = reconsiderStaffList;
        }
        return list;
    }

    /**
     * 复议改派-查找可以接单的复议人员
     * @param list
     * @return
     */
    @RequestMapping("/getReconsiderReformStaff")
    @ResponseBody
    public List<ReconsiderStaff> getReconsiderReformStaff(@RequestBody List<ResReconsiderReassignment> list) {
        List<ReconsiderStaff> reconsiderStaffList = new ArrayList<ReconsiderStaff>();
        if (!CollectionUtils.isEmpty(list)) {
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
            reconsiderStaffList = reconsiderStaffService.getReconsiderReformHandler(map);
        }
        return reconsiderStaffList;
    }
}
