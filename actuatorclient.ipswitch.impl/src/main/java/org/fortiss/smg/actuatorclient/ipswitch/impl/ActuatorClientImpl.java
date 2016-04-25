package org.fortiss.smg.actuatorclient.ipswitch.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
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
import org.fortiss.smg.containermanager.api.devices.SIDeviceType;
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
	private String path;
	private String protocol;
	private String username;
	private String password;
	private String wrapperTag;


	ArrayList<DeviceContainer> devices = new ArrayList<DeviceContainer>();

	private String DEV_4_COMPLETE ="IPSwitch_Temperature" ;	
	private String DEV_3_COMPLETE ="IPSwitch_Counter3";
	private String DEV_2_COMPLETE ="IPSwitch_Counter2";
	private String DEV_1_COMPLETE ="IPSwitch_Counter1";

	public ActuatorClientImpl(String protocol, String host, String path, String port, String wrapperTag,
			int pollFreq, String username, String password) {
		this.host = host;
		this.path = path;
		this.protocol = protocol;
		this.wrapperTag = wrapperTag;
		this.username = username;
		this.password = password;
		loadStaticDevs(wrapperTag);		
		pollFrequency = pollFreq;
	}

	public synchronized void activate() {
		sendNewDeviceEvents();

		executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(new IpswitchLooper(this), 0,
				getPollFrequency(), TimeUnit.SECONDS);
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
		return wrapperTag;
	}

	@Override
	public boolean isComponentAlive() {
		return true;
	}
	
	public String getHost() {
		return host;
	}
	
	public String getPath() {
		return path;
	}

	public String getProtocol() {
		return protocol;
	}
	
	private void loadStaticDevs(String wraperTagN) {
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
			//Get device specification
			HashMap<String, Object> deviceSpecPowerMeterAggregated = containerManagerClient.getDeviceSpecData(104);
			HashMap<String, Object> deviceSpecPowermeter = containerManagerClient.getDeviceSpecData(101);
			HashMap<String, Object> deviceSpecThermometer = containerManagerClient.getDeviceSpecData(13);
			//Build the devices
			devices.add(new DeviceContainer(new org.fortiss.smg.containermanager.api.devices.DeviceId(DEV_1_COMPLETE, wraperTagN), wraperTagN,  deviceSpecPowerMeterAggregated));
			devices.add(new DeviceContainer(new org.fortiss.smg.containermanager.api.devices.DeviceId(DEV_2_COMPLETE, wraperTagN), wraperTagN,  deviceSpecPowermeter));
			devices.add(new DeviceContainer(new org.fortiss.smg.containermanager.api.devices.DeviceId(DEV_3_COMPLETE, wraperTagN), wraperTagN,  deviceSpecPowermeter));
			devices.add(new DeviceContainer(new org.fortiss.smg.containermanager.api.devices.DeviceId(DEV_4_COMPLETE, wraperTagN), wraperTagN,  deviceSpecThermometer));	
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 	
	}

	public ArrayList<DeviceContainer> getDevices() {
		return devices;
	}	
	
	@Override
	public void onDoubleCommand(DoubleCommand command, DeviceId dev) {
		logger.debug("Received Doublecommand "+command.getValue()+" for " + dev.getDevid());
	}
	
	private void sendNewDeviceEvents() {
		for (DeviceContainer dev : devices) {
			try {
				logger.debug("Dev:" + dev.getDeviceId().getDevid() + " -> "
						+ clientId);
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
}
