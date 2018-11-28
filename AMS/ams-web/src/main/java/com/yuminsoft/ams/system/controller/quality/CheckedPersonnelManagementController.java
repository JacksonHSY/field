package com.yuminsoft.ams.system.controller.quality;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedHashTreeMap;
import com.google.gson.reflect.TypeToken;
import com.ymkj.pms.biz.api.vo.response.ResOrganizationTreeVO;
import com.yuminsoft.ams.system.common.AmsConstants;
import com.yuminsoft.ams.system.domain.quality.QualityRegularInfo;
import com.yuminsoft.ams.system.service.quality.CheckedPersonnelManagementService;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;

/**
 * 被检人员管理
 * @author YM10174
 */
@Controller
@RequestMapping("/checkedPersonnelManagement")
public class CheckedPersonnelManagementController {
	
	@Autowired
	private CheckedPersonnelManagementService checkedPersonnelManagementService;
	/**
	 * 被检人员管理
	 * @return
	 */
	@RequestMapping("/checkedPersonnelManagement")
	public String checkedPersonnelManagement() {
		return "quality/checkedPersonnelManagement/checkedPersonnelManagement";
	}
	/**
	 * 调用dubbo接口查询被检人员树（只查出初审人员）
	 * @return
	 */
	@RequestMapping("/getPersonTree")
	@ResponseBody
	public String getPersonTree() {
			return checkedPersonnelManagementService.getPersonTree();
	}
	
	/**
	 * 分页显示日志
	 * @return
	 */
	@RequestMapping("/pageList")
	@ResponseBody
	public ResponsePage<QualityRegularInfo> getPage(RequestPage requestPage,QualityRegularInfo qualityRegularInfo) {
		return checkedPersonnelManagementService.getPageList(requestPage,qualityRegularInfo);
	}
	
	/**
	 * 添加
	 * @param request
	 * @return
	 */
	@RequestMapping("/save")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public Result<String> deleteBatch(QualityRegularInfo qualityRegularInfo) {
			Gson gson = new Gson();
			List<ResOrganizationTreeVO> rs=new ArrayList<ResOrganizationTreeVO>();  
	        Type type = new TypeToken<ArrayList<ResOrganizationTreeVO>>() {}.getType(); 
	        rs=gson.fromJson(qualityRegularInfo.getUsers(), type);
	        Result<String> result = new Result<String>();
	        try{
	        	 
	        	 for(ResOrganizationTreeVO vo :rs){
	        		 //人员code存放在attributes中，得到usercode
	        		 LinkedHashTreeMap<String,String>  userDetail = (LinkedHashTreeMap<String,String>)vo.getAttributes();
	        		 Map<String, Object> map = new HashMap<String,Object>();
	        		 if(userDetail!=null){
	        			 qualityRegularInfo.setCheckedUser(userDetail.get("usercode"));
	        			 qualityRegularInfo.setCheckedUserName(vo.getText());
	        			 //如果当前人员已存在，则删除
	        			 map.put("checkedUser", userDetail.get("usercode"));
	        			 map.put("checkedUserName", vo.getText());
	        			 map.put("ifDelete", AmsConstants.TWO);
	        			 QualityRegularInfo info = checkedPersonnelManagementService.findOne(map);
	        			 if(info!=null){
	        				 checkedPersonnelManagementService.delete(info.getId());
	        			 }
	        			 checkedPersonnelManagementService.save(qualityRegularInfo);
	        			 
	        		 }
	        	 }
	        	 result.setType(Result.Type.SUCCESS);
	         }catch(Exception e){
	        	 result.setType(Result.Type.FAILURE);
	         }
		return result;
	}
	
	/**
	 * 删除
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteBatch")
	@ResponseBody
	public Result<String> deleteBatch(HttpServletRequest request,String[] ids) {
		return checkedPersonnelManagementService.deleteBatch(ids);
	}
}
