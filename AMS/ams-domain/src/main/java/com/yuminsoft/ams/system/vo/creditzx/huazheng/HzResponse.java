package com.yuminsoft.ams.system.vo.creditzx.huazheng;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class HzResponse<T extends Serializable> implements Serializable {
    private static final long serialVersionUID = -9077384189597066737L;

    private String code;        // 响应码 000000-有记录,000001-未查询到记录,000003-请求时间与当前时间相差过长,999999-缺少参数或异常
    private String messages;    // 响应消息
    private String metaCode;    // 手机号三要素认证(华证返回)
    private Integer status;     // 0-查询成功无数据、1-查询成功有数据、10-查询失败
    private T data;             // 响应数据


    public boolean success() {
        return "000000".equals(this.code);
    }
}
