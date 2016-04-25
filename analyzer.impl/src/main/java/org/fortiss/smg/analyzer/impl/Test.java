package org.fortiss.smg.analyzer.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeoutException;

import org.apache.commons.math3.analysis.interpolation.LoessInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.fortiss.smg.analyzer.api.DataSet;
import org.fortiss.smg.analyzer.impl.calculations.Sum;
import org.fortiss.smg.analyzer.impl.calculations.centralTendency.ArithmeticMean;
import org.fortiss.smg.analyzer.impl.calculations.centralTendency.ArithmeticMeanTime;
import org.fortiss.smg.analyzer.impl.calculations.centralTendency.CalculationMethods;
import org.fortiss.smg.analyzer.impl.calculations.centralTendency.CubicMean;
import org.fortiss.smg.analyzer.impl.calculations.centralTendency.GeometricMean;
import org.fortiss.smg.analyzer.impl.calculations.centralTendency.HarmonicMean;
import org.fortiss.smg.analyzer.impl.calculations.centralTendency.Median;
import org.fortiss.smg.analyzer.impl.calculations.centralTendency.PercentBool;
import org.fortiss.smg.analyzer.impl.calculations.centralTendency.RootMeanSquare;
import org.fortiss.smg.analyzer.impl.databaseConnection.DatabaseRequestor;
import org.fortiss.smg.analyzer.impl.databaseConnection.SQL;
import org.fortiss.smg.analyzer.impl.pattern.ClassifierAll;
import org.fortiss.smg.analyzer.impl.pattern.ClassifierSingle;
import org.fortiss.smg.analyzer.impl.pattern.ClassifierSome;
import org.fortiss.smg.analyzer.impl.pattern.Interpolator;
import org.fortiss.smg.analyzer.impl.pattern.Peak;
import org.fortiss.smg.analyzer.impl.visualization.CSVReader;
import org.fortiss.smg.analyzer.impl.visualization.CSVWriter;
import org.fortiss.smg.analyzer.impl.visualization.OutWriter;
import org.fortiss.smg.analyzer.impl.visualization.Plotter;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.containermanager.api.devices.SIUnitType;
import org.fortiss.smg.informationbroker.api.DoublePoint;
import org.fortiss.smg.informationbroker.api.InformationBrokerInterface;

/**
 * test class for testing methods without junit - but now they are substituted
 * by junit. This methods still exist to show an exemplary use of the methods
 * provided by the analyzer.
 * 
 * @author Ann Katrin Gibtner (annkatrin.gibtner@tum.de)
 *
 */
public class Test {
	/**
	 * fetches and logs all elements of a data set
	 * 
	 * @param broker
	 */
	public static void fetch(InformationBrokerInterface broker)
			throws TimeoutException {
		Calendar stop = Calendar.getInstance();
		Calendar start = Calendar.getInstance();
		stop.set(2014, 1, 30, 23, 57, 49);
		start.set(2013, 8, 19, 10, 30, 0);

		DataSet electricty1 = new DataSet(start.getTimeInMillis(), stop.getTimeInMillis(), new DeviceId(
				SIUnitType.W.toString(),
				"siemensQ?C1/MMXU1.W.phsA.cVal.cVal.mag"), null);

		try {
			DatabaseRequestor.fetchesDataSet(electricty1, broker);
		} catch (SQLException e) {
			// if this one is caught, you need to check the database if there
			// are events for the time and device stated above
			e.printStackTrace();
		}

		System.out.println("***** START FETCHING *****");
		System.out.println("Stop Date: "
				+ electricty1.getStopDate());
		System.out.println("Start Date: "
				+ electricty1.getStartDate());

		electricty1.toString();
	}

	/**
	 * generates all possible mean calculation for the given list of items
	 * 
	 * @param broker
	 */
	public static void mean(InformationBrokerInterface broker)
			throws TimeoutException {
		Calendar stop = Calendar.getInstance();
		Calendar start = Calendar.getInstance();
		stop.set(2014, 1, 30, 23, 57, 49);
		start.set(2013, 8, 19, 10, 30, 0);

		DataSet electricty1 = new DataSet(start.getTimeInMillis(), stop.getTimeInMillis(), new DeviceId(
				SIUnitType.W.toString(),
				"siemensQ?C1/MMXU1.W.phsA.cVal.cVal.mag"), null);

		try {
			DatabaseRequestor.fetchesDataSet(electricty1, broker);
		} catch (SQLException e) {
			// if this one is caught, you need to check the database if there
			// are events for the time and device stated above
			e.printStackTrace();
		}

		List<DoublePoint> fetchOnly = electricty1.getDataList();
		Sum sum = new Sum();
		ArithmeticMean arith = new ArithmeticMean();
		Median median = new Median();
		GeometricMean geo = new GeometricMean();
		HarmonicMean harmo = new HarmonicMean();
		RootMeanSquare square = new RootMeanSquare();
		CubicMean cubic = new CubicMean();

		System.out.println("***** MEAN CALCULATIONS *****");
		System.out.println("Sum: " + sum.calculate(fetchOnly));
		System.out.println("Arithmetic Mean " + arith.calculate(fetchOnly));
		System.out.println("Median " + median.calculate(fetchOnly));
		System.out.println("Geometric Mean " + geo.calculate(fetchOnly));
		System.out.println("Harmonic Mean " + harmo.calculate(fetchOnly));
		System.out.println("Square Mean " + square.calculate(fetchOnly));
		System.out.println("Cubic Mean " + cubic.calculate(fetchOnly));
	}

