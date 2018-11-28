package com.yuminsoft.ams.system.domain.quality;


import com.yuminsoft.ams.system.domain.AbstractEntity;
import lombok.Data;
import lombok.ToString;

/**
 * 质检抽检率设置
 * @author sunlonggang
 */
@Data
@ToString
public class QualitySetInfo extends AbstractEntity {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3586572718823213187L;
	/**
	 * 起始日期
	 */
	private String startDate;
	/**
	 * 结束日期
	 */
	private String endDate;
	/**
	 * 通过件抽检率
	 */
	private String passRate;
	/**
	 * 拒绝件抽检率
	 */
	private String rejectRate;
	
	/**
	 * Y-是，N-否
	 */
	private String isRegular;
	/**
	 * 下一个周期开始时间
	 * @return
	 */
	private String nextStartDate;
	/**
	 * 下一个周期结束时间
	 * @return
	 */
	private String nextEndDate;
	/**
	 * 下一个通过件抽检率
	 * @return
	 */
	private String nextPassRate;
	/**
	 * 下一个通过件抽检率
	 * @return
	 */
	private String nextRejectRate;
	/**
	 * 周期内历史通过件查出总数
	 * @return
	 */
	private int passHistoryQueryCount;
	/**
	 * 周期内历史通过件抽出总数
	 * @return
	 */
	private int passHistoryExtractCount;
	/**
	 * 
	 * 周期内历史拒绝件查出总数
	 * @return
	 */
	private int rejectHistoryQueryCount;
	/**
	 * 周期内历史拒绝件抽出总数
	 * @return
	 */
	private int rejectHistoryExtractCount;
	/**
	 * 客户类型
	 * @return
	 */
	private String applyType;
}