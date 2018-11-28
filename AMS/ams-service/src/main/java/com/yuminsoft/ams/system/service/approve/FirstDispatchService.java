package com.yuminsoft.ams.system.service.approve;


import com.ymkj.ams.api.vo.response.audit.ResBMSAutomaticDispatchAttrVO;

public interface FirstDispatchService {

	/**
	 * 初审自动派单
	 * 
	 * @author dmz
	 * @date 2017年3月7日
	 * @param order
	 */
	public void automaticDispatch(ResBMSAutomaticDispatchAttrVO order);
}
