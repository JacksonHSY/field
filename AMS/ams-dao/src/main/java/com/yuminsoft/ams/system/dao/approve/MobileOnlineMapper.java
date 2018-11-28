package com.yuminsoft.ams.system.dao.approve;

import com.yuminsoft.ams.system.domain.approve.MobileOnline;
import org.apache.ibatis.annotations.Param;

public interface MobileOnlineMapper {

    /**
     *
     * @param loanNo
     * @param name
     * @param idNo
     * @param mobile
     * @return
     */
    public MobileOnline findByLoanNoAndNameAndIdNoAndMobile(@Param("loanNo") String loanNo,@Param("name") String name,@Param("idNo") String idNo,@Param("mobile") String mobile);

    /**
     * 新增入网时长和实名认证查询结果
     * @param mobileOnline
     * @author wulj
     * @return
     */
    public int insert(MobileOnline mobileOnline);

    /**
     * 更新入网时长和实名认证查询结果
     * @param mobileOnline
     * @return
     */
    public int update(MobileOnline mobileOnline);

}
