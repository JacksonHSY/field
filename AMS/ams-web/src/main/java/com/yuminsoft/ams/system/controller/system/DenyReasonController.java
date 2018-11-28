package com.yuminsoft.ams.system.controller.system;

import com.ymkj.ams.api.service.approve.integrate.ReasonExecuter;
import com.ymkj.ams.api.vo.request.master.ReqBMSTMReasonVO;
import com.ymkj.ams.api.vo.response.master.ResListVO;
import com.yuminsoft.ams.system.controller.BaseController;
import com.yuminsoft.ams.system.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 拒绝原因管理
 * @author zhouwq
 */
@Controller
@RequestMapping("/denyReason")
public class DenyReasonController extends BaseController{

	@Autowired
	private ReasonExecuter reasonExecuter;

	/**
	 * 获取拒绝或者退回原因列表
	 * @param req
	 * @return
	 * @author JiaCX
	 * @date 2017年5月5日 上午10:17:58
	 */
	@RequestMapping("/getReasonList")
	@ResponseBody
	public List<ReqBMSTMReasonVO> getReasonList(ReqBMSTMReasonVO req){
		req.setSysCode("ams");

		ResListVO<ReqBMSTMReasonVO> reasonList;

		if("1".equals(req.getType())){
			reasonList = reasonExecuter.getPrimaryReason(req);
		}else{
			reasonList = reasonExecuter.getSecondaryReason(req);
		}

		if(reasonList != null && CollectionUtils.isEmpty(reasonList.getCollections())){
			return reasonList.getCollections();
		}

		return new ArrayList<ReqBMSTMReasonVO>();
	}
	
}