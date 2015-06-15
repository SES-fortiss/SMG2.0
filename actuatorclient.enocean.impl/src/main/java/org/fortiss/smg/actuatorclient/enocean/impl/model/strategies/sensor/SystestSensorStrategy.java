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
import org.fortiss.smg.containermanager.api.devices.SIUnitType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This Sensor-Strategy is only for use in tests.
 * It is written to react in a hardcoded fashion to any telegram of type 0x07
 * Please refer to the code for details.
 */
public class SystestSensorStrategy extends SensorStrategy{
	
	
	private static final Logger logger = LoggerFactory.getLogger(SystestSensorStrategy.class);
	public SystestSensorStrategy(ActuatorClientImpl impl) {
		super();
	}
	
	@Override
	public void handleTelegram(UniversalTelegram telegram) {
		try{
		if (telegram.getOrg().equals(EnOceanOrigin.EEP_4BS)) {
			double value = (double) telegram.getDataInt();
			DeviceId origin = impl.getDeviceSpecs().get(38).getDeviceId();
			DoubleEvent ev = new DoubleEvent(value);
            impl.getMaster().sendDoubleEvent(ev, origin,impl.getClientId());
            logger.info("EnOceanLooper: run(): getEventHandler - new Event from " +  origin + " value " + value );
//			getEventHandler().doubleEvent("systestQ?0", telegram.getDataInt(), SIUnitType.CELSIUS, 1D);
		}
		} catch (TimeoutException e) {
       	 logger.error("timeout sending to master", e);
		 }
	}
//	@Override
//	public Map<String, DeviceSpec> getSpecs() {
//		return asHashMap(new DeviceSpecBuilder("EnOcean.Fortiss.SystestSensor", DeviceCategory.SENSOR).addDouble(SIUnitType.PERCENT).asEvent().create());
//	}

}
