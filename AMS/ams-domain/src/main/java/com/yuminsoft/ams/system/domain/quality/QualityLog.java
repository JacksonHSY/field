package com.yuminsoft.ams.system.domain.quality;

import com.yuminsoft.ams.system.domain.AbstractEntity;
import lombok.Data;
import lombok.ToString;

/**
 * 质检日志记录表
 * @author fuhongxing
 */
@Data
@ToString
public class QualityLog extends AbstractEntity {

    private static final long serialVersionUID = 6236437427003498320L;
	
	/**
     * 借款编号
     */
    private String loanNo;
    /**
     * 备注
     */
    private String remark;
    /**
     * 环节
     */
    private String link;
    /**
     * 当前操作
     */
    private String operation;


	public QualityLog() {
		super();
	}
	
    public QualityLog(String loanNo, String remark,String link,String operation) {
        super();
        this.loanNo = loanNo;
        this.remark = remark;
        this.link=link;
        this.operation=operation;
    }

}