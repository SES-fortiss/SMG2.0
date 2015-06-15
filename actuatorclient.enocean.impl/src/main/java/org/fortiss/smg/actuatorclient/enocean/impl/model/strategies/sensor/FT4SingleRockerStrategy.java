/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor;

import java.util.Map;




import java.util.concurrent.TimeoutException;

//import org.fortiss.smartmicrogrid.shared.builders.devspecbuilder.DeviceSpecBuilder;
import org.fortiss.smg.websocket.api.shared.schema.DeviceCategory;
import org.fortiss.smg.websocket.api.shared.schema.DeviceSpec;
import org.fortiss.smg.actuatorclient.enocean.impl.ActuatorClientImpl;
import org.fortiss.smg.actuatorclient.enocean.impl.telegrams.EnOceanOrigin;
import org.fortiss.smg.actuatorclient.enocean.impl.telegrams.UniversalTelegram;
import org.fortiss.smg.actuatormaster.api.events.DoubleEvent;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FT4SingleRockerStrategy extends SensorStrategy {
	private static final Logger logger = LoggerFactory.getLogger(FT4SingleRockerStrategy.class);
	
	public FT4SingleRockerStrategy() {
		super();
	}
	/**
	 * See wireless_system_high_res-1.pdf page219
	 */
	// TODO: duplicate code, merge with other FT4
	@Override
	public void handleTelegram(UniversalTelegram telegram) {
		if (telegram == null) {
			return;
		}
		
		try{
		DeviceId origin = impl.getDeviceSpecs().get(26).getDeviceId();
		
		logger.debug("Received telegram: {}", telegram);
		if (telegram.getOrg().equals(EnOceanOrigin.EEP_RPS) && telegram.getDataByte(3) == 0x50) {
			DoubleEvent ev = new DoubleEvent(0.0);
	        impl.getMaster().sendDoubleEvent(ev, origin,impl.getClientId());
	        logger.info("EnOceanLooper: run(): getEventHandler - new Event from " +  origin + " value " + 0.0 );
//			getEventHandler().booleanEvent(sensor.getId(), false);
		} else if (telegram.getOrg().equals(EnOceanOrigin.EEP_RPS) && telegram.getDataByte(3) == 0x70) {
			DoubleEvent ev = new DoubleEvent(1.0);
	        impl.getMaster().sendDoubleEvent(ev, origin,impl.getClientId());
	        logger.info("EnOceanLooper: run(): getEventHandler - new Event from " +  origin + " value " + 1.0 );
//			getEventHandler().booleanEvent(sensor.getId(), true);
		} else if (telegram.getOrg().equals(EnOceanOrigin.EEP_RPS) && telegram.getDataByte(3) == 0x10) {
			origin = impl.getDeviceSpecs().get(27).getDeviceId();
			DoubleEvent ev = new DoubleEvent(0.0);
	        impl.getMaster().sendDoubleEvent(ev, origin,impl.getClientId());
	        logger.info("EnOceanLooper: run(): getEventHandler - new Event from " +  origin + " value " + 0.0 );
//			getEventHandler().booleanEvent(sensor.getId(), false);
		} else if (telegram.getOrg().equals(EnOceanOrigin.EEP_RPS) && telegram.getDataByte(3) == 0x30) {
			origin = impl.getDeviceSpecs().get(27).getDeviceId();
			DoubleEvent ev = new DoubleEvent(1.0);
	        impl.getMaster().sendDoubleEvent(ev, origin,impl.getClientId());
	        logger.info("EnOceanLooper: run(): getEventHandler - new Event from " +  origin + " value " + 1.0 );
//			getEventHandler().booleanEvent(sensor.getId(), true);
		} else if (telegram.getOrg().equals(EnOceanOrigin.EEP_RPS) && telegram.getDataByte(3) == 0x00
				&& telegram.getStatus() == 0x20) {
			// release button
		} else {
			// invalid sensor-databyte(3) (10,30,50,70) or origin
			logger.warn(sensor.getId() + " uses FT4 - check correctness!");
		}
		} catch (TimeoutException e) {
       	 logger.error("timeout sending to master", e);
		 }
	}

//	@Override
//	public Map<String, DeviceSpec> getSpecs() {
//		DeviceSpecBuilder builder = new DeviceSpecBuilder("EnOcean.Eltako.FT4SingleRocker", DeviceCategory.SWITCH);
//		builder.addBoolean().asEvent();
//		return asHashMap(builder.create());
//	}
}
