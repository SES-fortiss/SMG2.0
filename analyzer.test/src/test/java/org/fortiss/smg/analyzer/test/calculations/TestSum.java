package org.fortiss.smg.analyzer.test.calculations;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.fortiss.smg.analyzer.impl.calculations.Sum;
import org.fortiss.smg.informationbroker.api.DoublePoint;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TestSum {

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void testSum() {
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
		Sum mean = new Sum();
		Double sol = mean.calculate(test);
		assertEquals(76.20256246, sol, 0.000000001);
	}

	@Test
	public void testSumNull() {
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("list empty or null");
		Sum mean = new Sum();
		mean.calculate(null);
	}

	@Test
	public void testSumEmpty() {
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("list empty or null");
		List<DoublePoint> test = new ArrayList<DoublePoint>();
		Sum mean = new Sum();
		mean.calculate(test);
	}

}
