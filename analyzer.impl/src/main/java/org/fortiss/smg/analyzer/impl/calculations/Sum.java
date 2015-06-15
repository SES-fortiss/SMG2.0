/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.analyzer.impl.calculations;

import java.util.Iterator;
import java.util.List;

import org.fortiss.smg.analyzer.impl.calculations.centralTendency.CalculationMethods;
import org.fortiss.smg.informationbroker.api.DoublePoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ann Katrin Gibtner (annkatrin.gibtner@tum.de)
 *
 */
public class Sum extends CalculationMethods {

	private final static Logger logger = LoggerFactory.getLogger(Sum.class);

	/**
	 * calculates sum over all Elements in the list
	 * 
	 * @param list
	 *            list which elements should be summed up
	 * 
	 * @return sum as double
	 * @throws IllegalArgumentException
	 *             if {@code list} is empty or null
	 */
	@Override
	public Double calculate(List<DoublePoint> list)
			throws IllegalArgumentException {
		if (list == null || list.isEmpty()) {
			logger.warn("no points available");
			throw new IllegalArgumentException("list empty or null");
		}
		double sum = 0.0;
		for (Iterator<DoublePoint> iterator = list.iterator(); iterator
				.hasNext();) {
			DoublePoint current = (DoublePoint) iterator.next();
			if (current == null || current.getValue() == null) {
			} else {
				sum += current.getValue();
			}
		}
		return sum;
	}

}
