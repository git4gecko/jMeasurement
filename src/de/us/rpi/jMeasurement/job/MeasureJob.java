package de.us.rpi.jMeasurement.job;

import java.sql.SQLException;
import java.util.Timer;

public class MeasureJob {

	private DatabaseCon con = null;
	private long pollIntervall = 5 * 60 * 1000;

	public static void main(String[] args) {
		MeasureJob job;
		try {
			job = new MeasureJob();
			job.process();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void process() throws Exception {
		Timer timer = new Timer();
		DatabaseCon con = null;
		try {
			con = new DatabaseCon();
			timer.schedule( new MeasureTask(con), 1000, this.pollIntervall);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			/*if (con != null)
				con.getConnection().close();*/
		}
			


	}

}