	/**
	 * Calculates the percentage of true values in all measured values
	 * 
	 * @param broker
	 */
	public static void percentBool(InformationBrokerInterface broker)
			throws TimeoutException {
		Calendar lightStop = Calendar.getInstance();
		Calendar lightStart = Calendar.getInstance();
		lightStop.set(2013, 10, 14, 16, 23, 0);
		lightStart.set(2013, 10, 11, 17, 43, 51);

		DataSet boolData = new DataSet(lightStart.getTimeInMillis(), lightStop.getTimeInMillis(), new DeviceId(
				null, "enoceanQ?office1030light"), null);

		try {
			DatabaseRequestor.fetchesDataSet(boolData, broker);
		} catch (SQLException e) {
			// if this one is caught, you need to check the database if there
			// are events for the time and device stated above
			e.printStackTrace();
		}

		PercentBool perc = new PercentBool();
		System.out.println("Percentage Bool "
				+ perc.calculate(boolData.getDataList()));
	}

	/**
	 * Calculates an barChart using a specified mean
	 * 
	 * @param broker
	 */
	public static void useBarChartVar(InformationBrokerInterface broker)
			throws TimeoutException {
		Calendar startDate = Calendar.getInstance();
		Calendar stopDate = Calendar.getInstance();
		startDate.set(2013, 9, 1, 00, 00, 00);
		stopDate.set(2014, 2, 1, 00, 00, 00);

		DataSet dataSet = new DataSet(startDate.getTimeInMillis(), stopDate.getTimeInMillis(), new DeviceId(
				SIUnitType.CELSIUS.toString(), "enoceanQ?0003B078"), null);
		try {
			DatabaseRequestor.fetchesDataSet(dataSet, broker);
		} catch (SQLException e1) {
			// if this one is caught, you need to check the database if there
			// are events for the time and device stated above
			e1.printStackTrace();
		}

		CalculationMethods cal = new ArithmeticMean();

		System.out.println("***** CREATE BAR CHART PILLAR *****");
		System.out.println("Stop Date: "
				+ dataSet.getStopDate());
		System.out.println("Start Date: "
				+ dataSet.getStartDate());
		List<DoublePoint> pillarList = new ArrayList<DoublePoint>();
		pillarList = Comparison.barChart(dataSet, Calendar.MONTH, 1, cal);
		pillarList.toString();

		// draw diagramm
		OutWriter out = new CSVWriter();
		LinkedHashMap<String, List<DoublePoint>> map = new LinkedHashMap<String, List<DoublePoint>>();

		if (pillarList == null || pillarList.isEmpty()) {
			System.out.println("no elements found");
		} else {
			for (DoublePoint point : pillarList) {
				ArrayList<DoublePoint> pointList = new ArrayList<DoublePoint>();
				pointList.add(point);
				String time = String.format("%d", point.getTime());
				map.put(time, pointList);
			}
		}
		List<String> headers = new ArrayList<String>();
		headers.add("Zeit in Monate");
		headers.add("Aggregierte Werte des Sensors enoceanQ?0003B078");
		try {
			out.writeDoublePoint(
					map,
					new FileOutputStream(
							new File(
									"C:\\xampp\\htdocs\\fortiss\\csv\\longterm_temp_month.csv")),
					headers);
		} catch (FileNotFoundException e) {
			// might need to create the folders if they don't exist
			e.printStackTrace();
		}
	}

