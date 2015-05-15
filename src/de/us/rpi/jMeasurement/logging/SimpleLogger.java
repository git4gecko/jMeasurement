package de.us.rpi.jMeasurement.logging;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;

import de.us.rpi.jMeasurement.bmp085.BMP085Device;
import de.us.rpi.jMeasurement.bmp085.BMP085Result;

public class SimpleLogger extends TimerTask {
	
	BMP085Device bmp085 = null;
	
	private void init() throws Exception {
		bmp085 = new BMP085Device(0);
	}
	
	private Connection createConnection() {
		Connection con = null;
		try {
			/*
			 *  String url = "jdbc:mysql://" + dbHost + "/" + database;
            conn = DriverManager.getConnection(url, dbUser, dbPassword)
			 */
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost/measurement", "root", "root");
		} catch (Exception e) {
			e.printStackTrace();
			con = null;
		}
		return con;
	}

	// CREATE TABLE bmp085 ( key_sensor INT, pressure NUMERIC, altitude NUMERIC, temperature NUMERIC, time timestamp); 
	@Override
	public void run() {
		Connection con = null;
		 PreparedStatement stmnt = null;
		try {
			BMP085Result data = bmp085.process();
			System.out.println(String.format("temperature= %5.3f° Celsius, pressure= %6.2f hpa", 
					data.getTemperature(), (data.getPressure() * 0.01)));
			con = createConnection();
			if (con != null) {
				//  key_sensor SMALLINT, pressure DOUBLE, altitude DOUBLE, temperature DOUBLE, time DATETIME
				stmnt = con.prepareStatement("insert into bmp085" +
								"(key_sensor, pressure, temperature, time) "+
								"values(?, ?, ?, ?)");
				stmnt.setInt(1, 1);
				stmnt.setDouble(2, data.getTemperature());
				stmnt.setDouble(3, data.getPressure());
				stmnt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
				stmnt.execute();
				stmnt.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null)
				try { con.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		
	}
	
	public static void main(String arg[]) {
		try {
			SimpleLogger l = new SimpleLogger();
			l.init();
			Timer timer = new Timer();

		    // Start in einer Sekunde dann Ablauf alle 30 Sekunden
		    timer.schedule(l, 1000, (60 * 5 * 1000) );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}