package com.yuminsoft.ams.system.service.approve;

import com.ymkj.ams.api.vo.response.reconsider.ResReconsiderDispatch;

import java.util.List;

/**
 * Created by YM10106 on 2018/6/20.
 */
public interface ReconsiderDispatchService {
    /**
     * 复议派单查询
     *
     * @return
     */
    List<ResReconsiderDispatch> getReconsiderDispatchList();


    /**
     * 自动分派
     * @param resReconsiderDispatch
     */
    void automaticDispatch(ResReconsiderDispatch resReconsiderDispatch);
}
