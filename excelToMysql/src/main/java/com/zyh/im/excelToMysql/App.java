package com.zyh.im.excelToMysql;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.zyh.im.excelToMysql.util.baseSqlOperator;

/**
 * Hello world!
 *
 */
public class App 
{
	private static Logger logger = Logger.getLogger(App.class); 
	private final static String table_name_prefix = "im_excel_";
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        
        logger.info("begin"); 
        User user = (User) baseSqlOperator.getObject("getUser", 1);
        System.out.println(user);
        
        List<User> users = baseSqlOperator.getList("getUsers");
        System.out.println(users);
        List<Map<String, Object>> newSheetList = new ArrayList<Map<String,Object>>();
        try {
        	
        	InputStream is =App.class.getClassLoader().getResourceAsStream("test.xlsx");
			//POIFSFileSystem fs=new POIFSFileSystem(is);
			
			//XSSFWorkbook
			//HSSFWorkbook wb = new HSSFWorkbook(fs); 
        	List result = baseSqlOperator.getList("getTableRel");
        	Set<String> sheetName = new HashSet<String>();
        	for(Object item :result) {
        		HashMap<String,Object> line = (HashMap<String,Object>)item;
        		sheetName.add((String)line.get("sheet_name"));       		
        	}
			XSSFWorkbook wb  = new XSSFWorkbook(is);
			
			for(int i=0;i<wb.getNumberOfSheets();i++) {
				XSSFSheet sheet = wb.getSheetAt(i);
				

				ExcelSheetObject check =checkSheetStructure.checkSheet(sheet);
				
				if(sheetName.contains(sheet.getSheetName()))continue;
				HashMap<String, Object> rel = new HashMap<String,Object>();
				rel.put("sheet_name",sheet.getSheetName());
				rel.put("table_name", table_name_prefix+String.valueOf(i));
				rel.put("table_desc",sheet.getSheetName()+String.valueOf(i));
				
				newSheetList.add(rel);
				//new HashMap<String,Object>(){"1",""};
				sheetName.add(sheet.getSheetName());
				System.out.println(sheet.getSheetName());
				

				int lostIndex = sheet.getLastRowNum();
				//记录行数，列数；对行列进行分析

				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        if(newSheetList==null||newSheetList.size()==0)return;
        int count = baseSqlOperator.insert("insert", newSheetList);
        
        baseSqlOperator.commit();
        
    }
    
    public Map<String,String> createTable(String tableName,List<String> columns){
    	
	    	Map<String, String> map = new HashMap<String, String>();
	    	StringBuilder sql = new StringBuilder();
	    	for(int i=0 ;i < columns.size();i++) {
	    		sql.append("(");
	    		
	    	}
	    	return null;
    }
    

    public Map<String, String> createTable(Object obj) {
        try {
            Class<?> clazz = obj.getClass();
            Field[] f = clazz.getDeclaredFields();
            String tableName = clazz.getName();
            Map<String, String> map = new HashMap<String, String>();
            String sql = "";
            for (int i = 0; i < f.length; i++) {
                Field field = f[i];
                String paramType = setParamterType(field);
                String param = field.getName();
                if (param.equals("id")) {
                    sql += "(" + param + " " + paramType + " PRIMARY KEY NOT NULL,";// 主键";
                } else {
                    sql += param + " " + paramType + ",";
                }
            }
            sql = sql.substring(0, sql.length() - 1);
            sql += ")";
            map.put("name", tableName);
            map.put("fields", sql);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private static String setParamterType(Field f) throws Exception {
        if (("int").equals(f.getType().getCanonicalName())) {
            return "int(11)";
        } else if (("long").equals(f.getType().getCanonicalName())
                || ("java.lang.Long").equals(f.getType().getCanonicalName())) {
            return "int(11)";
        } else if (("float").equals(f.getType().getCanonicalName())) {
            return "float(10)";
        } else if (("float[]").equals(f.getType().getCanonicalName())) {
            return "varchar(255)";
        } else if (("java.lang.String").equals(f.getType().getCanonicalName())) {
            return "varchar(255)";
        } else if (("java.lang.Long[]").equals(f.getType().getCanonicalName())) {
            return "varchar(255)";
        } else if (("int[]").equals(f.getType().getCanonicalName())) {
            return "varchar(255)";
        }
        return null;
    }
    

    public void createTable(Map<String, String> map) {
    	
    }
}
