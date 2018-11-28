package com.yuminsoft.ams.system.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 抽象VO父类
 * Created by wulinjie on 2017/6/16.
 */
@Data
public abstract class AbstractEntityVo implements Serializable {

    private static final long serialVersionUID = 4833684695363912915L;
    protected Long id;                    // 主键id
    protected String createdBy;           // 创建人
    protected Date createdDate;           // 创建时间
    protected String lastModifiedBy;      // 修改人
    protected Date lastModifiedDate;      // 修改时间
    protected Integer version;		    // 版本号
}
