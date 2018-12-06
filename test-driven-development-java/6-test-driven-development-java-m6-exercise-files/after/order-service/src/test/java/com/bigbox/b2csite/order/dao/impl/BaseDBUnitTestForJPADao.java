package com.bigbox.b2csite.order.dao.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.dbunit.IDatabaseTester;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.ibatis.common.jdbc.ScriptRunner;

public class BaseDBUnitTestForJPADao {

	protected static EntityManagerFactory EMF = null;
	protected static IDatabaseConnection CONN = null;
	
	protected EntityManager entityManager = null;
	static IDatabaseTester databaseTester=null;;
	
	private static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(
				"jdbc:mysql://localhost/sample", "root", "root");
	}
	
	@BeforeClass
	public static void setupTestClass() throws Exception {
		
		
		// Initialize object for ScripRunner
					ScriptRunner sr = new ScriptRunner(getConnection(), false, false);

					// Give the input file to Reader
					Reader reader = new BufferedReader(
		                               new FileReader("tabledef/b2csite.ddl.sql"));

					// Exctute script
					sr.runScript(reader);
		
	/*	Properties dbProps = new Properties();
		dbProps.put("user", DBInfo.USER);
		dbProps.put("password", DBInfo.PASSWORD);
		
		Connection jdbcConn = Driver.load().connect(DBInfo.URL, dbProps);
		CONN = new DatabaseConnection(jdbcConn);
		RunScript.execute(CONN.getConnection(), new FileReader("tabledef/b2csite.ddl.sql"));*/
		
		/*databaseTester = new JdbcDatabaseTester("com.mysql.jdbc.Driver","jdbc:mysql://localhost/sample","root", "root");
		IDataSet dataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("customer- init.xml"));
		databaseTester.setDataSet(dataSet);
		databaseTester.onSetup();    */
		CONN = new DatabaseConnection(getConnection());
		final Map<Object, Object> props = new HashMap<>();
		props.put("javax.persistence.jdbc.url", DBInfo.URL);
		//props.put("hibernate.hbm2ddl.auto", "create-drop");
		EMF = Persistence
				.createEntityManagerFactory("orderPersistenceUnit", props);
		
		
	}
	
	@AfterClass
	public static void teardownTestClass() throws Exception {
		
		try {
			if (EMF != null) {
				if (EMF.isOpen()) {
					EMF.close();
				}
				EMF = null;
			}
		}
		finally {
			if (CONN != null) {
				CONN.close();
				CONN = null;
			}
		}
	}
	
	@Before
	public void baseSetup() throws Exception {
		entityManager = EMF.createEntityManager();
	}
	
	@After
	public void baseTeardown() throws Exception {
		if (entityManager != null) {
			if (entityManager.isOpen()) {
				entityManager.close();
			}
			entityManager = null;
		}
	}
}
