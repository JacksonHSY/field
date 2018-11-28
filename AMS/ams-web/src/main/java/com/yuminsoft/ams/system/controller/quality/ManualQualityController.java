package com.yuminsoft.ams.system.controller.quality;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.common.FileEnum;
import com.yuminsoft.ams.system.common.QualityEnum;
import com.yuminsoft.ams.system.common.ResponseEnum;
import com.yuminsoft.ams.system.controller.BaseController;
import com.yuminsoft.ams.system.domain.quality.QualityTaskInfo;
import com.yuminsoft.ams.system.excel.ExcelTemplet;
import com.yuminsoft.ams.system.excel.ExcelUtil;
import com.yuminsoft.ams.system.exception.FileException;
import com.yuminsoft.ams.system.service.quality.QualityCheckInfoService;
import com.yuminsoft.ams.system.service.quality.QualityControlDeskService;
import com.yuminsoft.ams.system.service.quality.QualityTaskInfoService;
import com.yuminsoft.ams.system.util.ResponseInfo;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;
import com.yuminsoft.ams.system.vo.quality.QualityControlDeskVo;

/**
 * @author lihm
 * 质检手工申请派单
 * @data 2017年6月6日下午5:17:34
 */
@Controller
@RequestMapping("/manualQuality")
public class ManualQualityController extends BaseController{
	
	@Autowired
	private QualityControlDeskService qualityControlDeskService;
	
	@Autowired
	private QualityCheckInfoService qualityCheckInfoService;
	
	@Autowired
	private QualityTaskInfoService taskInfoService;
	
	@Value("${sys.code}")
	public String sysCode;
	
	/**
	 * 手工质检申请派单
	 * @return
	 */
	@RequestMapping("/manualQuality")
	public String manualQuality() {
		Map<String, String> infoMap = qualityControlDeskService.getQualityRolesInfo(ShiroUtils.getCurrentUser().getUsercode());
		request.getSession().setAttribute("roleCode",infoMap.get("roleCode"));
		return "quality/manualQuality/manualQuality";
	}
	
	/**
	 * 手工分派列表
	 * @return
	 */
	@RequestMapping("/manualDispatchList")
	@ResponseBody
	public ResponsePage<QualityControlDeskVo> getToDoPage(RequestPage requestPage,QualityControlDeskVo qualityControlDeskVo)  {
		return qualityControlDeskService.getPageList(requestPage, qualityControlDeskVo, QualityEnum.MenuFlag.质检手工分派.getCode());
	}

	/**
	 * 手工分派列表导出
	 * @return
	 */
	@RequestMapping("/exportManualDispatchList")
	@ResponseBody
	public void exportManualDispatchList(QualityControlDeskVo qualityControlDeskVo,HttpServletRequest request,HttpServletResponse response) {
		qualityControlDeskService.exportManualDispatchList(qualityControlDeskVo,request,response);
	}

	/**
	 * 手工分派关闭
	 * @param request
	 * @param ids
	 * @return
	 */
	@RequestMapping("/close")
	@ResponseBody
	public Result<String> close(HttpServletRequest request, @RequestBody String[] ids) {
		return qualityCheckInfoService.closes(ids);
	}
	
	/**
	 * @author lihuimeng
	 * @date 2017年6月6日 下午5:18:15
	 * 手工派单
	 */
	@RequestMapping("/updateCheckUser")
	@ResponseBody
	public Result<String> updateCheckUser(HttpServletRequest request, @RequestBody String[] ids) {
		String checkUser = request.getParameter("checkUser");
		Map<String, Object> map = new HashMap<>();
		map.put("assignDate", new Date());
		map.put("assignType", QualityEnum.QualityAssignType.MANUAL_ASSIGN.getCode());
		return qualityCheckInfoService.updateCheckUser(map,ids,checkUser);
	}

