package com.yuminsoft.ams.system.service.quality;

import com.yuminsoft.ams.system.domain.quality.QualityCheckInfo;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;
import com.yuminsoft.ams.system.vo.quality.QualityControlDeskVo;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 质检工作台 
 * @author YM10105
 *
 */
public interface QualityInspectionProcessService {
	/**
	 * @Desc: 查询质检处理情况待处理列表
	 * @Author: phb
	 * @Date: 2017/5/17 17:55
	 */
	public ResponsePage<QualityControlDeskVo> getPageTodoList(RequestPage requestPage, QualityControlDeskVo qualityControlDeskVo);

	/**
	 * @Desc: 查询质检处理情况已完成列表
	 * @Author: phb
	 * @Date: 2017/5/17 17:55
	 */
	public ResponsePage<QualityControlDeskVo> getPageDoneList(RequestPage requestPage, QualityControlDeskVo qualityControlDeskVo);

	/**
	 * @Desc: 导出待处理列表
	 * @Author: phb
	 * @Date: 2017/5/6 17:32
	 */
	public void exportToDoList(QualityControlDeskVo qualityControlDeskVo, HttpServletRequest request, HttpServletResponse response);

	/**
	 * @Desc: 质检修改信审人员
	 * @Author: phb
	 * @Date: 2017/5/9 17:25
	 */
	public Result<String> modifyApprovePerson(QualityCheckInfo qualityCheckInfo);

	/**
	 * @Desc: 	导出已完成列表
	 * @Author: phb
	 * @Date: 2017/5/6 17:33
	 */
	public void exportDoneExcel(QualityControlDeskVo qualityControlDeskVo, HttpServletRequest req, HttpServletResponse res);

	/**
	 * @Desc: 查询符合条件的申请件自动发起反馈
	 * @Author: phb
	 * @Date: 2017/5/10 20:54
	 */
	public Result<String> qualityFeedbackJobExecute();
	
	public List<QualityControlDeskVo> buildQueryResult(List<QualityControlDeskVo> qualityInfoList);
}
