package de.us.rpi.jMeasurement.oneWire;

import java.io.File;

/**
 * Base class of all one wire devices
 * 
 * @author us
 *
 */
public abstract class OneWireDevice {
	/** one wire address */
	protected String address = null;
	/** Complete path to sensor file */
	protected File sensorFile = null;
	
	/**
	 * Constructor with sensor directory
	 * @param sensorDir
	 */
	public OneWireDevice(File sensorDir) {
		sensorFile = new File(sensorDir.getPath()+"/"+getDeviceFile());
		address = sensorDir.getName();
	}
	
	/**
	 * Returns the one wire address (filename)
	 * 
	 * @return address
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * Returns the current measurement
	 * 
	 * @return measurement
	 * 
	 * @throws Exception
	 */
	abstract public float getValue()  throws Exception;

	/**
	 * Returns the static device filename
	 * 
	 * @return device filename (w1_slave)
	 */
	abstract public String getDeviceFile();

	/**
	 * Returns the address
	 * 
	 * @return address
	 */
	@Override
	public String toString() {
		return "["+address+"]";
	}
	
}