	/**
	 * 导入
	 * @param file
	 * @param req
	 * @param response
	 */
	@RequestMapping(value="/uploadExcle",method=RequestMethod.POST)
	public void uploadExcle(@RequestParam("loadExcel") MultipartFile file,HttpServletRequest req, HttpServletResponse response){
		 //解析excle
		OutputStream outputStream = null;
		 ResponseInfo responseInfo = null;
		 Date date = new Date();
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		 StringBuffer str = new StringBuffer(file.getOriginalFilename());
		 String fileName = str.insert(str.lastIndexOf("."),"导入结果"+sdf.format(date)).toString();
		try {
			Workbook workbook = WorkbookFactory.create(file.getInputStream());
			ExcelTemplet et = new ExcelTemplet().new QualityInputExcel();
			List<Map<String, String>> list =ExcelUtil.getExcelData(workbook,et);
			if(CollectionUtils.isEmpty(list)){
	            throw new FileException(FileEnum.FILE_EMPTY_FILE,"导入文件不能为空！");
	        }
				qualityCheckInfoService.importLoanInfo(list);
				ExcelUtil.addResultToWorkBook(workbook, list, et);
				response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(fileName, "UTF-8"));
		        response.setContentType(file.getContentType());
		        outputStream = response.getOutputStream();
		        workbook.write(outputStream);
		        outputStream.flush();
		        return;
			//qualityCheckInfoService.inportCheckInfo(list);
		} catch (EncryptedDocumentException | InvalidFormatException e) {
			 responseInfo = new ResponseInfo(ResponseEnum.VALIDATE_FORMAT.getCode(),e.getMessage());
		} catch (IOException e) {
			responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
		} try {
	        response.setContentType("text/html");
	        response.getWriter().write(JSON.toJSONString(responseInfo));
			} catch (Exception  e) {
	    }
	}
	
	/**
	 * 导入删除
	 * @param file
	 * @param req
	 * @param response
	 */
	@RequestMapping(value="/deleteExcle",method=RequestMethod.POST)
	public void deleteExcle(@RequestParam("loadExcel") MultipartFile file,HttpServletRequest req, HttpServletResponse response){
		 //解析excle
		 OutputStream outputStream = null;
		 ResponseInfo responseInfo = null;
		 Date date = new Date();
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		 StringBuffer str = new StringBuffer(file.getOriginalFilename());
		 String fileName = str.insert(str.lastIndexOf("."),"导入结果"+sdf.format(date)).toString();
		try {
			Workbook workbook = WorkbookFactory.create(file.getInputStream());
			ExcelTemplet et = new ExcelTemplet().new QualityDeleteExcel();
			List<Map<String, String>> list =ExcelUtil.getExcelData(workbook,et);
			if(CollectionUtils.isEmpty(list)){
	            throw new FileException(FileEnum.FILE_EMPTY_FILE,"导入文件不能为空！");
	        }
				qualityCheckInfoService.importDeleteInfo(list);
				ExcelUtil.addResultToWorkBook(workbook, list, et);
				response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(fileName, "UTF-8"));
		        response.setContentType(file.getContentType());
		        outputStream = response.getOutputStream();
		        workbook.write(outputStream);
		        outputStream.flush();
		        return;
			//qualityCheckInfoService.inportCheckInfo(list);
		} catch (EncryptedDocumentException | InvalidFormatException e) {
			 responseInfo = new ResponseInfo(ResponseEnum.VALIDATE_FORMAT.getCode(),e.getMessage());
		} catch (IOException e) {
			responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
		} try {
	        response.setContentType("text/html");
	        response.getWriter().write(JSON.toJSONString(responseInfo));
			} catch (Exception  e) {
	    }
	}
	
	/**
	 * 获取被分派的质检员
	 * @author lihuimeng
	 * @date 2017年6月22日 下午12:58:13
	 */
	@RequestMapping("/getAssignPerson")
	@ResponseBody
	public List<QualityTaskInfo> getAssignPerson(){
		String userCode = ShiroUtils.getCurrentUser().getUsercode();
		return taskInfoService.findQualityUser(sysCode, userCode);
	}
}




















