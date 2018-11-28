package com.yuminsoft.ams.system.controller.system;

import com.yuminsoft.ams.system.common.QualityEnum;
import com.yuminsoft.ams.system.util.EnumerationUtils;
import com.yuminsoft.ams.system.vo.system.SelectOptionVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 公共api
 * Created by wulinjie on 2017/6/16.
 */
@RestController
@RequestMapping("common")
public class CommonController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonController.class);

    /**
     * 获取质检抽检件状态(0-通过件，1-拒绝件，2-其他件)
     * {@link com.yuminsoft.ams.system.common.QualityEnum.ApprovalStatus}
     * @author wulinjie
     * @return 质检抽检件状态
     */
    @RequestMapping("getQualityApprovalStatus")
    public List<SelectOptionVo> getQualityApprovalStatus(){

        return EnumerationUtils.getOptions(QualityEnum.ApprovalStatus.class);
    }

    /**
     * 获取质检抽差错类型(重大差错、一般差错、预警、建议)
     * {@link com.yuminsoft.ams.system.common.QualityEnum.CheckResult}
     * @author wulinjie
     * @return 质检抽差错类型
     */
    @RequestMapping("getQualityCheckResult")
    public List<SelectOptionVo> getQualityCheckResult(){

        return EnumerationUtils.getOptions(QualityEnum.CheckResult.class);
    }

}
