package com.yuminsoft.ams.system.vo.pic;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * PIC 请求参数
 * Created by YM10195 on 2017/7/19.
 */
@Data
@ToString
public class PicRequestVo implements Serializable{
    private static final long serialVersionUID = -8664352300815339471L;


    private String appNo;           // 申请件编号
    private String operator;        // 操作人姓名
    private String jobNumber;       // 操作人工号d
    private String nodeKey;         // PIC 环节
    private String subclass_sort;   // 文件类型
    private List<Long> ids;         // 附件ID集合
}
