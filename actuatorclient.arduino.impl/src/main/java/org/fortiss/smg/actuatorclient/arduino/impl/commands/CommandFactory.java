package org.fortiss.smg.actuatorclient.arduino.impl.commands;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.actuatorclient.arduino.impl.connectors.CommunicationManager;
import org.fortiss.smg.actuatorclient.arduino.impl.connectors.TransferData;
import org.fortiss.smg.actuatormaster.api.IActuatorMaster;
import org.fortiss.smg.actuatormaster.api.events.DeviceEvent;
import org.fortiss.smg.actuatormaster.api.events.DoubleEvent;
import org.fortiss.smg.containermanager.api.devices.DeviceContainer;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.smgschemas.commands.DoubleCommand;
import org.slf4j.LoggerFactory;

/**
 * Responsible for sending commands to the actuator master
 * @author Balsa
 *
 */
public class CommandFactory implements Runnable {
	
	private static org.slf4j.Logger logger = LoggerFactory
			.getLogger(CommandFactory.class);
	
	// List of all sensors attached to the Arduino microcontroller 
	private ArrayList<DeviceContainer> devices = new ArrayList<DeviceContainer>();
	// ActuatorMaster
	private IActuatorMaster master;
	// Unique identifier for Arduino - ActuatorClient
	private String clientId;
	
	// ScheduleExecutorService for calling threads every interval
	private ScheduledExecutorService executor;
	
	// CommunicationManager is used for sending data and getting data from the devices
	private CommunicationManager communicationManager;
	
	/**
	 * CommandFactory is responsible for sending all data to the actuator master
	 * @param devices list of sensors attached to the Arduino 
	 * @param master ActuatorMaster responsible for receiving data from ActuatorClient
	 * @param clientId
	 * @param communicationManager
	 */
	public CommandFactory(ArrayList<DeviceContainer> devices, IActuatorMaster master, String clientId, CommunicationManager communicationManager) {
		super();
		this.devices = devices;
		this.master = master;
		this.clientId = clientId;
		this.communicationManager = communicationManager;
	}
	
	/**
	 * Method for setting up the communication manager, that is later setting up all connectors.
	 * It is every second calling method for updating all values from sensors
	 */
	public void setupCommunicationManager() {
		communicationManager.setup();
		executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(communicationManager, 0,
				1, TimeUnit.SECONDS);
	}
	
	/**
	 * Method for registering new device events to the actuator master
	 * @param null
	 * @return void 
	 */
	public void registerNewDeivceEvents() {
		for (DeviceContainer device : devices) {
			try {
				logger.debug("Registring new device event for: " + device.getDeviceId().getDevid() + " of the client: " + clientId);
				master.sendDeviceEvent(new DeviceEvent(device), clientId);
			} catch (TimeoutException e) {
				logger.debug("Failed to register new device event for: " + device.getDeviceId() + " of the client: " + clientId
						+ " to master");
			}
		}
	}
	
	/**
	 * Method for asynchronous changing state of the sensor 
	 * @param command the DoubleCommand sent by actuator master
	 * @param device the name of the sensor that should be updated
	 */
	public void sendDataToDevice(DoubleCommand command, DeviceId device) {
		double value = command.getValue();
		String deviceId = device.getDevid(); 
		// Command for turning off the device
		if (value == 0) {
			communicationManager.updateSensorValue(deviceId, 0);
		}
		// Command for turning on the device
		else if (value == 1) {
			communicationManager.updateSensorValue(deviceId, 1);
		}
		
		// Update changed value to the ActuatorMaster
		// TODO maybe do not need, since all sensors are updating in the run
//		DoubleEvent doubleEvent = new DoubleEvent(value);
//		try {
//			master.sendDoubleEvent(doubleEvent, device, clientId);
//		} catch (TimeoutException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	/**
	 * Method called every frequence of seconds, that is getting data from the trasnferData (global hashmap) 
	 * and sending them to the actuator master
	 */
	@Override
	public void run() {
		ConcurrentHashMap<String, TransferData> trasferData = communicationManager.transferData;
		for (Entry<String, TransferData> entry : trasferData.entrySet()) {
			String key = entry.getKey();
			TransferData value = entry.getValue();
			if (value.getUpdate() == 1) {
				
				System.out.println("UPDATE: " + key + " : " + value.getValue() + " / " + value.getSignal());
			  
				// TODO DON'T FORGET TO ADD THREAD DELAY AFTER RECEIVING REQUEEST FROM MASTER!!!!
			  
				// TODO just for testing sending data to the sensor
				// TODO This is the alternative for testing. Method sendDataToDevice is responsible for this
			  
				String valueData = value.getValue();
				int num = Integer.parseInt(valueData);
				if (num < 26 && key.equals("smg-client-adroino_nano.12.1")) {
					communicationManager.updateSensorValue("smg-client-adroino_nano.0.4", 0);
					try {
						Thread.currentThread().sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
					communicationManager.updateSensorValue("smg-client-adroino_nano.0.3", 1);
					System.out.println("SEND DATA: " + key + " : " + value.getValue() + " / " + 1);
				}
				if (num >= 26 && key.equals("smg-client-adroino_nano.12.1")) {
					communicationManager.updateSensorValue("smg-client-adroino_nano.0.3", 0);
					try {
						Thread.currentThread().sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
					communicationManager.updateSensorValue("smg-client-adroino_nano.0.4", 1);
					System.out.println("SEND DATA: " + key + " : " + value.getValue() + " / " + 0);
				}
				
				value.setUpdate(0);
				entry.setValue(value);
				
				// TODO Currently not possible:  Data from Arduino is string and int, but possible when change data just to int
				for(DeviceContainer device : devices){
					if (value.getDeviceId().equals(device.getDeviceId().getDevid())) {
						DoubleEvent doubleEvent = new DoubleEvent(Double.parseDouble(value.getValue()));
						try {
							master.sendDoubleEvent(doubleEvent, device.getDeviceId(), clientId);
						} catch (TimeoutException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	
	/**
	 * Method for destroying all resources, closing communication manager and turning off executor
	 */
	public void destroy() {
		communicationManager.close();
		executor.shutdown();
		try {
			executor.awaitTermination(1, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
