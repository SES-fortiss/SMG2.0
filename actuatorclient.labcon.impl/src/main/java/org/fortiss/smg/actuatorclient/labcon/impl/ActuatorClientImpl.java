/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.actuatorclient.labcon.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.actuatormaster.api.IActuatorClient;
import org.fortiss.smg.actuatormaster.api.IActuatorMaster;
import org.fortiss.smg.actuatormaster.api.events.DeviceEvent;
import org.fortiss.smg.containermanager.api.devices.DeviceContainer;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.containermanager.api.devices.SIDeviceType;
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
	private String username;
	private String password;
	
	
	private LabconController controller;
	private HTMLReaderLabcon reader;
	
	/*
	 * Parameters for LabCon
	 */
	private int powerState = -1;
	private double brighness = -1;
	private double temperature = -1;
	private double consumption = -1;
	
	// SSH Configuration
	private String sftpHost = "192.168.21.201";
	private int sftpPort = 22;
	private String sftpUser = "root";
	private String sftpPassword = "!SMG2011";
	private String sftpDir = "/usr/local/labcon/zbs_logs/";
	private String remoteFile = "ZBS-110_0013a200409c5958_CoffeeMaker_get.csv";
	private String remoteFile_MultiSensor = "ZBS-121_0013a200408a1ec7_MultiSensor_get.csv";
	
	
	private String wrapperTag;
	ArrayList<String> labconDeviceIds;

	public ArrayList<String> getLabConDeviceIds() {
		return labconDeviceIds;
	}

	ArrayList<DeviceContainer> devices = new ArrayList<DeviceContainer>();
	private DeviceContainer labConSpecControlCoffeeMaker;
	private DeviceContainer labConSpecConsumptionCoffeeMaker;
	
	private DeviceContainer labConSpecMultiSensorTemperature;
	private DeviceContainer labConSpecMultiSensorBrightness;
	

	public ActuatorClientImpl(String host, String port, String wrapperTag,
			int pollFreq, String username, String password) {
		this.host = host;
		//this.port = port;
		this.wrapperTag = wrapperTag;
		this.username = username;
		this.password = password;
		
		loadStaticDevs(wrapperTag);
		
		
		
		controller = new LabconController(this, this.host, this.username, this.password);
		
		
		// SSH Configuration to read/refresh device info
		sftpHost = host;
		sftpPort = 22;
		sftpUser = "root";
		sftpPassword = "!SMG2011";
		sftpDir = "/usr/local/labcon/zbs_logs/";
		
		
		//reader = new SSHReaderLabcon(sftpHost, sftpPort, sftpUser, sftpPassword, sftpDir, remoteFile);
		reader = new HTMLReaderLabcon(this.host, this.username, this.password); 
		
	
		
		pollFrequency = pollFreq;

	}

	public synchronized void activate() {

		sendNewDeviceEvents();

		executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(new LabconLooper(this), 0,
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

	private void loadStaticDevs(String wraperTagN) {
		
		labConSpecControlCoffeeMaker = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"coffeemaker_plug", wraperTagN), wraperTagN
						+ ".powerplug");
		
		labConSpecControlCoffeeMaker.setRange(0,1, 1);
		labConSpecControlCoffeeMaker.setCommandRange(0,1,1);
		labConSpecControlCoffeeMaker.setHrName("CoffeeMaker PowerPlug");
		labConSpecControlCoffeeMaker.setDeviceType(SIDeviceType.Powerplug);
		
		labConSpecConsumptionCoffeeMaker = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"coffeemaker_powermeter", wraperTagN), wraperTagN
						+ ".powermeter");
		
		labConSpecConsumptionCoffeeMaker.setRange(0,Double.MAX_VALUE, 1);
		labConSpecConsumptionCoffeeMaker.setHrName("CoffeeMaker Consumption [W]");
		labConSpecConsumptionCoffeeMaker.setDeviceType(SIDeviceType.ConsumptionPowermeter);
		
		labConSpecMultiSensorTemperature = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"multisensor_temperature", wraperTagN), wraperTagN
						+ ".multisensor");
		
		labConSpecMultiSensorTemperature.setRange(-50,150, 0.1);
		labConSpecMultiSensorTemperature.setHrName("Multisensor [C]");
		labConSpecMultiSensorTemperature.setDeviceType(SIDeviceType.Temperature);
		
		labConSpecMultiSensorBrightness = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"multisensor_brightness", wraperTagN), wraperTagN
						+ ".multisensor");
		
		labConSpecMultiSensorBrightness.setRange(0,1000, 1);
		labConSpecMultiSensorBrightness.setHrName("Multisensor [Lux]");
		labConSpecMultiSensorBrightness.setDeviceType(SIDeviceType.Brightness);
		
		
		devices.add(labConSpecControlCoffeeMaker);
		devices.add(labConSpecConsumptionCoffeeMaker);
		devices.add(labConSpecMultiSensorTemperature);
		devices.add(labConSpecMultiSensorBrightness);

		
	}

	public ArrayList<DeviceContainer> getDevices() {
		return devices;
	}

	@Override
	public void onDoubleCommand(DoubleCommand command, DeviceId dev) {
		logger.debug("Received Doublecommand "+command.getValue()+" for " + dev.getDevid());
		if (dev.getDevid() == "coffeemaker_plug") {
			
			if (command.getValue() == 1.0) {
				if (this.getPowerState() == 0) {
					controller.controlCoffeeMachine(command.getValue());
					this.setPowerState(1);
				}
				else {
					logger.debug("Device " + dev.getDevid() + " was already on");
				}
			}
			else if (command.getValue() == 0.0) {
				if (this.getPowerState() == 1) {
					controller.controlCoffeeMachine(command.getValue());
					this.setPowerState(0);
				}
				else {
					logger.debug("Device " + dev.getDevid() + " was already off");
				}
			}
			else if (command.getValue() == -1.0) {
				toggleDevice();
			}
			else {
				logger.debug("Received Doublecommand "+command.getValue()+" for " + dev.getDevid() + " but it is not a valid command");
			}
		}
	}
	
	// Toggle 
	private void toggleDevice(){
			if (this.getPowerState() == 0) {
				controller.controlCoffeeMachine(1.0);
				this.setPowerState(1);
			}
			else {
				controller.controlCoffeeMachine(0.0);
				this.setPowerState(0);
			}
	}
	

	private void sendNewDeviceEvents() {
		for (DeviceContainer dev : devices) {
			try {
				logger.debug("Dev:" + dev.getDeviceId().getDevid() + " -> "
						+ clientId);
				DeviceEvent ev = new DeviceEvent(dev);
				master.sendDeviceEvent(ev, this.clientId);
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

	/*public void connectToSSH() {
		if (reader != null) {
			reader.connect();
		}
		else {
			//reader = new SSHReaderLabcon(sftpHost, sftpPort, sftpUser, sftpPassword, sftpDir, remoteFile);
			reader.connect();
		}
	}*/

	public boolean isConnectedToSSH() {
		if (reader != null) {
			return reader.isConnected();
		}
		else {
			return false;
		}
	}

	public List<String[]> readDevice() {
		logger.info("LabConWrapperImpl: read coffeemaker: ############### reading remote file for coffee maker ###############");
		
		//reader.setRemoteFile(remoteFile);
		
		return reader.readRemoteFile(remoteFile);
		
	}
	
	public List<String[]> readMultiSensor(){
		logger.info("LabConWrapperImpl: read multiSensor: ############### reading remote file for Multi Sensors ###############");

		//reader.setRemoteFile(remoteFile_MultiSensor);
		
		return reader.readRemoteFile_MultiSensor(remoteFile_MultiSensor);
		
	}

	public int getPowerState() {
		return this.powerState;
	}

	public void setPowerState(int powerState) {
		this.powerState = powerState;
	}

	public double getBrighness() {
		return brighness;
	}

	public void setBrighness(double brighness) {
		this.brighness = brighness;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public double getConsumption() {
		return consumption;
	}

	public void setConsumption(double consumption) {
		this.consumption = consumption;
	}
	

}
