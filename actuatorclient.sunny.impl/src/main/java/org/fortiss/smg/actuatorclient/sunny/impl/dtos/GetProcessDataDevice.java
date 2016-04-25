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
