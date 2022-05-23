package com.jesusmoro.vcm.services;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.jesusmoro.vcm.services.exceptions.DatabaseException;

public class DB {

	private static Connection conn = null;
	
	public static Connection getConnection() {
		if (conn == null) {
			try {
				Properties props = loardProperties();
				String url = props.getProperty("spring.dataslource.ur");
				conn = DriverManager.getConnection(url, props);
			}
			catch (SQLException e) {
				throw new DatabaseException(e.getMessage());
			}
		}
		return conn;
	}
	
	public static void closeConnection() {
		if (conn != null) {
			try {
				conn.close();
			}
			catch (SQLException e) {
				throw new DatabaseException(e.getMessage());
			}
		}
	}
	
	private static Properties loardProperties() {
		try (FileInputStream fs = new FileInputStream("application-dev.properties")){
			Properties props = new Properties();
			props.load(fs);
			return props;
		}
		catch (IOException e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	
}
