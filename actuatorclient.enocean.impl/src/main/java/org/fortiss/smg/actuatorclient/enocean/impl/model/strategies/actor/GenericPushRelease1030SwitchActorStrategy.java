/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.actor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;



import java.util.concurrent.TimeoutException;


//import org.fortiss.smartmicrogrid.shared.builders.devspecbuilder.DeviceSpecBuilder;
import org.fortiss.smg.websocket.api.shared.schema.DeviceCategory;
import org.fortiss.smg.websocket.api.shared.schema.DeviceSpec;
import org.fortiss.smg.actuatorclient.enocean.impl.ActuatorClientImpl;
//import org.fortiss.smartmicrogrid.shared.schema.TeachInTelegramInfo;
import org.fortiss.smg.actuatorclient.enocean.impl.telegrams.EnOceanOrigin;
import org.fortiss.smg.actuatorclient.enocean.impl.telegrams.UniversalTelegram;
import org.fortiss.smg.actuatormaster.api.events.DoubleEvent;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import teachintelegraminfo.TeachInTelegramInfo;

public class GenericPushRelease1030SwitchActorStrategy extends ActorStrategy {

	private static final Logger logger = LoggerFactory.getLogger(GenericPushRelease1030SwitchActorStrategy.class);

	private boolean lightState = false;

	
	public GenericPushRelease1030SwitchActorStrategy() {
		
	}
	
//	@Override
//	public DeviceSpec getSpec() {
//		DeviceSpecBuilder builder = new DeviceSpecBuilder("EnOcean.Fortiss.Steckdosenleiste", DeviceCategory.SWITCH);
//		builder.addBoolean().withFalseSynonym("OFF").withTrueSynonym("ON").asCommand();
//		builder.addBoolean().withFalseSynonym("OFF").withTrueSynonym("ON").asEvent();
//
//		builder.addToggle().withToggleSynonym("Toggle ON/OFF").asCommand();
//
//
//		return builder.create();
//	}

	/**
	 * Set Booleans by sending button-push-messages to enocean-actors like the power-socket
	 */
	@Override
	public void setBoolean(boolean valueBool, String internalID, int delay, String tag, boolean execute, String valueIdentifier) {
		logger.debug("received setBoolean({},{})", valueBool, internalID);
		lightState = valueBool;

		UniversalTelegram telegram = new UniversalTelegram();
		telegram.setOrg(EnOceanOrigin.EEP_RPS);
		telegram.setDataByte((char) (valueBool ? 0x10 : 0x30), 3); //upper or lower button?
		telegram.setStatus((char) 0x30); //PRESS
		
		if (execute) {
			communicator.sendTelegram(telegram);
		} //otherwise change state without executing (external connection)

		try {
			TimeUnit.MILLISECONDS.sleep(50);
		} catch (InterruptedException e) {
			logger.warn("Sleeping between button press and releas in Generic Actor Strategy interrupted");
		}
		
		telegram.setDataByte((char) (valueBool ? 0x10 : 0x30), 3);
		telegram.setStatus((char) 0x20); //RELEASE
		telegram.setIdHexString(communicator.getSenderdeviceId(), actor.getChannel()); 
		
		if (execute) {
			communicator.sendTelegram(telegram);
		} //otherwise change state without executing (external connection)
		try{
		DeviceId origin = impl.getDeviceSpecs().get(1).getDeviceId();
		Double value = 0.0;
		if (valueBool)
			value = 1.0;
			
		DoubleEvent ev = new DoubleEvent(value);
        impl.getMaster().sendDoubleEvent(ev, origin, impl.getClientId());
        logger.info("EnOceanLooper: run(): getEventHandler - new Event from " +  origin + " value " + value );
//		getEventHandler().booleanEvent(actor.getId(), valueBool);
		} catch (TimeoutException e) {
			logger.error("timeout sending to master", e);
		}
	}

	@Override
	public void toggle(String internalIDToggle, int delay, String tag, boolean execute, String valueIdentifier) {
		logger.debug("received toggle for {}", internalIDToggle);
		setBoolean(!lightState, internalIDToggle, delay, tag, execute, valueIdentifier);
	}

	@Override
	public Collection<TeachInTelegramInfo> getSupportedTeachInTelegrams() {

		ArrayList<TeachInTelegramInfo> ts = new ArrayList<TeachInTelegramInfo>();

		UniversalTelegram telegram = new UniversalTelegram();
		telegram.setOrg(EnOceanOrigin.EEP_RPS);
		telegram.setDataByte((char) 0x10, 3);
		telegram.setStatus((char) 0x30);

		TeachInTelegramInfo ti = new TeachInTelegramInfo();
		ti.setTeachInTelegramString(telegram.getTelegramString());
		ti.setTeachInTelegramDescription("Schalter 10/30 Logik einlernen 0x10 (bitte ebenfalls 0x30 einlernen)");
		ts.add(ti);

		ti = new TeachInTelegramInfo();
		telegram.setDataByte((char) 0x30, 3);
		telegram.setStatus((char) 0x30);
		ti.setTeachInTelegramString(telegram.getTelegramString());
		ti.setTeachInTelegramDescription("Schalter 10/30 Logik einlernen 0x30 (bitte ebenfalls 0x10 einlernen)");
		ts.add(ti);

		return ts;
	}
}
