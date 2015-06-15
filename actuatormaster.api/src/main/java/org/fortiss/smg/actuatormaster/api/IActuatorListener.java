/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.actuatormaster.api;

import org.fortiss.smg.actuatormaster.api.events.DeviceEvent;
import org.fortiss.smg.actuatormaster.api.events.DoubleEvent;
import org.fortiss.smg.containermanager.api.devices.DeviceId;

public interface IActuatorListener {

	//void onEventReceived(AbstractDeviceEvent ev, DeviceId dev, String client);
	void onDoubleEventReceived(DoubleEvent ev, DeviceId dev, String client);
	
	void onDeviceEventReceived(DeviceEvent ev, String client);
	
}
