/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.analyzer.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.fortiss.smg.analyzer.api.DataSet;
import org.fortiss.smg.analyzer.impl.calculations.centralTendency.CalculationMethods;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.informationbroker.api.DoublePoint;
import org.fortiss.smg.informationbroker.api.InformationBrokerInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * provides fundamental functions to compare data sets
 * 
 * @author Ann Katrin Gibtner (annkatrin.gibtner@tum.de)
 *
 */
public class Comparison {

	private final static Logger logger = LoggerFactory
			.getLogger(Comparison.class);
	private final static int fraction = 0;
	private final static int absolute = 1;
	private final static int percent = 2;

	/**
	 * Calculates the percentage value of all factions in comparison to a total
	 * value. Used to create pie charts.
	 * 
	 * @param reference
	 *            values of this device will represent the whole pie
	 * @param comparer
	 *            each device of the list represents one fraction of the pie
	 *            NOTE: both reference and comparer should include data over the
	 *            same time interval! Otherwise the function will not work as
	 *            supposed!
	 * @param aggregationMethode
	 *            indicates which calculation should be used to aggregate the
	 *            values of a data set (e.g. arithmetic mean, sum, ...)
	 * @return a list with the share of each fraction in total will be returned
	 *         in percent; NOTE: if one fraction dosen't include any values
	 *         {@code null} will be the value of this fraction.
	 */
	public static HashMap<DeviceId, Double> deviceCompareFraction(
			DataSet reference, List<DataSet> comparer,
			CalculationMethods aggregationMethode) {
		return deviceCompare(reference, comparer, aggregationMethode, fraction);
	}

	/**
	 * Calculates the absolute difference between a set of devices and one
	 * reference device
	 * 
	 * @param reference
	 *            values of this device will represent the whole pie
	 * @param comparer
	 *            each device of the list represents one fraction of the pie
	 *            NOTE: both reference and comparer should include data over the
	 *            same time interval! Otherwise the function will not work as
	 *            supposed!
	 * @param aggregationMethode
	 *            indicates which calculation should be used to aggregate the
	 *            values of a data set (e.g. arithmetic mean, sum, ...)
	 * @return a map with the share of each fraction in total will be returned
	 *         in percent; NOTE: if one fraction dosen't include any values
	 *         {@code null} will be the value of this fraction.
	 */
	public static HashMap<DeviceId, Double> deviceCompareAbsolute(
			DataSet reference, List<DataSet> comparer,
			CalculationMethods aggregationMethode) {
		return deviceCompare(reference, comparer, aggregationMethode, absolute);
	}

	/**
	 * Calculates the percentage difference between a set of devices and one
	 * reference device
	 * 
	 * @param reference
	 *            values of this device will represent the whole pie
	 * @param comparer
	 *            each device of the list represents one fraction of the pie
	 *            NOTE: both reference and comparer should include data over the
	 *            same time interval! Otherwise the function will not work as
	 *            supposed!
	 * @param aggregationMethode
	 *            indicates which calculation should be used to aggregate the
	 *            values of a data set (e.g. arithmetic mean, sum, ...)
	 * @return a map with the share of each fraction in total will be returned
	 *         in percent; NOTE: if one fraction dosen't include any values
	 *         {@code null} will be the value of this fraction.
	 */
	public static HashMap<DeviceId, Double> deviceComparePercent(
			DataSet reference, List<DataSet> comparer,
			CalculationMethods aggregationMethode) {
		return deviceCompare(reference, comparer, aggregationMethode, percent);
	}

