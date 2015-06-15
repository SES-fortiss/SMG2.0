/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.actuatorclient.ipswitch.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.actuatormaster.api.events.DoubleEvent;
import org.fortiss.smg.containermanager.api.devices.DeviceContainer;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.slf4j.LoggerFactory;

public class IpswitchLooper implements Runnable {

	private static org.slf4j.Logger logger = LoggerFactory
			.getLogger(IpswitchLooper.class);

	private ActuatorClientImpl impl;
	private DeviceId origin;
 	private DoubleEvent ev;
 	
//IPSwitch
	private final static int CONN_TIMEOUT = 5000; //5 sec
	private final static int REFRESH_RATE = 30000;//30sec

	private URL counterUrl;
	private URL temperatureUrl;
	private URLConnection counterPage;
	private URLConnection temperaturePage;
	private BufferedReader input;
	private BufferedReader br;

	IpswitchLooper(ActuatorClientImpl impl) {
		this.impl = impl;
	}

	@Override
	public void run() {	
		readDevice();		
		logger.info("Read values at: " + (new Date()));
	}
	
	private void readDevice() {
		try {

			logger.info("IPSwitchWrapperImpl: run: Con1");
			counterUrl = new URL(impl.getProtocol()+"://" + impl.getHost() + "/" + impl.getPath());
			counterPage = counterUrl.openConnection();
			counterPage.setConnectTimeout(CONN_TIMEOUT);
			counterPage.setReadTimeout(CONN_TIMEOUT);
			input = new BufferedReader(new InputStreamReader(
					counterPage.getInputStream()));

			logger.debug("IPSwitchLooper: run: Read Counter");
			Counter(input);
			input.close();
			logger.debug("IPSwitchLooper: run: Finished Counter");

			logger.debug("Con2");
			temperatureUrl = new URL(impl.getProtocol()+"://" + impl.getHost() + "/?Password=");
			temperaturePage = temperatureUrl.openConnection();
			temperaturePage.setConnectTimeout(CONN_TIMEOUT);
			temperaturePage.setReadTimeout(CONN_TIMEOUT);
			br = new BufferedReader(new InputStreamReader(
					temperaturePage.getInputStream()));

			logger.debug("IPSwitchLooper: run: Read T");
			Temperature(br);
			br.close();
			logger.debug("IPSwitchWrapperImpl: run: Finished T");
			logger.debug("IPSwitchWrapperImpl: run: Closed Readers");

	} catch (MalformedURLException e) {
		logger.warn("IPSwitchWrapperImpl: run: Malformed URL used for IPSwitch", e);
	} catch (SocketTimeoutException e) {
		logger.info("IPSwitchWrapperImpl: run: TimeOut while connecting to IPSwitch", e);
	} catch (IOException e) {
		logger.warn("IOException with Streams/Readers", e);
	}
}
		
		void Counter(BufferedReader input) {
			String inputLine;
			int counterNo = 1;
			boolean found = false;
			double counterValue = 0.0;
			DeviceContainer origin = null;
			
			try {
				while (((inputLine = input.readLine()) != null) && (found == false)) {

					StringTokenizer tok = new StringTokenizer(inputLine, "<br>");
					while ((tok.hasMoreElements()) && (found == false)) {
						String counter = "iC" + counterNo + "=";
						String str1 = tok.nextToken();
						StringTokenizer blank = new StringTokenizer(str1);
						while ((blank.hasMoreElements()) && (found == false)) {
							String str2 = blank.nextToken();
							if (str2.equals(counter)) {
								String value = blank.nextToken();
								if (str2.equals("iC1=")) {
									int len = value.length();
									value = value.substring(0, len - 1);
									origin = impl.getDevices().get(0);
								} else if (str2.equals("iC2=")) {
									origin = impl.getDevices().get(1);
								} else if (str2.equals("iC3=")) {
									origin = impl.getDevices().get(2);
									found = true;
								}
								counterValue = Double.parseDouble(value);
								counterNo++;
								//Counts impulses (1k per kWh) Accuracy in impulses perfect
								//Double-division is assumed to have almost no influence
								ev = new DoubleEvent(counterValue);
								impl.getMaster().sendDoubleEvent(ev, origin.getDeviceId(), impl.getClientId());
								
								logger.debug("IPSwitchWrapperImpl: Counter: CounterValue " + counterValue);
							}
						}
					}
				}
			} catch (IOException e) {
				logger.warn(
						"IPSwitchWrapperImpl: Counter: Stream/Reader Exception while reading IPSwitch Temperature",
						e);
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				logger.debug("Sending doubleEvent Timed out");
			}
		}

		void Temperature(BufferedReader br) {
			String readLine;
			double tempValue = 0.0;
			boolean found = false;
			try {
				while (((readLine = br.readLine()) != null) && (found == false)) {
					StringTokenizer token = new StringTokenizer(readLine, "<br>");
					while ((token.hasMoreElements()) && (found == false)) {
						String str = token.nextToken();
						StringTokenizer blanktoken = new StringTokenizer(str);
						while (blanktoken.hasMoreElements()) {
							String temp = blanktoken.nextToken();
							if (temp.equals("iTi=")) {
								found = true;
								temp = blanktoken.nextToken();
								String value = temp.substring(0, temp.length() - 3);
								tempValue = Double.parseDouble(value);

								ev = new DoubleEvent(tempValue);
								impl.getMaster().sendDoubleEvent(ev, impl.getDevices().get(3).getDeviceId(), impl.getClientId());
								logger.debug("IPSwitchWrapperImpl: Temperature: T  " + tempValue);
							}
							break;
						}
					}
				}
			} catch (IOException e) {
				logger.warn(
						"IPSwitchWrapperImpl: Temperature: Stream/Reader Exception while reading IPSwitch Temperature",
						e);
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				logger.debug("Sending doubleEvent Timed out");
			}
		}

}
