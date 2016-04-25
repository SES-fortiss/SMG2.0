package org.fortiss.smg.analyzer.impl.visualization;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.fortiss.smg.analyzer.api.DataSet;
import org.fortiss.smg.analyzer.impl.calculations.centralTendency.CalculationMethods;
import org.fortiss.smg.analyzer.impl.pattern.Interpolator;
import org.fortiss.smg.informationbroker.api.DoublePoint;

/**
 * provides methods to plot information
 * 
 * @author Ann Katrin Gibtner (annkatrin.gibtner@tum.de)
 *
 */
public class Plotter {

	/**
	 * calculates the quantile value to all elements in the given list and plots
	 * the result in the given location
	 * 
	 * @param list
	 *            all elements
	 * @param savingLocation
	 *            location of plotted file
	 */
	public static void quantile(List<DoublePoint> list,
			String savingLocation) {
		CalculationMethods.sort(list);
		OutWriter writer = new CSVWriter();
		LinkedHashMap<String, List<DoublePoint>> map = new LinkedHashMap<String, List<DoublePoint>>();
		double counter = 0.0;

		for (DoublePoint point : list) {
			counter += 1.0;
			List<DoublePoint> actList = new ArrayList<DoublePoint>();
			actList.add(point);
			map.put("" + (counter - 0.5) / list.size(), actList);
		}

		try {
			writer.writeDoublePoint(map, new FileOutputStream(new File(
					savingLocation)), "Quantiles");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * plots the points of a graph to a csv file
	 * 
	 * @param func
	 *            points are extracted from the function
	 * @param origin
	 *            origin of the values for {@code func}
	 * @param measurePoints
	 *            defines how many points will be extracted; the points will be
	 *            extracted in a way so they have the same distance to each
	 *            other
	 * @param savingLocation
	 *            specifies where the csv file should be saved
	 */
	public static void function(List<PolynomialSplineFunction> func,
			List<String> origin, int measurePoints, String savingLocation) {
		OutWriter writer = new CSVWriter();
		LinkedHashMap<String, List<Double>> map = new LinkedHashMap<String, List<Double>>();
		// calculate least common time interval
		double firstKnot = 0.0;
		double lastKnot = Double.MAX_VALUE;
		for (PolynomialSplineFunction actualSpline : func) {
			double[] knots = actualSpline.getKnots();
			if (firstKnot < knots[0]) {
				firstKnot = knots[0];
			}
			if (lastKnot > knots[knots.length - 1]) {
				lastKnot = knots[knots.length - 1];
			}
		}
		double timeSpan = (lastKnot - firstKnot) / measurePoints;
		for (int i = 0; i < measurePoints; i++) {
			List<Double> yList = new ArrayList<Double>();
			for (PolynomialSplineFunction actualSpline : func) {
				yList.add(actualSpline.value(firstKnot));
			}
			map.put(String.format("%.4f", firstKnot), yList);
			firstKnot += timeSpan;
		}
		try {
			writer.writeDouble(map, new FileOutputStream(new File(
					savingLocation)), origin);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * plots a csv file containing a function and one data set
	 * 
	 * @param function
	 *            function which shall be plotted
	 * @param dataSet
	 *            single data set
	 * @param savingLocation
	 *            location where the file should be saved
	 */
	public static void functionAndDataSet(Interpolator function,
			DataSet dataSet, String savingLocation) {
		OutWriter out = new CSVWriter();
		LinkedHashMap<String, List<Double>> plotterMap = new LinkedHashMap<String, List<Double>>();

		List<DoublePoint> dataList = dataSet.getDataList();
		long difference = dataSet.getStartDate()
				- function.getFunctionStartTime().getTimeInMillis();
		double[] knots = function.getFunction().getKnots();

		for (DoublePoint point : dataList) {
			if (point.getTime() - difference < knots[0]
					|| point.getTime() - difference > knots[knots.length - 1]) {
			} else {
				List<Double> yValues = new ArrayList<Double>();
				yValues.add(function.getFunction().value(
						(double) (point.getTime() - difference)));
				yValues.add(point.getValue());
				plotterMap.put(String.format("%d",
						(long) (point.getTime() - difference)), yValues);
			}
		}
		List<String> origin = new ArrayList<String>();
		origin.add("Zeit in Stunden");
		origin.add("Mustertag");
		Calendar start = Calendar.getInstance();
		start.setTimeInMillis(dataSet.getStartDate());
		SimpleDateFormat formateDate = new SimpleDateFormat("dd.MM.yyyy");
		origin.add("Messwerte des "
				+ formateDate.format(start.getTime()));

		try {
			out.writeDouble(plotterMap, new FileOutputStream(new File(
					savingLocation)), origin);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * plots a csv file containing the data points of a single data set
	 * 
	 * @param dataSet
	 *            data set which should be ploted
	 * @param savingLocation
	 *            location where the file should be saved
	 */
	public static void singleDataSet(DataSet dataSet,
			String savingLocation) {
		OutWriter out = new CSVWriter();
		LinkedHashMap<String, List<Double>> plotterMap = new LinkedHashMap<String, List<Double>>();

		List<DoublePoint> allPoints = dataSet.getDataList();

		for (DoublePoint recentPoint : allPoints) {
			List<Double> values = new ArrayList<Double>();
			values.add(recentPoint.getValue());
			plotterMap.put(String.format("%d", recentPoint.getTime()),
					values);
		}

		List<String> origin = new ArrayList<String>();
		origin.add("Zeit in Stunden");
		SimpleDateFormat formateDate = new SimpleDateFormat("dd MM yyyy");
		Calendar start = Calendar.getInstance();
		start.setTimeInMillis(dataSet.getStartDate());
		origin.add("Messdaten des Montags: "
				+ formateDate.format(start.getTime()));

		try {
			out.writeDouble(plotterMap, new FileOutputStream(new File(
					savingLocation)), origin);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * plots a csv file containing all peaks, the corresponding data set and the
	 * interpolated function
	 * 
	 * @param peaks
	 *            a list of lists (of DoubleDoublePoints) which represent a peak
	 * @param dataSet
	 *            single data set
	 * @param classifyingFunction
	 *            the interpolated function
	 * @param savingLocation
	 *            location where file should be saved
	 */
	public static void peakAndFunction(List<List<DoublePoint>> peaks,
			DataSet dataSet, Interpolator classifyingFunction,
			String savingLocation) {
		List<DoublePoint> allPeaks = new ArrayList<DoublePoint>();
		for (List<DoublePoint> peak : peaks) {
			allPeaks.addAll(peak);
		}

		OutWriter out = new CSVWriter();
		LinkedHashMap<String, List<Double>> plotterMap = new LinkedHashMap<String, List<Double>>();

		List<DoublePoint> dataList = dataSet.getDataList();
		long difference = dataSet.getStartDate()
				- classifyingFunction.getFunctionStartTime().getTimeInMillis();
		double[] knots = classifyingFunction.getFunction().getKnots();

		int i = 0;
		for (DoublePoint point : dataList) {
			if (point.getTime() - difference < knots[0]
					|| point.getTime() - difference > knots[knots.length - 1]) {
			} else {
				List<Double> yValues = new ArrayList<Double>();
				yValues.add(classifyingFunction.getFunction().value(
						(double) (point.getTime() - difference)));
				yValues.add(point.getValue());

				if (i < allPeaks.size()) {
					while (allPeaks.get(i).getCalendarTime()
							.compareTo(point.getCalendarTime()) < 0) {
						i++;
					}
					if (allPeaks.get(i).getCalendarTime()
							.compareTo(point.getCalendarTime()) == 0) {
						yValues.add(allPeaks.get(i).getValue());
						i++;
					} else if (allPeaks.get(i).getCalendarTime()
							.compareTo(point.getCalendarTime()) > 0) {
						yValues.add(null);
					}
				} else {
					yValues.add(null);
				}

				plotterMap.put(String.format("%d",
						(long) (point.getTime() - difference)), yValues);
			}
		}
		List<String> origin = new ArrayList<String>();
		origin.add("Zeit in Stunden");
		origin.add("Mustertag");
		SimpleDateFormat formateDate = new SimpleDateFormat("dd.MM.yyyy");
		Calendar start = Calendar.getInstance();
		start.setTimeInMillis(dataSet.getStartDate());
		origin.add("Messwerte des "
				+ formateDate.format(start.getTime()));
		origin.add("Spitzenwerte");

		try {
			out.writeDouble(plotterMap, new FileOutputStream(new File(
					savingLocation)), origin);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