	/**
	 * Calculates the difference between a set of devices and one reference
	 * device
	 * 
	 * @param total
	 *            values of this device will represent the whole pie
	 * @param fractions
	 *            each device of the list represents one fraction of the pie
	 *            NOTE: both reference and fractions should include data over
	 *            the same time interval! Otherwise the function will not work
	 *            as supposed!
	 * @param aggregationMethode
	 *            indicates which calculation should be used to aggregate the
	 *            values of a data set (e.g. arithmetic mean, sum, ...)
	 * @param modus
	 *            decides if the return value should be in form of fractions,
	 *            percent or absolute
	 * @return a map with the share of each fraction in total will be returned
	 *         in percent; NOTE: if one fraction dosen't include any values
	 *         {@code null} will be the value of this fraction.
	 * @throws IllegalArgumentException
	 *             if {@code total, fractions} == null or {@code fraction} is
	 *             empty or {@code total} didn't provide a list of data points
	 *             or an empty list
	 */
	private static HashMap<DeviceId, Double> deviceCompare(DataSet total,
			List<DataSet> fractions, CalculationMethods aggregationMethode,
			int modus) throws IllegalArgumentException {
		if (total == null) {
			logger.warn("deviceCompare: 'total' == null");
			throw new IllegalArgumentException("data set is null");
		}
		if (total.getDataList() == null || total.getDataList().isEmpty()) {
			logger.warn("deviceCompare: 'total' didn't provide any values");
			throw new IllegalArgumentException(
					"data set dosen't include any values");
		}
		if (fractions == null || fractions.isEmpty()) {
			logger.warn("deviceCompare: no fractions provided(==null)");
			throw new IllegalArgumentException(
					"no fractions provided (either null or empty list)");
		}
		Double totalSum = aggregationMethode.calculate(total.getDataList());

		HashMap<DeviceId, Double> fractionSum = new LinkedHashMap<DeviceId, Double>();
		for (Iterator<DataSet> iterator = fractions.iterator(); iterator
				.hasNext();) {
			DataSet singleData = (DataSet) iterator.next();

			if (singleData == null || singleData.getDeviceId() == null) {
				logger.info("deviceCompare: data set is null - ignoring this set");
			} else {
				if (singleData.getDataList() == null
						|| singleData.getDataList().isEmpty()) {
					fractionSum.put(singleData.getDeviceId(), null);
				} else if (modus == 0) {
					fractionSum.put(singleData.getDeviceId(),
							(aggregationMethode.calculate(singleData
									.getDataList()) / totalSum) * 100);
				} else if (modus == 1) {
					fractionSum.put(
							singleData.getDeviceId(),
							aggregationMethode.calculate(singleData
									.getDataList()) - totalSum);

				} else if (modus == 2) {
					fractionSum.put(singleData.getDeviceId(),
							(aggregationMethode.calculate(singleData
									.getDataList()) / totalSum) * 100 - 100);
				}
			}
		}
		return fractionSum;
	}

	/**
	 * Compares values of data sets to a reference set which the same time span
	 * and (maybe) different time points. E.g. the total value of consumed
	 * electricity of all months in the year x compared to the months of year
	 * x-1. The per cent rise/fall of the values is calculated in the function.
	 * 
	 * @param reference
	 *            data set used as reference
	 * @param comparer
	 *            data sets, which will be compare to the reference; NOTE:
	 *            reference and all comparers should cover the same time span
	 *            from (maybe) differing time points
	 * @param field
	 *            a fixed Calendar field (e.g. Calendar.MONTH); for creating a
	 *            bar chart see:
	 *            {@link #barChart(DataSet, int, int, CalculationMethods, Logger, InformationBrokerInterface)}
	 *            ; if no bar charts are required choose {@code field} =
	 *            {@code interval} and {@code delta} = {@code intervAmount}.
	 * @param delta
	 *            for creating a bar chart; if no bar charts are required choose
	 *            {@code field} = {@code interval} and {@code delta} =
	 *            {@code intervAmount}.
	 * @param cal
	 *            indicates which calculation should be used to aggregate the
	 *            values of a data set (e.g. arithmetic mean, sum, ...)
	 * @return results of the calculation, a list of data points for each
	 *         comparer; if one value of the reference or the comparer set isn't
	 *         provided this value is set to null;
	 * @see #barChart(DataSet, int, int, CalculationMethods, Logger,
	 *      InformationBrokerInterface)
	 */
	public static List<DataSet> timeComparePercent(DataSet reference,
			List<DataSet> comparer, int field, int delta, CalculationMethods cal) {
		return timeCompare(reference, comparer, field, delta, cal, false);
	}

