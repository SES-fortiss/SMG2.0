/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.analyzer.impl.calculations.centralTendency;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.fortiss.smg.informationbroker.api.DoublePoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ann Katrin Gibtner (annkatrin.gibtner@tum.de)
 *
 */
abstract public class CalculationMethods {

	private final static Logger logger = LoggerFactory
			.getLogger(CalculationMethods.class);

	public abstract Double calculate(List<DoublePoint> list)
			throws IllegalArgumentException, ArithmeticException;

	/**
	 * Sorts the elements of a given list and removes all elements which are
	 * null or hold the value null
	 * 
	 * @param list
	 *            list that should be sorted
	 */
	public static void sort(List<DoublePoint> list) {
		if (list == null || list.isEmpty()) {
			logger.warn("no points available");
		} else {
			for (Iterator<DoublePoint> iterator = list.iterator(); iterator
					.hasNext();) {
				DoublePoint myPoint = (DoublePoint) iterator.next();
				if (myPoint == null || myPoint.getValue() == null) {
					iterator.remove();
				}
			}
			Collections.sort(list, new Comparator<DoublePoint>() {
				public int compare(DoublePoint o1, DoublePoint o2) {
					return Double.compare(o1.getValue(), o2.getValue());
				}
			});
		}
	}

}
