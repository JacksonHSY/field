package com.yuminsoft.ams.system.service.system;

import com.yuminsoft.ams.system.domain.system.UserLog;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;

import java.util.List;
import java.util.Map;

/**
 * 用户日志查询
 * @author luting
 */
public interface UserLogService {
    /**
     * 保存日志
     *
     * @param userLog
     * @return
     */
    int save(UserLog userLog);

    /**
     * 根据借款编号查询操作日志
     *
     * @param loanNo
     * @return
     */
    List<UserLog> getByLoanNo(String loanNo);

    /**
     * 分页查询
     *
     * @param currentPage
     * @param pageSize
     * @return
     */
    ResponsePage<UserLog> getAll(int currentPage, int pageSize, Map<String, Object> map);

    /**
     * 分页查询
     *
     * @param requestPage
     * @return
     */
    ResponsePage<UserLog> getPageList(RequestPage requestPage);

}
