package org.fortiss.smg.containermanager.api.devices;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape=JsonFormat.Shape.OBJECT)
public enum ContainerFunction {
	OFFICE("Office"), CONFERENCE("Conference"), KITCHEN("Kitchen"), TOILETTE("Toilette"), PUBLIC("Public"), ROOT("Root"), WING("Wing"), UTILITY("Utility"), NONE("None");
	/* Delete Wind */
	private String value;

	ContainerFunction(String s) {
		this.value = s;
	}
	
	public String value() {
		return this.value;
	}
	
	public static ContainerFunction fromString(String x) {
		for (ContainerFunction function : ContainerFunction.values()) {
			if (function.toString().equals(x)) {
				return function;
			}
		}
		return ContainerFunction.NONE;
    }
	
	/*
	@JsonValue
	@Override
	public String toString() {
		return this.value;
	}
	
	*/
	
	public static ContainerFunction fromValue(String v) {
		for (ContainerFunction c: ContainerFunction.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}
	
}
