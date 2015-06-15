/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.analyzer.impl.calculations.centralTendency;

import java.util.List;

import org.fortiss.smg.informationbroker.api.DoublePoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ann Katrin Gibtner (annkatrin.gibtner@tum.de)
 *
 */
public class PercentBool extends CalculationMethods {

	private final static Logger logger = LoggerFactory
			.getLogger(PercentBool.class);

	/**
	 * Calculates the percentage of boolean events. E.g. what is the percentage
	 * of "true" events to all events
	 * 
	 * @param list
	 *            List including the values of which the mean will be calculated
	 * @return percent
	 * @throws IllegalArgumentException
	 *             if {@code list} is empty or null
	 */
	@Override
	public Double calculate(List<DoublePoint> list)
			throws IllegalArgumentException {
		if (list == null || list.isEmpty()) {
			logger.warn("no points available");
			throw new IllegalArgumentException("list is empty or null");
		}
		ArithmeticMean arith = new ArithmeticMean();
		Double percent;
		try {
			percent = arith.calculate(list) * 100;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw e;
		}
		return percent;
	}
}
