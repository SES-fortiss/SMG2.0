/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.actuatorclient.enocean.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.actuatorclient.enocean.impl.model.Actor;
import org.fortiss.smg.actuatorclient.enocean.impl.persistence.WrapperPersistor;
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
	private int port;
	private String wrapperTag;
	// private EnOceanCommunicator communicator;
	ArrayList<String> enOceanDeviceIds;
	private Map<String, Actor> internatlIdActorMap = new HashMap<String, Actor>();

	ArrayList<DeviceContainer> devices = new ArrayList<DeviceContainer>();
	private DeviceContainer heatingActorStrategy;
	private DeviceContainer genericPushRelease3010SwitchActorStrategy;
	private DeviceContainer light1030ActorStrategy;
	private DeviceContainer light5070ActorStrategy;
	private DeviceContainer ourHardCodedActorStrategy;
	private DeviceContainer fortissBlindActorStrategy;
	private DeviceContainer steckdosenleisteActorStrategy;
	private DeviceContainer systestActorStrategy;
	// Sensors

	private DeviceContainer bSCsOTSSensors;
	private DeviceContainer fAH60SensorStrategy;
	private DeviceContainer fBH55Brightness_Room255SensorStrategy;
	private DeviceContainer fT4DoubleRockerBoolean1030_Room225SensorStrategy;
	private DeviceContainer fT4DoubleRockerBoolean5070_Room225SensorStrategy;
	private DeviceContainer fT4DoubleRockerBooleanHandControl5070_Room225SensorStrategy;
	private DeviceContainer fT4DoubleRockerBoolean1030_BlindsRoom225SensorStrategy;
	private DeviceContainer fT4DoubleRockerBooleanHandControl1030_Room225SensorStrategy;
	private DeviceContainer fT4DoubleRockerBooleanHandControl1030Blinds_Room225SensorStrategy;
	private DeviceContainer fT4DoubleRockerBoolean1030_DijkstraSensorStrategy;
	private DeviceContainer fT4DoubleRockerBoolean5070_DijkstraSensorStrategy;
	private DeviceContainer fT4DoubleRockerBoolean10_DijkstraSensorStrategy;
	private DeviceContainer fT4DoubleRockerBoolean50_DijkstraSensorStrategy;
	private DeviceContainer fT4DoubleRockerBooleanHandControl1030_DijkstraSensorStrategy;
	private DeviceContainer fT4DoubleRockerBooleanHandControl5070_DijkstraSensorStrategy;
	private DeviceContainer fT4DoubleRockerBooleanHandControl10_DijkstraSensorStrategy;
	private DeviceContainer fT4DoubleRockerBooleanHandControl50_DijkstraSensorStrategy;
	private DeviceContainer fBH55MotionDetectorSensorStrategy;
	private DeviceContainer fT4SingleRocker5070Strategy;
	private DeviceContainer fT4SingleRocker1030Strategy;
	private DeviceContainer fTR55DRoom255_2SensorStrategy;
	private DeviceContainer fTKRoom225_1SensorStrategy;
	private DeviceContainer fTR55DRoom255_3SensorStrategy;
	private DeviceContainer fWZ12Sensor1Strategy;
	private DeviceContainer fWZ12Sensor2Strategy;
	private DeviceContainer fWZ12Sensor3Strategy;
	private DeviceContainer fWZ12Sensor4Strategy;
	private DeviceContainer fTR55DRoom255_1SensorStrategy;
	private DeviceContainer hardCodedButton1Strategy;
	private DeviceContainer hardCodedButton2Strategy;
	private DeviceContainer systestSensorStrategy;
	private DeviceContainer ftDoubleRockerUSBSwitch1030Stratgy;
	private DeviceContainer ftDoubleRockerUSBSwitch5070Stratgy;
	private DeviceContainer fTKTest_1SensorStrategy;
	private DeviceContainer fTKRoom225_2SensorStrategy;
	private EnOceanLooper looper;
	
	public ActuatorClientImpl(IActuatorMaster master, String clientId,
			String host, int port, String wrapperTag, int pollFreq) {
		this.master = master;
		this.clientId = clientId;
		//this.communicator = new EnOceanCommunicator(this.host);
		this.wrapperTag = wrapperTag;
		loadStaticDevs(wrapperTag);
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

		// executor = Executors.newSingleThreadScheduledExecutor();

		// executor.scheduleAtFixedRate(looper, 0, getPollFrequency(),
		// TimeUnit.SECONDS);
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

	public ArrayList<DeviceContainer> getDevices() {
		return devices;
	}

	private void loadStaticDevs(String wraperTag) {
		DefaultProxy<ContainerManagerInterface> clientInfo = new DefaultProxy<ContainerManagerInterface>(
				ContainerManagerInterface.class,
				ContainerManagerQueueNames
						.getContainerManagerInterfaceQueryQueue(), 5000);

		logger.info("try to init CM interface");
		ContainerManagerInterface containerManagerClient = null;
		int deviceCode = 0;
		HashMap<String, Object> deviceSpec = new HashMap<String, Object>();
		try {

			containerManagerClient = clientInfo.init();

			/*
			 * HashMap<String, Object> deviceSpec = containerManagerClient
			 * .getDeviceSpecData(13); if (deviceSpec != null) {
			 * 
			 * DeviceContainer device = new DeviceContainer( new
			 * org.fortiss.smg.containermanager.api.devices.DeviceId(
			 * "01008831.EnOcean.Eltako.FTR55.Actual", wrapperTag), wrapperTag,
			 * deviceSpec); device.setHrName("Temperature in Room 225");
			 * devices.add(device);
			 * 
			 * 
			 * 
			 * 
			 * } if (deviceSpec != null) {
			 * 
			 * DeviceContainer bSCsOTSSensors = new DeviceContainer( new
			 * org.fortiss.smg.containermanager.api.devices.DeviceId(
			 * "3b078.EnOcean.BSC.sOTS", wrapperTag), wrapperTag, deviceSpec);
			 * bSCsOTSSensors.setHrName("Outside temperature");
			 * devices.add(bSCsOTSSensors);
			 * 
			 * 
			 * DeviceContainer fTR55DRoom255_3SensorStrategy = new
			 * DeviceContainer( new
			 * org.fortiss.smg.containermanager.api.devices.DeviceId(
			 * "0100E48.EnOcean.Eltako.FTR55.NightReduction", wrapperTag),
			 * wrapperTag, deviceSpec);
			 * fTR55DRoom255_3SensorStrategy.setHrName("Temperature in Room 225"
			 * ); devices.add(fTR55DRoom255_3SensorStrategy);
			 * 
			 * }
			 * 
			 * 
			 * deviceSpec = containerManagerClient .getDeviceSpecData(5); if
			 * (deviceSpec != null) {
			 * 
			 * DeviceContainer fAH60SensorStrategy = new DeviceContainer( new
			 * org.fortiss.smg.containermanager.api.devices.DeviceId(
			 * "321e6.EnOcean.Eltako.FAH60", wrapperTag), wrapperTag,
			 * deviceSpec); fAH60SensorStrategy.setHrName("Outdoor brightness");
			 * devices.add(fAH60SensorStrategy);
			 * 
			 * }
			 * 
			 * deviceSpec = containerManagerClient .getDeviceSpecData(5); if
			 * (deviceSpec != null) {
			 * 
			 * DeviceContainer fBH55Brightness_Room255SensorStrategy = new
			 * DeviceContainer( new
			 * org.fortiss.smg.containermanager.api.devices.DeviceId(
			 * "0003091A.EnOcean.Eltako.FBH55", wrapperTag), wrapperTag,
			 * deviceSpec); fBH55Brightness_Room255SensorStrategy.setHrName(
			 * "Indoor brightness and Occupancy");
			 * devices.add(fBH55Brightness_Room255SensorStrategy);
			 * 
			 * }
			 */



//		deviceSpec = containerManagerClient.getDeviceSpecData(13); // temperature
//		if (!deviceSpec.isEmpty()) {
//			DeviceContainer fTR55DRoom255_3SensorStrategy = new DeviceContainer(
//					new org.fortiss.smg.containermanager.api.devices.DeviceId(
//							"0100E48.EnOcean.Eltako.FTR55.NightReduction",
//							wrapperTag), wrapperTag, deviceSpec);
//			fTR55DRoom255_3SensorStrategy.setHrName("Temperature in Room 225");
//		}
//		/*
//		 * Device Spec from the EnOcean Log Generator Devices
//		 */
//
//		deviceSpec = containerManagerClient.getDeviceSpecData(132); // heating
//		if (!deviceSpec.isEmpty()) {
//			heatingActorStrategy = new DeviceContainer(
//					new org.fortiss.smg.containermanager.api.devices.DeviceId(
//							"heating_ch12.Enocean.Fortiss.Heating", wrapperTag),
//					wrapperTag, deviceSpec);
//			heatingActorStrategy.setHrName("adjustable range: 8°C - 40°C");
//			// + ".enocean", SIDeviceType.Heating, true, true, 0, 1, 1, 0, 1, 1,
//			// "adjustable range: 8Â°C - 40Â°C");
//		}
//
//		deviceSpec = containerManagerClient.getDeviceSpecData(13); //
//		if (!deviceSpec.isEmpty()) {
//			genericPushRelease3010SwitchActorStrategy = new DeviceContainer(
//					new org.fortiss.smg.containermanager.api.devices.DeviceId(
//							"test3.EnOcean.Fortiss.GenericPushRelease3010Switch",
//							wrapperTag), wrapperTag, deviceSpec);
//			genericPushRelease3010SwitchActorStrategy
//					.setHrName("test3.EnOcean.Fortiss.GenericPushRelease3010Switch On-Off");
//			// + ".enocean", SIDeviceType.Switch, true, true, 0, 1, 1, 0, 1, 1,
//			// "On/Off");
//		}
//
//		deviceSpec = containerManagerClient.getDeviceSpecData(143); // light simple
//		if (!deviceSpec.isEmpty()) {
//			light1030ActorStrategy = new DeviceContainer(
//					new org.fortiss.smg.containermanager.api.devices.DeviceId(
//							"office1030light.Enocean.Fortiss.Light1030",
//							wrapperTag), wrapperTag, deviceSpec);
//			// + ".enocean", SIDeviceType.LightSimple, true, true, 0, 1, 1, 0,
//			// 1, 1, "On/Off");
//
//			light1030ActorStrategy.setHrName("office1030light");
//		}
//
//		deviceSpec = containerManagerClient.getDeviceSpecData(143); // light
//																	// simple
//		if (!deviceSpec.isEmpty()) {
//			light5070ActorStrategy = new DeviceContainer(
//
//			new org.fortiss.smg.containermanager.api.devices.DeviceId(
//					"office5070light.EnOcean.Fortiss.Light5070", wrapperTag),
//					wrapperTag, deviceSpec);
//			light5070ActorStrategy.setHrName("office5070light");
//			// + ".enocean", SIDeviceType.LightSimple, true, true, 0, 1, 1, 0,
//			// 1, 1, "On/Off");
//		}
//		deviceSpec = containerManagerClient.getDeviceSpecData(132); // heating
//		if (!deviceSpec.isEmpty()) {
//			ourHardCodedActorStrategy = new DeviceContainer(
//					new org.fortiss.smg.containermanager.api.devices.DeviceId(
//							"EnOcean.Fortiss.OurHardcodedActor", wrapperTag),
//					wrapperTag, deviceSpec);
//			ourHardCodedActorStrategy.setHrName("OurHardcodedActor");
//			// + ".enocean", SIDeviceType.Heating, true, true, 0, 1, 1, 0, 1, 1,
//			// "");
//		}
//		deviceSpec = containerManagerClient.getDeviceSpecData(136); // blinds
//		if (!deviceSpec.isEmpty()) {
//			fortissBlindActorStrategy = new DeviceContainer(
//					new org.fortiss.smg.containermanager.api.devices.DeviceId(
//							"officeblinds.EnOcean.Fortiss.Blinds", wrapperTag),
//					wrapperTag, deviceSpec);
//			fortissBlindActorStrategy.setHrName("officeblinds Position of blinds (0% is up, 100% is down)");
//			// + ".enocean", SIDeviceType.Blinds, true, true, 0, 1, 1, 0, 1, 1,
//			// "Position of blinds (0% is up, 100% is down)");
//		}
//		deviceSpec = containerManagerClient.getDeviceSpecData(145); // switch
//		if (!deviceSpec.isEmpty()) {
//			steckdosenleisteActorStrategy = new DeviceContainer(
//					new org.fortiss.smg.containermanager.api.devices.DeviceId(
//							"Steckdosenleiste.EnOcean.Fortiss.Steckdosenleiste",
//							wrapperTag), wrapperTag, deviceSpec);
//			steckdosenleisteActorStrategy.setHrName("Steckdosenleiste");
//			
//			//TODO
//			
//			// + ".enocean", SIDeviceType.Switch, true, true, 0, 1, 1, 0, 1, 1,
//			// "on/off");
//		}
//		deviceSpec = containerManagerClient.getDeviceSpecData(136); // blinds
//		if (!deviceSpec.isEmpty()) {
//			systestActorStrategy = new DeviceContainer(
//					new org.fortiss.smg.containermanager.api.devices.DeviceId(
//							"EnOcean.Fortiss.SystestActorStrategy", wrapperTag),
//					wrapperTag, deviceSpec);
//			systestActorStrategy.setHrName("blinds  Position of blinds (0% is up, 100% is down) ");//
//			// + ".enocean", SIDeviceType.Blinds, true, true, 0, 1, 1, 0, 1, 1,
//			// "");
//		}
//
//		// Sensors
//		deviceSpec = containerManagerClient.getDeviceSpecData(13); // temperature
//		if (!deviceSpec.isEmpty()) {
//			bSCsOTSSensors = new DeviceContainer(
//					new org.fortiss.smg.containermanager.api.devices.DeviceId(
//							"3b078.EnOcean.BSC.sOTS", wrapperTag), wrapperTag,
//					deviceSpec);
//			bSCsOTSSensors.setHrName("3b078 Outside temperature");
//			// + ".enocean", SIDeviceType.Temperature,true, false,
//			// -Double.MAX_VALUE, Double.MAX_VALUE, 0.1,"");
//		}
//		deviceSpec = containerManagerClient.getDeviceSpecData(5); // brightness
//		if (!deviceSpec.isEmpty()) {
//			fAH60SensorStrategy = new DeviceContainer(
//					new org.fortiss.smg.containermanager.api.devices.DeviceId(
//							".EnOcean.Eltako.FAH60", wrapperTag),
//					wrapperTag, deviceSpec);
//			fAH60SensorStrategy.setHrName("321e6 Outdoor brightness");
//			// + ".enocean", SIDeviceType.Brightness, true, false,
//			// -Double.MAX_VALUE, Double.MAX_VALUE, 0.1,"Outdoor brightness");
//		}
//		deviceSpec = containerManagerClient.getDeviceSpecData(5); // brightness
//		if (!deviceSpec.isEmpty()) {
//			fBH55Brightness_Room255SensorStrategy = new DeviceContainer(
//					new org.fortiss.smg.containermanager.api.devices.DeviceId(
//							"0003091A.EnOcean.Eltako.FBH55", wrapperTag),
//					wrapperTag, deviceSpec);
//			//TODO decide 
//			fBH55Brightness_Room255SensorStrategy.setHrName("0003091A - Indoor brightness and Occupancy ");
//			// + ".enocean", SIDeviceType.Brightness,true, false,
//			// -Double.MAX_VALUE, Double.MAX_VALUE,
//			// 0.1,"Indoor brightness and Occupancy");
//		}
//		deviceSpec = containerManagerClient.getDeviceSpecData(143); // light
//																	// simple
//		if (!deviceSpec.isEmpty()) {
//			fT4DoubleRockerBoolean1030_Room225SensorStrategy = new DeviceContainer(
//					new org.fortiss.smg.containermanager.api.devices.DeviceId(
//							"1E4DE8 10/30.EnOcean.Eltako.FT4DoubleRocker1030",
//							wrapperTag), wrapperTag, deviceSpec);
//			fT4DoubleRockerBoolean1030_Room225SensorStrategy.setHrName("");
//			// + ".enocean", SIDeviceType.Switch, true, false,
//			// -Double.MAX_VALUE, Double.MAX_VALUE,
//			// 0.1,"Lamps in Window side Room225");
//		}
//		deviceSpec = containerManagerClient.getDeviceSpecData(145); // switch light
//																	// simple
//		if (!deviceSpec.isEmpty()) {
//			fT4DoubleRockerBoolean5070_Room225SensorStrategy = new DeviceContainer(
//					new org.fortiss.smg.containermanager.api.devices.DeviceId(
//							"1E4DE8 50/70.EnOcean.Eltako.FT4DoubleRocker5070",
//							wrapperTag), wrapperTag, deviceSpec);
//			fT4DoubleRockerBoolean5070_Room225SensorStrategy.setHrName("1E4DE8 50/70 Lamps in Door side Room225");
//			// + ".enocean", SIDeviceType.Switch, true, false,
//			// -Double.MAX_VALUE, Double.MAX_VALUE,
//			// 0.1,"Lamps in Door side Room225");
//		}
//		deviceSpec = containerManagerClient.getDeviceSpecData(145); // switch blinds
//		if (!deviceSpec.isEmpty()) {
//			fT4DoubleRockerBoolean1030_BlindsRoom225SensorStrategy = new DeviceContainer(
//					new org.fortiss.smg.containermanager.api.devices.DeviceId(
//							"1E4D8A 10/30.EnOcean.Eltako.FT4DoubleRocker1030",
//							wrapperTag), wrapperTag, deviceSpec);
//			fT4DoubleRockerBoolean1030_BlindsRoom225SensorStrategy.setHrName("1E4D8A 10/30 Blinds Room22");
//			// + ".enocean", SIDeviceType.Switch, true, false,
//			// -Double.MAX_VALUE, Double.MAX_VALUE, 0.1,"Blinds Room225");
//		}
//		deviceSpec = containerManagerClient.getDeviceSpecData(143); // light
//																	// simple
//		if (!deviceSpec.isEmpty()) {
//			fT4DoubleRockerBooleanHandControl1030_Room225SensorStrategy = new DeviceContainer(
//					new org.fortiss.smg.containermanager.api.devices.DeviceId(
//							"1E64E6 10/30.EnOcean.Eltako.FT4DoubleRocker",
//							wrapperTag), wrapperTag, deviceSpec);
//			fT4DoubleRockerBooleanHandControl1030_Room225SensorStrategy.setHrName("1E64E6 10/30 Lamps in Window side Room 225");
//			// + ".enocean", SIDeviceType.Switch, true, false,
//			// -Double.MAX_VALUE, Double.MAX_VALUE,
//			// 0.1,"Lamps in Window side Room 225");
//		}
//		deviceSpec = containerManagerClient.getDeviceSpecData(145); // light
//																	// simple
//		if (!deviceSpec.isEmpty()) {
//			fT4DoubleRockerBooleanHandControl5070_Room225SensorStrategy = new DeviceContainer(
//					new org.fortiss.smg.containermanager.api.devices.DeviceId(
//							"1E64E6 50/70.EnOcean.Eltako.FT4DoubleRocker",
//							wrapperTag), wrapperTag, deviceSpec);
//			fT4DoubleRockerBooleanHandControl5070_Room225SensorStrategy.setHrName("1E64E6 50/70 Lamps in Door side Room 225");
//			// + ".enocean", SIDeviceType.Switch, true, false,
//			// -Double.MAX_VALUE, Double.MAX_VALUE,
//			// 0.1,"Lamps in Door side Room 225");
//		}
//		deviceSpec = containerManagerClient.getDeviceSpecData(145); // blinds
//		if (!deviceSpec.isEmpty()) {
//			fT4DoubleRockerBooleanHandControl1030Blinds_Room225SensorStrategy = new DeviceContainer(
//					new org.fortiss.smg.containermanager.api.devices.DeviceId(
//							"1E6596 10/30.EnOcean.Eltako.FT4DoubleRocker",
//							wrapperTag), wrapperTag, deviceSpec);
//			fT4DoubleRockerBooleanHandControl1030Blinds_Room225SensorStrategy.setHrName("1E6596 10/30 Blinds in Room 225");
//			// + ".enocean", SIDeviceType.Switch, true, false,
//			// -Double.MAX_VALUE, Double.MAX_VALUE, 0.1,"Blinds in Room 225");
//		}
//		deviceSpec = containerManagerClient.getDeviceSpecData(145); // light
//																	// simple
//		if (!deviceSpec.isEmpty()) {
//			fT4DoubleRockerBoolean1030_DijkstraSensorStrategy = new DeviceContainer(
//					new org.fortiss.smg.containermanager.api.devices.DeviceId(
//							"1E4E19 10/30.EnOcean.Eltako.FT4DoubleRocker1030",
//							wrapperTag), wrapperTag, deviceSpec);
//			fT4DoubleRockerBoolean1030_DijkstraSensorStrategy.setHrName("1E4E19 10/30 Halogen Lamps");
//			// + ".enocean", SIDeviceType.Switch, true, false,
//			// -Double.MAX_VALUE, Double.MAX_VALUE, 0.1,"Halogen Lamps");
//		}
//		deviceSpec = containerManagerClient.getDeviceSpecData(145); // light
//																	// simple
//		if (!deviceSpec.isEmpty()) {
//			fT4DoubleRockerBoolean5070_DijkstraSensorStrategy = new DeviceContainer(
//					new org.fortiss.smg.containermanager.api.devices.DeviceId(
//							"1E4E19 50/70.EnOcean.Eltako.FT4DoubleRocker5070",
//							wrapperTag), wrapperTag, deviceSpec);
//			fT4DoubleRockerBoolean5070_DijkstraSensorStrategy.setHrName("1E4E19 50/70 Fluorescent Lamps");
//			// + ".enocean", SIDeviceType.Switch, true, false,
//			// -Double.MAX_VALUE, Double.MAX_VALUE, 0.1,"Fluorescent Lamps");
//		}
//		deviceSpec = containerManagerClient.getDeviceSpecData(145); // blinds
//		if (!deviceSpec.isEmpty()) {
//			fT4DoubleRockerBoolean10_DijkstraSensorStrategy = new DeviceContainer(
//					new org.fortiss.smg.containermanager.api.devices.DeviceId(
//							"1E4DD0 10.EnOcean.Eltako.FT4DoubleRocker",
//							wrapperTag), wrapperTag, deviceSpec);
//			fT4DoubleRockerBoolean10_DijkstraSensorStrategy.setHrName("1E4DD0 Left Blinds");
//			// + ".enocean", SIDeviceType.Switch, true, false,
//			// -Double.MAX_VALUE, Double.MAX_VALUE, 0.1,"Left Blinds");
//		}
//		deviceSpec = containerManagerClient.getDeviceSpecData(136); // blinds
//		if (!deviceSpec.isEmpty()) {
//			fT4DoubleRockerBoolean50_DijkstraSensorStrategy = new DeviceContainer(
//					new org.fortiss.smg.containermanager.api.devices.DeviceId(
//							"1E4DD0 50.EnOcean.Eltako.FT4DoubleRocker",
//							wrapperTag), wrapperTag, deviceSpec);
//			fT4DoubleRockerBoolean50_DijkstraSensorStrategy.setHrName("1E4DD0 Right Blinds");
//			// + ".enocean", SIDeviceType.Switch, true, false,
//			// -Double.MAX_VALUE, Double.MAX_VALUE, 0.1,"Right Blinds");
//		}
//		deviceSpec = containerManagerClient.getDeviceSpecData(145); // light
//																	// simple
//		if (!deviceSpec.isEmpty()) {
//			fT4DoubleRockerBooleanHandControl1030_DijkstraSensorStrategy = new DeviceContainer(
//					new org.fortiss.smg.containermanager.api.devices.DeviceId(
//							"1E657C 10/30.EnOcean.Eltako.FT4DoubleRocker",
//							wrapperTag), wrapperTag, deviceSpec);
//			fT4DoubleRockerBooleanHandControl1030_DijkstraSensorStrategy.setHrName("1E657C 10/30 Halogen Lamps");
//			// + ".enocean", SIDeviceType.Switch, true, false,
//			// -Double.MAX_VALUE, Double.MAX_VALUE, 0.1,"Halogen Lamps");
//		}
//		deviceSpec = containerManagerClient.getDeviceSpecData(145); // light
//																	// simple
//		if (!deviceSpec.isEmpty()) {
//			fT4DoubleRockerBooleanHandControl5070_DijkstraSensorStrategy = new DeviceContainer(
//					new org.fortiss.smg.containermanager.api.devices.DeviceId(
//							"1E657C 50/70.EnOcean.Eltako.FT4DoubleRocker",
//							wrapperTag), wrapperTag, deviceSpec);
//			fT4DoubleRockerBooleanHandControl5070_DijkstraSensorStrategy.setHrName("1E657C 50/70 Fluorescent Lamps");
//			// + ".enocean", SIDeviceType.Switch, true, false,
//			// -Double.MAX_VALUE, Double.MAX_VALUE, 0.1,"Fluorescent Lamps");
//		}
//		deviceSpec = containerManagerClient.getDeviceSpecData(145); // switch
//		if (!deviceSpec.isEmpty()) {
//			fT4DoubleRockerBooleanHandControl10_DijkstraSensorStrategy = new DeviceContainer(
//					new org.fortiss.smg.containermanager.api.devices.DeviceId(
//							"1E656C 10.EnOcean.Eltako.FT4DoubleRocker",
//							wrapperTag), wrapperTag, deviceSpec);
//			fT4DoubleRockerBooleanHandControl10_DijkstraSensorStrategy.setHrName("1E656C Left Blinds");
//			// + ".enocean", SIDeviceType.Switch, true, false,
//			// -Double.MAX_VALUE, Double.MAX_VALUE, 0.1,"Left Blinds");
//		}
//		deviceSpec = containerManagerClient.getDeviceSpecData(145); // switch
//		if (!deviceSpec.isEmpty()) {
//			fT4DoubleRockerBooleanHandControl50_DijkstraSensorStrategy = new DeviceContainer(
//					new org.fortiss.smg.containermanager.api.devices.DeviceId(
//							"1E656C 50.EnOcean.Eltako.FT4DoubleRocker",
//							wrapperTag), wrapperTag, deviceSpec);
//			fT4DoubleRockerBooleanHandControl50_DijkstraSensorStrategy.setHrName("1E656C Right Blinds");
//			// + ".enocean", SIDeviceType.Switch, true, false,
//			// -Double.MAX_VALUE, Double.MAX_VALUE, 0.1,"Right Blinds");
//		}
//		deviceSpec = containerManagerClient.getDeviceSpecData(153); // motion-occupancy
//		if (!deviceSpec.isEmpty()) {
//			fBH55MotionDetectorSensorStrategy = new DeviceContainer(
//					new org.fortiss.smg.containermanager.api.devices.DeviceId(
//							"0003380A.EnOcean.Eltako.FBH55Occupancy",
//							wrapperTag), wrapperTag, deviceSpec);
//			fBH55MotionDetectorSensorStrategy.setHrName("0003380A Occupancy");
//			// + ".enocean", SIDeviceType.SignificantMotion, true, false,
//			// -Double.MAX_VALUE, Double.MAX_VALUE, 0.1,"");
//		}
//		deviceSpec = containerManagerClient.getDeviceSpecData(145); // switch
//		if (!deviceSpec.isEmpty()) {
//			fT4SingleRocker5070Strategy = new DeviceContainer(
//					new org.fortiss.smg.containermanager.api.devices.DeviceId(
//							"001E4DD8.EnOcean.Eltako.FT4SingleRocker5070",
//							wrapperTag), wrapperTag, deviceSpec);
//			fT4SingleRocker5070Strategy.setHrName("001E4DD8 Switch");
//			// + ".enocean", SIDeviceType.Switch, true, false,
//			// -Double.MAX_VALUE, Double.MAX_VALUE, 0.1,"");
//		}
//		deviceSpec = containerManagerClient.getDeviceSpecData(145); // switch
//		if (!deviceSpec.isEmpty()) {
//			fT4SingleRocker1030Strategy = new DeviceContainer(
//					new org.fortiss.smg.containermanager.api.devices.DeviceId(
//							"001E4DA8.EnOcean.Eltako.FT4SingleRocker1030",
//							wrapperTag), wrapperTag, deviceSpec);
//			fT4SingleRocker1030Strategy.setHrName("001E4DA8 SingleRocker1030");
//			// + ".enocean", SIDeviceType.Switch, true, false,
//			// -Double.MAX_VALUE, Double.MAX_VALUE, 0.1,"");
//		}
//		//TODO inconsistency ?
//		
//		deviceSpec = containerManagerClient.getDeviceSpecData(151); // window
//		if (!deviceSpec.isEmpty()) {
//			fTKRoom225_2SensorStrategy = new DeviceContainer(
//					new org.fortiss.smg.containermanager.api.devices.DeviceId(
//							"100627f.EnOcean.Eltako.FTK", wrapperTag),
//					wrapperTag, deviceSpec);
//			fTKRoom225_2SensorStrategy.setHrName("100627F Left Window open/close");
//			// + ".enocean", SIDeviceType.Window, true, false,
//			// -Double.MAX_VALUE, Double.MAX_VALUE,
//			// 0.1,"Left Window open/close");
//		}
//		deviceSpec = containerManagerClient.getDeviceSpecData(151); // window
//		if (!deviceSpec.isEmpty()) {
//			fTKRoom225_1SensorStrategy = new DeviceContainer(
//					new org.fortiss.smg.containermanager.api.devices.DeviceId(
//							"1007d14.EnOcean.Eltako.FTK", wrapperTag),
//					wrapperTag, deviceSpec);
//			fTKRoom225_1SensorStrategy.setHrName("1007D14 Right Window open/close");
//			// + ".enocean", SIDeviceType.Window, true, false,
//			// -Double.MAX_VALUE, Double.MAX_VALUE,
//			// 0.1,"Right Window open/close");
//		}
//		deviceSpec = containerManagerClient.getDeviceSpecData(13); // temperature
//		if (!deviceSpec.isEmpty()) {
//			fTR55DRoom255_1SensorStrategy = new DeviceContainer(
//					new org.fortiss.smg.containermanager.api.devices.DeviceId(
//							"01008831.EnOcean.Eltako.FTR55.Actual", wrapperTag),
//					wrapperTag, deviceSpec);
//			fTR55DRoom255_1SensorStrategy.setHrName("01008831 act Temperature in Room 225");
//			// + ".enocean", SIDeviceType.Temperature, true, false,
//			// -Double.MAX_VALUE, Double.MAX_VALUE,
//			// 0.1,"Temperature in Room 225");
//		}
//		deviceSpec = containerManagerClient.getDeviceSpecData(13); // temperature
//		if (!deviceSpec.isEmpty()) {
//			fTR55DRoom255_2SensorStrategy = new DeviceContainer(
//					new org.fortiss.smg.containermanager.api.devices.DeviceId(
//							"010088C8.EnOcean.Eltako.FTR55.Reference",
//							wrapperTag), wrapperTag, deviceSpec);
//			fTR55DRoom255_2SensorStrategy.setHrName("010088C8 ref Temperature in Room 225");
//			// + ".enocean", SIDeviceType.Temperature, true, false,
//			// -Double.MAX_VALUE, Double.MAX_VALUE,
//			// 0.1,"Temperature in Room 225");
//		}
//		deviceSpec = containerManagerClient.getDeviceSpecData(13); // temperature
//		if (!deviceSpec.isEmpty()) {
//			fTR55DRoom255_3SensorStrategy = new DeviceContainer(
//					new org.fortiss.smg.containermanager.api.devices.DeviceId(
//							"0100E48.EnOcean.Eltako.FTR55.NightReduction",
//							wrapperTag), wrapperTag, deviceSpec);
//			fTR55DRoom255_3SensorStrategy.setHrName("0100E48 Temperature in Room 225");
//			// + ".enocean", SIDeviceType.Temperature, true, false,
//			// -Double.MAX_VALUE, Double.MAX_VALUE,
//			// 0.1,"Temperature in Room 225");
//		}
//		deviceSpec = containerManagerClient.getDeviceSpecData(101); // ConsumptionPowermeter
//		if (!deviceSpec.isEmpty()) {
//			fWZ12Sensor1Strategy = new DeviceContainer(
//					new org.fortiss.smg.containermanager.api.devices.DeviceId(
//							"008758B7.EnOcean.Eltako.FWZ12", wrapperTag),
//					wrapperTag, deviceSpec);
//			fWZ12Sensor1Strategy.setHrName("008758B7 Current Power Consumption");
//			// + ".enocean", SIDeviceType.ConsumptionPowermeter, true, false,
//			// -Double.MAX_VALUE, Double.MAX_VALUE,
//			// 0.1,"Current Power Consumption");
//		}
//		deviceSpec = containerManagerClient.getDeviceSpecData(101); // ConsumptionPowermeter
//		if (!deviceSpec.isEmpty()) {
//			fWZ12Sensor2Strategy = new DeviceContainer(
//					new org.fortiss.smg.containermanager.api.devices.DeviceId(
//							"0087A835.EnOcean.Eltako.FWZ12", wrapperTag),
//					wrapperTag, deviceSpec);
//			fWZ12Sensor2Strategy.setHrName("0087A835 Current Power Consumption");
//			// + ".enocean", SIDeviceType.ConsumptionPowermeter, true, false,
//			// -Double.MAX_VALUE, Double.MAX_VALUE,
//			// 0.1,"Current Power Consumption");
//		}
//		deviceSpec = containerManagerClient.getDeviceSpecData(104); // ConsumptionPowermeterAggregated
//		if (!deviceSpec.isEmpty()) {
//			fWZ12Sensor3Strategy = new DeviceContainer(
//					new org.fortiss.smg.containermanager.api.devices.DeviceId(
//							"0087DFF7.EnOcean.Eltako.FWZ12", wrapperTag),
//					wrapperTag, deviceSpec);
//			fWZ12Sensor3Strategy.setHrName("0087DFF7 Current Power Consumption");
//			// + ".enocean", SIDeviceType.ConsumptionPowermeter, true, false,
//			// -Double.MAX_VALUE, Double.MAX_VALUE,
//			// 0.1,"Current Power Consumption");
//		}
//		deviceSpec = containerManagerClient.getDeviceSpecData(101); // ConsumptionPowermeter
//		if (!deviceSpec.isEmpty()) {
//			fWZ12Sensor4Strategy = new DeviceContainer(
//					new org.fortiss.smg.containermanager.api.devices.DeviceId(
//							"0087D5F9.B7EnOcean.Eltako.FWZ12", wrapperTag),
//					wrapperTag, deviceSpec);
//			fWZ12Sensor4Strategy.setHrName("0087D5F9 Current Power Consumption");
//			// + ".enocean", SIDeviceType.ConsumptionPowermeter, true, false,
//			// -Double.MAX_VALUE, Double.MAX_VALUE,
//			// 0.1,"Current Power Consumption");
//		}
//		deviceSpec = containerManagerClient.getDeviceSpecData(145); // switch
//		if (!deviceSpec.isEmpty()) {
//			hardCodedButton1Strategy = new DeviceContainer(
//					new org.fortiss.smg.containermanager.api.devices.DeviceId(
//							"5070.EnOcean.Fortiss.HardCodedButton1", wrapperTag),
//					wrapperTag, deviceSpec);
//			hardCodedButton1Strategy.setHrName("5070.EnOcean.Fortiss.HardCodedButton1 Current Power Consumption (Off peak time)");
//			// + ".enocean", SIDeviceType.Switch, true, false,
//			// -Double.MAX_VALUE, Double.MAX_VALUE,
//			// 0.1,"Current Power Consumption (Off peak time)");
//		}
//		deviceSpec = containerManagerClient.getDeviceSpecData(145); // switch
//		if (!deviceSpec.isEmpty()) {
//			hardCodedButton2Strategy = new DeviceContainer(
//					new org.fortiss.smg.containermanager.api.devices.DeviceId(
//							"1030.EnOcean.Fortiss.HardCodedButton2", wrapperTag),
//					wrapperTag, deviceSpec);
//			hardCodedButton2Strategy.setHrName("1030.EnOcean.Fortiss.HardCodedButton2 Current Power Consumption (Off peak time)");
//			// + ".enocean", SIDeviceType.Switch, true, false,
//			// -Double.MAX_VALUE, Double.MAX_VALUE,
//			// 0.1,"Current Power Consumption (Off peak time)");
//		}
//		deviceSpec = containerManagerClient.getDeviceSpecData(13); // temperature
//		if (!deviceSpec.isEmpty()) {
//			systestSensorStrategy = new DeviceContainer(
//					new org.fortiss.smg.containermanager.api.devices.DeviceId(
//							"EnOcean.Fortiss.SystestSensor", wrapperTag),
//					wrapperTag, deviceSpec);
//			
//			// + ".enocean", SIDeviceType.Temperature, true, false,
//			// -Double.MAX_VALUE, Double.MAX_VALUE, 0.1,"");
//		}
//		deviceSpec = containerManagerClient.getDeviceSpecData(151); // window
//		if (!deviceSpec.isEmpty()) {
//			fTKTest_1SensorStrategy = new DeviceContainer(
//					new org.fortiss.smg.containermanager.api.devices.DeviceId(
//							"001860B.EnOcean.Eltako.FTK", wrapperTag),
//					wrapperTag, deviceSpec);
//			fTKTest_1SensorStrategy.setHrName("001860B Test Window open/close");
//			// + ".enocean", SIDeviceType.Window, true, false,
//			// -Double.MAX_VALUE, Double.MAX_VALUE,
//			// 0.1,"Left Window open/close");
//		}
//		
//		
//		
//		
//		
//		
//		// ftDoubleRockerUSBSwitch1030Stratgy = new DeviceContainer(
//		// new org.fortiss.smg.containermanager.api.devices.DeviceId(
//		// "001B1096 10/30.EnOcean.Eltako.FT4DoubleRocker",
//		// wrapperTag),wrapperTag + ".enocean", SIDeviceType.Switch,
//		// true, false, -Double.MAX_VALUE, Double.MAX_VALUE, 0.1,"Electrisity");
//		// ftDoubleRockerUSBSwitch5070Stratgy = new DeviceContainer(
//		// new org.fortiss.smg.containermanager.api.devices.DeviceId(
//		// "001B1096 50/70.EnOcean.Eltako.FT4DoubleRocker",
//		// wrapperTag),wrapperTag + ".enocean", SIDeviceType.Switch,
//		// true, false, -Double.MAX_VALUE, Double.MAX_VALUE, 0.1,"Electrisity");
//		devices.add(heatingActorStrategy);
//		devices.add(genericPushRelease3010SwitchActorStrategy);
//		devices.add(light1030ActorStrategy);
//		devices.add(light5070ActorStrategy);
//		devices.add(ourHardCodedActorStrategy);
//		devices.add(fortissBlindActorStrategy);
//		devices.add(steckdosenleisteActorStrategy);
//		
//
//		
//		
//		devices.add(systestActorStrategy);
//		devices.add(bSCsOTSSensors);
//		devices.add(fAH60SensorStrategy);
//
//		devices.add(fBH55Brightness_Room255SensorStrategy);
//		devices.add(fT4DoubleRockerBoolean1030_Room225SensorStrategy);
//		devices.add(fT4DoubleRockerBoolean5070_Room225SensorStrategy);
//		devices.add(fT4DoubleRockerBooleanHandControl5070_Room225SensorStrategy);
//		devices.add(fT4DoubleRockerBoolean1030_BlindsRoom225SensorStrategy);
//		devices.add(fT4DoubleRockerBooleanHandControl1030_Room225SensorStrategy);
//		devices.add(fT4DoubleRockerBooleanHandControl1030Blinds_Room225SensorStrategy);
//		devices.add(fT4DoubleRockerBoolean1030_DijkstraSensorStrategy);
//		devices.add(fT4DoubleRockerBoolean5070_DijkstraSensorStrategy);
//		devices.add(fT4DoubleRockerBoolean10_DijkstraSensorStrategy);
//
//		devices.add(fT4DoubleRockerBoolean50_DijkstraSensorStrategy);
//		devices.add(fT4DoubleRockerBooleanHandControl1030_DijkstraSensorStrategy);
//		devices.add(fT4DoubleRockerBooleanHandControl5070_DijkstraSensorStrategy);
//		devices.add(fT4DoubleRockerBooleanHandControl10_DijkstraSensorStrategy);
//		devices.add(fT4DoubleRockerBooleanHandControl50_DijkstraSensorStrategy);
//		devices.add(fBH55MotionDetectorSensorStrategy);
//		devices.add(fT4SingleRocker5070Strategy);
//		devices.add(fT4SingleRocker1030Strategy);
//		devices.add(fTR55DRoom255_2SensorStrategy);
//		devices.add(fTKRoom225_1SensorStrategy);
//
//		devices.add(fTR55DRoom255_3SensorStrategy);
//		devices.add(fWZ12Sensor1Strategy);
//		devices.add(fWZ12Sensor2Strategy);
//		devices.add(fWZ12Sensor3Strategy);
//		devices.add(fWZ12Sensor4Strategy);
//		devices.add(fTR55DRoom255_1SensorStrategy);
//		devices.add(hardCodedButton1Strategy);
//		devices.add(hardCodedButton2Strategy);
//		devices.add(systestSensorStrategy);
//		devices.add(fTKTest_1SensorStrategy);
//		
//		// devices.add(ftDoubleRockerUSBSwitch1030Stratgy);
//		// devices.add(ftDoubleRockerUSBSwitch5070Stratgy);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}

		
	}

	@Override
	public void onDoubleCommand(DoubleCommand command, DeviceId dev) {
		logger.debug("Received Doublecommand for " + dev.getDevid());
	for (DeviceContainer device : devices) {
			if(device.getDeviceId().equals(dev)) {
				//if (device.getHrName().equals("Steckdosenleiste")) {
					boolean valueBool = true;
					if (command.getValue() == 0.0) {
						valueBool = false;
					}
					//looper.getActor("Steckdosenleiste").setBoolean(valueBool, dev.toContainterId(), 0, "", true, "");
				//}
				
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

	public ArrayList<String> getEnOceanDeviceIds() {
		return enOceanDeviceIds;
	}


	// public HashMap<String,Integer> mapDeviceIDtoGoogleId(){
	// HashMap map = new HashMap<String,Integer>();
	// map.put("Temprature", 13);
	// map.put("Humidity", 12);
	// map.put("Brightness", 5);
	// map.put("Blinds", 136);
	// map.put("Window", 151);
	// map.put("Door", 152);
	// map.put("Blinds", 136);
	// return map;
	// }

	// ArrayList<String> requestDeviceIDs() throws ClientProtocolException,
	// IOException {
	// GetDevicesResponse resp = communicator.execute(new GetDevicesRequest(),
	// GetDevicesResponse.class);
	// ArrayList<String> result = new ArrayList<String>();
	// for (Device device : resp.getDevices()) {
	// result.add(device.getKey());
	// }
	// return result;
	// }
	//
	// public EnOceanCommunicator getCommunicator() {
	// return communicator;
	// }
	//
}
