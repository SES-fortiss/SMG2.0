package org.fortiss.smg.actuatorclient.enocean.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.actuatorclient.enocean.impl.model.Actor;
import org.fortiss.smg.actuatorclient.enocean.impl.persistence.WrapperPersistor;
import org.fortiss.smg.actuatormaster.api.IActuatorClient;
import org.fortiss.smg.actuatormaster.api.IActuatorMaster;
import org.fortiss.smg.actuatormaster.api.events.DeviceEvent;
import org.fortiss.smg.containermanager.api.devices.DeviceContainer;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
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
	private int port;
	private String wrapperTag;
	// private EnOceanCommunicator communicator;
	ArrayList<String> enOceanDeviceIds = new ArrayList<String>();


	ArrayList<DeviceContainer> devices = new ArrayList<DeviceContainer>();

	private EnOceanLooper looper;

	public ActuatorClientImpl(String host, int port, String wrapperTag, int pollFreq) {

		this.wrapperTag = wrapperTag;
		this.pollFrequency = pollFreq;
		this.port = port;
		this.host = host;
	}

	public void connectToEncoean() {
		looper = new EnOceanLooper(this);
		WrapperPersistor persistor = new WrapperPersistor(this);
		persistor.createWrappersFromPersistency();
	}

	public synchronized void activate() {
		sendNewDeviceEvents();
		//		 executor = Executors.newSingleThreadScheduledExecutor();
		//		 executor.scheduleAtFixedRate(looper, 0, getPollFrequency(),
		//		 TimeUnit.SECONDS);
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

	public int getPort() {
		return this.port;
	}

	@Override
	public boolean isComponentAlive() {
		return true;
	}

	public List<DeviceContainer> getDeviceSpecs() {
		return devices;
	}

	public EnOceanLooper getLooperForTest() {
		return this.looper;
	}

	public List<String> getAvailableActorStrategies() {
		List<String> classNames = new ArrayList<String>();
		classNames
		.add("org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.actor.FortissBlindActorStrategy");
		classNames
		.add("org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.actor.Light1030ActorStrategy");
		classNames
		.add("org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.actor.Light5070ActorStrategy");
		classNames
		.add("org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.actor.HeatingActorStrategy");
		classNames
		.add("org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.actor.SteckdosenleisteActorStrategy");
		classNames
		.add("org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.actor.GenericPushRelease1030SwitchActorStrategy");
		return classNames;
	}

	public List<String> getAvailableSensorStrategies() {
		List<String> classNames = new ArrayList<String>();
		classNames
		.add("org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor.BSCsOTSSensorStrategy");
		classNames
		.add("org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor.FAH60SensorStrategy");
		classNames
		.add("org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor.FBH55SensorStrategy");
		classNames
		.add("org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor.FT4DoubleRockerBooleanSensorStrategy");
		classNames
		.add("org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor.FT4SingleRockerStrategy");
		classNames
		.add("org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor.FTKSensorStrategy");
		classNames
		.add("org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor.FTR55DSensorStrategy");
		classNames
		.add("org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor.FWZ12SensorStrategy");
		classNames
		.add("org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor.SystestSensorStrategy");
		return classNames;
	}
	//
	//	public ArrayList<DeviceContainer> getDevices() {
	//		return devices;
	//	}


	@Override
	public void onDoubleCommand(DoubleCommand command, DeviceId dev) {
		logger.debug("Received Doublecommand for " + dev.getDevid());
		for (DeviceContainer device : devices) {
			if(device.getDeviceId().equals(dev)) {
				if(device.isBinary()){
					boolean valueBool = true;
					if (command.getValue() == 0.0) {
						valueBool = false;
					}
					this.looper.getActor(dev.toString()).setBoolean(valueBool, dev.toString(), 0, "", true, "");
				}
			}
		}
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
	//////////////// I add it !
	//	public void setEnoceanDeviceIds(DeviceId devId){
	//		enOceanDeviceIds.add(devId.toString());
	//	}

	public ArrayList<String> getEnOceanDeviceIds() {
		return enOceanDeviceIds;
	}

}