	/**
	 * Calculates pie chart using the given {@code DoubleAnalyzer}
	 * 
	 * @param total
	 *            total pie
	 * @param fractions
	 *            parts of pie chart
	 */
	public static void testDeviceComparePercent(
			InformationBrokerInterface broker) throws TimeoutException {
		Calendar start = Calendar.getInstance();
		Calendar stop = Calendar.getInstance();
		start.set(2013, 11, 1, 0, 0, 0);
		stop.set(2013, 11, 2, 0, 0, 0);

		DataSet outside = new DataSet(start.getTimeInMillis(), stop.getTimeInMillis(), new DeviceId(
				SIUnitType.CELSIUS.toString(), "enoceanQ?0003B078"));
		DataSet meetingRoom = new DataSet(start.getTimeInMillis(), stop.getTimeInMillis(), new DeviceId(
				SIUnitType.CELSIUS.toString(), "enoceanQ?010066F4_Actual"));
		DataSet room2 = new DataSet(start.getTimeInMillis(), stop.getTimeInMillis(), new DeviceId(
				SIUnitType.CELSIUS.toString(), "enoceanQ?01003E48_Actual"));
		DataSet solar = new DataSet(start.getTimeInMillis(), stop.getTimeInMillis(), new DeviceId(
				SIUnitType.CELSIUS.toString(), "solarlogQ?EinspeiseWerte"));
		try {
			DatabaseRequestor.fetchesDataSet(outside, broker);
			DatabaseRequestor.fetchesDataSet(meetingRoom, broker);
			DatabaseRequestor.fetchesDataSet(room2, broker);
			DatabaseRequestor.fetchesDataSet(solar, broker);
		} catch (SQLException e1) {
			// if this one is caught, you need to check the database if there
			// are events for the time and device stated above
			e1.printStackTrace();
		}

		Plotter.singleDataSet(meetingRoom,
				"C:\\xampp\\htdocs\\fortiss\\csv\\ana01.csv");
		Plotter.singleDataSet(room2,
				"C:\\xampp\\htdocs\\fortiss\\csv\\ana02.csv");

		List<DataSet> comparer = new ArrayList<DataSet>();
		comparer.add(room2);
		comparer.add(meetingRoom);
		comparer.add(solar);

		System.out.println("***** TEST PIE *****");
		System.out.println("Stop Date: " + stop.getTime().toString());
		System.out.println("Start Date: " + start.getTime().toString());
		HashMap<DeviceId, Double> finish = Comparison.deviceComparePercent(
				outside, comparer, new ArithmeticMean());

		CalculationMethods cal = new ArithmeticMean();
		System.out.println(cal.calculate(meetingRoom.getDataList()));
		System.out.println(cal.calculate(room2.getDataList()));

		// write a nice diagram
		OutWriter out = new CSVWriter();
		LinkedHashMap<String, List<DoublePoint>> map = new LinkedHashMap<String, List<DoublePoint>>();

		for (DeviceId key : finish.keySet()) {
			ArrayList<DoublePoint> list = new ArrayList<DoublePoint>();
			list.add(new DoublePoint(finish.get(key), 0.0, 0));
			map.put(key.toString(), list);
		}

		List<String> header = new ArrayList<String>();
		header.add("origin");
		header.add("percent");
		try {
			out.writeDoublePoint(map, new FileOutputStream(new File(
					"C:\\xampp\\htdocs\\fortiss\\csv\\testpie.csv")), header);
		} catch (FileNotFoundException e) {
			// might need to create the folders if they don't exist
			e.printStackTrace();
		}

		System.out.println("Pie finished");

	}

