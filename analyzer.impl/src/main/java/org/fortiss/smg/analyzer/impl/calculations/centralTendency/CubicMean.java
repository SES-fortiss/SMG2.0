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
public class CubicMean extends CalculationMethods {

	private final static Logger logger = LoggerFactory
			.getLogger(CubicMean.class);

	/**
	 * Calculates cubic mean
	 * 
	 * @param list
	 *            List including the values of which the mean will be calculated
	 * @return the cubic mean will be returned
	 * @throws IllegalArgumentException
	 *             if {@code list} is null or empty
	 * @throws ArithmeticException
	 *             if sum of all values is negative
	 */
	@Override
	public Double calculate(List<DoublePoint> list)
			throws IllegalArgumentException, ArithmeticException {
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
				sum += Math.pow(singleEle.getValue(), 3);
			}
		}
		if (sum < 0.0) {
			logger.warn("sum of all values is negative");
			throw new ArithmeticException("sum of all values is negative");
		}
		return Math.pow(sum / (double) size, 1.0 / 3.0);
	}

}
