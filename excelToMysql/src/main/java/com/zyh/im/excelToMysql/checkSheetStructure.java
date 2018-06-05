package com.zyh.im.excelToMysql;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.zyh.im.excelToMysql.ExcelSheetObject.ColumnField;
import com.zyh.im.excelToMysql.ExcelSheetObject.Coordinate;

public class checkSheetStructure {

	private XSSFSheet sheet;
	public checkSheetStructure(XSSFSheet sheet) {
		this.sheet = sheet;
	}
	public static ExcelSheetObject checkSheet(XSSFSheet sheet) {
		
		ExcelSheetObject checkResult = new ExcelSheetObject();
		checkResult.setSheet(sheet);
		//维度;行坐标为key，非空单元为value
		LinkedHashMap<Integer,Integer> columnNumOfEveryRow = new LinkedHashMap<Integer,Integer>();
		//维度;列坐标key，非空单元为value
		LinkedHashMap<Integer,Integer> rowNumOfEveryColumn = new LinkedHashMap<Integer,Integer>();
		int rowNum = sheet.getLastRowNum()+1;
		for(int i=1;i<=rowNum;i++) {
			
			XSSFRow row = sheet.getRow(i-1);
			if(row == null) {rowNum--;continue;}
		    
		    Iterator col = row.cellIterator();
		    int threshold = 6;
		    int blackCellNum=0;
/*		    while(col.hasNext()&&threshold>0) {
		    		XSSFCell xcell = (XSSFCell) col.next();
		    		if("".equals(getCellValue(xcell)))
		    			blackCellNum++;
		    }*/
		    int cellNum = row.getLastCellNum()+1;
		    for(int j=1;j<=cellNum;j++){
		    	XSSFCell xcell = row.getCell(j-1);

	    		if(xcell==null||"".equals(getCellValue(xcell)))
	    		{	blackCellNum++;
		    		if(!rowNumOfEveryColumn.containsKey(j))
		    			rowNumOfEveryColumn.put(j, 0);
	    		}
	    		else{
	    			int cur = rowNumOfEveryColumn.get(j)==null?0:rowNumOfEveryColumn.get(j);
	    			rowNumOfEveryColumn.put(j, cur+1);
	    		}
		    }
		    
		    columnNumOfEveryRow.put(i, (cellNum-blackCellNum));
		}

		checkResult.setColumnNumOfEveryRow(columnNumOfEveryRow);
		checkResult.setRowNumOfEveryColumn(rowNumOfEveryColumn);
		distinctRowColumnNum(checkResult);
		findLeftTopAndRightBottom(checkResult);
		AnalysisColumnField(checkResult);
		return checkResult;
		
	}
	
