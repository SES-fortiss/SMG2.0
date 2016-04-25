package org.fortiss.smg.remoteframework.test.rabbit.schemas;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public class Zoo {

	String name;
	int id;

	@Override
	public String toString() {
		return "Zoo [name=" + name + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}


