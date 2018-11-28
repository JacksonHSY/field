package com.yuminsoft.ams.system.dao.approve;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;
import com.yuminsoft.ams.system.domain.approve.MobileHistory;
import com.yuminsoft.ams.system.vo.firstApprove.MobileHistoryVO;


public interface MobileHistoryMapper {
    
    public int delete(Long id);
    
    public int save(MobileHistory mobileHistory);
    
    public int update(MobileHistory record);
    
    public int updateMobileHisById(MobileHistory record);
    
    public MobileHistory findById(Long id);
    
    public MobileHistory findOne(Map<String, Object> map);
    
    public List<MobileHistory> findAll(Map<String, Object> map);
    
    public List<MobileHistory> queryMobileHisByLoanId(String loanId);
    
    public List<MobileHistoryVO> queryMobileHis(String loanId);
    
    /**
     * 根据号码查找第三方电话来源的借款编号列表
     * 
     * @param telPhone
     * @return
     * @author JiaCX
     * @date 2017年7月7日 上午11:10:50
     */
    public List<String> findLoanNosByThirdTelNum(@Param("telPhone") String telPhone);

	/**
	 * 根据借款编号查询电核记录
	 * 
	 * @author Jia CX
	 * <p>2018年3月27日 下午4:26:01</p>
	 * 
	 * @param loanNo
	 * @return
	 */
	public Page<MobileHistory> queryTelephoneCheckList(@Param("loanNo") String loanNo);
    
}