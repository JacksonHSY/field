package com.yuminsoft.ams.system.service.bms.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ymkj.base.core.biz.api.message.Response;
import com.ymkj.bms.biz.api.service.job.IBMSLoanJobExecuter;
import com.ymkj.bms.biz.api.vo.request.job.ReqZhongAnHistoryVO;
import com.yuminsoft.ams.system.exception.BusinessException;
import com.yuminsoft.ams.system.service.BaseService;
import com.yuminsoft.ams.system.service.bms.ZhongAnCheatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by YM10106 on 2018/3/21.
 */
@Service
public class ZhongAnCheatServiceImpl extends BaseService implements ZhongAnCheatService {

    @Autowired
    private IBMSLoanJobExecuter ibmsSLoanJobExecuter;

    /**
     * 众安反欺诈评分
     *
     * @param reqZhongAnHistoryVO
     */
    @Override
    public void zhongAnHistory(ReqZhongAnHistoryVO reqZhongAnHistoryVO) {
        try {
            Response<Object> response = ibmsSLoanJobExecuter.zhongAnHistory(reqZhongAnHistoryVO);
            LOGGER.info("众安反欺诈评分 params:{} result:{}", JSONObject.toJSONString(reqZhongAnHistoryVO), JSON.toJSONString(response));
            // 判断调用失败
            if (null == response || !response.isSuccess()) {
                throw new BusinessException("众安反欺诈评分调用失败");
            }
        } catch (Exception e) {
            LOGGER.error("众安反欺诈评分调用异常:", e);
            throw new BusinessException("众安反欺诈评分调用失败");
        }
    }
}
