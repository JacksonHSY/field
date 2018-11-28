package com.yuminsoft.ams.system.vo.creditzx.huazheng;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 征信系统实名认证响应报文
 */
@Data
@ToString
public class IdCardCheckInfoVo implements Serializable{

    private static final long serialVersionUID = -2591987412413362076L;

    private Long id;                // 征信ID
    private String appNo;           // 借款编号
    private String name;            // 姓名
    private String idCard;          // 证件号码
    private String mobile;          // 手机号
    private String mobileService;   // 运营商(1-移动，2-联通，3-电信)
    private String creatorId;       // 创建人
    private Boolean isCheck = false;// 是否去华证查询
    private Long timestamp;         // 必填(判断请求有效性超时使用)
    private String succeed;         // 是否验证成功(T-正确,F-不正确,N-库中无记录)
    private String msg;             // 消息提示
}
