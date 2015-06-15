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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.math3.exception.NoDataException;
import org.fortiss.smg.analyzer.api.DataSet;
import org.fortiss.smg.analyzer.impl.correlation.CorrelationCalculation;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.informationbroker.api.DoublePoint;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TestCorrelation {

	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Test
	public void pearsonCorrelation_sameTimeInterval() {
		DeviceId dev = new DeviceId("blaa", "bluuubb");
		Calendar start = Calendar.getInstance();
		start.setTimeInMillis(0);
		Calendar stop = Calendar.getInstance();
		stop.setTimeInMillis(20);

		List<DoublePoint> list1 = new ArrayList<DoublePoint>();
		list1.add(new DoublePoint(5.0, 0.0, 0));
		list1.add(new DoublePoint(10.0, 0.0, 10));
		list1.add(new DoublePoint(15.0, 0.0, 20));
		DataSet set1 = new DataSet(start, stop, dev, list1);

		List<DoublePoint> list2 = new ArrayList<DoublePoint>();
		list2.add(new DoublePoint(10.0, 0.0, 0));
		list2.add(new DoublePoint(100.0, 0.0, 10));
		list2.add(new DoublePoint(1000.0, 0.0, 20));
		DataSet set2 = new DataSet(start, stop, dev, list2);

		assertEquals(0.893,
				CorrelationCalculation.pearsonCorrelation(set1, set2, 10),
				0.001);
	}

	@Test
	public void pearsonCorrelation_diffrentTimeInterval() {
		DeviceId dev = new DeviceId("blaa", "bluuubb");

		Calendar start1 = Calendar.getInstance();
		start1.setTimeInMillis(0);
		Calendar stop1 = Calendar.getInstance();
		stop1.setTimeInMillis(20);
		List<DoublePoint> list1 = new ArrayList<DoublePoint>();
		list1.add(new DoublePoint(5.0, 0.0, 0));
		list1.add(new DoublePoint(10.0, 0.0, 10));
		list1.add(new DoublePoint(15.0, 0.0, 20));
		DataSet set1 = new DataSet(start1, stop1, dev, list1);

		Calendar start2 = Calendar.getInstance();
		start2.setTimeInMillis(5);
		Calendar stop2 = Calendar.getInstance();
		stop2.setTimeInMillis(25);
		List<DoublePoint> list2 = new ArrayList<DoublePoint>();
		list2.add(new DoublePoint(10.0, 0.0, 5));
		list2.add(new DoublePoint(100.0, 0.0, 15));
		list2.add(new DoublePoint(1000.0, 0.0, 25));
		DataSet set2 = new DataSet(start2, stop2, dev, list2);

		assertEquals(0.893,
				CorrelationCalculation.pearsonCorrelation(set1, set2, 10),
				0.001);
	}

	@Test
	public void pearsonCorrelation_diffrentSplineInterval() {
		DeviceId dev = new DeviceId("blaa", "bluuubb");

		Calendar start1 = Calendar.getInstance();
		start1.setTimeInMillis(0);
		Calendar stop1 = Calendar.getInstance();
		stop1.setTimeInMillis(20);
		List<DoublePoint> list1 = new ArrayList<DoublePoint>();
		list1.add(new DoublePoint(5.0, 0.0, 0));
		list1.add(new DoublePoint(10.0, 0.0, 10));
		list1.add(new DoublePoint(15.0, 0.0, 19));
		DataSet set1 = new DataSet(start1, stop1, dev, list1);

		Calendar start2 = Calendar.getInstance();
		start2.setTimeInMillis(5);
		Calendar stop2 = Calendar.getInstance();
		stop2.setTimeInMillis(25);
		List<DoublePoint> list2 = new ArrayList<DoublePoint>();
		list2.add(new DoublePoint(10.0, 0.0, 6));
		list2.add(new DoublePoint(100.0, 0.0, 15));
		list2.add(new DoublePoint(1000.0, 0.0, 25));
		DataSet set2 = new DataSet(start2, stop2, dev, list2);

		assertEquals(0.9103,
				CorrelationCalculation.pearsonCorrelation(set1, set2, 10),
				0.001);
	}
	
	@Test
	public void pearsonCorrelation_dataSetNull(){
		DeviceId dev = new DeviceId("blaa", "bluuubb");

		Calendar start1 = Calendar.getInstance();
		start1.setTimeInMillis(0);
		Calendar stop1 = Calendar.getInstance();
		stop1.setTimeInMillis(20);
		List<DoublePoint> list1 = new ArrayList<DoublePoint>();
		list1.add(new DoublePoint(5.0, 0.0, 0));
		list1.add(new DoublePoint(10.0, 0.0, 10));
		list1.add(new DoublePoint(15.0, 0.0, 19));
		DataSet set1 = new DataSet(start1, stop1, dev, list1);
		
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("at least one data set is null");
		CorrelationCalculation.pearsonCorrelation(set1, null, 10);
	}
	
	@Test
	public void pearsonCorrelation_dataPoints_Empty(){
		DeviceId dev = new DeviceId("blaa", "bluuubb");

		Calendar start1 = Calendar.getInstance();
		start1.setTimeInMillis(0);
		Calendar stop1 = Calendar.getInstance();
		stop1.setTimeInMillis(20);
		List<DoublePoint> list1 = new ArrayList<DoublePoint>();
		list1.add(new DoublePoint(5.0, 0.0, 0));
		list1.add(new DoublePoint(10.0, 0.0, 10));
		list1.add(new DoublePoint(15.0, 0.0, 19));
		DataSet set1 = new DataSet(start1, stop1, dev, list1);

		Calendar start2 = Calendar.getInstance();
		start2.setTimeInMillis(5);
		Calendar stop2 = Calendar.getInstance();
		stop2.setTimeInMillis(25);
		List<DoublePoint> list2 = new ArrayList<DoublePoint>();
		DataSet set2 = new DataSet(start2, stop2, dev, list2);
		
		exception.expect(NoDataException.class);
		CorrelationCalculation.pearsonCorrelation(set1, set2, 10);
	}
	
	@Test
	public void pearsonCorrelation_dataPoints_Null(){
		DeviceId dev = new DeviceId("blaa", "bluuubb");

		Calendar start1 = Calendar.getInstance();
		start1.setTimeInMillis(0);
		Calendar stop1 = Calendar.getInstance();
		stop1.setTimeInMillis(20);
		List<DoublePoint> list1 = new ArrayList<DoublePoint>();
		list1.add(new DoublePoint(5.0, 0.0, 0));
		list1.add(new DoublePoint(10.0, 0.0, 10));
		list1.add(new DoublePoint(15.0, 0.0, 19));
		DataSet set1 = new DataSet(start1, stop1, dev, list1);

		Calendar start2 = Calendar.getInstance();
		start2.setTimeInMillis(5);
		Calendar stop2 = Calendar.getInstance();
		stop2.setTimeInMillis(25);
		DataSet set2 = new DataSet(start2, stop2, dev, null);
		
		exception.expect(NoDataException.class);
		CorrelationCalculation.pearsonCorrelation(set1, set2, 10);
	}

	@Test
	public void pearsonCorrelation_noMatchingTimeInterval() {
		DeviceId dev = new DeviceId("blaa", "bluuubb");
		Calendar start1 = Calendar.getInstance();
		start1.setTimeInMillis(0);
		Calendar stop1 = Calendar.getInstance();
		stop1.setTimeInMillis(20);
	
		List<DoublePoint> list1 = new ArrayList<DoublePoint>();
		list1.add(new DoublePoint(5.0, 0.0, 0));
		list1.add(new DoublePoint(10.0, 0.0, 10));
		list1.add(new DoublePoint(15.0, 0.0, 20));
		DataSet set1 = new DataSet(start1, stop1, dev, list1);
	
		Calendar start2 = Calendar.getInstance();
		start1.setTimeInMillis(2);
		Calendar stop2 = Calendar.getInstance();
		stop2.setTimeInMillis(20);
		List<DoublePoint> list2 = new ArrayList<DoublePoint>();
		list2.add(new DoublePoint(10.0, 0.0, 0));
		list2.add(new DoublePoint(100.0, 0.0, 10));
		list2.add(new DoublePoint(1000.0, 0.0, 20));
		DataSet set2 = new DataSet(start2, stop2, dev, list2);
	
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("data sets don't have matching interval");
		CorrelationCalculation.pearsonCorrelation(set1, set2, 10);
	}
}
