/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.analyzer.impl.calculations.centralTendency;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.fortiss.smg.informationbroker.api.DoublePoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ann Katrin Gibtner (annkatrin.gibtner@tum.de)
 *
 */
public class Mode {

	private final static Logger logger = LoggerFactory.getLogger(Mode.class);

	/**
	 * Calculates the elements of the list with the most occurring values. (e.g.
	 * list has the values {@code [ 1, 2, 6, 1, 1, 3, 2, 4, 2 ]} the function
	 * will return 1 and 2.
	 * 
	 * @param list
	 *            all given values
	 * @return mode is returned. If all values just occur one time no value is
	 *         returned.
	 * @throws IllegalArgumentException
	 *             if {@code list} is null or empty
	 */
	public static List<Double> calcMode(List<DoublePoint> list)
			throws IllegalArgumentException {
		CalculationMethods.sort(list);
		if (list == null || list.isEmpty()) {
			logger.warn("no points available");
			throw new IllegalArgumentException("list is empty or null");
		}
		List<Double> solution = new ArrayList<Double>();
		int counter = 0;
		int golbcounter = 2;
		DoublePoint comparer = list.get(0);
		for (Iterator<DoublePoint> iterator = list.iterator(); iterator
				.hasNext();) {
			DoublePoint point1 = (DoublePoint) iterator.next();
			if (point1.getValue().doubleValue() == comparer.getValue()
					.doubleValue()) {
				counter++;
				if (counter > golbcounter) {
					golbcounter = counter;
					solution.clear();
					solution.add(point1.getValue());
				} else if (counter == golbcounter) {
					solution.add(point1.getValue());
				}
			} else {
				comparer = point1;
				counter = 1;
			}
		}
		return solution;
	}
}
