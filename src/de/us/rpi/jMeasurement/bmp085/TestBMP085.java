package de.us.rpi.jMeasurement.bmp085;

public class TestBMP085 {

	public static void main(String arg[]) {
		try {
			BMP085Device dev = new BMP085Device(1);
			System.out.println("got device!");
			BMP085Result data = dev.process();
			System.out.println(String.format("temperature= %5.3f° Celsius, pressure= %6.2f hpa", 
													data.getTemperature(), (data.getPressure() * 0.01)));
			System.out.println("Debug: "+data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
