/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.analyzer.impl.pattern;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.analysis.interpolation.LoessInterpolator;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.fortiss.smg.analyzer.api.DataSet;
import org.fortiss.smg.informationbroker.api.DoublePoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * provides methods to interpolate the data events of data sets and regression
 * functions to merge more then one data set
 * 
 * @author Ann Katrin Gibtner (annkatrin.gibtner@tum.de)
 *
 */
public class Interpolator {

	private final static Logger logger = LoggerFactory
			.getLogger(Interpolator.class);

	/**
	 * the absolute start time of a function
	 */
	private Calendar functionStartTime;
	/**
	 * the interpolated function
	 */
	private PolynomialSplineFunction function;

	public Interpolator() {
		this.functionStartTime = Calendar.getInstance();
		this.function = null;
	}

	public Interpolator(Calendar functionStartTime,
			PolynomialSplineFunction function) throws NullArgumentException{
		if (functionStartTime == null) {
			throw new NullArgumentException();
		}
		this.functionStartTime = functionStartTime;
		this.function = function;
	}

	public Calendar getFunctionStartTime() {
		return functionStartTime;
	}

	public PolynomialSplineFunction getFunction() {
		return function;
	}

	/**
	 * calculates the LOESS interpolation with default bandwidth, robustnessIter
	 * and accuracy; function is set to null if no elements are found;
	 * 
	 * @param allDataSets
	 *            includes all data sets that should provide points for
	 *            interpolating
	 * @see <a
	 *      href="http://commons.apache.org/proper/commons-math/apidocs/org/apache/commons/math3/analysis/interpolation/LoessInterpolator.html">http://commons.apache.org/proper/commons-math/apidocs/org/apache/commons /math3/analysis/interpolation/LoessInterpolator.html</a>
	 * 
	 */
	public void loessInterpolation(List<DataSet> allDataSets)
			throws IllegalArgumentException {
		HashMap<String, double[]> allPoints = mergeAllPoints(allDataSets);
		LoessInterpolator interpolator = new LoessInterpolator();
		this.function = interpolator.interpolate(allPoints.get("x"),
				allPoints.get("y"));
	}

	/**
	 * calculates the LOESS interpolation with default accuracy; function is set
	 * to null if no elements are found;
	 * 
	 * @param allDataSets
	 *            includes all data sets that should provide points for
	 *            interpolating
	 * @param bandwidth
	 *            when computing the loess fit at a particular point, this
	 *            fraction of source points closest to the current point is
	 *            taken into account
	 * @param robustnessIter
	 *            The number of robustness iterations parameter: this many
	 *            robustness iterations are done.
	 * @see <a
	 *      href="http://commons.apache.org/proper/commons-math/apidocs/org/apache/commons/math3/analysis/interpolation/LoessInterpolator.html">http://commons.apache.org/proper/commons-math/apidocs/org/apache/commons /math3/analysis/interpolation/LoessInterpolator.html</a>
	 * 
	 */
	public void loessInterpolation(List<DataSet> allDataSets, double bandwidth,
			int robustnessIter) {
		HashMap<String, double[]> allPoints = mergeAllPoints(allDataSets);
		LoessInterpolator interpolator = new LoessInterpolator(bandwidth,
				robustnessIter);
		this.function = interpolator.interpolate(allPoints.get("x"),
				allPoints.get("y"));
	}

	/**
	 * calculates the LOESS interpolation with default accuracy; function is set
	 * to null if no elements are found;
	 * 
	 * @param allDataSets
	 *            includes all data sets that should provide points for
	 *            interpolating
	 * @param bandwidth
	 *            when computing the loess fit at a particular point, this
	 *            fraction of source points closest to the current point is
	 *            taken into account
	 * @param robustnessIter
	 *            The number of robustness iterations parameter: this many
	 *            robustness iterations are done.
	 * @param accuracy
	 *            If the median residual at a certain robustness iteration is
	 *            less than this amount, no more iterations are done.
	 * @see <a
	 *      href="http://commons.apache.org/proper/commons-math/apidocs/org/apache/commons/math3/analysis/interpolation/LoessInterpolator.html">http://commons.apache.org/proper/commons-math/apidocs/org/apache/commons /math3/analysis/interpolation/LoessInterpolator.html</a>
	 * 
	 */
	public void loessInterpolation(List<DataSet> allDataSets, double bandwidth,
			int robustnessIter, double accuracy) {
		HashMap<String, double[]> allPoints = mergeAllPoints(allDataSets);
		LoessInterpolator interpolator = new LoessInterpolator(bandwidth,
				robustnessIter, accuracy);
		this.function = interpolator.interpolate(allPoints.get("x"),
				allPoints.get("y"));
	}

