/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.actuatormaster.api;

import org.fortiss.smg.ambulance.api.HealthCheck;
import org.fortiss.smg.config.lib.WrapperConfig;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.smgschemas.commands.DoubleCommand;

public interface IActuatorClient extends HealthCheck {

	void onDoubleCommand(DoubleCommand command, DeviceId dev);
	
}
