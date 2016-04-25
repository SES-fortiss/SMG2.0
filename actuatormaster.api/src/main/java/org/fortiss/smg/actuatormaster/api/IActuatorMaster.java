package org.fortiss.smg.actuatormaster.api;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.tuple.Triple;
import org.fortiss.smg.actuatormaster.api.events.DeviceEvent;
import org.fortiss.smg.actuatormaster.api.events.DoubleEvent;
import org.fortiss.smg.ambulance.api.HealthCheck;
import org.fortiss.smg.config.lib.WrapperConfig;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.smgschemas.commands.DoubleCommand;

public interface IActuatorMaster extends HealthCheck {
    
	
	/**
	 * used to register as ActuatorClient
	 * @param clientName
	 * @return returns the queue name
	 * @throws TimeoutException
	 */

	String registerClient(String clientName) throws TimeoutException;
	
	boolean isRegisteredClient(String clientName) throws TimeoutException;
	
	/**
	 * used to register as listener.
	 * @param clientName Human readable name of the component
	 * @param queueName the queueAdress of the component. Has to be unique
	 * @throws TimeoutException
	 */
	void registerListener(String clientName, String queueName) throws TimeoutException;
	
	
	boolean isRegisteredListener(String clientName, String queueName) throws TimeoutException;

	/**
	 * redirects command to the clients
	 * @param command
	 * @param dev
	 * @throws TimeoutException
	 */
	void sendDoubleCommand(DoubleCommand command, DeviceId dev) throws TimeoutException;
	

	/**
	 * client received an event and sends it to the master
	 * @param ev
	 * @param dev
	 * @param client
	 * @throws TimeoutException
	 */
	void sendDoubleEvent(DoubleEvent ev, DeviceId dev, String client) throws TimeoutException;
	
	/**
	 * client saw an new device (or update) and sends it to the master
	 * @param DeviceEvent ev
	 * @param client
	 * @throws TimeoutException
	 */
	void sendDeviceEvent(DeviceEvent ev, String client) throws TimeoutException;
	
	/**
	 * client saw an new device (or update) and sends it to the master
	 */
	ArrayList<Triple<DeviceId, DoubleEvent, Long>> getLatestNumber(int number) throws TimeoutException;
	
	
	/**
	 * request Registered Devices from the master
	 */
	void resendRegisteredDevices() throws TimeoutException;
	
	
	ArrayList<WrapperConfig> getWrapperConfig(String key) throws TimeoutException;

	
}
