package com.yuminsoft.ams.system.service.approve;

import com.yuminsoft.ams.system.domain.approve.ReconsiderStaff;
import com.yuminsoft.ams.system.util.Result;

import java.util.List;
import java.util.Map;

/**
 * Created by YM10106 on 2018/6/13.
 */
public interface ReconsiderStaffService {
    /**
     * 根据工号禁用或开启用户接单(Y/N)
     *
     * @param staffCode
     * @param ifAccept
     * @return
     */
    Result<String> updateResconsiderStaffAccept(String staffCode,String ifAccept);

    /**
     * 根据员工号返回复议员工信息
     * @param staffCode
     * @return
     */
    ReconsiderStaff getReconsiderStaffByStaffCoder(String staffCode);

    /**
     * 根据级别查询所有有效的复议人员
     * @param ruleLevel
     * @return
     */
    List<ReconsiderStaff> getReconsiderStaffByRuleLevel(String ruleLevel,String ifAccept);

    /**
     * 查询所有有效复议人员
     * @return
     */
    List<ReconsiderStaff> getAllReconsiderOperator();

    /**
     * 复议改派查询可以改派的人
     * @param map
     * @return
     */
    List<ReconsiderStaff>  getReconsiderReformHandler(Map<String,Object> map);
}
