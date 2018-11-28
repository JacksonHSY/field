package com.yuminsoft.ams.system.controller.quality;

import com.alibaba.fastjson.JSONObject;
import com.yuminsoft.ams.system.domain.quality.QualityExtractCase;
import com.yuminsoft.ams.system.service.quality.QualitySetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *  质检手动触发自动抽单入口
 * @author wangzx
 * @date 2018/7/9 10:24:46
 */
@Slf4j
@Controller
@RequestMapping("/qualityManualCheckController")
public class QualityManualCheckController {
    @Autowired
    QualitySetService qualitySetService;

    /**
     * 手动触发自动抽单
     * @param date
     */
    @RequestMapping("/qualityManualFindData")
    @ResponseBody
    public JSONObject qualityManualFindData(String date){
        QualityExtractCase qualityExtractCase = qualitySetService.systemSamplingRegular(date);
        JSONObject json = new JSONObject();
        json.put("通过件",qualityExtractCase.getPassCount());
        json.put("已抽通过件",qualityExtractCase.getNeedPassCount());
        json.put("拒绝件",qualityExtractCase.getRejectCount());
        json.put("已抽拒绝件",qualityExtractCase.getNeedRejectCount());
        return json;

    }
}
