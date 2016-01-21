package de.us.rpi.jMeasurement.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import de.us.rpi.jMeasurement.job.DatabaseCon;

/**
 * A set of function to handle files
 * 
 * @author us
 *
 */
public class FileTool {

	/**
	 * Read a property file from filesystem
	 * 
	 * @param path Path to the property file
	 * 
	 * @return Property-Object
	 */
	public static Properties readProperty(File path) {
		Properties prop = new Properties();
		InputStream input = null;
		
		try {
			input = new FileInputStream(path);
			prop.load(input);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				try { input.close(); } catch (Exception e) { e.printStackTrace(); }
			}
		}
		return prop;
	}
	
	/**
	 * Simple Testcall
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Properties p = FileTool.readProperty(new File("resource/"+DatabaseCon.PROP_FILE));
		System.out.println("url ="+p.getProperty("mysql.url"));
		System.out.println("user="+p.getProperty("mysql.user"));
		System.out.println("pwd ="+p.getProperty("mysql.password"));
	}
}
