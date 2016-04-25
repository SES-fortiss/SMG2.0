package org.fortiss.smg.informationbroker.api;

import org.fortiss.smg.ambulance.api.HealthCheck;

public interface InformationBrokerInterfaceForICTProject extends HealthCheck {
	
	public void onDoubleEventReceivedForICTProject(String deviceRoom, String hrName ,String devId, String tech,
			String devType , String sensorOrActuator , double minRange , double maxRange);
}
