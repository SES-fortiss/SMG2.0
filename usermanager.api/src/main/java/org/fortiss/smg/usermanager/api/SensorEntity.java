package org.fortiss.smg.usermanager.api;

import java.io.Serializable;

public class SensorEntity implements Serializable{

	private static final long serialVersionUID = -5998757157590095154L;

	private int id;
	private String name;
	private String unit;
	private double minValue;
	private double maxValue;

	public SensorEntity(int id, String name, String unit, double minValue, double maxValue){
		this.id = id;
		this.name = name;
		this.unit = unit;
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}	
	
	public String getUnit() {
		return unit;
	}
	
	public double getMinValue() {
		return minValue;
	}
	
	public double getMaxValue() {
		return maxValue;
	}
}
