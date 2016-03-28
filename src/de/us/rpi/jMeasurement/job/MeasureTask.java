package de.us.rpi.jMeasurement.job;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.TimerTask;

import de.us.rpi.jMeasurement.bmp085.BMP085Device;
import de.us.rpi.jMeasurement.bmp085.BMP085Result;
import de.us.rpi.jMeasurement.oneWire.OneWire;
import de.us.rpi.jMeasurement.oneWire.OneWireDevice;
import de.us.rpi.jMeasurement.ubidots.Ubidots;

public class MeasureTask extends TimerTask {
	private final static String INSERT__DS18B20_SQL = "insert into DS18B20 (value, at, sensor_sensorId) values (?, ?, ?)";
	private final static String INSERT__BMP085_SQL = "insert into BMP085 (pressure, temperature, at, sensor_sensorId) values (?, ?, ?)";
	private DatabaseCon con = null;
	private OneWire ow = null;
	private List<OneWireDevice> oneWireList  = null;
	private Ubidots dots = null;
	
	public MeasureTask(DatabaseCon con, Ubidots dots) {
		ow = new OneWire();
		oneWireList = ow.getAllDevices();
		this.dots = dots;
		this.con = con;
	}

	@Override
	public void run() {
		System.out.println("MeasureTask: "+System.currentTimeMillis());
		processDS18B20();
		processBMP085();
	}
	
	private void processBMP085() {
		PreparedStatement pStmnt = null;
		try {		
			pStmnt = con.getConnection().prepareStatement(INSERT__BMP085_SQL);
			BMP085Device dev = new BMP085Device(1);
			// System.out.println("got device!");
			BMP085Result data = dev.process();
			System.out.println(String.format("temperature= %5.3f° Celsius, pressure= %6.2f hpa", 
													data.getTemperature(), (data.getPressure() * 0.01)));
			dots.writePressure((float)(data.getPressure() * 0.01));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pStmnt != null) {
				try {
					pStmnt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private void processDS18B20() {
		PreparedStatement pStmnt = null;
		try {
			pStmnt = con.getConnection().prepareStatement(INSERT__DS18B20_SQL);
			for (OneWireDevice dev : oneWireList) {
				System.out.println("MeasureTask: Dev="+dev.getAddress()+", value="+dev.getValue());
				pStmnt.setFloat(1, dev.getValue());
				pStmnt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
				pStmnt.setString(3, dev.getAddress());
				pStmnt.executeUpdate();
				
				try {
					if (dots != null) {
						if ("28-00000393c3b6".equals(dev.getAddress()))
							dots.writeOffice(dev.getValue());
						else
							dots.writeGarden(dev.getValue());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pStmnt != null) {
				try {
					pStmnt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
