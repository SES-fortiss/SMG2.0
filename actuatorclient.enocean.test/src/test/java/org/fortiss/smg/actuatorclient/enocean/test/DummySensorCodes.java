/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.actuatorclient.enocean.test;

public class DummySensorCodes {

	public static String getString() {
		return "INSERT INTO `ContainerManager_Devices` (`DeviceCode`, `DeviceType`, `SMGDeviceType`, `AllowedUserProfile`, `MinUpdateRate`, `MaxUpdateRate`, `AcceptsCommands`, `HasValue`, `RangeMin`, `RangeMax`, `RangeStep`, `CommandMinRange`, `CommandMaxRange`, `CommandRangeStep`, `CommandRangeStepType`, `HumanReadableName`, `Description`) VALUES"+ 
"(1, 'ACCELEROMETER', 'Accelerometer', 0, 1000, 3600000, 0, 0, 0, 50, 0.1, -1, -1, -1, 'NONE', '3-Axis Accelerometer', 'Measures the acceleration force in m/s^2 that is applied to a device on all three physical axes (x, y, and z), including the force of gravity.')," + 
"(13, 'AMBIENT_TEMPERATURE', 'Temperature', 1, 1000, 3600000, 0, 0, -50, 100, 0.1, -1, -1, -1, 'NONE', 'Thermometer', 'Ambient temperature sensor in Celsius')," +
"(143, '', 'LightSimple', 0, -1, -1, 1, 1, 0, 1, 1, 0, 1, 1, 'LINEAR', '', '')," +
"(144, '', 'LightDimmable', 0, -1, -1, 1, 1, 1, 100, 1, 0, 100, 1, 'LINEAR', '', '')," +
"(145, '', 'Switch', 0, -1, -1, 1, 1, 0, 1, 1, 0, 1, 1, 'LINEAR', 'Switch configurable [Default: 0/1 off/on]', 'Switch Device to describe any switchable Device (e.g. Aircondition Auto,Cool,Fan)');";
	}

}