	/**
	 * tests the deviceCompareFraction function with the data of different
	 * devices
	 * 
	 * @param logger
	 *            for logging information
	 * @param broker
	 *            database interface
	 */
	public static void testDeviceCompareFraction(
			InformationBrokerInterface broker) throws TimeoutException {
		Calendar start = Calendar.getInstance();
		Calendar stop = Calendar.getInstance();
		start.set(2013, 11, 1, 0, 0, 0);
		stop.set(2013, 11, 2, 0, 0, 0);

		// testing Compare Fraction
		DataSet westWingSicherung = new DataSet(start.getTimeInMillis(), stop.getTimeInMillis(), new DeviceId(
				SIUnitType.W.toString(), "siemensQ153?C2/MMXU1.TotW.mag.mag"));
		DataSet westWingSwitch = new DataSet(start.getTimeInMillis(), stop.getTimeInMillis(), new DeviceId(
				SIUnitType.W.toString(), "siemensQ153?C4/MMXU1.TotW.mag.mag"));
		DataSet eastWingSicherung = new DataSet(start.getTimeInMillis(), stop.getTimeInMillis(), new DeviceId(
				SIUnitType.W.toString(), "siemensQ153?C1/MMXU1.TotW.mag.mag"));
		DataSet eastWingSwitch = new DataSet(start.getTimeInMillis(), stop.getTimeInMillis(), new DeviceId(
				SIUnitType.W.toString(), "siemensQ153?C3/MMXU1.TotW.mag.mag"));
		DataSet server = new DataSet(start.getTimeInMillis(), stop.getTimeInMillis(), new DeviceId(
				SIUnitType.W.toString(), "siemensQ?C1/MMXU1.TotW.mag.mag"));
		DataSet total = new DataSet(start.getTimeInMillis(), stop.getTimeInMillis(), new DeviceId(
				SIUnitType.W.toString(), "acc.VPowerTracker1"));

		try {
			DatabaseRequestor.fetchesDataSet(westWingSicherung, broker);
			DatabaseRequestor.fetchesDataSet(westWingSwitch, broker);
			DatabaseRequestor.fetchesDataSet(eastWingSicherung, broker);
			DatabaseRequestor.fetchesDataSet(eastWingSwitch, broker);
			DatabaseRequestor.fetchesDataSet(server, broker);
			DatabaseRequestor.fetchesDataSet(total, broker);
		} catch (SQLException e1) {
			// if this one is caught, you need to check the database if there
			// are events for the time and device stated above
			e1.printStackTrace();
		}

		// searches for complete data set
		Plotter.singleDataSet(total,
				"C:\\xampp\\htdocs\\fortiss\\csv\\ana01.csv");
		Plotter.singleDataSet(server,
				"C:\\xampp\\htdocs\\fortiss\\csv\\ana02.csv");
		Plotter.singleDataSet(eastWingSwitch,
				"C:\\xampp\\htdocs\\fortiss\\csv\\ana03.csv");
		Plotter.singleDataSet(eastWingSicherung,
				"C:\\xampp\\htdocs\\fortiss\\csv\\ana04.csv");
		Plotter.singleDataSet(westWingSwitch,
				"C:\\xampp\\htdocs\\fortiss\\csv\\ana05.csv");
		Plotter.singleDataSet(westWingSicherung,
				"C:\\xampp\\htdocs\\fortiss\\csv\\ana06.csv");

		System.out.println("Finished TrendAna Print");

		// Calculates totals
		CalculationMethods cal = new ArithmeticMeanTime();
		System.out.println(cal.calculate(westWingSicherung.getDataList())
				+ westWingSicherung.getDeviceId().toString());
		System.out.println(cal.calculate(westWingSwitch.getDataList())
				+ westWingSwitch.getDeviceId().toString());
		System.out.println(cal.calculate(eastWingSicherung.getDataList())
				+ eastWingSicherung.getDeviceId().toString());
		System.out.println(cal.calculate(eastWingSwitch.getDataList())
				+ eastWingSwitch.getDeviceId().toString());
		System.out.println(cal.calculate(server.getDataList())
				+ server.getDeviceId().toString());
		System.out.println(cal.calculate(total.getDataList())
				+ total.getDeviceId().toString());

		List<DataSet> comparer = new ArrayList<DataSet>();
		comparer.add(westWingSicherung);
		comparer.add(westWingSwitch);
		comparer.add(eastWingSicherung);
		comparer.add(eastWingSwitch);
		comparer.add(server);

		System.out.println("***** TEST PIE *****");
		System.out.println("Stop Date: " + stop.getTime().toString());
		System.out.println("Start Date: " + start.getTime().toString());
		HashMap<DeviceId, Double> finish = Comparison.deviceCompareFraction(
				total, comparer, new ArithmeticMeanTime());

		double sum = 0.0;
		for (DeviceId key : finish.keySet()) {
			sum += finish.get(key);
		}
		System.out.println("total: " + sum);

		// write a nice diagram
		OutWriter out = new CSVWriter();
		LinkedHashMap<String, List<DoublePoint>> map = new LinkedHashMap<String, List<DoublePoint>>();

		for (DeviceId key : finish.keySet()) {
			ArrayList<DoublePoint> list = new ArrayList<DoublePoint>();
			list.add(new DoublePoint(finish.get(key), 0.0, 0));
			map.put(key.toString(), list);
		}

		List<String> header = new ArrayList<String>();
		header.add("origin");
		header.add("percent");
		try {
			out.writeDoublePoint(
					map,
					new FileOutputStream(
							new File(
									"C:\\xampp\\htdocs\\fortiss\\csv\\compare_origin_power_fraction.csv")),
					header);
		} catch (FileNotFoundException e) {
			// might need to create the folders if they don't exist
			e.printStackTrace();
		}

		System.out.println("Pie finished");
	}

