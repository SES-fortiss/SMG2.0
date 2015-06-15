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

public class FTKSensorStrategy extends SensorStrategy{
	private static final Logger logger = LoggerFactory.getLogger(FTKSensorStrategy.class);
	
	public FTKSensorStrategy() {
		super();
	}
	/**
	 * See wireless_system_high_res-1.pdf page219
	 */
	@Override
	public void handleTelegram(UniversalTelegram telegram) {
		try{
		if (telegram.getOrg().equals(EnOceanOrigin.EEP_1BS) && !isTeachIn(telegram)) {
			
			DeviceId origin = impl.getDeviceSpecs().get(29).getDeviceId();
			double value = 0.0;
						           
			
			if (telegram.getDataByte(0)==0x09) { //BUG IN DATASHEET - Byte 0 contains value (not byte 3)
				// contact closed:
					value = 1.0;
					DoubleEvent ev = new DoubleEvent(value);
					impl.getMaster().sendDoubleEvent(ev, origin,impl.getClientId());
		            logger.info("EnOceanLooper: run(): getEventHandler - new Event from " +  origin + " value " + value );
//		            getEventHandler().booleanEvent(sensor.getId(), true);	
				
				
			}else if (telegram.getDataByte(0)==0x08) { //BUG IN DATASHEET - Byte 0 contains value (not byte 3)
				// contact open
				value = 0.0;
				DoubleEvent ev = new DoubleEvent(value);
				impl.getMaster().sendDoubleEvent(ev, origin,impl.getClientId());
	            logger.info("EnOceanLooper: run(): getEventHandler - new Event from " +  origin + " value " + value );
//				getEventHandler().booleanEvent(sensor.getId(), false);
	            
			}else {
				logger.warn("invalid telegram"+telegram.getTelegramString());
			}
		}
		} catch (TimeoutException e) {
       	 logger.error("timeout sending to master", e);
		 }
	}

	private boolean isTeachIn(UniversalTelegram telegram) {
		return telegram.getDataByte(3) == 0x00 &&
				telegram.getDataByte(2) == 0x00 &&
				telegram.getDataByte(1) == 0x00 &&
				telegram.getDataByte(0) == 0x00;
	}

//	@Override
//	public Map<String, DeviceSpec> getSpecs() {
//		DeviceSpecBuilder builder = new DeviceSpecBuilder("EnOcean.Eltako.FTK", DeviceCategory.WINDOW_DETECTOR);
//		builder.addBoolean().withFalseSynonym("window open").withTrueSynonym("window closed").asEvent();
//		return asHashMap(builder.create());
//	}

}
