package de.us.rpi.jMeasurement.oneWire;

import java.util.List;

/**
 * First Tests
 * 
 * call: sudo java -cp ./jMeasurement.jar de.us.rpi.jMeasurement.oneWire.TestOneWire
 * 
 * @author us
 *
 */
public class TestOneWire {

	/**
	 * Startup:
	 *  1: List all active devices and the measurements
	 *  
	 * @param args none
	 */
	public static void main(String[] args) {
		OneWire ow = null;
		try {
			ow = new OneWire();
			List<OneWireDevice> devList = ow.getAllDevices();
			for (OneWireDevice dev : devList) 
				System.out.println("Device: "+dev.getAddress()+", value="+dev.getValue());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
