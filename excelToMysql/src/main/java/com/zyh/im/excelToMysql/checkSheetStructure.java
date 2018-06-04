package com.zyh.im.excelToMysql;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.plaf.synth.Region;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.charts.XSSFChartDataFactory;

public class checkSheetStructure {

	private XSSFSheet sheet;
	public checkSheetStructure(XSSFSheet sheet) {
		this.sheet = sheet;
	}
	public static ExcelSheetObject checkSheet(XSSFSheet sheet) {
		
		ExcelSheetObject checkResult = new ExcelSheetObject();
		
		Map<Integer,Integer> columns = new HashMap<Integer,Integer>();
		int rowNum = sheet.getLastRowNum();
		for(int i=1;i<=rowNum;i++) {
			checkResult.setRowNum(rowNum);
			XSSFRow row = sheet.getRow(i);
			if(row == null) {rowNum--;continue;}
		    columns.put(i, (int) row.getLastCellNum());
		    Iterator col = row.cellIterator();
		    while(col.hasNext()) {
		    		XSSFCell xcell = (XSSFCell) col.next();
		
		    }
		    
		}

		checkResult.setRowNum(sheet.getLastRowNum());
		
		return checkResult;
		
	}
	
	
	public boolean hasMerged() {
        return sheet.getNumMergedRegions() > 0 ? true : false;
    }
	
	// 判断指定区域内是否含有合并单元格
    public boolean hasMerged(CellRangeAddress region) {
        for (int row = region.getFirstRow(); row < region.getLastRow(); row++) {
            for (int col = region.getFirstColumn(); col < region.getLastColumn(); col++){
                for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
                    CellRangeAddress r = sheet.getMergedRegion(i);
                    if (r.isInRange(row, col)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
	
    public  Object getCellValue(XSSFCell cell) {
    	String value="" ;
    switch (cell.getCellType()) {
    case XSSFCell.CELL_TYPE_NUMERIC: // 数字
        //如果为时间格式的内容
        if (cell.isPartOfArrayFormulaGroup()) {//isCellDateFormatted(cell)) {      
           //注：format格式 yyyy-MM-dd hh:mm:ss 中小时为12小时制，若要24小时制，则把小h变为H即可，yyyy-MM-dd HH:mm:ss
           SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
           value=sdf.format(HSSFDateUtil.getJavaDate(cell.
           getNumericCellValue())).toString();                                 
             break;
         } else {
             value = new DecimalFormat("0").format(cell.getNumericCellValue());
         }
        break;
    case HSSFCell.CELL_TYPE_STRING: // 字符串
        value = cell.getStringCellValue();
        break;
    case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
        value = cell.getBooleanCellValue() + "";
        break;
    case HSSFCell.CELL_TYPE_FORMULA: // 公式
        value = cell.getCellFormula() + "";
        break;
    case HSSFCell.CELL_TYPE_BLANK: // 空值
        value = "";
        break;
    case HSSFCell.CELL_TYPE_ERROR: // 故障
        value = "非法字符";
        break;
    default:
        value = "未知类型";
        break;
}
	return value;
    }
}
