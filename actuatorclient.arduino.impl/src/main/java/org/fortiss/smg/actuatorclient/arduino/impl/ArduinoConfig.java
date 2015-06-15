/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.actuatorclient.arduino.impl;

import java.util.ArrayList;

import org.fortiss.smg.actuatorclient.arduino.impl.models.beans.Subdevice;

public class ArduinoConfig {
	
	private String wrapper_id;
	private String wrapper_name;
	private String protocol;
	private String host;
	private String port;
	private int polling_frequency;
	private ArrayList<Subdevice> subdevices = new ArrayList<Subdevice>();
	
	public String getWrapper_id() {
		return wrapper_id;
	}
	public void setWrapper_id(String wrapper_id) {
		this.wrapper_id = wrapper_id;
	}
	public String getWrapper_name() {
		return wrapper_name;
	}
	public void setWrapper_name(String wrapper_name) {
		this.wrapper_name = wrapper_name;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public int getPolling_frequency() {
		return polling_frequency;
	}
	public void setPolling_frequency(int polling_frequency) {
		this.polling_frequency = polling_frequency;
	}
	public ArrayList<Subdevice> getSubdevices() {
		return subdevices;
	}
	public void setSubdevices(ArrayList<Subdevice> subdevices) {
		this.subdevices = subdevices;
	}
}
