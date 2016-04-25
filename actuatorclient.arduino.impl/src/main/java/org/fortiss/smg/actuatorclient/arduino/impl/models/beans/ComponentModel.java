package org.fortiss.smg.actuatorclient.arduino.impl.models.beans;

public class ComponentModel {
    // Name of the sensor or component - should be unique
	public String name;
	// Unique code of the sensor type (temperature, humidity - by Google Standard)
    public int code;
    // Threshold for each sensor, when threshold is reached Arduino will send that data
    private float threshold;
    // Pin number of the Arduino where the sensor is connected
	private String pin;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

	public float getThreshold() {
		return threshold;
	}

	public void setThreshold(float threshold) {
		this.threshold = threshold;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}
}
