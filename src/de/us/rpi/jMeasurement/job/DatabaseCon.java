package de.us.rpi.jMeasurement.job;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseCon {
	private Connection connection = null;
	
	public DatabaseCon() throws Exception {
		 Class.forName("com.mysql.jdbc.Driver").newInstance(); 
		 connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/measurement?" +
				 						"user=root&password=jacla42$");
	}

	public Connection getConnection() {
		return connection;
	}

}
