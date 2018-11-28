package com.yuminsoft.ams.system.dao;

import com.yuminsoft.ams.system.domain.DebtsInfo;

import java.util.List;
import java.util.Map;

public interface DebtsInfoMapper {

	int save(DebtsInfo debtsInfo);

	int delete(Long id);

	int update(DebtsInfo debtsInfo);

	DebtsInfo findById(Long id);

	List<DebtsInfo> findByLoanNo(String loanNo);

	DebtsInfo findOne(Map<String, Object> map);

	List<DebtsInfo> findAll(Map<String, Object> map);

	int deleteDebtsInfoByLoanNo(String loanNo);

    /**
     * 查询负债汇总信息
     * @param loanNo
     * @author wulj
     * @return
     */
    DebtsInfo findOutCreditDebtByLoanNo(String loanNo);
}