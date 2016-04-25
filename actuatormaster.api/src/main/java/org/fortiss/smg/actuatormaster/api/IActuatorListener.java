package org.fortiss.smg.actuatormaster.api;

import java.util.concurrent.TimeoutException;

import org.fortiss.smg.actuatormaster.api.events.DeviceEvent;
import org.fortiss.smg.actuatormaster.api.events.DoubleEvent;
import org.fortiss.smg.containermanager.api.devices.DeviceId;

public interface IActuatorListener {

	//void onEventReceived(AbstractDeviceEvent ev, DeviceId dev, String client);
	void onDoubleEventReceived(DoubleEvent ev, DeviceId dev, String client) throws TimeoutException;
	
	void onDeviceEventReceived(DeviceEvent ev, String client);
	
}
