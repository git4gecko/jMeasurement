package de.us.rpi.jMeasurement.job;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import de.us.rpi.jMeasurement.tools.FileTool;

/**
 * Simple database wrapper
 * 
 * The database parameter are stored in <code>PROP_FILE</code>
 * 
 * @author us	
 *
 */
public class DatabaseCon {
	public final static String PROP_FILE = "database.properties"; 
	private Connection connection = null;
	
	/**
	 * Initialize a database connection
	 * 
	 * @throws Exception
	 */
	public DatabaseCon() throws Exception {
		init();
	}

	/**
	 * Gets the connection
	 * 
	 * @return database connection
	 */
	public Connection getConnection() {
		return connection;
	}
	
	/**
	 * Creates a database connection
	 * 
	 * @throws Exception
	 */
	private void init() throws Exception {
		Properties p = FileTool.readProperty(new File(PROP_FILE));
		String conStr = p.getProperty("mysql.url")+
							"?user="+p.getProperty("mysql.user")+
							"&password="+p.getProperty("mysql.password");

		Class.forName("com.mysql.jdbc.Driver").newInstance(); 
		 connection = DriverManager.getConnection(conStr);
	}
}
