package com.yuminsoft.ams.system.excel;

import com.yuminsoft.ams.system.common.FileEnum;
import com.yuminsoft.ams.system.exception.FileException;
import com.yuminsoft.ams.system.util.DateUtils;
import com.yuminsoft.ams.system.util.Strings;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.*;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Excel 工具类
 * @author fuhongxing
 */
public class ExcelUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtil.class);
    /**
     * 解析Excel文件，将单元格数据按指定数据格式写入Map集合
     * 
     * @param workBook
     * @param excelTemplet
     * @return
     */
    public static List<Map<String, String>> getExcelData(Workbook workBook,ExcelTemplet excelTemplet) {
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        Sheet sheet = getSheet(workBook, excelTemplet);
        int startRow = excelTemplet.getStartRow();
        /** Excel 索引下标从0开始，进行减一操作 **/
        if ((startRow - 1) < 0) {
            throw new FileException(FileEnum.FILE_ERROR_PARSE, "startRow参数有误,原值为[" + startRow + "]");
        }
        startRow--;
        try {
            if (sheet.getFirstRowNum() != 0) {
                /** 第一行为空行 **/
                throw new FileException(FileEnum.FILE_ERROR_FORMAT_CONTENT, "1");
            }
            /** 单元格数据对照Map **/
            Map<String, String> columnMap = excelTemplet.getColumnMap();
            for (int j = startRow; j <= sheet.getLastRowNum(); j++) {
                Row row = sheet.getRow(j);
                if (row == null) {
                    continue;
                }
                result.add(convertDataToMap(row, columnMap));
            }
        } catch (Exception e) {
        	LOGGER.error("解析Excel文件，将单元格数据按指定数据格式写入Map集合异常", e);
            throw new FileException(FileEnum.FILE_ERROR_PARSE, e.getMessage());
        }
        return result;
    }

    /**
     * 获取Sheet
     * 
     * @param workBook
     * @param excelTemplet
     * @return
     */
    public static Sheet getSheet(Workbook workBook, ExcelTemplet excelTemplet) {
        String sheetName = Strings.convertValue(excelTemplet.getSheetName(), String.class);
        Sheet sheet = null;
        if (Strings.isEmpty(sheetName)) {
            sheet = workBook.getSheetAt(0);
        } else {
            sheet = workBook.getSheet(sheetName);
        }
        //判断sheet文件是否为空
        if(sheet == null){
        	throw new FileException(FileEnum.FILE_ERROR_PARSE, "未找到sheet");
        }
        return sheet;
    }

    /**
     * 将Excel行数据转换成map
     * 
     * @param row
     * @param template
     * @return
     */
    public static Map<String, String> convertDataToMap(Row row,Map<String, String> columnTemplate) {
        Map<String, String> returnMap = new HashMap<String, String>();
        for (Map.Entry<String, String> entry : columnTemplate.entrySet()) {
            String colName = entry.getKey();
            int colIndex = CellReference.convertColStringToIndex(colName);
            Cell cell = row.getCell(colIndex);
            // Assert.notNull(cell, ResponseEnum.FILE_ERROR_PARSE,"单元格[" +
            // colName + "] 未找到，请确认数据");
            String ret = "";
            if (cell != null) {
                switch (cell.getCellType()) {
                case Cell.CELL_TYPE_BLANK:
                    /** 空值 **/
                    ret = "";
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    /** 布尔型 **/
                    ret = Strings.convertValue(cell.getBooleanCellValue(), String.class);
                    break;
                case Cell.CELL_TYPE_ERROR:
                    /** 错误 **/
                    ret = "";
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    /** 公式类型 **/
                    // Workbook wb = cell.getSheet().getWorkbook();
                    // CreationHelper crateHelper = wb.getCreationHelper();
                    // FormulaEvaluator evaluator =
                    // crateHelper.createFormulaEvaluator();
                    // ret = getCellValue(evaluator.evaluateInCell(cell));
                    ret = "";
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    /** 数值型 **/
                    if (DateUtil.isCellDateFormatted(cell)) {
                        Date theDate = cell.getDateCellValue();
                        //格式化时间
                        ret = DateUtils.dateToString(theDate, DateUtils.FORMAT_DATE_YYYY_MM_DD);
                    } else {
                        ret = NumberToTextConverter.toText(cell.getNumericCellValue());
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    /** 字符串型 **/
                    ret = cell.getRichStringCellValue().getString();
                    break;
                default:
                    ret = "";
                }
            }
            ret = Strings.convertValue(ret, String.class);
            returnMap.put(entry.getValue(), ret == null ? "" : ret.trim());
        }
        return returnMap;
    }

    /**
     * 将处理结果 追加单元格，供下载
     * 
     * @param workBook
     * @param columnTemplate
     * @return
     */
    public static Workbook addResultToWorkBook(Workbook workBook,List<Map<String, String>> result, ExcelTemplet excelTemplet) {
        Sheet sheet = getSheet(workBook, excelTemplet);
        int startRow = excelTemplet.getStartRow();
        /** Excel 索引下标从0开始，进行减一操作 **/
        if ((startRow - 1) < 0) {
            throw new FileException(FileEnum.FILE_ERROR_PARSE, "startRow参数有误,原值为[" + startRow + "]");
        }
        startRow--;
        String feedBackKeyName = ExcelTemplet.FEED_BACK_MSG;
        if (Strings.isEmpty(feedBackKeyName)) {
            return workBook;
        }
        /** 写入单元格的索引 **/
        String writeColumnName = excelTemplet.getFeedBackColumnName();
        int writeColumnIndex = CellReference.convertColStringToIndex(writeColumnName);
        int temp = writeColumnIndex + 1;
        try {
            /** 设置第一行Title单元格 **/
            Row row = sheet.getRow(startRow - 1);
            if (row != null) {
            	Cell cell = row.createCell(writeColumnIndex,Cell.CELL_TYPE_STRING);
                cell.setCellValue("反馈结果");
                // 设置标题为黑色粗体
                CellStyle style = getBoldStyle(workBook);
                cell.setCellStyle(style);
                
            	//需要添加的列数据
            	if(!CollectionUtils.isEmpty(excelTemplet.getResultCell())){
            		for (String c : excelTemplet.getResultCell()) {
            			cell = row.createCell(temp++,Cell.CELL_TYPE_STRING);
                        cell.setCellValue(c);
                        // 设置标题为黑色粗体
                        style = getBoldStyle(workBook);
                        cell.setCellStyle(style);
					}
            	}
            }
            // 红色字体样式
            CellStyle colorStyle = getRedStyle(workBook);
            for (int j = startRow, i = 0; j <= sheet.getLastRowNum(); j++) {
                row = sheet.getRow(j);
                if (row == null) {
                    continue;
                }
                Cell cell = row.createCell(writeColumnIndex, Cell.CELL_TYPE_STRING);
                String cellValue = Strings.convertValue(result.get(i).get(feedBackKeyName), String.class);
                cell.setCellValue(cellValue);
                cell.setCellStyle(colorStyle);
                int cloum = writeColumnIndex+1;
                //需要添加的列数据
            	if(!CollectionUtils.isEmpty(excelTemplet.getResultCell())){
            		//此处先写死，后期通过反射实现
            		cell = row.createCell(cloum++,Cell.CELL_TYPE_STRING);
                    cell.setCellValue(result.get(i).get("oneMonthQueryCount"));
                    
                   
                    
            	}
                
                i++;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new FileException(FileEnum.FILE_ERROR_WRITE,ex.getMessage());
        }
        return workBook;
    }
    
    /**
     * 获取黑色粗体样式
     * @param workbook
     * @return
     */
    private static CellStyle getBoldStyle(Workbook workbook) {
        // 设置标题字体大小
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        // 设置单元格粗体
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    /**
     * 获取红色字体样式
     * @param workbook
     * @return
     */
    private static CellStyle getRedStyle(Workbook workbook) {
        // 设置单元格字体颜色
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        // 设置单元格字体为红色
        font.setColor(HSSFColor.RED.index);
        style.setFont(font);
        return style;
    }
    
    
    
    /**
     * 生成excel
     * @param headers 标题
     * @param dataset 数据
     * @param pattern 日期格式
     * @return
     * @throws Exception
     * 
     * @author fuhongxing
     */
    public static <T> Workbook getWorkbook(String sheetName, String[] headers, Collection<T> dataset, String pattern) throws Exception {
    	LOGGER.info(" ======开始创建excel======");
        // 声明一个工作薄
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 生成一个表格
        XSSFSheet sheet = workbook.createSheet(sheetName);
        // 设置表格默认列宽度为20个字节
        sheet.setDefaultColumnWidth((short) 20);
        // 生成一个样式
        XSSFCellStyle style = workbook.createCellStyle();
        // 设置样式
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        XSSFCell cell = null;
        // 设置表格标题行
        XSSFRow row = sheet.createRow(0);
        for (short i = 0; i < headers.length; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(style);
            cell.setCellValue(headers[i]);
        }
        
        // 遍历集合数据，设置数据内容
        Iterator<T> it = dataset.iterator();
        int index = 0;
        while (it.hasNext()) {
            index++;
            row = sheet.createRow(index);
            T t = it.next();
            // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
            List<String> fields = getFields(t);
            for (short i = 0; i < headers.length; i++) {
                cell = row.createCell(i);//创建单元格
                cell.setCellStyle(style);
                String fieldName = fields.get(i);
                
                //拼接get方法名
                String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Method getMethod = t.getClass().getMethod(getMethodName, new Class[] {});//获取方法
                Object value = getMethod.invoke(t, new Object[] {});//调用方法返回值
                // 判断值的类型后进行强制类型转换
                if (value instanceof Date) {
                    Date date = (Date) value;
                    SimpleDateFormat format = new SimpleDateFormat(pattern);
                    if(!StringUtils.isEmpty(value)){
                        cell.setCellValue(format.format(date));
                    }
                } else {
                    // 其它数据类型都当作字符串简单处理
                    if(!StringUtils.isEmpty(value)){
                        cell.setCellValue(value.toString());
                    }
                }
            }
        }
        return workbook;
    }  
    
    /**
     * 生成excel
     * @param headers 标题
     * @param dataset 数据
     * @param pattern 日期格式
     * @return
     * @throws Exception
     * 
     * @author fuhongxing
     */
    public static <T> XSSFWorkbook getWorkbook(XSSFWorkbook workbook,String sheetName, String[] headers, Collection<T> dataset,int index, String pattern) throws Exception {
    	LOGGER.info(" ======开始创建excel======");
        // 生成一个表格
        XSSFSheet sheet = workbook.getSheet(sheetName);
        if(sheet == null){
        	sheet = workbook.createSheet(sheetName);
        }
        // 设置表格默认列宽度为20个字节
        sheet.setDefaultColumnWidth((short) 20);
        // 生成一个样式
        XSSFCellStyle style = workbook.createCellStyle();
        // 设置样式
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        XSSFCell cell = null;
        // 设置表格标题行
        XSSFRow row = sheet.createRow(0);
        for (short i = 0; i < headers.length; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(style);
            cell.setCellValue(headers[i]);
        }
        
        // 遍历集合数据，设置数据内容
        Iterator<T> it = dataset.iterator();
        while (it.hasNext()) {
            index++;
            row = sheet.createRow(index);
            T t = it.next();
            // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
            List<String> fields = getFields(t);
            for (short i = 0; i < headers.length; i++) {
                cell = row.createCell(i);//创建单元格
                cell.setCellStyle(style);
                String fieldName = fields.get(i);
                
                //拼接get方法名
                String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Method getMethod = t.getClass().getMethod(getMethodName, new Class[] {});//获取方法
                Object value = getMethod.invoke(t, new Object[] {});//调用方法返回值
                // 判断值的类型后进行强制类型转换
                if (value instanceof Date) {
                    Date date = (Date) value;
                    SimpleDateFormat format = new SimpleDateFormat(pattern);
                    if(!StringUtils.isEmpty(value)){
                        cell.setCellValue(format.format(date));
                    }
                } else {
                    // 其它数据类型都当作字符串简单处理
                    if(!StringUtils.isEmpty(value)){
                        cell.setCellValue(value.toString());
                    }
                }
            }
        }
        return workbook;
    }
    
    /** 
     * 利用JAVA的反射机制，导出excel文件(可去除VO中不需要的字段)
	 * @param <T>
     * @param fileName  文件名
     * @param headers 导出的表格属性列名 (key-->属性名,value-->导出第一列的标题)
     * @param dataset  数据内容
     * @param out  文件输出流
     * @param pattern  设定时间类型输出格式。默认为"yyyy-MM-dd" 
     */  
	public static <T> boolean exportExcel(String fileName, Map<String, String> headers, Collection<T> dataset, HttpServletRequest request, HttpServletResponse response , String pattern) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// 声明一个工作薄
		XSSFWorkbook workbook = new XSSFWorkbook();
		// 生成一个表格
		XSSFSheet sheet = workbook.createSheet(fileName);
		// 设置表格默认列宽度为20个字节
		sheet.setDefaultColumnWidth((short) 20);
		// 生成一个样式
		XSSFCellStyle style = workbook.createCellStyle();
		// 设置样式
		style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		XSSFCell cell = null;
		// 设置表格标题行
		XSSFRow row = sheet.createRow(0);
		Set<String> set = headers.keySet();
		int headerIndex=0;
		for (String header : set) {
			cell = row.createCell(headerIndex);
			cell.setCellStyle(style);
			cell.setCellValue(headers.get(header));
			headerIndex ++;
		}
		
		// 遍历集合数据，设置数据内容
		Iterator<T> it = dataset.iterator();
		int index = 0;
		while (it.hasNext()) {
			index++;
			row = sheet.createRow(index);
			T t = it.next();
			// 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
			Field[] fields = t.getClass().getDeclaredFields();
			int i=0;//单元格下标
			for (String header : set) {
				cell = row.createCell(i++);//创建单元格
				cell.setCellStyle(style);
				String fieldName = header;
				//拼接get方法名
				String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				Class tCls = t.getClass();
				Method getMethod = tCls.getMethod(getMethodName, new Class[] {});//获取方法
				Object value = getMethod.invoke(t, new Object[] {});//调用方法返回值
				// 判断值的类型后进行强制类型转换
				if (value instanceof Date) {
					Date date = (Date) value;
					SimpleDateFormat format = new SimpleDateFormat(pattern);
					if(!StringUtils.isEmpty(value)){
						cell.setCellValue(format.format(date));
					}
				} else if (value instanceof DateTime) {
					DateTime dateTime = (DateTime) value;
					Date date = new Date(dateTime.getMillis());
					SimpleDateFormat format = new SimpleDateFormat(pattern);
					if(!StringUtils.isEmpty(value)){
						cell.setCellValue(format.format(date));
					}
				} else {
					// 其它数据类型都当作字符串简单处理
					if(!StringUtils.isEmpty(value)){
						cell.setCellValue(value.toString());
					}
				}
			}
		}
		//写入文件流
		boolean isSuccess = write(request, response , workbook ,fileName,dataset.size());
		return isSuccess;
    }  
    
    /** 
     * 利用JAVA的反射机制，导出excel文件
     * @param <T>
     * @param fileName 文件名
     * @param headers 表格属性列名数组 
     * @param dataset 数据内容
     * @param out 文件输出流
     * @param pattern 设定时间类型输出格式。默认为"yyyy-MM-dd" 
     * @author fuhongxing
     */  
    public static <T> boolean exportExcel(String fileName, String[] headers, Collection<T> dataset, HttpServletRequest request, HttpServletResponse response , String pattern) throws Exception {
        LOGGER.info("=====开始执行excel导出=====");
    	// 声明一个工作薄
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 生成一个表格
        XSSFSheet sheet = workbook.createSheet(fileName);
        // 设置表格默认列宽度为20个字节
        sheet.setDefaultColumnWidth((short) 20);
        // 生成一个样式
        XSSFCellStyle style = workbook.createCellStyle();
        // 设置样式
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        XSSFCell cell = null;
        // 设置表格标题行
        XSSFRow row = sheet.createRow(0);
        for (short i = 0; i < headers.length; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(style);
            cell.setCellValue(headers[i]);
        }

        // 遍历集合数据，设置数据内容
        Iterator<T> it = dataset.iterator();
        int index = 0;
        while (it.hasNext()) {
            index++;
            row = sheet.createRow(index);
            T t = it.next();
            // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
            Field[] fields = t.getClass().getDeclaredFields();
            for (short i = 0; i < headers.length; i++) {
                cell = row.createCell(i);//创建单元格
                cell.setCellStyle(style);
                Field field = fields[i];
                String fieldName = field.getName();
                
                //拼接get方法名
                String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Method getMethod = t.getClass().getMethod(getMethodName, new Class[] {});//获取方法
                Object value = getMethod.invoke(t, new Object[] {});//调用方法返回值
                // 判断值的类型后进行强制类型转换
                if (value instanceof Date) {
                    Date date = (Date) value;
                    SimpleDateFormat format = new SimpleDateFormat(pattern);
                    if(!StringUtils.isEmpty(value)){
                        cell.setCellValue(format.format(date));
                    }
                } else {
                    // 其它数据类型都当作字符串简单处理
                    if(!StringUtils.isEmpty(value)){
                        cell.setCellValue(value.toString());
                    }
                }
            }
        }
        //写入文件流
        boolean isSuccess = write(request, response , workbook ,fileName,dataset.size());
        return isSuccess;
    }  
    
    /**
     * 写入到文件流
     * @param os
     * @param workbook
     * @return
     * @author fuhongxing
     */
    private static boolean write(HttpServletRequest request , HttpServletResponse response , XSSFWorkbook wb, String fileName ,Integer total) {
        boolean isSuccess = false;
        OutputStream outputStream = null;
        //获取客户端浏览器和操作系统信息  
        //Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko --->IE11
        //Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.149 Safari/537.36 --->谷歌
        //Mozilla/5.0 (Windows NT 6.1; WOW64; rv:39.0) Gecko/20100101 Firefox/39.0--->火狐
        String userAgent = request.getHeader("USER-AGENT");
        try {
            String finalFileName = null;
            //fileName = fileName + DateUtils.dateToString(new Date(), "yyyy-MM-dd")+".xlsx";
             //解决文件名乱码
             if(org.apache.commons.lang3.StringUtils.contains(userAgent, "MSIE") || org.apache.commons.lang3.StringUtils.contains(userAgent, "11.0")){//IE浏览器
                    finalFileName = URLEncoder.encode(fileName,"UTF8");
              }else if(org.apache.commons.lang3.StringUtils.contains(userAgent, "Mozilla")){//google,火狐浏览器
                 finalFileName = new String(fileName.getBytes("utf-8"), "ISO8859-1");
              }else{
                  finalFileName = URLEncoder.encode(fileName,"UTF8");//其他浏览器
             }
             //设置让浏览器弹出下载提示框，而不是直接在浏览器中打开
            response.setHeader("Content-Disposition", "attachment;filename="+finalFileName);
            response.setContentType("application/ynd.ms-excel;charset=UTF-8");
            outputStream = response.getOutputStream();
            wb.write(outputStream);
            LOGGER.info("成功导出" + fileName +"--->"+total+"条");
        } catch (Exception e) {
            isSuccess = true;
            LOGGER.error("excel写入文件异常异常", e);
        } finally {
            try {
                if(outputStream != null){
                    outputStream.flush();
                    outputStream.close();
                }
                
                if(wb != null){
                    wb.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }
    
    /**
     * 获取当前对象所有的字段名
     * @param t
     * 
     * @author fuhongxing
     */
    public static <T> List<String> getFields(T t){
        List<String> list = new ArrayList<String>();
        if(t != null){
            Field[] fields = t.getClass().getDeclaredFields();
            for(Field f : fields){
                String fieldName = f.getName();
                if("serialVersionUID".equals(fieldName)){
                    continue;
                }else{
                    list.add(fieldName);
                }
            }
        }
        return list;
  }

    /**
     * 解析Excel文件，将单元格数据按指定数据格式写入Map集合
     * 
     * @param workBook
     * @param excelTemplet
     * @return
     */
    public static List<Map<String, String>> getExcelDataForWM(Workbook workBook,ExcelTemplet excelTemplet) {
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        Sheet sheet = getSheet(workBook, excelTemplet);
        int startRow = excelTemplet.getStartRow();
        /** Excel 索引下标从0开始，进行减一操作 **/
        if ((startRow - 1) < 0) {
            throw new FileException(FileEnum.FILE_ERROR_PARSE, "startRow参数有误,原值为[" + startRow + "]");
        }
        startRow--;
        try {
            /** 单元格数据对照Map **/
            Map<String, String> columnMap = excelTemplet.getColumnMap();
            for (int j = startRow; j <= sheet.getLastRowNum(); j++) {
                Row row = sheet.getRow(j);
                if (row == null) {
                    continue;
                }
                result.add(convertDataToMap(row, columnMap));
            }
        } catch (Exception e) {
            LOGGER.error("解析Excel文件，将单元格数据按指定数据格式写入Map集合异常", e);
            throw new FileException(FileEnum.FILE_ERROR_PARSE, e.getMessage());
        }
        return result;
    }
    //
    public static String getCellValue(Cell cell) {
		String value = null;
		if(cell != null){
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_BLANK:
			//	value = "";
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				value = String.valueOf(cell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_ERROR:
				value = "Error Code:"+String.valueOf(cell.getErrorCellValue());
				break;
			case Cell.CELL_TYPE_FORMULA:
				Workbook wb = cell.getSheet().getWorkbook();
				CreationHelper crateHelper = wb.getCreationHelper();
				FormulaEvaluator evaluator = crateHelper.createFormulaEvaluator();
				value =  getCellValue(evaluator.evaluateInCell(cell)).toString();
			case Cell.CELL_TYPE_NUMERIC:
				if (DateUtil.isCellDateFormatted(cell)) {
					Date theDate = cell.getDateCellValue();
					value = DateFormatUtils.format(theDate, "yyyy-MM-dd");
				} else {
					value = NumberToTextConverter.toText(cell.getNumericCellValue());
				}
				break;
			case Cell.CELL_TYPE_STRING:
				value = cell.getRichStringCellValue().getString();
				break;
			default:
				value = null;
			}
		}

		if(value != null){
		    value = value.trim();
        }
		return value; 
	}
    
}
