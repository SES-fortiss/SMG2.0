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
public class RootMeanSquare extends CalculationMethods {

	private final static Logger logger = LoggerFactory
			.getLogger(RootMeanSquare.class);

	/**
	 * Calculates root mean square
	 * 
	 * @param list
	 *            List including the values of which the mean will be calculated
	 * @return square mean
	 * @throws IllegalArgumentException
	 *             if {@code list} is null or empty
	 */
	@Override
	public Double calculate(List<DoublePoint> list)
			throws IllegalArgumentException {
		if (list == null || list.isEmpty()) {
			logger.warn("no points available");
			throw new IllegalArgumentException("list is null or empty");
		}
		int size = list.size();
		double sum = 0.0;
		for (Iterator<DoublePoint> iterator = list.iterator(); iterator
				.hasNext();) {
			DoublePoint singleEle = (DoublePoint) iterator.next();
			if (singleEle == null || singleEle.getValue() == null) {
				size--;
			} else {
				sum += Math.pow(singleEle.getValue(), 2);
			}
		}
		return Math.sqrt(sum / (double) size);
	}

}
