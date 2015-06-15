/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.config.lib;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Subdevice {
	
	public int deviceCode;
	public float threshold;
	public String pin;
	
	public Subdevice(@JsonProperty("devicecode") int deviceCode,@JsonProperty("threshold") float threshold,@JsonProperty("pin") String pin) {
		super();
		this.deviceCode = deviceCode;
		this.threshold = threshold;
		this.pin = pin;
	}
	
	public Subdevice() {}
	
	public int getDeviceCode() {
		return deviceCode;
	}
	public void setDeviceCode(int deviceCode) {
		this.deviceCode = deviceCode;
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
