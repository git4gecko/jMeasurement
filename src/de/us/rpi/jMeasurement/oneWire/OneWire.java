package de.us.rpi.jMeasurement.oneWire;

import java.io.File;
import java.util.List;
import java.util.Vector;

/**
 * Main class of one wire devices
 * 
 * @author us
 *
 */
public class OneWire {
	/** Base directory of one wire */
	public static final String BASE = "/sys/bus/w1";
	/** Base directory of all one wire devices */
	public static final String DEVICES = OneWire.BASE+"/devices";
	
	/**
	 * Returns a list of active one wire devices
	 * 
	 * @return list of active devices
	 */
	public List<OneWireDevice> getAllDevices() {
		Vector<OneWireDevice> devList = new Vector<OneWireDevice>();
		File deviceDir = new File(DEVICES);
		for (File device : deviceDir.listFiles()) {
			String name = device.getName(); 
			if (!name.startsWith("wi_")) {
				if (name.startsWith("28-"))
					devList.add(new DS18B20(device));
			}
		}
		return devList;
	}
}
