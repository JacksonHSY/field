package com.yuminsoft.ams.system.domain.approve;

import com.yuminsoft.ams.system.domain.AbstractEntity;
import lombok.Data;
import lombok.ToString;

/**
 * 资料核对信息表
 * @author wulj
 */
@Data
@ToString
public class ApproveCheckInfo extends AbstractEntity {

    private static final long serialVersionUID = 474504881860365480L;

    private String loanNo;                  // 申请件编号
    private String name;                    // 名字
    private Integer oneMonthsCount;         // 近一个月查询次数
    private Integer threeMonthsCount;       // 近三个月查询次数
    private Integer weekendPay;             // 是否周末发薪(0-是,1-否)
    private Integer creditCheckException;   // 征信验证是否异常(0-是,1-否)
    private Integer courtCheckException;    // 法网核查是否异常(0-是,1-否)
    private Integer internalCheckException; // 内部匹配是否异常(0-是,1-否)
    private String memo;                    // 备注
}
