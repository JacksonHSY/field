package com.yuminsoft.ams.system.controller.approve;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.remoting.TimeoutException;
import com.ymkj.bds.biz.api.vo.request.AddressInfoReqVO;
import com.ymkj.bds.biz.api.vo.request.LicensePlateReqVO;
import com.ymkj.bds.biz.api.vo.request.NameAndIdCardReqVO;
import com.ymkj.bds.biz.api.vo.request.UnitNameReqVO;
import com.ymkj.bds.biz.api.vo.response.*;
import com.yuminsoft.ams.system.controller.BaseController;
import com.yuminsoft.ams.system.service.bds.BdsApiService;
import com.yuminsoft.ams.system.util.BeanUtil;
import com.yuminsoft.ams.system.vo.apply.PhoneNumMatchResVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 内部匹配
 * 
 * @author JiaCX
 * 2017年6月13日 下午4:58:19
 *
 */
@Controller
@RequestMapping("/finishInsideMatch")
public class FinishInsideMatchController extends BaseController{

	@Autowired
	private BdsApiService bdsApiService;
	
	/**
	 * 内部匹配---电话号码匹配
	 * 
	 * @param loanNo
	 * @return
	 * @author JiaCX
	 * @date 2017年6月13日 下午4:01:04
	 */
	@RequestMapping("/matchByPhoneNumber/{loanNo}")
	@ResponseBody
	public List<PhoneNumMatchResVO>  matchByPhoneNumber(@PathVariable String loanNo) {
	    List<PhoneNumMatchResVO> resultList = new ArrayList<PhoneNumMatchResVO>();
	    try{
	        List<PhoneNumberResVO> list = bdsApiService.matchByPhoneNumber(loanNo);
	        for(PhoneNumberResVO phoneNumberResVO: list) {
	            if(CollectionUtils.isNotEmpty(phoneNumberResVO.getData())){
	                for(MatchPhoneNumberResVO tem: phoneNumberResVO.getData()) {
	                    PhoneNumMatchResVO vo = BeanUtil.copyProperties(tem, PhoneNumMatchResVO.class);
	                    vo.setSource(phoneNumberResVO.getSource());
	                    vo.setFieldName(phoneNumberResVO.getFieldName());
	                    resultList.add(vo);
	                }
	            }
	        }
        } catch (Exception e){
	    	if(e instanceof TimeoutException){
				LOGGER.error("内部匹配电话号码匹配查询超时", e);
			}else{
	    		LOGGER.error("内部匹配电话号码匹配查询异常", e);
			}
		}

		return resultList;
	}
	
	/**
	 * 姓名身份证信息匹配
	 * @param req
	 * @return
	 * @author JiaCX
	 * @date 2017年5月3日 下午3:07:57
	 */
	@RequestMapping("/matchByNameAndIdCard")
	@ResponseBody
	public List<NameAndIdCardResVO> matchByNameAndIdCard(NameAndIdCardReqVO req){
	    return bdsApiService.matchByNameAndIdCard(req);
	}
	
	
	/**
	 *单位名称信息匹配
	 * 
	 * @param req
	 * @return
	 * @author JiaCX
	 * @date 2017年5月3日 下午3:07:57
	 */
	@RequestMapping("/matchByUnitName")
	@ResponseBody
	public List<UnitNameResVO> matchByUnitName(UnitNameReqVO req){
	    return bdsApiService.matchByUnitName(req);
	}
	
	/**
	 * 地址信息匹配
	 * 
	 * @param req
	 * @return
	 * @author JiaCX
	 * @date 2017年5月3日 下午4:23:54
	 */
	@RequestMapping("/matchByAddressInfo")
	@ResponseBody
	public List<AddressInfoResVO> matchByAddressInfo(AddressInfoReqVO req){
	    return bdsApiService.matchByAddressInfo(req);
	}
	
	/**
	 * 车牌号匹配
	 * 
	 * @param req
	 * @return
	 * @author JiaCX
	 * @date 2017年5月3日 下午4:44:07
	 */
	@RequestMapping("/matchByLicensePlate")
	@ResponseBody
	public List<LicensePlateHistoryResVO> matchByLicensePlate(LicensePlateReqVO req){
	    return bdsApiService.matchByLicensePlate(req);
	}
	
}
