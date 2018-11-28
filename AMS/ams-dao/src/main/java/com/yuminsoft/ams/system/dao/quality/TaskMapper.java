package com.yuminsoft.ams.system.dao.quality;

import com.yuminsoft.ams.system.domain.uflo.Task;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by ZJY on 2017/3/2.
 */
public interface TaskMapper {
	/**
	 * @Desc: 根据businessId查询任务ID
	 * @param businessId 借款编号
	 * @Author: phb
	 * @Date: 2017/5/10 17:33
	 */
	Long findTaskIdByBusinessId(String businessId);

	/**
	 * @Desc: 根据查询出登录人的任务
	 * @param loginUser 登录用户
	 * @param processId
	 * @Author: phb
	 * @Date: 2017/5/8 20:11
	 */
	List<String> findBusinessIdByLoginUser(@Param("loginUser") String loginUser, @Param("processId") Long processId);

	/**
	 * 根据借款编号，查询当前任务处理人
	 * @param businessId	借款编号
	 * @return
	 * @author wulinjie
	 */
	public Task findByBusinessId(String businessId);
}
