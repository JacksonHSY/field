package com.yuminsoft.ams.api.service;

import java.util.List;

import com.yuminsoft.ams.api.vo.request.ReqJob;
import com.yuminsoft.ams.api.vo.request.Request;
import com.yuminsoft.ams.api.vo.response.ResJob;


/**
 * 定时任务信息查询 
 * @author fuhongxing
 */
public interface JobService {
	
	/**
	 * 查询信审系统job信息
	 * @return
	 */
	public List<ResJob> getAmsJobInfo(Request req);
	
	/**
	 * 根据job状态或名称查询
	 * @param req
	 * @return
	 */
	public List<ResJob> getAmsJobInfo(ReqJob req);
}
