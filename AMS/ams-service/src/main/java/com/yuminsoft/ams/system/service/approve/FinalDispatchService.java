package com.yuminsoft.ams.system.service.approve;

import com.ymkj.ams.api.vo.response.audit.ResBMSAutomaticDispatchAttrVO;
import com.yuminsoft.ams.system.exception.FinalDispatchException;

import java.util.List;

/**
 * 终审自动派单Service
 */
public interface FinalDispatchService {

	/**
	 * 终审自动派单
	 * @param order
	 * @author wulj
	 */
	void automaticDispatch(ResBMSAutomaticDispatchAttrVO order) throws FinalDispatchException;

	/**
	 * 初始化终审待分派池
	 * @param orders
	 * @author wulj
	 * @return
	 */
	void initOrderPool(List<ResBMSAutomaticDispatchAttrVO> orders);

	/**
	 * 销毁终审待分派池
	 * @author wulj
	 */
	void destroyOrderPool();
}
