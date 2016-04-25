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
public class ArithmeticMean extends CalculationMethods {

	private Logger logger = LoggerFactory.getLogger(ArithmeticMean.class);

	/**
	 * Calculates the arithmetic mean
	 * 
	 * @param list
	 *            List including the values of which the mean will be calculated
	 * @return arithmetic mean
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
		double sum = 0.0;
		int size = list.size();
		for (Iterator<DoublePoint> iterator = list.iterator(); iterator
				.hasNext();) {
			DoublePoint singleEle = (DoublePoint) iterator.next();
			if (singleEle == null || singleEle.getValue() == null) {
				size--;
			} else {
				sum += singleEle.getValue();
			}
		}
		return sum / (double) size;
	}

	/**
	 * Calculates the trimmed arithmetic mean where the top and bottom 2 percent
	 * of a sorted version of the list are removed
	 * 
	 * @param list
	 *            all values
	 * @return trimmed arithmetic mean
	 * @throws IllegalArgumentException
	 *             if {@code list} is null or empty
	 */
	public Double trimmed(List<DoublePoint> list) {
		CalculationMethods.sort(list);
		if (list == null || list.isEmpty()) {
			logger.warn("trimmed: no points available");
			throw new IllegalArgumentException("list is null or empty");
		}
		List<DoublePoint> myList = list;
		int chop = (int) (0.02 * myList.size());
		for (int i = 0; i < chop; i++) {
			myList.remove(0);
			myList.remove(myList.size() - 1);
		}
		//no exception will occur - would have be caught earlier
		return calculate(myList);
	}

	/**
	 * Calculates the trimmed arithmetic mean with a variable trimmer
	 * 
	 * @param list
	 *            all values
	 * @param trimmer
	 *            determine how many percent of the list should be removed;
	 *            please chose the trimmer to be in the interval
	 *            {@code [1.0; 0.0]}
	 * @return trimmed arithmetic mean is returned
	 * @throws IllegalArgumentException
	 *             if {@code list} is null or empty or if {@code trimmer} is >
	 *             1.0 or < 0.0
	 * @see #trimmed(List)
	 */
	public Double trimmedVar(List<DoublePoint> list, double trimmer) {
		CalculationMethods.sort(list);
		if (list == null || list.isEmpty()) {
			logger.warn("trimmedVar: no points available");
			throw new IllegalArgumentException("list is empty or null");
		}
		if (trimmer > 1.0 || trimmer < 0.0) {
			logger.warn("trimmedVar: value for trimmer is not valid");
			throw new IllegalArgumentException(
					"trimmer is not valid: > 1.0 or < 0.0");
		}
		int chop = (int) (trimmer * list.size());
		List<DoublePoint> myList = list;
		for (int i = 0; i < chop; i++) {
			myList.remove(0);
			myList.remove(myList.size() - 1);
		}
		//no exception will occur - would have been caught earlier
		return calculate(list);
	}

}
