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
	private ScheduledExecutorService dummyswitcher;
	private int pollFrequency;
	private String host;
	private String wrapperTag;
	private ContainerManagerInterface broker;
	
	/*
	 * Values provided by this component
	 */

	/*
	 * switches - values provided from the simulation
	 */
	public Switcher gridconnected;
	public Switcher energyfrombatteryrequest;
	public Switcher generation;
	
	
	
	private double consumption = 0.0;
	private double generationpercentage = 0.0;
	
	private double batterypercentage = 1.0; // 1 = 100%
	public static double batterycapacitymax = 100000.0; // 100k max
	private double batterycapacitycurrent = 100000.0; // 100k max
	
	
	public ContainerManagerInterface containerManagerClient = null;
	private DefaultProxy<ContainerManagerInterface> clientInfo = null;

	
	ArrayList<DeviceContainer> devices = new ArrayList<DeviceContainer>();

	public ActuatorClientImpl(String host, String port, String wrapperTag,
			int pollFreq, String username, String password) {

		
		/* Test one Connection to CM */
		
		//DefaultProxy<ContainerManagerInterface> 
		clientInfo = new DefaultProxy<ContainerManagerInterface>(
				ContainerManagerInterface.class,
				ContainerManagerQueueNames.getContainerManagerInterfaceQueryQueue(), 10000);

		logger.info("try to init CM interface");
		
		try {
			containerManagerClient = clientInfo.init();
		}
		catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		} catch (TimeoutException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
		
		
		this.host = host;
		this.wrapperTag = wrapperTag;
		loadStaticDevs(wrapperTag);
		this.pollFrequency = pollFreq;


		this.generation = new Switcher(0.0);
		this.energyfrombatteryrequest = new Switcher(0.0);
		this.gridconnected = new Switcher(0.0);

		
	}

	
	public synchronized void activate() {
		

	
	
	
	
		sendNewDeviceEvents();
		
		executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(new DummyLooper(this), 0,
				getPollFrequency(), TimeUnit.SECONDS);
		
		/*
		 * this class simulates calls which are usually issued via the containermanager
		 */
		/*dummyswitcher = Executors.newSingleThreadScheduledExecutor();
		dummyswitcher.scheduleAtFixedRate(new DummySwitcher(this), 10,
				getPollFrequency(), TimeUnit.SECONDS);*/

	}




	public synchronized void deactivate() {
		
		try {
			clientInfo.destroy();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		executor.shutdown();
		dummyswitcher.shutdown();
		try {
			executor.awaitTermination(1, TimeUnit.MINUTES);
			dummyswitcher.awaitTermination(1, TimeUnit.MINUTES);
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
		logger.info("Executing: Command " + command.getValue() + " was sent to "
				+ dev.toString() + "(" + dev.getDevid() + ")");
		
		if (dev.getDevid().contains("generation")) {
			logger.info("Updating Generation " + command.getValue() );
			generation.setStatus(command.getValue()*500.0);			
		} else if (dev.getDevid().contains("energyfrombatteryrequest")) {
			energyfrombatteryrequest.setStatus(command.getValue());
		} else if (dev.getDevid().contains("gridconnected")) {
			gridconnected.setStatus(command.getValue());
		}



	}
	
	
	private void sendNewDeviceEvents() {
		for (DeviceContainer dev : devices) {
			try {
			
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
//		DefaultProxy<ContainerManagerInterface> clientInfo = new DefaultProxy<ContainerManagerInterface>(
//				ContainerManagerInterface.class,
//				ContainerManagerQueueNames.getContainerManagerInterfaceQueryQueue(), 10000);
//
//		logger.info("try to init CM interface");
//		ContainerManagerInterface containerManagerClient = null;
		try {
//			containerManagerClient = clientInfo.init();
			
		/*
		 * Device Spec from the Solar Log Generator Devices
		 */
		DeviceContainer dummyConsumptionMeter = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"dummy_consumption", wrapperTag), wrapperTag
						, containerManagerClient.getDeviceSpecData(101));
		dummyConsumptionMeter.setHrName("Dummy Consumption [W]");

		DeviceContainer dummyGenerationMeter = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"dummy_generation", wrapperTag), wrapperTag
						, containerManagerClient.getDeviceSpecData(111));
		dummyConsumptionMeter.setHrName("Dummy Generation [W]");


		DeviceContainer dummyBattery = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"dummy_battery", wrapperTag),
				wrapperTag, containerManagerClient.getDeviceSpecData(140) 
				);		

		DeviceContainer dummySwitchGridConnected = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"dummy_gridconnected", wrapperTag),
				wrapperTag, containerManagerClient.getDeviceSpecData(145) 
				);	
	

		DeviceContainer dummySwitchEnergyfromBatteryRequest = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"dummy_switchenergyfrombatteryrequest", wrapperTag),
				wrapperTag, containerManagerClient.getDeviceSpecData(145) 
				);	
		
		
		DeviceContainer dummySwitchGeneration = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"dummy_switchgeneration", wrapperTag),
				wrapperTag, containerManagerClient.getDeviceSpecData(145) 
				);
		
		DeviceContainer dummyEnoceanLight = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"001E4E19-5", wrapperTag), wrapperTag
						, containerManagerClient.getDeviceSpecData(5));
		
		DeviceContainer dummyEnoceanAccupancy = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"0003380A-153", wrapperTag), wrapperTag
						, containerManagerClient.getDeviceSpecData(153));
		
		DeviceContainer dummyEnoceanInsideBrightness = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"001E657C-5", wrapperTag), wrapperTag
						, containerManagerClient.getDeviceSpecData(5));
		
		DeviceContainer dummyEnoceanBlindes = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"001E4DD0-136", wrapperTag), wrapperTag
						, containerManagerClient.getDeviceSpecData(136));
		
		DeviceContainer dummyEnoceanInsideTemp = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"010066F4-13", wrapperTag), wrapperTag
						, containerManagerClient.getDeviceSpecData(13));
		
		DeviceContainer dummyEnoceanWindow = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"01003F84-151", wrapperTag), wrapperTag
						, containerManagerClient.getDeviceSpecData(151));
		
		DeviceContainer dummyEnoceanOutsideBrightness = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"000321E6-5", wrapperTag), wrapperTag
						, containerManagerClient.getDeviceSpecData(5));
		
		DeviceContainer dummyEnoceanOutsideTemp = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"0003B078-13", wrapperTag), wrapperTag
						, containerManagerClient.getDeviceSpecData(13));
		
		devices.add(dummyConsumptionMeter);
		devices.add(dummyGenerationMeter);
		devices.add(dummyBattery);
		devices.add(dummySwitchGridConnected);
		devices.add(dummySwitchGeneration);
		devices.add(dummyEnoceanOutsideTemp);
		devices.add(dummyEnoceanOutsideBrightness);
		devices.add(dummyEnoceanWindow);
		devices.add(dummyEnoceanInsideTemp);
		devices.add(dummyEnoceanBlindes);
		devices.add(dummyEnoceanLight);
		devices.add(dummyEnoceanAccupancy);
		devices.add(dummyEnoceanInsideBrightness);

//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		
//		
//		try {
//			clientInfo.destroy();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}
	
	public double getConsumption() {
		return consumption;
	}


	public void setConsumption(Double consumption) {
		this.consumption = consumption;
	}

	public double getGeneration() {
		return generation.getStatus();
	}


	public void setGeneration(Double generation) {
		this.generation.setStatus(generation);;
	}


	public Double getBatteryPercentage() {
		return batterypercentage;
	}


	public void setBatteryPercentage(Double batterypercentage) {
		this.batterypercentage = batterypercentage;
	}

	public double getGridConnected() {
		return gridconnected.getStatus();
	}


	public void setGridConnected(double gridconnected) {
		this.gridconnected.setStatus(gridconnected);
	}
	
	public Double getBattery() {
		return batterycapacitycurrent;
	}


	public void setBattery(Double batterycapacitycurrent) {
		this.batterycapacitycurrent = batterycapacitycurrent;
	}


	
	
}
