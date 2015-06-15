/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.optimizer.advisor;

import static org.fortiss.smg.optimizer.advisor.Decision.findDecision;
import static org.fortiss.smg.optimizer.advisor.Postprocess.postProcess;
import static org.fortiss.smg.optimizer.advisor.Preprocess.preProcess;
import static org.fortiss.smg.optimizer.data.Period.toPeriods;
import static org.fortiss.smg.optimizer.data.Specification.getSpecification;
import static org.fortiss.smg.optimizer.utils.Export.fine;
import static org.fortiss.smg.optimizer.utils.Export.finer;
import static org.fortiss.smg.optimizer.utils.Export.info;
import static org.fortiss.smg.optimizer.utils.Export.setExecuteTime;

import java.util.Arrays;

import org.fortiss.smg.optimizer.data.Interval;
import org.fortiss.smg.optimizer.data.Period;

/**
 * Utilities of the advisor for optimizing schedule.
 * 
 * @author Cheng Zhang
 * @version 1.0
 */
public class Advisor {

	/***
	 * Generate an optimized schedule with given intervals
	 * 
	 * @param intervals
	 * @return
	 */
	public static void doOptimize(Interval[] intervals) {
		setExecuteTime();
		// Log
		info("Export Specification: " + getSpecification().toString(),
				getSpecification());
		Period[] periods = toPeriods(intervals);
		// Log
		finer(Arrays.toString(periods));
		// Log for chart
		info("Export Demand, Least Supply and Maximum Supply of Intervals:"
				+ Arrays.toString(Interval.exportIntervals(intervals)));
		// Preprocess period
		preProcess(periods);
		// Log
		fine("Export Possible Demand, Least Supply, Maximum Supply and Needed Storage of Periods:"
				+ Arrays.toString(Period.exportPeriods(periods)));
		// Find a optimzation
		if (!findDecision(periods))
			return;
		info("Export Possible Demand, Least Supply, Maximum Supply, Needed Storage and Exchange of Periods:"
				+ Arrays.toString(Period.exportFinalPeriods(periods)), periods);
		// Update relevant data
		postProcess(periods);
		info("Export Consumption, Generation, From Grid, To Grid, Charging and Discharging Powers of Intervals:"
				+ Arrays.toString(Interval.exportPowers(intervals)), intervals);
	}

}
