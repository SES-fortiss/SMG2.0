/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.actuatorclient.sunny.impl.dtos;

import java.util.Arrays;
import java.util.Collection;

import org.fortiss.smg.actuatorclient.sunny.impl.jsonrpc.dto.JSONRPCInputParam;

public class GetProcessDataDevice implements JSONRPCInputParam {
	private String key; 
	private Collection<String> channels; 
	
	public GetProcessDataDevice(String key, String ... channels) {
		this.key = key; 
		this.channels = Arrays.asList(channels);
		
	}
	
	public String getKey() {
		return key;
	}
	
	public Collection<String> getChannels() {
		return channels;
	}
}