	/**
	 * Compares values of data sets to a reference set which the same time span
	 * and (maybe) different time points. E.g. the total value of consumed
	 * electricity of all months in the year x compared to the months of year
	 * x-1. The values are subtracted from each other in this function.
	 * 
	 * @param reference
	 *            data set used as reference
	 * @param comparer
	 *            data sets, which will be compare to the reference; NOTE:
	 *            reference and all comparers should cover the same time span
	 *            from (maybe) differing time points
	 * @param field
	 *            a fixed Calendar field (e.g. Calendar.MONTH); for creating a
	 *            bar chart see:
	 *            {@link #barChart(DataSet, int, int, CalculationMethods, Logger, InformationBrokerInterface)}
	 *            ; if no bar charts are required choose {@code field} =
	 *            {@code interval} and {@code delta} = {@code intervAmount}.
	 * @param delta
	 *            for creating a bar chart; if no bar charts are required choose
	 *            {@code field} = {@code interval} and {@code delta} =
	 *            {@code intervAmount}.
	 * @param cal
	 *            indicates which calculation should be used to aggregate the
	 *            values of a data set (e.g. arithmetic mean, sum, ...)
	 * @return results of the calculation, a list of data points for each
	 *         comparer; if one value of the reference or the comparer set isn't
	 *         provided this value is set to null;
	 * @see #barChart(DataSet, int, int, CalculationMethods, Logger,
	 *      InformationBrokerInterface)
	 */
	public static List<DataSet> timeCompareAbsolute(DataSet reference,
			List<DataSet> comparer, int field, int delta, CalculationMethods cal) {
		return timeCompare(reference, comparer, field, delta, cal, true);
	}

	/**
	 * Compares values of data sets to a reference set which the same time span
	 * and (maybe) different time points. E.g. the total value of consumed
	 * electricity of all months in the year x compared to the months of year
	 * x-1.
	 * 
	 * @param reference
	 *            data set used as reference
	 * @param comparers
	 *            data sets, which will be compare to the reference; NOTE:
	 *            reference and all comparers should cover the same time span
	 *            from (maybe) differing time points
	 * @param field
	 *            a fixed Calendar field (e.g. Calendar.MONTH); for creating a
	 *            bar chart see:
	 *            {@link #barChart(DataSet, int, int, CalculationMethods, Logger, InformationBrokerInterface)}
	 *            ; if no bar charts are required choose {@code field} =
	 *            {@code interval} and {@code delta} = {@code intervAmount}.
	 * @param delta
	 *            for creating a bar chart; if no bar charts are required choose
	 *            {@code field} = {@code interval} and {@code delta} =
	 *            {@code intervAmount}.
	 * @param cal
	 *            indicates which calculation should be used to aggregate the
	 *            values of a data set (e.g. arithmetic mean, sum, ...)
	 * @param modusSubtract
	 *            <ul>
	 *            <li>true: values are subtracted from each other;
	 *            <li>false: the per cent rise/fall of the values is calculated
	 *            </ul>
	 * @return results of the calculation, a list of data points for each
	 *         comparer; if one value of the reference or the comparer set isn't
	 *         provided the value of the data point is set to null;
	 * @throws IllegalArgumentException
	 *             if {@code reference, comparers, cal } == null or
	 *             {@code comparers} is empty
	 * @see #barChart(DataSet, int, int, CalculationMethods, Logger,
	 *      InformationBrokerInterface)
	 */
	private static List<DataSet> timeCompare(DataSet reference,
			List<DataSet> comparers, int field, int delta,
			CalculationMethods cal, boolean modusSubtract)
			throws IllegalArgumentException {

		if (reference == null || comparers == null || comparers.isEmpty()
				|| cal == null) {
			logger.warn("timeCompare: some input value == null");
			throw new IllegalArgumentException(
					"reference, comparers and/or cal are null or empty");
		}
		List<DoublePoint> referencePoints = barChart(reference, field, delta,
				cal);

		List<DataSet> barChartSetList = new ArrayList<DataSet>();

		for (DataSet currentSet : comparers) {
			if (currentSet == null) {
				barChartSetList.add(null);
			} else {
				DataSet barChartSet = null;
				try {
					barChartSet = new DataSet(currentSet.getStartDate(),
							currentSet.getStopDate(), currentSet.getDeviceId(),
							barChart(currentSet, field, delta, cal));
				} catch (IllegalArgumentException e) {
					barChartSet = new DataSet(currentSet.getStartDate(),
							currentSet.getStopDate(), currentSet.getDeviceId(),
							null);
				}
				barChartSetList.add(barChartSet);
			}
		}

		List<DataSet> solutionDataSetList = new ArrayList<DataSet>();
		for (DataSet compareSet : barChartSetList) {
			if (compareSet == null || compareSet.getDataList() == null
					|| compareSet.getDataList().isEmpty()) {
				solutionDataSetList.add(null);
			} else {
				DataSet currentSolDataSet = new DataSet(
						compareSet.getStartDate(), compareSet.getStopDate(),
						compareSet.getDeviceId());
				List<DoublePoint> currentSolPointList = new ArrayList<DoublePoint>();
				for (Iterator<DoublePoint> iterReference = referencePoints
						.iterator(), iterCompare = compareSet.getDataList()
						.iterator(); iterReference.hasNext()
						&& iterCompare.hasNext();) {
					DoublePoint referencePoint = (DoublePoint) iterReference
							.next();
					DoublePoint comparePoint = (DoublePoint) iterCompare.next();

					if (comparePoint.getValue() == null
							|| referencePoint.getValue() == null) {
						currentSolPointList.add(new DoublePoint(null,
								comparePoint.getMaxAbsError(), comparePoint
										.getTime()));
					} else if (modusSubtract == true) {
						currentSolPointList.add(new DoublePoint(comparePoint
								.getValue() - referencePoint.getValue(),
								comparePoint.getMaxAbsError(), comparePoint
										.getTime()));
					} else {
						currentSolPointList
								.add(new DoublePoint(
										(comparePoint.getValue()
												/ referencePoint.getValue()
												* 100 - 100), comparePoint
												.getMaxAbsError(), comparePoint
												.getTime()));
					}
				}
				currentSolDataSet.setDataList(currentSolPointList);
				solutionDataSetList.add(currentSolDataSet);
			}
		}
		return solutionDataSetList;
	}

