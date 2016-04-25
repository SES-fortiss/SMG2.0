package org.fortiss.smg.containermanager.api.devices;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ContainerType {	
	COMPLEX, FLOOR, ROOM, BUILDING, DEVICE, DEVICEGATEWAY, UNKNOWN;
	
	
	public static ContainerType fromSting(String x) {
		for (ContainerType type : ContainerType.values()) {
			if (type.toString().equals(x)) {
				return type;
			}
		}
		return ContainerType.UNKNOWN;
    }
	
}
