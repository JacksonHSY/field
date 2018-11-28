package com.yuminsoft.ams.system.domain.approve;

import com.yuminsoft.ams.system.domain.AbstractEntity;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * 规则配置
 *
 * @author fusj
 */
@Data
@ToString
public class AgenLeader extends AbstractEntity {
    private static final long serialVersionUID = 1L;

    //	用户编码	userCode
    private String userCode;
    private String proxyUser;
    private String status;//-1为代理组长失效
    private List<AgenLeader> agenLeaderList;
}
