/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.actuatorclient.hexabus.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import org.slf4j.LoggerFactory;
import org.neo4j.shell.util.json.JSONException;
import org.neo4j.shell.util.json.JSONObject;

public class JSONReaderHexabus {

	private HashSet<String> supportedUnits = new HashSet<String>();

	private static org.slf4j.Logger logger = LoggerFactory
			.getLogger(JSONReaderHexabus.class);
	private Calendar lastEventTime;

	private List<String[]> eventDataList = new ArrayList<String[]>();
	List<String[]> results;

	public JSONReaderHexabus() {
		lastEventTime = new GregorianCalendar();
		lastEventTime.add(Calendar.HOUR, -1); // server is 1 hr slower than the
												// current time.
		lastEventTime.add(Calendar.HOUR, -1); // to solve summer time difference
												// between server and realtime
		// set supported units(Units, that should be parsed from the Json)
		supportedUnits.add("W");
		supportedUnits.add("degC");
		supportedUnits.add("%r.h.");
	}

	public Calendar getLastEventString() {
		return lastEventTime;
	}

	public void setLastEventString(Calendar lastEventString) {
		this.lastEventTime = lastEventString;
	}

	public List<String[]> getEventDataList() {
		return eventDataList;
	}

	public void setEventDataList(List<String[]> eventDataList) {
		this.eventDataList = eventDataList;
	}

	/**
	 * 
	 * @param requestUrlStr
	 * @return [0] = ip.endpointId [1] = value [2] = minValue [3] = maxValue [4]
	 *         = unit
	 */
	public List<String[]> readJson_Hexabus(String requestUrlStr) {
		JSONObject hexabusJson = null;
		JSONObject hexabusJsonDevice = null;
		JSONObject hexabusJsonEndpoint = null;
		results = new ArrayList<String[]>();

		// connect to the JSON Link
		try {
			URL requestUrl = new URL(requestUrlStr);
			Scanner scanner = new Scanner(requestUrl.openStream());
			String response = scanner.useDelimiter("\\Z").next();
			hexabusJson = new JSONObject(response.toString());
			scanner.close();
		} catch (Exception e) {
			logger.info("JSONReaderHexabus could not connect to the parsing webpage");
		}

		// parse the JSON
		try {
			if (hexabusJson != null) {
				hexabusJson = (JSONObject) hexabusJson.get("devices");
				HashSet<String> devices = new HashSet<String>();
				for (int i = 0; i < hexabusJson.names().length(); i++) {
					logger.info("Device found "
							+ hexabusJson.names().getString(i));
					devices.add(hexabusJson.names().getString(i));
				}
				for (String dev : devices) {
					hexabusJsonDevice = (JSONObject) hexabusJson.get(dev);
					String hrName = hexabusJsonDevice.getString("name");
					hexabusJsonDevice = (JSONObject) hexabusJsonDevice
							.get("endpoints");

					HashSet<String> endpoints = new HashSet<String>();
					for (int i = 0; i < hexabusJsonDevice.names().length(); i++)
						endpoints.add(hexabusJsonDevice.names().getString(i));
					// Check all endpoints of the device
					for (String endpoint : endpoints) {
						hexabusJsonEndpoint = (JSONObject) hexabusJsonDevice
								.get(endpoint);
						String unit = "";
						if (hexabusJsonEndpoint.has("unit"))
							unit = hexabusJsonEndpoint.getString("unit");
						if (hexabusJsonEndpoint.has("last_value")
								&& supportedUnits.contains(unit)) {
							results.add(new String[] {
									hexabusJsonEndpoint.getString("id"),
									hexabusJsonEndpoint.getJSONObject(
											"last_value").getString("value"),
									hexabusJsonEndpoint.getString("minvalue"),
									hexabusJsonEndpoint.getString("maxvalue"),
									unit,
									hrName,
									hexabusJsonEndpoint
											.getString("description") });
						}
					}

					/*
					 * Nicht dynamischer code
					 * logger.debug("Looking for Endpoints (Sensors) for Device "
					 * + dev + " Spec says: " + devspec.get(dev));
					 * 
					 * for(String endpoints : devspec.get(dev)) {
					 * 
					 * results.add( new
					 * String[]{hexabusJsonDevice.getJSONObject(
					 * endpoints).getString("id"),
					 * hexabusJsonDevice.getJSONObject
					 * (endpoints).getJSONObject("last_value"
					 * ).getString("value"),
					 * hexabusJsonDevice.getJSONObject(endpoints
					 * ).getString("minvalue"),
					 * hexabusJsonDevice.getJSONObject(endpoints
					 * ).getString("maxvalue"),
					 * hexabusJsonDevice.getJSONObject(endpoints
					 * ).getString("unit"), hrName,
					 * hexabusJsonDevice.getJSONObject
					 * (endpoints).getString("description")} ); }
					 */

				}
				logger.info("############### JSON Data parsed ###############",
						eventDataList.size());
			}
			return results;
		} catch (JSONException e) {
			logger.debug("Hexabus has not parsed the Json");
			e.printStackTrace();
		}
		return new ArrayList<String[]>();
	}
}
