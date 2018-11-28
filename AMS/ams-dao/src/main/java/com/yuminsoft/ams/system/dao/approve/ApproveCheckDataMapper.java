package com.yuminsoft.ams.system.dao.approve;

import com.yuminsoft.ams.system.domain.approve.ApproveCheckData;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ApproveCheckDataMapper {

	public int save(ApproveCheckData approveCheckData);

	public int delete(Long id);

	public int update(ApproveCheckData approveCheckData);

	public ApproveCheckData findById(Long id);

	public ApproveCheckData findOne(Map<String, Object> map);

	public List<ApproveCheckData> findAll(Map<String, Object> map);

	/**
	 * 查询所有需要迁移的借款编号
	 * @param start
	 * @param limit
	 * @return
	 */
	public List<String> findMigratedLoanNo(@Param("start") Integer start,@Param("limit") Integer limit);

	/***
	 * 根据借款编号删除
	 * @author dmz
	 * @date 2017年3月31日
	 * @param loanNo
	 */
	int deleteApproveCheckDataByLoanNo(String loanNo);
	
	/***
	 * 查询个人流水
	 * @author Shipf
	 */
	public List<ApproveCheckData> findPersonalRecord(Map<String, Object> map);
	
	/***
	 * 查询对公流水
	 * @author Shipf
	 */
	public List<ApproveCheckData> findPublicRecord(Map<String, Object> map);

}