	/**
	 * tests the timeCompare function by the values of December in comparison to
	 * November
	 * 
	 * @param logger
	 *            for logging information
	 * @param broker
	 *            database connection
	 */
	public static void testTimeCompare(InformationBrokerInterface broker)
			throws TimeoutException {
		Calendar start = Calendar.getInstance();
		Calendar stop = Calendar.getInstance();
		start.set(2013, 11, 1, 00, 00, 00);
		stop.set(2013, 11, 31, 00, 00, 00);

		System.out.println("***** TEST Time *****");
		System.out.println("Stop Date: " + stop.getTime().toString());
		System.out.println("Start Date: " + start.getTime().toString());

		DataSet dezember = new DataSet(start.getTimeInMillis(), stop.getTimeInMillis(), new DeviceId(
				SIUnitType.W.toString(), "acc.VPowerTracker1"));
		try {
			DatabaseRequestor.fetchesDataSet(dezember, broker);
		} catch (SQLException e1) {
			// if this one is caught, you need to check the database if there
			// are events for the time and device stated above
			e1.printStackTrace();
		}

		List<DoublePoint> dezemberPoints = Comparison.barChart(dezember,
				Calendar.DAY_OF_MONTH, 1, new ArithmeticMeanTime());

		Calendar startNov = Calendar.getInstance();
		Calendar stopNov = Calendar.getInstance();
		startNov.setTimeInMillis(start.getTimeInMillis());
		stopNov.setTimeInMillis(stop.getTimeInMillis());
		startNov.add(Calendar.MONTH, -1);
		stopNov.add(Calendar.MONTH, -1);

		System.out.println("NovStop Date: " + stopNov.getTime().toString());
		System.out.println("NovStart Date: " + startNov.getTime().toString());

		DataSet november = new DataSet(startNov.getTimeInMillis(), stopNov.getTimeInMillis(), new DeviceId(
				SIUnitType.W.toString(), "acc.VPowerTracker1"));
		try {
			DatabaseRequestor.fetchesDataSet(november, broker);
		} catch (SQLException e1) {
			// if this one is caught, you need to check the database if there
			// are events for the time and device stated above
			e1.printStackTrace();
		}

		List<DoublePoint> novemberPoints = Comparison.barChart(november,
				Calendar.DAY_OF_MONTH, 1, new ArithmeticMeanTime());

		List<DataSet> comparer = new ArrayList<DataSet>();
		comparer.add(november);

		List<DataSet> sol = Comparison.timeComparePercent(dezember, comparer,
				Calendar.DAY_OF_MONTH, 1, new ArithmeticMeanTime());

		// print csv
		OutWriter out = new CSVWriter();
		LinkedHashMap<String, List<DoublePoint>> map = new LinkedHashMap<String, List<DoublePoint>>();

		for (Iterator<DoublePoint> iterSol = sol.get(0).getDataList()
				.iterator(), iterDez = dezemberPoints.iterator(), iterNov = novemberPoints
				.iterator(); iterSol.hasNext() && iterDez.hasNext()
				&& iterNov.hasNext();) {
			DoublePoint solNext = (DoublePoint) iterSol.next();
			DoublePoint dezNext = (DoublePoint) iterDez.next();
			DoublePoint novNext = (DoublePoint) iterNov.next();
			ArrayList<DoublePoint> list = new ArrayList<DoublePoint>();
			list.add(solNext);
			list.add(dezNext);
			list.add(novNext);
			String time = String.format("%d", solNext.getTime());
			map.put(time, list);

		}

		ArrayList<String> headers = new ArrayList<String>();
		headers.add("Zeit in Tagen");
		headers.add("Unterschied");
		headers.add("Dezember");
		headers.add("November");

		try {
			out.writeDoublePoint(map, new FileOutputStream(new File(
					"C:\\xampp\\htdocs\\fortiss\\csv\\zeitVergl_strom01.csv")),
					headers);
		} catch (FileNotFoundException e) {
			// might need to create the folders if they don't exist
			e.printStackTrace();
		}
	}

	/**
	 * tests selfCompare with some values
	 * 
	 * @param logger
	 * @param broker
	 */
	public static void selfComp(InformationBrokerInterface broker)
			throws TimeoutException {
		System.out.println("***** TEST SELF COMPARE *****");
		Calendar tempstart = Calendar.getInstance();
		Calendar tempstop = Calendar.getInstance();
		tempstart.set(2013, 10, 5, 0, 0, 1);
		tempstop.set(2013, 10, 5, 23, 59, 59);
		System.out.println("Stop Date: " + tempstop.getTime().toString());
		System.out.println("Start Date: " + tempstart.getTime().toString());
		DeviceId tempdev = new DeviceId(SIUnitType.CELSIUS.toString(),
				"enoceanQ?0003B078");
		ArithmeticMean arith = new ArithmeticMean();

		DataSet temp = new DataSet(tempstart.getTimeInMillis(), tempstop.getTimeInMillis(), tempdev);
		try {
			DatabaseRequestor.fetchesDataSet(temp, broker);
		} catch (SQLException e1) {
			// if this one is caught, you need to check the database if there
			// are events for the time and device stated above
			e1.printStackTrace();
		}

		Calendar temp2start = Calendar.getInstance();
		Calendar temp2stop = Calendar.getInstance();
		temp2start.setTimeInMillis(tempstart.getTimeInMillis());
		temp2start.add(Calendar.DAY_OF_MONTH, 1);
		temp2stop.setTimeInMillis(tempstop.getTimeInMillis());
		temp2stop.add(Calendar.DAY_OF_MONTH, 1);
		DataSet temp2 = new DataSet(temp2start.getTimeInMillis(), temp2stop.getTimeInMillis(), tempdev);
		try {
			DatabaseRequestor.fetchesDataSet(temp2, broker);
		} catch (SQLException e1) {
			// if this one is caught, you need to check the database if there
			// are events for the time and device stated above
			e1.printStackTrace();
		}
		List<DataSet> comparer = new ArrayList<DataSet>();
		comparer.add(temp2);

		List<DataSet> solution = Comparison.timeCompareAbsolute(temp, comparer,
				Calendar.HOUR_OF_DAY, 1, arith);
		solution.toString();

		// write a nice diagram
		OutWriter out = new CSVWriter();
		LinkedHashMap<String, List<DoublePoint>> map = new LinkedHashMap<String, List<DoublePoint>>();

		int hour = 0;
		for (DoublePoint point : solution.get(0).getDataList()) {
			ArrayList<DoublePoint> list = new ArrayList<DoublePoint>();
			list.add(point);
			map.put("" + (hour++), list);
		}
		try {
			out.writeDoublePoint(map, new FileOutputStream(new File(
					"D:\\Uni\\testfile.csv")), "hour");
		} catch (FileNotFoundException e) {
			// might need to create the folders if they don't exist
			e.printStackTrace();
		}
	}

