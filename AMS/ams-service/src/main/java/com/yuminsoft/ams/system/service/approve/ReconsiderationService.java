package com.yuminsoft.ams.system.service.approve;

import com.ymkj.ams.api.vo.request.reconsider.ReqReconsderReassignment;
import com.ymkj.ams.api.vo.request.reconsider.ReqReconsiderApprove;
import com.ymkj.ams.api.vo.request.reconsider.ReqReconsiderApproved;
import com.ymkj.ams.api.vo.response.reconsider.ResReconsiderApprove;
import com.ymkj.ams.api.vo.response.reconsider.ResReconsiderApproved;
import com.ymkj.ams.api.vo.response.reconsider.ResReconsiderReassignment;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.vo.apply.ReconsiderationVO;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;

public interface ReconsiderationService {

    /**
     * 查询复议待办任务
     *
     * @param req
     * @return
     */
    ResponsePage<ResReconsiderApprove> getReconsiderUnfinished(ReqReconsiderApprove req);

    /**
     * 查询复议已完成任务
     *
     * @return
     */
    ResponsePage<ResReconsiderApproved> getReconsiderFinished(ReqReconsiderApproved req);

    /**
     * 复议办理
     *
     * @param reconsiderationVO
     * @return
     */
    Result<String> reconsiderHandle(ReconsiderationVO reconsiderationVO);

    /**
     * 复议改派查询
     * @param req
     * @return
     */
    ResponsePage<ResReconsiderReassignment> getReconsiderReformList(ReqReconsderReassignment req);

    /**
     * 复议改派
     * @param resReconsiderReassignment
     */
    void getReconsiderReformSubmit(ResReconsiderReassignment resReconsiderReassignment);
}
