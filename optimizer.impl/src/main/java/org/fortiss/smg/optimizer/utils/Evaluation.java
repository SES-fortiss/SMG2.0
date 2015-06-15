/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.optimizer.utils;

import static org.fortiss.smg.optimizer.utils.Export.info;
import static org.fortiss.smg.optimizer.utils.Tools.sum;

import java.util.ArrayList;
import java.util.List;

import org.fortiss.smg.optimizer.data.Interval;

/**
 * Utilities to evaluate intervals.
 * 
 * @author Cheng Zhang
 * @version 1.0
 */
public class Evaluation {

	public static void doEvaluate(Interval[] intervals) {
		unused(intervals);
	}

	public static double cost(Interval[] intervals) {
		return (-sum(evaluate(intervals)) / 100000);
	}

	public static double[] evaluate(Interval[] intervals) {
		double[] cost = new double[intervals.length];
		for (int i = 0; i < intervals.length; i++) {
			cost[i] = intervals[i].getCost();
		}
		return cost;
	}

	public static Interval[] unused(Interval[] intervals) {
		List<Interval> unused = new ArrayList<Interval>();
		for (Interval interval : intervals) {
			if (interval.hasWastage()) {
				unused.add(interval);
				info(interval.toString() + "[Wastage:" + -interval.getWastage()
						+ "]");
			}
		}
		return unused.toArray(new Interval[unused.size()]);
	}
}
