package util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCHelper {
	private String url;
	private String username;
	private String password;

	// Load MySql
	public JDBCHelper() {
		InputStream inputStream = this.getClass().getClassLoader()
				.getResourceAsStream("dbConfig.properties");
		Properties p = new Properties();
		try {
			p.load(inputStream);
			url = p.getProperty("DatabaseConnectionAddress") + p.getProperty("DatabaseSchemaName");
			username = p.getProperty("DatabaseConnectionUsername");
			password = p.getProperty("DatabaseConnectionPassword");
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	// Get Connection
	public Connection getConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	public void close(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
