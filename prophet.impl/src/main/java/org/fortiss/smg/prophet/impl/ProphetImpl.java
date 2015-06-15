/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.prophet.impl;

import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.prophet.api.ProphetInterface;
import org.fortiss.smg.ambulance.api.HealthCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProphetImpl implements ProphetInterface {

	private static Logger logger = LoggerFactory.getLogger(ProphetImpl.class);

	
	public ProphetImpl() {
		// Here we can generate e.g. regular forecasts (we can pass this to a looper which
		// calls this.getProductionForecast()
		
	}
	
	
	
	
	public String doSomething(String s) {
		return "Hello smg";
	}

	@Override
	public boolean isComponentAlive() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public HashMap<Long, Double> getProductionForecast(long starttime,
			int durationHours, int intervalMinutes, String containerId)
			throws TimeoutException {
		HashMap<Long, Double> result = new HashMap<Long, Double>();
		// TODO Auto-generated method stub
		int datapoints = (int) (durationHours * 60.0) / intervalMinutes;

		for (int i = 0; i < datapoints; i++) {
			long timeslot = starttime + (i * intervalMinutes * 60000);
			result.put(timeslot, 0.0);
		}
		return result;
	}

	@Override
	public HashMap<Long, Double> getConsumptionForecast(long starttime,
			int durationHours, int intervalMinutes, String containerId)
			throws TimeoutException {
		HashMap<Long, Double> result = new HashMap<Long, Double>();
		// TODO Auto-generated method stub
		int datapoints = (int) (durationHours * 60.0) / intervalMinutes;

		for (int i = 0; i < datapoints; i++) {
			long timeslot = starttime + (i * intervalMinutes * 60000);
			result.put(timeslot, 0.0);
		}
		return result;
		}
}
