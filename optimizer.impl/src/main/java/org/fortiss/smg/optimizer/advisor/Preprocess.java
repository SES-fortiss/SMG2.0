/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.optimizer.advisor;

import static java.lang.Math.min;
import static org.fortiss.smg.optimizer.data.Specification.getSpecification;
import static org.fortiss.smg.optimizer.utils.Tools.Amplitude;
import static org.fortiss.smg.optimizer.utils.Tools.Balance;
import static org.fortiss.smg.optimizer.utils.Tools.bound;
import static org.fortiss.smg.optimizer.utils.Tools.cumulative;
import static org.fortiss.smg.optimizer.utils.Tools.reverse;
import static org.fortiss.smg.optimizer.utils.Tools.sum;

import org.fortiss.smg.optimizer.data.Period;

/**
 * Utilities of the pre process in the advisor.
 * 
 * @author Cheng Zhang
 * @version 1.0
 */
public class Preprocess {

	/***
	 * Prepare basic data of price periods
	 * 
	 * @param periods
	 * @return
	 */
	public static void preProcess(Period[] periods) {
		for (Period period : periods) {
			double[] pendulum = bound(cumulative(period.getDemands()), 0,
					-getSpecification().getMaximumCapacity());
			double neededStorage = pendulum[Amplitude]
					+ getSpecification().getMaximumCapacity();
			double possibleDemand = pendulum[Balance];
			double leastSupply = bound(
					cumulative(reverse(period.getDemands())), 0,
					getSpecification().getMaximumCapacity())[Balance];
			double maximumSupply = min(sum(period.getSupplies()),
					getSpecification().getMaximumCapacity()) - leastSupply;
			period.update(possibleDemand, leastSupply, maximumSupply,
					neededStorage);
		}
	}
}
