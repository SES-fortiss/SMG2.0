package org.fortiss.smg.containermanager.api.devices;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SIDeviceType {
	ConsumptionPowermeter(SIUnitType.W), ProductionPowermeter(SIUnitType.W), 
	FeedPowerMeter(SIUnitType.W), ConsumptionVoltmeter(SIUnitType.V), 
	ProductionVoltmeter(SIUnitType.V), FeedVoltmeter(SIUnitType.V), 
	FeedAmperemeter(SIUnitType.A), ConsumptionAmperemeter(SIUnitType.A), 
	ProductionAmperemeter(SIUnitType.A), ConsumptionPowermeterAggregated(SIUnitType.WH), 
	ProductionPowermeterAggregated(SIUnitType.WH), FeedPowerMeterAggregated(SIUnitType.WH), 
	//Light(SIUnitType.LUX), same as brightness (Google uses: light) 
	Brightness(SIUnitType.LUX),  
	LightSimple(SIUnitType.BOOL),
	LightDimmable(SIUnitType.PERCENT),
	Temperature(SIUnitType.CELSIUS), 
	Balance(SIUnitType.KG),	
	Powerplug(SIUnitType.BOOL), 
	Heating(SIUnitType.PERCENT), 
	Cooling(SIUnitType.PERCENT), 
	Blinds(SIUnitType.PERCENT), 
	Window(SIUnitType.BOOL), 
	Door(SIUnitType.BOOL), 
	Occupancy(SIUnitType.BOOL), 
	Humidity(SIUnitType.PERCENT), 
	Noise(SIUnitType.DB), 
	Pressure(SIUnitType.HPA), 
	Battery(SIUnitType.PERCENT), 
	Calculator(SIUnitType.NONE),
	Frequency(SIUnitType.HZ),
	Accelerometer(SIUnitType.NONE),
	Acceleration(SIUnitType.NONE),
	Proximity(SIUnitType.NONE),
	Gravity(SIUnitType.NONE),
	MagneticField(SIUnitType.NONE),
	MagneticFieldUncalibrated(SIUnitType.NONE),
	SignificantMotion(SIUnitType.BOOL),
	Switch(SIUnitType.SWITCHPOSITION),
	GameRotationVector(SIUnitType.NONE),
	GeomagneticRotationVector(SIUnitType.NONE),
	RotationVector(SIUnitType.NONE),
	StepCounter(SIUnitType.Count),
	StepDetector(SIUnitType.BOOL),
	;
	
	
	

	final SIUnitType type;

	private SIDeviceType(final SIUnitType type) {
		this.type = type;
	}

	public SIUnitType getType() {
		return type;
	}
	
	@JsonCreator
	public static SIDeviceType fromString(String text) {
		    if (text != null) {
		      for (SIDeviceType b : SIDeviceType.values()) {
		        if (text.toLowerCase().equals(b.name().toLowerCase())) {
		          return b;
		        }
		      }
		    }
		    return null;
		  }
	
	
}
