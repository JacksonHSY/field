package com.yuminsoft.ams.system.service.approve;

import com.yuminsoft.ams.system.domain.approve.MobileOnline;

public interface MobileOnlineService {

    /**
     * 查询入网时长和实名认证
     * @param loanNo
     * @param name
     * @param idNo
     * @param mobile
     * @return
     */
    public MobileOnline getByLoanNoAndNameAndIdNoAndMobile(String loanNo, String name, String idNo, String mobile);

    /**
     * 新增入网时长和实名认证
     * @param mobileOnline
     * @return
     */
    public MobileOnline createMobileOnline(MobileOnline mobileOnline);

    /**
     * 更新或保存
     * @param mobileOnline
     * @return
     */
    public MobileOnline saveOrUpdate(MobileOnline mobileOnline);
}
