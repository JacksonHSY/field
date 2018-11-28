package com.yuminsoft.ams.system.service.bms;

import com.ymkj.bms.biz.api.vo.request.job.ReqZhongAnHistoryVO;

/**
 * Created by YM10106 on 2018/3/21.
 */
public interface ZhongAnCheatService {

    /**
     * 众安反欺诈评分
     *
     * @param reqZhongAnHistoryVO
     */
    void zhongAnHistory(ReqZhongAnHistoryVO reqZhongAnHistoryVO);

}
