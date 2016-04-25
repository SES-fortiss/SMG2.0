package org.fortiss.smg.actuatorclient.sunny.impl.dtos;

import java.util.Collection;

public class Device {
	private String key; 
	private String name; 
	private Collection<Channel> channels;
	private Collection<Device> children;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Collection<Channel> getChannels() {
		return channels;
	}
	public void setChannels(Collection<Channel> channels) {
		this.channels = channels;
	}
	public Collection<Device> getChildren() {
		return children;
	}
	public void setChildren(Collection<Device> children) {
		this.children = children;
	}
	
	
}