	/**
	 * tests the quantilePlotter function with results of a bar chart
	 * 
	 * @param ana
	 *            TrendAnalyzer
	 */
	public static void plotterQuantile(InformationBrokerInterface broker)
			throws TimeoutException {
		Calendar tempStart = Calendar.getInstance();
		Calendar tempStop = Calendar.getInstance();
		tempStart.set(2013, 8, 1, 00, 15, 13);
		tempStop.set(2013, 10, 1, 12, 23, 13);

		DataSet temperaturData = new DataSet(tempStart.getTimeInMillis(), tempStop.getTimeInMillis(), new DeviceId(
				SIUnitType.CELSIUS.toString(), "enoceanQ?0003B078"));
		try {
			DatabaseRequestor.fetchesDataSet(temperaturData, broker);
		} catch (SQLException e) {
			// if this one is caught, you need to check the database if there
			// are events for the time and device stated above
			e.printStackTrace();
		}

		ArithmeticMean mean = new ArithmeticMean();
		List<DoublePoint> list = Comparison.barChart(temperaturData,
				Calendar.DAY_OF_MONTH, 1, mean);
		list.toString();
		Plotter.quantile(list, "D:\\Uni\\quantilePlot.csv");
	}

	// TODO: comment
	public static void interpolateSpilne(InformationBrokerInterface broker)
			throws TimeoutException {
		Calendar tempStart = Calendar.getInstance();
		Calendar tempStop = Calendar.getInstance();
		tempStart.set(2013, 8, 1, 00, 15, 13);
		tempStop.set(2013, 10, 1, 12, 23, 13);

		DataSet temperature = new DataSet(tempStart.getTimeInMillis(), tempStop.getTimeInMillis(), new DeviceId(
				SIUnitType.CELSIUS.toString(), "enoceanQ?0003B078"));
		try {
			DatabaseRequestor.fetchesDataSet(temperature, broker);
		} catch (SQLException e) {
			// if this one is caught, you need to check the database if there
			// are events for the time and device stated above
			e.printStackTrace();
		}

		System.out.println("Start Interpol");
		PolynomialSplineFunction funcInterpol = Interpolator
				.interpolateSpline(temperature);
		System.out.println("finished Interpol" + funcInterpol.toString());
	}

	// TODO: comment
	public static void sqlTest(InformationBrokerInterface broker)
			throws TimeoutException {
		// get the newest and oldest event for a double type of the old database
		try {
			System.out.println("sqlDouble latest: "
					+ SQL.newest(
							new DeviceId(SIUnitType.W.toString(),
									"siemensQ?C1/MMXU1.W.phsA.cVal.cVal.mag"),
							broker).toString());
			System.out.println("sqlDouble oldest: "
					+ SQL.oldest(
							new DeviceId(SIUnitType.W.toString(),
									"siemensQ?C1/MMXU1.W.phsA.cVal.cVal.mag"),
							broker).toString());
			// get newest and oldest event for a boolean type of the old
			// database
			System.out.println("sqlBool lastest: "
					+ SQL.newest(
							new DeviceId(null, "enoceanQ?office1030light"),
							broker).toString());
			System.out.println("sqlBool oldest: "
					+ SQL.oldest(
							new DeviceId(null, "enoceanQ?office1030light"),
							broker).toString());
			// get newest and oldest event for a double type of the new database
			System.out.println("sqlNew latest: "
					+ SQL.newest(new DeviceId("solar_generator_watt",
							"solarlog.wrapper"), broker));
			System.out.println("sqlNew oldest: "
					+ SQL.oldest(new DeviceId("solar_generator_watt",
							"solarlog.wrapper"), broker));
		} catch (SQLException e) {
			// if this one is caught, you need to check the database if there
			// are events for the time and device stated above
			e.printStackTrace();
		}

	}

