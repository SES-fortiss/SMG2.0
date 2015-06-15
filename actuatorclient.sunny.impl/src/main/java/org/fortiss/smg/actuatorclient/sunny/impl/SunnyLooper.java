/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.actuatorclient.sunny.impl;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.apache.http.client.ClientProtocolException;
import org.fortiss.smg.actuatorclient.sunny.impl.dtos.Channel;
import org.fortiss.smg.actuatorclient.sunny.impl.dtos.Device;
import org.fortiss.smg.actuatorclient.sunny.impl.dtos.GetProcessDataDevice;
import org.fortiss.smg.actuatorclient.sunny.impl.dtos.GetProcessDataRequest;
import org.fortiss.smg.actuatorclient.sunny.impl.dtos.GetProcessDataResponse;
import org.fortiss.smg.actuatormaster.api.events.DoubleEvent;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SunnyLooper implements Runnable {

	private static Logger logger = LoggerFactory
			.getLogger(SunnyLooper.class);

	private ActuatorClientImpl impl;

	SunnyLooper(ActuatorClientImpl impl) {
		this.impl = impl;
	}

	@Override
	public void run() {
		GetProcessDataRequest req = new GetProcessDataRequest();
		for (String deviceId : impl.getSunnyDeviceIds()) {
			req.addDevice(new GetProcessDataDevice(deviceId, "BatSoc",
					"BatVtg", "BatTmp", "TotSicPvPwr", "TotSicBatCur",
					"InvPwrAt"));
		}

		/*
		 * ArrayList<String> added= new ArrayList<String>(); for
		 * (DeviceContainer dev : impl.getDeviceSpecs()) { String deviceId =
		 * dev.getInternDeviceParent(); if(!added.contains(deviceId)){
		 * added.add(deviceId); req.addDevice(new GetProcessDataDevice(deviceId,
		 * "BatSoc", "BatVtg", "BatTmp", "TotSicPvPwr", "TotSicBatCur",
		 * "InvPwrAt")); }
		 * 
		 * }
		 */
		try {
			GetProcessDataResponse response = impl.getCommunicator().execute(
					req, GetProcessDataResponse.class);
			for (Device device : response.getDevicesUnique()) {
				for (Channel channel : device.getChannels()) {

					DeviceId origin = new DeviceId("", impl.getWrapperTag());

				

					if (StaticHelperFunctions.isBattery(channel.getMeta())) {
						if (channel.getMeta().equals("BatSoc")) {
							origin =  impl.getDevices().get(0).getDeviceId();
						} else if (channel.getMeta().equals("BatTmp")) {
							origin =  impl.getDevices().get(1).getDeviceId();
						}if (channel.getMeta().equals("BatVtg")) {
							origin =  impl.getDevices().get(2).getDeviceId();
						}
					} else {
						if (channel.getMeta().equals("TotSicPvPwr")) {
							origin =  impl.getDevices().get(3).getDeviceId();
						} else if (channel.getMeta().equals("TotSicBatCur")) {
							origin =  impl.getDevices().get(4).getDeviceId();
						} 
				
						if (channel.getMeta().equals("InvPwrAt")) {
							origin =  impl.getDevices().get(5).getDeviceId();
						}
					}

					
					Double value = channel.getValue();
					if (channel.getUnit().equals("kW")) {
						value = value * 1000; // TODO: this is hacky
					}

					DoubleEvent ev = new DoubleEvent(value);

					impl.getMaster().sendDoubleEvent(ev, origin,
							impl.getClientId());
					logger.info("SunnyWrapper: run(): getEventHandler - new Event from "
							+ origin
							+ " value "
							+ value
							+ " unit "
							+ StaticHelperFunctions.convertUnit(channel
									.getUnit()));

				}
			}
		} catch (ClientProtocolException e) {
			logger.error("could not access sunny", e);
		} catch (IOException e) {
			logger.error("could not access sunny", e);
		} catch (TimeoutException e) {
			logger.error("timeout sending to master", e);
		}
	}

}
