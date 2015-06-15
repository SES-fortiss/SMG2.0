/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.analyzer.test.pattern;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.fortiss.smg.analyzer.api.DataSet;
import org.fortiss.smg.analyzer.impl.pattern.Classifier;
import org.fortiss.smg.analyzer.impl.pattern.Interpolator;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.informationbroker.api.DoublePoint;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TestClassifier {

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void compareAll_positive() {
		double[] coefficents = new double[1];
		coefficents[0] = 5.0;
		PolynomialFunction[] function = new PolynomialFunction[1];
		function[0] = new PolynomialFunction(coefficents);
		double[] knots = new double[2];
		knots[0] = 10.0;
		knots[1] = 20.0;
		PolynomialSplineFunction spline = new PolynomialSplineFunction(knots,
				function);
		Calendar functionStart = Calendar.getInstance();
		functionStart.setTimeInMillis(5);
		Interpolator testInterpol = new Interpolator(functionStart, spline);

		// should not find a matching part in the function
		DoublePoint point1 = new DoublePoint(2.0, 0.0, 5);
		// should find matching part
		DoublePoint point2 = new DoublePoint(6.5, 0.0, 10);
		DoublePoint point3 = new DoublePoint(4.8, 0.0, 13);
		List<DoublePoint> pointList = new ArrayList<DoublePoint>();
		pointList.add(point1);
		pointList.add(point2);
		pointList.add(point3);
		Calendar startSet = Calendar.getInstance();
		Calendar stopSet = Calendar.getInstance();
		startSet.setTimeInMillis(5);
		stopSet.setTimeInMillis(13);
		DataSet set = new DataSet(startSet, stopSet,
				new DeviceId("bla", "blub"), pointList);
		List<DataSet> setList = new ArrayList<DataSet>();
		setList.add(set);
		assertEquals(set,
				Classifier.compareAll(setList, 20, testInterpol).get(0));
		;
	}

	@Test
	public void compareAll_negative() {
		double[] coefficents = new double[1];
		coefficents[0] = 5.0;
		PolynomialFunction[] function = new PolynomialFunction[1];
		function[0] = new PolynomialFunction(coefficents);
		double[] knots = new double[2];
		knots[0] = 10.0;
		knots[1] = 20.0;
		PolynomialSplineFunction spline = new PolynomialSplineFunction(knots,
				function);
		Calendar functionStart = Calendar.getInstance();
		functionStart.setTimeInMillis(5);
		Interpolator testInterpol = new Interpolator(functionStart, spline);

		// should not find a matching part in the function
		DoublePoint point1 = new DoublePoint(2.0, 0.0, 5);
		// should find matching part
		DoublePoint point2 = new DoublePoint(6.5, 0.0, 10);
		DoublePoint point3 = new DoublePoint(4.8, 0.0, 13);
		List<DoublePoint> pointList = new ArrayList<DoublePoint>();
		pointList.add(point1);
		pointList.add(point2);
		pointList.add(point3);
		Calendar startSet = Calendar.getInstance();
		Calendar stopSet = Calendar.getInstance();
		startSet.setTimeInMillis(5);
		stopSet.setTimeInMillis(13);
		DataSet set = new DataSet(startSet, stopSet,
				new DeviceId("bla", "blub"), pointList);
		List<DataSet> setList = new ArrayList<DataSet>();
		setList.add(set);

		List<DataSet> expecetedSetList = new ArrayList<DataSet>();
		assertEquals(expecetedSetList,
				Classifier.compareAll(setList, 15, testInterpol));
	}

	@Test
	public void compareAll_negativeTolerance() {
		double[] coefficents = new double[1];
		coefficents[0] = 5.0;
		PolynomialFunction[] function = new PolynomialFunction[1];
		function[0] = new PolynomialFunction(coefficents);
		double[] knots = new double[2];
		knots[0] = 10.0;
		knots[1] = 20.0;
		PolynomialSplineFunction spline = new PolynomialSplineFunction(knots,
				function);
		Calendar functionStart = Calendar.getInstance();
		functionStart.setTimeInMillis(5);
		Interpolator testInterpol = new Interpolator(functionStart, spline);

		// should not find a matching part in the function
		DoublePoint point1 = new DoublePoint(2.0, 0.0, 5);
		// should find matching part
		DoublePoint point2 = new DoublePoint(6.5, 0.0, 10);
		DoublePoint point3 = new DoublePoint(4.8, 0.0, 13);
		List<DoublePoint> pointList = new ArrayList<DoublePoint>();
		pointList.add(point1);
		pointList.add(point2);
		pointList.add(point3);
		Calendar startSet = Calendar.getInstance();
		Calendar stopSet = Calendar.getInstance();
		startSet.setTimeInMillis(5);
		stopSet.setTimeInMillis(13);
		DataSet set = new DataSet(startSet, stopSet,
				new DeviceId("bla", "blub"), pointList);
		List<DataSet> setList = new ArrayList<DataSet>();
		setList.add(set);

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("negative tolerance found (not valid)");
		Classifier.compareAll(setList, -20, testInterpol);
	}

	@Test
	public void compareAll_emptyDoublePointList() {
		double[] coefficents = new double[1];
		coefficents[0] = 5.0;
		PolynomialFunction[] function = new PolynomialFunction[1];
		function[0] = new PolynomialFunction(coefficents);
		double[] knots = new double[2];
		knots[0] = 10.0;
		knots[1] = 20.0;
		PolynomialSplineFunction spline = new PolynomialSplineFunction(knots,
				function);
		Calendar functionStart = Calendar.getInstance();
		functionStart.setTimeInMillis(5);
		Interpolator testInterpol = new Interpolator(functionStart, spline);

		List<DoublePoint> pointList = new ArrayList<DoublePoint>();
		Calendar startSet = Calendar.getInstance();
		Calendar stopSet = Calendar.getInstance();
		startSet.setTimeInMillis(5);
		stopSet.setTimeInMillis(13);
		DataSet set = new DataSet(startSet, stopSet,
				new DeviceId("bla", "blub"), pointList);
		List<DataSet> setList = new ArrayList<DataSet>();
		setList.add(set);

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("data set dosen't include data points");
		Classifier.compareAll(setList, 20, testInterpol);
	}

	@Test
	public void compareAll_nullDoublePointList() {
		double[] coefficents = new double[1];
		coefficents[0] = 5.0;
		PolynomialFunction[] function = new PolynomialFunction[1];
		function[0] = new PolynomialFunction(coefficents);
		double[] knots = new double[2];
		knots[0] = 10.0;
		knots[1] = 20.0;
		PolynomialSplineFunction spline = new PolynomialSplineFunction(knots,
				function);
		Calendar functionStart = Calendar.getInstance();
		functionStart.setTimeInMillis(5);
		Interpolator testInterpol = new Interpolator(functionStart, spline);

		List<DoublePoint> pointList = null;
		Calendar startSet = Calendar.getInstance();
		Calendar stopSet = Calendar.getInstance();
		startSet.setTimeInMillis(5);
		stopSet.setTimeInMillis(13);
		DataSet set = new DataSet(startSet, stopSet,
				new DeviceId("bla", "blub"), pointList);
		List<DataSet> setList = new ArrayList<DataSet>();
		setList.add(set);

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("data set dosen't include data points");
		Classifier.compareAll(setList, 20, testInterpol);
	}

	@Test
	public void compareAll_emptyDataSet() {
		double[] coefficents = new double[1];
		coefficents[0] = 5.0;
		PolynomialFunction[] function = new PolynomialFunction[1];
		function[0] = new PolynomialFunction(coefficents);
		double[] knots = new double[2];
		knots[0] = 10.0;
		knots[1] = 20.0;
		PolynomialSplineFunction spline = new PolynomialSplineFunction(knots,
				function);
		Calendar functionStart = Calendar.getInstance();
		functionStart.setTimeInMillis(5);
		Interpolator testInterpol = new Interpolator(functionStart, spline);

		List<DataSet> setList = new ArrayList<DataSet>();

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("list of data sets is empty or null");
		Classifier.compareAll(setList, 20, testInterpol);
	}

	@Test
	public void compareAll_nullDataSet() {
		double[] coefficents = new double[1];
		coefficents[0] = 5.0;
		PolynomialFunction[] function = new PolynomialFunction[1];
		function[0] = new PolynomialFunction(coefficents);
		double[] knots = new double[2];
		knots[0] = 10.0;
		knots[1] = 20.0;
		PolynomialSplineFunction spline = new PolynomialSplineFunction(knots,
				function);
		Calendar functionStart = Calendar.getInstance();
		functionStart.setTimeInMillis(5);
		Interpolator testInterpol = new Interpolator(functionStart, spline);

		List<DataSet> setList = null;

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("list of data sets is empty or null");
		Classifier.compareAll(setList, 20, testInterpol);
	}

	@Test
	public void compareAll_interpolatorFunctionNull() {
		PolynomialSplineFunction spline = null;
		Calendar functionStart = Calendar.getInstance();
		functionStart.setTimeInMillis(5);
		Interpolator testInterpol = new Interpolator(functionStart, spline);

		// should not find a matching part in the function
		DoublePoint point1 = new DoublePoint(2.0, 0.0, 5);
		// should find matching part
		DoublePoint point2 = new DoublePoint(6.5, 0.0, 10);
		DoublePoint point3 = new DoublePoint(4.8, 0.0, 13);
		List<DoublePoint> pointList = new ArrayList<DoublePoint>();
		pointList.add(point1);
		pointList.add(point2);
		pointList.add(point3);
		Calendar startSet = Calendar.getInstance();
		Calendar stopSet = Calendar.getInstance();
		startSet.setTimeInMillis(5);
		stopSet.setTimeInMillis(13);
		DataSet set = new DataSet(startSet, stopSet,
				new DeviceId("bla", "blub"), pointList);
		List<DataSet> setList = new ArrayList<DataSet>();
		setList.add(set);

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("interpolator is not initialized correctly");
		Classifier.compareAll(setList, 20, testInterpol);
	}

	@Test
	public void compareAll_interpolatorTimeNull() {
		double[] coefficents = new double[1];
		coefficents[0] = 5.0;
		PolynomialFunction[] function = new PolynomialFunction[1];
		function[0] = new PolynomialFunction(coefficents);
		double[] knots = new double[2];
		knots[0] = 10.0;
		knots[1] = 20.0;
		PolynomialSplineFunction spline = new PolynomialSplineFunction(knots,
				function);
	
		exception.expect(NullArgumentException.class);
		Interpolator testInterpol = new Interpolator(null, spline);

		// should not find a matching part in the function
		DoublePoint point1 = new DoublePoint(2.0, 0.0, 5);
		// should find matching part
		DoublePoint point2 = new DoublePoint(6.5, 0.0, 10);
		DoublePoint point3 = new DoublePoint(4.8, 0.0, 13);
		List<DoublePoint> pointList = new ArrayList<DoublePoint>();
		pointList.add(point1);
		pointList.add(point2);
		pointList.add(point3);
		Calendar startSet = Calendar.getInstance();
		Calendar stopSet = Calendar.getInstance();
		startSet.setTimeInMillis(5);
		stopSet.setTimeInMillis(13);
		DataSet set = new DataSet(startSet, stopSet,
				new DeviceId("bla", "blub"), pointList);
		List<DataSet> setList = new ArrayList<DataSet>();
		setList.add(set);
		
		Classifier.compareAll(setList, 20, testInterpol);
	}

	@Test
	public void compareAll_interpolatorNull() {
		Interpolator testInterpol = null;

		// should not find a matching part in the function
		DoublePoint point1 = new DoublePoint(2.0, 0.0, 5);
		// should find matching part
		DoublePoint point2 = new DoublePoint(6.5, 0.0, 10);
		DoublePoint point3 = new DoublePoint(4.8, 0.0, 13);
		List<DoublePoint> pointList = new ArrayList<DoublePoint>();
		pointList.add(point1);
		pointList.add(point2);
		pointList.add(point3);
		Calendar startSet = Calendar.getInstance();
		Calendar stopSet = Calendar.getInstance();
		startSet.setTimeInMillis(5);
		stopSet.setTimeInMillis(13);
		DataSet set = new DataSet(startSet, stopSet,
				new DeviceId("bla", "blub"), pointList);
		List<DataSet> setList = new ArrayList<DataSet>();
		setList.add(set);

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("interpolator is not initialized correctly");
		Classifier.compareAll(setList, 20, testInterpol);
	}

}
