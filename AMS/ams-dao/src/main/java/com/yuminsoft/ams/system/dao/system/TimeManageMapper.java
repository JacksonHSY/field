package com.yuminsoft.ams.system.dao.system;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;
import com.yuminsoft.ams.system.domain.system.TimeManagement;

/**
 * 系统使用时间限制mapper
 * 
 * @author Jia CX
 * @date 2017年12月11日 下午3:02:53
 * @notes
 * 
 */
public interface TimeManageMapper {

	/**
	 * 根据条件查找列表
	 * 
	 * @author Jia CX
	 * @date 2017年12月11日 下午3:10:38
	 * @notes
	 * 
	 * @param queryMap
	 * @return
	 */
	Page<TimeManagement> findAll(Map<String, Object> queryMap);

	/**
	 * 根据条件查找单个记录
	 * 
	 * @author Jia CX
	 * @date 2017年12月11日 下午4:34:02
	 * @notes
	 * 
	 * @param queryMap
	 * @return
	 */
	TimeManagement findOne(Map<String, Object> queryMap);

	/**
	 * 更新一条记录
	 * 
	 * @author Jia CX
	 * @date 2017年12月11日 下午4:41:37
	 * @notes
	 * 
	 * @param tm
	 */
	void update(TimeManagement tm);

	/**
	 * 插入一条记录
	 * 
	 * @author Jia CX
	 * @date 2017年12月11日 下午4:44:49
	 * @notes
	 * 
	 * @param t
	 */
	void insert(TimeManagement t);

	/**
	 * 删除某个角色今天之后的单独日期的记录
	 * 
	 * @author Jia CX
	 * @date 2017年12月11日 下午4:49:19
	 * @notes
	 * 
	 */
	void deleteAppointedDate(@Param("roleCode") String roleCode);

	/**
	 * 根据id删除某条记录
	 * 
	 * @author Jia CX
	 * @date 2017年12月11日 下午5:02:27
	 * @notes
	 * 
	 * @param id
	 * @return
	 */
	void delete(@Param("id") Long id);

	/**
	 * 查询某个角色今天的有效时间限制记录（可能会有多条，如果有单独时间设置，以单独时间设置为准）
	 * 
	 * @author Jia CX
	 * @date 2017年12月12日 上午10:07:38
	 * @notes
	 * 
	 * @param queryMap
	 * @return
	 */
	TimeManagement findOneOfToday(Map<String, Object> queryMap);

	/**
	 * 批量插入
	 * 
	 * @author Jia CX
	 * <p>2018年5月15日 下午2:35:59</p>
	 * 
	 * @param insertList
	 */
	void batchInsert(List<TimeManagement> list);

	/**
	 * 批量删除
	 * 
	 * @author Jia CX
	 * <p>2018年5月15日 下午4:15:49</p>
	 * 
	 * @param ids
	 */
	void batchDelete(@Param("ids") String[] ids);

	/**
	 * 批量更新
	 * 
	 * @author Jia CX
	 * <p>2018年5月15日 下午7:25:32</p>
	 * 
	 * @param queryMap
	 */
	void batchUpdate(Map<String, Object> queryMap);

}
