package com.yuminsoft.ams.system.service.approve.impl;

import com.yuminsoft.ams.system.common.EnumUtils;
import com.yuminsoft.ams.system.dao.approve.ReconsiderStaffMapper;
import com.yuminsoft.ams.system.domain.approve.ReconsiderStaff;
import com.yuminsoft.ams.system.exception.BusinessException;
import com.yuminsoft.ams.system.service.approve.ReconsiderStaffService;
import com.yuminsoft.ams.system.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by YM10106 on 2018/6/13.
 */
@Service
public class ReconsiderStaffServiceImpl implements ReconsiderStaffService {

    @Autowired
    private ReconsiderStaffMapper reconsiderStaffMapper;

    /**
     * 根据工号禁用或开启用户接单(Y/N)
     *
     * @param staffCode
     * @param ifAccept
     * @return
     */
    @Override
    public Result<String> updateResconsiderStaffAccept(String staffCode, String ifAccept) {
        Result<String> result = new Result<String>(Result.Type.FAILURE);
        if (EnumUtils.YOrNEnum.Y.getValue().equals(ifAccept) || EnumUtils.YOrNEnum.N.getValue().equals(ifAccept)) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("staffCode", staffCode);
            map.put("status", EnumUtils.DisplayEnum.ENABLE.getValue());
            ReconsiderStaff reconsiderStaff = reconsiderStaffMapper.findOne(map);
            if (null != reconsiderStaff) {
                if (!reconsiderStaff.getIfAccept().equals(ifAccept)) {
                    reconsiderStaff.setIfAccept(ifAccept);
                    boolean update = reconsiderStaffMapper.update(reconsiderStaff);
                    if (update) {
                        result.setType(Result.Type.SUCCESS);
                        result.addMessage("操作成功");
                    } else {
                        throw new BusinessException("修改复议员是否开启接单失败");
                    }
                } else {
                    result.addMessage("参数错误,操作失败");
                }
            } else {
                result.addMessage("未找到对应复议人员,操作失败");
            }
        } else {
            result.addMessage("参数错误,操作失败");
        }
        return result;
    }

    /**
     * 根据员工号返回复议员工信息
     *
     * @param staffCode
     * @return
     */
    @Override
    public ReconsiderStaff getReconsiderStaffByStaffCoder(String staffCode) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("staffCode", staffCode);
        return reconsiderStaffMapper.findOne(map);
    }

    /**
     * 根据级别查询所有有效的复议人员
     * @param ruleLevel
     * @return
     */
    @Override
    public List<ReconsiderStaff> getReconsiderStaffByRuleLevel(String ruleLevel,String ifAccept) {
        Map<String,Object> map =new HashMap<String,Object>();
        map.put("ifAccept",ifAccept);
        map.put("ruleLevel",ruleLevel);
        return reconsiderStaffMapper.findReconsiderStaffByRuleLevel(map);
    }

    /**
     * 查询所有有效复议人员
     * @return
     */
    @Override
    public List<ReconsiderStaff> getAllReconsiderOperator() {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("statue",EnumUtils.DisplayEnum.ENABLE.getValue());
        return reconsiderStaffMapper.findAll(map);
    }

    /**
     * 复议改派查询可以改派的人
     * @param map
     * @return
     */
    @Override
    public List<ReconsiderStaff> getReconsiderReformHandler(Map<String,Object> map){
        return reconsiderStaffMapper.findReconsiderReformHandler(map);
    }
}
