package org.fortiss.smg.analyzer.impl.pattern;

import java.util.ArrayList;
import java.util.List;

import org.fortiss.smg.analyzer.api.DataSet;
import org.fortiss.smg.informationbroker.api.DoublePoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * provides methods to find peak values
 * 
 * @author Ann Katrin Gibtner (annkatrin.gibtner@tum.de)
 *
 */
public class Peak {

	private final static Logger logger = LoggerFactory.getLogger(Peak.class);

	/**
	 * finds a list of {@code DoublePoints} that are considered to be peaks.
	 * Therefore these points will have to have a bigger aberration from the
	 * values of interpolation function than {@code minAberration}. In addition
	 * at least {@code minConcurrency} of the points in a {@code timeInterval}
	 * need to have that aberration.
	 * 
	 * @param timeInterval
	 *            use time in milliseconds; at least {@code minCongruency} of
	 *            the points in this interval need to have at least
	 *            {@code minAberration} form the value of the interpolated
	 *            function; use positive integer;
	 * @param minVariance
	 *            use for example {@code 30} for 30%; a value of {@code compare}
	 *            needs to be at least {@code minAberration} to be considered as
	 *            a peak; use positive integer;
	 * @param minCongruency
	 *            use for example {@code 80} for 80%; number of values
	 *            considered as peaks in {@code timeInterval} must be at least
	 *            {@code minCongruency} of all points available in that
	 *            interval; use number between 0 and 100;
	 * @param compare
	 *            DataSet that should be checked for peaks
	 * @param interpolator
	 *            defines which interpolation function should be used; the
	 *            function represents the classifier.
	 * @return a list containing lists (of DoublePoints) which represent a
	 *         single peak; null if parameter weren't valid or no data points
	 *         were found
	 * @throws IllegalArgumentException
	 *             if {@code compare} is null or dosen't provide data points, or
	 *             {@code timeInterval, minCongruency, minVariance} are not
	 *             valid, or {@code interpolator} is not initialized correctly
	 */
	public static List<List<DoublePoint>> findPeaks(int timeInterval,
			int minVariance, int minCongruency, DataSet compare,
			Interpolator interpolator) throws IllegalArgumentException {
		if (compare == null) {
			logger.warn("findPeaks: data set is null");
			throw new IllegalArgumentException("data set is null");
		}
		if (compare.getDataList() == null || compare.getDataList().isEmpty()) {
			logger.warn("findPeaks: no points avaiable - please fetch first");
			throw new IllegalArgumentException(
					"no data points found in data set - please fetch first");
		}
		if (timeInterval < 0 || minVariance < 0 || minCongruency < 0
				|| minCongruency > 100) {
			logger.warn("findPeaks: 'timeInterval', 'minVariance' or 'minCongruency' not valid");
			throw new IllegalArgumentException(
					"'timeInterval', 'minVariance' or 'minCongruency' not valid");
		}
		if (interpolator == null || interpolator.getFunction() == null
				|| interpolator.getFunctionStartTime() == null) {
			logger.warn("findPeaks: interpolator not initialized correctly");
			throw new IllegalArgumentException(
					"interpolator is not initialized correctly");
		}
		List<List<DoublePoint>> allPeaks = new ArrayList<List<DoublePoint>>();
		List<DoublePoint> peakCandidates = new ArrayList<DoublePoint>();
		long difference = compare.getStartDate()
				- interpolator.getFunctionStartTime().getTimeInMillis();
		double[] knots = interpolator.getFunction().getKnots();
		long filledTimeInterval = 0;
		int countPointsInInterval = 0;
		double currentCongruency = 100.0;
		double previousCongruency = 100.0;

		for (DoublePoint point : compare.getDataList()) {
			if (point.getTime() - difference < knots[0]
					|| point.getTime() - difference > knots[knots.length - 1]) {
			} else {
				double loessValue = interpolator.getFunction().value(
						point.getTime() - difference);
				double check = point.getValue(); // =^ p in BA
				if (Math.abs(loessValue - check) / loessValue >= (double) minVariance / 100.0) {
					peakCandidates.add(point);
					countPointsInInterval++;
					filledTimeInterval = Math.abs(peakCandidates.get(0)
							.getTime() - point.getTime());
					currentCongruency = (double) peakCandidates.size()
							/ countPointsInInterval * 100.0;
				} else if (peakCandidates.size() > 0) {
					countPointsInInterval++;
					currentCongruency = (double) peakCandidates.size()
							/ countPointsInInterval * 100.0;
					filledTimeInterval = Math.abs(peakCandidates.get(0)
							.getTime() - point.getTime());
				}
				if (currentCongruency < minCongruency
						&& filledTimeInterval >= timeInterval) {
					if (Math.abs(peakCandidates.get(0).getTime()
							- peakCandidates.get(peakCandidates.size() - 1)
									.getTime()) >= timeInterval
							&& previousCongruency >= minCongruency) {
						List<DoublePoint> peakPoints = new ArrayList<DoublePoint>();
						for (DoublePoint peakCandidate : peakCandidates) {
							peakPoints.add(peakCandidate);
						}
						allPeaks.add(peakPoints);
					}
					peakCandidates.clear();
					filledTimeInterval = 0;
					countPointsInInterval = 0;
					currentCongruency = 100;
				}
				previousCongruency = currentCongruency;
			}
		}
		if (peakCandidates == null || peakCandidates.isEmpty()) {
		} else if (Math.abs(peakCandidates.get(0).getTime()
				- peakCandidates.get(peakCandidates.size() - 1).getTime()) >= timeInterval
				&& currentCongruency >= minCongruency) {
			allPeaks.add(peakCandidates);
		}
		return allPeaks;
	}

}
