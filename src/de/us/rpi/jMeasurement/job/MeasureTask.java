package de.us.rpi.jMeasurement.job;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.TimerTask;

import de.us.rpi.jMeasurement.oneWire.OneWire;
import de.us.rpi.jMeasurement.oneWire.OneWireDevice;

public class MeasureTask extends TimerTask {
	private final String INSERT_SQL = "insert into DS18B20 (value, at, sensor_sensorId) values (?, ?, ?)";
	private DatabaseCon con = null;
	private OneWire ow = null;
	private List<OneWireDevice> oneWireList  = null;
	
	public MeasureTask(DatabaseCon con) {
		ow = new OneWire();
		oneWireList = ow.getAllDevices();
		this.con = con;
	}

	@Override
	public void run() {
		System.out.println("MeasureTask: "+System.currentTimeMillis());
		PreparedStatement pStmnt = null;
		try {
			pStmnt = con.getConnection().prepareStatement(INSERT_SQL);
			for (OneWireDevice dev : oneWireList) {
				System.out.println("MeasureTask: Dev="+dev.getAddress()+", value="+dev.getValue());
				pStmnt.setFloat(1, dev.getValue());
				pStmnt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
				pStmnt.setString(3, dev.getAddress());
				pStmnt.executeUpdate();
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
