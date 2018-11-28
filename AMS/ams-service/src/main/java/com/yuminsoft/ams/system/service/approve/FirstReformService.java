package com.yuminsoft.ams.system.service.approve;


import com.ymkj.ams.api.vo.request.audit.ReqBMSReassignmentBatchVo;
import com.ymkj.ams.api.vo.response.audit.ResBMSReassignmentVo;
import com.ymkj.ams.api.vo.response.audit.ResReassignmentUpdVO;
import com.ymkj.pms.biz.api.vo.response.ResOrganizationVO;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.vo.apply.ReformVO;

import java.util.List;

public interface FirstReformService {

    /**
     * 初审改派
     *
     * @param request
     * @return
     * @author dmz
     * @date 2017年4月8日
     */
    public Result<ResReassignmentUpdVO> updateFirstReformService(ReqBMSReassignmentBatchVo request, ReformVO reformVO);

    /**
     * 如果有退回件标识，则对获取到的结果进行筛选
     *
     * @param rows
     * @return
     * @author zhouwen
     * @date 2017年7月6日
     */
    public List<ResBMSReassignmentVo> getReturnLoanNoList(List<ResBMSReassignmentVo> rows);

    /**
     * 获取大组信息，没有接单能力用户的大组不显示
     *
     * @param taskDef
     * @param reformVOList
     * @return
     */
    public List<ResOrganizationVO> getBigGroupByAccountAndAbility(String taskDef, List<ReformVO> reformVOList);

    /**
     * 据登录用户和大组id获取小组
     *
     * @param orgId        大组ID
     * @param taskDef
     * @param reformVOList
     * @return
     */
    public List<ResOrganizationVO> getTeamByAccountAndOrgIdAndAbility(Long orgId, String taskDef, List<ReformVO> reformVOList);
}
