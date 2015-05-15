package de.us.rpi.jMeasurement.bmp085;

import java.io.IOException;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

/**
 * Main class of BMP085 device
 * 
 * @author us
 *
 */
public class BMP085Device {
	/** The BMP085 */
	private I2CDevice device = null;

	/** Operating Mode (internal oversampling) */
	private static final int OSS = 3;
	
	/** I2C-Address of all BMP085 */ 
	private static final int BMP085_I2C_ADDR = 0x77;
	
	/** Calibration data from device */
	private CalibrationData calData = new CalibrationData();

// BMP085 Registers
	private static final int CAL_AC1 = 0xAA;
	private static final int CAL_AC2 = 0xAC;
	private static final int CAL_AC3 = 0xAE;
	private static final int CAL_AC4 = 0xB0;
	private static final int CAL_AC5 = 0xB2;
	private static final int CAL_AC6 = 0xB4;
	private static final int CAL_B1 = 0xB6; 
	private static final int CAL_B2 = 0xB8; 
	private static final int CAL_MB = 0xBA; 
	private static final int CAL_MC = 0xBC; 
	private static final int CAL_MD = 0xBE; 
	private static final int CONTROL = 0xF4;
	private static final int DATA_REG = 0xF6;
	private static final byte READTEMPCMD = 0x2E;
	private static final int READPRESSURECMD = 0xF4;

	/**
	 * Construktor with i2c busnumber
	 * 
	 * @param bus 0 or 1
	 * 
	 * @throws Exception
	 */
	public BMP085Device(int bus) throws Exception {
		init(bus);
	}
	
	/**
	 * Read the data
	 * 
	 * @return  Metric Data (pressure and temperature)
	 * 
	 * @throws Exception
	 */
	public BMP085Result process() throws Exception {

		device.write(CONTROL, READTEMPCMD);
		Thread.sleep(50);
		int rawTemperature = readU16(DATA_REG);

		device.write(CONTROL, (byte) READPRESSURECMD);
		Thread.sleep(50);

		int msb = readU8(DATA_REG);
		int lsb = readU8(DATA_REG + 1);
		int xlsb = readU8(DATA_REG + 2);
		int rawPressure = ((msb << 16) + (lsb << 8) + xlsb) >> (8 - OSS);

		return convertData(rawPressure, rawTemperature);
	}

	/**
	 * Initialize the device<br>
	 * <ul>
	 * <li>Get device from bus</li>
	 * <li>Load calibration data</li>
	 * </ul>
	 * 
	 * @param busNo 0 or 1 
	 * 
	 * @throws Exception
	 */
	private void init(int busNo) throws Exception {
		final I2CBus bus = busNo == 0 ? I2CFactory.getInstance(I2CBus.BUS_0)
							: I2CFactory.getInstance(I2CBus.BUS_1);
		device = bus.getDevice(BMP085_I2C_ADDR);
		readCalibrationData();
	}

	/**
	 * Read the calibration data from device
	 * 
	 * @throws IOException
	 */
	private void readCalibrationData() throws IOException {
		calData.ac1 = readS16(CAL_AC1);
		calData.ac2 = readS16(CAL_AC2);
		calData.ac3 = readS16(CAL_AC3);
		calData.ac4 = readU16(CAL_AC4);
		calData.ac5 = readU16(CAL_AC5);
		calData.ac6 = readU16(CAL_AC6);
		calData.b1 = readS16(CAL_B1);
		calData.b2 = readS16(CAL_B2);
		calData.mb = readS16(CAL_MB);
		calData.mc = readS16(CAL_MC);
		calData.md = readS16(CAL_MD);
	}

	/**
	 * Convert raw data to human readable metric data
	 * 
	 * @param rawPressure raw preasure data
	 * @param rawTemperature ram temperature data
	 * 
	 * @return Metric Data
	 */
	private BMP085Result convertData(int rawPressure, int rawTemperature) {
		double temperature = 0.0;
		double pressure = 0.0;
		double x1 = ((rawTemperature - calData.ac6) * calData.ac5) / 32768;
		double x2 = (calData.mc * 2048) / (x1 + calData.md);
		double b5 = x1 + x2;
		temperature = ((b5 + 8) / 16) / 10.0;

		double b6 = b5 - 4000;
		x1 = (calData.b2 * (b6 * b6 / 4096)) / 2048;
		x2 = calData.ac2 * b6 / 2048;
		double x3 = x1 + x2;
		double b3 = (((calData.ac1 * 4 + x3) * Math.pow(2, OSS)) + 2) / 4;
		x1 = calData.ac3 * b6 / 8192;
		x2 = (calData.b1 * (b6 * b6 / 4096)) / 65536;
		x3 = ((x1 + x2) + 2) / 4;
		double b4 = calData.ac4 * (x3 + 32768) / 32768;
		double b7 = (rawPressure - b3) * (50000 / Math.pow(2, OSS));
		if (b7 < 0x80000000)
			pressure = (b7 * 2) / b4;
		else
			pressure = (b7 / b4) * 2;
		x1 = (pressure / 256) * (pressure / 256);
		x1 = (x1 * 3038) / 65536;
		x2 = (-7375 * pressure) / 65536;
		pressure = pressure + (x1 + x2 + 3791) / 16;

		return new BMP085Result(pressure, temperature);

	}

	/**
	 * Read unsigned 1 byte from address
	 * 
	 * @param address
	 * 
	 * @return Value
	 * 
	 * @throws IOException
	 */
	private int readU8(int address) throws IOException {
		return device.read(address);
	}

	/**
	 * Read unsigned 2 byte from address
	 * 
	 * @param address
	 * 
	 * @return Value
	 * 
	 * @throws IOException
	 */
	private int readU16(int address) throws IOException {
		int hibyte = device.read(address);
		return (hibyte << 8) + device.read(address + 1);
	}

	/**
	 * Read signed 2 byte from address
	 * 
	 * @param address
	 * 
	 * @return Value
	 * 
	 * @throws IOException
	 */
	private int readS16(int address) throws IOException {
		int hibyte = device.read(address);
		if (hibyte > 127)
			hibyte -= 256;
		return (hibyte * 256) + device.read(address + 1);
	}

}
