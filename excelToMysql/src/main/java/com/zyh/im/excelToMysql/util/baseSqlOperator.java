package com.zyh.im.excelToMysql.util;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.parsing.PropertyParser;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.zyh.im.excelToMysql.App;
import com.zyh.im.excelToMysql.User;

public enum baseSqlOperator {

	;

	public static SqlSession session = null;
	public static final String statement_prefix = "com.zyh.im.ExcelToMysqlMapper.";

	static {

		String resource = "conf.xml";
		InputStream is = baseSqlOperator.class.getClassLoader().getResourceAsStream(resource);
		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
		session = sessionFactory.openSession();

	}

	public static void commit() {
		session.commit();
	}

	public static void colse() {
		session.close();
	}

	public static Object getObject(String statement, Object params) {

		return session.selectOne(statement_prefix + statement, params);
	}

	public static List getList(String statement, Object params) {

		return session.selectList(statement_prefix + statement, params);
	}

	public static List getList(String statement) {

		return session.selectList(statement_prefix + statement);
	}

	public static int insert(String statement, Object objects) {
		if (objects == null)
			return 0;
		return session.insert(statement_prefix + statement, objects);

	}

	public static int update(String statement, Object params) {
		return session.update(statement_prefix + statement, params);
	}

	public static int delete(String statement, Object params) {
		return session.delete(statement_prefix + statement, params);
	}

}
