package org.fortiss.smg.rulesystem.api;

public class Events {
	
	private double value;
	public String origin; // this is the container id
	double maxAbsError =0.0;
	private String devid;

	
	/**
	 * constructor called when origin is the containerID 
	 */
	public Events(String origin, double value) {
		this.origin = origin;
		this.value = value;
	}
	/**
	 * Constructor is called when origin is a device and so maxAbsError is also used
	 * @param devId
	 * @param value
	 * @param maxAbsError
	 */
	public Events(String devId, double value, double maxAbsError) {
		this.origin = devId;
		this.value = value;
		this.maxAbsError = maxAbsError;
	
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public double getMaxAbsError() {
		return maxAbsError;
	}
	public void setMaxAbsError(double maxAbsError) {
		this.maxAbsError = maxAbsError;
	}
	public String getDevid() {
		return devid;
	}
	public void setDevid(String devid) {
		this.devid = devid;
	}

}
