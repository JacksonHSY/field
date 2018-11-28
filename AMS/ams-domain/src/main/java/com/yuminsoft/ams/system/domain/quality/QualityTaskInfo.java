package com.yuminsoft.ams.system.domain.quality;

import com.yuminsoft.ams.system.domain.AbstractEntity;

import lombok.Data;
import lombok.ToString;

/**
 * 质检员当前任务信息表
 * @author fuhongxing
 */
@ToString
@Data
public class QualityTaskInfo extends AbstractEntity {

    
	private static final long serialVersionUID = 6622700623115148058L;
	
	/**
     * 质检员
     */
	private String checkUser;
	/**
     * 是否接单
     */
    private String ifAccept;

    /**
     * 质检员姓名
     */
    private String checkUserName;
    /**
     * 最大队列值
     * @return
     */
    private Integer maxTaskNum;
    /**
     * 批量修改人员code
     * @return
     */
    private String users;
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