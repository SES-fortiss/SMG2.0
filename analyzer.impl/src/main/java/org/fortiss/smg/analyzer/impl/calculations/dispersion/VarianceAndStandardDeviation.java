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

import org.fortiss.smg.analyzer.impl.calculations.centralTendency.ArithmeticMean;
import org.fortiss.smg.analyzer.impl.calculations.centralTendency.RootMeanSquare;
import org.fortiss.smg.informationbroker.api.DoublePoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ann Katrin Gibtner (annkatrin.gibtner@tum.de)
 *
 */
public class VarianceAndStandardDeviation {

	private final static Logger logger = LoggerFactory
			.getLogger(VarianceAndStandardDeviation.class);

	/**
	 * Calculates the variance of the given list
	 * 
	 * @param list
	 *            all elements
	 * @return variance
	 * @throws IllegalArgumentException
	 *             if {@code list} is null or empty
	 */
	public static Double variance(List<DoublePoint> list)
			throws IllegalArgumentException {
		if (list == null || list.isEmpty()) {
			logger.warn("no points available");
			throw new IllegalArgumentException("list is empty or null");
		}
		RootMeanSquare squareMean = new RootMeanSquare();
		ArithmeticMean arithMean = new ArithmeticMean();
		// no exception will occur - would have been caught
		// earlier
		return Math.pow(squareMean.calculate(list), 2)
				- Math.pow(arithMean.calculate(list), 2);
	}

	/**
	 * Calculates the standard deviation of the given list
	 * 
	 * @param list
	 *            all elements
	 * @return standard deviation is returned
	 * @throws IllegalArgumentException
	 *             if {@code list} is null or empty
	 */
	public static Double standardDeviation(List<DoublePoint> list)
			throws IllegalArgumentException {
		if (list == null || list.isEmpty()) {
			logger.warn("no points available");
			throw new IllegalArgumentException("list is null or empty");
		}
		//cannot cause exception - would have been caught earlier
		return Math.sqrt(variance(list));
	}
}
