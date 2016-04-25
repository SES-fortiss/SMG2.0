package com.siemens.ctic2.iec61850;

import java.util.List;

public abstract class NamedAttribute {
	private String name;
	
	public NamedAttribute(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public abstract void addTo(List<Object> list);
}