	public static void AnalysisColumnField(ExcelSheetObject checkResult){
		Coordinate colHeader = checkResult.getLeftTop();
		int colHeaderLth = checkResult.getColumnNum();
		XSSFRow row = checkResult.getSheet().getRow(colHeader.Y-1);
		XSSFRow afterRow = checkResult.getSheet().getRow(colHeader.Y);
		List<ColumnField> colList = new ArrayList<ColumnField>(colHeaderLth);
		for(int i=colHeader.X-1;i<colHeaderLth;i++){
			XSSFCell cell = row.getCell(i);
			ColumnField cfield = new ColumnField();
			cfield.setColumnName(cell.getStringCellValue());
			cell = afterRow.getCell(i);
			cfield.setColumnType(getCellType(cell));
			colList.add(cfield);
		}
		checkResult.setColumnFileds(colList);
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
	
    public static  Object getCellValue(XSSFCell cell) {
    	String value="" ;

    switch (cell.getCellTypeEnum()) {
    case NUMERIC: // 数字
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
    case STRING: // 字符串
        value = cell.getStringCellValue();
        break;
    case BOOLEAN: // Boolean
        value = cell.getBooleanCellValue() + "";
        break;
    case FORMULA: // 公式
        value = cell.getCellFormula() + "";
        break;
    case BLANK: // 空值
        value = "";
        break;
    case ERROR: // 故障
        value = "非法字符";
        break;
    default:
        value = "未知类型";
        break;
}
	return value;
    }

/*	private static String getCellFormatValue(XSSFCell cell) 
    { String cellvalue = ""; if (cell != null) 
     // 判断当前Cell的Type 
    	switch (cell.getCellType()) 
    	{ // 如果当前Cell的Type为NUMERIC 
    	case XSSFCell.CELL_TYPE_NUMERIC: 
    		case XSSFCell.CELL_TYPE_FORMULA: 
    		{ // 判断当前的cell是否为Date 
    			if (HSSFDateUtil.isCellDateFormatted(cell)) 
    			{ // 如果是Date类型则，取得该Cell的Date值 Date 
    				date = cell.getDateCellValue(); // 把Date转换成本地格式的字符串
    				cellvalue = cell.getDateCellValue().toLocaleString();
    			} // 如果是纯数字
    			else 
    			{ // 取得当前Cell的数值 
    				double num = new Double((double)cell.getNumericCellValue()); 
    				cellvalue = String.valueOf(myformat.format(num)); 
    				}
    			break; 
    		} 
    		// 如果当前Cell的Type为STRIN 
    		case XSSFCell.CELL_TYPE_STRING: // 取得当前的Cell字符串
    			cellvalue = cell.getStringCellValue().replaceAll("'", "''"); break; 
    			// 默认的Cell值 
    			default: cellvalue = " "; } }else

	{
		cellvalue = "";
	} return cellvalue;

	}}}}}*/

    public static String getCellType(XSSFCell cell) {
    	String value="";
    	switch (cell.getCellTypeEnum()) {
    		case NUMERIC: // 数字
	        //如果为时间格式的内容
	        if (HSSFDateUtil.isCellDateFormatted(cell)) {//isCellDateFormatted(cell)) {      
	        	value = "datetime DEFAULT NULL";
             break;
	        } else {
	        	double number = cell.getNumericCellValue();
	        	double eps = 1e-10;  // 精度范围
	        	if (number - (double)((int)number) < eps)
	        		value = "decimal(8,0) DEFAULT NULL";
	        	else
	        		value = "decimal(8,4) DEFAULT NULL";
	        }
        break;
    case STRING: // 字符串
        value = cell.getStringCellValue();
        value = "varchar(30) DEFAULT NULL";
        break;
    case BOOLEAN: // Boolean
        value = cell.getBooleanCellValue() + "";
        value = "tinyint(4) DEFAULT NULL";
        break;
    case FORMULA: // 公式
        value = cell.getCellFormula() + "";
        value = "";
        break;
    case BLANK: // 空值
        value = "";
        break;
    case ERROR: // 故障
        value = "非法字符";
        break;
    default:
        value = "未知类型";
        break;
}
	return value;
    }
    //找到有效区域内最可能的行数
    public static void distinctRowColumnNum(ExcelSheetObject checkResult){
    	
    	TreeMap<Integer,Integer> columnNum = new TreeMap<Integer,Integer>();
    	int maxColumnCount = 0;int sumColumnCount = checkResult.getColumnNumOfEveryRow().size();
    	for(Entry<Integer, Integer> kv :checkResult.getColumnNumOfEveryRow().entrySet()){
    		Integer count = columnNum.get(kv.getValue())==null?0:columnNum.get(kv.getValue());
    		columnNum.put(kv.getValue(),count+1);
    		maxColumnCount = (count+1)> maxColumnCount ?(count+1):maxColumnCount;
    	}
    	int columnCount = 0;
    	for(Entry<Integer, Integer> kv :columnNum.entrySet()){
    		if(kv.getValue()!=maxColumnCount)continue;
    		columnCount = kv.getKey()>columnCount?kv.getKey():columnCount;
    	}
    	
    	checkResult.setColumnNum(columnCount);
    	
/*    	TreeMap<Integer,Integer> percentRate = new TreeMap<Integer,Integer>();
    	for(Entry<Integer, Integer> kv :columnNum.entrySet()){
    		percentRate.put(kv.getKey(), kv.getValue()/sumColumnCount);
    		double rate = kv.getValue()/sumColumnCount;
    		columnCount = kv.getKey()>columnCount?kv.getKey():columnCount;
    	}*/
    	
    	int maxRowCount = 0;
    	
    	TreeMap<Integer,Integer> rowNum = new TreeMap<Integer,Integer>();
    	for(Entry<Integer, Integer> kv :checkResult.getRowNumOfEveryColumn().entrySet()){
    		Integer count = rowNum.get(kv.getValue())==null?0:rowNum.get(kv.getValue());
    		rowNum.put(kv.getValue(),count+1);
    		maxRowCount = (kv.getValue()>2&&((count+1))> maxRowCount)?(count+1):maxRowCount;
    	}
    	//存在行数过小
    	//记录优选列表
    	

    	ArrayList<Integer> bestList = new ArrayList<Integer>(); 
    	
    	int rowCount = 0;
    	for(Entry<Integer, Integer> kv :rowNum.entrySet()){
    		if(kv.getValue()!=maxRowCount)continue;
    		rowCount = kv.getKey()>rowCount?kv.getKey():rowCount;
    	}
    	
    	checkResult.setRowNum(rowCount);
    }
    
    
    public static boolean isVaildCount(HashMap<Integer,Integer> counts){
    	
    	
    	
    	return false;
    }
    public static void findLeftTopAndRightBottom(ExcelSheetObject checkResult){
    	
    	
    	LinkedHashMap<Integer,Integer> columnNum = new LinkedHashMap<Integer,Integer>();
    	int leftx=0,rightx=0;
    	int lefty=0,righty=0;
    	boolean series = false;
    	int tempy = 0,tempx=0;
    	
    	Iterator<Entry<Integer, Integer>>  iterator =checkResult.getColumnNumOfEveryRow().entrySet().iterator();
    	Entry<Integer, Integer> before = null;
    	while(iterator.hasNext()){
    		Entry<Integer, Integer> kv = iterator.next();

    		if(kv.getValue()==checkResult.getColumnNum())
			{
				tempy = tempy!=0?tempy:kv.getKey();//一旦第一次取到lefty，就不再获取
				lefty = tempy>lefty?tempy:lefty;
				righty = tempy>righty?tempy:kv.getKey();
				
    			if(before!=null && before.getValue()>kv.getValue())
    				{tempy--;lefty--;}
    				
			}
		else if(kv.getValue()!=checkResult.getColumnNum())
			{
				righty = tempy>righty?tempy:kv.getKey();
				//tempy =tempy!=0?0:tempy;
			}
    		before = kv;
    	}
    	
    	for(Entry<Integer, Integer> kv :checkResult.getColumnNumOfEveryRow().entrySet()){
    		if(kv.getValue()==checkResult.getColumnNum())
    			{
    			tempy = tempy!=0?tempy:kv.getKey();//一旦第一次取到lefty，就不再获取
    			lefty = tempy>lefty?tempy:lefty;
    			righty = tempy>righty?tempy:kv.getKey();
    			}
    		else if(kv.getValue()!=checkResult.getColumnNum())
    			{
					righty = tempy>righty?tempy:kv.getKey();
    				//tempy =tempy!=0?0:tempy;
    			}
    	}
    	
    	
    	for(Entry<Integer, Integer> kv :checkResult.getRowNumOfEveryColumn().entrySet()){
    		if(kv.getValue()>=checkResult.getRowNum())
    			{
    			tempx = tempx!=0?tempx:kv.getKey();
    			leftx = tempx>leftx?tempx:leftx;
    			rightx = (tempx>rightx)?tempx:((rightx-leftx+1)<checkResult.getColumnNum()?kv.getKey():rightx);
    			}
    		else if(kv.getValue()!=checkResult.getRowNum())
    			{
    			
    				rightx = (tempx>rightx)?tempx:((rightx-leftx+1)<checkResult.getColumnNum()?kv.getKey():rightx);
    				//tempx =tempx!=0?0:tempx;
    			}
    	}
    	
    	checkResult.setLeftTop(new Coordinate(leftx,lefty));
    	checkResult.setRightBottom(new Coordinate(rightx,righty));
    	
    }
}
