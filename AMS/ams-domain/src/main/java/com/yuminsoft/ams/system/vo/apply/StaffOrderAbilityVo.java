package com.yuminsoft.ams.system.vo.apply;

import com.google.common.collect.Lists;
import com.yuminsoft.ams.system.domain.approve.StaffOrderAbility;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
public class StaffOrderAbilityVo implements Serializable {
    private static final long serialVersionUID = 6157283899146938810L;
    private String productCode;
    private String productName;
    private String type;
    private List<StaffOrderAbility> staffAbilityList = Lists.newArrayList();
}
