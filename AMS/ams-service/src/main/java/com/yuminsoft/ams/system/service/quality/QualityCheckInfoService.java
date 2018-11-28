package com.yuminsoft.ams.system.service.quality;

import com.yuminsoft.ams.system.domain.quality.QualityCheckInfo;
import com.yuminsoft.ams.system.util.Result;

import java.util.List;
import java.util.Map;


/**
 * 质检信息
 * @author YM10105
 *
 */
public interface QualityCheckInfoService {
	/**
	 * 手工质检申请派单
	 * @param ids
	 * @return
	 */
	public Result<String> closes(String[] ids);
	/**
	 * 批量修改质检员
	 * @param map
	 * @param checkUser 
	 * @return
	 */
	public Result<String> updateCheckUser(Map<String,Object> map, String[] ids, String checkUser);

	/**
	 * 手工质检数据校验
	 *
	 */
	public void importLoanInfo(List<Map<String, String>> sheetDataList);
	/**
	 * 手工质检数据导入删除
	 */
	public void importDeleteInfo(List<Map<String, String>> sheetDataList);


	/**
	 * @Desc: 根据申请件编号查询质检申请件信息
	 * @Author: phb
	 * @Date: 2017/5/2 13:41
	 */
	public QualityCheckInfo findQualityCheckInfoByLoanNo(String loanNo);
	
	/**
	 * 
	 * update
	 */
	int update(QualityCheckInfo info);

	/**
	 * 派单专用
	 * @param info
	 * @return
	 */
	int updateStatus(QualityCheckInfo info);
	
	/**
	 * 系统分派的查询质检件
	 * @author lihuimeng
	 * @date 2017年6月6日 上午11:31:34
	 */
	public List<QualityCheckInfo> findForAssign(Map<String, Object> map);
	
	/**
	 * 根据code查找当前人辖下人员
	 */
	
	public List<String> getStuffList(String usercode);
	/**
	 * 根据code获取name
	 */
	public String getNameByCode(String code);
	
    public QualityCheckInfo findById(Long id);
	
}
