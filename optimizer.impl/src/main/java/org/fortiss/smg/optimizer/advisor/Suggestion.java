/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.optimizer.advisor;

import static org.fortiss.smg.optimizer.data.Specification.getSpecification;
import static org.fortiss.smg.optimizer.utils.Evaluation.cost;

import org.fortiss.smg.optimizer.data.Interval;

/**
 * Utilities of the suggestion in the advisor.
 * 
 * @author Cheng Zhang
 * @version 1.0
 */
public class Suggestion {

	/***
	 * Generate a expected maximum capacity of the battery with given intervals
	 * and current cost
	 * 
	 * @param intervals
	 * @param cost
	 * @return
	 */
	public static double getSuggestion(Interval[] intervals, double cost) {
		if (intervals == null || intervals.length == 0)
			return -1;
		double leastCapacity = getSpecification().getMaximumCapacity();
		do {
			getSpecification().setMaximumCapacity(
					leastCapacity + getSpecification().getMaximumCapacity());
			Interval[] newIntervals = Interval.copy(intervals);
			Advisor.doOptimize(newIntervals);
			double comparable = cost(newIntervals);
			if (cost == comparable) {
				break;
			}
			cost = comparable;
		} while (true);
		return getSpecification().getMaximumCapacity();
	}
}
