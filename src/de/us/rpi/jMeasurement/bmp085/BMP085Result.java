package de.us.rpi.jMeasurement.bmp085;

/**
 * Result data class of pressure and temperature
 * 
 * @author us
 *
 */
public class BMP085Result {
	private double pressure = -42.0; 
	private double temperature = -42.0;
	
	public BMP085Result() {
	}
	
	public BMP085Result(double pressure, double temperature) {
			this.pressure = pressure;
		this.setTemperature(temperature);
	}
	
	public double getPressure() {
		return pressure;
	}
	public void setPressure(double pressure) {
		this.pressure = pressure;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
	
	@Override
	public String toString() {
		return "["+temperature+", "+pressure+"]";
	}
}
