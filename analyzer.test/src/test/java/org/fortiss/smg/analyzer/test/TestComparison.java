/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.analyzer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.analyzer.api.DataSet;
import org.fortiss.smg.analyzer.impl.Comparison;
import org.fortiss.smg.analyzer.impl.calculations.Sum;
import org.fortiss.smg.analyzer.impl.calculations.centralTendency.ArithmeticMean;
import org.fortiss.smg.analyzer.impl.databaseConnection.DatabaseRequestor;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.informationbroker.api.DoublePoint;
import org.fortiss.smg.informationbroker.api.InformationBrokerInterface;
import org.fortiss.smg.informationbroker.api.InformationBrokerQueueNames;
import org.fortiss.smg.remoteframework.lib.DefaultProxy;
import org.fortiss.smg.sqltools.lib.utils.TestingDBUtil;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TestComparison {

	private static MockOtherBundles mocker;
	private TestingDBUtil db;

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@BeforeClass
	public static void setUpDataBase() throws SQLException,
			ClassNotFoundException {
		mocker = new MockOtherBundles();
	}

	@Before
	public void setUp() throws IOException, TimeoutException,
			ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		db = new TestingDBUtil();

		db.setTable("doubleevents");
		System.out.println("searching for informationbroker");
		DefaultProxy<InformationBrokerInterface> clientInfo = new DefaultProxy<InformationBrokerInterface>(
				InformationBrokerInterface.class,
				InformationBrokerQueueNames.getQueryQueue(), 300);

		InformationBrokerInterface broker = clientInfo.init();
		System.out.println("found informationbroker");

		System.out.println("Database set up " + db.isComponentAlive());
	}

	@Test
	public void barChart_value() throws IllegalArgumentException,
			TimeoutException, SQLException {
		Calendar start = Calendar.getInstance();
		Calendar stop = Calendar.getInstance();
		start.setTimeInMillis(1415109600000L);
		stop.setTimeInMillis(1415113200000L);
		DataSet tester = new DataSet(start, stop, new DeviceId(
				"fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.5", "hexabus.wrapper"));
		DatabaseRequestor.fetchesDataSet(tester, db);
		ArithmeticMean mean = new ArithmeticMean();
		List<DoublePoint> bars = Comparison.barChart(tester, Calendar.MINUTE,
				2, mean);
		assertEquals(23.85, bars.get(2).getValue(), 0.000000000001);
	}

	@Test
	public void barChart_timestamp() throws IllegalArgumentException,
			TimeoutException, SQLException {
		Calendar start = Calendar.getInstance();
		Calendar stop = Calendar.getInstance();
		start.setTimeInMillis(1415109600000L);
		stop.setTimeInMillis(1415113200000L);
		DataSet tester = new DataSet(start, stop, new DeviceId(
				"fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.5", "hexabus.wrapper"));
		DatabaseRequestor.fetchesDataSet(tester, db);
		ArithmeticMean mean = new ArithmeticMean();
		List<DoublePoint> bars = Comparison.barChart(tester, Calendar.MINUTE,
				2, mean);
		assertEquals(1415109840000L, bars.get(2).getTime(), 0.0);
	}

	@Test
	public void barChart_maxAbsError() throws IllegalArgumentException,
			TimeoutException, SQLException {
		Calendar start = Calendar.getInstance();
		Calendar stop = Calendar.getInstance();
		start.setTimeInMillis(1415109600000L);
		stop.setTimeInMillis(1415113200000L);
		DataSet tester = new DataSet(start, stop, new DeviceId(
				"fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.5", "hexabus.wrapper"));
		DatabaseRequestor.fetchesDataSet(tester, db);
		ArithmeticMean mean = new ArithmeticMean();
		List<DoublePoint> bars = Comparison.barChart(tester, Calendar.MINUTE,
				2, mean);
		assertEquals(0.0, bars.get(2).getMaxAbsError(), 0.0);
	}

	@Test
	public void barChart_numElements() throws IllegalArgumentException,
			TimeoutException, SQLException {
		Calendar start = Calendar.getInstance();
		Calendar stop = Calendar.getInstance();
		start.setTimeInMillis(1415109600000L);
		stop.setTimeInMillis(1415113200000L);
		DataSet tester = new DataSet(start, stop, new DeviceId(
				"fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.5", "hexabus.wrapper"));
		DatabaseRequestor.fetchesDataSet(tester, db);
		ArithmeticMean mean = new ArithmeticMean();
		List<DoublePoint> bars = Comparison.barChart(tester, Calendar.MINUTE,
				2, mean);
		assertEquals(30, bars.size(), 0.0);
	}

	@Test
	public void barChart_invalidField() throws IllegalArgumentException,
			TimeoutException, SQLException {
		Calendar start = Calendar.getInstance();
		Calendar stop = Calendar.getInstance();
		start.setTimeInMillis(1415109600000L);
		stop.setTimeInMillis(1415113200000L);
		DataSet tester = new DataSet(start, stop, new DeviceId(
				"fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.5", "hexabus.wrapper"));
		DatabaseRequestor.fetchesDataSet(tester, db);
		ArithmeticMean mean = new ArithmeticMean();
		exception.expect(IllegalArgumentException.class);
		Comparison.barChart(tester, Calendar.DAY_OF_WEEK_IN_MONTH, 2, mean);
	}

	@Test
	public void barChart_invalidMean() throws IllegalArgumentException,
			TimeoutException, SQLException {
		Calendar start = Calendar.getInstance();
		Calendar stop = Calendar.getInstance();
		start.setTimeInMillis(1415109600000L);
		stop.setTimeInMillis(1415113200000L);
		DataSet tester = new DataSet(start, stop, new DeviceId(
				"fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.5", "hexabus.wrapper"));
		DatabaseRequestor.fetchesDataSet(tester, db);
		ArithmeticMean mean = null;
		exception.expect(IllegalArgumentException.class);
		Comparison.barChart(tester, Calendar.MINUTE, 2, mean);
	}

	@Test
	public void deviceCompareFraction_first() {
		Calendar start = Calendar.getInstance();
		Calendar stop = Calendar.getInstance();
		start.setTimeInMillis(1415109600000L);
		stop.setTimeInMillis(1415110200000L);

		List<DoublePoint> fridgeElements = new ArrayList<DoublePoint>();
		// new DoublePoint(value, maxAbsError, time)
		fridgeElements.add(new DoublePoint(10.0, 0.3, 1415109600000L));
		fridgeElements.add(new DoublePoint(14.8627, 0.3, 1415109900000L));
		fridgeElements.add(new DoublePoint(9.76, 0.3, 1415110200000L));
		DeviceId fridgeDev = new DeviceId("fridge", "test");
		DataSet fridge = new DataSet(start, stop, fridgeDev, fridgeElements);
		// 34,6227 -> 24,7305%

		List<DoublePoint> microwaveElements = new ArrayList<DoublePoint>();
		microwaveElements.add(new DoublePoint(45.0, 0.1, 1415109600000L));
		microwaveElements.add(new DoublePoint(30.875, 0.1, 1415109900000L));
		microwaveElements.add(new DoublePoint(0.0, 0.1, 1415110200000L));
		DeviceId microDev = new DeviceId("micro", "test2");
		DataSet microwave = new DataSet(start, stop, microDev,
				microwaveElements);
		// 75,875 -> 54,196428571428571428571428571429

		List<DoublePoint> totalElements = new ArrayList<DoublePoint>();
		totalElements.add(new DoublePoint(55.0, 0.0, 1415109600000L));
		totalElements.add(new DoublePoint(70.0, 0.0, 1415109900000L));
		totalElements.add(new DoublePoint(15.0, 0.0, 1415110200000L));
		DeviceId totalDev = new DeviceId("total", "totalwrapper");
		DataSet total = new DataSet(start, stop, totalDev, totalElements);
		// 140

		List<DataSet> fractions = new ArrayList<DataSet>();
		fractions.add(fridge);
		fractions.add(microwave);

		Sum mySum = new Sum();
		HashMap<DeviceId, Double> solution = Comparison.deviceCompareFraction(
				total, fractions, mySum);
		assertEquals(24.7305, solution.get(fridgeDev), 0.000000000001);
	}

	@Test
	public void deviceCompareFraction_second() {
		Calendar start = Calendar.getInstance();
		Calendar stop = Calendar.getInstance();
		start.setTimeInMillis(1415109600000L);
		stop.setTimeInMillis(1415110200000L);

		List<DoublePoint> fridgeElements = new ArrayList<DoublePoint>();
		fridgeElements.add(new DoublePoint(10.0, 0.3, 1415109600000L));
		fridgeElements.add(new DoublePoint(14.8627, 0.3, 1415109900000L));
		fridgeElements.add(new DoublePoint(9.76, 0.3, 1415110200000L));
		DeviceId fridgeDev = new DeviceId("fridge", "test");
		DataSet fridge = new DataSet(start, stop, fridgeDev, fridgeElements);
		// 34,6227 -> 24,7305%

		List<DoublePoint> microwaveElements = new ArrayList<DoublePoint>();
		microwaveElements.add(new DoublePoint(45.0, 0.1, 1415109600000L));
		microwaveElements.add(new DoublePoint(30.875, 0.1, 1415109900000L));
		microwaveElements.add(new DoublePoint(0.0, 0.1, 1415110200000L));
		DeviceId microDev = new DeviceId("micro", "test2");
		DataSet microwave = new DataSet(start, stop, microDev,
				microwaveElements);
		// 75,875 -> 54,196428571428571428571428571429%

		List<DoublePoint> totalElements = new ArrayList<DoublePoint>();
		totalElements.add(new DoublePoint(55.0, 0.0, 1415109600000L));
		totalElements.add(new DoublePoint(70.0, 0.0, 1415109900000L));
		totalElements.add(new DoublePoint(15.0, 0.0, 1415110200000L));
		DeviceId totalDev = new DeviceId("total", "totalwrapper");
		DataSet total = new DataSet(start, stop, totalDev, totalElements);
		// 140

		List<DataSet> fractions = new ArrayList<DataSet>();
		fractions.add(fridge);
		fractions.add(microwave);

		Sum mySum = new Sum();
		HashMap<DeviceId, Double> solution = Comparison.deviceCompareFraction(
				total, fractions, mySum);
		assertEquals(54.196428571428571428571428571429, solution.get(microDev),
				0.000000000001);
	}

	@Test
	public void deviceCompareAbsolute_first() {
		Calendar start = Calendar.getInstance();
		Calendar stop = Calendar.getInstance();
		start.setTimeInMillis(1415109600000L);
		stop.setTimeInMillis(1415110200000L);

		List<DoublePoint> fridgeElements = new ArrayList<DoublePoint>();
		fridgeElements.add(new DoublePoint(10.0, 0.3, 1415109600000L));
		fridgeElements.add(new DoublePoint(14.8627, 0.3, 1415109900000L));
		fridgeElements.add(new DoublePoint(9.76, 0.3, 1415110200000L));
		DeviceId fridgeDev = new DeviceId("fridge", "test");
		DataSet fridge = new DataSet(start, stop, fridgeDev, fridgeElements);
		// 34,6227 -> -105,3773

		List<DoublePoint> microwaveElements = new ArrayList<DoublePoint>();
		microwaveElements.add(new DoublePoint(45.0, 0.1, 1415109600000L));
		microwaveElements.add(new DoublePoint(30.875, 0.1, 1415109900000L));
		microwaveElements.add(new DoublePoint(0.0, 0.1, 1415110200000L));
		DeviceId microDev = new DeviceId("micro", "test2");
		DataSet microwave = new DataSet(start, stop, microDev,
				microwaveElements);
		// 75,875 -> -64,125

		List<DoublePoint> totalElements = new ArrayList<DoublePoint>();
		totalElements.add(new DoublePoint(55.0, 0.0, 1415109600000L));
		totalElements.add(new DoublePoint(70.0, 0.0, 1415109900000L));
		totalElements.add(new DoublePoint(15.0, 0.0, 1415110200000L));
		DeviceId totalDev = new DeviceId("total", "totalwrapper");
		DataSet total = new DataSet(start, stop, totalDev, totalElements);
		// 140

		List<DataSet> fractions = new ArrayList<DataSet>();
		fractions.add(fridge);
		fractions.add(microwave);

		Sum mySum = new Sum();
		HashMap<DeviceId, Double> solution = Comparison.deviceCompareAbsolute(
				total, fractions, mySum);
		assertEquals(-105.3773, solution.get(fridgeDev), 0.000000000001);
	}

	@Test
	public void deviceCompareAbsolute_second() {
		Calendar start = Calendar.getInstance();
		Calendar stop = Calendar.getInstance();
		start.setTimeInMillis(1415109600000L);
		stop.setTimeInMillis(1415110200000L);

		List<DoublePoint> fridgeElements = new ArrayList<DoublePoint>();
		fridgeElements.add(new DoublePoint(10.0, 0.3, 1415109600000L));
		fridgeElements.add(new DoublePoint(14.8627, 0.3, 1415109900000L));
		fridgeElements.add(new DoublePoint(9.76, 0.3, 1415110200000L));
		DeviceId fridgeDev = new DeviceId("fridge", "test");
		DataSet fridge = new DataSet(start, stop, fridgeDev, fridgeElements);
		// 34,6227 -> -105,3773

		List<DoublePoint> microwaveElements = new ArrayList<DoublePoint>();
		microwaveElements.add(new DoublePoint(45.0, 0.1, 1415109600000L));
		microwaveElements.add(new DoublePoint(30.875, 0.1, 1415109900000L));
		microwaveElements.add(new DoublePoint(0.0, 0.1, 1415110200000L));
		DeviceId microDev = new DeviceId("micro", "test2");
		DataSet microwave = new DataSet(start, stop, microDev,
				microwaveElements);
		// 75,875 -> -64,125

		List<DoublePoint> totalElements = new ArrayList<DoublePoint>();
		totalElements.add(new DoublePoint(55.0, 0.0, 1415109600000L));
		totalElements.add(new DoublePoint(70.0, 0.0, 1415109900000L));
		totalElements.add(new DoublePoint(15.0, 0.0, 1415110200000L));
		DeviceId totalDev = new DeviceId("total", "totalwrapper");
		DataSet total = new DataSet(start, stop, totalDev, totalElements);
		// 140

		List<DataSet> fractions = new ArrayList<DataSet>();
		fractions.add(fridge);
		fractions.add(microwave);

		Sum mySum = new Sum();
		HashMap<DeviceId, Double> solution = Comparison.deviceCompareAbsolute(
				total, fractions, mySum);
		assertEquals(-64.125, solution.get(microDev), 0.000000000001);
	}

	@Test
	public void deviceComparePercent_first() {
		Calendar start = Calendar.getInstance();
		Calendar stop = Calendar.getInstance();
		start.setTimeInMillis(1415109600000L);
		stop.setTimeInMillis(1415110200000L);

		List<DoublePoint> fridgeElements = new ArrayList<DoublePoint>();
		fridgeElements.add(new DoublePoint(10.0, 0.3, 1415109600000L));
		fridgeElements.add(new DoublePoint(14.8627, 0.3, 1415109900000L));
		fridgeElements.add(new DoublePoint(9.76, 0.3, 1415110200000L));
		DeviceId fridgeDev = new DeviceId("fridge", "test");
		DataSet fridge = new DataSet(start, stop, fridgeDev, fridgeElements);
		// 34,6227 -> -75,2695%

		List<DoublePoint> microwaveElements = new ArrayList<DoublePoint>();
		microwaveElements.add(new DoublePoint(45.0, 0.1, 1415109600000L));
		microwaveElements.add(new DoublePoint(30.875, 0.1, 1415109900000L));
		microwaveElements.add(new DoublePoint(0.0, 0.1, 1415110200000L));
		DeviceId microDev = new DeviceId("micro", "test2");
		DataSet microwave = new DataSet(start, stop, microDev,
				microwaveElements);
		// 75,875 -> -45,803571428571428571428571428571%

		List<DoublePoint> totalElements = new ArrayList<DoublePoint>();
		totalElements.add(new DoublePoint(55.0, 0.0, 1415109600000L));
		totalElements.add(new DoublePoint(70.0, 0.0, 1415109900000L));
		totalElements.add(new DoublePoint(15.0, 0.0, 1415110200000L));
		DeviceId totalDev = new DeviceId("total", "totalwrapper");
		DataSet total = new DataSet(start, stop, totalDev, totalElements);
		// 140

		List<DataSet> fractions = new ArrayList<DataSet>();
		fractions.add(fridge);
		fractions.add(microwave);

		Sum mySum = new Sum();
		HashMap<DeviceId, Double> solution = Comparison.deviceComparePercent(
				total, fractions, mySum);
		assertEquals(-75.2695, solution.get(fridgeDev), 0.000000000001);
	}

	@Test
	public void deviceComparePercent_second() {
		Calendar start = Calendar.getInstance();
		Calendar stop = Calendar.getInstance();
		start.setTimeInMillis(1415109600000L);
		stop.setTimeInMillis(1415110200000L);

		List<DoublePoint> fridgeElements = new ArrayList<DoublePoint>();
		fridgeElements.add(new DoublePoint(10.0, 0.3, 1415109600000L));
		fridgeElements.add(new DoublePoint(14.8627, 0.3, 1415109900000L));
		fridgeElements.add(new DoublePoint(9.76, 0.3, 1415110200000L));
		DeviceId fridgeDev = new DeviceId("fridge", "test");
		DataSet fridge = new DataSet(start, stop, fridgeDev, fridgeElements);
		// 34,6227 -> -75,2695%

		List<DoublePoint> microwaveElements = new ArrayList<DoublePoint>();
		microwaveElements.add(new DoublePoint(45.0, 0.1, 1415109600000L));
		microwaveElements.add(new DoublePoint(30.875, 0.1, 1415109900000L));
		microwaveElements.add(new DoublePoint(0.0, 0.1, 1415110200000L));
		DeviceId microDev = new DeviceId("micro", "test2");
		DataSet microwave = new DataSet(start, stop, microDev,
				microwaveElements);
		// 75,875 -> -45,803571428571428571428571428571%

		List<DoublePoint> totalElements = new ArrayList<DoublePoint>();
		totalElements.add(new DoublePoint(55.0, 0.0, 1415109600000L));
		totalElements.add(new DoublePoint(70.0, 0.0, 1415109900000L));
		totalElements.add(new DoublePoint(15.0, 0.0, 1415110200000L));
		DeviceId totalDev = new DeviceId("total", "totalwrapper");
		DataSet total = new DataSet(start, stop, totalDev, totalElements);
		// 140

		List<DataSet> fractions = new ArrayList<DataSet>();
		fractions.add(fridge);
		fractions.add(microwave);

		Sum mySum = new Sum();
		HashMap<DeviceId, Double> solution = Comparison.deviceComparePercent(
				total, fractions, mySum);
		assertEquals(-45.803571428571428571428571428571,
				solution.get(microDev), 0.000000000001);
	}

	@Test
	public void deviceCompare_referenceNull() {
		Calendar start = Calendar.getInstance();
		Calendar stop = Calendar.getInstance();
		start.setTimeInMillis(1415109600000L);
		stop.setTimeInMillis(1415110200000L);

		List<DoublePoint> fridgeElements = new ArrayList<DoublePoint>();
		fridgeElements.add(new DoublePoint(10.0, 0.3, 1415109600000L));
		fridgeElements.add(new DoublePoint(14.8627, 0.3, 1415109900000L));
		fridgeElements.add(new DoublePoint(9.76, 0.3, 1415110200000L));
		DeviceId fridgeDev = new DeviceId("fridge", "test");
		DataSet fridge = new DataSet(start, stop, fridgeDev, fridgeElements);

		List<DoublePoint> microwaveElements = new ArrayList<DoublePoint>();
		microwaveElements.add(new DoublePoint(45.0, 0.1, 1415109600000L));
		microwaveElements.add(new DoublePoint(30.875, 0.1, 1415109900000L));
		microwaveElements.add(new DoublePoint(0.0, 0.1, 1415110200000L));
		DeviceId microDev = new DeviceId("micro", "test2");
		DataSet microwave = new DataSet(start, stop, microDev,
				microwaveElements);

		DataSet total = null;
		List<DataSet> fractions = new ArrayList<DataSet>();
		fractions.add(fridge);
		fractions.add(microwave);

		Sum mySum = new Sum();
		exception.expect(IllegalArgumentException.class);
		Comparison.deviceCompareFraction(total, fractions, mySum);

	}

	@Test
	public void deviceCompare_referenceListNull() {
		Calendar start = Calendar.getInstance();
		Calendar stop = Calendar.getInstance();
		start.setTimeInMillis(1415109600000L);
		stop.setTimeInMillis(1415110200000L);

		List<DoublePoint> fridgeElements = new ArrayList<DoublePoint>();
		fridgeElements.add(new DoublePoint(10.0, 0.3, 1415109600000L));
		fridgeElements.add(new DoublePoint(14.8627, 0.3, 1415109900000L));
		fridgeElements.add(new DoublePoint(9.76, 0.3, 1415110200000L));
		DeviceId fridgeDev = new DeviceId("fridge", "test");
		DataSet fridge = new DataSet(start, stop, fridgeDev, fridgeElements);

		List<DoublePoint> microwaveElements = new ArrayList<DoublePoint>();
		microwaveElements.add(new DoublePoint(45.0, 0.1, 1415109600000L));
		microwaveElements.add(new DoublePoint(30.875, 0.1, 1415109900000L));
		microwaveElements.add(new DoublePoint(0.0, 0.1, 1415110200000L));
		DeviceId microDev = new DeviceId("micro", "test2");
		DataSet microwave = new DataSet(start, stop, microDev,
				microwaveElements);

		DataSet total = new DataSet(start, stop, new DeviceId("total",
				"totalwrapper"));
		List<DataSet> fractions = new ArrayList<DataSet>();
		fractions.add(fridge);
		fractions.add(microwave);

		Sum mySum = new Sum();
		exception.expect(IllegalArgumentException.class);
		Comparison.deviceCompareFraction(total, fractions, mySum);
	}

	@Test
	public void deviceCompare_referenceListEmpty() {
		Calendar start = Calendar.getInstance();
		Calendar stop = Calendar.getInstance();
		start.setTimeInMillis(1415109600000L);
		stop.setTimeInMillis(1415110200000L);

		List<DoublePoint> fridgeElements = new ArrayList<DoublePoint>();
		fridgeElements.add(new DoublePoint(10.0, 0.3, 1415109600000L));
		fridgeElements.add(new DoublePoint(14.8627, 0.3, 1415109900000L));
		fridgeElements.add(new DoublePoint(9.76, 0.3, 1415110200000L));
		DeviceId fridgeDev = new DeviceId("fridge", "test");
		DataSet fridge = new DataSet(start, stop, fridgeDev, fridgeElements);

		List<DoublePoint> microwaveElements = new ArrayList<DoublePoint>();
		microwaveElements.add(new DoublePoint(45.0, 0.1, 1415109600000L));
		microwaveElements.add(new DoublePoint(30.875, 0.1, 1415109900000L));
		microwaveElements.add(new DoublePoint(0.0, 0.1, 1415110200000L));
		DeviceId microDev = new DeviceId("micro", "test2");
		DataSet microwave = new DataSet(start, stop, microDev,
				microwaveElements);

		List<DoublePoint> totalList = new ArrayList<DoublePoint>();
		DeviceId totalDev = new DeviceId("total", "totalwrapper");
		DataSet total = new DataSet(start, stop, totalDev, totalList);
		List<DataSet> fractions = new ArrayList<DataSet>();
		fractions.add(fridge);
		fractions.add(microwave);

		Sum mySum = new Sum();
		exception.expect(IllegalArgumentException.class);
		Comparison.deviceCompareFraction(total, fractions, mySum);
	}

	@Test
	public void deviceCompare_comparerNull() {
		Calendar start = Calendar.getInstance();
		Calendar stop = Calendar.getInstance();
		start.setTimeInMillis(1415109600000L);
		stop.setTimeInMillis(1415110200000L);

		List<DoublePoint> totalElements = new ArrayList<DoublePoint>();
		totalElements.add(new DoublePoint(55.0, 0.0, 1415109600000L));
		totalElements.add(new DoublePoint(70.0, 0.0, 1415109900000L));
		totalElements.add(new DoublePoint(15.0, 0.0, 1415110200000L));
		DeviceId totalDev = new DeviceId("total", "totalwrapper");
		DataSet total = new DataSet(start, stop, totalDev, totalElements);

		List<DataSet> fractions = null;

		Sum mySum = new Sum();
		exception.expect(IllegalArgumentException.class);
		Comparison.deviceCompareFraction(total, fractions, mySum);
	}

	@Test
	public void deviceCompare_comparerEmpty() {
		Calendar start = Calendar.getInstance();
		Calendar stop = Calendar.getInstance();
		start.setTimeInMillis(1415109600000L);
		stop.setTimeInMillis(1415110200000L);

		List<DoublePoint> totalElements = new ArrayList<DoublePoint>();
		totalElements.add(new DoublePoint(55.0, 0.0, 1415109600000L));
		totalElements.add(new DoublePoint(70.0, 0.0, 1415109900000L));
		totalElements.add(new DoublePoint(15.0, 0.0, 1415110200000L));
		DeviceId totalDev = new DeviceId("total", "totalwrapper");
		DataSet total = new DataSet(start, stop, totalDev, totalElements);

		List<DataSet> fractions = new ArrayList<DataSet>();

		Sum mySum = new Sum();
		exception.expect(IllegalArgumentException.class);
		Comparison.deviceCompareFraction(total, fractions, mySum);
	}

	@Test
	public void deviceCompare_comparerOneElementNull() {
		Calendar start = Calendar.getInstance();
		Calendar stop = Calendar.getInstance();
		start.setTimeInMillis(1415109600000L);
		stop.setTimeInMillis(1415110200000L);

		List<DoublePoint> fridgeElements = new ArrayList<DoublePoint>();
		fridgeElements.add(new DoublePoint(10.0, 0.3, 1415109600000L));
		fridgeElements.add(new DoublePoint(14.8627, 0.3, 1415109900000L));
		fridgeElements.add(new DoublePoint(9.76, 0.3, 1415110200000L));
		DeviceId fridgeDev = new DeviceId("fridge", "test");
		DataSet fridge = new DataSet(start, stop, fridgeDev, fridgeElements);

		DataSet microwave = null;

		List<DoublePoint> totalElements = new ArrayList<DoublePoint>();
		totalElements.add(new DoublePoint(55.0, 0.0, 1415109600000L));
		totalElements.add(new DoublePoint(70.0, 0.0, 1415109900000L));
		totalElements.add(new DoublePoint(15.0, 0.0, 1415110200000L));
		DeviceId totalDev = new DeviceId("total", "totalwrapper");
		DataSet total = new DataSet(start, stop, totalDev, totalElements);

		List<DataSet> fractions = new ArrayList<DataSet>();
		fractions.add(fridge);
		fractions.add(microwave);

		Sum mySum = new Sum();
		HashMap<DeviceId, Double> solution = Comparison.deviceCompareFraction(
				total, fractions, mySum);
		assertNull(solution.get(new DeviceId("micro", "test2")));
	}

	@Test
	public void deviceCompare_comparerOneElementListEmpty() {
		Calendar start = Calendar.getInstance();
		Calendar stop = Calendar.getInstance();
		start.setTimeInMillis(1415109600000L);
		stop.setTimeInMillis(1415110200000L);

		List<DoublePoint> fridgeElements = new ArrayList<DoublePoint>();
		fridgeElements.add(new DoublePoint(10.0, 0.3, 1415109600000L));
		fridgeElements.add(new DoublePoint(14.8627, 0.3, 1415109900000L));
		fridgeElements.add(new DoublePoint(9.76, 0.3, 1415110200000L));
		DeviceId fridgeDev = new DeviceId("fridge", "test");
		DataSet fridge = new DataSet(start, stop, fridgeDev, fridgeElements);

		List<DoublePoint> microwaveElements = new ArrayList<DoublePoint>();
		DeviceId microDev = new DeviceId("micro", "test2");
		DataSet microwave = new DataSet(start, stop, microDev,
				microwaveElements);

		List<DoublePoint> totalElements = new ArrayList<DoublePoint>();
		totalElements.add(new DoublePoint(55.0, 0.0, 1415109600000L));
		totalElements.add(new DoublePoint(70.0, 0.0, 1415109900000L));
		totalElements.add(new DoublePoint(15.0, 0.0, 1415110200000L));
		DeviceId totalDev = new DeviceId("total", "totalwrapper");
		DataSet total = new DataSet(start, stop, totalDev, totalElements);

		List<DataSet> fractions = new ArrayList<DataSet>();
		fractions.add(fridge);
		fractions.add(microwave);

		Sum mySum = new Sum();
		HashMap<DeviceId, Double> solution = Comparison.deviceCompareFraction(
				total, fractions, mySum);
		assertNull(solution.get(new DeviceId("micro", "test2")));
	}

	@Test
	public void deviceCompare_comparerOneElementListNull() {
		Calendar start = Calendar.getInstance();
		Calendar stop = Calendar.getInstance();
		start.setTimeInMillis(1415109600000L);
		stop.setTimeInMillis(1415110200000L);

		List<DoublePoint> fridgeElements = new ArrayList<DoublePoint>();
		fridgeElements.add(new DoublePoint(10.0, 0.3, 1415109600000L));
		fridgeElements.add(new DoublePoint(14.8627, 0.3, 1415109900000L));
		fridgeElements.add(new DoublePoint(9.76, 0.3, 1415110200000L));
		DeviceId fridgeDev = new DeviceId("fridge", "test");
		DataSet fridge = new DataSet(start, stop, fridgeDev, fridgeElements);

		List<DoublePoint> microwaveElements = null;
		DeviceId microDev = new DeviceId("micro", "test2");
		DataSet microwave = new DataSet(start, stop, microDev,
				microwaveElements);

		List<DoublePoint> totalElements = new ArrayList<DoublePoint>();
		totalElements.add(new DoublePoint(55.0, 0.0, 1415109600000L));
		totalElements.add(new DoublePoint(70.0, 0.0, 1415109900000L));
		totalElements.add(new DoublePoint(15.0, 0.0, 1415110200000L));
		DeviceId totalDev = new DeviceId("total", "totalwrapper");
		DataSet total = new DataSet(start, stop, totalDev, totalElements);

		List<DataSet> fractions = new ArrayList<DataSet>();
		fractions.add(fridge);
		fractions.add(microwave);

		Sum mySum = new Sum();
		HashMap<DeviceId, Double> solution = Comparison.deviceCompareFraction(
				total, fractions, mySum);
		assertNull(solution.get(new DeviceId("micro", "test2")));
	}

	@Test
	public void timeCompareAbsolute_first() {
		Calendar startRef = Calendar.getInstance();
		Calendar stopRef = Calendar.getInstance();
		startRef.setTimeInMillis(1396656000000L); // 2014.04.05
		stopRef.setTimeInMillis(1396742400000L); // 2014.04.06
		DoublePoint refPoint1 = new DoublePoint(10.0, 0.0, 1396693800000L);
		DoublePoint refPoint2 = new DoublePoint(12.0, 0.0, 1396696500000L);
		// 11.0
		DoublePoint refPoint3 = new DoublePoint(9.0, 0.0, 1396707780000L);
		DoublePoint refPoint4 = new DoublePoint(11.0, 0.0, 1396714980000L);
		// 10.0
		List<DoublePoint> refList = new ArrayList<DoublePoint>();
		refList.add(refPoint1);
		refList.add(refPoint2);
		refList.add(refPoint3);
		refList.add(refPoint4);
		DataSet refSet = new DataSet(startRef, stopRef, new DeviceId("ref",
				"refWrapper"), refList);

		Calendar startCom1 = Calendar.getInstance();
		Calendar stopCom1 = Calendar.getInstance();
		startCom1.setTimeInMillis(1396915200000L); // 2014.04.08
		stopCom1.setTimeInMillis(1397001600000L); // 2014.04.09
		DoublePoint com1Point1 = new DoublePoint(5.0, 0.0, 1396948320000L);
		DoublePoint com1Point2 = new DoublePoint(8.0, 0.0, 1396958100000L);
		// 6.5 -> -4.5
		DoublePoint com1Point3 = new DoublePoint(9.0, 0.0, 1396963980000L);
		DoublePoint com1Point4 = new DoublePoint(8.5, 0.0, 1396977720000L);
		// 8,75 -> -1,25
		List<DoublePoint> com1List = new ArrayList<DoublePoint>();
		com1List.add(com1Point1);
		com1List.add(com1Point2);
		com1List.add(com1Point3);
		com1List.add(com1Point4);
		DataSet com1Set = new DataSet(startCom1, stopCom1, new DeviceId("temp",
				"test"), com1List);

		Calendar startCom2 = Calendar.getInstance();
		Calendar stopCom2 = Calendar.getInstance();
		startCom2.setTimeInMillis(1397260800000L); // 2014.04.12
		stopCom2.setTimeInMillis(1397347200000L); // 2014.04.13
		DoublePoint com2Point1 = new DoublePoint(15.0, 0.0, 1397283180000L);
		DoublePoint com2Point2 = new DoublePoint(18.0, 0.0, 1397303100000L);
		// 16.5 -> 5.5
		DoublePoint com2Point3 = new DoublePoint(13.0, 0.0, 1397309053000L);
		DoublePoint com2Point4 = new DoublePoint(20.0, 0.0, 1397315156000L);
		// 16.5 -> 6.5
		List<DoublePoint> com2List = new ArrayList<DoublePoint>();
		com2List.add(com2Point1);
		com2List.add(com2Point2);
		com2List.add(com2Point3);
		com2List.add(com2Point4);
		DataSet com2Set = new DataSet(startCom2, stopCom2, new DeviceId(
				"temp2", "testa"), com2List);

		List<DataSet> comSet = new ArrayList<DataSet>();
		comSet.add(com1Set);
		comSet.add(com2Set);
		ArithmeticMean mean = new ArithmeticMean();

		List<DataSet> outcome = Comparison.timeCompareAbsolute(refSet, comSet,
				Calendar.HOUR_OF_DAY, 12, mean);
		assertEquals(-1.25, outcome.get(0).getDataList().get(1).getValue(), 0.0);
	}

	@Test
	public void timeCompareAbsolute_second() {
		Calendar startRef = Calendar.getInstance();
		Calendar stopRef = Calendar.getInstance();
		startRef.setTimeInMillis(1396656000000L); // 2014.04.05
		stopRef.setTimeInMillis(1396742400000L); // 2014.04.06
		DoublePoint refPoint1 = new DoublePoint(10.0, 0.0, 1396693800000L);
		DoublePoint refPoint2 = new DoublePoint(12.0, 0.0, 1396696500000L);
		// 11.0
		DoublePoint refPoint3 = new DoublePoint(9.0, 0.0, 1396707780000L);
		DoublePoint refPoint4 = new DoublePoint(11.0, 0.0, 1396714980000L);
		// 10.0
		List<DoublePoint> refList = new ArrayList<DoublePoint>();
		refList.add(refPoint1);
		refList.add(refPoint2);
		refList.add(refPoint3);
		refList.add(refPoint4);
		DataSet refSet = new DataSet(startRef, stopRef, new DeviceId("ref",
				"refWrapper"), refList);

		Calendar startCom1 = Calendar.getInstance();
		Calendar stopCom1 = Calendar.getInstance();
		startCom1.setTimeInMillis(1396915200000L); // 2014.04.08
		stopCom1.setTimeInMillis(1397001600000L); // 2014.04.09
		DoublePoint com1Point1 = new DoublePoint(5.0, 0.0, 1396948320000L);
		DoublePoint com1Point2 = new DoublePoint(8.0, 0.0, 1396958100000L);
		// 6.5 -> -4.5
		DoublePoint com1Point3 = new DoublePoint(9.0, 0.0, 1396963980000L);
		DoublePoint com1Point4 = new DoublePoint(8.5, 0.0, 1396977720000L);
		// 8,75 -> -1,25
		List<DoublePoint> com1List = new ArrayList<DoublePoint>();
		com1List.add(com1Point1);
		com1List.add(com1Point2);
		com1List.add(com1Point3);
		com1List.add(com1Point4);
		DataSet com1Set = new DataSet(startCom1, stopCom1, new DeviceId("temp",
				"test"), com1List);

		Calendar startCom2 = Calendar.getInstance();
		Calendar stopCom2 = Calendar.getInstance();
		startCom2.setTimeInMillis(1397260800000L); // 2014.04.12
		stopCom2.setTimeInMillis(1397347200000L); // 2014.04.13
		DoublePoint com2Point1 = new DoublePoint(15.0, 0.0, 1397283180000L);
		DoublePoint com2Point2 = new DoublePoint(18.0, 0.0, 1397303100000L);
		// 16.5 -> 5.5
		DoublePoint com2Point3 = new DoublePoint(13.0, 0.0, 1397309053000L);
		DoublePoint com2Point4 = new DoublePoint(20.0, 0.0, 1397315156000L);
		// 16.5 -> 6.5
		List<DoublePoint> com2List = new ArrayList<DoublePoint>();
		com2List.add(com2Point1);
		com2List.add(com2Point2);
		com2List.add(com2Point3);
		com2List.add(com2Point4);
		DataSet com2Set = new DataSet(startCom2, stopCom2, new DeviceId(
				"temp2", "testa"), com2List);

		List<DataSet> comSet = new ArrayList<DataSet>();
		comSet.add(com1Set);
		comSet.add(com2Set);
		ArithmeticMean mean = new ArithmeticMean();

		List<DataSet> outcome = Comparison.timeCompareAbsolute(refSet, comSet,
				Calendar.HOUR_OF_DAY, 12, mean);
		assertEquals(5.5, outcome.get(1).getDataList().get(0).getValue(), 0.0);
	}

	@Test
	public void timeComparePercent_first() {
		Calendar startRef = Calendar.getInstance();
		Calendar stopRef = Calendar.getInstance();
		startRef.setTimeInMillis(1396656000000L); // 2014.04.05
		stopRef.setTimeInMillis(1396742400000L); // 2014.04.06
		DoublePoint refPoint1 = new DoublePoint(10.0, 0.0, 1396693800000L);
		DoublePoint refPoint2 = new DoublePoint(12.0, 0.0, 1396696500000L);
		// 11.0
		DoublePoint refPoint3 = new DoublePoint(9.0, 0.0, 1396707780000L);
		DoublePoint refPoint4 = new DoublePoint(11.0, 0.0, 1396714980000L);
		// 10.0
		List<DoublePoint> refList = new ArrayList<DoublePoint>();
		refList.add(refPoint1);
		refList.add(refPoint2);
		refList.add(refPoint3);
		refList.add(refPoint4);
		DataSet refSet = new DataSet(startRef, stopRef, new DeviceId("ref",
				"refWrapper"), refList);

		Calendar startCom1 = Calendar.getInstance();
		Calendar stopCom1 = Calendar.getInstance();
		startCom1.setTimeInMillis(1396915200000L); // 2014.04.08
		stopCom1.setTimeInMillis(1397001600000L); // 2014.04.09
		DoublePoint com1Point1 = new DoublePoint(5.0, 0.0, 1396948320000L);
		DoublePoint com1Point2 = new DoublePoint(8.0, 0.0, 1396958100000L);
		// 6.5 -> -4.5
		DoublePoint com1Point3 = new DoublePoint(9.0, 0.0, 1396963980000L);
		DoublePoint com1Point4 = new DoublePoint(8.5, 0.0, 1396977720000L);
		// 8,75 -> -1,25
		List<DoublePoint> com1List = new ArrayList<DoublePoint>();
		com1List.add(com1Point1);
		com1List.add(com1Point2);
		com1List.add(com1Point3);
		com1List.add(com1Point4);
		DataSet com1Set = new DataSet(startCom1, stopCom1, new DeviceId("temp",
				"test"), com1List);

		Calendar startCom2 = Calendar.getInstance();
		Calendar stopCom2 = Calendar.getInstance();
		startCom2.setTimeInMillis(1397260800000L); // 2014.04.12
		stopCom2.setTimeInMillis(1397347200000L); // 2014.04.13
		DoublePoint com2Point1 = new DoublePoint(15.0, 0.0, 1397283180000L);
		DoublePoint com2Point2 = new DoublePoint(18.0, 0.0, 1397303100000L);
		// 16.5 -> 5.5
		DoublePoint com2Point3 = new DoublePoint(13.0, 0.0, 1397309053000L);
		DoublePoint com2Point4 = new DoublePoint(20.0, 0.0, 1397315156000L);
		// 16.5 -> 6.5
		List<DoublePoint> com2List = new ArrayList<DoublePoint>();
		com2List.add(com2Point1);
		com2List.add(com2Point2);
		com2List.add(com2Point3);
		com2List.add(com2Point4);
		DataSet com2Set = new DataSet(startCom2, stopCom2, new DeviceId(
				"temp2", "testa"), com2List);

		List<DataSet> comSet = new ArrayList<DataSet>();
		comSet.add(com1Set);
		comSet.add(com2Set);
		ArithmeticMean mean = new ArithmeticMean();

		List<DataSet> outcome = Comparison.timeComparePercent(refSet, comSet,
				Calendar.HOUR_OF_DAY, 12, mean);
		assertEquals(-40.909090909090909090909090909091, outcome.get(0)
				.getDataList().get(0).getValue(), 0.000000000001);
	}

	@Test
	public void timeComparePercent_second() {
		Calendar startRef = Calendar.getInstance();
		Calendar stopRef = Calendar.getInstance();
		startRef.setTimeInMillis(1396656000000L); // 2014.04.05
		stopRef.setTimeInMillis(1396742400000L); // 2014.04.06
		DoublePoint refPoint1 = new DoublePoint(10.0, 0.0, 1396693800000L);
		DoublePoint refPoint2 = new DoublePoint(12.0, 0.0, 1396696500000L);
		// 11.0
		DoublePoint refPoint3 = new DoublePoint(9.0, 0.0, 1396707780000L);
		DoublePoint refPoint4 = new DoublePoint(11.0, 0.0, 1396714980000L);
		// 10.0
		List<DoublePoint> refList = new ArrayList<DoublePoint>();
		refList.add(refPoint1);
		refList.add(refPoint2);
		refList.add(refPoint3);
		refList.add(refPoint4);
		DataSet refSet = new DataSet(startRef, stopRef, new DeviceId("ref",
				"refWrapper"), refList);

		Calendar startCom1 = Calendar.getInstance();
		Calendar stopCom1 = Calendar.getInstance();
		startCom1.setTimeInMillis(1396915200000L); // 2014.04.08
		stopCom1.setTimeInMillis(1397001600000L); // 2014.04.09
		DoublePoint com1Point1 = new DoublePoint(5.0, 0.0, 1396948320000L);
		DoublePoint com1Point2 = new DoublePoint(8.0, 0.0, 1396958100000L);
		// 6.5 -> -4.5
		DoublePoint com1Point3 = new DoublePoint(9.0, 0.0, 1396963980000L);
		DoublePoint com1Point4 = new DoublePoint(8.5, 0.0, 1396977720000L);
		// 8,75 -> -1,25
		List<DoublePoint> com1List = new ArrayList<DoublePoint>();
		com1List.add(com1Point1);
		com1List.add(com1Point2);
		com1List.add(com1Point3);
		com1List.add(com1Point4);
		DataSet com1Set = new DataSet(startCom1, stopCom1, new DeviceId("temp",
				"test"), com1List);

		Calendar startCom2 = Calendar.getInstance();
		Calendar stopCom2 = Calendar.getInstance();
		startCom2.setTimeInMillis(1397260800000L); // 2014.04.12
		stopCom2.setTimeInMillis(1397347200000L); // 2014.04.13
		DoublePoint com2Point1 = new DoublePoint(15.0, 0.0, 1397283180000L);
		DoublePoint com2Point2 = new DoublePoint(18.0, 0.0, 1397303100000L);
		// 16.5 -> 5.5
		DoublePoint com2Point3 = new DoublePoint(13.0, 0.0, 1397309053000L);
		DoublePoint com2Point4 = new DoublePoint(20.0, 0.0, 1397315156000L);
		// 16.5 -> 6.5
		List<DoublePoint> com2List = new ArrayList<DoublePoint>();
		com2List.add(com2Point1);
		com2List.add(com2Point2);
		com2List.add(com2Point3);
		com2List.add(com2Point4);
		DataSet com2Set = new DataSet(startCom2, stopCom2, new DeviceId(
				"temp2", "testa"), com2List);

		List<DataSet> comSet = new ArrayList<DataSet>();
		comSet.add(com1Set);
		comSet.add(com2Set);
		ArithmeticMean mean = new ArithmeticMean();

		List<DataSet> outcome = Comparison.timeComparePercent(refSet, comSet,
				Calendar.HOUR_OF_DAY, 12, mean);
		assertEquals(65.0, outcome.get(1).getDataList().get(1).getValue(),
				0.000000000001);
	}

	@Test
	public void timeCompare_referenceNull() {
		DataSet refSet = null;

		Calendar startCom1 = Calendar.getInstance();
		Calendar stopCom1 = Calendar.getInstance();
		startCom1.setTimeInMillis(1396915200000L); // 2014.04.08
		stopCom1.setTimeInMillis(1397001600000L); // 2014.04.09
		DoublePoint com1Point1 = new DoublePoint(5.0, 0.0, 1396948320000L);
		DoublePoint com1Point2 = new DoublePoint(8.0, 0.0, 1396958100000L);
		// 6.5 -> -4.5
		DoublePoint com1Point3 = new DoublePoint(9.0, 0.0, 1396963980000L);
		DoublePoint com1Point4 = new DoublePoint(8.5, 0.0, 1396977720000L);
		// 8,75 -> -1,25
		List<DoublePoint> com1List = new ArrayList<DoublePoint>();
		com1List.add(com1Point1);
		com1List.add(com1Point2);
		com1List.add(com1Point3);
		com1List.add(com1Point4);
		DataSet com1Set = new DataSet(startCom1, stopCom1, new DeviceId("temp",
				"test"), com1List);

		Calendar startCom2 = Calendar.getInstance();
		Calendar stopCom2 = Calendar.getInstance();
		startCom2.setTimeInMillis(1397260800000L); // 2014.04.12
		stopCom2.setTimeInMillis(1397347200000L); // 2014.04.13
		DoublePoint com2Point1 = new DoublePoint(15.0, 0.0, 1397283180000L);
		DoublePoint com2Point2 = new DoublePoint(18.0, 0.0, 1397303100000L);
		// 16.5 -> 5.5
		DoublePoint com2Point3 = new DoublePoint(13.0, 0.0, 1397309053000L);
		DoublePoint com2Point4 = new DoublePoint(20.0, 0.0, 1397315156000L);
		// 16.5 -> 6.5
		List<DoublePoint> com2List = new ArrayList<DoublePoint>();
		com2List.add(com2Point1);
		com2List.add(com2Point2);
		com2List.add(com2Point3);
		com2List.add(com2Point4);
		DataSet com2Set = new DataSet(startCom2, stopCom2, new DeviceId(
				"temp2", "testa"), com2List);

		List<DataSet> comSet = new ArrayList<DataSet>();
		comSet.add(com1Set);
		comSet.add(com2Set);
		ArithmeticMean mean = new ArithmeticMean();

		exception.expect(IllegalArgumentException.class);
		Comparison.timeComparePercent(refSet, comSet, Calendar.HOUR_OF_DAY, 12,
				mean);
	}

	@Test
	public void timeCompare_referenceListNull() {
		Calendar startRef = Calendar.getInstance();
		Calendar stopRef = Calendar.getInstance();
		startRef.setTimeInMillis(1396656000000L); // 2014.04.05
		stopRef.setTimeInMillis(1396742400000L); // 2014.04.06
		DataSet refSet = new DataSet(startRef, stopRef, new DeviceId("ref",
				"refWrapper"));

		Calendar startCom1 = Calendar.getInstance();
		Calendar stopCom1 = Calendar.getInstance();
		startCom1.setTimeInMillis(1396915200000L); // 2014.04.08
		stopCom1.setTimeInMillis(1397001600000L); // 2014.04.09
		DoublePoint com1Point1 = new DoublePoint(5.0, 0.0, 1396948320000L);
		DoublePoint com1Point2 = new DoublePoint(8.0, 0.0, 1396958100000L);
		// 6.5 -> -4.5
		DoublePoint com1Point3 = new DoublePoint(9.0, 0.0, 1396963980000L);
		DoublePoint com1Point4 = new DoublePoint(8.5, 0.0, 1396977720000L);
		// 8,75 -> -1,25
		List<DoublePoint> com1List = new ArrayList<DoublePoint>();
		com1List.add(com1Point1);
		com1List.add(com1Point2);
		com1List.add(com1Point3);
		com1List.add(com1Point4);
		DataSet com1Set = new DataSet(startCom1, stopCom1, new DeviceId("temp",
				"test"), com1List);

		Calendar startCom2 = Calendar.getInstance();
		Calendar stopCom2 = Calendar.getInstance();
		startCom2.setTimeInMillis(1397260800000L); // 2014.04.12
		stopCom2.setTimeInMillis(1397347200000L); // 2014.04.13
		DoublePoint com2Point1 = new DoublePoint(15.0, 0.0, 1397283180000L);
		DoublePoint com2Point2 = new DoublePoint(18.0, 0.0, 1397303100000L);
		// 16.5 -> 5.5
		DoublePoint com2Point3 = new DoublePoint(13.0, 0.0, 1397309053000L);
		DoublePoint com2Point4 = new DoublePoint(20.0, 0.0, 1397315156000L);
		// 16.5 -> 6.5
		List<DoublePoint> com2List = new ArrayList<DoublePoint>();
		com2List.add(com2Point1);
		com2List.add(com2Point2);
		com2List.add(com2Point3);
		com2List.add(com2Point4);
		DataSet com2Set = new DataSet(startCom2, stopCom2, new DeviceId(
				"temp2", "testa"), com2List);

		List<DataSet> comSet = new ArrayList<DataSet>();
		comSet.add(com1Set);
		comSet.add(com2Set);
		ArithmeticMean mean = new ArithmeticMean();

		exception.expect(IllegalArgumentException.class);
		Comparison.timeComparePercent(refSet, comSet, Calendar.HOUR_OF_DAY, 12,
				mean);
	}

	@Test
	public void timeCompare_referenceListEmpty() {
		Calendar startRef = Calendar.getInstance();
		Calendar stopRef = Calendar.getInstance();
		startRef.setTimeInMillis(1396656000000L); // 2014.04.05
		stopRef.setTimeInMillis(1396742400000L); // 2014.04.06
		List<DoublePoint> refList = new ArrayList<DoublePoint>();
		DataSet refSet = new DataSet(startRef, stopRef, new DeviceId("ref",
				"refWrapper"), refList);

		Calendar startCom1 = Calendar.getInstance();
		Calendar stopCom1 = Calendar.getInstance();
		startCom1.setTimeInMillis(1396915200000L); // 2014.04.08
		stopCom1.setTimeInMillis(1397001600000L); // 2014.04.09
		DoublePoint com1Point1 = new DoublePoint(5.0, 0.0, 1396948320000L);
		DoublePoint com1Point2 = new DoublePoint(8.0, 0.0, 1396958100000L);
		// 6.5 -> -4.5
		DoublePoint com1Point3 = new DoublePoint(9.0, 0.0, 1396963980000L);
		DoublePoint com1Point4 = new DoublePoint(8.5, 0.0, 1396977720000L);
		// 8,75 -> -1,25
		List<DoublePoint> com1List = new ArrayList<DoublePoint>();
		com1List.add(com1Point1);
		com1List.add(com1Point2);
		com1List.add(com1Point3);
		com1List.add(com1Point4);
		DataSet com1Set = new DataSet(startCom1, stopCom1, new DeviceId("temp",
				"test"), com1List);

		Calendar startCom2 = Calendar.getInstance();
		Calendar stopCom2 = Calendar.getInstance();
		startCom2.setTimeInMillis(1397260800000L); // 2014.04.12
		stopCom2.setTimeInMillis(1397347200000L); // 2014.04.13
		DoublePoint com2Point1 = new DoublePoint(15.0, 0.0, 1397283180000L);
		DoublePoint com2Point2 = new DoublePoint(18.0, 0.0, 1397303100000L);
		// 16.5 -> 5.5
		DoublePoint com2Point3 = new DoublePoint(13.0, 0.0, 1397309053000L);
		DoublePoint com2Point4 = new DoublePoint(20.0, 0.0, 1397315156000L);
		// 16.5 -> 6.5
		List<DoublePoint> com2List = new ArrayList<DoublePoint>();
		com2List.add(com2Point1);
		com2List.add(com2Point2);
		com2List.add(com2Point3);
		com2List.add(com2Point4);
		DataSet com2Set = new DataSet(startCom2, stopCom2, new DeviceId(
				"temp2", "testa"), com2List);

		List<DataSet> comSet = new ArrayList<DataSet>();
		comSet.add(com1Set);
		comSet.add(com2Set);
		ArithmeticMean mean = new ArithmeticMean();

		exception.expect(IllegalArgumentException.class);
		Comparison.timeComparePercent(refSet, comSet, Calendar.HOUR_OF_DAY, 12,
				mean);
	}

	@Test
	public void timeCompare_comparerNull() {
		Calendar startRef = Calendar.getInstance();
		Calendar stopRef = Calendar.getInstance();
		startRef.setTimeInMillis(1396656000000L); // 2014.04.05
		stopRef.setTimeInMillis(1396742400000L); // 2014.04.06
		DoublePoint refPoint1 = new DoublePoint(10.0, 0.0, 1396693800000L);
		DoublePoint refPoint2 = new DoublePoint(12.0, 0.0, 1396696500000L);
		// 11.0
		DoublePoint refPoint3 = new DoublePoint(9.0, 0.0, 1396707780000L);
		DoublePoint refPoint4 = new DoublePoint(11.0, 0.0, 1396714980000L);
		// 10.0
		List<DoublePoint> refList = new ArrayList<DoublePoint>();
		refList.add(refPoint1);
		refList.add(refPoint2);
		refList.add(refPoint3);
		refList.add(refPoint4);
		DataSet refSet = new DataSet(startRef, stopRef, new DeviceId("ref",
				"refWrapper"), refList);

		List<DataSet> comSet = null;
		ArithmeticMean mean = new ArithmeticMean();

		exception.expect(IllegalArgumentException.class);
		Comparison.timeComparePercent(refSet, comSet, Calendar.HOUR_OF_DAY, 12,
				mean);
	}

	@Test
	public void timeCompare_comparerEmpty() {
		Calendar startRef = Calendar.getInstance();
		Calendar stopRef = Calendar.getInstance();
		startRef.setTimeInMillis(1396656000000L); // 2014.04.05
		stopRef.setTimeInMillis(1396742400000L); // 2014.04.06
		DoublePoint refPoint1 = new DoublePoint(10.0, 0.0, 1396693800000L);
		DoublePoint refPoint2 = new DoublePoint(12.0, 0.0, 1396696500000L);
		// 11.0
		DoublePoint refPoint3 = new DoublePoint(9.0, 0.0, 1396707780000L);
		DoublePoint refPoint4 = new DoublePoint(11.0, 0.0, 1396714980000L);
		// 10.0
		List<DoublePoint> refList = new ArrayList<DoublePoint>();
		refList.add(refPoint1);
		refList.add(refPoint2);
		refList.add(refPoint3);
		refList.add(refPoint4);
		DataSet refSet = new DataSet(startRef, stopRef, new DeviceId("ref",
				"refWrapper"), refList);

		List<DataSet> comSet = new ArrayList<DataSet>();
		ArithmeticMean mean = new ArithmeticMean();

		exception.expect(IllegalArgumentException.class);
		Comparison.timeComparePercent(refSet, comSet, Calendar.HOUR_OF_DAY, 12,
				mean);
	}

	@Test
	public void timeCompare_CalculationNull() {
		Calendar startRef = Calendar.getInstance();
		Calendar stopRef = Calendar.getInstance();
		startRef.setTimeInMillis(1396656000000L); // 2014.04.05
		stopRef.setTimeInMillis(1396742400000L); // 2014.04.06
		DoublePoint refPoint1 = new DoublePoint(10.0, 0.0, 1396693800000L);
		DoublePoint refPoint2 = new DoublePoint(12.0, 0.0, 1396696500000L);
		// 11.0
		DoublePoint refPoint3 = new DoublePoint(9.0, 0.0, 1396707780000L);
		DoublePoint refPoint4 = new DoublePoint(11.0, 0.0, 1396714980000L);
		// 10.0
		List<DoublePoint> refList = new ArrayList<DoublePoint>();
		refList.add(refPoint1);
		refList.add(refPoint2);
		refList.add(refPoint3);
		refList.add(refPoint4);
		DataSet refSet = new DataSet(startRef, stopRef, new DeviceId("ref",
				"refWrapper"), refList);

		Calendar startCom1 = Calendar.getInstance();
		Calendar stopCom1 = Calendar.getInstance();
		startCom1.setTimeInMillis(1396915200000L); // 2014.04.08
		stopCom1.setTimeInMillis(1397001600000L); // 2014.04.09
		DoublePoint com1Point1 = new DoublePoint(5.0, 0.0, 1396948320000L);
		DoublePoint com1Point2 = new DoublePoint(8.0, 0.0, 1396958100000L);
		// 6.5 -> -4.5
		DoublePoint com1Point3 = new DoublePoint(9.0, 0.0, 1396963980000L);
		DoublePoint com1Point4 = new DoublePoint(8.5, 0.0, 1396977720000L);
		// 8,75 -> -1,25
		List<DoublePoint> com1List = new ArrayList<DoublePoint>();
		com1List.add(com1Point1);
		com1List.add(com1Point2);
		com1List.add(com1Point3);
		com1List.add(com1Point4);
		DataSet com1Set = new DataSet(startCom1, stopCom1, new DeviceId("temp",
				"test"), com1List);

		Calendar startCom2 = Calendar.getInstance();
		Calendar stopCom2 = Calendar.getInstance();
		startCom2.setTimeInMillis(1397260800000L); // 2014.04.12
		stopCom2.setTimeInMillis(1397347200000L); // 2014.04.13
		DoublePoint com2Point1 = new DoublePoint(15.0, 0.0, 1397283180000L);
		DoublePoint com2Point2 = new DoublePoint(18.0, 0.0, 1397303100000L);
		// 16.5 -> 5.5
		DoublePoint com2Point3 = new DoublePoint(13.0, 0.0, 1397309053000L);
		DoublePoint com2Point4 = new DoublePoint(20.0, 0.0, 1397315156000L);
		// 16.5 -> 6.5
		List<DoublePoint> com2List = new ArrayList<DoublePoint>();
		com2List.add(com2Point1);
		com2List.add(com2Point2);
		com2List.add(com2Point3);
		com2List.add(com2Point4);
		DataSet com2Set = new DataSet(startCom2, stopCom2, new DeviceId(
				"temp2", "testa"), com2List);

		List<DataSet> comSet = new ArrayList<DataSet>();
		comSet.add(com1Set);
		comSet.add(com2Set);
		ArithmeticMean mean = null;

		exception.expect(IllegalArgumentException.class);
		Comparison.timeComparePercent(refSet, comSet, Calendar.HOUR_OF_DAY, 12,
				mean);
	}

	@Test
	public void timeCompare_referenceOneElementNull() {
		Calendar startRef = Calendar.getInstance();
		Calendar stopRef = Calendar.getInstance();
		startRef.setTimeInMillis(1396656000000L); // 2014.04.05
		stopRef.setTimeInMillis(1396742400000L); // 2014.04.06

		DoublePoint refPoint3 = new DoublePoint(9.0, 0.0, 1396707780000L);
		DoublePoint refPoint4 = new DoublePoint(11.0, 0.0, 1396714980000L);
		// 10.0
		List<DoublePoint> refList = new ArrayList<DoublePoint>();
		refList.add(refPoint3);
		refList.add(refPoint4);
		DataSet refSet = new DataSet(startRef, stopRef, new DeviceId("ref",
				"refWrapper"), refList);

		Calendar startCom1 = Calendar.getInstance();
		Calendar stopCom1 = Calendar.getInstance();
		startCom1.setTimeInMillis(1396915200000L); // 2014.04.08
		stopCom1.setTimeInMillis(1397001600000L); // 2014.04.09
		DoublePoint com1Point1 = new DoublePoint(5.0, 0.0, 1396948320000L);
		DoublePoint com1Point2 = new DoublePoint(8.0, 0.0, 1396958100000L);
		// 6.5 -> -4.5
		DoublePoint com1Point3 = new DoublePoint(9.0, 0.0, 1396963980000L);
		DoublePoint com1Point4 = new DoublePoint(8.5, 0.0, 1396977720000L);
		// 8,75 -> -1,25
		List<DoublePoint> com1List = new ArrayList<DoublePoint>();
		com1List.add(com1Point1);
		com1List.add(com1Point2);
		com1List.add(com1Point3);
		com1List.add(com1Point4);
		DataSet com1Set = new DataSet(startCom1, stopCom1, new DeviceId("temp",
				"test"), com1List);

		Calendar startCom2 = Calendar.getInstance();
		Calendar stopCom2 = Calendar.getInstance();
		startCom2.setTimeInMillis(1397260800000L); // 2014.04.12
		stopCom2.setTimeInMillis(1397347200000L); // 2014.04.13
		DoublePoint com2Point1 = new DoublePoint(15.0, 0.0, 1397283180000L);
		DoublePoint com2Point2 = new DoublePoint(18.0, 0.0, 1397303100000L);
		// 16.5 -> 5.5
		DoublePoint com2Point3 = new DoublePoint(13.0, 0.0, 1397309053000L);
		DoublePoint com2Point4 = new DoublePoint(20.0, 0.0, 1397315156000L);
		// 16.5 -> 6.5
		List<DoublePoint> com2List = new ArrayList<DoublePoint>();
		com2List.add(com2Point1);
		com2List.add(com2Point2);
		com2List.add(com2Point3);
		com2List.add(com2Point4);
		DataSet com2Set = new DataSet(startCom2, stopCom2, new DeviceId(
				"temp2", "testa"), com2List);
		List<DataSet> comSet = new ArrayList<DataSet>();
		comSet.add(com1Set);
		comSet.add(com2Set);
		ArithmeticMean mean = new ArithmeticMean();

		List<DataSet> outcome = Comparison.timeComparePercent(refSet, comSet,
				Calendar.HOUR_OF_DAY, 12, mean);
		assertNull(outcome.get(0).getDataList().get(0).getValue());
	}

	@Test
	public void timeCompare_comparerOneCompNull() {
		Calendar startRef = Calendar.getInstance();
		Calendar stopRef = Calendar.getInstance();
		startRef.setTimeInMillis(1396656000000L); // 2014.04.05
		stopRef.setTimeInMillis(1396742400000L); // 2014.04.06
		DoublePoint refPoint1 = new DoublePoint(10.0, 0.0, 1396693800000L);
		DoublePoint refPoint2 = new DoublePoint(12.0, 0.0, 1396696500000L);
		// 11.0
		DoublePoint refPoint3 = new DoublePoint(9.0, 0.0, 1396707780000L);
		DoublePoint refPoint4 = new DoublePoint(11.0, 0.0, 1396714980000L);
		// 10.0
		List<DoublePoint> refList = new ArrayList<DoublePoint>();
		refList.add(refPoint1);
		refList.add(refPoint2);
		refList.add(refPoint3);
		refList.add(refPoint4);
		DataSet refSet = new DataSet(startRef, stopRef, new DeviceId("ref",
				"refWrapper"), refList);

		Calendar startCom1 = Calendar.getInstance();
		Calendar stopCom1 = Calendar.getInstance();
		startCom1.setTimeInMillis(1396915200000L); // 2014.04.08
		stopCom1.setTimeInMillis(1397001600000L); // 2014.04.09
		DoublePoint com1Point1 = new DoublePoint(5.0, 0.0, 1396948320000L);
		DoublePoint com1Point2 = new DoublePoint(8.0, 0.0, 1396958100000L);
		// 6.5 -> -4.5
		DoublePoint com1Point3 = new DoublePoint(9.0, 0.0, 1396963980000L);
		DoublePoint com1Point4 = new DoublePoint(8.5, 0.0, 1396977720000L);
		// 8,75 -> -1,25
		List<DoublePoint> com1List = new ArrayList<DoublePoint>();
		com1List.add(com1Point1);
		com1List.add(com1Point2);
		com1List.add(com1Point3);
		com1List.add(com1Point4);
		DataSet com1Set = new DataSet(startCom1, stopCom1, new DeviceId("temp",
				"test"), com1List);

		List<DataSet> comSet = new ArrayList<DataSet>();
		comSet.add(com1Set);
		comSet.add(null);
		ArithmeticMean mean = new ArithmeticMean();

		List<DataSet> outcome = Comparison.timeComparePercent(refSet, comSet,
				Calendar.HOUR_OF_DAY, 12, mean);
		assertNull(outcome.get(1));
	}

	@Test
	public void timeCompare_comparerOneCompListNull() {
		Calendar startRef = Calendar.getInstance();
		Calendar stopRef = Calendar.getInstance();
		startRef.setTimeInMillis(1396656000000L); // 2014.04.05
		stopRef.setTimeInMillis(1396742400000L); // 2014.04.06
		DoublePoint refPoint1 = new DoublePoint(10.0, 0.0, 1396693800000L);
		DoublePoint refPoint2 = new DoublePoint(12.0, 0.0, 1396696500000L);
		// 11.0
		DoublePoint refPoint3 = new DoublePoint(9.0, 0.0, 1396707780000L);
		DoublePoint refPoint4 = new DoublePoint(11.0, 0.0, 1396714980000L);
		// 10.0
		List<DoublePoint> refList = new ArrayList<DoublePoint>();
		refList.add(refPoint1);
		refList.add(refPoint2);
		refList.add(refPoint3);
		refList.add(refPoint4);
		DataSet refSet = new DataSet(startRef, stopRef, new DeviceId("ref",
				"refWrapper"), refList);

		Calendar startCom1 = Calendar.getInstance();
		Calendar stopCom1 = Calendar.getInstance();
		startCom1.setTimeInMillis(1396915200000L); // 2014.04.08
		stopCom1.setTimeInMillis(1397001600000L); // 2014.04.09
		List<DoublePoint> com1List = null;
		DataSet com1Set = new DataSet(startCom1, stopCom1, new DeviceId("temp",
				"test"), com1List);

		Calendar startCom2 = Calendar.getInstance();
		Calendar stopCom2 = Calendar.getInstance();
		startCom2.setTimeInMillis(1397260800000L); // 2014.04.12
		stopCom2.setTimeInMillis(1397347200000L); // 2014.04.13
		DoublePoint com2Point1 = new DoublePoint(15.0, 0.0, 1397283180000L);
		DoublePoint com2Point2 = new DoublePoint(18.0, 0.0, 1397303100000L);
		// 16.5 -> 5.5
		DoublePoint com2Point3 = new DoublePoint(13.0, 0.0, 1397309053000L);
		DoublePoint com2Point4 = new DoublePoint(20.0, 0.0, 1397315156000L);
		// 16.5 -> 6.5
		List<DoublePoint> com2List = new ArrayList<DoublePoint>();
		com2List.add(com2Point1);
		com2List.add(com2Point2);
		com2List.add(com2Point3);
		com2List.add(com2Point4);
		DataSet com2Set = new DataSet(startCom2, stopCom2, new DeviceId(
				"temp2", "testa"), com2List);

		List<DataSet> comSet = new ArrayList<DataSet>();
		comSet.add(com1Set);
		comSet.add(com2Set);
		ArithmeticMean mean = new ArithmeticMean();

		List<DataSet>expectedList = Comparison.timeComparePercent(refSet, comSet, Calendar.HOUR_OF_DAY, 12,
				mean);
		assertNull(expectedList.get(0));
	}

	@Test
	public void timeCompare_comparerOneCompListEmpty() {
		Calendar startRef = Calendar.getInstance();
		Calendar stopRef = Calendar.getInstance();
		startRef.setTimeInMillis(1396656000000L); // 2014.04.05
		stopRef.setTimeInMillis(1396742400000L); // 2014.04.06
		DoublePoint refPoint1 = new DoublePoint(10.0, 0.0, 1396693800000L);
		DoublePoint refPoint2 = new DoublePoint(12.0, 0.0, 1396696500000L);
		// 11.0
		DoublePoint refPoint3 = new DoublePoint(9.0, 0.0, 1396707780000L);
		DoublePoint refPoint4 = new DoublePoint(11.0, 0.0, 1396714980000L);
		// 10.0
		List<DoublePoint> refList = new ArrayList<DoublePoint>();
		refList.add(refPoint1);
		refList.add(refPoint2);
		refList.add(refPoint3);
		refList.add(refPoint4);
		DataSet refSet = new DataSet(startRef, stopRef, new DeviceId("ref",
				"refWrapper"), refList);

		Calendar startCom1 = Calendar.getInstance();
		Calendar stopCom1 = Calendar.getInstance();
		startCom1.setTimeInMillis(1396915200000L); // 2014.04.08
		stopCom1.setTimeInMillis(1397001600000L); // 2014.04.09
		List<DoublePoint> com1List = new ArrayList<DoublePoint>();
		DataSet com1Set = new DataSet(startCom1, stopCom1, new DeviceId("temp",
				"test"), com1List);

		Calendar startCom2 = Calendar.getInstance();
		Calendar stopCom2 = Calendar.getInstance();
		startCom2.setTimeInMillis(1397260800000L); // 2014.04.12
		stopCom2.setTimeInMillis(1397347200000L); // 2014.04.13
		DoublePoint com2Point1 = new DoublePoint(15.0, 0.0, 1397283180000L);
		DoublePoint com2Point2 = new DoublePoint(18.0, 0.0, 1397303100000L);
		// 16.5 -> 5.5
		DoublePoint com2Point3 = new DoublePoint(13.0, 0.0, 1397309053000L);
		DoublePoint com2Point4 = new DoublePoint(20.0, 0.0, 1397315156000L);
		// 16.5 -> 6.5
		List<DoublePoint> com2List = new ArrayList<DoublePoint>();
		com2List.add(com2Point1);
		com2List.add(com2Point2);
		com2List.add(com2Point3);
		com2List.add(com2Point4);
		DataSet com2Set = new DataSet(startCom2, stopCom2, new DeviceId(
				"temp2", "testa"), com2List);

		List<DataSet> comSet = new ArrayList<DataSet>();
		comSet.add(com1Set);
		comSet.add(com2Set);
		ArithmeticMean mean = new ArithmeticMean();

		List<DataSet> outcome = Comparison.timeComparePercent(refSet, comSet,
				Calendar.HOUR_OF_DAY, 12, mean);
		assertNull(outcome.get(0));
	}

	@Test
	public void timeCompare_comparerOneCompListElementNull() {
		Calendar startRef = Calendar.getInstance();
		Calendar stopRef = Calendar.getInstance();
		startRef.setTimeInMillis(1396656000000L); // 2014.04.05
		stopRef.setTimeInMillis(1396742400000L); // 2014.04.06
		DoublePoint refPoint1 = new DoublePoint(10.0, 0.0, 1396693800000L);
		DoublePoint refPoint2 = new DoublePoint(12.0, 0.0, 1396696500000L);
		// 11.0
		DoublePoint refPoint3 = new DoublePoint(9.0, 0.0, 1396707780000L);
		DoublePoint refPoint4 = new DoublePoint(11.0, 0.0, 1396714980000L);
		// 10.0
		List<DoublePoint> refList = new ArrayList<DoublePoint>();
		refList.add(refPoint1);
		refList.add(refPoint2);
		refList.add(refPoint3);
		refList.add(refPoint4);
		DataSet refSet = new DataSet(startRef, stopRef, new DeviceId("ref",
				"refWrapper"), refList);

		Calendar startCom1 = Calendar.getInstance();
		Calendar stopCom1 = Calendar.getInstance();
		startCom1.setTimeInMillis(1396915200000L); // 2014.04.08
		stopCom1.setTimeInMillis(1397001600000L); // 2014.04.09
		DoublePoint com1Point1 = new DoublePoint(5.0, 0.0, 1396948320000L);
		DoublePoint com1Point2 = new DoublePoint(8.0, 0.0, 1396958100000L);
		// 6.5 -> -4.5
		DoublePoint com1Point3 = new DoublePoint(9.0, 0.0, 1396963980000L);
		DoublePoint com1Point4 = new DoublePoint(8.5, 0.0, 1396977720000L);
		// 8,75 -> -1,25
		List<DoublePoint> com1List = new ArrayList<DoublePoint>();
		com1List.add(com1Point1);
		com1List.add(com1Point2);
		com1List.add(com1Point3);
		com1List.add(com1Point4);
		DataSet com1Set = new DataSet(startCom1, stopCom1, new DeviceId("temp",
				"test"), com1List);

		Calendar startCom2 = Calendar.getInstance();
		Calendar stopCom2 = Calendar.getInstance();
		startCom2.setTimeInMillis(1397260800000L); // 2014.04.12
		stopCom2.setTimeInMillis(1397347200000L); // 2014.04.13
		DoublePoint com2Point3 = new DoublePoint(13.0, 0.0, 1397309053000L);
		DoublePoint com2Point4 = new DoublePoint(20.0, 0.0, 1397315156000L);
		// 16.5 -> 6.5
		List<DoublePoint> com2List = new ArrayList<DoublePoint>();
		com2List.add(com2Point3);
		com2List.add(com2Point4);
		DataSet com2Set = new DataSet(startCom2, stopCom2, new DeviceId(
				"temp2", "testa"), com2List);

		List<DataSet> comSet = new ArrayList<DataSet>();
		comSet.add(com1Set);
		comSet.add(com2Set);
		ArithmeticMean mean = new ArithmeticMean();

		List<DataSet> outcome = Comparison.timeComparePercent(refSet, comSet,
				Calendar.HOUR_OF_DAY, 12, mean);
		assertNull(outcome.get(1).getDataList().get(0).getValue());
	}

}
