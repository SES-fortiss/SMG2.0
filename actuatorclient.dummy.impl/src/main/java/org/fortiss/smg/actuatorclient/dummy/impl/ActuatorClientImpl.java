package org.fortiss.smg.actuatorclient.dummy.impl;

import java.io.IOException;
import java.util.ArrayList;
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
	private String wrapperTag;
	private ContainerManagerInterface broker;
	
	/*
	 * Values provided by this component
	 */
	private Double counsumption = 0.0;
	private Double temperature = 20.0;
	private Double brightness = 10.0;
	private Double battery = 100.0;
	private Double switchdevice = 0.0;
	
	
	
	
	ArrayList<DeviceContainer> devices = new ArrayList<DeviceContainer>();

	public ActuatorClientImpl(String host, String port, String wrapperTag,
			int pollFreq, String username, String password) {

		this.host = host;
		this.wrapperTag = wrapperTag;
		loadStaticDevs(wrapperTag);
		this.pollFrequency = pollFreq;


	}

	
	public synchronized void activate() {
		sendNewDeviceEvents();
		
		executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(new DummyLooper(this), 0,
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
	
	public String doSomething(String s) {
		return "Hello smg";
	}

	@Override
	public boolean isComponentAlive() {
		/* implement self test */
		return true;
	}

	@Override
	public void onDoubleCommand(DoubleCommand command, DeviceId dev) {
		logger.info("Command " + command.getValue() + " was sent to "
				+ dev.toString());

	}
	
	
	private void sendNewDeviceEvents() {
		for (DeviceContainer dev : devices) {
			try {
				//master.sendDeviceEvent(dev, this.clientId);
				logger.info("Try to send " + dev.getDeviceId()
						+ " to master");
				master.sendDeviceEvent(new DeviceEvent(dev), this.clientId);
				logger.info("Sent " + dev.getDeviceId()
						+ " to master");
				
				
			} catch (TimeoutException e) {
				logger.debug("Failed to send " + dev.getDeviceId()
						+ " to master");
			}
		}
	}
	
	public String getClientId() {
		return this.clientId;
	}

	public List<DeviceContainer> getDeviceSpecs() {
		return this.devices;
	}

	public IActuatorMaster getMaster() {
		return this.master;
	}

	public int getPollFrequency() {
		return this.pollFrequency;
	}

	public String getWrapperTag() {
		return this.wrapperTag;
	}
	
	public String getHost() {
		return this.host;
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
	
	
	private void loadStaticDevs(String wraperTag) {
		DefaultProxy<ContainerManagerInterface> clientInfo = new DefaultProxy<ContainerManagerInterface>(
				ContainerManagerInterface.class,
				ContainerManagerQueueNames.getContainerManagerInterfaceQueryQueue(), 10000);

		logger.info("try to init CM interface");
		ContainerManagerInterface containerManagerClient = null;
		try {
			containerManagerClient = clientInfo.init();
			
		/*
		 * Device Spec from the Solar Log Generator Devices
		 */
		DeviceContainer dummyConsumptionMeter = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"dummy_consumption_watt", wrapperTag), wrapperTag
						+ ".solar");
		
		dummyConsumptionMeter.setRange(0,Integer.MAX_VALUE, 1);
		dummyConsumptionMeter.setHrName("Dummy Consumption [W]");
		dummyConsumptionMeter.setDeviceType(SIDeviceType.ConsumptionPowermeter);
		
		DeviceContainer dummyTemp = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"dummy_temperature", wrapperTag), wrapperTag
						, containerManagerClient.getDeviceSpecData(13)
				);
		
		
		DeviceContainer dummyBrightness = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"dummy_brightness", wrapperTag),
				wrapperTag, containerManagerClient.getDeviceSpecData(5) 
				);

		DeviceContainer dummyBattery = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"dummy_battery", wrapperTag),
				wrapperTag, containerManagerClient.getDeviceSpecData(140) 
				);		

		DeviceContainer dummySwitch = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"dummy_switch", wrapperTag),
				wrapperTag, containerManagerClient.getDeviceSpecData(145) 
				);		
		
		
		devices.add(dummyConsumptionMeter);
		devices.add(dummyTemp);
		devices.add(dummyBrightness);
		devices.add(dummyBattery);
		devices.add(dummySwitch);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		try {
			clientInfo.destroy();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public Double getConsumption() {
		return counsumption;
	}


	public void setConsumption(Double counsumption) {
		this.counsumption = counsumption;
	}



	public Double getTemperature() {
		return temperature;
	}


	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}


	public Double getBrightness() {
		return brightness;
	}


	public void setBrightness(Double brightness) {
		this.brightness = brightness;
	}


	public Double getBattery() {
		return battery;
	}


	public void setBattery(Double battery) {
		this.battery = battery;
	}


	public Double getSwitchdevice() {
		return switchdevice;
	}


	public void setSwitchdevice(Double switchdevice) {
		this.switchdevice = switchdevice;
	}

	
	
}
