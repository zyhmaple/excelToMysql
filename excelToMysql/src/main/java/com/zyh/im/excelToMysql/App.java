package com.zyh.im.excelToMysql;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
				if(sheetName.contains(sheet.getSheetName()))continue;
				HashMap<String, Object> rel = new HashMap<String,Object>();
				rel.put("sheet_name",sheet.getSheetName());
				rel.put("table_name", table_name_prefix+String.valueOf(i));
				rel.put("table_desc",sheet.getSheetName()+String.valueOf(i));
				
				newSheetList.add(rel);
				//new HashMap<String,Object>(){"1",""};
				sheetName.add(sheet.getSheetName());
				System.out.println(sheet.getSheetName());
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
}
