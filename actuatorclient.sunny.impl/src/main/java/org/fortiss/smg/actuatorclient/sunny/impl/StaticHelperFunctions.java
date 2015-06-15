/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.actuatorclient.sunny.impl;

import java.util.Arrays;

import org.fortiss.smg.containermanager.api.devices.SIUnitType;

public class StaticHelperFunctions {

	public static  boolean isBattery(String string) {
		return Arrays.asList("BatSoc", "BatVtg", "BatTmp").contains(string);
	}

	public static boolean isInverter(String meta) {
		return meta.equals("InvPwrAt");
	}

	public static SIUnitType convertUnit(String unit) {
		if (unit.equals("%")) {
			return SIUnitType.PERCENT;
		} else if (unit.equals("degC")) {
			return SIUnitType.CELSIUS;
		} else if (unit.equals("kW")) {
			return SIUnitType.W; // TODO: this is hacky
		} else {
			return SIUnitType.fromValue(unit);
		}
	}
	
}
