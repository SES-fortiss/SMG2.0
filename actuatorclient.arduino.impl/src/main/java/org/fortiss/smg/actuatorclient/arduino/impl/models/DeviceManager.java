package org.fortiss.smg.actuatorclient.arduino.impl.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.actuatorclient.arduino.impl.ArduinoConfig;
import org.fortiss.smg.actuatorclient.arduino.impl.models.beans.ArduinoClientModel;
import org.fortiss.smg.actuatorclient.arduino.impl.models.beans.CommunicationModel;
import org.fortiss.smg.actuatorclient.arduino.impl.models.beans.ComponentModel;
import org.fortiss.smg.actuatorclient.arduino.impl.models.beans.Subdevice;
import org.fortiss.smg.containermanager.api.ContainerManagerInterface;
import org.fortiss.smg.containermanager.api.ContainerManagerQueueNames;
import org.fortiss.smg.containermanager.api.devices.DeviceContainer;
import org.fortiss.smg.remoteframework.lib.DefaultProxy;
import org.slf4j.LoggerFactory;

/**
 * Device manager responsible for parsing config file and creating list of DeviceContainer
 * @author Balsa
 *
 */
public class DeviceManager {
	
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(DeviceManager.class);
	
	// ArduinoClientModel object obtained from the arduino configuration file
    private ArduinoClientModel arduinoClientModel;
    
 	// List of all sensors attached to the Arduino microcontroller
    private ArrayList<DeviceContainer> devices = new ArrayList<DeviceContainer>();
    
    public DeviceManager() {	
    }
    
    /**
     * The method responsible for storing all data regarding sensors and components 
     * in the list of DeviceContainer and then register them to the ContainerManager
     * @param arduinoConfig arduinoConfig is the configuration class obtained from config file
     */
    public void loadDevices(ArduinoConfig arduinoConfig) {
    	initializeModel(arduinoConfig);
    	registerDevices(arduinoConfig.getWrapper_id());
    }
    
    /**
     * Method for reading all relevant information regarding devices and sensors from arduino configuration object
     * obtained from the global configuration file
     * @param arduinoConfig arduinoConfig is the configuration class obtained from config file
     */
    public void initializeModel(ArduinoConfig arduinoConfig) {
    	CommunicationModel communicationModel = new CommunicationModel();
    	communicationModel.setName(arduinoConfig.getProtocol());
    	communicationModel.setAddress(arduinoConfig.getHost());
    	communicationModel.setPort(arduinoConfig.getPort());
    	ArrayList<CommunicationModel> communications = new ArrayList<CommunicationModel>();
    	communications.add(communicationModel);
    	
    	String arduinoWrappedData = "";
    	ArrayList<ComponentModel> components = new ArrayList<ComponentModel>();
    	int counter = 0;
    	for (Subdevice subdevice: arduinoConfig.getSubdevices()) {
    		ComponentModel component = new ComponentModel();
    		component.setCode(subdevice.getDeviceCode());
    		component.setName(arduinoConfig.getWrapper_name() + "." + subdevice.getDeviceCode() + "." + (++counter));
    		component.setThreshold(subdevice.getThreshold());
    		component.setPin(subdevice.getPin());
    		components.add(component);
    		
    		// Adding all relevant data regarding sensors to the wrapped data, that will be sent to Arduino
    		// in order to setup all sensors
    		arduinoWrappedData += "{";
    		arduinoWrappedData += "name:" + component.getName() + ",";
    		arduinoWrappedData += "code:" + component.getCode() + ",";
    		arduinoWrappedData += "threshold:" + component.getThreshold() + ",";
    		arduinoWrappedData += "pin:" + component.getPin();
    		arduinoWrappedData += "}";
    	}
    	
    	arduinoClientModel = new ArduinoClientModel();
    	arduinoClientModel.setName(arduinoConfig.getWrapper_name());
    	arduinoClientModel.setCommunications(communications);
    	arduinoClientModel.setComponents(components);
    	arduinoClientModel.setArduinoWrappedData(arduinoWrappedData);
    }
    
    /**
     * Method responsible for storing all data regarding sensors and their specification into the list of 
	 * DeviceContainer, and then register them to the ContainerManager
     * @param wrapperTag the wrapper tag of the actuator client
     */
    private void registerDevices(String wrapperTag) {
    	DefaultProxy<ContainerManagerInterface> clientInfo = new DefaultProxy<ContainerManagerInterface>(
				ContainerManagerInterface.class,
				ContainerManagerQueueNames.getContainerManagerInterfaceQueryQueue(), 1000000);
		ContainerManagerInterface containerManagerClient = null;
		try {
			containerManagerClient = clientInfo.init();
			for (ComponentModel sensor: arduinoClientModel.getComponents()) {
				if (sensor.getCode() != 0) {
					HashMap<String, Object> sensorSpecification = containerManagerClient.getDeviceSpecData(sensor.getCode());
					DeviceContainer deviceContainer = new DeviceContainer(new org.fortiss.smg.containermanager.api.devices.DeviceId(
							sensor.getName(), wrapperTag), wrapperTag,  sensorSpecification);
					deviceContainer.setVirtualContainer(false);
					deviceContainer.setHrName(sensor.getName());
					devices.add(deviceContainer);
					logger.info("Device container " + sensor.getName() + " is successfully added");
					System.out.println(arduinoClientModel.getName() + ": " + sensor.getName());
				}
			}
		} catch (TimeoutException e) {
			logger.error("Timeout Exception");
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("IO Exception");
			e.printStackTrace();
		}
    }
    
    public ArduinoClientModel getArduinoClientModel() {
		return arduinoClientModel;
	}

	public void setArduinoClientModel(ArduinoClientModel arduinoClientModel) {
		this.arduinoClientModel = arduinoClientModel;
	}

	public ArrayList<DeviceContainer> getDevices() {
		return devices;
	}

	public void setDevices(ArrayList<DeviceContainer> devices) {
		this.devices = devices;
	}
}
