/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
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