	// TODO comment
	public static void classifyAll(InformationBrokerInterface broker,
			DeviceId device) throws TimeoutException {

		ClassifierAll classAll = null;
		try {
			classAll = new ClassifierAll(device, broker, Calendar.MONDAY,
					Calendar.DAY_OF_MONTH, 1, 30);
		} catch (SQLException e) {
			// if this one is caught, you need to check the database if there
			// are events for the time and device stated above
			e.printStackTrace();
		}
		System.out.println(classAll.getAllDataSets().size());

		// builds lists for plotter
		List<PolynomialSplineFunction> funcList = new ArrayList<PolynomialSplineFunction>();
		funcList.add(classAll.getInterpol().getFunction());
		List<String> origin = new ArrayList<String>();
		origin.add("Zeit in Stunden");
		origin.add("Klassifikator");
		Plotter.function(funcList, origin, 100,
				"C:\\xampp\\htdocs\\fortiss\\csv\\loess.csv");

		System.out.println(classAll.getSimilar().size());
		int i = 1;
		for (DataSet analy : classAll.getAllDataSets()) {
			Plotter.functionAndDataSet(classAll.getInterpol(), analy,
					"C:\\xampp\\htdocs\\fortiss\\csv\\function" + i + ".csv");
			i++;
		}

		SimpleDateFormat formateDate = new SimpleDateFormat("yyyy MM dd");
		for (DataSet analy : classAll.getSimilar()) {
			System.out.println(formateDate.format(analy.getStartDate()));
		}
		System.out.println("finished loess interpolating");
	}

	/**
	 * tests the classifier with a single element
	 * 
	 * @param logger
	 * @param broker
	 * @param device
	 */
	public static void classifySingle(InformationBrokerInterface broker,
			DeviceId device) throws TimeoutException {
		ClassifierSingle classi = null;
		try {
			classi = new ClassifierSingle(device, broker, Calendar.MONDAY,
					Calendar.DAY_OF_MONTH, 1);
		} catch (SQLException e) {
			// if this one is caught, you need to check the database if there
			// are events for the time and device stated above
			e.printStackTrace();
		}
		classi.classifySingle(classi.getAllDataSets().get(14), 30);
		System.out.println("All Mondays " + classi.getAllDataSets().size());
		System.out.println("Similar " + classi.getSimilar().size());

		// builds map for plotter loess
		List<PolynomialSplineFunction> funcList = new ArrayList<PolynomialSplineFunction>();
		funcList.add(classi.getInterpol().getFunction());
		List<String> origin = new ArrayList<String>();
		origin.add("Zeit in Stunden");
		origin.add("Klassifikator");
		Plotter.function(funcList, origin, 100,
				"C:\\xampp\\htdocs\\fortiss\\csv\\loess.csv");

		// build map for plotter both, loess and comparing dataset
		int i = 1;
		for (DataSet analy : classi.getAllDataSets()) {
			Plotter.functionAndDataSet(classi.getInterpol(), analy,
					"C:\\xampp\\htdocs\\fortiss\\csv\\function" + i + ".csv");
			i++;
		}

		// prints all dates that were similar and classifier date
		SimpleDateFormat formateDate = new SimpleDateFormat("yyyy MM dd");
		System.out.println("Datum Klassifikator: "
				+ formateDate.format(classi.getAllDataSets().get(14)
						.getStartDate()));
		for (DataSet analy : classi.getSimilar()) {
			System.out.println(formateDate.format(analy.getStartDate()));
		}

		Plotter.singleDataSet(classi.getAllDataSets().get(4),
				"C:\\xampp\\htdocs\\fortiss\\csv\\allPoints.csv");

		System.out.println("finished loess interpolating");
	}

