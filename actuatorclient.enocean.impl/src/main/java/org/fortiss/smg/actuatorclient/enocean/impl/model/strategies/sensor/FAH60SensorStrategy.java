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
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.containermanager.api.devices.SIUnitType;
import org.fortiss.smg.actuatorclient.enocean.impl.ActuatorClientImpl;
import org.fortiss.smg.actuatorclient.enocean.impl.telegrams.EnOceanOrigin;
import org.fortiss.smg.actuatorclient.enocean.impl.telegrams.UniversalTelegram;
import org.fortiss.smg.actuatormaster.api.events.DoubleEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FAH60SensorStrategy extends SensorStrategy {
	
	
	private static final Logger logger = LoggerFactory.getLogger(FAH60SensorStrategy.class);
	public FAH60SensorStrategy() {
		
	}
	
	/**
	 * See wireless_system_high_res-1.pdf page219
	 */
	@Override
	public void handleTelegram(UniversalTelegram telegram) {
		try{if (telegram.getOrg().equals(EnOceanOrigin.EEP_4BS) && !isTeachIn(telegram)) {
			if (telegram.getDataByte(2)==0x00) {
				int luxByte = telegram.getDataByte(3);
				double realLux = luxByte*100.0/256;
				DeviceId origin = impl.getDeviceSpecs().get(9).getDeviceId();
				DoubleEvent ev = new DoubleEvent(realLux);
	            impl.getMaster().sendDoubleEvent(ev, origin,impl.getClientId());
	            logger.info("EnOceanLooper: run(): getEventHandler - new Event from " +  origin + " value " + realLux );
//				getEventHandler().doubleEvent( sensor.getId(),realLux, SIUnitType.LUX, 0.1953125);
			}else {
				int luxByte = telegram.getDataByte(2);
				double realLux = luxByte*29700/256+300;
				DeviceId origin = impl.getDeviceSpecs().get(9).getDeviceId();
				DoubleEvent ev = new DoubleEvent(realLux);
	            impl.getMaster().sendDoubleEvent(ev, origin,impl.getClientId());
	            logger.info("EnOceanLooper: run(): getEventHandler - new Event from " +  origin + " value " + realLux );
//				getEventHandler().doubleEvent(sensor.getId(), realLux,  SIUnitType.LUX, 58.0078125);
			}
		}
		} catch (TimeoutException e) {
        	 logger.error("timeout sending to master", e);
		 }
	}

	private boolean isTeachIn(UniversalTelegram telegram) {
		return telegram.getDataByte(3) == 0x18 &&
				telegram.getDataByte(2) == 0x08 &&
				telegram.getDataByte(1) == 0x0D &&
				telegram.getDataByte(0) == 0x87;
	}

//	@Override
//	public Map<String, DeviceSpec> getSpecs() {
//		DeviceSpecBuilder builder = new DeviceSpecBuilder("EnOcean.Eltako.FAH60", DeviceCategory.SENSOR);
//		builder.addDouble(SIUnitType.LUX).withRange(0d,30000d).withDescription("Outdoor brightness").asEvent();
//		return asHashMap(builder.create());
//	}
}