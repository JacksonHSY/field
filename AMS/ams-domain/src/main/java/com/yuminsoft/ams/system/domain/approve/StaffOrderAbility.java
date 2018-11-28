package com.yuminsoft.ams.system.domain.approve;

import com.yuminsoft.ams.system.domain.AbstractEntity;
import lombok.Data;
import lombok.ToString;

/**
 * 员工接单能力表
 * @author fuhongxing
 */
@Data
@ToString
public class StaffOrderAbility extends AbstractEntity {
	private static final long serialVersionUID = 2181770879560689618L;

	private String staffCode;//员工工号

    private String productCode;//产品编码

    private String areaCode;//地区编码

    private Integer value;//能力值
    
    private String type;    // 申请类型
    @Deprecated
    private String fraudLevel; //欺诈等级

    private String comCreditRating;//ZDSC综合信用评级
}