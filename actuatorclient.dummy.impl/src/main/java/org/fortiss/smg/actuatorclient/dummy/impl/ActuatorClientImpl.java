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
	
	
	
	
	public double[] consumptionArray = {3696,3497,3159,3297,3724,3106,3238,2523,3618,3308,3138,3361,3827,3777,3297,3059,2587,3925,3393,3247,2402,3049,2959,4402,3449,3253,4546,2457,4215,3423,4971,5091,4090,6107,5451,5578,8512,7292,9281,8612,9902,8039,8194,9248,9571,9118,8921,7176,7774,9087,7880,9574,8275,8591,7663,9494,8609,7927,10578,7701,12075,8316,8320,7921,7433,8526,7101,5932,8927,8785,6392,8734,7466,8652,4079,3810,3437,4504,3362,4031,3807,6350,3954,4805,3761,3253,3144,2649,3798,3735,2336,3110,2584,3414,4501,3394};
	public double[] generationArray = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,8,24,45,62,161,440,432,569,742,936,1467,579,1920,2231,2428,2630,2785,2976,3109,3294,3345,3508,3600,3773,1581,3773,3773,3773,729,3771,3771,3772,3773,3771,1048,3773,3772,3770,3642,3446,3366,3259,3085,2866,2724,2516,2319,2083,1864,1605,1262,1014,741,469,275,126,28,4,0,0,0,0,0,0,0,0,0,0,0,0,0};
	private int arrayCounter = 0;
	
	
	
	private double consumption = 0.0;
	private double generationpercentage = 0.0;
	
	private double batterypercentage = 1.0 *250; //for the arrayexample x 500; // 1 = 100%
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
				ContainerManagerQueueNames.getContainerManagerInterfaceQueryQueue(), 5000);

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


		this.gridconnected = new Switcher(1.0);
		this.generation = new Switcher(0.0);
		this.energyfrombatteryrequest = new Switcher(0.0);
		

		
	}

	
	public synchronized void activate() {
		

	
	
	
	
		sendNewDeviceEvents();
		
		executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(new DummyLooper(this), 0,
				getPollFrequency(), TimeUnit.SECONDS);
		
		/*
		 * this class simulates calls which are usually issued via the containermanager
		 */
		dummyswitcher = Executors.newSingleThreadScheduledExecutor();
		dummyswitcher.scheduleAtFixedRate(new DummySwitcher(this), 10,
				getPollFrequency(), TimeUnit.SECONDS);
		
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
		} 
		else if (dev.getDevid().contains("switchgridconnected")) {
			gridconnected.setStatus(command.getValue());
		}
		else if (dev.getDevid().contains("energyfrombatteryrequest")) {
			energyfrombatteryrequest.setStatus(command.getValue());
		} 



	}
	
	
	private void sendNewDeviceEvents() {
		for (DeviceContainer dev : devices) {
			try {
			
				logger.info("Try to send " + dev.getDeviceId()
						+ " to master");
				this.master.sendDeviceEvent(new DeviceEvent(dev), this.clientId);
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
		dummyGenerationMeter.setHrName("Dummy Generation [W]");


		DeviceContainer dummyBattery = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"dummy_battery", wrapperTag),
				wrapperTag, containerManagerClient.getDeviceSpecData(140) 
				);		
		dummyBattery.setHrName("Dummy Battery [%]");
		
		DeviceContainer dummySwitchGridConnected = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"dummy_switchgridconnected", wrapperTag),
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
		
		
		devices.add(dummyConsumptionMeter);
		devices.add(dummyGenerationMeter);
		devices.add(dummyBattery);
		devices.add(dummySwitchGridConnected);
		devices.add(dummySwitchGeneration);

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

	public Double getBattery() {
		return batterycapacitycurrent;
	}


	public void setBattery(Double batterycapacitycurrent) {
		this.batterycapacitycurrent = batterycapacitycurrent;
	}

	public double[] getEnergyvalues() {
		arrayCounter++;
		arrayCounter = arrayCounter%96;
		
		double[] result = {consumptionArray[arrayCounter],generationArray[arrayCounter]};
		return 	result;
	}
	
	
}
