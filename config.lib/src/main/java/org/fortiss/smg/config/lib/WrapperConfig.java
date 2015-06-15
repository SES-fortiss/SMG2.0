/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.config.lib;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WrapperConfig {

	public String key;
	public String wrapperID;
	public String wrapperName;
	public String protocol;
	public String host;
	public String port;
	public String path;
	public String username;
	public String password;
	public int pollingfrequency;
	public ArrayList<Subdevice> subdevices = new ArrayList<Subdevice>();
	
	protected WrapperConfig() {
		
	}
	
	@JsonCreator
	public WrapperConfig(@JsonProperty("key") String key, @JsonProperty("wrapperID") String wrapperID, 
			@JsonProperty("wrapperName") String wrapperName, @JsonProperty("protocol") String protocol, 
			@JsonProperty("host") String host, @JsonProperty("port") String port, 
			@JsonProperty("path") String path, @JsonProperty("username") String username, 
			@JsonProperty("password")String password, @JsonProperty("pollingfrequency") int pollingfrequency,
			@JsonProperty("subdevices") ArrayList<Subdevice> subdevices) {
		super();
		this.key = key;
		this.wrapperID = wrapperID;
		this.wrapperName = wrapperName;
		this.protocol = protocol;
		this.host = host;
		this.port = port;
		this.path = path;
		this.username = username;
		this.password = password;
		this.pollingfrequency = pollingfrequency;
		this.subdevices = subdevices;
	}
	


	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getWrapperID() {
		return wrapperID;
	}

	public void setWrapperID(String wrapperID) {
		this.wrapperID = wrapperID;
	}

	public String getWrapperName() {
		return wrapperName;
	}

	public void setWrapperName(String wrapperName) {
		this.wrapperName = wrapperName;
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getPollingfrequency() {
		return pollingfrequency;
	}

	public void setPollingfrequency(int pollingfrequency) {
		this.pollingfrequency = pollingfrequency;
	}

	public ArrayList<Subdevice> getSubdevices() {
		return subdevices;
	}

	public void setSubdevices(ArrayList<Subdevice> subdevices) {
		this.subdevices = subdevices;
	}
	
}
