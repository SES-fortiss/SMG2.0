package org.fortiss.smg.actuatorclient.dummy.impl;

import java.util.concurrent.TimeoutException;

import org.fortiss.smg.actuatorclient.dummy.impl.ActuatorClientImpl;
import org.fortiss.smg.actuatormaster.api.events.DoubleEvent;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.slf4j.LoggerFactory;

public class DummyLooper implements Runnable {

	private static org.slf4j.Logger logger = LoggerFactory
			.getLogger(DummyLooper.class);
	
	private ActuatorClientImpl impl;
	private DeviceId origin;
 	private DoubleEvent ev;

	
	DummyLooper( ActuatorClientImpl impl) {
		this.impl = impl;
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		readDummyValues();
	}


	private void readDummyValues() {
		try {
		
		/*
		 * Consumption between 0 and 4000
		 */
		Double consumption = Math.random() * 4000.0;
		impl.setConsumption(consumption);
		origin = impl.getDeviceSpecs().get(0).getDeviceId();
    	ev = new DoubleEvent(consumption);
    	impl.getMaster().sendDoubleEvent(ev, origin, impl.getClientId());
		logger.info("DummyDevice: run(): getEventHandler - new Event from " + origin + " value " + consumption);
		
		/*
		 * Temperature between -20 and 40
		 */
		Double temperature = -20.0 + Math.random() * 60; 
		impl.setTemperature(temperature);
		origin = impl.getDeviceSpecs().get(1).getDeviceId();
    	ev = new DoubleEvent(temperature);
    	impl.getMaster().sendDoubleEvent(ev, origin, impl.getClientId());
		logger.info("DummyDevice: run(): getEventHandler - new Event from " + origin + " value " + consumption);
		/*
		 * Brightness between 0 and 3000
		 */
		Double brightness = Math.random() * 3000.0;
		impl.setBrightness(brightness);
		origin = impl.getDeviceSpecs().get(2).getDeviceId();
    	ev = new DoubleEvent(brightness);
    	impl.getMaster().sendDoubleEvent(ev, origin, impl.getClientId());
		logger.info("DummyDevice: run(): getEventHandler - new Event from " + origin + " value " + consumption);
		
		/* 
		 * Battery between 0 and 100
		 */
		Double battery = Math.random() * 100.0;
		impl.setBattery(battery);
		origin = impl.getDeviceSpecs().get(3).getDeviceId();
    	ev = new DoubleEvent(battery);
    	impl.getMaster().sendDoubleEvent(ev, origin, impl.getClientId());
		logger.info("DummyDevice: run(): getEventHandler - new Event from " + origin + " value " + consumption);
		
		/*
		 * Switch 0/1
		 */
		
		
		
		}
		catch (TimeoutException e) {
	       	 logger.error("timeout sending to master", e);
		}
	}

}
