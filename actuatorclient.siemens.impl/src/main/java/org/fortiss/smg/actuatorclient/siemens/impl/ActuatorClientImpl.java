package org.fortiss.smg.actuatorclient.siemens.impl;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.fortiss.smg.actuatormaster.api.IActuatorClient;
import org.fortiss.smg.actuatormaster.api.IActuatorMaster;
import org.fortiss.smg.containermanager.api.devices.DeviceContainer;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.smgschemas.commands.DoubleCommand;
import org.slf4j.LoggerFactory;

import ch.iec._61400.ews._1.AssociateRequest;
import ch.iec._61400.ews._1.AssociateResponse;
import ch.iec._61400.ews._1.GetLogicalDeviceDirectoryRequest;
import ch.iec._61400.ews._1.GetLogicalDeviceDirectoryResponse;
import ch.iec._61400.ews._1.GetServerDirectoryRequest;
import ch.iec._61400.ews._1.GetServerDirectoryResponse;
import ch.iec._61400.ews._1.TFC;
import ch.iec._61400.ews._1.TObjectClass;
import ch.iec._61400.ews._1_0.ServicePortType;

public class ActuatorClientImpl implements IActuatorClient {

	private static org.slf4j.Logger logger = LoggerFactory
			.getLogger(ActuatorClientImpl.class);
	private IActuatorMaster master;
	private String clientId;
	private List<ScheduledExecutorService> executorList;
	private int pollFrequency;
	private String host;
	private String port;
	private String username;
	private String password;
	private String assocID; 
	private String wrapperTag;
	private boolean isAlive = false;

	private ServicePortType service;
	
	/* more to add in case they are really needed !!! */
	private List<String> logicalNodeSubstringStarts = (Arrays.asList("MMXU1")); //, "MMTR"));
	private List<String> availableLogicalDevice = new ArrayList<String>();

	public List<String> getAvailableLogicalDevice() {
		return availableLogicalDevice;
	}

	private List<TFC> usedTypeList = (Arrays.asList(TFC.MX));

	Map<String, DeviceContainer> devices = new HashMap<String, DeviceContainer>();
	