	/**
	 * Interpolates the values of the given data set using a spline
	 * interpolation
	 * 
	 * @param dataSet
	 *            provides a list of {@code value} for the y axis and
	 *            {@code timestamp} for the x axis
	 * @return null if less than 3 x and y values are provided; else an
	 *         interpolated function
	 * @throws IllegalArgumentException
	 *             if {@code dataSet} dosen't provide more than 3 values
	 */
	public static PolynomialSplineFunction interpolateSpline(DataSet dataSet) {
		HashMap<String, double[]> xAndYValues = extractPoints(dataSet);
		if (xAndYValues.get("x").length < 3) {
			logger.warn("interpolateSpline: not enough points available (more than 3 needed)");
			throw new IllegalArgumentException("not enough points available");
		}
		SplineInterpolator interpol = new SplineInterpolator();
		return interpol.interpolate(xAndYValues.get("x"), xAndYValues.get("y"));
	}

	/**
	 * Extracts the points that are available in the database for given
	 * {@code dataSet}
	 * 
	 * @param dataSet
	 *            providing data points
	 * @return {@code Value} elements are added to {@code "x"} and
	 *         {@code timestamp} elements to {@code "y"}
	 * @throws IllegalArgumentException
	 *             if {@code dataSet} is null or dosen't contain any data points
	 */
	private static HashMap<String, double[]> extractPoints(DataSet dataSet)
			throws IllegalArgumentException {
		if (dataSet == null) {
			logger.warn("extractPoints: data set == null");
			throw new IllegalArgumentException("dataSet is null");
		}
		List<DoublePoint> fetch = new ArrayList<DoublePoint>();
		fetch = dataSet.getDataList();
		HashMap<String, double[]> xAndYValues = new HashMap<String, double[]>();
		if (fetch == null || fetch.isEmpty()) {
			logger.warn("extractPoints: no points found in data set - please fetch first");
			throw new IllegalArgumentException(
					"no data points are stored in data set - fetch data first");
		}
		double x[] = new double[fetch.size()];
		double y[] = new double[fetch.size()];
		int counter = 0;
		for (Iterator<DoublePoint> iterator = fetch.iterator(); iterator
				.hasNext();) {
			DoublePoint myPoint = (DoublePoint) iterator.next();
			x[counter] = myPoint.getTime();
			y[counter] = myPoint.getValue();
			counter++;
		}
		xAndYValues.put("x", x);
		xAndYValues.put("y", y);
		return xAndYValues;
	}

	/**
	 * merges all available points into 2 double Arrays, {@code timestamp}
	 * values are normalized so they fit in the time range of the first data
	 * set; the values are sorted by {@code timestamp} and points with the same
	 * {@code timestamp} are merged into one point
	 * 
	 * @param allDataSets
	 *            provide the data points
	 * @return null if no elements are found, else {@code Value} elements are
	 *         added to {@code "x"} and {@code timestamp} elements to
	 *         {@code "y"}
	 * @throws IllegalArgumentException
	 *             if {@code allDataSets} is null or empty
	 * @see #sortAndRemove(List)
	 * @see #concatenate(List)
	 */
	private HashMap<String, double[]> mergeAllPoints(List<DataSet> allDataSets)
			throws IllegalArgumentException {
		if (allDataSets == null || allDataSets.isEmpty()) {
			logger.warn("mergeAllPoints: no points available");
			throw new IllegalArgumentException("list is empty or null");
		}
		this.functionStartTime.setTimeInMillis(allDataSets.get(0)
				.getStartDate().getTimeInMillis());
		List<double[]> sortedElements = concatenate(allDataSets);
		sortAndRemove(sortedElements);
		// restructures the list with tuples (x, y) back into 2 arrays
		double[] sortedX = new double[sortedElements.size()];
		double[] sortedY = new double[sortedElements.size()];
		for (int i = 0; i < sortedElements.size(); i++) {
			sortedX[i] = sortedElements.get(i)[0];
			sortedY[i] = sortedElements.get(i)[1];
		}
		// builds a map for returning the values
		HashMap<String, double[]> totalMap = new HashMap<String, double[]>();
		totalMap.put("x", sortedX);
		totalMap.put("y", sortedY);
		return totalMap;
	}

