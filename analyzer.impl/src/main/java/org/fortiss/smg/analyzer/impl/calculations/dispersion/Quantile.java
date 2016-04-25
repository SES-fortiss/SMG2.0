package org.fortiss.smg.analyzer.impl.calculations.dispersion;

import java.util.ArrayList;
import java.util.List;

import org.fortiss.smg.analyzer.impl.calculations.centralTendency.CalculationMethods;
import org.fortiss.smg.informationbroker.api.DoublePoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ann Katrin Gibtner (annkatrin.gibtner@tum.de)
 *
 */
public class Quantile {

	private final static Logger logger = LoggerFactory
			.getLogger(Quantile.class);

	/**
	 * Calculates a single quantile of the given list. A Quantile is used to
	 * divide the data of the given list. Therefore a quantile of e.g. 0.4 will
	 * separate 40% of the data with the lowest values from the rest. All values
	 * lower the returned value belong to the lower 40%
	 * 
	 * @param list
	 *            all elements
	 * @param quantile
	 *            indicates where the data should be divided. Chose the quantile
	 *            out of the interval ]0.0; 1.0[.
	 * @return boundary value for dividing the data is returned
	 * @throws IllegalArgumentException
	 *             if {@code list} is empty, has only 1 element or is null or if
	 *             quantile has no valid value
	 */
	public static Double singleQuantile(List<DoublePoint> list, double quantile)
			throws IllegalArgumentException {
		CalculationMethods.sort(list);
		if (list == null || list.isEmpty() || list.size() == 1) {
			logger.warn("no points available");
			throw new IllegalArgumentException(
					"list is null, empty or has only one value");
		}
		if (quantile <= 0.0 || quantile >= 1.0) {
			logger.warn("value for quantile not valid");
			throw new IllegalArgumentException(
					"value for qunatile is not valid (should be ]0.0; 1.0[)");
		}
		Double sol;
		if (quantile * list.size() == (int) (quantile * list.size())) {
			sol = 0.5 * (list.get((int) (quantile * list.size())).getValue() + list
					.get((int) (quantile * list.size() - 1)).getValue());
		} else {
			sol = list.get((int) (quantile * list.size())).getValue();
		}
		return sol;
	}

	/**
	 * Divides the data in equal sized parts. The returned values indicate after
	 * which values a new part starts.
	 * 
	 * @param list
	 *            all elements
	 * @param divisions
	 *            indicates in how many parts the data should be divided.
	 *            {@code divisions} should be smaller than the list size and
	 *            bigger 0.
	 * @return the values which divide the data in equal size parts are returned
	 * @throws IllegalArgumentException
	 *             if {@code list} is null, empty or has only 1 element or if
	 *             {@code divisions} is not valid
	 */
	public static List<Double> quantiles(List<DoublePoint> list, int divisions)
			throws IllegalArgumentException {
		if (list == null || list.isEmpty() || divisions >= list.size()
				|| divisions <= 0) {
			logger.warn("no points available");
			throw new IllegalArgumentException(
					"list is null, empty or has only one element or divisions is <= 0");
		}
		List<Double> sol = new ArrayList<Double>();
		for (int i = 1; i < divisions; i++) {
			double counter = (double) (i) / divisions;
			// no exception will occur - would have been caught earlier
			sol.add(singleQuantile(list, counter));
		}
		return sol;
	}

	/**
	 * Calculates the distance between the first quartile and the 3rd quartile
	 * 
	 * @param list
	 *            all elements
	 * @return range is returned
	 * @throws IllegalArgumentException
	 *             if no quartiles were found
	 */
	public static Double interquartileRange(List<DoublePoint> list)
			throws IllegalArgumentException {
		List<Double> quartiles = new ArrayList<Double>();
		try {
			quartiles = quantiles(list, 4);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw e;
		}
		return Math.abs(quartiles.get(0)) + Math.abs(quartiles.get(2));
	}
}
