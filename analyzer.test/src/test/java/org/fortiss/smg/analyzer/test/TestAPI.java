/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.analyzer.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.analyzer.api.DataSet;
import org.fortiss.smg.analyzer.api.NoDataFoundException;
import org.fortiss.smg.analyzer.impl.AnalyzerImpl;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.informationbroker.api.InformationBrokerInterface;
import org.fortiss.smg.informationbroker.api.InformationBrokerQueueNames;
import org.fortiss.smg.remoteframework.lib.DefaultProxy;
import org.fortiss.smg.sqltools.lib.utils.TestingDBUtil;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TestAPI {

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
	public void getSum() throws IllegalArgumentException, NoDataFoundException,
			TimeoutException {
		Calendar startCal = Calendar.getInstance();
		Calendar stopCal = Calendar.getInstance();
		startCal.setTimeInMillis(1416215372031L);
		stopCal.setTimeInMillis(1416215392031L);

		// 3 data points - values:
		// 21.3
		// 21.3
		// 22.5

		DeviceId dev = new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
				"hexabus.wrapper");
		AnalyzerImpl impl = new AnalyzerImpl(db);
		assertEquals(65.1, impl.getSum(startCal, stopCal, dev), 0.0);
	}

	@Test
	public void getSum_Null() throws IllegalArgumentException,
			NoDataFoundException, TimeoutException {
		Calendar startCal = Calendar.getInstance();
		Calendar stopCal = Calendar.getInstance();
		startCal.setTimeInMillis(1416215372032L);
		stopCal.setTimeInMillis(1416215372033L);

		// no data points

		DeviceId dev = new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
				"hexabus.wrapper");
		AnalyzerImpl impl = new AnalyzerImpl(db);
		exception.expect(NoDataFoundException.class);
		impl.getSum(startCal, stopCal, dev);
	}

	@Test
	public void getSum_CalendarNull() throws IllegalArgumentException,
			NoDataFoundException, TimeoutException {
		Calendar startCal = null;
		Calendar stopCal = Calendar.getInstance();
		stopCal.setTimeInMillis(1416215392031L);

		DeviceId dev = null;
		AnalyzerImpl impl = new AnalyzerImpl(db);
		exception.expect(IllegalArgumentException.class);
		impl.getSum(startCal, stopCal, dev);
	}

	@Test
	public void getSum_DeviceNull() throws IllegalArgumentException,
			NoDataFoundException, TimeoutException {
		Calendar startCal = Calendar.getInstance();
		Calendar stopCal = Calendar.getInstance();
		startCal.setTimeInMillis(1416215372031L);
		stopCal.setTimeInMillis(1416215392031L);

		// 3 data points - values:
		// 21.3
		// 21.3
		// 22.5

		DeviceId dev = null;
		AnalyzerImpl impl = new AnalyzerImpl(db);
		exception.expect(IllegalArgumentException.class);
		impl.getSum(startCal, stopCal, dev);
	}

	@Test
	public void getArithmeticMean() throws IllegalArgumentException,
			TimeoutException, NoDataFoundException {
		Calendar startCal = Calendar.getInstance();
		Calendar stopCal = Calendar.getInstance();
		startCal.setTimeInMillis(1416215372031L);
		stopCal.setTimeInMillis(1416215392031L);

		// 3 data points - values:
		// 21.3
		// 21.3
		// 22.5

		DeviceId dev = new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
				"hexabus.wrapper");
		AnalyzerImpl impl = new AnalyzerImpl(db);
		assertEquals(21.7, impl.getArithmeticMean(startCal, stopCal, dev), 0.0);
	}

	@Test
	public void getArithmeticMean_Null() throws IllegalArgumentException,
			TimeoutException, NoDataFoundException {
		Calendar startCal = Calendar.getInstance();
		Calendar stopCal = Calendar.getInstance();
		startCal.setTimeInMillis(1416215372032L);
		stopCal.setTimeInMillis(1416215372033L);

		// no data points

		DeviceId dev = new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
				"hexabus.wrapper");
		AnalyzerImpl impl = new AnalyzerImpl(db);
		exception.expect(NoDataFoundException.class);
		impl.getArithmeticMean(startCal, stopCal, dev);
	}

	@Test
	public void getArithmeticMean_CalendarNull()
			throws IllegalArgumentException, TimeoutException,
			NoDataFoundException {
		Calendar startCal = null;
		Calendar stopCal = Calendar.getInstance();
		stopCal.setTimeInMillis(1416215392031L);

		DeviceId dev = new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
				"hexabus.wrapper");
		AnalyzerImpl impl = new AnalyzerImpl(db);
		exception.expect(IllegalArgumentException.class);
		impl.getArithmeticMean(startCal, stopCal, dev);
	}

	@Test
	public void getArithmeticMean_DeviceNull() throws IllegalArgumentException,
			TimeoutException, NoDataFoundException {
		Calendar startCal = Calendar.getInstance();
		Calendar stopCal = Calendar.getInstance();
		startCal.setTimeInMillis(1416215372031L);
		stopCal.setTimeInMillis(1416215392031L);

		// 3 data points - values:
		// 21.3
		// 21.3
		// 22.5

		DeviceId dev = null;
		AnalyzerImpl impl = new AnalyzerImpl(db);
		exception.expect(IllegalArgumentException.class);
		impl.getArithmeticMean(startCal, stopCal, dev);
	}

	@Test
	public void getArithmeticMeanByTime() throws IllegalArgumentException,
			TimeoutException, NoDataFoundException {
		Calendar startCal = Calendar.getInstance();
		Calendar stopCal = Calendar.getInstance();
		startCal.setTimeInMillis(1416215372031L);
		stopCal.setTimeInMillis(1416215392031L);

		// 3 data points - values:
		// 21.3
		// 21.3
		// 22.5

		DeviceId dev = new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
				"hexabus.wrapper");
		AnalyzerImpl impl = new AnalyzerImpl(db);
		assertEquals(0.12055,
				impl.getArithmeticMeanByTime(startCal, stopCal, dev), 0.00001);
	}

	@Test
	public void getArithmeticMeanByTime_Null() throws IllegalArgumentException,
			TimeoutException, NoDataFoundException {
		Calendar startCal = Calendar.getInstance();
		Calendar stopCal = Calendar.getInstance();
		startCal.setTimeInMillis(1416215372032L);
		stopCal.setTimeInMillis(1416215372033L);

		// no data points

		DeviceId dev = new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
				"hexabus.wrapper");
		AnalyzerImpl impl = new AnalyzerImpl(db);
		exception.expect(NoDataFoundException.class);
		impl.getArithmeticMeanByTime(startCal, stopCal, dev);
	}

	@Test
	public void getArithmeticMeanByTime_CalendarNull()
			throws IllegalArgumentException, TimeoutException,
			NoDataFoundException {
		Calendar startCal = null;
		Calendar stopCal = Calendar.getInstance();
		stopCal.setTimeInMillis(1416215392031L);

		DeviceId dev = new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
				"hexabus.wrapper");
		AnalyzerImpl impl = new AnalyzerImpl(db);
		exception.expect(IllegalArgumentException.class);
		impl.getArithmeticMeanByTime(startCal, stopCal, dev);
	}

	@Test
	public void getArithmeticMeanByTime_DeviceNull()
			throws IllegalArgumentException, TimeoutException,
			NoDataFoundException {
		Calendar startCal = Calendar.getInstance();
		Calendar stopCal = Calendar.getInstance();
		startCal.setTimeInMillis(1416215372031L);
		stopCal.setTimeInMillis(1416215392031L);

		// 3 data points - values:
		// 21.3
		// 21.3
		// 22.5

		DeviceId dev = null;
		AnalyzerImpl impl = new AnalyzerImpl(db);
		exception.expect(IllegalArgumentException.class);
		impl.getArithmeticMeanByTime(startCal, stopCal, dev);
	}

	@Test
	public void getMax() throws IllegalArgumentException, TimeoutException,
			NoDataFoundException {
		Calendar startCal = Calendar.getInstance();
		Calendar stopCal = Calendar.getInstance();
		startCal.setTimeInMillis(1416215372031L);
		stopCal.setTimeInMillis(1416215392031L);

		// 3 data points - values:
		// 21.3
		// 21.3
		// 22.5

		DeviceId dev = new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
				"hexabus.wrapper");
		AnalyzerImpl impl = new AnalyzerImpl(db);
		assertEquals(22.5, impl.getMax(startCal, stopCal, dev), 0.0);
	}

	@Test
	public void getMax_Null() throws IllegalArgumentException,
			TimeoutException, NoDataFoundException {
		Calendar startCal = Calendar.getInstance();
		Calendar stopCal = Calendar.getInstance();
		startCal.setTimeInMillis(1416215372032L);
		stopCal.setTimeInMillis(1416215372033L);

		// no data points

		DeviceId dev = new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
				"hexabus.wrapper");
		AnalyzerImpl impl = new AnalyzerImpl(db);
		exception.expect(NoDataFoundException.class);
		impl.getMax(startCal, stopCal, dev);
	}

	@Test
	public void getMax_CalendarNull() throws IllegalArgumentException,
			TimeoutException, NoDataFoundException {
		Calendar startCal = null;
		Calendar stopCal = Calendar.getInstance();
		stopCal.setTimeInMillis(1416215392031L);

		DeviceId dev = new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
				"hexabus.wrapper");
		AnalyzerImpl impl = new AnalyzerImpl(db);
		exception.expect(IllegalArgumentException.class);
		impl.getMax(startCal, stopCal, dev);
	}

	@Test
	public void getMax_DeviceNull() throws IllegalArgumentException,
			TimeoutException, NoDataFoundException {
		Calendar startCal = Calendar.getInstance();
		Calendar stopCal = Calendar.getInstance();
		startCal.setTimeInMillis(1416215372031L);
		stopCal.setTimeInMillis(1416215392031L);

		// 3 data points - values:
		// 21.3
		// 21.3
		// 22.5

		DeviceId dev = null;
		AnalyzerImpl impl = new AnalyzerImpl(db);
		exception.expect(IllegalArgumentException.class);
		impl.getMax(startCal, stopCal, dev);
	}

	@Test
	public void getMin() throws IllegalArgumentException, TimeoutException,
			NoDataFoundException {
		Calendar startCal = Calendar.getInstance();
		Calendar stopCal = Calendar.getInstance();
		startCal.setTimeInMillis(1416215372031L);
		stopCal.setTimeInMillis(1416215392031L);

		// 3 data points - values:
		// 21.3
		// 21.3
		// 22.5

		DeviceId dev = new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
				"hexabus.wrapper");
		AnalyzerImpl impl = new AnalyzerImpl(db);
		assertEquals(21.3, impl.getMin(startCal, stopCal, dev), 0.0);
	}

	@Test
	public void getMin_Null() throws IllegalArgumentException,
			TimeoutException, NoDataFoundException {
		Calendar startCal = Calendar.getInstance();
		Calendar stopCal = Calendar.getInstance();
		startCal.setTimeInMillis(1416215372032L);
		stopCal.setTimeInMillis(1416215372033L);

		// no data points

		DeviceId dev = new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
				"hexabus.wrapper");
		AnalyzerImpl impl = new AnalyzerImpl(db);
		exception.expect(NoDataFoundException.class);
		impl.getMin(startCal, stopCal, dev);
	}

	@Test
	public void getMin_CalendarNull() throws IllegalArgumentException,
			TimeoutException, NoDataFoundException {
		Calendar startCal = Calendar.getInstance();
		Calendar stopCal = null;
		startCal.setTimeInMillis(1416215372031L);

		DeviceId dev = new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
				"hexabus.wrapper");
		AnalyzerImpl impl = new AnalyzerImpl(db);
		exception.expect(IllegalArgumentException.class);
		impl.getMin(startCal, stopCal, dev);
	}

	@Test
	public void getMin_DeviceNull() throws IllegalArgumentException,
			TimeoutException, NoDataFoundException {
		Calendar startCal = Calendar.getInstance();
		Calendar stopCal = Calendar.getInstance();
		startCal.setTimeInMillis(1416215372031L);
		stopCal.setTimeInMillis(1416215392031L);

		// 3 data points - values:
		// 21.3
		// 21.3
		// 22.5

		DeviceId dev = null;
		AnalyzerImpl impl = new AnalyzerImpl(db);
		exception.expect(IllegalArgumentException.class);
		impl.getMin(startCal, stopCal, dev);
	}

	@Test
	public void getConsumptionRating_low() throws IllegalArgumentException,
			NoDataFoundException, TimeoutException {
		Calendar startCal = Calendar.getInstance();
		Calendar stopCal = Calendar.getInstance();
		startCal.setTimeInMillis(1416215372031L);
		stopCal.setTimeInMillis(1416215392031L);

		// 3 data points - values:
		// 21.3
		// 21.3
		// 22.5

		Calendar startCal2 = Calendar.getInstance();
		Calendar stopCal2 = Calendar.getInstance();
		startCal2.setTimeInMillis(1416176588571L);
		stopCal2.setTimeInMillis(1416176608630L);
		// time 2 stop: 1416176608630
		// time 2 start: 1416176588571

		// 3 data points - values:
		// 23
		// 23
		// 23

		DeviceId dev = new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
				"hexabus.wrapper");
		DataSet current = new DataSet(startCal, stopCal, dev);
		DataSet reference = new DataSet(startCal2, stopCal2, dev);

		AnalyzerImpl impl = new AnalyzerImpl(db);

		boolean expectedArray[] = new boolean[4];
		expectedArray[0] = true;
		expectedArray[1] = false;
		expectedArray[2] = false;
		expectedArray[3] = false;

		assertArrayEquals(expectedArray,
				impl.getConsumptionRating(current, reference, 30.0));
	}

	@Test
	public void getConsumptionRating_normal() throws IllegalArgumentException,
			NoDataFoundException, TimeoutException {
		Calendar startCal = Calendar.getInstance();
		Calendar stopCal = Calendar.getInstance();
		startCal.setTimeInMillis(1416175158602L);
		stopCal.setTimeInMillis(1416215372031L);

		// many data points
		// mean = 22.866003616636473
		// max = 23.5
		// threshold = 22,888869620253109473

		Calendar startCal2 = Calendar.getInstance();
		Calendar stopCal2 = Calendar.getInstance();
		startCal2.setTimeInMillis(1416176588571L);
		stopCal2.setTimeInMillis(1416176608630L);
		// time 2 stop: 1416176608630
		// time 2 start: 1416176588571

		// 3 data points - values:
		// 23
		// 23
		// 23

		DeviceId dev = new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
				"hexabus.wrapper");
		DataSet current = new DataSet(startCal2, stopCal2, dev);
		DataSet reference = new DataSet(startCal, stopCal, dev);

		AnalyzerImpl impl = new AnalyzerImpl(db);

		boolean expectedArray[] = new boolean[4];
		expectedArray[0] = false;
		expectedArray[1] = true;
		expectedArray[2] = false;
		expectedArray[3] = false;

		assertArrayEquals(expectedArray,
				impl.getConsumptionRating(current, reference, 2));
	}

	@Test
	public void getConsumptionRating_normalAndExtreme()
			throws IllegalArgumentException, NoDataFoundException,
			TimeoutException {
		Calendar startCal = Calendar.getInstance();
		Calendar stopCal = Calendar.getInstance();
		startCal.setTimeInMillis(1416215372031L);
		stopCal.setTimeInMillis(1416215392031L);

		// 3 data points - values:
		// 21.3
		// 21.3
		// 22.5

		Calendar startCal2 = Calendar.getInstance();
		Calendar stopCal2 = Calendar.getInstance();
		startCal2.setTimeInMillis(1416176588571L);
		stopCal2.setTimeInMillis(1416176608630L);
		// time 2 stop: 1416176608630
		// time 2 start: 1416176588571

		// 3 data points - values:
		// 23
		// 23
		// 23

		DeviceId dev = new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
				"hexabus.wrapper");
		DataSet current = new DataSet(startCal2, stopCal2, dev);
		DataSet reference = new DataSet(startCal, stopCal, dev);

		AnalyzerImpl impl = new AnalyzerImpl(db);

		boolean expectedArray[] = new boolean[4];
		expectedArray[0] = false;
		expectedArray[1] = true;
		expectedArray[2] = false;
		expectedArray[3] = true;

		assertArrayEquals(expectedArray,
				impl.getConsumptionRating(current, reference, 30.0));
	}

	@Test
	public void getConsumptionRating_high() throws IllegalArgumentException,
			NoDataFoundException, TimeoutException {
		Calendar startCal = Calendar.getInstance();
		Calendar stopCal = Calendar.getInstance();
		startCal.setTimeInMillis(1416175158602L);
		stopCal.setTimeInMillis(1416215372031L);

		// many data points
		// mean = 22.866003616636473
		// max = 23.5
		// threshold = 22,888869620253109473

		Calendar startCal2 = Calendar.getInstance();
		Calendar stopCal2 = Calendar.getInstance();
		startCal2.setTimeInMillis(1416176588571L);
		stopCal2.setTimeInMillis(1416176608630L);
		// time 2 stop: 1416176608630
		// time 2 start: 1416176588571

		// 3 data points - values:
		// 23
		// 23
		// 23

		DeviceId dev = new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
				"hexabus.wrapper");
		DataSet current = new DataSet(startCal2, stopCal2, dev);
		DataSet reference = new DataSet(startCal, stopCal, dev);

		AnalyzerImpl impl = new AnalyzerImpl(db);

		boolean expectedArray[] = new boolean[4];
		expectedArray[0] = false;
		expectedArray[1] = false;
		expectedArray[2] = true;
		expectedArray[3] = false;

		assertArrayEquals(expectedArray,
				impl.getConsumptionRating(current, reference, 0.1));
	}

	@Test
	public void getConsumptionRating_extreme() throws IllegalArgumentException,
			NoDataFoundException, TimeoutException {
		Calendar startCal = Calendar.getInstance();
		Calendar stopCal = Calendar.getInstance();
		startCal.setTimeInMillis(1416215372031L);
		stopCal.setTimeInMillis(1416215392031L);

		// 3 data points - values:
		// 21.3
		// 21.3
		// 22.5

		Calendar startCal2 = Calendar.getInstance();
		Calendar stopCal2 = Calendar.getInstance();
		startCal2.setTimeInMillis(1416176588571L);
		stopCal2.setTimeInMillis(1416176608630L);
		// time 2 stop: 1416176608630
		// time 2 start: 1416176588571

		// 3 data points - values:
		// 23
		// 23
		// 23

		DeviceId dev = new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
				"hexabus.wrapper");
		DataSet current = new DataSet(startCal2, stopCal2, dev);
		DataSet reference = new DataSet(startCal, stopCal, dev);

		AnalyzerImpl impl = new AnalyzerImpl(db);

		boolean expectedArray[] = new boolean[4];
		expectedArray[0] = false;
		expectedArray[1] = false;
		expectedArray[2] = false;
		expectedArray[3] = true;

		assertArrayEquals(expectedArray,
				impl.getConsumptionRating(current, reference, 1.0));
	}

	@Test
	public void getConsumptionRating_allValuesSame()
			throws IllegalArgumentException, NoDataFoundException,
			TimeoutException {
		Calendar startCal = Calendar.getInstance();
		Calendar stopCal = Calendar.getInstance();
		startCal.setTimeInMillis(1416176588571L);
		stopCal.setTimeInMillis(1416176608630L);
		// 3 data points - values:
		// 23
		// 23
		// 23

		Calendar startCal2 = Calendar.getInstance();
		Calendar stopCal2 = Calendar.getInstance();
		startCal2.setTimeInMillis(1416176588571L);
		stopCal2.setTimeInMillis(1416176608630L);
		// 3 data points - values:
		// 23
		// 23
		// 23

		DeviceId dev = new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
				"hexabus.wrapper");
		DataSet current = new DataSet(startCal2, stopCal2, dev);
		DataSet reference = new DataSet(startCal, stopCal, dev);

		AnalyzerImpl impl = new AnalyzerImpl(db);

		boolean expectedArray[] = new boolean[4];
		expectedArray[0] = false;
		expectedArray[1] = true;
		expectedArray[2] = false;
		expectedArray[3] = false;

		assertArrayEquals(expectedArray,
				impl.getConsumptionRating(current, reference, 1.0));
	}

	@Test
	public void getConsumptionRating_oneDataSetEmpty()
			throws IllegalArgumentException, NoDataFoundException,
			TimeoutException {
		Calendar startCal = Calendar.getInstance();
		Calendar stopCal = Calendar.getInstance();
		startCal.setTimeInMillis(1416176588572L);
		stopCal.setTimeInMillis(1416176588573L);
		// no data

		Calendar startCal2 = Calendar.getInstance();
		Calendar stopCal2 = Calendar.getInstance();
		startCal2.setTimeInMillis(1416176588571L);
		stopCal2.setTimeInMillis(1416176608630L);
		// 3 data points - values:
		// 23
		// 23
		// 23

		DeviceId dev = new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
				"hexabus.wrapper");
		DataSet current = new DataSet(startCal2, stopCal2, dev);
		DataSet reference = new DataSet(startCal, stopCal, dev);

		AnalyzerImpl impl = new AnalyzerImpl(db);

		exception.expect(NoDataFoundException.class);
		impl.getConsumptionRating(current, reference, 3.0);
	}

	@Test
	public void getConsumptionRating_oneDataSetNull()
			throws IllegalArgumentException, NoDataFoundException,
			TimeoutException {
		Calendar startCal2 = Calendar.getInstance();
		Calendar stopCal2 = Calendar.getInstance();
		startCal2.setTimeInMillis(1416176588571L);
		stopCal2.setTimeInMillis(1416176608630L);
		// 3 data points - values:
		// 23
		// 23
		// 23

		DeviceId dev = new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
				"hexabus.wrapper");
		DataSet current = new DataSet(startCal2, stopCal2, dev);
		DataSet reference = null;

		AnalyzerImpl impl = new AnalyzerImpl(db);

		exception.expect(IllegalArgumentException.class);
		impl.getConsumptionRating(current, reference, 3.0);
	}

	@Test
	public void getCorrelationTwoDevices() throws TimeoutException, NoDataFoundException{
		AnalyzerImpl impl = new AnalyzerImpl(db);
		
		Calendar start = Calendar.getInstance();
		Calendar stop = Calendar.getInstance();
		start.setTimeInMillis(1416215347498L);
		stop.setTimeInMillis(1416215422097L);
		
		DataSet xSet = new DataSet(start, stop, new DeviceId("fdfb:8f9f:4c8:bb4b:50:c4ff:fe04:8404.2", "hexabus.wrapper"));
		DataSet ySet = new DataSet(start, stop, new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.3", "hexabus.wrapper"));
		assertEquals(-0.999, impl.getCorrelationTwoDevices(xSet, ySet, 10), 0.001);
	}
	
	@Test
	public void getCorrelationTwoDevices_dataSetNull() throws TimeoutException, NoDataFoundException{
		AnalyzerImpl impl = new AnalyzerImpl(db);
		
		Calendar start = Calendar.getInstance();
		Calendar stop = Calendar.getInstance();
		start.setTimeInMillis(1416215347498L);
		stop.setTimeInMillis(1416215422097L);
		
		DataSet xSet = new DataSet(start, stop, new DeviceId("fdfb:8f9f:4c8:bb4b:50:c4ff:fe04:8404.2", "hexabus.wrapper"));
		DataSet ySet = null;
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("DataSet == null is not valid!");
		assertEquals(-0.999, impl.getCorrelationTwoDevices(xSet, ySet, 10), 0.001);
	}
	
	public void getCorrelationTwoDevices_noDataFound() throws TimeoutException, NoDataFoundException{
		AnalyzerImpl impl = new AnalyzerImpl(db);
		
		Calendar start = Calendar.getInstance();
		Calendar stop = Calendar.getInstance();
		start.setTimeInMillis(1416215347498L);
		stop.setTimeInMillis(1416215422097L);
		
		DataSet xSet = new DataSet(start, stop, new DeviceId("fdfb:8f9f:4c8:bb4b:50:c4ff:fe04:8404.2", "hexabus.wrapper"));
		DataSet ySet = null;
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("no data found in the database");
		assertEquals(-0.999, impl.getCorrelationTwoDevices(xSet, ySet, 10), 0.001);
	}
}