	/**
	 * adds the values of {@code timestamp} and {@code value} of all data points
	 * to a list of tupel ({@code timestamp}, {@code value})
	 * 
	 * @param allDataSets
	 *            data points are extracted from these data sets
	 * @return a list of tupel ({@code timestamp}, {@code value})
	 * @see #mergeAllPoints(List)
	 */
	private static List<double[]> concatenate(List<DataSet> allDataSets) {
		double[] mergedX = new double[0];
		double[] mergedY = new double[0];
		long startTime = allDataSets.get(0).getStartDate().getTimeInMillis();
		for (Iterator<DataSet> iterator = allDataSets.iterator(); iterator
				.hasNext();) {
			DataSet actualDataSet = (DataSet) iterator.next();
			HashMap<String, double[]> actMap = extractPoints(actualDataSet);
			if (actMap == null || actMap.isEmpty()) {
				iterator.remove();
			} else {
				long actStartDate = actualDataSet.getStartDate()
						.getTimeInMillis();
				long difference = actStartDate - startTime;
				double[] actX = actMap.get("x");
				for (int i = 0; i < actX.length; i++) {
					actX[i] = actX[i] - difference;
				}
				mergedX = ArrayUtils.addAll(mergedX, actX);
				mergedY = ArrayUtils.addAll(mergedY, actMap.get("y"));
			}
		}
		// restructures from 2 separate lists with x and y values to a List with
		// a tuple (x, y)
		List<double[]> sortedElements = new ArrayList<double[]>();
		for (int i = 0; i < mergedY.length; i++) {
			double[] addingPoint = new double[2];
			addingPoint[0] = mergedX[i];
			addingPoint[1] = mergedY[i];
			sortedElements.add(addingPoint);
		}
		return sortedElements;
	}

	/**
	 * Sorts a list in ascending order referring to the first element (in this
	 * case {@code timestamp}) of each double[] and merges elements that have
	 * {@code elementi[0] == elementi+1[0]}; the values will be merged by using
	 * the arithmetic mean. NOTE: just use this for double[] with the size of 2!
	 * 
	 * @param sortedElements
	 *            provides elements that should be sorted
	 * 
	 * @see #mergeAllPoints(List)
	 */
	private static void sortAndRemove(List<double[]> sortedElements) {
		Collections.sort(sortedElements, new Comparator<double[]>() {
			public int compare(double[] arg0, double[] arg1) {
				return Double.compare(arg0[0], arg1[0]);
			}
		});

		double repeatedEntriesYValue = 0.0;
		int numberOfRepetition = 1;
		for (Iterator<double[]> iterator = sortedElements.iterator(); iterator
				.hasNext();) {
			double[] actual = (double[]) iterator.next();
			int index = sortedElements.indexOf(actual);
			if (index + 1 < sortedElements.size()) {
				double[] next = sortedElements.get(index + 1);
				if (actual[0] == next[0]) {
					repeatedEntriesYValue += actual[1];
					numberOfRepetition++;
					// removes 'actual' from list
					iterator.remove();
				} else {
					actual[1] = (repeatedEntriesYValue + actual[1])
							/ numberOfRepetition;
					repeatedEntriesYValue = 0.0;
					numberOfRepetition = 1;
				}
			}
		}
	}

}
