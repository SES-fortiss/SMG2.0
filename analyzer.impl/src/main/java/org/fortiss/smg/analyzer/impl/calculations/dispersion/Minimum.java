/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.analyzer.impl.calculations.dispersion;

import java.util.List;

import org.fortiss.smg.analyzer.impl.calculations.centralTendency.CalculationMethods;
import org.fortiss.smg.informationbroker.api.DoublePoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ann Katrin Gibtner (annkatrin.gibtner@tum.de)
 *
 */
public class Minimum extends CalculationMethods {

	private final static Logger logger = LoggerFactory.getLogger(Minimum.class);

	/**
	 * Returns the element of the list with the smallest value
	 * 
	 * @param list
	 *            all elements
	 * @return the smallest value
	 * @throws IllegalArgumentException
	 *             if {@code list} is null or empty
	 */
	@Override
	public Double calculate(List<DoublePoint> list)
			throws IllegalArgumentException {
		CalculationMethods.sort(list);
		if (list == null || list.isEmpty()) {
			logger.warn("no points available");
			throw new IllegalArgumentException("list is null or empty");
		}
		return list.get(0).getValue();
	}

}
