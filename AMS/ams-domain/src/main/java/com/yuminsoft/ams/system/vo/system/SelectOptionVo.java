package com.yuminsoft.ams.system.vo.system;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * combobox 选项
 * Created by wulinjie on 2017/6/16.
 */
@Data
@ToString
public class SelectOptionVo implements Serializable{

    private static final long serialVersionUID = -109276017794320054L;
    private String code;    // 键
    private String value;   // 值

}
