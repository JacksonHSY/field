package com.yuminsoft.ams.system.vo.quality;

import com.yuminsoft.ams.system.vo.AbstractEntityVo;
import lombok.Data;
import lombok.ToString;

/**
 * Created by wulinjie on 2017/6/21.
 */
@Data
@ToString
public class QualityTaskInfoVo extends AbstractEntityVo {
    private static final long serialVersionUID = -3902025322809579969L;
    /**
     * 质检员
     */
    private String checkUser;
    /**
     * 质检员姓名
     */
    private String checkUserName;
    /**
     * 是否接单
     */
    private String ifAccept;
    /**
     * 最大队列值
     * @return
     */
    private Integer maxTaskNum;
    /**
     * 质检人员列表
     * @return
     */
    private String checkedUsers;
    /**
     * 是否申请复核接单 Y 是 N 否
     * @return
     */
    private String ifApplyCheck;
    /**
     * 是否反馈接单 Y 是 N 否
     * @return
     */
    private String ifReback;
}
