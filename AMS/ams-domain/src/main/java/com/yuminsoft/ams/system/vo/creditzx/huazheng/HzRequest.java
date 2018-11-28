package com.yuminsoft.ams.system.vo.creditzx.huazheng;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class HzRequest implements Serializable{

    private static final long serialVersionUID = -6018911261121074868L;

    private Long id;                // 征信ID
    private String appNo;           // 借款编号
    private String name;            // 姓名
    private String idCard;          // 证件号码
    private String mobile;          // 手机号
    private Integer mobileService;  // 运营商(1-移动，2-联通，3-电信)
    private Boolean isCheck = false;// 是否去华证查询
    private Long timestamp;         // 必填(判断请求有效性超时使用)
    private String creatorId;       // 创建人

}
