package org.fortiss.smg.actuatorclient.froeschl.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.actuatormaster.api.IActuatorClient;
import org.fortiss.smg.actuatormaster.api.IActuatorMaster;
import org.fortiss.smg.actuatormaster.api.events.DeviceEvent;
import org.fortiss.smg.containermanager.api.ContainerManagerInterface;
import org.fortiss.smg.containermanager.api.ContainerManagerQueueNames;
import org.fortiss.smg.containermanager.api.devices.DeviceContainer;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.remoteframework.lib.DefaultProxy;
import org.fortiss.smg.smgschemas.commands.DoubleCommand;
import org.slf4j.LoggerFactory;

public class ActuatorClientImpl implements IActuatorClient {

	private static org.slf4j.Logger logger = LoggerFactory
			.getLogger(ActuatorClientImpl.class);
	private IActuatorMaster master;
	private String clientId;
	private ScheduledExecutorService executor;
	private int pollFrequency;
	private String host;
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	private String username;
	private String password;
	private int deviceCounter;	
	private JSONReaderFroeschl reader;


	private String wrapperTag;

	private ArrayList<DeviceContainer> devices = new ArrayList<DeviceContainer>();
	

	public ActuatorClientImpl(String host, String port, String wrapperTag,
			int pollFreq, String username, String password) {
		this.host = host;
		//this.port = port;
		this.wrapperTag = wrapperTag;
		this.username = username;
		this.password = password;
		this.deviceCounter = 0;
		reader = new JSONReaderFroeschl();
		loadStaticDevs(this.wrapperTag, reader.readJsonFroeschl(host));
		//sendNewDeviceEvents();
		pollFrequency = pollFreq;

	}

	public int getDeviceCounter() {
		return deviceCounter;
	}

	public void setDeviceCounter(int deviceCounter) {
		this.deviceCounter = deviceCounter;
	}

	public synchronized void activate() {
		
		
		sendNewDeviceEvents();

		executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(new FroeschlLooper(this),0 ,
				getPollFrequency(), TimeUnit.SECONDS);
		
		logger.info("Scheduled Executor started");
	}

	public synchronized void deactivate() {
		executor.shutdown();
		try {
			executor.awaitTermination(1, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String getClientId() {
		return clientId;
	}

	public List<DeviceContainer> getDeviceSpecs() {
		return devices;
	}

	public IActuatorMaster getMaster() {
		return master;
	}

	public int getPollFrequency() {
		return pollFrequency;
	}

	public String getWrapperTag() {
		return this.wrapperTag;
	}

	@Override
	public boolean isComponentAlive() {
		return true;
	}

	public void loadStaticDevs(String wrapperTagN, String[] parsedDevices) {
		deviceCounter = 0;
		
		if (parsedDevices != null) {
			DefaultProxy<ContainerManagerInterface> clientInfo = new DefaultProxy<ContainerManagerInterface>(
					ContainerManagerInterface.class,
					ContainerManagerQueueNames.getContainerManagerInterfaceQueryQueue(), 10000);

			logger.info("try to init CM interface");
			ContainerManagerInterface containerManagerClient = null;
			try {
				containerManagerClient = clientInfo.init();
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			try {	
			boolean virtual = false;
				// Actual Value
				HashMap<String, Object> deviceSpec = containerManagerClient.getDeviceSpecData(121);
				if (deviceSpec != null) {
					
					DeviceContainer device = new DeviceContainer(new org.fortiss.smg.containermanager.api.devices.DeviceId(
					"FroeschlActualVal", wrapperTagN), wrapperTagN,  deviceSpec); 
					device.setVirtualContainer(virtual);
					devices.add(device);
					deviceCounter++;
				}
				//Aggregated Value
				deviceSpec = containerManagerClient.getDeviceSpecData(124);
				if (deviceSpec != null) {
				
					DeviceContainer device = new DeviceContainer(new org.fortiss.smg.containermanager.api.devices.DeviceId(
					"FroeschlZaehlerstand", wrapperTagN), wrapperTagN,  deviceSpec); 
					device.setVirtualContainer(virtual);
					devices.add(device);	
					deviceCounter++;
				}
				//Aggregated Daily Value
				deviceSpec = containerManagerClient.getDeviceSpecData(124);
				if (deviceSpec != null) {
				
					DeviceContainer device = new DeviceContainer(new org.fortiss.smg.containermanager.api.devices.DeviceId(
					"FroeschlDailyEnergyConsumption", wrapperTagN), wrapperTagN,  deviceSpec); 
					device.setVirtualContainer(virtual);
					devices.add(device);	
					deviceCounter++;
				}
				
				System.out.println("This is the number of devices for Froeschl " + devices.size());
			}
			

			catch (TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			logger.info("try to stop CM interface");
			
			try {
				clientInfo.destroy();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			logger.info("Froeschl ActuatorclientImpl could not load devices. Reading the JSON returns Null");
		}
	}

	public ArrayList<DeviceContainer> getDevices() {
		return devices;
	}

	@Override
	public void onDoubleCommand(DoubleCommand command, DeviceId dev) {
		logger.debug("Received Doublecommand "+command.getValue()+" for " + dev.getDevid());
		logger.debug("Froeschl does not recieve commands");
	}
	

	private void sendNewDeviceEvents() {
		for (DeviceContainer dev : devices) {
			try {
				logger.debug("Dev:" + dev.getDeviceId().getDevid() + " -> "
						+ clientId);
				System.out.println(dev.getHrName());
				master.sendDeviceEvent(new DeviceEvent(dev), this.clientId);
			} catch (TimeoutException e) {
				logger.debug("Failed to send " + dev.getDeviceId()
						+ " to master");
			}
		}
	}

	public void setMaster(IActuatorMaster master) {
		this.master = master;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public void setPollFrequency(int pollFrequency) {
		this.pollFrequency = pollFrequency;
	}

	public void setWrapperTag(String wrapperTag) {
		this.wrapperTag = wrapperTag;
	}
	
	public JSONReaderFroeschl getReader() {
		return reader;
	}
}
