/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.actuatorclient.arduino.impl.connectors;

/**
 * TrasnferData responsible for storing information about the sensor name, value and signal
 * @author Balsa
 *
 */
public class TransferData {
	
	// Unique id of the sensor
	private String deviceId;
	// Value of the sensor
	private String value;
	// Flag showing if the sensor needs to be turned on (1), turned off (0), or nothing (-1) 
	private int signal;
	// Flag showing if value should be sent to the ActuatorMaster
	private int update;
	
	public TransferData(String deviceId, String value, int signal, int update) {
		this.deviceId = deviceId;
		this.value = value;
		this.signal = signal;
		this.update = update;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getSignal() {
		return signal;
	}
	public void setSignal(int signal) {
		this.signal = signal;
	}
	public int getUpdate() {
		return update;
	}
	public void setUpdate(int update) {
		this.update = update;
	}
	
}
