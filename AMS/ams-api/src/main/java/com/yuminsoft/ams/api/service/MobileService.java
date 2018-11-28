package com.yuminsoft.ams.api.service;

import com.yuminsoft.ams.api.util.ResponseDatagram;
import com.yuminsoft.ams.api.vo.request.ReqMobileHistory;
import com.yuminsoft.ams.api.vo.response.ResMobileHistory;

/**
 *  电核信息查询
 * @author fuhongxing
 */
public interface MobileService {
	/**
	 * 查询电核信息
	 * @return
	 */
	public ResponseDatagram<ResMobileHistory> getMobileHistoryInfo(ReqMobileHistory req);
}
