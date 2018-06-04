package com.zyh.im.excelToMysql;

import java.util.HashMap;

public class ExcelSheetObject {

	
	public int rowNum;
	
	public HashMap<Integer,Integer> columnNumOfRow;
	
	public Coordinate leftTop;
	
	public Coordinate rightBottom;
	
	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	public HashMap<Integer, Integer> getColumnNumOfRow() {
		return columnNumOfRow;
	}

	public void setColumnNumOfRow(HashMap<Integer, Integer> columnNumOfRow) {
		this.columnNumOfRow = columnNumOfRow;
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


	
	class Coordinate{
		int X;
		int Y;
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
	
}
