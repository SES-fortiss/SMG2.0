/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.containermanager.api.devices;

import java.util.HashMap;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class DeviceContainer extends Container {

	DeviceContainerSpec spec;

	protected DeviceContainer() {
		spec = new DeviceContainerSpec();
	}

	public DeviceContainer(DeviceId deviceId, String internDeviceParent) {
		super();
		spec = new DeviceContainerSpec();
		spec.deviceId = deviceId;
		spec.internDeviceParent = internDeviceParent;
		this.containerId = deviceId.toContainterId();
		this.containerType = ContainerType.DEVICE;
		this.virtualContainer = false;
	}

	/**
	 * Generate DeviceContainer according to provided Spec
	 * 
	 * @param deviceId
	 * @param internDeviceParent
	 * @param spec
	 */
	public DeviceContainer(DeviceId deviceId, String internDeviceParent,
			HashMap<String, Object> deviceSpec) {
		this(deviceId, internDeviceParent);
		deserialize(deviceSpec);
		//SIDeviceType device = this.spec.deviceType;
		this.hrName = (this.spec.deviceType.toString() + " (" 
				+ deviceId.toContainterId() + ") " );
	}

	public DeviceContainerSpec getSpec() {
		return spec;
	}

	public void setSpec(DeviceContainerSpec spec) {
		this.spec = spec;
	}

	/**
	 * Generate DeviceContainer according to provided Spec
	 * 
	 * @param deviceId
	 * @param internDeviceParent
	 * @param spec
	 */
	public DeviceContainer(DeviceId deviceId, String internDeviceParent,
			DeviceContainerSpec spec) {
		this(deviceId, internDeviceParent);
		this.spec = spec;
		this.spec.deviceId = deviceId;
		this.spec.internDeviceParent = internDeviceParent;
	}

	/*
	 * loads all deviceSpecs from the Hashmap
	 */
	public void deserialize(HashMap<String, Object> deviceSpec) {
		spec.deserialize(deviceSpec);
		if (deviceSpec.containsKey("smgdevicetype")) {
			this.containerId = spec.deviceId.toContainterId();
			this.hrName = (spec.deviceType.toString() + " Sensor/Actor ("
					+ spec.deviceId.toContainterId() + ") Unit:" + spec.deviceType
					.getType().toString());
		}
	}

	@JsonIgnore
	public boolean getAcceptsCommands() {
		return spec.acceptsCommands;
	}

	/**
	 * Information whether this device has binary information (aka boolean
	 * switch).
	 * 
	 * @deprecated
	 * @return
	 */
	@JsonIgnore
	public boolean getBinaryDevice() {
		return this.isBinary();
	}

	public double getCommandMaxRange() {
		return spec.commandMaxRange;
	}

	public double getCommandMinRange() {
		return spec.commandMinRange;
	}

	public double getCommandRangeStep() {
		return spec.commandRangeStep;
	}

	public CommandRangeStepType getCommandRangeStepType() {
		return spec.commandRangeStepType;
	}

	public DeviceId getDeviceId() {
		return spec.deviceId;
	}

	public SIDeviceType getDeviceType() {
		return spec.deviceType;
	}

	public String getInternDeviceParent() {
		return spec.internDeviceParent;
	}

	public double getMaxUpdateRate() {
		return spec.maxUpdateRate;
	}

	public double getMinUpdateRate() {
		return spec.minUpdateRate;
	}

	public double getRangeMax() {
		return spec.rangeMax;
	}

	public double getRangeMin() {
		return spec.rangeMin;
	}

	public double getRangeStep() {
		return spec.rangeStep;
	}

	public double getValue() {
		return spec.value;
	}

	/**
	 * checks whether a device has a binary input (e.g. a switch)
	 * 
	 * @return true for binary devices
	 */
	public boolean isBinary() {
		if (Math.abs(spec.commandMaxRange - spec.commandMinRange) == spec.commandRangeStep) {
			return true;
		}
		return false;
	}

	public boolean isHasValue() {
		return spec.hasValue;
	}

	/*
	 * returns a Hashmap with all properties
	 */
	public HashMap<String, Object> serializeDev() {
		HashMap<String, Object> map = spec.serialize();
		map.put("devid", this.containerId);
		map.put("humanreadablename",hrName);
		return map;
	}

	public void setAcceptsCommands(boolean acceptsCommands) {
		spec.acceptsCommands = acceptsCommands;
	}

	public void setCommandMaxRange(double commandMaxRange) {
		spec.commandMaxRange = commandMaxRange;
	}

	public void setCommandMinRange(double commandMinRange) {
		spec.commandMinRange = commandMinRange;
	}

	public void setCommandRangeStep(double commandRangeStep) {
		spec.commandRangeStep = commandRangeStep;
	}

	public void setCommandRangeStepType(
			CommandRangeStepType commandRangeStepType) {
		spec.commandRangeStepType = commandRangeStepType;
	}

	public void setDeviceId(DeviceId deviceId) {
		spec.deviceId = deviceId;
	}

	public void setDeviceType(SIDeviceType deviceType) {
		spec.deviceType = deviceType;
	}

	public void setHasValue(boolean hasValue) {
		spec.hasValue = hasValue;
	}

	public void setInternDeviceParent(String internDeviceParent) {
		spec.internDeviceParent = internDeviceParent;
	}

	public void setMaxUpdateRate(double maxUpdateRate) {
		spec.maxUpdateRate = maxUpdateRate;
	}

	public void setMinUpdateRate(double minUpdateRate) {
		spec.minUpdateRate = minUpdateRate;
	}

	public void setRangeMax(double rangeMax) {
		spec.rangeMax = rangeMax;
	}

	public void setRangeMin(double rangeMin) {
		spec.rangeMin = rangeMin;
	}

	public void setRange(double rangeMin, double rangeMax) {
		spec.rangeMin = rangeMin;
		spec.rangeMax = rangeMax;
	}
	
	public void setRange(double rangeMin, double rangeMax, double rangeStep) {
		spec.rangeMin = rangeMin;
		spec.rangeMax = rangeMax;
		spec.rangeStep = rangeStep;
	}
	
	public void setRangeStep(double rangeStep) {
		spec.rangeStep = rangeStep;
	}

	public void setValue(double value) {
		spec.value = value;
		// update own statistics
		//System.out.println(summaryStatistics + " " + spec);
		summaryStatistics.put(spec.deviceType, new SummaryStatistics());
		summaryStatistics.get(spec.deviceType).addValue(value);
	}

	public void setCommandRange(double rangeMin, double rangeMax, double rangeStep) {
		spec.commandMinRange = rangeMin;
		spec.commandMaxRange = rangeMax;
		spec.commandRangeStep = rangeStep;
		spec.acceptsCommands = true;
	}
}