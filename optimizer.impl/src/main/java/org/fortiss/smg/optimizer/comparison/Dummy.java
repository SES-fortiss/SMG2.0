/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.optimizer.comparison;

import static org.fortiss.smg.optimizer.data.Specification.getSpecification;

import org.fortiss.smg.optimizer.data.Interval;

/**
 * Utilities of an advisor with a dummy algorithm
 * 
 * @author Cheng Zhang
 * @version 1.0
 */
public class Dummy {

	/***
	 * Generate an optimized schedule with given intervals
	 * 
	 * @param intervals
	 * @return
	 */
	public static void doOptimize(Interval[] intervals) {
		// Log
		// log.finest(Arrays.toString(intervals));
		// log.finer(Specification.getSpecification().toString());

		double capacity = getSpecification().getInitialCapacity();
		for (Interval interval : intervals) {
			capacity += getBatteryExchange(interval, capacity);
		}
	}

	/** Get possible battery exchange at an interval */
	private static double getBatteryExchange(Interval interval, double capacity) {
		double batteryExchange = getSpecification().getDummyBatteryExchange(
				interval.getConsumption(), interval.getGeneration(), capacity,
				interval.getDuration());
		interval.setBatteryExchange(batteryExchange);
		return batteryExchange;
	}

}
