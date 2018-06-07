package com.zyh.im.excelToMysql;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;

public class ExcelSheetObject {

	
	private int rowNum;
	
	private int columnNum;
	
	private LinkedHashMap<Integer,Integer> columnNumOfEveryRow;
	
	private LinkedHashMap<Integer,Integer> RowNumOfEveryColumn;
	
	private Coordinate leftTop;
	
	private Coordinate rightBottom;
	
	private List<ColumnField> columnFileds;
	
	private List<String> columnValues;
	
	private XSSFSheet sheet;
	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}



	public Coordinate getLeftTop() {
		return leftTop;
	}

	public void setLeftTop(Coordinate leftTop) {
		this.leftTop = leftTop;
	}

	public Coordinate getRightBottom() {
		return rightBottom;
	}

	public void setRightBottom(Coordinate rightBottom) {
		this.rightBottom = rightBottom;
	}


	
	public int getColumnNum() {
		return columnNum;
	}

	public void setColumnNum(int columnNum) {
		this.columnNum = columnNum;
	}

	public LinkedHashMap<Integer, Integer> getColumnNumOfEveryRow() {
		return columnNumOfEveryRow;
	}

	public void setColumnNumOfEveryRow(LinkedHashMap<Integer, Integer> columnNumOfEveryRow) {
		this.columnNumOfEveryRow = columnNumOfEveryRow;
	}

	public LinkedHashMap<Integer, Integer> getRowNumOfEveryColumn() {
		return RowNumOfEveryColumn;
	}

	public void setRowNumOfEveryColumn(LinkedHashMap<Integer, Integer> rowNumOfEveryColumn) {
		RowNumOfEveryColumn = rowNumOfEveryColumn;
	}



	public List<ColumnField> getColumnFileds() {
		return columnFileds;
	}

	public void setColumnFileds(List<ColumnField> columnFileds) {
		this.columnFileds = columnFileds;
	}

	public XSSFSheet getSheet() {
		return sheet;
	}

	public void setSheet(XSSFSheet sheet) {
		this.sheet = sheet;
	}



	public List<String> getColumnValues() {
		return columnValues;
	}

	public void setColumnValues(List<String> columnValues) {
		this.columnValues = columnValues;
	}



	/*
	 *  坐标
	 */
	static class Coordinate{
		int X;
		int Y;
		public Coordinate(int x, int y) {
			this.X = x;
			this.Y = y;
		}
		public int getX() {
			return X;
		}
		public void setX(int x) {
			X = x;
		}
		public int getY() {
			return Y;
		}
		public void setY(int y) {
			Y = y;
		}
	}

	static class ColumnField{
		String columnName;
		String columnType;
		public String getColumnName() {
			return columnName;
		}
		public void setColumnName(String columnName) {
			this.columnName = columnName;
		}
		public String getColumnType() {
			return columnType;
		}
		public void setColumnType(String columnType) {
			this.columnType = columnType;
		}
	}


	
}
