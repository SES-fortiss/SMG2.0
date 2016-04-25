package org.fortiss.smg.analyzer.impl.correlation;

import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.fortiss.smg.analyzer.api.DataSet;
import org.fortiss.smg.analyzer.impl.pattern.Interpolator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Includes methods for calculation correlation coefficients
 * 
 * @author Ann Katrin Gibtner (annkatrin.gibtner@tum.de)
 *
 */
public class CorrelationCalculation {

	private final static Logger logger = LoggerFactory
			.getLogger(CorrelationCalculation.class);

	/**
	 * calculates the pearson correlation coefficient for two data sets. The
	 * data sets each represent an attribute. Both sets are equivalent,
	 * therefore it dosen't matter which set is chosen for which attribute.
	 * 
	 * @param xDataSet
	 *            represents one attribute. Should already include data
	 * @param yDataSet
	 *            represents the other attribute. Should already include data
	 * @param numberPoints
	 *            defines how many points should be compared for the correlation
	 *            analysis
	 * @return the correlation coefficient
	 * @throws IllegalArgumentException
	 *             if at least one data set is null, or the data sets don't have
	 *             the same interval length (from start to stop)
	 * @throws NoDataException
	 *             if any data set dosen't contain any data points
	 */
	public static double pearsonCorrelation(DataSet xDataSet, DataSet yDataSet,
			int numberPoints) throws IllegalArgumentException, NoDataException {
		if (xDataSet == null || yDataSet == null) {
			logger.warn("pearsonCorrelation: at least one data set is null");
			throw new IllegalArgumentException("at least one data set is null");
		}
		if (xDataSet.getDataList() == null || xDataSet.getDataList().isEmpty()
				|| yDataSet.getDataList() == null
				|| yDataSet.getDataList().isEmpty()) {
			throw new NoDataException();
		}
		if (equalIntervallLength(xDataSet, yDataSet)) {
			double[] xArray = new double[numberPoints];
			double[] yArray = new double[numberPoints];
			PolynomialSplineFunction xFunction = Interpolator
					.interpolateSpline(xDataSet);
			PolynomialSplineFunction yFunction = Interpolator
					.interpolateSpline(yDataSet);
			double firstTimeStamp;
			double lastTimeStamp;
			double dividedInterval;

			if (sameIntervallStartPoint(xDataSet, yDataSet)) {
				// choose shortest spline interval, in which both splines are
				// defined
				if (xFunction.getKnots()[0] < yFunction.getKnots()[0]) {
					firstTimeStamp = yFunction.getKnots()[0];
				} else {
					firstTimeStamp = xFunction.getKnots()[0];
				}
				if (xFunction.getKnots()[xFunction.getN()] < yFunction
						.getKnots()[yFunction.getN()]) {
					lastTimeStamp = xFunction.getKnots()[xFunction.getN()];
				} else {
					lastTimeStamp = yFunction.getKnots()[yFunction.getN()];
				}

				// calculates the length of a subinterval (defined by
				// numberPoints)
				dividedInterval = (lastTimeStamp - firstTimeStamp)
						/ numberPoints;

				// fills arrays with #numberPoints data points
				for (int i = 0; i < numberPoints; i++) {
					xArray[i] = xFunction.value(firstTimeStamp + i
							* dividedInterval);
					yArray[i] = yFunction.value(firstTimeStamp + i
							* dividedInterval);
				}
			} else {
				// choose shortest spline interval, in which both splines are
				// defined, if they would have the same start point
				double intervallDifference = xDataSet.getStartDate()
						
						- yDataSet.getStartDate();
				if (xFunction.getKnots()[0] < yFunction.getKnots()[0]
						+ intervallDifference) {
					firstTimeStamp = yFunction.getKnots()[0]
							+ intervallDifference;
				} else {
					firstTimeStamp = xFunction.getKnots()[0];
				}
				if (xFunction.getKnots()[xFunction.getN()] < yFunction
						.getKnots()[yFunction.getN()] + intervallDifference) {
					lastTimeStamp = xFunction.getKnots()[xFunction.getN()];
				} else {
					lastTimeStamp = yFunction.getKnots()[yFunction.getN()]
							+ intervallDifference;
				}

				// calculates the length of a subinterval (defined by
				// numberPoints)
				dividedInterval = (lastTimeStamp - firstTimeStamp)
						/ numberPoints;

				// fills arrays with #numberPoints data points
				for (int i = 0; i < numberPoints; i++) {
					xArray[i] = xFunction.value(firstTimeStamp + i
							* dividedInterval);
					yArray[i] = yFunction.value(firstTimeStamp + i
							* dividedInterval - intervallDifference);
				}
			}
			PearsonsCorrelation correlation = new PearsonsCorrelation();
			return correlation.correlation(xArray, yArray);

		} else {
			logger.warn("pearsonCorrelation: data sets don't have matching interval");
			throw new IllegalArgumentException(
					"data sets don't have matching interval");
		}
	}

	/**
	 * checks whether both data sets have the same interval length form their
	 * start time till their stop time
	 * 
	 * @param set1
	 *            first set of data
	 * @param set2
	 *            second set of data
	 * @return true if the interval length is the same; else false
	 */
	private static boolean equalIntervallLength(DataSet set1, DataSet set2) {
		if ((set1.getStopDate() - set1.getStartDate()) == (set2.getStopDate() - set2
				.getStartDate())) {
			return true;
		}
		return false;
	}

	/**
	 * checks whether both data sets start at the same time
	 * 
	 * @param set1
	 *            first set of data
	 * @param set2
	 *            second set of data
	 * @return true if the star time is the same; else false;
	 */
	private static boolean sameIntervallStartPoint(DataSet set1, DataSet set2) {
		return set1.getStartDate()==(set2.getStartDate());
	}
}
