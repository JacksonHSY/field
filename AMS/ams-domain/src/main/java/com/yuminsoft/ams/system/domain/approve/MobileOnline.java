package com.yuminsoft.ams.system.domain.approve;

import com.yuminsoft.ams.system.domain.AbstractEntity;
import lombok.Data;
import lombok.ToString;

/**
 * 信审手机入网时长 + 实名认证
 */
@Data
@ToString
public class MobileOnline extends AbstractEntity {

    private static final long serialVersionUID = 6654848799314414802L;

    private String loanNo;              // 借款编号
    private String name;                // 姓名
    private String idNo;                // 身份证号
    private String mobile;              // 手机号
    private Integer searchTimes;        // 调用征信查询次数
    private Long mobileOnlineId;        // 征信入网时长ID
    private String mobileOnline;        // 征信入网时长查询结果
    private Long realCertiId;           // 征信实名认证ID
    private String realCerti;           // 征信实名认证查询结果
}