	/**
	 * creates a pillar list, used for creating bar charts, with the width of a
	 * fixed Calendar field multiplied with a factor. The method how the single
	 * pillars are calculated is defined by the parameter {@code cal}.
	 * 
	 * <ul>
	 * <li>Each element in the list represents one pillar in a diagram.
	 * <li>The value of an element represents the height of a pillar. This is
	 * the result of the passed form of calculation of all events in a
	 * determined time space.
	 * <li>The width of a pillar is given by {@code field}*{@code delta}. This
	 * means that also a variable width of for example 3 days is possible. For a
	 * fixed width of x seconds chose {@code delta} = x and {@code field} =
	 * Calendar.SECOND.
	 * <li>In order to make the diagram more readable the start time of a pillar
	 * is modified as follows: all units smaller than {@code field} are set to
	 * their minimum. E.g. if {@code field} is Calendar.DAY_OF_MONTH a pillar
	 * starts for example at 01.04.2013 00:00:00 and the next pillar at
	 * 02.04.2013 00:00:00 and NOT 01.04.2013 14:23:45 and 02.04.2013 14:23:45
	 * </ul>
	 * 
	 * @param dataSet
	 *            includes all data, which should be used to build the bars,
	 *            NOTE: data needs to be fetched before calling this method!
	 * @param field
	 *            fixed Calendar field (e.g. Calendar.DAY_OF_MONTH)
	 * @param delta
	 *            used to multiply the fixed Calendar field with an int value
	 * @param cal
	 *            indicates which calculation should be used (e.g. arithmetic
	 *            mean, sum, ...)
	 * @return a list of aggregated data points; if for one time interval no
	 *         data is provided null will be inserted as value
	 * @throws IllegalArgumentException
	 *             if {@code dataSet, cal or dataSet.getDataList()} == null or
	 *             empty
	 */
	public static List<DoublePoint> barChart(DataSet dataSet, int field,
			int delta, CalculationMethods cal) throws IllegalArgumentException {
		if (dataSet == null) {
			logger.warn("barChart: dataSet == null");
			throw new IllegalArgumentException(
					"'dataSet' is null (no valid input)");
		}
		if (dataSet.getDataList() == null) {
			logger.warn("barChart: No elements found in dataSet");
			throw new IllegalArgumentException(
					"'dataSet.getDataList()' is null (no valid input)");
		}
		if (dataSet.getDataList().isEmpty()) {
			logger.warn("barChart: No elements found in dataSet");
			throw new IllegalArgumentException(
					"'dataSet.getDataList()' is empty (no valid input)");
		}
		if (cal == null) {
			logger.warn("barChart: cal == null");
			throw new IllegalArgumentException("'cal' is null (no valid input)");
		}
		List<DoublePoint> endList = new ArrayList<DoublePoint>();
		List<DoublePoint> pillarList = new ArrayList<DoublePoint>();
		DoublePoint pillarPoint = new DoublePoint(null, 0.0, 0);
		Calendar deltaCal = Calendar.getInstance();
		deltaCal.setTimeInMillis(dataSet.getStartDate().getTimeInMillis());
		Comparison.initDate(deltaCal, field);

		for (Iterator<DoublePoint> iterator = dataSet.getDataList().iterator(); iterator
				.hasNext();) {
			DoublePoint DoublePoint = (DoublePoint) iterator.next();
			if (DoublePoint.getCalendarTime().compareTo(deltaCal) >= 0) {
				try {
					pillarPoint.setValue(cal.calculate(pillarList));
				} catch (IllegalArgumentException e) {
					pillarPoint.setValue(null);
				}
				pillarList.clear();
			}
			pillarPoint = nextPillar(field, delta, endList, pillarPoint,
					deltaCal, DoublePoint);
			pillarPoint.setMaxAbsError(DoublePoint.getMaxAbsError());
			pillarList.add(DoublePoint);
		}
		if (pillarList.isEmpty() == false) {
			try {
				pillarPoint.setValue(cal.calculate(pillarList));
			} catch (IllegalArgumentException e) {
				pillarPoint.setValue(null);
			}
		}
		return endList;
	}

