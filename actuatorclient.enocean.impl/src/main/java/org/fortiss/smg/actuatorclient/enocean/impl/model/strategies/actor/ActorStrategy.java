/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.actor;

import java.util.Collection;





//import org.fortiss.smartmicrogrid.shared.EventHandler;
import org.fortiss.smg.actuatormaster.api.events.DoubleEvent;
import org.fortiss.smg.actuatormaster.api.events.DeviceEvent;
import org.fortiss.smg.websocket.api.shared.schema.DeviceSpec;
//import org.fortiss.smartmicrogrid.shared.schema.SIUnitType;
import org.fortiss.smg.containermanager.api.devices.SIUnitType;
import org.fortiss.smg.actuatorclient.enocean.impl.ActuatorClientImpl;
//import org.fortiss.smartmicrogrid.shared.schema.TeachInTelegramInfo;
import org.fortiss.smg.actuatorclient.enocean.impl.EnOceanCommunicator;
import org.fortiss.smg.actuatorclient.enocean.impl.model.Actor;
import org.fortiss.smg.actuatorclient.enocean.impl.model.DummyEventHandler;
import org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor.AbstractImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import teachintelegraminfo.TeachInTelegramInfo;

//import org.fortiss.smartmicrogrid.wrapper.generic.NotSupportedException;

public abstract class ActorStrategy  extends AbstractImpl  {
	private static final Logger logger = LoggerFactory.getLogger(ActorStrategy.class);
	protected EnOceanCommunicator communicator;

	protected Actor actor;

//	private EventHandler eventHandler;
	
	public void setActor(Actor actor){
		this.actor = actor;
	}

	
	public void setCommunicator(EnOceanCommunicator communicator) {
		this.communicator = communicator;
	}

	public void setString(String valueStr, String internalID, int delay, String tag, boolean execute, String valueIdentifier) {
		logger.info("setString() is not supported by this device");

	}

	public void toggle(String internalIDToggle, int delay, String tag, boolean execute, String valueIdentifier) {
		logger.info("toggle() is not supported by this device");

	}

	public void setBoolean(boolean valueBool, String internalID, int delay, String tag, boolean execute, String valueIdentifier) {
		logger.info("setBoolean() is not supported by this device");

	}

	public void setDouble(double valueDbl, SIUnitType unit, String internalID, int delay, String tag, boolean execute, String valueIdentifier) {
		logger.info("setDouble() is not supported by this device");

	}

	public void stopLastCmd(String internalIDStop, int delay, String tag, boolean execute) {
		logger.info("stopLastCMD() is not supported by this device");

	}

	public abstract Collection<TeachInTelegramInfo> getSupportedTeachInTelegrams();

	/**
	 * Each Strategy needs an eventHandler in order to send updated values.
	 * All Events are created in Strategies
	 * @return The instance of the eventHandler-Proxy to send events to.
	 */
//	public EventHandler getEventHandler() {
//		if (eventHandler == null) {
//			return new DummyEventHandler();
//		} else {
//			return eventHandler;
//		}
//	}
//	public void setEventHandler(EventHandler eventHandler) {
//		this.eventHandler = eventHandler;
//	}

	public String getDescription() {
		return "no description...";
	}

//	public abstract DeviceSpec getSpec();

	public void startStrategy() {
		// TODO Auto-generated method stub	
	}


}
