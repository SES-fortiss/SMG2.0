package org.fortiss.smg.analyzer.test.pattern;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.fortiss.smg.analyzer.api.DataSet;
import org.fortiss.smg.analyzer.impl.pattern.Interpolator;
import org.fortiss.smg.analyzer.impl.pattern.Peak;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.informationbroker.api.DoublePoint;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TestPeak {

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void peak_validInput() {
		// building interpolator
		double[] coefficents = new double[1];
		coefficents[0] = 5.0;
		PolynomialFunction[] function = new PolynomialFunction[1];
		function[0] = new PolynomialFunction(coefficents);
		double[] knots = new double[2];
		knots[0] = 10.0;
		knots[1] = 100.0;
		PolynomialSplineFunction spline = new PolynomialSplineFunction(knots,
				function);
		Calendar functionStart = Calendar.getInstance();
		functionStart.setTimeInMillis(5);
		Interpolator testInterpol = new Interpolator(functionStart, spline);

		// building test data set
		// should not find a matching part in the function
		DoublePoint point1 = new DoublePoint(2.0, 0.0, 5);
		// should find matching part -> peak points
		DoublePoint point2 = new DoublePoint(2.5, 0.0, 10);
		DoublePoint point3 = new DoublePoint(3.8, 0.0, 13);
		DoublePoint point4 = new DoublePoint(5.6, 0.0, 16);
		DoublePoint point5 = new DoublePoint(8.3, 0.0, 21);
		// no peak point
		DoublePoint point6 = new DoublePoint(5.5, 0.0, 23);
		// peak point
		DoublePoint point7 = new DoublePoint(8.8, 0.0, 25);
		DoublePoint point8 = new DoublePoint(9.6, 0.0, 28);
		DoublePoint point9 = new DoublePoint(21.0, 0.0, 38);
		DoublePoint point10 = new DoublePoint(10.2, 0.0, 39);
		DoublePoint point11 = new DoublePoint(5.0, 0.0, 41);
		// no peak point
		DoublePoint point12 = new DoublePoint(5.5, 0.0, 43);
		DoublePoint point13 = new DoublePoint(2.3, 0.0, 46);
		DoublePoint point14 = new DoublePoint(5.9, 0.0, 49);
		DoublePoint point15 = new DoublePoint(2.6, 0.0, 50);
		DoublePoint point16 = new DoublePoint(3.2, 0.0, 52);
		List<DoublePoint> pointList = new ArrayList<DoublePoint>();
		pointList.add(point1);
		pointList.add(point2);
		pointList.add(point3);
		pointList.add(point4);
		pointList.add(point5);
		pointList.add(point6);
		pointList.add(point7);
		pointList.add(point8);
		pointList.add(point9);
		pointList.add(point10);
		pointList.add(point11);
		pointList.add(point12);
		pointList.add(point13);
		pointList.add(point14);
		pointList.add(point15);
		pointList.add(point16);
		Calendar startSet = Calendar.getInstance();
		Calendar stopSet = Calendar.getInstance();
		startSet.setTimeInMillis(5);
		stopSet.setTimeInMillis(60);
		DataSet set = new DataSet(startSet, stopSet,
				new DeviceId("bla", "blub"), pointList);

		List<DoublePoint> expected1 = new ArrayList<DoublePoint>();
		expected1.add(point2);
		expected1.add(point3);
		expected1.add(point5);
		List<DoublePoint> expected2 = new ArrayList<DoublePoint>();
		expected2.add(point7);
		expected2.add(point8);
		expected2.add(point9);
		expected2.add(point10);
		List<List<DoublePoint>> expected = new ArrayList<List<DoublePoint>>();
		expected.add(expected1);
		expected.add(expected2);

		assertEquals(expected, Peak.findPeaks(10, 20, 70, set, testInterpol));
	}

	@Test
	public void peak_timeNegative() {
		// building interpolator
		double[] coefficents = new double[1];
		coefficents[0] = 5.0;
		PolynomialFunction[] function = new PolynomialFunction[1];
		function[0] = new PolynomialFunction(coefficents);
		double[] knots = new double[2];
		knots[0] = 10.0;
		knots[1] = 100.0;
		PolynomialSplineFunction spline = new PolynomialSplineFunction(knots,
				function);
		Calendar functionStart = Calendar.getInstance();
		functionStart.setTimeInMillis(5);
		Interpolator testInterpol = new Interpolator(functionStart, spline);

		// building test data set
		// should not find a matching part in the function
		DoublePoint point1 = new DoublePoint(2.0, 0.0, 5);
		// should find matching part -> peak points
		DoublePoint point2 = new DoublePoint(2.5, 0.0, 10);
		DoublePoint point3 = new DoublePoint(3.8, 0.0, 13);
		DoublePoint point4 = new DoublePoint(5.6, 0.0, 16);
		DoublePoint point5 = new DoublePoint(8.3, 0.0, 21);
		// no peak point
		DoublePoint point6 = new DoublePoint(5.5, 0.0, 23);
		// peak point
		DoublePoint point7 = new DoublePoint(8.8, 0.0, 25);
		DoublePoint point8 = new DoublePoint(9.6, 0.0, 28);
		DoublePoint point9 = new DoublePoint(21.0, 0.0, 38);
		DoublePoint point10 = new DoublePoint(10.2, 0.0, 39);
		DoublePoint point11 = new DoublePoint(5.0, 0.0, 41);
		// no peak point
		DoublePoint point12 = new DoublePoint(5.5, 0.0, 43);
		DoublePoint point13 = new DoublePoint(2.3, 0.0, 46);
		DoublePoint point14 = new DoublePoint(5.9, 0.0, 49);
		DoublePoint point15 = new DoublePoint(2.6, 0.0, 50);
		DoublePoint point16 = new DoublePoint(3.2, 0.0, 52);
		List<DoublePoint> pointList = new ArrayList<DoublePoint>();
		pointList.add(point1);
		pointList.add(point2);
		pointList.add(point3);
		pointList.add(point4);
		pointList.add(point5);
		pointList.add(point6);
		pointList.add(point7);
		pointList.add(point8);
		pointList.add(point9);
		pointList.add(point10);
		pointList.add(point11);
		pointList.add(point12);
		pointList.add(point13);
		pointList.add(point14);
		pointList.add(point15);
		pointList.add(point16);
		Calendar startSet = Calendar.getInstance();
		Calendar stopSet = Calendar.getInstance();
		startSet.setTimeInMillis(5);
		stopSet.setTimeInMillis(60);
		DataSet set = new DataSet(startSet, stopSet,
				new DeviceId("bla", "blub"), pointList);

		exception.expect(IllegalArgumentException.class);
		Peak.findPeaks(-10, 20, 70, set, testInterpol);
	}

	@Test
	public void peak_timeZero() {
		// building interpolator
		double[] coefficents = new double[1];
		coefficents[0] = 5.0;
		PolynomialFunction[] function = new PolynomialFunction[1];
		function[0] = new PolynomialFunction(coefficents);
		double[] knots = new double[2];
		knots[0] = 10.0;
		knots[1] = 100.0;
		PolynomialSplineFunction spline = new PolynomialSplineFunction(knots,
				function);
		Calendar functionStart = Calendar.getInstance();
		functionStart.setTimeInMillis(5);
		Interpolator testInterpol = new Interpolator(functionStart, spline);

		// building test data set
		// should not find a matching part in the function
		DoublePoint point1 = new DoublePoint(2.0, 0.0, 5);
		// should find matching part -> peak points
		DoublePoint point2 = new DoublePoint(2.5, 0.0, 10);
		DoublePoint point3 = new DoublePoint(3.8, 0.0, 13);
		DoublePoint point4 = new DoublePoint(5.6, 0.0, 16);
		DoublePoint point5 = new DoublePoint(8.3, 0.0, 21);
		// no peak point
		DoublePoint point6 = new DoublePoint(5.5, 0.0, 23);
		// peak point
		DoublePoint point7 = new DoublePoint(8.8, 0.0, 25);
		DoublePoint point8 = new DoublePoint(9.6, 0.0, 28);
		DoublePoint point9 = new DoublePoint(21.0, 0.0, 38);
		DoublePoint point10 = new DoublePoint(10.2, 0.0, 39);
		DoublePoint point11 = new DoublePoint(5.0, 0.0, 41);
		// no peak point
		DoublePoint point12 = new DoublePoint(5.5, 0.0, 43);
		DoublePoint point13 = new DoublePoint(2.3, 0.0, 46);
		DoublePoint point14 = new DoublePoint(5.9, 0.0, 49);
		DoublePoint point15 = new DoublePoint(2.6, 0.0, 50);
		DoublePoint point16 = new DoublePoint(3.2, 0.0, 52);
		List<DoublePoint> pointList = new ArrayList<DoublePoint>();
		pointList.add(point1);
		pointList.add(point2);
		pointList.add(point3);
		pointList.add(point4);
		pointList.add(point5);
		pointList.add(point6);
		pointList.add(point7);
		pointList.add(point8);
		pointList.add(point9);
		pointList.add(point10);
		pointList.add(point11);
		pointList.add(point12);
		pointList.add(point13);
		pointList.add(point14);
		pointList.add(point15);
		pointList.add(point16);
		Calendar startSet = Calendar.getInstance();
		Calendar stopSet = Calendar.getInstance();
		startSet.setTimeInMillis(5);
		stopSet.setTimeInMillis(60);
		DataSet set = new DataSet(startSet, stopSet,
				new DeviceId("bla", "blub"), pointList);

		List<DoublePoint> expected1 = new ArrayList<DoublePoint>();
		expected1.add(point2);
		expected1.add(point3);
		List<DoublePoint> expected2 = new ArrayList<DoublePoint>();
		expected2.add(point5);
		List<DoublePoint> expected3 = new ArrayList<DoublePoint>();
		expected3.add(point7);
		expected3.add(point8);
		expected3.add(point9);
		expected3.add(point10);
		List<DoublePoint> expected4 = new ArrayList<DoublePoint>();
		expected4.add(point13);
		List<DoublePoint> expected5 = new ArrayList<DoublePoint>();
		expected5.add(point15);
		expected5.add(point16);
		List<List<DoublePoint>> expected = new ArrayList<List<DoublePoint>>();
		expected.add(expected1);
		expected.add(expected2);
		expected.add(expected3);
		expected.add(expected4);
		expected.add(expected5);

		assertEquals(expected, Peak.findPeaks(0, 20, 70, set, testInterpol));
	}

	@Test
	public void peak_timeExtrem() {
		// building interpolator
		double[] coefficents = new double[1];
		coefficents[0] = 5.0;
		PolynomialFunction[] function = new PolynomialFunction[1];
		function[0] = new PolynomialFunction(coefficents);
		double[] knots = new double[2];
		knots[0] = 10.0;
		knots[1] = 100.0;
		PolynomialSplineFunction spline = new PolynomialSplineFunction(knots,
				function);
		Calendar functionStart = Calendar.getInstance();
		functionStart.setTimeInMillis(5);
		Interpolator testInterpol = new Interpolator(functionStart, spline);

		// building test data set
		// should not find a matching part in the function
		DoublePoint point1 = new DoublePoint(2.0, 0.0, 5);
		// should find matching part -> peak points
		DoublePoint point2 = new DoublePoint(2.5, 0.0, 10);
		DoublePoint point3 = new DoublePoint(3.8, 0.0, 13);
		DoublePoint point4 = new DoublePoint(5.6, 0.0, 16);
		DoublePoint point5 = new DoublePoint(8.3, 0.0, 21);
		// no peak point
		DoublePoint point6 = new DoublePoint(5.5, 0.0, 23);
		// peak point
		DoublePoint point7 = new DoublePoint(8.8, 0.0, 25);
		DoublePoint point8 = new DoublePoint(9.6, 0.0, 28);
		DoublePoint point9 = new DoublePoint(21.0, 0.0, 38);
		DoublePoint point10 = new DoublePoint(10.2, 0.0, 39);
		DoublePoint point11 = new DoublePoint(5.0, 0.0, 41);
		// no peak point
		DoublePoint point12 = new DoublePoint(5.5, 0.0, 43);
		DoublePoint point13 = new DoublePoint(2.3, 0.0, 46);
		DoublePoint point14 = new DoublePoint(5.9, 0.0, 49);
		DoublePoint point15 = new DoublePoint(2.6, 0.0, 50);
		DoublePoint point16 = new DoublePoint(3.2, 0.0, 52);
		List<DoublePoint> pointList = new ArrayList<DoublePoint>();
		pointList.add(point1);
		pointList.add(point2);
		pointList.add(point3);
		pointList.add(point4);
		pointList.add(point5);
		pointList.add(point6);
		pointList.add(point7);
		pointList.add(point8);
		pointList.add(point9);
		pointList.add(point10);
		pointList.add(point11);
		pointList.add(point12);
		pointList.add(point13);
		pointList.add(point14);
		pointList.add(point15);
		pointList.add(point16);
		Calendar startSet = Calendar.getInstance();
		Calendar stopSet = Calendar.getInstance();
		startSet.setTimeInMillis(5);
		stopSet.setTimeInMillis(60);
		DataSet set = new DataSet(startSet, stopSet,
				new DeviceId("bla", "blub"), pointList);

		List<List<DoublePoint>> expected = new ArrayList<List<DoublePoint>>();

		assertEquals(expected,
				Peak.findPeaks(100000, 20, 70, set, testInterpol));
	}

	@Test
	public void peak_varianceNegative() {
		// building interpolator
		double[] coefficents = new double[1];
		coefficents[0] = 5.0;
		PolynomialFunction[] function = new PolynomialFunction[1];
		function[0] = new PolynomialFunction(coefficents);
		double[] knots = new double[2];
		knots[0] = 10.0;
		knots[1] = 100.0;
		PolynomialSplineFunction spline = new PolynomialSplineFunction(knots,
				function);
		Calendar functionStart = Calendar.getInstance();
		functionStart.setTimeInMillis(5);
		Interpolator testInterpol = new Interpolator(functionStart, spline);

		// building test data set
		// should not find a matching part in the function
		DoublePoint point1 = new DoublePoint(2.0, 0.0, 5);
		// should find matching part -> peak points
		DoublePoint point2 = new DoublePoint(2.5, 0.0, 10);
		DoublePoint point3 = new DoublePoint(3.8, 0.0, 13);
		DoublePoint point4 = new DoublePoint(5.6, 0.0, 16);
		DoublePoint point5 = new DoublePoint(8.3, 0.0, 21);
		// no peak point
		DoublePoint point6 = new DoublePoint(5.5, 0.0, 23);
		// peak point
		DoublePoint point7 = new DoublePoint(8.8, 0.0, 25);
		DoublePoint point8 = new DoublePoint(9.6, 0.0, 28);
		DoublePoint point9 = new DoublePoint(21.0, 0.0, 38);
		DoublePoint point10 = new DoublePoint(10.2, 0.0, 39);
		DoublePoint point11 = new DoublePoint(5.0, 0.0, 41);
		// no peak point
		DoublePoint point12 = new DoublePoint(5.5, 0.0, 43);
		DoublePoint point13 = new DoublePoint(2.3, 0.0, 46);
		DoublePoint point14 = new DoublePoint(5.9, 0.0, 49);
		DoublePoint point15 = new DoublePoint(2.6, 0.0, 50);
		DoublePoint point16 = new DoublePoint(3.2, 0.0, 52);
		List<DoublePoint> pointList = new ArrayList<DoublePoint>();
		pointList.add(point1);
		pointList.add(point2);
		pointList.add(point3);
		pointList.add(point4);
		pointList.add(point5);
		pointList.add(point6);
		pointList.add(point7);
		pointList.add(point8);
		pointList.add(point9);
		pointList.add(point10);
		pointList.add(point11);
		pointList.add(point12);
		pointList.add(point13);
		pointList.add(point14);
		pointList.add(point15);
		pointList.add(point16);
		Calendar startSet = Calendar.getInstance();
		Calendar stopSet = Calendar.getInstance();
		startSet.setTimeInMillis(5);
		stopSet.setTimeInMillis(60);
		DataSet set = new DataSet(startSet, stopSet,
				new DeviceId("bla", "blub"), pointList);

		exception.expect(IllegalArgumentException.class);
		exception
				.expectMessage("'timeInterval', 'minVariance' or 'minCongruency' not valid");
		Peak.findPeaks(10, -20, 70, set, testInterpol);
	}

	@Test
	public void peak_varianceExtremHigh() {
		// building interpolator
		double[] coefficents = new double[1];
		coefficents[0] = 5.0;
		PolynomialFunction[] function = new PolynomialFunction[1];
		function[0] = new PolynomialFunction(coefficents);
		double[] knots = new double[2];
		knots[0] = 10.0;
		knots[1] = 100.0;
		PolynomialSplineFunction spline = new PolynomialSplineFunction(knots,
				function);
		Calendar functionStart = Calendar.getInstance();
		functionStart.setTimeInMillis(5);
		Interpolator testInterpol = new Interpolator(functionStart, spline);

		// building test data set
		// should not find a matching part in the function
		DoublePoint point1 = new DoublePoint(2.0, 0.0, 5);
		// should find matching part -> peak points
		DoublePoint point2 = new DoublePoint(2.5, 0.0, 10);
		DoublePoint point3 = new DoublePoint(3.8, 0.0, 13);
		DoublePoint point4 = new DoublePoint(5.6, 0.0, 16);
		DoublePoint point5 = new DoublePoint(8.3, 0.0, 21);
		// no peak point
		DoublePoint point6 = new DoublePoint(5.5, 0.0, 23);
		// peak point
		DoublePoint point7 = new DoublePoint(8.8, 0.0, 25);
		DoublePoint point8 = new DoublePoint(9.6, 0.0, 28);
		DoublePoint point9 = new DoublePoint(21.0, 0.0, 38);
		DoublePoint point10 = new DoublePoint(10.2, 0.0, 39);
		DoublePoint point11 = new DoublePoint(5.0, 0.0, 41);
		// no peak point
		DoublePoint point12 = new DoublePoint(5.5, 0.0, 43);
		DoublePoint point13 = new DoublePoint(2.3, 0.0, 46);
		DoublePoint point14 = new DoublePoint(5.9, 0.0, 49);
		DoublePoint point15 = new DoublePoint(2.6, 0.0, 50);
		DoublePoint point16 = new DoublePoint(3.2, 0.0, 52);
		List<DoublePoint> pointList = new ArrayList<DoublePoint>();
		pointList.add(point1);
		pointList.add(point2);
		pointList.add(point3);
		pointList.add(point4);
		pointList.add(point5);
		pointList.add(point6);
		pointList.add(point7);
		pointList.add(point8);
		pointList.add(point9);
		pointList.add(point10);
		pointList.add(point11);
		pointList.add(point12);
		pointList.add(point13);
		pointList.add(point14);
		pointList.add(point15);
		pointList.add(point16);
		Calendar startSet = Calendar.getInstance();
		Calendar stopSet = Calendar.getInstance();
		startSet.setTimeInMillis(5);
		stopSet.setTimeInMillis(60);
		DataSet set = new DataSet(startSet, stopSet,
				new DeviceId("bla", "blub"), pointList);

		List<List<DoublePoint>> expected = new ArrayList<List<DoublePoint>>();

		assertEquals(expected, Peak.findPeaks(10, 2000, 70, set, testInterpol));
	}

	@Test
	public void peak_varianceZero() {
		// building interpolator
		double[] coefficents = new double[1];
		coefficents[0] = 5.0;
		PolynomialFunction[] function = new PolynomialFunction[1];
		function[0] = new PolynomialFunction(coefficents);
		double[] knots = new double[2];
		knots[0] = 10.0;
		knots[1] = 100.0;
		PolynomialSplineFunction spline = new PolynomialSplineFunction(knots,
				function);
		Calendar functionStart = Calendar.getInstance();
		functionStart.setTimeInMillis(5);
		Interpolator testInterpol = new Interpolator(functionStart, spline);

		// building test data set
		// should not find a matching part in the function
		DoublePoint point1 = new DoublePoint(2.0, 0.0, 5);
		// should find matching part -> peak points
		DoublePoint point2 = new DoublePoint(2.5, 0.0, 10);
		DoublePoint point3 = new DoublePoint(3.8, 0.0, 13);
		DoublePoint point4 = new DoublePoint(5.6, 0.0, 16);
		DoublePoint point5 = new DoublePoint(8.3, 0.0, 21);
		// no peak point
		DoublePoint point6 = new DoublePoint(5.5, 0.0, 23);
		// peak point
		DoublePoint point7 = new DoublePoint(8.8, 0.0, 25);
		DoublePoint point8 = new DoublePoint(9.6, 0.0, 28);
		DoublePoint point9 = new DoublePoint(21.0, 0.0, 38);
		DoublePoint point10 = new DoublePoint(10.2, 0.0, 39);
		DoublePoint point11 = new DoublePoint(5.0, 0.0, 41);
		// no peak point
		DoublePoint point12 = new DoublePoint(5.5, 0.0, 43);
		DoublePoint point13 = new DoublePoint(2.3, 0.0, 46);
		DoublePoint point14 = new DoublePoint(5.9, 0.0, 49);
		DoublePoint point15 = new DoublePoint(2.6, 0.0, 50);
		DoublePoint point16 = new DoublePoint(3.2, 0.0, 52);
		List<DoublePoint> pointList = new ArrayList<DoublePoint>();
		pointList.add(point1);
		pointList.add(point2);
		pointList.add(point3);
		pointList.add(point4);
		pointList.add(point5);
		pointList.add(point6);
		pointList.add(point7);
		pointList.add(point8);
		pointList.add(point9);
		pointList.add(point10);
		pointList.add(point11);
		pointList.add(point12);
		pointList.add(point13);
		pointList.add(point14);
		pointList.add(point15);
		pointList.add(point16);
		Calendar startSet = Calendar.getInstance();
		Calendar stopSet = Calendar.getInstance();
		startSet.setTimeInMillis(5);
		stopSet.setTimeInMillis(60);
		DataSet set = new DataSet(startSet, stopSet,
				new DeviceId("bla", "blub"), pointList);

		List<DoublePoint> expected1 = new ArrayList<DoublePoint>();
		expected1.add(point2);
		expected1.add(point3);
		expected1.add(point4);
		expected1.add(point5);
		expected1.add(point6);
		expected1.add(point7);
		expected1.add(point8);
		expected1.add(point9);
		expected1.add(point10);
		expected1.add(point11);
		expected1.add(point12);
		expected1.add(point13);
		expected1.add(point14);
		expected1.add(point15);
		expected1.add(point16);
		List<List<DoublePoint>> expected = new ArrayList<List<DoublePoint>>();
		expected.add(expected1);

		assertEquals(expected, Peak.findPeaks(10, 0, 70, set, testInterpol));
	}

	@Test
	public void peak_congruencyNegative() {
		// building interpolator
		double[] coefficents = new double[1];
		coefficents[0] = 5.0;
		PolynomialFunction[] function = new PolynomialFunction[1];
		function[0] = new PolynomialFunction(coefficents);
		double[] knots = new double[2];
		knots[0] = 10.0;
		knots[1] = 100.0;
		PolynomialSplineFunction spline = new PolynomialSplineFunction(knots,
				function);
		Calendar functionStart = Calendar.getInstance();
		functionStart.setTimeInMillis(5);
		Interpolator testInterpol = new Interpolator(functionStart, spline);

		// building test data set
		// should not find a matching part in the function
		DoublePoint point1 = new DoublePoint(2.0, 0.0, 5);
		// should find matching part -> peak points
		DoublePoint point2 = new DoublePoint(2.5, 0.0, 10);
		DoublePoint point3 = new DoublePoint(3.8, 0.0, 13);
		DoublePoint point4 = new DoublePoint(5.6, 0.0, 16);
		DoublePoint point5 = new DoublePoint(8.3, 0.0, 21);
		// no peak point
		DoublePoint point6 = new DoublePoint(5.5, 0.0, 23);
		// peak point
		DoublePoint point7 = new DoublePoint(8.8, 0.0, 25);
		DoublePoint point8 = new DoublePoint(9.6, 0.0, 28);
		DoublePoint point9 = new DoublePoint(21.0, 0.0, 38);
		DoublePoint point10 = new DoublePoint(10.2, 0.0, 39);
		DoublePoint point11 = new DoublePoint(5.0, 0.0, 41);
		// no peak point
		DoublePoint point12 = new DoublePoint(5.5, 0.0, 43);
		DoublePoint point13 = new DoublePoint(2.3, 0.0, 46);
		DoublePoint point14 = new DoublePoint(5.9, 0.0, 49);
		DoublePoint point15 = new DoublePoint(2.6, 0.0, 50);
		DoublePoint point16 = new DoublePoint(3.2, 0.0, 52);
		List<DoublePoint> pointList = new ArrayList<DoublePoint>();
		pointList.add(point1);
		pointList.add(point2);
		pointList.add(point3);
		pointList.add(point4);
		pointList.add(point5);
		pointList.add(point6);
		pointList.add(point7);
		pointList.add(point8);
		pointList.add(point9);
		pointList.add(point10);
		pointList.add(point11);
		pointList.add(point12);
		pointList.add(point13);
		pointList.add(point14);
		pointList.add(point15);
		pointList.add(point16);
		Calendar startSet = Calendar.getInstance();
		Calendar stopSet = Calendar.getInstance();
		startSet.setTimeInMillis(5);
		stopSet.setTimeInMillis(60);
		DataSet set = new DataSet(startSet, stopSet,
				new DeviceId("bla", "blub"), pointList);

		exception.expect(IllegalArgumentException.class);
		exception
				.expectMessage("'timeInterval', 'minVariance' or 'minCongruency' not valid");
		Peak.findPeaks(10, 20, -70, set, testInterpol);
	}

	@Test
	public void peak_congruencyOver100() {
		// building interpolator
		double[] coefficents = new double[1];
		coefficents[0] = 5.0;
		PolynomialFunction[] function = new PolynomialFunction[1];
		function[0] = new PolynomialFunction(coefficents);
		double[] knots = new double[2];
		knots[0] = 10.0;
		knots[1] = 100.0;
		PolynomialSplineFunction spline = new PolynomialSplineFunction(knots,
				function);
		Calendar functionStart = Calendar.getInstance();
		functionStart.setTimeInMillis(5);
		Interpolator testInterpol = new Interpolator(functionStart, spline);

		// building test data set
		// should not find a matching part in the function
		DoublePoint point1 = new DoublePoint(2.0, 0.0, 5);
		// should find matching part -> peak points
		DoublePoint point2 = new DoublePoint(2.5, 0.0, 10);
		DoublePoint point3 = new DoublePoint(3.8, 0.0, 13);
		DoublePoint point4 = new DoublePoint(5.6, 0.0, 16);
		DoublePoint point5 = new DoublePoint(8.3, 0.0, 21);
		// no peak point
		DoublePoint point6 = new DoublePoint(5.5, 0.0, 23);
		// peak point
		DoublePoint point7 = new DoublePoint(8.8, 0.0, 25);
		DoublePoint point8 = new DoublePoint(9.6, 0.0, 28);
		DoublePoint point9 = new DoublePoint(21.0, 0.0, 38);
		DoublePoint point10 = new DoublePoint(10.2, 0.0, 39);
		DoublePoint point11 = new DoublePoint(5.0, 0.0, 41);
		// no peak point
		DoublePoint point12 = new DoublePoint(5.5, 0.0, 43);
		DoublePoint point13 = new DoublePoint(2.3, 0.0, 46);
		DoublePoint point14 = new DoublePoint(5.9, 0.0, 49);
		DoublePoint point15 = new DoublePoint(2.6, 0.0, 50);
		DoublePoint point16 = new DoublePoint(3.2, 0.0, 52);
		List<DoublePoint> pointList = new ArrayList<DoublePoint>();
		pointList.add(point1);
		pointList.add(point2);
		pointList.add(point3);
		pointList.add(point4);
		pointList.add(point5);
		pointList.add(point6);
		pointList.add(point7);
		pointList.add(point8);
		pointList.add(point9);
		pointList.add(point10);
		pointList.add(point11);
		pointList.add(point12);
		pointList.add(point13);
		pointList.add(point14);
		pointList.add(point15);
		pointList.add(point16);
		Calendar startSet = Calendar.getInstance();
		Calendar stopSet = Calendar.getInstance();
		startSet.setTimeInMillis(5);
		stopSet.setTimeInMillis(60);
		DataSet set = new DataSet(startSet, stopSet,
				new DeviceId("bla", "blub"), pointList);

		exception.expect(IllegalArgumentException.class);
		exception
				.expectMessage("'timeInterval', 'minVariance' or 'minCongruency' not valid");
		Peak.findPeaks(10, 20, 700, set, testInterpol);
	}

	@Test
	public void peak_congruencyZero() {
		// building interpolator
		double[] coefficents = new double[1];
		coefficents[0] = 5.0;
		PolynomialFunction[] function = new PolynomialFunction[1];
		function[0] = new PolynomialFunction(coefficents);
		double[] knots = new double[2];
		knots[0] = 10.0;
		knots[1] = 100.0;
		PolynomialSplineFunction spline = new PolynomialSplineFunction(knots,
				function);
		Calendar functionStart = Calendar.getInstance();
		functionStart.setTimeInMillis(5);
		Interpolator testInterpol = new Interpolator(functionStart, spline);

		// building test data set
		// should not find a matching part in the function
		DoublePoint point1 = new DoublePoint(2.0, 0.0, 5);
		// should find matching part -> peak points
		DoublePoint point2 = new DoublePoint(2.5, 0.0, 10);
		DoublePoint point3 = new DoublePoint(3.8, 0.0, 13);
		DoublePoint point4 = new DoublePoint(5.6, 0.0, 16);
		DoublePoint point5 = new DoublePoint(8.3, 0.0, 21);
		// no peak point
		DoublePoint point6 = new DoublePoint(5.5, 0.0, 23);
		// peak point
		DoublePoint point7 = new DoublePoint(8.8, 0.0, 25);
		DoublePoint point8 = new DoublePoint(9.6, 0.0, 28);
		DoublePoint point9 = new DoublePoint(21.0, 0.0, 38);
		DoublePoint point10 = new DoublePoint(10.2, 0.0, 39);
		DoublePoint point11 = new DoublePoint(5.0, 0.0, 41);
		// no peak point
		DoublePoint point12 = new DoublePoint(5.5, 0.0, 43);
		DoublePoint point13 = new DoublePoint(2.3, 0.0, 46);
		DoublePoint point14 = new DoublePoint(5.9, 0.0, 49);
		DoublePoint point15 = new DoublePoint(2.6, 0.0, 50);
		DoublePoint point16 = new DoublePoint(3.2, 0.0, 52);
		List<DoublePoint> pointList = new ArrayList<DoublePoint>();
		pointList.add(point1);
		pointList.add(point2);
		pointList.add(point3);
		pointList.add(point4);
		pointList.add(point5);
		pointList.add(point6);
		pointList.add(point7);
		pointList.add(point8);
		pointList.add(point9);
		pointList.add(point10);
		pointList.add(point11);
		pointList.add(point12);
		pointList.add(point13);
		pointList.add(point14);
		pointList.add(point15);
		pointList.add(point16);
		Calendar startSet = Calendar.getInstance();
		Calendar stopSet = Calendar.getInstance();
		startSet.setTimeInMillis(5);
		stopSet.setTimeInMillis(60);
		DataSet set = new DataSet(startSet, stopSet,
				new DeviceId("bla", "blub"), pointList);

		List<DoublePoint> expected1 = new ArrayList<DoublePoint>();
		expected1.add(point2);
		expected1.add(point3);
		expected1.add(point5);
		expected1.add(point7);
		expected1.add(point8);
		expected1.add(point9);
		expected1.add(point10);
		expected1.add(point13);
		expected1.add(point15);
		expected1.add(point16);
		List<List<DoublePoint>> expected = new ArrayList<List<DoublePoint>>();
		expected.add(expected1);

		assertEquals(expected, Peak.findPeaks(10, 20, 0, set, testInterpol));
	}

	@Test
	public void peak_congruency100() {
		// building interpolator
		double[] coefficents = new double[1];
		coefficents[0] = 5.0;
		PolynomialFunction[] function = new PolynomialFunction[1];
		function[0] = new PolynomialFunction(coefficents);
		double[] knots = new double[2];
		knots[0] = 10.0;
		knots[1] = 100.0;
		PolynomialSplineFunction spline = new PolynomialSplineFunction(knots,
				function);
		Calendar functionStart = Calendar.getInstance();
		functionStart.setTimeInMillis(5);
		Interpolator testInterpol = new Interpolator(functionStart, spline);

		// building test data set
		// should not find a matching part in the function
		DoublePoint point1 = new DoublePoint(2.0, 0.0, 5);
		// should find matching part -> peak points
		DoublePoint point2 = new DoublePoint(2.5, 0.0, 10);
		DoublePoint point3 = new DoublePoint(3.8, 0.0, 13);
		DoublePoint point4 = new DoublePoint(5.6, 0.0, 16);
		DoublePoint point5 = new DoublePoint(8.3, 0.0, 21);
		// no peak point
		DoublePoint point6 = new DoublePoint(5.5, 0.0, 23);
		// peak point
		DoublePoint point7 = new DoublePoint(8.8, 0.0, 25);
		DoublePoint point8 = new DoublePoint(9.6, 0.0, 28);
		DoublePoint point9 = new DoublePoint(21.0, 0.0, 38);
		DoublePoint point10 = new DoublePoint(10.2, 0.0, 39);
		DoublePoint point11 = new DoublePoint(5.0, 0.0, 41);
		// no peak point
		DoublePoint point12 = new DoublePoint(5.5, 0.0, 43);
		DoublePoint point13 = new DoublePoint(2.3, 0.0, 46);
		DoublePoint point14 = new DoublePoint(5.9, 0.0, 49);
		DoublePoint point15 = new DoublePoint(2.6, 0.0, 50);
		DoublePoint point16 = new DoublePoint(3.2, 0.0, 52);
		List<DoublePoint> pointList = new ArrayList<DoublePoint>();
		pointList.add(point1);
		pointList.add(point2);
		pointList.add(point3);
		pointList.add(point4);
		pointList.add(point5);
		pointList.add(point6);
		pointList.add(point7);
		pointList.add(point8);
		pointList.add(point9);
		pointList.add(point10);
		pointList.add(point11);
		pointList.add(point12);
		pointList.add(point13);
		pointList.add(point14);
		pointList.add(point15);
		pointList.add(point16);
		Calendar startSet = Calendar.getInstance();
		Calendar stopSet = Calendar.getInstance();
		startSet.setTimeInMillis(5);
		stopSet.setTimeInMillis(60);
		DataSet set = new DataSet(startSet, stopSet,
				new DeviceId("bla", "blub"), pointList);

		List<DoublePoint> expected2 = new ArrayList<DoublePoint>();
		expected2.add(point7);
		expected2.add(point8);
		expected2.add(point9);
		expected2.add(point10);
		List<List<DoublePoint>> expected = new ArrayList<List<DoublePoint>>();
		expected.add(expected2);

		assertEquals(expected, Peak.findPeaks(10, 20, 100, set, testInterpol));
	}

	@Test
	public void peak_dataListEmpty() {
		// building interpolator
		double[] coefficents = new double[1];
		coefficents[0] = 5.0;
		PolynomialFunction[] function = new PolynomialFunction[1];
		function[0] = new PolynomialFunction(coefficents);
		double[] knots = new double[2];
		knots[0] = 10.0;
		knots[1] = 100.0;
		PolynomialSplineFunction spline = new PolynomialSplineFunction(knots,
				function);
		Calendar functionStart = Calendar.getInstance();
		functionStart.setTimeInMillis(5);
		Interpolator testInterpol = new Interpolator(functionStart, spline);

		List<DoublePoint> pointList = new ArrayList<DoublePoint>();
		Calendar startSet = Calendar.getInstance();
		Calendar stopSet = Calendar.getInstance();
		startSet.setTimeInMillis(5);
		stopSet.setTimeInMillis(60);
		DataSet set = new DataSet(startSet, stopSet,
				new DeviceId("bla", "blub"), pointList);

		exception.expect(IllegalArgumentException.class);
		exception
				.expectMessage("no data points found in data set - please fetch first");
		Peak.findPeaks(10, 20, 70, set, testInterpol);
	}

	@Test
	public void peak_dataListNull() {
		// building interpolator
		double[] coefficents = new double[1];
		coefficents[0] = 5.0;
		PolynomialFunction[] function = new PolynomialFunction[1];
		function[0] = new PolynomialFunction(coefficents);
		double[] knots = new double[2];
		knots[0] = 10.0;
		knots[1] = 100.0;
		PolynomialSplineFunction spline = new PolynomialSplineFunction(knots,
				function);
		Calendar functionStart = Calendar.getInstance();
		functionStart.setTimeInMillis(5);
		Interpolator testInterpol = new Interpolator(functionStart, spline);

		Calendar startSet = Calendar.getInstance();
		Calendar stopSet = Calendar.getInstance();
		startSet.setTimeInMillis(5);
		stopSet.setTimeInMillis(60);
		DataSet set = new DataSet(startSet, stopSet,
				new DeviceId("bla", "blub"), null);

		exception.expect(IllegalArgumentException.class);
		exception
				.expectMessage("no data points found in data set - please fetch first");
		Peak.findPeaks(10, 20, 70, set, testInterpol);
	}

	@Test
	public void peak_dataSetNull() {
		// building interpolator
		double[] coefficents = new double[1];
		coefficents[0] = 5.0;
		PolynomialFunction[] function = new PolynomialFunction[1];
		function[0] = new PolynomialFunction(coefficents);
		double[] knots = new double[2];
		knots[0] = 10.0;
		knots[1] = 100.0;
		PolynomialSplineFunction spline = new PolynomialSplineFunction(knots,
				function);
		Calendar functionStart = Calendar.getInstance();
		functionStart.setTimeInMillis(5);
		Interpolator testInterpol = new Interpolator(functionStart, spline);

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("data set is null");
		Peak.findPeaks(10, 20, 70, null, testInterpol);
	}

	@Test
	public void peak_dataSetTimeMissmatch() {
		// building interpolator
		double[] coefficents = new double[1];
		coefficents[0] = 5.0;
		PolynomialFunction[] function = new PolynomialFunction[1];
		function[0] = new PolynomialFunction(coefficents);
		double[] knots = new double[2];
		knots[0] = 100.0;
		knots[1] = 200.0;
		PolynomialSplineFunction spline = new PolynomialSplineFunction(knots,
				function);
		Calendar functionStart = Calendar.getInstance();
		functionStart.setTimeInMillis(5);
		Interpolator testInterpol = new Interpolator(functionStart, spline);

		// building test data set
		// should not find a matching part in the function
		DoublePoint point1 = new DoublePoint(2.0, 0.0, 5);
		// should find matching part -> peak points
		DoublePoint point2 = new DoublePoint(2.5, 0.0, 10);
		DoublePoint point3 = new DoublePoint(3.8, 0.0, 13);
		DoublePoint point4 = new DoublePoint(5.6, 0.0, 16);
		DoublePoint point5 = new DoublePoint(8.3, 0.0, 21);
		// no peak point
		DoublePoint point6 = new DoublePoint(5.5, 0.0, 23);
		// peak point
		DoublePoint point7 = new DoublePoint(8.8, 0.0, 25);
		DoublePoint point8 = new DoublePoint(9.6, 0.0, 28);
		DoublePoint point9 = new DoublePoint(21.0, 0.0, 38);
		DoublePoint point10 = new DoublePoint(10.2, 0.0, 39);
		DoublePoint point11 = new DoublePoint(5.0, 0.0, 41);
		// no peak point
		DoublePoint point12 = new DoublePoint(5.5, 0.0, 43);
		DoublePoint point13 = new DoublePoint(2.3, 0.0, 46);
		DoublePoint point14 = new DoublePoint(5.9, 0.0, 49);
		DoublePoint point15 = new DoublePoint(2.6, 0.0, 50);
		DoublePoint point16 = new DoublePoint(3.2, 0.0, 52);
		List<DoublePoint> pointList = new ArrayList<DoublePoint>();
		pointList.add(point1);
		pointList.add(point2);
		pointList.add(point3);
		pointList.add(point4);
		pointList.add(point5);
		pointList.add(point6);
		pointList.add(point7);
		pointList.add(point8);
		pointList.add(point9);
		pointList.add(point10);
		pointList.add(point11);
		pointList.add(point12);
		pointList.add(point13);
		pointList.add(point14);
		pointList.add(point15);
		pointList.add(point16);
		Calendar startSet = Calendar.getInstance();
		Calendar stopSet = Calendar.getInstance();
		startSet.setTimeInMillis(5);
		stopSet.setTimeInMillis(60);
		DataSet set = new DataSet(startSet, stopSet,
				new DeviceId("bla", "blub"), pointList);

		List<List<DoublePoint>> expected = new ArrayList<List<DoublePoint>>();

		assertEquals(expected, Peak.findPeaks(10, 20, 70, set, testInterpol));
	}

	@Test
	public void peak_interpolatorSplineNull() {
		Calendar functionStart = Calendar.getInstance();
		functionStart.setTimeInMillis(5);
		Interpolator testInterpol = new Interpolator(functionStart, null);

		// building test data set
		// should not find a matching part in the function
		DoublePoint point1 = new DoublePoint(2.0, 0.0, 5);
		// should find matching part -> peak points
		DoublePoint point2 = new DoublePoint(2.5, 0.0, 10);
		DoublePoint point3 = new DoublePoint(3.8, 0.0, 13);
		DoublePoint point4 = new DoublePoint(5.6, 0.0, 16);
		DoublePoint point5 = new DoublePoint(8.3, 0.0, 21);
		// no peak point
		DoublePoint point6 = new DoublePoint(5.5, 0.0, 23);
		// peak point
		DoublePoint point7 = new DoublePoint(8.8, 0.0, 25);
		DoublePoint point8 = new DoublePoint(9.6, 0.0, 28);
		DoublePoint point9 = new DoublePoint(21.0, 0.0, 38);
		DoublePoint point10 = new DoublePoint(10.2, 0.0, 39);
		DoublePoint point11 = new DoublePoint(5.0, 0.0, 41);
		// no peak point
		DoublePoint point12 = new DoublePoint(5.5, 0.0, 43);
		DoublePoint point13 = new DoublePoint(2.3, 0.0, 46);
		DoublePoint point14 = new DoublePoint(5.9, 0.0, 49);
		DoublePoint point15 = new DoublePoint(2.6, 0.0, 50);
		DoublePoint point16 = new DoublePoint(3.2, 0.0, 52);
		List<DoublePoint> pointList = new ArrayList<DoublePoint>();
		pointList.add(point1);
		pointList.add(point2);
		pointList.add(point3);
		pointList.add(point4);
		pointList.add(point5);
		pointList.add(point6);
		pointList.add(point7);
		pointList.add(point8);
		pointList.add(point9);
		pointList.add(point10);
		pointList.add(point11);
		pointList.add(point12);
		pointList.add(point13);
		pointList.add(point14);
		pointList.add(point15);
		pointList.add(point16);
		Calendar startSet = Calendar.getInstance();
		Calendar stopSet = Calendar.getInstance();
		startSet.setTimeInMillis(5);
		stopSet.setTimeInMillis(60);
		DataSet set = new DataSet(startSet, stopSet,
				new DeviceId("bla", "blub"), pointList);

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("interpolator is not initialized correctly");
		Peak.findPeaks(10, 20, 70, set, testInterpol);
	}

	@Test
	public void peak_interpolatorTimeNull() {
		// building interpolator
		double[] coefficents = new double[1];
		coefficents[0] = 5.0;
		PolynomialFunction[] function = new PolynomialFunction[1];
		function[0] = new PolynomialFunction(coefficents);
		double[] knots = new double[2];
		knots[0] = 10.0;
		knots[1] = 100.0;
		PolynomialSplineFunction spline = new PolynomialSplineFunction(knots,
				function);
		exception.expect(NullArgumentException.class);
		Interpolator testInterpol = new Interpolator(null, spline);

		// building test data set
		// should not find a matching part in the function
		DoublePoint point1 = new DoublePoint(2.0, 0.0, 5);
		// should find matching part -> peak points
		DoublePoint point2 = new DoublePoint(2.5, 0.0, 10);
		DoublePoint point3 = new DoublePoint(3.8, 0.0, 13);
		DoublePoint point4 = new DoublePoint(5.6, 0.0, 16);
		DoublePoint point5 = new DoublePoint(8.3, 0.0, 21);
		// no peak point
		DoublePoint point6 = new DoublePoint(5.5, 0.0, 23);
		// peak point
		DoublePoint point7 = new DoublePoint(8.8, 0.0, 25);
		DoublePoint point8 = new DoublePoint(9.6, 0.0, 28);
		DoublePoint point9 = new DoublePoint(21.0, 0.0, 38);
		DoublePoint point10 = new DoublePoint(10.2, 0.0, 39);
		DoublePoint point11 = new DoublePoint(5.0, 0.0, 41);
		// no peak point
		DoublePoint point12 = new DoublePoint(5.5, 0.0, 43);
		DoublePoint point13 = new DoublePoint(2.3, 0.0, 46);
		DoublePoint point14 = new DoublePoint(5.9, 0.0, 49);
		DoublePoint point15 = new DoublePoint(2.6, 0.0, 50);
		DoublePoint point16 = new DoublePoint(3.2, 0.0, 52);
		List<DoublePoint> pointList = new ArrayList<DoublePoint>();
		pointList.add(point1);
		pointList.add(point2);
		pointList.add(point3);
		pointList.add(point4);
		pointList.add(point5);
		pointList.add(point6);
		pointList.add(point7);
		pointList.add(point8);
		pointList.add(point9);
		pointList.add(point10);
		pointList.add(point11);
		pointList.add(point12);
		pointList.add(point13);
		pointList.add(point14);
		pointList.add(point15);
		pointList.add(point16);
		Calendar startSet = Calendar.getInstance();
		Calendar stopSet = Calendar.getInstance();
		startSet.setTimeInMillis(5);
		stopSet.setTimeInMillis(60);
		DataSet set = new DataSet(startSet, stopSet,
				new DeviceId("bla", "blub"), pointList);

		Peak.findPeaks(10, 20, 70, set, testInterpol);
	}

	@Test
	public void peak_interpolatorTimeNegative() {
		// building interpolator
		double[] coefficents = new double[1];
		coefficents[0] = 5.0;
		PolynomialFunction[] function = new PolynomialFunction[1];
		function[0] = new PolynomialFunction(coefficents);
		double[] knots = new double[2];
		knots[0] = 10.0;
		knots[1] = 100.0;
		PolynomialSplineFunction spline = new PolynomialSplineFunction(knots,
				function);
		Calendar functionStart = Calendar.getInstance();
		functionStart.setTimeInMillis(-5);
		Interpolator testInterpol = new Interpolator(functionStart, spline);

		// building test data set
		// should not find a matching part in the function
		DoublePoint point1 = new DoublePoint(2.0, 0.0, 5);
		// should find matching part -> peak points
		DoublePoint point2 = new DoublePoint(2.5, 0.0, 10);
		DoublePoint point3 = new DoublePoint(3.8, 0.0, 13);
		DoublePoint point4 = new DoublePoint(5.6, 0.0, 16);
		DoublePoint point5 = new DoublePoint(8.3, 0.0, 21);
		// no peak point
		DoublePoint point6 = new DoublePoint(5.5, 0.0, 23);
		// peak point
		DoublePoint point7 = new DoublePoint(8.8, 0.0, 25);
		DoublePoint point8 = new DoublePoint(9.6, 0.0, 28);
		DoublePoint point9 = new DoublePoint(21.0, 0.0, 38);
		DoublePoint point10 = new DoublePoint(10.2, 0.0, 39);
		DoublePoint point11 = new DoublePoint(5.0, 0.0, 41);
		// no peak point
		DoublePoint point12 = new DoublePoint(5.5, 0.0, 43);
		DoublePoint point13 = new DoublePoint(2.3, 0.0, 46);
		DoublePoint point14 = new DoublePoint(5.9, 0.0, 49);
		DoublePoint point15 = new DoublePoint(2.6, 0.0, 50);
		DoublePoint point16 = new DoublePoint(3.2, 0.0, 52);
		List<DoublePoint> pointList = new ArrayList<DoublePoint>();
		pointList.add(point1);
		pointList.add(point2);
		pointList.add(point3);
		pointList.add(point4);
		pointList.add(point5);
		pointList.add(point6);
		pointList.add(point7);
		pointList.add(point8);
		pointList.add(point9);
		pointList.add(point10);
		pointList.add(point11);
		pointList.add(point12);
		pointList.add(point13);
		pointList.add(point14);
		pointList.add(point15);
		pointList.add(point16);
		Calendar startSet = Calendar.getInstance();
		Calendar stopSet = Calendar.getInstance();
		startSet.setTimeInMillis(5);
		stopSet.setTimeInMillis(60);
		DataSet set = new DataSet(startSet, stopSet,
				new DeviceId("bla", "blub"), pointList);

		List<DoublePoint> expected1 = new ArrayList<DoublePoint>();
		expected1.add(point5);
		expected1.add(point7);
		expected1.add(point8);
		expected1.add(point9);
		expected1.add(point10);
		List<List<DoublePoint>> expected = new ArrayList<List<DoublePoint>>();
		expected.add(expected1);

		assertEquals(expected, Peak.findPeaks(10, 20, 70, set, testInterpol));
	}

	@Test
	public void peak_interpolatorNull() {
		Interpolator testInterpol = null;

		// building test data set
		// should not find a matching part in the function
		DoublePoint point1 = new DoublePoint(2.0, 0.0, 5);
		// should find matching part -> peak points
		DoublePoint point2 = new DoublePoint(2.5, 0.0, 10);
		DoublePoint point3 = new DoublePoint(3.8, 0.0, 13);
		DoublePoint point4 = new DoublePoint(5.6, 0.0, 16);
		DoublePoint point5 = new DoublePoint(8.3, 0.0, 21);
		// no peak point
		DoublePoint point6 = new DoublePoint(5.5, 0.0, 23);
		// peak point
		DoublePoint point7 = new DoublePoint(8.8, 0.0, 25);
		DoublePoint point8 = new DoublePoint(9.6, 0.0, 28);
		DoublePoint point9 = new DoublePoint(21.0, 0.0, 38);
		DoublePoint point10 = new DoublePoint(10.2, 0.0, 39);
		DoublePoint point11 = new DoublePoint(5.0, 0.0, 41);
		// no peak point
		DoublePoint point12 = new DoublePoint(5.5, 0.0, 43);
		DoublePoint point13 = new DoublePoint(2.3, 0.0, 46);
		DoublePoint point14 = new DoublePoint(5.9, 0.0, 49);
		DoublePoint point15 = new DoublePoint(2.6, 0.0, 50);
		DoublePoint point16 = new DoublePoint(3.2, 0.0, 52);
		List<DoublePoint> pointList = new ArrayList<DoublePoint>();
		pointList.add(point1);
		pointList.add(point2);
		pointList.add(point3);
		pointList.add(point4);
		pointList.add(point5);
		pointList.add(point6);
		pointList.add(point7);
		pointList.add(point8);
		pointList.add(point9);
		pointList.add(point10);
		pointList.add(point11);
		pointList.add(point12);
		pointList.add(point13);
		pointList.add(point14);
		pointList.add(point15);
		pointList.add(point16);
		Calendar startSet = Calendar.getInstance();
		Calendar stopSet = Calendar.getInstance();
		startSet.setTimeInMillis(5);
		stopSet.setTimeInMillis(60);
		DataSet set = new DataSet(startSet, stopSet,
				new DeviceId("bla", "blub"), pointList);

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("interpolator is not initialized correctly");
		Peak.findPeaks(10, 20, 70, set, testInterpol);
	}
}
