/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.analyzer.impl.calculations.centralTendency;

import java.util.Iterator;
import java.util.List;

import org.fortiss.smg.informationbroker.api.DoublePoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ann Katrin Gibtner (annkatrin.gibtner@tum.de)
 *
 */
public class GeometricMean extends CalculationMethods {

	private final static Logger logger = LoggerFactory
			.getLogger(GeometricMean.class);

	/**
	 * Calculates the geometric mean NOTE: only defined for positive values
	 * 
	 * @param list
	 *            List including the values of which the mean will be calculated
	 * @return the geometric mean will be returned
	 * @throws IllegalArgumentException
	 *             if {@code list} is null or empty
	 * @throws ArithmeticException
	 *             if one value is negative
	 */
	@Override
	public Double calculate(List<DoublePoint> list)
			throws IllegalArgumentException, ArithmeticException {
		if (list == null || list.isEmpty()) {
			logger.warn("no points available");
			throw new IllegalArgumentException("list is null or empty");
		}
		int size = list.size();
		double product = 1;
		double largeProduct = 1;
		for (Iterator<DoublePoint> iterator = list.iterator(); iterator
				.hasNext();) {
			DoublePoint singleEle = (DoublePoint) iterator.next();
			if (singleEle == null || singleEle.getValue() == null) {
				size--;
			} else {
				if (singleEle.getValue() < 0.0) {
					logger.warn("negative value found - not definied");
					throw new ArithmeticException(
							"geometric mean is not defined for negative values");
				}
				if (product >= Math.sqrt(Double.MAX_VALUE)) {
					largeProduct *= Math.pow(product, 1.0 / (double) size);
					product = 1;
				}
				product *= singleEle.getValue();
			}
		}
		return Math.pow(product, 1.0 / (double) size) * largeProduct;
	}

}