	/**
	 * Creates the next plain pillar element for the 'barChart' functions
	 * 
	 * @param field
	 *            fixed Calendar field (e.g. Calendar.DAY_OF_MONTH)
	 * @param delta
	 *            used to multiply the fixed Calendar field with an int value
	 * @param pillarList
	 *            new element for this list will be created
	 * @param pillarPoint
	 *            element that will be initialized
	 * @param deltaCal
	 *            if time of {@code deltaCal} is passed {@code pillarPoint} will
	 *            be created
	 * @param DoublePoint
	 *            actual element
	 * @return pillarPoint new initialized element
	 * @see #barChart(DataSet, int, int, CalculationMethods, Logger,
	 *      InformationBrokerInterface)
	 */
	private static DoublePoint nextPillar(int field, int delta,
			List<DoublePoint> pillarList, DoublePoint pillarPoint,
			Calendar deltaCal, DoublePoint DoublePoint) {
		while (DoublePoint.getCalendarTime().compareTo(deltaCal) >= 0) {
			pillarPoint = new DoublePoint(null, 0.0, deltaCal.getTimeInMillis());
			pillarList.add(pillarPoint);
			deltaCal.add(field, delta);
		}
		return pillarPoint;
	}

	/**
	 * Initializes a Calendar objects in a way that all fields smaller than
	 * {@code field} are set to their minimum. E.g. {@code field} is
	 * Calendar.DAY_OF_MONTH then Calendar.HOUR_OF_DAY, Calendar.MINUTE and
	 * Calendar.SECOND are set to 0;
	 * 
	 * @param date
	 *            object that should be initialized
	 * @param field
	 *            fixed Calendar field (e.g. Calendar.DAY_OF_MONTH)
	 * @throws IllegalArgumentException
	 *             if {@code field} is not supported by the method
	 */
	public static void initDate(Calendar date, int field)
			throws IllegalArgumentException {
		switch (field) {
		case Calendar.YEAR:
			date.set(Calendar.MONTH, 0);
		case Calendar.MONTH:
			date.set(Calendar.DAY_OF_MONTH, 1);
		case Calendar.DAY_OF_MONTH:
		case Calendar.DAY_OF_WEEK:
		case Calendar.DAY_OF_YEAR:
			date.set(Calendar.HOUR_OF_DAY, 0);
		case Calendar.HOUR_OF_DAY:
			date.set(Calendar.MINUTE, 0);
		case Calendar.MINUTE:
			date.set(Calendar.SECOND, 0);
		case Calendar.SECOND:
			date.set(Calendar.MILLISECOND, 0);
			break;
		case Calendar.WEEK_OF_YEAR:
			date.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			date.set(Calendar.HOUR_OF_DAY, 0);
			date.set(Calendar.MINUTE, 0);
			date.set(Calendar.SECOND, 0);
			date.set(Calendar.MILLISECOND, 0);
			break;
		default:
			throw new IllegalArgumentException(
					"Value for argument 'field' not supported");
		}
	}
}
