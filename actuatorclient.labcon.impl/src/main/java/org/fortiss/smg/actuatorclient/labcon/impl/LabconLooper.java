/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.actuatorclient.labcon.impl;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;
import org.fortiss.smg.actuatormaster.api.events.DoubleEvent;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.slf4j.LoggerFactory;

public class LabconLooper implements Runnable {

	private static org.slf4j.Logger logger = LoggerFactory
			.getLogger(LabconLooper.class);

	private ActuatorClientImpl impl;
	private DeviceId origin;
 	private DoubleEvent ev;

	LabconLooper(ActuatorClientImpl impl) {
		this.impl = impl;
	}

	@Override
	public void run() {
		
		/*if(!impl.isConnectedToSSH()){
			logger.info("LabconWrapperImpl: ############### Connecting Reader ###############");
			impl.connectToSSH();
				
		}*/
		
		readDevice();
		readMultiSensor();			
		logger.info("Read values at: " + (new Date()));
	}
	
	
	private void readDevice() {
		logger.info("LabconWrapperImpl: read coffeemaker: ############### reading remote file for coffee maker ###############");
		
		List<String[]> eventData = impl.readDevice();
		if (eventData != null) {
			for (String[] data : eventData) {
				if (data != null) {
					createNewEvent(data);
				}
			}
		}
	}
	
	private void readMultiSensor(){
		logger.info("LabconWrapperImpl: read multiSensor: ############### reading remote file for Multi Sensors ###############");
		
		List<String[]> eventDataMultiSensor = impl.readMultiSensor();
		if (eventDataMultiSensor != null) {
			for (String[] data : eventDataMultiSensor) {	
				if (data != null) {
					createNewEvent_MultiSensor(data);
				}
			}
		}
	}

	private void createNewEvent(String[] data) {
		// sample data[] : 2013,1,30,10,23,16,ON,49.9375,231,0,0,12.303, -> only if something is plugged in
		// data[0] //year
		// data[1] //month (its 0 based. so add 1 to get real month of Calendar)
		// data[2] //date
		// data[3] //hour
		// data[4] //minute
		// data[5] //second
		// data[6] //PowerState
		// data[7] //Frequency
		// data[8] //Voltage_RMS in V
		// data[9] //Current_RMS in mA
		// data[10] //LOAD in W
		// data[11] //WORK in kWh
		// sample data[] : 2013,7,18,7,43,46,OFF,13.883, -> if nothing isplugged in
		// data[0] //year
		// data[1] //month (its 0 based. so add 1 to get real month of Calendar)
		// data[2] //date
		// data[3] //hour
		// data[4] //minute
		// data[5] //second
		// data[6] //POW
		// data[7] //FREQ
		Double consumption = 0.0;
		try {
			if (data.length == 12) {
				/*
			 	* Consistency for power state
			 	*/
				if (data[6].equals("OFF") && impl.getPowerState() != 0) {
					impl.setPowerState(0);
					//getEventHandler().booleanEvent(FULL_DEVICE_ID, false);
				}
				else if (data[6].equals("ON") && impl.getPowerState() != 1) { 
					impl.setPowerState(1);
					//getEventHandler().booleanEvent(FULL_DEVICE_ID, true);
				}
				else {
					logger.debug("Labcon Power state inconsistent");
				}
			
				/*
			 	* generate Consumption Value Event
			 	*/
			
				consumption = Double.parseDouble(data[10]);
				logger.info("LabconWrapperImpl: createNewEvent: Coffee maker: ############### Labcon Wrapper creating event with {} ###############", consumption);
				impl.setConsumption(consumption);
				// DeviceSpec for Consumption
				origin = impl.getDeviceSpecs().get(1).getDeviceId();
            	ev = new DoubleEvent(consumption);
            	impl.getMaster().sendDoubleEvent(ev, origin, impl.getClientId());
				logger.info("LabconWrapper: run(): getEventHandler - new Event from " + origin + " value " + consumption);
			}
            else if (data.length >= 5) {
				logger.info("LabconWrapperImpl: ############### Labcon Coffee Maker is {} ###############", data[6].toString());
				impl.setConsumption(consumption);
				origin = impl.getDeviceSpecs().get(1).getDeviceId();
            	ev = new DoubleEvent(consumption);
            	impl.getMaster().sendDoubleEvent(ev, origin, impl.getClientId());
				logger.info("LabconWrapper: run(): getEventHandler - new Event from " + origin + " value " + consumption);
			}
			else {
				logger.info("An error occured generating an event from data ");
			}
		}
		catch (TimeoutException e) {
       	 logger.error("timeout sending to master", e);
		}
		
		
	}
	
	private void createNewEvent_MultiSensor(String[] data){
		// sample data[] : 2013,8,22,12,15,58,410,24.4,OK,4.01, -> only if something is plugged in
		// data[0] //year
		// data[1] //month (its 0 based. so add 1 to get real month of Calendar)
		// data[2] //date
		// data[3] //hour
		// data[4] //minute
		// data[5] //second
		
		// data[6] //Brightness
		// data[7] //Temperature
		// data[8] //BatteryState
		// data[9] //BatteryVoltage
		try {
			if (data.length == 10) {
				Double brighness = Double.parseDouble(data[6]);
				logger.info("LabconWrapperImpl: createNewEvent: multi sensor: ############### eventvalue Brightness: {} ###############", brighness);
				impl.setBrighness(brighness);
				origin = impl.getDeviceSpecs().get(3).getDeviceId();
            	ev = new DoubleEvent(brighness);
            	impl.getMaster().sendDoubleEvent(ev, origin, impl.getClientId());
				logger.info("LabconWrapper: run(): getEventHandler - new Event from " + origin + " value " + brighness);
						
				Double temperature = Double.parseDouble(data[7]);
				logger.info("LabconWrapperImpl: createNewEvent: multi sensor: ############### eventvalue Temperature: {} ###############", temperature);
				impl.setTemperature(temperature);
				origin = impl.getDeviceSpecs().get(2).getDeviceId();
            	ev = new DoubleEvent(temperature);
            	impl.getMaster().sendDoubleEvent(ev, origin, impl.getClientId());
				logger.info("LabconWrapper: run(): getEventHandler - new Event from " + origin + " value " + temperature);
			
			}
		//if MultiSensor is not connected, it doesn't write anything into the file. so we don't need to check or create any event.
		}
		catch (TimeoutException e) {
	       	 logger.error("timeout sending to master", e);
		}
	}
}
