package de.us.rpi.jMeasurement.oneWire;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Container class of an DS18B20
 * 
 * @author us
 */
public class DS18B20 extends OneWireDevice {
	public final String DEVICE_FILE = "w1_slave";
	
	/**
	 * Constructor with sensor file
	 * 
	 * @param sensor sensor file
	 */
	public DS18B20(File sensor) {
		super(sensor);
	}
	
	/**
	 * Returns the static device filename
	 * 
	 * @return device filename (w1_slave)
	 */
	public String getDeviceFile() {
		return DEVICE_FILE;
	}
	
	/**
	 * Returns the current measurement
	 * 
	 * @return Temperature in celsius
	 * 
	 * @throws on CRC or I/O error 
	 */
	@Override
	public float getValue() throws Exception {
		BufferedReader in = null;
		float value = Float.MIN_VALUE;
	    try {
	    	String line = null;
	        in = new BufferedReader(new FileReader(sensorFile));
	        while ((line = in.readLine()) != null) {
	        	int idx = line.indexOf("crc=");
	        	if (idx >= 0) {
	        		if (!line.endsWith("YES"))
	        			throw new Exception("CRC failed!");
	        	} else {
		        	idx = line.indexOf("t=");
		        	if (idx >= 0) {
		        		value = Float.parseFloat(line.substring(idx+2)) / 1000.0f;
		        	}
	        	}
	        }
	    } catch (Exception e) {
	    	value = Float.MIN_VALUE;
	    	throw new Exception("Can't evalute measurement! ", e);
	    } finally {
	    	try { in.close(); } catch (Exception e) { ; }
	    }
	    return value;
	}
}
