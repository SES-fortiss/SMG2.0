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
import java.util.List;

import org.fortiss.smg.analyzer.api.DataSet;
import org.fortiss.smg.informationbroker.api.DoublePoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * provides methods to classify one or more data sets with a classification
 * function
 * 
 * @author Ann Katrin Gibtner (annkatrin.gibtner@tum.de)
 *
 */
public class Classifier {

	private final static Logger logger = LoggerFactory
			.getLogger(Classifier.class);

	/**
	 * Compares data sets to an interpolation function given by {@code interpol}
	 * 
	 * @param compareItems
	 *            all data sets that should be compared with the interpolated
	 *            function
	 * @param tolerance
	 *            the maximum deviation the points of a data set may be apart
	 *            from the points of the interpolated function. The deviation is
	 *            calculated on the average distance; use for example {@code 30}
	 *            for a tolerance of 30%; negative tolerance will return false
	 * @param interpol
	 *            defines which interpolation function should be used
	 * @return a list of all data sets that have a average distance <=
	 *         {@code tolerance}
	 * @throws IllegalArgumentException
	 *             if {@code compareItems} is null or empty
	 * @see #compareSingleValues(TrendAnalyzer, int)
	 */
	public static List<DataSet> compareAll(List<DataSet> compareItems,
			int tolerance, Interpolator interpol)
			throws IllegalArgumentException {
		if (compareItems == null || compareItems.isEmpty()) {
			logger.warn("compareAll: no data sets available");
			throw new IllegalArgumentException(
					"list of data sets is empty or null");
		}
		List<DataSet> similar = new ArrayList<DataSet>();
		for (DataSet actual : compareItems) {
			if (compareSingleValues(actual, tolerance, interpol)) {
				similar.add(actual);
			}
		}
		return similar;
	}

	/**
	 * Compares the values of one data set to an interpolated function.
	 * 
	 * @param dataSetToClassify
	 *            data set that should be compared to the interpolated function
	 * @param tolerance
	 *            the maximum deviation the points of the data set may be apart
	 *            from the points of the interpolated function. The deviation is
	 *            calculated on the average distance; use for example {@code 30}
	 *            for a tolerance of 30%; negative tolerance will return false
	 * @param interpol
	 *            defines which interpolation function should be used
	 * @return true if average distance is {@code <= tolerance}; else false
	 * @throws IllegalArgumentException
	 *             if {@code dataSetToClassify} is null or dosen't contain any
	 *             values, or {@code tolerance} is not valid (<0), or
	 *             {@code interpol} is not initialized correctly, or intervals
	 *             of {@code dataSetToClassify and interpol} don't overlap
	 */
	private static boolean compareSingleValues(DataSet dataSetToClassify,
			int tolerance, Interpolator interpol)
			throws IllegalArgumentException {
		if (dataSetToClassify == null) {
			logger.warn("compareSingleValues: dataSetToClassify is null");
			throw new IllegalArgumentException("data set is null");
		}
		List<DoublePoint> testList = dataSetToClassify.getDataList();
		if (testList == null || testList.isEmpty()) {
			logger.warn("compareSingleValues: dataSetToClassify dosen't include any elements");
			throw new IllegalArgumentException(
					"data set dosen't include data points");
		}
		if (tolerance < 0) {
			logger.warn("compareSingleValues: negative tolerance");
			throw new IllegalArgumentException(
					"negative tolerance found (not valid)");
		}
		if (interpol == null || interpol.getFunction() == null
				|| interpol.getFunctionStartTime() == null) {
			logger.warn("compareSingleValues: interpol was not initialized correctly");
			throw new IllegalArgumentException(
					"interpolator is not initialized correctly");
		}
		long difference = dataSetToClassify.getStartDate().getTimeInMillis()
				- interpol.getFunctionStartTime().getTimeInMillis();
		double sum = 0.0;
		// number of points in the data set, that have a corresponding value on
		// the function
		int avaiablePoints = 0;
		double[] knots = interpol.getFunction().getKnots();
		for (DoublePoint point : testList) {
			if (point.getTime() - difference < knots[0]
					|| point.getTime() - difference > knots[knots.length - 1]) {
			} else {
				double ref = interpol.getFunction().value(
						point.getTime() - difference);
				double check = point.getValue();
				sum += ((double) tolerance / 100)
						- (Math.abs(ref - check) / ref);
				avaiablePoints++;
			}
		}
		if (sum / avaiablePoints >= 0) {
			return true;
		}
		if (avaiablePoints == 0) {
			logger.info("compareSingleValues: intervals of dataSetToClassify and interpol missmatch");
			throw new IllegalArgumentException(
					"intervals of data set and interpolator missmatch - no overlapping points found");
		} else {
			return false;
		}
	}
}
