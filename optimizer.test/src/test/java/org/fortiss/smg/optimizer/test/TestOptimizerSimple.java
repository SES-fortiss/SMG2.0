/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.optimizer.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.TimeoutException;
import org.fortiss.smg.optimizer.impl.OptimizerImpl;
import org.fortiss.smg.sqltools.lib.utils.TestingDBUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/***
 * Unit test for the optimizer.
 * 
 * @author Cheng Zhang
 *
 */
public class TestOptimizerSimple {

	private TestingDBUtil db;

	@BeforeClass
	public static void setUpDataBase() throws SQLException,
			ClassNotFoundException {
		new MockOtherBundles();
	}

	@Before
	public void setUp() throws IOException, TimeoutException,
			ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		db = new TestingDBUtil();
		System.out.println("Statement created...");
		db.executeQuery("TRUNCATE Optimizer_Exchanges");
		db.executeQuery("TRUNCATE Optimizer_Intervals");
		db.executeQuery("TRUNCATE Optimizer_Paths");
		db.executeQuery("TRUNCATE Optimizer_Periods");
		db.executeQuery("TRUNCATE Optimizer_Pools");
		db.executeQuery("TRUNCATE Optimizer_Suggestion");
		System.out.println("TestDB is clean for Optimizer Tests");
	}

	@After
	public void tearDown() {
	}

	// @Test(timeout = 5000)
	// public void testOptimizationImpl() throws TimeoutException {
	// OptimizerImpl impl = new OptimizerImpl();
	// assertEquals("Hello Optimizer", impl.doSomething("Hello Optimizer"));
	// }

	// @Test
	// public void testOptimizationRTP() throws TimeoutException {
	// OptimizerImpl impl = new OptimizerImpl(Local.date, Local.duration,
	// Local.forecastConsumption, Local.forecastGeneration, Local.RTP,
	// 0.95, 0.95, 10000, 1000, 20000, 20000, 20000, false, false);
	// assertEquals("Successful do no optimize", impl.doNoOptimize());
	// assertEquals("Successful do dummy optimize", impl.doDummyOptimize());
	// assertEquals("Successful do optimize", impl.doOptimize());
	// }

	@Test
	public void testOptimizationRTP3() throws TimeoutException {
		OptimizerImpl impl = new OptimizerImpl(Local.date, Local.duration,
				Local.forecastConsumption, Local.forecastGeneration, Local.RTP,
				0.95, 0.95, 10000, 1000, 20000, 20000, 20000, false, false);
		// assertEquals("Successful do no optimize", impl.doNoOptimize());
		// assertEquals("Successful do dummy optimize", impl.doDummyOptimize());
		assertEquals("Successful do optimize", impl.doOptimize());
	}

	// @Test
	// public void testOptimizationTOU() throws TimeoutException {
	// OptimizerImpl impl = new OptimizerImpl(Local.date, Local.duration,
	// Local.forecastConsumption, Local.forecastGeneration,
	// Local.TOU_DE, 0.95, 0.95, 10000, 1000, 20000, 20000, 20000,
	// false, false);
	// // assertEquals("Successful do no optimize", impl.doNoOptimize());
	// // assertEquals("Successful do dummy optimize", impl.doDummyOptimize());
	// assertEquals("Successful do optimize", impl.doOptimize());
	// }

	// @Test
	// public void testOptimizationTOU3() throws TimeoutException {
	// OptimizerImpl impl = new OptimizerImpl(Local.date, Local.duration,
	// Local.forecastConsumption, Local.forecastGeneration,
	// Local.TOU_DE, 0.95, 0.95, 10000, 1000, 20000, 20000, 20000,
	// true, true);
	// // assertEquals("Successful do no optimize", impl.doNoOptimize());
	// // assertEquals("Successful do dummy optimize", impl.doDummyOptimize());
	// assertEquals("Successful do optimize", impl.doOptimize());
	// }

	// @Test
	// public void testOptimizationDB() throws TimeoutException {
	// @SuppressWarnings("deprecation")
	// OptimizerImpl impl = new OptimizerImpl(new Date(114, 10, 26));
	// // 2014-11-26
	// assertEquals("Successful do optimize", impl.doOptimize());
	// }

	// @Test
	// public void testOptimizationDBNoData() throws TimeoutException {
	// @SuppressWarnings("deprecation")
	// OptimizerImpl impl = new OptimizerImpl(new Date(115, 10, 26));
	// // 2014-11-26
	// assertEquals("Successful do optimize without data", impl.doOptimize());
	// }

}
