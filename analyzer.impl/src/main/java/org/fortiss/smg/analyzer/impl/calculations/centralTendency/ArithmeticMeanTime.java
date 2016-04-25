package org.fortiss.smg.analyzer.impl.calculations.centralTendency;

import java.util.List;

import org.fortiss.smg.informationbroker.api.DoublePoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ann Katrin Gibtner (annkatrin.gibtner@tum.de)
 *
 */
public class ArithmeticMeanTime extends CalculationMethods {

	private final static Logger logger = LoggerFactory
			.getLogger(ArithmeticMeanTime.class);

	/**
	 * Calculates the arithmetic mean multiplied with the timeSpan of all points
	 * in hours, can be used to calculate for example watt-hour
	 * 
	 * @param list
	 *            List including the values of which the mean per time will be
	 *            calculated
	 * @return arithmetic mean multiplied with the time in hours
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
		ArithmeticMean mean = new ArithmeticMean();
		Double arith;
		try {
			arith = mean.calculate(list);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw e;
		}
		long timeSpan = Math.abs(list.get(0).getTime()
				- list.get(list.size() - 1).getTime());
		double hour = (double) (timeSpan / 3600000.0);
		return arith * hour;
	}

}
