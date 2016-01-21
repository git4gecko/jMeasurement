package de.us.rpi.jMeasurement.ubidots;

import java.io.File;
import java.util.Properties;

import com.ubidots.ApiClient;
import com.ubidots.DataSource;
import com.ubidots.Variable;

import de.us.rpi.jMeasurement.tools.FileTool;

public class Ubidots {
	public final static String PROP_FILE = "ubidots.properties";
	private ApiClient api;
	private DataSource dataSource;
	private Variable tempOffice;
	private Variable tempGarden;
	
	public Ubidots() {
		init();
	}
	
	public void writeOffice(float value) {
		tempOffice.saveValue(value);
	}
	
	public void writeGarden(float value) {
		tempGarden.saveValue(value);
	}
	
	private void init() {
		Properties p = FileTool.readProperty(new File(PROP_FILE));
		api =  new ApiClient(p.getProperty("ident"));
		dataSource = api.getDataSource(p.getProperty("datasource"));
		tempOffice = api.getVariable(p.getProperty("tempOffice"));
		tempGarden = api.getVariable(p.getProperty("tempGarden"));
	}
}
