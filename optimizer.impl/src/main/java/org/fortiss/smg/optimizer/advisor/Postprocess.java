/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.optimizer.advisor;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static org.fortiss.smg.optimizer.data.Specification.getSpecification;
import static org.fortiss.smg.optimizer.utils.Tools.range;
import static org.fortiss.smg.optimizer.utils.Tools.sum;

import org.fortiss.smg.optimizer.data.Period;

/**
 * Utilities of the post process in the advisor.
 * 
 * @author Cheng Zhang
 * @version 1.0
 */
public class Postprocess {

	/***
	 * Update intervals of price periods
	 * 
	 * @param periods
	 * @return
	 */
	public static void postProcess(Period[] periods) {
		double currentCapacity = getSpecification().getInitialCapacity();
		for (Period period : periods) {
			currentCapacity = update(period, currentCapacity);
		}
	}

	/** update intervals in a price period */
	private static double update(Period period, double currentCapacity) {

		double[] demands = period.getDemands();
		double[] supplies = period.getSupplies();
		double total = sum(period.getDemands(), true);
		double exchange = period.getExchange();
		double leastSupply = period.getLeastSupply();
		double initCapacity = currentCapacity;

		double[] batteryExchanges = new double[demands.length];
		if (exchange > 0 && total < exchange + leastSupply) {
			double gap = exchange + leastSupply - total;
			for (int i = demands.length - 1; i >= 0; i--) {
				batteryExchanges[i] = range(max(demands[i], 0), supplies[i],
						gap);
				gap -= batteryExchanges[i] - max(demands[i], 0);
			}
			currentCapacity += exchange;
		} else {
			double extraDemands[] = new double[demands.length];
			if (exchange > 0) {
				double extra = -exchange;
				for (int i = demands.length - 1; i >= 0; i--) {
					if (demands[i] < 0 && extra > 0) {
						extraDemands[i] = max(extra, demands[i]);
						extra -= extraDemands[i];
					}
				}
			}
			currentCapacity = update(currentCapacity + min(exchange, 0),
					-min(exchange, 0), demands, extraDemands, batteryExchanges);
		}
		// period.update(batteryExchanges);
		period.update(batteryExchanges, initCapacity);
		return currentCapacity;
	}

	/** prepare battery exchanges at intervals in a price period */
	public static double update(double currentCapacity, double capacity,
			double[] demands, double[] extraDemands, double[] batteryExchanges) {
		for (int i = 0; i < demands.length; i++) {
			double demand = demands[i] - extraDemands[i];
			if (demand < 0 && capacity > 0) {
				batteryExchanges[i] = max(demand, -capacity);
				capacity += batteryExchanges[i];
			} else if (demand > 0
					&& capacity + currentCapacity < getSpecification()
							.getMaximumCapacity()) {
				batteryExchanges[i] = min(demand, getSpecification()
						.getMaximumCapacity() - capacity - currentCapacity);
				capacity += batteryExchanges[i];
			}
		}
		return currentCapacity + capacity;
	}
}
