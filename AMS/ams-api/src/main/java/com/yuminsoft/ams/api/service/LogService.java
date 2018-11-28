package com.yuminsoft.ams.api.service;

import com.yuminsoft.ams.api.util.ResponseDatagram;
import com.yuminsoft.ams.api.vo.request.ReqUserLog;
import com.yuminsoft.ams.api.vo.request.Request;
import com.yuminsoft.ams.api.vo.response.ResUserLog;

/**
 * 信审系统日志信息查询
 * @author fuhongxing
 */
public interface LogService {
	
	/**
	 * 查询用户操作日志信息(分页)
	 * @return
	 */
	public ResponseDatagram<ResUserLog> getUserLogInfo(Request req);
	
	/**
	 * 根据借款编号查询用户操作日志信息
	 * @return
	 */
	public ResponseDatagram<ResUserLog> getUserLogByLoanNo(ReqUserLog req);
}
