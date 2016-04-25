package org.fortiss.smg.actuatorclient.arduino.impl;

import org.fortiss.smg.actuatormaster.api.IActuatorClient;
import org.fortiss.smg.actuatormaster.api.IActuatorMaster;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.fortiss.smg.actuatorclient.arduino.impl.commands.CommandFactory;
import org.fortiss.smg.actuatorclient.arduino.impl.connectors.CommunicationManager;
import org.fortiss.smg.actuatorclient.arduino.impl.models.DeviceManager;
import org.fortiss.smg.actuatorclient.arduino.impl.models.beans.ArduinoClientModel;

import org.fortiss.smg.containermanager.api.devices.DeviceContainer;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.smgschemas.commands.DoubleCommand;
import org.slf4j.LoggerFactory;

public class ActuatorClientImpl implements IActuatorClient {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(ActuatorClientImpl.class);
    
    // Frequency for updating data
    private int frequency;
    
    // List of all sensors attached to the Arduino microcontroller
    private ArrayList<DeviceContainer> devices = new ArrayList<DeviceContainer>();
    
    // ArduinoClientModel object obtained from the arduino configuration file
    private ArduinoClientModel arduinoClientModel;
    // ActuatorMaster
    private IActuatorMaster actuatorMaster;
    // Unique identifier for Arduino - ActuatorClient
    private String clientId;
    
    private ScheduledExecutorService executor;
    
    private CommunicationManager communicationManager;
    
    private CommandFactory commandFactory;
    
    /**
     * Constructor for creating ActuatorClientImpl
     * @param arduinoConfig the arduinoCofing object obtained from the configuration file
     */
	public ActuatorClientImpl(ArduinoConfig arduinoConfig) {
		super();
		this.frequency = arduinoConfig.getPolling_frequency();
		DeviceManager deviceManager = new DeviceManager();
		deviceManager.loadDevices(arduinoConfig);
		arduinoClientModel = deviceManager.getArduinoClientModel();
		devices = deviceManager.getDevices();
		communicationManager = new CommunicationManager(arduinoClientModel);
	}
	
	/**
	 * Method called by ActuatorClientActivator, responsible for sending data from sensors to the actuator master
	 * This method uses frequency as a parameter for updating data to the master
	 * Every several seconds it is calling CommunicationManager (all Threads inside) that are sending their values
	 * @param null
	 * @return void
	 */
	public void activate() {
		System.out.println("The ActuatorClient - Arduino is working!");
		logger.info("The ActuatorClient - Arduino is working!");
		commandFactory = new CommandFactory(devices, actuatorMaster, clientId, communicationManager);
		commandFactory.setupCommunicationManager();
		commandFactory.registerNewDeivceEvents();
		executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(commandFactory, 0,
				frequency, TimeUnit.SECONDS);
	}

	/**
	 * Method for receiving data from the actuator master
	 * We use this method when we want to update the state of some Arduino component (For example light attached
	 * to the Arduino microcontroller)
	 * @param command the command that represents 1 (for switching on) or 0 (for switching off)
	 * @param dev the device that should receive the command and change the state
	 * @return void
	 */
	@Override
	public void onDoubleCommand(DoubleCommand command, DeviceId dev) {
		commandFactory.sendDataToDevice(command, dev);	
	}

	/**
	 * This method should destroy all resources at the end and prevent memory leaking
	 * Should destroy all started threads 
	 */
	public void destroy() {
		commandFactory.destroy();
		executor.shutdown();
		try {
			executor.awaitTermination(1, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public boolean isComponentAlive() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
	public void setActuatorMaster(IActuatorMaster actuatorMaster) {
		this.actuatorMaster = actuatorMaster;
	}
}
