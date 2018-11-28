package com.yuminsoft.ams.system.excel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Excel导入数据格式定义
 * @author fuhongxing
 */
public class ExcelTemplet {
    
    /** 正文数据从某行读取 **/
    private int startRow;
    /** sheet 名称 **/
    private String sheetName;
    /** 单元格数据对照Map **/
    private Map<String,String> columnMap = new HashMap<String,String>();
    /** 导入数据处理的结果写入的单元格字母索引 如（A,B,C....） **/
    private String feedBackColumnName;
    /** 导入数据处理的结果 保存Map的Key **/
    public static final String FEED_BACK_MSG = "feedBackMsg";
    /**需要追加的列*/
    private List<String> resultCell;
    
    public int getStartRow() {
        return startRow;
    }

    public String getSheetName() {
        return sheetName;
    }

    public Map<String, String> getColumnMap() {
        return columnMap;
    }
    
    public String getFeedBackColumnName() {
        return feedBackColumnName;
    }
    
    public List<String> getResultCell() {
		return resultCell;
	}

	public void setResultCell(List<String> resultCell) {
		this.resultCell = resultCell;
	}

	
    /** 征信数据导入导出**/
    public class CreditInputExcel extends ExcelTemplet {
        public CreditInputExcel() {
            sheetName = "";
            startRow = 2;
            columnMap.put("A", "contract");
            columnMap.put("B", "appNo");
            columnMap.put("C", "name");
            columnMap.put("D", "idCard");
            
            feedBackColumnName = "E";
        }
    }
    
    /**质检数据导入*/
    public class QualityInputExcel extends ExcelTemplet {
        public QualityInputExcel() {
        	super.sheetName = "";
            super.startRow = 2;
            super.columnMap.put("A", "applyNo");
            super.columnMap.put("B", "customerName");
            super.columnMap.put("C", "idCard");
            super.columnMap.put("D", "applySource");
            
            super.feedBackColumnName = "E";
        }
    }
    
    /**质检数据导入删除*/
    public class QualityDeleteExcel extends ExcelTemplet {
        public QualityDeleteExcel() {
        	super.sheetName = "";
            super.startRow = 2;
            super.columnMap.put("A", "applyNo");
            super.columnMap.put("B", "customerName");
            super.columnMap.put("C", "idCard");
            
            super.feedBackColumnName = "E";
        }
    }
}
