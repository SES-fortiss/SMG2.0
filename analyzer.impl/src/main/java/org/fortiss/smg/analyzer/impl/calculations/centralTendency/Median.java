package org.fortiss.smg.analyzer.impl.calculations.centralTendency;

import java.util.List;

import org.fortiss.smg.informationbroker.api.DoublePoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ann Katrin Gibtner (annkatrin.gibtner@tum.de)
 *
 */
public class Median extends CalculationMethods {

	private final static Logger logger = LoggerFactory.getLogger(Median.class);

	/**
	 * Calculates the median if the median is not unique the arithmetic mean of
	 * the upper and lower median is calculated
	 * 
	 * @param list
	 *            List including the values of which the mean will be calculated
	 * @return median is returned
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
		} else {
			int lowerMedian;
			int upperMedian;

			lowerMedian = (list.size() - 1) / 2;
			upperMedian = (list.size()) / 2;
			return (list.get(lowerMedian).getValue() + list.get(upperMedian)
					.getValue()) / 2;
		}
	}

}