	public ActuatorClientImpl(String host, String port, String wrapperTag,
			int pollFreq, String username, String password) {
		getClass().getClassLoader();
		this.host = host;
		this.port = port;
		this.wrapperTag = wrapperTag;
		this.username = username;
		this.password = password;
		
		loadStaticDevs(wrapperTag);
		
		pollFrequency = pollFreq;
		
		executorList = new ArrayList<ScheduledExecutorService>();
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public synchronized void activate() {

		//sendNewDeviceEvents();
		this.service = SiemensClientNew.getService(this.host, this.port);
		
		logger.debug("Got service {}", service);
		assocID = associate();
		if (assocID == null) {
			logger.debug("Activation of SiemensWrapper failed !");
			return;
		}
		logger.debug("SiemensWrapper - Updater: run: AssocID {}", assocID);
		
		
		availableLogicalDevice = fetchLogicalDevices();
		SiemensDeviceCreator deviceCreator = new SiemensDeviceCreator(this, "");
		
		for (String lDevice : availableLogicalDevice) {
			deviceCreator.setLogicalDevice(lDevice);
			Map<String, DeviceContainer> toAdd = deviceCreator.getDevices(); 
			for (String key : toAdd.keySet()) {
				this.devices.put(key, toAdd.get(key));
				logger.info("Got new Device: " + key );
			}
		}
	
		
		/* For every Logical Device - in this case one C1 and C1-C4 - create separate threads 
		 * probably not good 2014/12 use a single one again 
		 */
		//int delay = 0;
		//for (String lDevice : availableLogicalDevice) {
			ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
			/*String assocID = associate();
			while (assocID == null) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				assocID = associate();
			}*/
			//executor.scheduleAtFixedRate(new SiemensLooper(this, lDevice, assocID ), delay,
			//			getPollFrequency(), TimeUnit.SECONDS);
			executor.scheduleAtFixedRate(new SiemensLooper(this, availableLogicalDevice, assocID ), 0,
					getPollFrequency(), TimeUnit.SECONDS);

			//delay=delay+1;
			logger.info("SiemensWrapper: activate: started Thread for Logical Devices: " + availableLogicalDevice);
			
			executorList.add(executor);
		//}
			this.isAlive = true;
		
	}

	public ServicePortType getService() {
		return service;
	}

	public synchronized void deactivate() {
		for ( ScheduledExecutorService executor : executorList) {
			executor.shutdown();
			try {
				executor.awaitTermination(1, TimeUnit.MINUTES);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	
	public List<String> getLogicalNodeSubstringStarts() {
		return logicalNodeSubstringStarts;
	}

	public void setLogicalNodeSubstringStarts(
			List<String> logicalNodeSubstringStarts) {
		this.logicalNodeSubstringStarts = logicalNodeSubstringStarts;
	}
		
	public List<TFC> getUsedTypeList() {
		return usedTypeList;
	}

	public void setUsedTypeList(List<TFC> usedTypeList) {
		this.usedTypeList = usedTypeList;
	}
	
	public String getClientId() {
		return clientId;
	}

	public Map<String, DeviceContainer> getDevices() {
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
		return this.isAlive;
	}

	private void loadStaticDevs(String wraperTagN) {
		
		/*labConSpecControlCoffeeMaker = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"coffeemaker_plug", wraperTagN), wraperTagN
						+ ".powerplug", SIUnitType.NONE, DeviceType.Powerplug,
				true, true, 0, 1, 1, -1, 1, 1, "CoffeeMaker PowerPlug");
		
		labConSpecConsumptionCoffeeMaker = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"coffeemaker_powermeter", wraperTagN), wraperTagN
						+ ".powermeter", SIUnitType.W, DeviceType.Powermeter,
				false, true, 0, Double.MAX_VALUE, 1, "CoffeeMaker Consumption [W]");
		
		labConSpecMultiSensorTemperature = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"multisensor_temperature", wraperTagN), wraperTagN
						+ ".multisensor", SIUnitType.CELSIUS,
				DeviceType.Temperature, false, true, -50, 150, 0.1,
				"Multisensor [C]");
		
		labConSpecMultiSensorBrightness = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"multisensor_brightness", wraperTagN), wraperTagN
						+ ".multisensor", SIUnitType.LUX, DeviceType.Brightness,
				false, true, 0, 1000, 1, "Multisensor [LUX]");
		
		devices.add(labConSpecControlCoffeeMaker);
		devices.add(labConSpecConsumptionCoffeeMaker);
		devices.add(labConSpecMultiSensorTemperature);
		devices.add(labConSpecMultiSensorBrightness);
*/

	}

	@Override
	public void onDoubleCommand(DoubleCommand command, DeviceId dev) {
		logger.debug("Received Doublecommand "+command.getValue()+" for " + dev.getDevid());
		
	}

/*	private void sendNewDeviceEvents() {
		for (DeviceContainer dev : devices.values()) {
			try {
				logger.debug("Dev:" + dev.getDeviceId().getDevid() + " -> "
						+ clientId);
				master.sendDeviceEvent(dev, this.clientId);
			} catch (TimeoutException e) {
				logger.debug("Failed to send " + dev.getDeviceId()
						+ " to master");
			}
		}
	}
*/
	
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
	
	public String associate() {
		AssociateRequest req = new AssociateRequest();
		req.setMaxMessageSize(4096);
		req.setUserName(this.username);
		req.setPassword(this.password);
		req.setUUID(UUID.randomUUID().toString());
		AssociateResponse resp = new AssociateResponse();
		resp = this.service.associate(req);
		
		
		if (resp.getServiceError() != null) {
			logger.error(
					"could not associate with powerbridge: {}. Leaving Wrapper inactive...",
					resp.getServiceError());
			return null;
		}
		
		
		
		
		return resp.getAssocID();
	}
	
	/**
	 * Fetches logical devices from the server. in an id "C1/MMXU1", C1 would be
	 * the Logical device reference or LDRef
	 * 
	 * @return a list of LDRefs or an empty list in case of an error.
	 */
	private List<String> fetchLogicalDevices() {
		GetServerDirectoryRequest req = new GetServerDirectoryRequest();
		req.setAssocID(assocID);
		req.setUUID(UUID.randomUUID().toString());
		req.setObjClass(TObjectClass.LD);
		GetServerDirectoryResponse resp = service.getServerDirectory(req);
		if (resp.getServiceError() != null) {
			logger.error("SiemensWrapper: fetchLogicalDevices: Error when fetching LDRefs: "
					+ resp.getServiceError());
			return Collections.emptyList();
		}
		//System.out.println("errors: " + resp.getServiceError());
		List<String> ldRefs = new ArrayList<String>();
		ldRefs = resp.getLDRef();
		logger.debug("SiemensWrapper: fetchLogicalDevices: found the following Logical Devices: " + ldRefs);
		if (ldRefs == null) {
			return Collections.emptyList();
		} else {
			return ldRefs;
		}
	}

	List<String> fetchLogicalNodes(String ldref, ServicePortType service, String assocID) {
		GetLogicalDeviceDirectoryRequest req = new GetLogicalDeviceDirectoryRequest();
		req.setAssocID(assocID);
		req.setUUID(UUID.randomUUID().toString());
		req.setLDRef(ldref);
		service.getLogicalDeviceDirectory(req);
		GetLogicalDeviceDirectoryResponse resp = service
				.getLogicalDeviceDirectory(req);
		if (resp.getServiceError() != null && !resp.getServiceError().isEmpty()) {
			logger.error("SiemensWrapper: fetchLogicalNodes: error when fetching Logical Nodes for {}: {}", ldref,
					resp.getServiceError());
			return Collections.emptyList();
		}
		logger.debug("SiemensWrapper: fetchLogicalNodes: found logical nodes for LDRef \"{}\": {}", ldref,
				resp.getLNRef());
		if (resp.getLNRef() == null) {
			return Collections.emptyList();
		} else {
			return resp.getLNRef();
		}

	}

	public String getAssociate() {
		return this.assocID;
	}
	
	public void setAssociate(String assocID) {
		this.assocID = assocID;
	}

}
