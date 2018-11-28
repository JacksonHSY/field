package com.yuminsoft.ams.system.vo.bms;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class ResCreditReportVo implements Serializable{
    private static final long serialVersionUID = 5237850964423338707L;

    private String loanNo;  // 借款编号

    private String name;    // 姓名

    private String idCard;  // 身份证

    private String mobile;  // 手机号

    private Long mobileOnlineId;        // 在网时长ID

    private Long realNameOnlineId;      // 实名认证ID

    private String mobileOnline;        // 在网时长

    private String realNameOnline;      //  实名认证

}
