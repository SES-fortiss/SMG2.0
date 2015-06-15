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

public class BSCsOTSSensorStrategy extends SensorStrategy {
	
	
	private static final Logger logger = LoggerFactory.getLogger(BSCsOTSSensorStrategy.class);
	public BSCsOTSSensorStrategy() {
		super();
	}
	
	/**
	 * Strategy to read a Temperature-Sensor
	 * Magic values can be looked up in enOcean-Documentation
	 */
	@Override
	public void handleTelegram(UniversalTelegram telegram) {
		try{
			if (telegram.getOrg().equals(EnOceanOrigin.EEP_4BS) && !isTeachIn(telegram)) {
			int tempByte = telegram.getDataByte(1);
			double actualTemp =  (60.0 - tempByte*80.0/255.0);
			//Accuracy 0.8Kelvin
			DeviceId origin = impl.getDeviceSpecs().get(8).getDeviceId();
			DoubleEvent ev = new DoubleEvent(actualTemp);
            impl.getMaster().sendDoubleEvent(ev, origin,impl.getClientId());
            logger.info("EnOceanLooper: run(): getEventHandler - new Event from " +  origin + " value " + actualTemp );
//			getEventHandler().doubleEvent(sensor.getId(), actualTemp,  SIUnitType.CELSIUS, 0.8);
		}
		} catch (TimeoutException e) {
	        	 logger.error("timeout sending to master", e);
			
		}
	}

	private boolean isTeachIn(UniversalTelegram telegram) {
		if (telegram.getOrg().equals(EnOceanOrigin.EEP_4BS)){
			return ((telegram.getDataByte(0)>>3) & 0x01) == 0x00;
		}else{
			return false;
		}
	}

//	@Override
//	public Map<String, DeviceSpec> getSpecs() {
//		DeviceSpecBuilder builder = new DeviceSpecBuilder("EnOcean.BSC.sOTS", DeviceCategory.SENSOR);
//		builder.addDouble(SIUnitType.CELSIUS).withRange(-20,60d).withDescription("Outside temperature").asEvent();
//		return asHashMap(builder.create());
//	}

}
