package org.fortiss.smg.containermanager.api.devices;

import java.util.HashMap;

public class DeviceContainerSpec {

	// you are able to send commands
	boolean acceptsCommands = false;

	boolean binaryDevice; // SW: we don't need this. this should be a function!

	// inclusive
	double commandMaxRange = -1;
	double commandMinRange = -1;

	double commandRangeStep = -1;

	CommandRangeStepType commandRangeStepType  = CommandRangeStepType.LINEAR;

	// intern device name
	DeviceId deviceId;
	SIDeviceType deviceType;

	// has a value (only in case of stran ge toggle false)
	boolean hasValue = true;
	// hierarchy for devices given by the wrapper
	String internDeviceParent;

	double maxUpdateRate = -1;
	// in milliseconds
	double minUpdateRate = -1;
	// inclusive
	double rangeMax = -1;

	double rangeMin = -1;

	// is equal to precision
	double rangeStep = -1;

	double value = -1;

	/*
	 * returns a Hashmap with all properties
	 */
	public HashMap<String, Object> serialize() {
		HashMap<String, Object> deviceSpec = new HashMap<String, Object>();
		deviceSpec.put("devid", deviceId);
		//deviceSpec.put("internparent", internDeviceParent);
		deviceSpec.put("devicetype", deviceType);
		//deviceSpec.put("value", value);
		deviceSpec.put("minupdaterate", minUpdateRate);
		deviceSpec.put("maxupdaterate", maxUpdateRate);
		deviceSpec.put("acceptscommands", acceptsCommands);
		deviceSpec.put("hasvalue", hasValue);
		deviceSpec.put("rangemin", rangeMin);
		deviceSpec.put("rangemax", rangeMax);
		deviceSpec.put("rangestep", rangeStep);
		deviceSpec.put("commandminrange", commandMinRange);
		deviceSpec.put("commandmaxrange", commandMaxRange);
		deviceSpec.put("commandrangestep", commandRangeStep);
		deviceSpec.put("commandrangesteptype", commandRangeStepType);
		return deviceSpec;
	}

	/*
	 * loads all deviceSpecs from the Hashmap
	 */
	public void deserialize(HashMap<String, Object> deviceSpec) {

		if (deviceSpec.containsKey("smgdevicetype")) {
			this.deviceType = SIDeviceType.fromString(deviceSpec.get(
					"smgdevicetype").toString());
		}

		// this.containerFunction = ContainerFunction.;
		// this.aggFunctions = AggregateTypes;

		if (deviceSpec.containsKey("minupdaterate")) {
			this.minUpdateRate = (Double.parseDouble(deviceSpec.get(
					"minupdaterate").toString()));
		}

		if (deviceSpec.containsKey("maxupdaterate")) {
			this.maxUpdateRate = (Double.parseDouble(deviceSpec.get(
					"maxupdaterate").toString()));
		}

		if (deviceSpec.containsKey("acceptscommands")) {
			this.acceptsCommands = (Double.parseDouble(deviceSpec.get(
					"acceptscommands").toString()) != 0);
		}

		if (deviceSpec.containsKey("hasvalue")) {
			this.hasValue = (Double.parseDouble(deviceSpec.get("hasvalue")
					.toString()) != 0);
		}

		if (deviceSpec.containsKey("rangemin")) {
			this.rangeMin = (Double.parseDouble(deviceSpec.get("rangemin")
					.toString()));
		}

		if (deviceSpec.containsKey("rangemax")) {
			this.rangeMax = (Double.parseDouble(deviceSpec.get("rangemax")
					.toString()));
		}

		if (deviceSpec.containsKey("rangestep")) {
			this.rangeStep = (Double.parseDouble(deviceSpec.get("rangestep")
					.toString()));
		}

		if (deviceSpec.containsKey("commandminrange")) {
			this.commandMinRange = (Double.parseDouble(deviceSpec.get(
					"commandminrange").toString()));
		}

		if (deviceSpec.containsKey("commandmaxrange")) {
			this.commandMaxRange = (Double.parseDouble(deviceSpec.get(
					"commandmaxrange").toString()));
		}

		if (deviceSpec.containsKey("commandrangestep")) {
			this.commandRangeStep = (Double.parseDouble(deviceSpec.get(
					"commandrangestep").toString()));
		}

		if (deviceSpec.containsKey("commandrangesteptype")) {
			this.commandRangeStepType = CommandRangeStepType
					.fromString(deviceSpec.get("commandrangesteptype")
							.toString());
		}
	}

	public boolean isAcceptsCommands() {
		return acceptsCommands;
	}

	public void setAcceptsCommands(boolean acceptsCommands) {
		this.acceptsCommands = acceptsCommands;
	}

	public boolean isBinaryDevice() {
		return binaryDevice;
	}

	public void setBinaryDevice(boolean binaryDevice) {
		this.binaryDevice = binaryDevice;
	}

	public double getCommandMaxRange() {
		return commandMaxRange;
	}

	public void setCommandMaxRange(double commandMaxRange) {
		this.commandMaxRange = commandMaxRange;
	}

	public double getCommandMinRange() {
		return commandMinRange;
	}

	public void setCommandMinRange(double commandMinRange) {
		this.commandMinRange = commandMinRange;
	}

	public double getCommandRangeStep() {
		return commandRangeStep;
	}

	public void setCommandRangeStep(double commandRangeStep) {
		this.commandRangeStep = commandRangeStep;
	}

	public CommandRangeStepType getCommandRangeStepType() {
		return commandRangeStepType;
	}

	public void setCommandRangeStepType(
			CommandRangeStepType commandRangeStepType) {
		this.commandRangeStepType = commandRangeStepType;
	}

	public DeviceId getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(DeviceId deviceId) {
		this.deviceId = deviceId;
	}

	public SIDeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(SIDeviceType deviceType) {
		this.deviceType = deviceType;
	}

	public boolean isHasValue() {
		return hasValue;
	}

	public void setHasValue(boolean hasValue) {
		this.hasValue = hasValue;
	}

	public String getInternDeviceParent() {
		return internDeviceParent;
	}

	public void setInternDeviceParent(String internDeviceParent) {
		this.internDeviceParent = internDeviceParent;
	}

	public double getMaxUpdateRate() {
		return maxUpdateRate;
	}

	public void setMaxUpdateRate(double maxUpdateRate) {
		this.maxUpdateRate = maxUpdateRate;
	}

	public double getMinUpdateRate() {
		return minUpdateRate;
	}

	public void setMinUpdateRate(double minUpdateRate) {
		this.minUpdateRate = minUpdateRate;
	}

	public double getRangeMax() {
		return rangeMax;
	}

	public void setRangeMax(double rangeMax) {
		this.rangeMax = rangeMax;
	}

	public double getRangeMin() {
		return rangeMin;
	}

	public void setRangeMin(double rangeMin) {
		this.rangeMin = rangeMin;
	}

	public double getRangeStep() {
		return rangeStep;
	}

	public void setRangeStep(double rangeStep) {
		this.rangeStep = rangeStep;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public void setRange(int min, int max) {
		this.rangeMin = min;
		this.rangeMax = max;
	}
	
	public void setCommandRange(int min, int max) {
		this.commandMinRange = min;
		this.commandMaxRange = max;
	}
}
