package org.fortiss.smg.actuatormaster.api;

import org.fortiss.smg.ambulance.api.HealthCheck;
import org.fortiss.smg.config.lib.WrapperConfig;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.smgschemas.commands.DoubleCommand;

public interface IActuatorClient extends HealthCheck {

	void onDoubleCommand(DoubleCommand command, DeviceId dev);
	
}
