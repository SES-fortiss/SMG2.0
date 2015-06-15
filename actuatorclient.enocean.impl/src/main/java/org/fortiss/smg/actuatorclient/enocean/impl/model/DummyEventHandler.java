/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.actuatorclient.enocean.impl.model;

//import org.fortiss.smartmicrogrid.shared.EventHandler;
import org.fortiss.smg.actuatormaster.api.events.DeviceEvent;
import org.fortiss.smg.actuatormaster.api.events.DoubleEvent;
//import org.fortiss.smartmicrogrid.shared.schema.DeviceSpec
import org.fortiss.smg.websocket.api.shared.schema.DeviceSpec;
//import org.fortiss.smartmicrogrid.shared.schema.SIUnitType;
import org.fortiss.smg.containermanager.api.devices.SIUnitType;

//public class DummyEventHandler implements EventHandler {
public class DummyEventHandler {
	// DUMMY EVENTHANDLER - In case a Strategy uses the EventHandler before
	// the setter was called
//	@Override
	public void toggleEvent(String origin) {
	}

//	@Override
	public void stringEvent(String origin, String value) {
	}


//	@Override
	public void doubleEvent(String origin, double value, SIUnitType unit, double absMaxError) {
	}

//	@Override
	public void booleanEvent(String origin, boolean value) {
	}

//	@Override
	public void newDeviceEvent(String newDevOrigin, DeviceSpec spec) {
	}
}