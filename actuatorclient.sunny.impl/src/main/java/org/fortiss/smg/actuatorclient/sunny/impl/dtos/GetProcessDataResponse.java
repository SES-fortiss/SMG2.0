/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.actuatorclient.sunny.impl.dtos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.fortiss.smg.actuatorclient.sunny.impl.jsonrpc.dto.GenericResponse;

public class GetProcessDataResponse extends GenericResponse{
	private final Map<String,Collection<Device>> result = new HashMap<String, Collection<Device>>();

	public Collection<Device> getDevices() {
		return result.get("devices");
	}
	
	public Collection<Device> getDevicesUnique() {
		ArrayList<Device> devices = new ArrayList<Device>();
		for (Device d : result.get("devices")) {
			if (!devices.contains(d)) {
				devices.add(d);
			}
			else {
				System.out.println("DOUBLE DEVICE ENTRY FOUND skipping " + d.getKey());
			}
		}
		return devices;
	}
}
