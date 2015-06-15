/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.analyzer.test.calculations;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.fortiss.smg.analyzer.impl.calculations.dispersion.BlockPlotter;
import org.fortiss.smg.analyzer.impl.calculations.dispersion.PeakValues;
import org.fortiss.smg.analyzer.impl.calculations.dispersion.Quantile;
import org.fortiss.smg.analyzer.impl.calculations.dispersion.VarianceAndStandardDeviation;
import org.fortiss.smg.informationbroker.api.DoublePoint;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TestDispersion {

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void Maximum_Normal() {
		List<DoublePoint> test = new ArrayList<DoublePoint>();
		test.add(new DoublePoint(1.9, 0, 0));
		test.add(new DoublePoint(2.3, 0, 0));
		test.add(new DoublePoint(6.0, 0, 0));
		test.add(new DoublePoint(null, 0, 0));
		test.add(new DoublePoint(4.6, 0, 0));
		test.add(new DoublePoint(2.3456, 0, 0));
		test.add(new DoublePoint(1.9321, 0, 0));
		test.add(new DoublePoint(1.9876456, 0, 0));
		test.add(new DoublePoint(4.6, 0, 0));
		test.add(new DoublePoint(4.6, 0, 0));
		test.add(new DoublePoint(6.19266378, 0, 0));
		test.add(new DoublePoint(6.19266378, 0, 0));
		DoublePoint sol = PeakValues.maximum(test);
		assertEquals(6.19266378, sol.getValue(), 0.00000001);
	}

	@Test
	public void Maximum_Empty() {
		List<DoublePoint> test = new ArrayList<DoublePoint>();
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("list is null or empty");
		PeakValues.maximum(test);
	}

	@Test
	public void Maximum_Null() {
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("list is null or empty");
		PeakValues.maximum(null);
	}

	@Test
	public void Minimum_Normal() {
		List<DoublePoint> test = new ArrayList<DoublePoint>();
		test.add(new DoublePoint(1.9, 0, 0));
		test.add(new DoublePoint(2.3, 0, 0));
		test.add(new DoublePoint(6.0, 0, 0));
		test.add(new DoublePoint(3.7, 0, 0));
		test.add(new DoublePoint(null, 0, 0));
		test.add(new DoublePoint(2.3456, 0, 0));
		test.add(new DoublePoint(1.9321, 0, 0));
		test.add(new DoublePoint(1.9876456, 0, 0));
		test.add(new DoublePoint(4.6, 0, 0));
		test.add(new DoublePoint(4.6, 0, 0));
		test.add(new DoublePoint(6.19266378, 0, 0));
		test.add(new DoublePoint(6.19266378, 0, 0));
		test.add(new DoublePoint(-6.19266378, 0, 0));
		DoublePoint sol = PeakValues.minimum(test);
		assertEquals(-6.19266378, sol.getValue(), 0.00000001);
	}

	@Test
	public void Minimum_Empty() {
		List<DoublePoint> test = new ArrayList<DoublePoint>();
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("list is null or empty");
		PeakValues.minimum(test);
	}

	@Test
	public void Minimum_Null() {
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("list is null or empty");
		PeakValues.minimum(null);
	}

	@Test
	public void Quantile_SingleQuantile() {
		List<DoublePoint> test = new ArrayList<DoublePoint>();
		test.add(new DoublePoint(null, 0, 0));
		test.add(new DoublePoint(79.1522269, 0, 0));
		test.add(new DoublePoint(-66.349732, 0, 0));
		test.add(new DoublePoint(41.74220715, 0, 0));
		test.add(new DoublePoint(-63.94046437, 0, 0));
		test.add(new DoublePoint(61.96682694, 0, 0));
		test.add(new DoublePoint(1.15543206, 0, 0));
		test.add(new DoublePoint(-12.31966315, 0, 0));
		test.add(new DoublePoint(-21.23892336, 0, 0));
		test.add(new DoublePoint(65.73601001, 0, 0));
		test.add(new DoublePoint(-65.45171625, 0, 0));
		test.add(new DoublePoint(-48.24572246, 0, 0));
		test.add(new DoublePoint(35.33412131, 0, 0));
		test.add(new DoublePoint(80.85923239, 0, 0));
		test.add(new DoublePoint(29.52100938, 0, 0));
		test.add(new DoublePoint(-41.71828209, 0, 0));
		Double sol = Quantile.singleQuantile(test, 0.4);
		assertEquals(-16.779293255, sol, 0.000001);
		test.add(new DoublePoint(41.71828209, 0, 0));
		sol = Quantile.singleQuantile(test, 0.4);
		assertEquals(-12.31966315, sol, 0.000001);
	}

	@Test
	public void Quantile_SingleQuantile_Null() {
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("list is null, empty or has only one value");
		Quantile.singleQuantile(null, 0.4);
	}

	@Test
	public void Quantile_SingleQuantile_Empty() {
		List<DoublePoint> test = new ArrayList<DoublePoint>();

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("list is null, empty or has only one value");
		Quantile.singleQuantile(test, 0.4);
	}

	@Test
	public void Quantile_SingleQuantile_OneEle() {
		List<DoublePoint> test = new ArrayList<DoublePoint>();
		test.add(new DoublePoint(29.52100938, 0, 0));

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("list is null, empty or has only one value");
		Quantile.singleQuantile(test, 0.4);
	}

	@Test
	public void Quantile_SingleQuantile_noValidEle() {
		List<DoublePoint> test = new ArrayList<DoublePoint>();
		test.add(new DoublePoint(-41.71828209, 0, 0));

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("list is null, empty or has only one value");
		Quantile.singleQuantile(test, 0.4);
	}

	@Test
	public void Quantile_Quantiles() {
		List<DoublePoint> test = new ArrayList<DoublePoint>();
		test.add(new DoublePoint(null, 0, 0));
		test.add(new DoublePoint(79.1522269, 0, 0));
		test.add(new DoublePoint(-66.349732, 0, 0));
		test.add(new DoublePoint(41.74220715, 0, 0));
		test.add(new DoublePoint(-63.94046437, 0, 0));
		test.add(new DoublePoint(61.96682694, 0, 0));
		test.add(new DoublePoint(1.15543206, 0, 0));
		test.add(new DoublePoint(-12.31966315, 0, 0));
		test.add(new DoublePoint(-21.23892336, 0, 0));
		test.add(new DoublePoint(65.73601001, 0, 0));
		test.add(new DoublePoint(-65.45171625, 0, 0));
		test.add(new DoublePoint(-48.24572246, 0, 0));
		test.add(new DoublePoint(35.33412131, 0, 0));
		test.add(new DoublePoint(80.85923239, 0, 0));
		test.add(new DoublePoint(29.52100938, 0, 0));
		test.add(new DoublePoint(-41.71828209, 0, 0));
		List<Double> sol = new ArrayList<Double>();
		sol = Quantile.quantiles(test, 4);
		assertEquals(3, sol.size());
		assertEquals(-48.24572246, sol.get(0), 0.000001);
		assertEquals(1.15543206, sol.get(1), 0.000001);
		assertEquals(61.96682694, sol.get(2), 0.000001);
	}

	@Test
	public void Quantile_Quantiles_Empty() {
		List<DoublePoint> test = new ArrayList<DoublePoint>();

		exception.expect(IllegalArgumentException.class);
		exception
				.expectMessage("list is null, empty or has only one element or divisions is <= 0");
		Quantile.quantiles(test, 2);
	}
	
	@Test
	public void Quantile_Quantiles_Null() {
		exception.expect(IllegalArgumentException.class);
		exception
				.expectMessage("list is null, empty or has only one element or divisions is <= 0");
		Quantile.quantiles(null, 2);
	}
	
	@Test
	public void Quantile_Quantiles_InvalidDivision() {
		List<DoublePoint> test = new ArrayList<DoublePoint>();
		test.add(new DoublePoint(29.52100938, 0, 0));
		test.add(new DoublePoint(-41.71828209, 0, 0));
		
		exception.expect(IllegalArgumentException.class);
		exception
				.expectMessage("list is null, empty or has only one element or divisions is <= 0");
		Quantile.quantiles(test, 4);
	}

	@Test
	public void Quantile_InterquartileRange() {
		List<DoublePoint> test = new ArrayList<DoublePoint>();
		test.add(new DoublePoint(null, 0, 0));
		test.add(new DoublePoint(79.1522269, 0, 0));
		test.add(new DoublePoint(-66.349732, 0, 0));
		test.add(new DoublePoint(41.74220715, 0, 0));
		test.add(new DoublePoint(-63.94046437, 0, 0));
		test.add(new DoublePoint(61.96682694, 0, 0));
		test.add(new DoublePoint(1.15543206, 0, 0));
		test.add(new DoublePoint(-12.31966315, 0, 0));
		test.add(new DoublePoint(-21.23892336, 0, 0));
		test.add(new DoublePoint(65.73601001, 0, 0));
		test.add(new DoublePoint(-65.45171625, 0, 0));
		test.add(new DoublePoint(-48.24572246, 0, 0));
		test.add(new DoublePoint(35.33412131, 0, 0));
		test.add(new DoublePoint(80.85923239, 0, 0));
		test.add(new DoublePoint(29.52100938, 0, 0));
		test.add(new DoublePoint(-41.71828209, 0, 0));
		Double sol = Quantile.interquartileRange(test);
		assertEquals(110.2125494, sol, 0.000001);
	}

	@Test
	public void BlockPlotter() {
		List<DoublePoint> test = new ArrayList<DoublePoint>();
		test.add(new DoublePoint(null, 0, 0));
		test.add(new DoublePoint(79.1522269, 0, 0));
		test.add(new DoublePoint(-66.349732, 0, 0));
		test.add(new DoublePoint(41.74220715, 0, 0));
		test.add(new DoublePoint(-63.94046437, 0, 0));
		test.add(new DoublePoint(61.96682694, 0, 0));
		test.add(new DoublePoint(1.15543206, 0, 0));
		test.add(new DoublePoint(-12.31966315, 0, 0));
		test.add(new DoublePoint(-21.23892336, 0, 0));
		test.add(new DoublePoint(65.73601001, 0, 0));
		test.add(new DoublePoint(-65.45171625, 0, 0));
		test.add(new DoublePoint(-48.24572246, 0, 0));
		test.add(new DoublePoint(35.33412131, 0, 0));
		test.add(new DoublePoint(80.85923239, 0, 0));
		test.add(new DoublePoint(29.52100938, 0, 0));
		test.add(new DoublePoint(-41.71828209, 0, 0));
		test.add(new DoublePoint(-441.71828209, 0, 0));
		test.add(new DoublePoint(8000.85923239, 0, 0));
		test.add(new DoublePoint(-440.71828209, 0, 0));
		BlockPlotter block = new BlockPlotter(test);
		assertEquals(-441.71828209, block.getMin().getValue(), 0.0000001);
		assertEquals(8000.85923239, block.getMax().getValue(), 0.0000001);
		assertEquals(-63.94046437, block.getQuartile1(), 0.000001);
		assertEquals(-5.582115545, block.getMedian(), 0.000001);
		assertEquals(61.96682694, block.getQuartile3(), 0.000001);
		assertEquals(-66.349732, block.getWhiskerLow().getValue(), 0.0000001);
		assertEquals(80.85923239, block.getWhiskerHigh().getValue(), 0.0000001);
		assertEquals(3, block.getOutliers().size());
		assertEquals(-441.71828209, block.getOutliers().get(0).getValue(),
				0.0000001);
		assertEquals(-440.71828209, block.getOutliers().get(1).getValue(),
				0.0000001);
		assertEquals(8000.85923239, block.getOutliers().get(2).getValue(),
				0.0000001);
	}

	@Test
	public void BlockPlotter_Null() {
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("list is empty or null");
		new BlockPlotter(null);
	}

	@Test
	public void BlockPlotter_Empty() {
		List<DoublePoint> test = new ArrayList<DoublePoint>();

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("list is empty or null");
		new BlockPlotter(test);
	}

	@Test
	public void VarianceAndStandardDeviation() {
		List<DoublePoint> test = new ArrayList<DoublePoint>();
		test.add(new DoublePoint(70.0, 0, 0));
		test.add(new DoublePoint(36.0, 0, 0));
		test.add(new DoublePoint(110.0, 0, 0));
		test.add(new DoublePoint(30.0, 0, 0));
		test.add(new DoublePoint(63.0, 0, 0));
		test.add(new DoublePoint(60.0, 0, 0));
		test.add(new DoublePoint(52.0, 0, 0));
		test.add(new DoublePoint(null, 0, 0));
		test.add(new DoublePoint(52.0, 0, 0));
		test.add(new DoublePoint(47.0, 0, 0));
		test.add(new DoublePoint(70.0, 0, 0));
		test.add(new DoublePoint(50.0, 0, 0));
		test.add(new DoublePoint(56.0, 0, 0));
		Double var = VarianceAndStandardDeviation.variance(test);
		Double stan = VarianceAndStandardDeviation.standardDeviation(test);
		assertEquals(379.17, var, 0.01);
		assertEquals(19.47, stan, 0.01);
	}

	@Test
	public void VarianceAndStandardDeviation_Deviation_Empty() {
		List<DoublePoint> test = new ArrayList<DoublePoint>();

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("list is null or empty");
		VarianceAndStandardDeviation.standardDeviation(test);
	}

	@Test
	public void VarianceAndStandardDeviation_Deviation_Null() {
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("list is null or empty");
		VarianceAndStandardDeviation.standardDeviation(null);
	}

	@Test
	public void VarianceAndStandardDeviation_Variance_Empty() {
		List<DoublePoint> test = new ArrayList<DoublePoint>();

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("list is empty or null");
		VarianceAndStandardDeviation.variance(test);
	}

	@Test
	public void VarianceAndStandardDeviation_Variance_Null() {
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("list is empty or null");
		VarianceAndStandardDeviation.variance(null);
	}
}
