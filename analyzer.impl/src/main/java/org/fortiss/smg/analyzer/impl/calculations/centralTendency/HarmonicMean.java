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
public class HarmonicMean extends CalculationMethods {

	private final static Logger logger = LoggerFactory
			.getLogger(HarmonicMean.class);

	/**
	 * Calculates the harmonic mean
	 * 
	 * @param list
	 *            List including the values of which the mean will be calculated
	 * @return 0.0 if any element of the list has the value 0.0, else the
	 *         harmonic mean
	 * @throws IllegalArgumentException
	 *             if {@code list} is empty or null
	 * 
	 */
	@Override
	public Double calculate(List<DoublePoint> list)
			throws IllegalArgumentException {
		if (list == null || list.isEmpty()) {
			logger.warn("no points available");
			throw new IllegalArgumentException("list is empty or null");
		}
		int size = list.size();
		double sum = 0.0;
		for (Iterator<DoublePoint> iterator = list.iterator(); iterator
				.hasNext();) {
			DoublePoint singleEle = (DoublePoint) iterator.next();
			if (singleEle == null || singleEle.getValue() == null) {
				size--;
			} else {
				if (singleEle.getValue() == 0.0) {
					logger.warn("some values is 0.0 -> returning 0.0");
					return 0.0;
				}
				sum += 1 / singleEle.getValue();
			}
		}
		return (double) size / sum;
	}

}
