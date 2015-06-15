/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.analyzer.test.pattern;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.math3.analysis.interpolation.LoessInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.commons.math3.exception.NonMonotonicSequenceException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.fortiss.smg.analyzer.api.DataSet;
import org.fortiss.smg.analyzer.impl.pattern.Interpolator;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.informationbroker.api.DoublePoint;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TestInterpolate {

	@Rule
	public ExpectedException thrownException = ExpectedException.none();

	@Test
	public void interpolateSpline_numbSplines() {
		DoublePoint point1 = new DoublePoint(3.0, 0.0, 1);
		DoublePoint point2 = new DoublePoint(14.0, 0.0, 2);
		DoublePoint point3 = new DoublePoint(5.0, 0.0, 3);
		DoublePoint point4 = new DoublePoint(7.0, 0.0, 4);
		DoublePoint point5 = new DoublePoint(6.0, 0.0, 5);
		List<DoublePoint> list = new ArrayList<DoublePoint>();
		list.add(point1);
		list.add(point2);
		list.add(point3);
		list.add(point4);
		list.add(point5);
		Calendar start = Calendar.getInstance();
		start.setTimeInMillis(1);
		Calendar stop = Calendar.getInstance();
		stop.setTimeInMillis(5);
		DataSet set = new DataSet(start, stop, new DeviceId("bla", "blub"), list);
		PolynomialSplineFunction poly = Interpolator.interpolateSpline(set);
		assertEquals(4, poly.getN(), 0.0);
	}

	@Test
	public void interpolateSpline_singleValue() {
		DoublePoint point1 = new DoublePoint(3.0, 0.0, 1);
		DoublePoint point2 = new DoublePoint(14.0, 0.0, 2);
		DoublePoint point3 = new DoublePoint(5.0, 0.0, 3);
		DoublePoint point4 = new DoublePoint(7.0, 0.0, 4);
		DoublePoint point5 = new DoublePoint(6.0, 0.0, 5);
		List<DoublePoint> list = new ArrayList<DoublePoint>();
		list.add(point1);
		list.add(point2);
		list.add(point3);
		list.add(point4);
		list.add(point5);
		Calendar start = Calendar.getInstance();
		start.setTimeInMillis(1);
		Calendar stop = Calendar.getInstance();
		stop.setTimeInMillis(5);
		DataSet set = new DataSet(start, stop, new DeviceId("bla", "blub"), list);
		PolynomialSplineFunction poly = Interpolator.interpolateSpline(set);
		assertEquals(9.482, poly.value(1.4), 0.0);
	}

	@Test
	public void interpolateSpline_notEnoughValues() {
		DoublePoint point1 = new DoublePoint(3.0, 0.0, 1);
		DoublePoint point2 = new DoublePoint(14.0, 0.0, 2);
		List<DoublePoint> list = new ArrayList<DoublePoint>();
		list.add(point1);
		list.add(point2);
		Calendar start = Calendar.getInstance();
		start.setTimeInMillis(1);
		Calendar stop = Calendar.getInstance();
		stop.setTimeInMillis(5);
		DataSet set = new DataSet(start, stop, new DeviceId("bla", "blub"), list);
		
		thrownException.expect(IllegalArgumentException.class);
		thrownException.expectMessage("not enough points available");
		Interpolator.interpolateSpline(set);
	}

	@Test
	public void interpolateSpline_DataSetNull() {
		DataSet set = null;
		thrownException.expect(IllegalArgumentException.class);
		thrownException.expectMessage("dataSet is null");
		Interpolator.interpolateSpline(set);
	}

	@Test
	public void interpolateSpline_nonMonotonicSequenceException() {
		thrownException.expect(NonMonotonicSequenceException.class);
		DoublePoint point1 = new DoublePoint(14.0, 0.0, 3);
		DoublePoint point2 = new DoublePoint(3.0, 0.0, 1);
		DoublePoint point3 = new DoublePoint(4.5, 0.0, 2);
		DoublePoint point4 = new DoublePoint(17.0, 0.0, 4);
		List<DoublePoint> list = new ArrayList<DoublePoint>();
		list.add(point1);
		list.add(point2);
		list.add(point3);
		list.add(point4);
		Calendar start = Calendar.getInstance();
		start.setTimeInMillis(1);
		Calendar stop = Calendar.getInstance();
		stop.setTimeInMillis(5);
		DataSet set = new DataSet(start, stop, new DeviceId("bla", "blub"), list);
		Interpolator.interpolateSpline(set);
	}

	@Test
	public void loessInterpolation_correctSplines() {
		DoublePoint point1 = new DoublePoint(3.0, 0.0, 1);
		DoublePoint point2 = new DoublePoint(14.0, 0.0, 10);
		List<DoublePoint> list1 = new ArrayList<DoublePoint>();
		list1.add(point1);
		list1.add(point2);
		Calendar start1 = Calendar.getInstance();
		start1.setTimeInMillis(1);
		Calendar stop1 = Calendar.getInstance();
		stop1.setTimeInMillis(10);
		DataSet set1 = new DataSet(start1, stop1, new DeviceId("bla", "blub"), list1);

		DoublePoint point3 = new DoublePoint(5.0, 0.0, 12);
		DoublePoint point4 = new DoublePoint(12.0, 0.0, 13);
		DoublePoint point5 = new DoublePoint(10.0, 0.0, 16);
		List<DoublePoint> list2 = new ArrayList<DoublePoint>();
		list2.add(point3);
		list2.add(point4);
		list2.add(point5);
		Calendar start2 = Calendar.getInstance();
		start2.setTimeInMillis(12);
		Calendar stop2 = Calendar.getInstance();
		stop2.setTimeInMillis(22);
		DataSet set2 = new DataSet(start2, stop2, new DeviceId("bla2", "blub2"), list2);

		DoublePoint point6 = new DoublePoint(1.0, 0.0, 23);
		DoublePoint point7 = new DoublePoint(11.0, 0.0, 27);
		DoublePoint point8 = new DoublePoint(15.0, 0.0, 44);
		DoublePoint point9 = new DoublePoint(3.0, 0.0, 45);
		DoublePoint point10 = new DoublePoint(13.0, 0.0, 47);
		List<DoublePoint> list3 = new ArrayList<DoublePoint>();
		list3.add(point6);
		list3.add(point7);
		list3.add(point8);
		list3.add(point9);
		list3.add(point10);
		Calendar start3 = Calendar.getInstance();
		start3.setTimeInMillis(23);
		Calendar stop3 = Calendar.getInstance();
		stop3.setTimeInMillis(44);
		DataSet set3 = new DataSet(start3, stop3, new DeviceId("bla3", "blub3"), list3);

		List<DataSet> setList = new ArrayList<DataSet>();
		setList.add(set1);
		setList.add(set2);
		setList.add(set3);

		Interpolator interpolator = new Interpolator();
		interpolator.loessInterpolation(setList);

		// building expected result
		// DoublePoint point1 = new DoublePoint(1, 3.0, null, 0.0);
		// DoublePoint point4 = new DoublePoint(2, 12.0, null, 0.0);
		// DoublePoint point5 = new DoublePoint(5, 10.5, null, 0.0);
		// DoublePoint point2 = new DoublePoint(10, 14.0, null, 0.0);
		// DoublePoint point8 = new DoublePoint(22, 15.0, null, 0.0);
		// DoublePoint point9 = new DoublePoint(23, 3.0, null, 0.0);
		// DoublePoint point10 = new DoublePoint(25, 13.0, null, 0.0);
		double[] x = new double[7];
		x[0] = 1.0;
		x[1] = 2.0;
		x[2] = 5.0;
		x[3] = 10.0;
		x[4] = 22.0;
		x[5] = 23.0;
		x[6] = 25.0;
		double[] y = new double[7];
		y[0] = 3.0;
		y[1] = 12.0;
		y[2] = 10.5;
		y[3] = 14.0;
		y[4] = 15.0;
		y[5] = 3.0;
		y[6] = 13.0;
		LoessInterpolator loessInterpol = new LoessInterpolator();
		PolynomialSplineFunction expected = loessInterpol.interpolate(x, y);

		assertArrayEquals(expected.getPolynomials(), interpolator.getFunction()
				.getPolynomials());
	}

	@Test
	public void loessInterpolation_numberIsTooSmallException() {
		thrownException.expect(NumberIsTooSmallException.class);
		DoublePoint point1 = new DoublePoint(3.0, 0.0, 1);
		DoublePoint point2 = new DoublePoint(14.0, 0.0, 3);
		DoublePoint point3 = new DoublePoint(2.0, 0.0, 10);
		List<DoublePoint> list1 = new ArrayList<DoublePoint>();
		list1.add(point1);
		list1.add(point2);
		list1.add(point3);
		Calendar start1 = Calendar.getInstance();
		start1.setTimeInMillis(1);
		Calendar stop1 = Calendar.getInstance();
		stop1.setTimeInMillis(10);
		DataSet set1 = new DataSet(start1, stop1, new DeviceId("bla", "blub"), list1);
		List<DataSet> setList = new ArrayList<DataSet>();
		setList.add(set1);
		Interpolator interpol = new Interpolator();
		interpol.loessInterpolation(setList);
	}
}
