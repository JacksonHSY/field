package com.yuminsoft.ams.api.service;

import com.yuminsoft.ams.api.util.ResponseDatagram;
import com.yuminsoft.ams.api.vo.request.ReqApprove;
import com.yuminsoft.ams.api.vo.response.ResApprove;

/**
 * 审批意见查询
 * @author fuhongxing
 */
public interface ApproveService {
	
	/**
	 * 查询审批意见信息
	 * @return
	 */
	public ResponseDatagram<ResApprove> getApproveInfo(ReqApprove req);
	
}
