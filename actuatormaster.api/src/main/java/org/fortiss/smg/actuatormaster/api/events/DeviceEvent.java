package org.fortiss.smg.actuatormaster.api.events;

import org.fortiss.smg.containermanager.api.devices.DeviceContainer;

public class DeviceEvent extends AbstractDeviceEvent<DeviceContainer> {

	protected DeviceEvent() {
		super();
	}
	
	public DeviceEvent(DeviceContainer value) {
		super(value);
	}
	
	
}