	public static void classifySome(InformationBrokerInterface broker,
			DeviceId device) throws TimeoutException {
		// initializing ClassiferSome
		ClassifierSome classi = null;
		try {
			classi = new ClassifierSome(device, broker, Calendar.MONDAY,
					Calendar.DAY_OF_MONTH, 1);
		} catch (SQLException e) {
			// if this one is caught, you need to check the database if there
			// are events for the time and device stated above
			e.printStackTrace();
		}
		List<DataSet> classifiers = new ArrayList<DataSet>();
		classifiers.add(classi.getAllDataSets().get(10));
		classifiers.add(classi.getAllDataSets().get(12));
		classifiers.add(classi.getAllDataSets().get(13));
		classi.classifySome(classifiers, 30);
		System.out.println("All Mondays " + classi.getAllDataSets().size());
		System.out.println("Similar " + classi.getSimilar().size());

		// builds map for plotter loess
		List<PolynomialSplineFunction> funcList = new ArrayList<PolynomialSplineFunction>();
		funcList.add(classi.getInterpol().getFunction());
		List<String> origin = new ArrayList<String>();
		origin.add("Zeit in Stunden");
		origin.add("Klassifikator");
		Plotter.function(funcList, origin, 100,
				"C:\\xampp\\htdocs\\fortiss\\csv\\loess.csv");

		// build map for plotter both, loess and all comparing data sets
		int i = 1;
		for (DataSet analy : classi.getAllDataSets()) {
			Plotter.functionAndDataSet(classi.getInterpol(), analy,
					"C:\\xampp\\htdocs\\fortiss\\csv\\function" + i + ".csv");
			i++;
		}

		// prints all dates that were similar
		SimpleDateFormat formateDate = new SimpleDateFormat("yyyy MM dd");
		for (DataSet analy : classi.getSimilar()) {
			System.out.println(formateDate.format(analy.getStartDate()));
		}
		System.out.println("finished loess interpolating");
	}

	public static void peak(InformationBrokerInterface broker, DeviceId device)
			throws TimeoutException {
		ClassifierSome classi = null;
		try {
			classi = new ClassifierSome(device, broker, Calendar.MONDAY,
					Calendar.DAY_OF_MONTH, 1);
		} catch (SQLException e) {
			// if this one is caught, you need to check the database if there
			// are events for the time and device stated above
			e.printStackTrace();
		}
		List<DataSet> classifiers = new ArrayList<DataSet>();
		classifiers.add(classi.getAllDataSets().get(10));
		classifiers.add(classi.getAllDataSets().get(12));
		classifiers.add(classi.getAllDataSets().get(13));
		classi.classifySome(classifiers, 30);

		List<List<DoublePoint>> allPeaks = Peak.findPeaks(0, 30, 100, classi
				.getAllDataSets().get(9), classi.getInterpol());

		System.out.println("Peaks " + allPeaks.size());
		SimpleDateFormat formateDate = new SimpleDateFormat("yyyy MM dd");
		SimpleDateFormat formateTime = new SimpleDateFormat("HH:mm:ss SSSS");
		System.out.println("Datum Vergleichstag: "
				+ formateDate.format(classi.getAllDataSets().get(9)
						.getStartDate()));
		System.out.println("");
		int i = 1;
		for (List<DoublePoint> single : allPeaks) {
			System.out
					.println("Elemente in Liste " + i + " : " + single.size());
			System.out.println("Starttime of List "
					+ i
					+ " : "
					+ formateTime.format(single.get(0).getCalendarTime()
							.getTime()));
			System.out.println("Stoptime of List "
					+ i
					+ " : "
					+ formateTime.format(single.get(single.size() - 1)
							.getCalendarTime().getTime()));
			i++;
		}

		Plotter.peakAndFunction(allPeaks, classi.getAllDataSets().get(9),
				classi.getInterpol(),
				"C:\\xampp\\htdocs\\fortiss\\csv\\peaks.csv");
	}

	public static void explainLoess() {
		LinkedHashMap<String, String[]> foundData = CSVReader
				.read("C:\\xampp\\htdocs\\fortiss\\csv\\loess_erklaer01.csv");

		double[] x = new double[foundData.size() - 1];
		double[] y = new double[foundData.size() - 1];
		int counter = 0;
		// convert String to double
		for (Entry<String, String[]> entry : foundData.entrySet()) {
			if (entry.getKey().equals("Zeit / relative Einheit")) {
			} else {
				x[counter] = Double.parseDouble(entry.getKey());
				y[counter] = Double.parseDouble(entry.getValue()[0]);
				counter++;
			}
		}

		// doing only a single Loess iteration
		LoessInterpolator loessInter = new LoessInterpolator(0.3, 10, 0);
		double[] ySmoothed = loessInter.smooth(x, y);
		System.out.println("Punkt 11 gesmoothed: " + ySmoothed[10]);

		LinkedHashMap<String, List<Double>> print = new LinkedHashMap<String, List<Double>>();
		for (int i = 0; i < ySmoothed.length; i++) {
			List<Double> yValues = new ArrayList<Double>();
			yValues.add(y[i]);
			yValues.add(ySmoothed[i]);
			print.put(String.format("%d", (long) x[i]), yValues);
		}
		List<String> header = new ArrayList<String>();
		header.add("Zeit / relative Einheit");
		header.add("Messwerte");
		header.add("geglaettete Messwerte");

		OutWriter out = new CSVWriter();
		try {
			out.writeDouble(
					print,
					new FileOutputStream(
							new File(
									"C:\\xampp\\htdocs\\fortiss\\csv\\loess_erklaer05Smooth.csv")),
					header);
		} catch (FileNotFoundException e) {
			// might need to create the folders if they don't exist
			e.printStackTrace();
		}
	}
}
