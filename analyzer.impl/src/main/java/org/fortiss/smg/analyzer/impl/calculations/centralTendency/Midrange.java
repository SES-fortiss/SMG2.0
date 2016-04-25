package org.fortiss.smg.analyzer.impl.calculations.centralTendency;

import java.util.List;

import org.fortiss.smg.informationbroker.api.DoublePoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ann Katrin Gibtner (annkatrin.gibtner@tum.de)
 *
 */
public class Midrange extends CalculationMethods {

	private final static Logger logger = LoggerFactory
			.getLogger(Midrange.class);

	/**
	 * Calculates the midrange of a list. (The average of the greatest and the
	 * smallest value of the list)
	 * 
	 * @param list
	 *            all elements
	 * @return the midrange is returned
	 * @throws IllegalArgumentException
	 *             if {@code list} is null or empty
	 */
	@Override
	public Double calculate(List<DoublePoint> list)
			throws IllegalArgumentException {
		CalculationMethods.sort(list);
		if (list == null || list.isEmpty()) {
			logger.warn("no points available");
			throw new IllegalArgumentException("list is empty or null");
		}
		return (list.get(0).getValue() + list.get(list.size() - 1).getValue()) / 2;
	}

}
