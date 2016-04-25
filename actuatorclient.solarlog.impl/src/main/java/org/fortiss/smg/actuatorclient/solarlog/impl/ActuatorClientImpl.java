package org.fortiss.smg.actuatorclient.solarlog.impl;

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
	
	ArrayList<DeviceContainer> devices = new ArrayList<DeviceContainer>();
	
	public ActuatorClientImpl(String host, String port,String wrapperTag, int pollFreq, String username, String password) {
	
		this.host = host;
		this.wrapperTag = wrapperTag;
		loadStaticDevs(wrapperTag);
		this.pollFrequency = pollFreq;
		


	}

	public synchronized void activate() {
		sendNewDeviceEvents();
		
		executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(new SolarLooper(this), 0,
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

	@Override
	public boolean isComponentAlive() {
		return true;
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
		DeviceContainer solarSpecGenerator = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"solar_generator_watt", wrapperTag), wrapperTag
						+ ".solar");
		
		solarSpecGenerator.setRange(0,Integer.MAX_VALUE, 1);
		solarSpecGenerator.setHrName("Solar Generator [W]");
		solarSpecGenerator.setDeviceType(SIDeviceType.ProductionPowermeter);
		
		DeviceContainer solarSpecGeneratorTemp = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"solar_generator_temperature", wrapperTag), wrapperTag
						, containerManagerClient.getDeviceSpecData(13)
				);
		
		System.out.println(containerManagerClient.getDeviceSpecData(137));
				/*
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"solar_generator_temperature", wrapperTag), wrapperTag
						+ ".solar", 	SIDeviceType.Temperature, false, true, -100, 200, 1,
				"Solar Generator [C]");*/
		

		
		//System.out.println(solarSpecGeneratorTemp.getDeviceType().getType());
		
		
		
		DeviceContainer solarSpecGeneratorVoltage = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"solar_generator_voltage", wrapperTag),
				wrapperTag + ".solar");
		
		solarSpecGeneratorVoltage.setRange(0,1.0E5);
		solarSpecGeneratorVoltage.setRangeStep(1);
		solarSpecGeneratorVoltage.setHrName("Solar Generator udc1 [V]");
		solarSpecGeneratorVoltage.setDeviceType(SIDeviceType.ProductionVoltmeter);
		
		DeviceContainer solarSpecGeneratorSavings = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"solar_generator_savings", wrapperTag), wrapperTag
						+ ".solar");
		
		solarSpecGeneratorSavings.setRange(0,Double.MAX_VALUE, 0.000001);
		solarSpecGeneratorSavings.setHrName("Solar Generator Savings [KG]");
		solarSpecGeneratorSavings.setDeviceType(SIDeviceType.Balance);
		
		
		/*
		 * Device Spec from the Solar Log Feed-in Devices
		 */
		DeviceContainer solarSpecFeedIn = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"solar_feed-in_watt",wrapperTag), wrapperTag
						+ ".solar");
		
		solarSpecFeedIn.setRange(0,Integer.MAX_VALUE, 1);
		solarSpecFeedIn.setHrName("Solar Feed-in [W]");
		solarSpecFeedIn.setDeviceType(SIDeviceType.FeedPowerMeter);
		
		
		DeviceContainer solarSpecFeedInTemp = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"solar_feed-in_temperature", wrapperTag), wrapperTag
						+ ".solar");
		
		solarSpecFeedInTemp.setRange(-100,200, 1);
		solarSpecFeedInTemp.setHrName("Solar Feed-in [C]");
		solarSpecFeedInTemp.setDeviceType(SIDeviceType.Temperature);
		
		
		DeviceContainer solarSpecFeedInDaySum = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"solar_feed-in_sum", wrapperTag), wrapperTag
						+ ".solar");
		
		solarSpecFeedInDaySum.setRange(0,Integer.MAX_VALUE, 1);
		solarSpecFeedInDaySum.setHrName("Solar Day Sum [WH]");
		solarSpecFeedInDaySum.setDeviceType(SIDeviceType.ProductionPowermeterAggregated);
		

		DeviceContainer solarSpecInventorStatus = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"solar_inventor_status", wrapperTag), wrapperTag
						+ ".solar");
		
		solarSpecInventorStatus.setHrName("Solar Inventor Status");
		//// type should be replaced with proper SI device type . Not exist yet 
		solarSpecInventorStatus.setDeviceType(SIDeviceType.Calculator);
		
		devices.add(solarSpecGenerator);
		devices.add(solarSpecGeneratorTemp);
		devices.add(solarSpecGeneratorVoltage);
		devices.add(solarSpecGeneratorSavings);
		devices.add(solarSpecFeedIn);
		devices.add(solarSpecFeedInTemp);
		devices.add(solarSpecFeedInDaySum);
		devices.add(solarSpecInventorStatus);
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

	@Override
	public void onDoubleCommand(DoubleCommand command, DeviceId dev) {
		logger.debug("Received Doublecommand for " + dev.getDevid());

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
	
	public void setPollFrequency(int pollFrequency) {
		this.pollFrequency = pollFrequency;
	}

	public void setMaster(IActuatorMaster master) {
		this.master = master;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public void setWrapperTag(String wrapperTag) {
		this.wrapperTag = wrapperTag;
	}



}
