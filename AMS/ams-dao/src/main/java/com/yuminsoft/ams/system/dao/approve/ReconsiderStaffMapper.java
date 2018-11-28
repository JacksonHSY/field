/**
 * 
 */
package com.yuminsoft.ams.system.dao.approve;

import com.yuminsoft.ams.system.domain.approve.ReconsiderStaff;

import java.util.List;
import java.util.Map;

/**
 * 复议mapper
 * 
 * @author Jia CX
 * <p>2018年6月12日 下午2:25:45</p>
 * 
 */
public interface ReconsiderStaffMapper {

	ReconsiderStaff findOne(Map<String,Object> map);

	List<ReconsiderStaff> findAll(Map<String,Object> map);
	
	boolean insert(ReconsiderStaff staff);
	
	boolean update(ReconsiderStaff staff);
	
	void batchInsert(List<ReconsiderStaff> list);
	
	void batchUpdate(List<ReconsiderStaff> list);

	/**
	 * 根据级别查询所有有效的复议人员
	 * @param map
	 * @return
	 */
	List<ReconsiderStaff> findReconsiderStaffByRuleLevel(Map<String,Object> map);

	/**
	 * 根据复议层级和该层级最后一次分派人，查询下一个可以派单的复议员工code
	 *
	 * @author Jia CX
	 * <p>2018年6月20日 下午6:14:50</p>
	 *
	 * @param map
	 * @return
	 */
	ReconsiderStaff findDispatchUser(Map<String, Object> map);

	/**
	 * 复议改派查询可以改派的人
	 * @param map
	 * @return
	 */
	List<ReconsiderStaff> findReconsiderReformHandler(Map<String,Object> map);
}
