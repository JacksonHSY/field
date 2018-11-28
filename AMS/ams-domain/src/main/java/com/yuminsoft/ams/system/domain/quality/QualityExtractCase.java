package com.yuminsoft.ams.system.domain.quality;

import java.util.Date;

import com.yuminsoft.ams.system.domain.AbstractEntity;

/**抽检记录
 * @author YM10174
 *
 */
public class QualityExtractCase extends AbstractEntity{
	private static final long serialVersionUID = 1L;
	//周期起始日期
	private Date startDate;	
	//周期结束日期
	private Date endDate;
	//查询通过件总数
	private int passCount;
	//查询拒绝件总数
	private int rejectCount;
	//需要抽取的通过件总数
	private int needPassCount;
	//需要抽取的拒绝件总数
	private int needRejectCount;
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public int getPassCount() {
		return passCount;
	}
	public void setPassCount(int passCount) {
		this.passCount = passCount;
	}
	public int getRejectCount() {
		return rejectCount;
	}
	public void setRejectCount(int rejectCount) {
		this.rejectCount = rejectCount;
	}
	public int getNeedPassCount() {
		return needPassCount;
	}
	public void setNeedPassCount(int needPassCount) {
		this.needPassCount = needPassCount;
	}
	public int getNeedRejectCount() {
		return needRejectCount;
	}
	public void setNeedRejectCount(int needRejectCount) {
		this.needRejectCount = needRejectCount;
	}
	
	
}
