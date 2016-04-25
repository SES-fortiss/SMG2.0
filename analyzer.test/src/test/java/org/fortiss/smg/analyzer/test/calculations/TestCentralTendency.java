package org.fortiss.smg.analyzer.test.calculations;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.fortiss.smg.analyzer.impl.calculations.centralTendency.ArithmeticMean;
import org.fortiss.smg.analyzer.impl.calculations.centralTendency.ArithmeticMeanTime;
import org.fortiss.smg.analyzer.impl.calculations.centralTendency.CubicMean;
import org.fortiss.smg.analyzer.impl.calculations.centralTendency.GeometricMean;
import org.fortiss.smg.analyzer.impl.calculations.centralTendency.HarmonicMean;
import org.fortiss.smg.analyzer.impl.calculations.centralTendency.Median;
import org.fortiss.smg.analyzer.impl.calculations.centralTendency.Midrange;
import org.fortiss.smg.analyzer.impl.calculations.centralTendency.Mode;
import org.fortiss.smg.analyzer.impl.calculations.centralTendency.PercentBool;
import org.fortiss.smg.analyzer.impl.calculations.centralTendency.RootMeanSquare;
import org.fortiss.smg.informationbroker.api.DoublePoint;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TestCentralTendency {

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void Mode_Bimodal() {
		List<DoublePoint> test = new ArrayList<DoublePoint>();
		test.add(new DoublePoint(1.9, 0, 0));
		test.add(new DoublePoint(2.3, 0, 0));
		test.add(new DoublePoint(6.0, 0, 0));
		test.add(new DoublePoint(1.9, 0, 0));
		test.add(new DoublePoint(1.9, 0, 0));
		test.add(new DoublePoint(null, 0, 0));
		test.add(new DoublePoint(3.7, 0, 0));
		test.add(new DoublePoint(2.3, 0, 0));
		test.add(new DoublePoint(4.6, 0, 0));
		test.add(new DoublePoint(2.3, 0, 0));
		List<Double> foo = Mode.calcMode(test);
		assertEquals(2, foo.size());
		assertEquals(1.9, foo.get(0), 0.0001);
		assertEquals(2.3, foo.get(1), 0.0001);
	}

	@Test
	public void Mode_Nomodal() {
		List<DoublePoint> test = new ArrayList<DoublePoint>();
		test.add(new DoublePoint(1.9, 0, 0));
		test.add(new DoublePoint(2.3, 0, 0));
		test.add(new DoublePoint(6.0, 0, 0));
		test.add(new DoublePoint(3.7, 0, 0));
		test.add(new DoublePoint(4.6, 0, 0));
		test.add(new DoublePoint(2.3456, 0, 0));
		test.add(new DoublePoint(1.9321, 0, 0));
		test.add(new DoublePoint(1.9876456, 0, 0));
		List<Double> solution = Mode.calcMode(test);
		assertEquals(0, solution.size());
	}

	@Test
	public void Mode_Unimodal() {
		List<DoublePoint> test = new ArrayList<DoublePoint>();
		test.add(new DoublePoint(1.9, 0, 0));
		test.add(new DoublePoint(2.3, 0, 0));
		test.add(new DoublePoint(6.0, 0, 0));
		test.add(new DoublePoint(1.9, 0, 0));
		test.add(new DoublePoint(1.9, 0, 0));
		test.add(new DoublePoint(3.7, 0, 0));
		test.add(new DoublePoint(2.3, 0, 0));
		test.add(new DoublePoint(4.6, 0, 0));
		test.add(new DoublePoint(2.3, 0, 0));
		test.add(new DoublePoint(4.6, 0, 0));
		test.add(new DoublePoint(4.6, 0, 0));
		test.add(new DoublePoint(4.6, 0, 0));
		List<Double> sol = Mode.calcMode(test);
		assertEquals(1, sol.size());
		assertEquals(4.6, sol.get(0), 0.0001);
	}

	@Test
	public void Mode_Empty() {
		List<DoublePoint> test = new ArrayList<DoublePoint>();

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("list is empty or null");
		Mode.calcMode(test);
	}

	@Test
	public void Mode_Null() {
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("list is empty or null");
		Mode.calcMode(null);
	}

	@Test
	public void ArithmeticMean() {
		List<DoublePoint> test = new ArrayList<DoublePoint>();
		test.add(new DoublePoint(2.34, 0, 0));
		test.add(new DoublePoint(-12.3, 0, 0));
		test.add(new DoublePoint(0.002, 0, 0));
		test.add(new DoublePoint(43.76, 0, 0));
		test.add(new DoublePoint(34.01, 0, 0));
		test.add(null);
		test.add(new DoublePoint(null, 0, 0));
		ArithmeticMean mean = new ArithmeticMean();
		Double sol = mean.calculate(test);
		assertEquals(13.5624, sol, 0.00001);
	}


	@Test
	public void ArithmeticMean_Null() {
		ArithmeticMean mean = new ArithmeticMean();

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("list is null or empty");
		mean.calculate(null);
	}

	@Test
	public void ArithmeticMean_Empty() {
		List<DoublePoint> list = new ArrayList<DoublePoint>();
		ArithmeticMean mean = new ArithmeticMean();

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("list is null or empty");
		mean.calculate(list);
	}
	
	@Test
	public void ArithmeticMean_Trimmed_Null() {
		ArithmeticMean mean = new ArithmeticMean();

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("list is null or empty");
		mean.trimmed(null);
	}
	
	@Test
	public void ArithmeticMean_Trimmed_Empty() {
		List<DoublePoint> list = new ArrayList<DoublePoint>();
		ArithmeticMean mean = new ArithmeticMean();

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("list is null or empty");
		mean.trimmed(list);
	}

	@Test
	public void ArithmeticMean_TrimmedVar_Null() {
		ArithmeticMean mean = new ArithmeticMean();

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("list is empty or null");
		mean.trimmedVar(null, 0.2);
	}
	
	@Test
	public void ArithmeticMean_TrimmedVar_Empty() {
		List<DoublePoint> list = new ArrayList<DoublePoint>();
		ArithmeticMean mean = new ArithmeticMean();

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("list is empty or null");
		mean.trimmedVar(list, 0.2);
	}
	
	@Test
	public void ArithmeticMean_TrimmedVar_OutOfBound() {
		List<DoublePoint> list = new ArrayList<DoublePoint>();
		list.add(new DoublePoint(34.01, 0, 0));
		ArithmeticMean mean = new ArithmeticMean();

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("trimmer is not valid: > 1.0 or < 0.0");
		mean.trimmedVar(list, 4.0);
	}
	@Test
	public void ArithmeticMean_Trimmed() {
		List<DoublePoint> test = new ArrayList<DoublePoint>();
		test.add(new DoublePoint(2.34, 0, 0));
		test.add(new DoublePoint(-12.3, 0, 0));
		test.add(new DoublePoint(0.002, 0, 0));
		test.add(new DoublePoint(43.76, 0, 0));
		test.add(new DoublePoint(34.01, 0, 0));
		test.add(null);
		test.add(new DoublePoint(null, 0, 0));
		ArithmeticMean mean = new ArithmeticMean();
		Double sol = mean.calculate(test);
		assertEquals(13.5624, sol, 0.00001);
		test.clear();
		test.add(new DoublePoint(-40.14986151, 0, 0));
		test.add(new DoublePoint(-17.33513872, 0, 0));
		test.add(new DoublePoint(34.08255306, 0, 0));
		test.add(new DoublePoint(53.73953008, 0, 0));
		test.add(new DoublePoint(55.10695951, 0, 0));
		test.add(new DoublePoint(30.73379889, 0, 0));
		test.add(new DoublePoint(-25.79588176, 0, 0));
		test.add(new DoublePoint(59.17240718, 0, 0));
		test.add(new DoublePoint(85.15035284, 0, 0));
		test.add(new DoublePoint(63.59805421, 0, 0));
		test.add(new DoublePoint(9.17737111, 0, 0));
		test.add(new DoublePoint(52.41384139, 0, 0));
		test.add(new DoublePoint(-67.88570965, 0, 0));
		test.add(new DoublePoint(-59.52615335, 0, 0));
		test.add(new DoublePoint(84.57213148, 0, 0));
		test.add(new DoublePoint(-71.36502455, 0, 0));
		test.add(new DoublePoint(-28.15803583, 0, 0));
		test.add(new DoublePoint(52.78990137, 0, 0));
		test.add(new DoublePoint(-79.14308801, 0, 0));
		test.add(new DoublePoint(-47.78545252, 0, 0));
		test.add(new DoublePoint(-64.62596009, 0, 0));
		test.add(new DoublePoint(-72.41218854, 0, 0));
		test.add(new DoublePoint(-66.65256583, 0, 0));
		test.add(new DoublePoint(-33.48118345, 0, 0));
		test.add(new DoublePoint(63.08168468, 0, 0));
		test.add(new DoublePoint(-60.14659276, 0, 0));
		test.add(new DoublePoint(73.9367503, 0, 0));
		test.add(new DoublePoint(15.214807, 0, 0));
		test.add(new DoublePoint(13.3838594, 0, 0));
		test.add(new DoublePoint(-41.78587687, 0, 0));
		test.add(new DoublePoint(17.16092057, 0, 0));
		test.add(new DoublePoint(-43.00276896, 0, 0));
		test.add(new DoublePoint(-2.53231575, 0, 0));
		test.add(new DoublePoint(-28.36868587, 0, 0));
		test.add(new DoublePoint(-64.86496179, 0, 0));
		test.add(new DoublePoint(45.33152208, 0, 0));
		test.add(new DoublePoint(93.88579048, 0, 0));
		test.add(new DoublePoint(-61.85028353, 0, 0));
		test.add(new DoublePoint(6.21272044, 0, 0));
		test.add(new DoublePoint(-14.79107378, 0, 0));
		test.add(new DoublePoint(26.37108355, 0, 0));
		test.add(new DoublePoint(8.15667724, 0, 0));
		test.add(new DoublePoint(29.98750636, 0, 0));
		test.add(new DoublePoint(-76.05376727, 0, 0));
		test.add(new DoublePoint(16.78408958, 0, 0));
		test.add(new DoublePoint(-82.57391935, 0, 0));
		test.add(new DoublePoint(86.61134961, 0, 0));
		test.add(new DoublePoint(-43.31624942, 0, 0));
		test.add(new DoublePoint(33.28128625, 0, 0));
		test.add(new DoublePoint(19.80071307, 0, 0));
		test.add(new DoublePoint(46.23119307, 0, 0));
		test.add(new DoublePoint(-8.35323071, 0, 0));
		test.add(new DoublePoint(-5.0949904, 0, 0));
		test.add(new DoublePoint(null, 0, 0));
		test.add(new DoublePoint(-45.56338668, 0, 0));
		test.add(new DoublePoint(-59.47913343, 0, 0));
		test.add(new DoublePoint(77.41943443, 0, 0));
		test.add(new DoublePoint(-2.89270138, 0, 0));
		test.add(new DoublePoint(-37.28899122, 0, 0));
		test.add(new DoublePoint(87.47300061, 0, 0));
		sol = mean.trimmed(test);
		assertEquals(-0.39869744333, sol, 0.00000001);
	}

	@Test
	public void ArithmeticMean_TrimmedVar() {
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
		test.add(new DoublePoint(-38.34972935, 0, 0));
		test.add(new DoublePoint(-69.50836565, 0, 0));
		test.add(new DoublePoint(-28.61387481, 0, 0));
		test.add(new DoublePoint(-26.39013929, 0, 0));
		test.add(new DoublePoint(-98.04483066, 0, 0));
		ArithmeticMean mean = new ArithmeticMean();
		Double sol = mean.trimmedVar(test, 0.2);
		assertEquals(-14.422002415, sol, 0.000000001);
	}

	@Test
	public void ArithmeticMeanTime() {
		List<DoublePoint> list = new ArrayList<DoublePoint>();
		list.add(new DoublePoint(25.0, 0, 1));
		list.add(new DoublePoint(10.0, 0, 4000));
		list.add(new DoublePoint(35.0, 0, 130000));
		list.add(new DoublePoint(22.0, 0, 1500000));
		// mean = 23
		// time span = 1.499.999
		ArithmeticMeanTime mean = new ArithmeticMeanTime();
		assertEquals(9.5833269444444444444444444444444, mean.calculate(list),
				0.000000001);
	}

	@Test
	public void CubicMean() {
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
		CubicMean mean = new CubicMean();
		Double sol = mean.calculate(test);
		assertEquals(35.2838608852, sol, 0.000001);
	}

	@Test
	public void CubicMean_Null() {
		CubicMean mean = new CubicMean();

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("list is null or empty");
		mean.calculate(null);
	}

	@Test
	public void CubicMean_Empty() {
		List<DoublePoint> test = new ArrayList<DoublePoint>();
		CubicMean mean = new CubicMean();

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("list is null or empty");
		mean.calculate(test);
	}

	@Test
	public void CubicMean_SumNegative() {
		List<DoublePoint> test = new ArrayList<DoublePoint>();
		CubicMean mean = new CubicMean();
		test.add(new DoublePoint(-10.1, 0, 0));

		exception.expect(ArithmeticException.class);
		exception.expectMessage("sum of all values is negative");
		mean.calculate(test);

	}

	@Test
	public void GeometricMean() {
		List<DoublePoint> test = new ArrayList<DoublePoint>();
		test.add(new DoublePoint(null, 0, 0));
		test.add(new DoublePoint(79.1522269, 0, 0));
		test.add(new DoublePoint(66.349732, 0, 0));
		test.add(new DoublePoint(41.74220715, 0, 0));
		test.add(new DoublePoint(63.94046437, 0, 0));
		test.add(new DoublePoint(61.96682694, 0, 0));
		test.add(new DoublePoint(1.15543206, 0, 0));
		test.add(new DoublePoint(12.31966315, 0, 0));
		test.add(new DoublePoint(21.23892336, 0, 0));
		test.add(new DoublePoint(65.73601001, 0, 0));
		test.add(new DoublePoint(65.45171625, 0, 0));
		test.add(new DoublePoint(48.24572246, 0, 0));
		test.add(new DoublePoint(35.33412131, 0, 0));
		test.add(new DoublePoint(80.85923239, 0, 0));
		test.add(new DoublePoint(29.52100938, 0, 0));
		test.add(new DoublePoint(41.71828209, 0, 0));
		GeometricMean mean = new GeometricMean();
		Double sol = mean.calculate(test);
		assertEquals(35.66072886632738, sol, 0.00000001);
	}

	@Test
	public void GeometricMean_Empty() {
		List<DoublePoint> test = new ArrayList<DoublePoint>();
		GeometricMean mean = new GeometricMean();

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("list is null or empty");
		mean.calculate(test);
	}

	@Test
	public void GeometricMean_Null() {
		GeometricMean mean = new GeometricMean();

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("list is null or empty");
		mean.calculate(null);
	}

	@Test
	public void GeometricMean_NegativeValues() {
		List<DoublePoint> test = new ArrayList<DoublePoint>();
		GeometricMean mean = new GeometricMean();
		test.add(new DoublePoint(80.85923239, 0, 0));
		test.add(new DoublePoint(-29.52100938, 0, 0));
		test.add(new DoublePoint(41.71828209, 0, 0));

		exception.expect(ArithmeticException.class);
		exception
				.expectMessage("geometric mean is not defined for negative values");
		mean.calculate(test);
	}

	@Test
	public void HarmonicMean() {
		List<DoublePoint> test = new ArrayList<DoublePoint>();
		test.add(new DoublePoint(null, 0, 0));
		test.add(new DoublePoint(79.1522269, 0, 0));
		test.add(new DoublePoint(66.349732, 0, 0));
		test.add(new DoublePoint(41.74220715, 0, 0));
		test.add(new DoublePoint(63.94046437, 0, 0));
		test.add(new DoublePoint(61.96682694, 0, 0));
		test.add(new DoublePoint(1.15543206, 0, 0));
		test.add(new DoublePoint(12.31966315, 0, 0));
		test.add(new DoublePoint(21.23892336, 0, 0));
		test.add(new DoublePoint(65.73601001, 0, 0));
		test.add(new DoublePoint(65.45171625, 0, 0));
		test.add(new DoublePoint(48.24572246, 0, 0));
		test.add(new DoublePoint(35.33412131, 0, 0));
		test.add(new DoublePoint(80.85923239, 0, 0));
		test.add(new DoublePoint(29.52100938, 0, 0));
		test.add(new DoublePoint(41.71828209, 0, 0));
		HarmonicMean mean = new HarmonicMean();
		Double sol = mean.calculate(test);
		assertEquals(12.22591750714, sol, 0.0000001);
	}

	@Test
	public void HarmonicMean_Null() {
		HarmonicMean mean = new HarmonicMean();

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("list is empty or null");
		mean.calculate(null);
	}

	@Test
	public void HarmonicMean_Empty() {
		List<DoublePoint> test = new ArrayList<DoublePoint>();
		HarmonicMean mean = new HarmonicMean();

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("list is empty or null");
		mean.calculate(test);
	}

	@Test
	public void Median() {
		List<DoublePoint> test = new ArrayList<DoublePoint>();
		test.add(new DoublePoint(null, 0, 0));
		test.add(new DoublePoint(79.1522269, 0, 0));
		test.add(new DoublePoint(66.349732, 0, 0));
		test.add(new DoublePoint(41.74220715, 0, 0));
		test.add(new DoublePoint(63.94046437, 0, 0));
		test.add(new DoublePoint(61.96682694, 0, 0));
		test.add(new DoublePoint(-1.15543206, 0, 0));
		test.add(new DoublePoint(12.31966315, 0, 0));
		Median mean = new Median();
		Double sol = mean.calculate(test);
		assertEquals(61.96682694, sol, 0.000001);
		test.add(new DoublePoint(21.23892336, 0, 0));
		sol = mean.calculate(test);
		assertEquals(51.854517045, sol, 0.00001);
	}

	@Test
	public void Median_Null() {
		Median mean = new Median();

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("list is null or empty");
		mean.calculate(null);
	}

	@Test
	public void Median_Empty() {
		List<DoublePoint> test = new ArrayList<DoublePoint>();
		Median mean = new Median();

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("list is null or empty");
		mean.calculate(test);
	}

	@Test
	public void Midrange() {
		List<DoublePoint> test = new ArrayList<DoublePoint>();
		test.add(new DoublePoint(null, 0, 0));
		test.add(new DoublePoint(79.1522269, 0, 0));
		test.add(new DoublePoint(66.349732, 0, 0));
		test.add(new DoublePoint(41.74220715, 0, 0));
		test.add(new DoublePoint(-63.94046437, 0, 0));
		test.add(new DoublePoint(61.96682694, 0, 0));
		test.add(new DoublePoint(1.15543206, 0, 0));
		test.add(new DoublePoint(-12.31966315, 0, 0));
		test.add(new DoublePoint(21.23892336, 0, 0));
		test.add(new DoublePoint(65.73601001, 0, 0));
		test.add(new DoublePoint(-65.45171625, 0, 0));
		test.add(new DoublePoint(48.24572246, 0, 0));
		test.add(new DoublePoint(35.33412131, 0, 0));
		test.add(new DoublePoint(80.85923239, 0, 0));
		test.add(new DoublePoint(29.52100938, 0, 0));
		test.add(new DoublePoint(41.71828209, 0, 0));
		Midrange mean = new Midrange();
		Double sol = mean.calculate(test);
		assertEquals(7.70375807, sol, 0.000001);
	}

	@Test
	public void Midrange_Empty() {
		List<DoublePoint> test = new ArrayList<DoublePoint>();
		Midrange mean = new Midrange();

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("list is empty or null");
		mean.calculate(test);
	}

	@Test
	public void Midrange_Null() {
		Midrange mean = new Midrange();

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("list is empty or null");
		mean.calculate(null);
	}

	@Test
	public void PercentBool() {
		List<DoublePoint> test = new ArrayList<DoublePoint>();
		test.add(new DoublePoint(null, 0, 0));
		test.add(new DoublePoint(1.0, 0, 0));
		test.add(new DoublePoint(1.0, 0, 0));
		test.add(new DoublePoint(1.0, 0, 0));
		test.add(new DoublePoint(1.0, 0, 0));
		test.add(new DoublePoint(1.0, 0, 0));
		test.add(new DoublePoint(1.0, 0, 0));
		test.add(new DoublePoint(1.0, 0, 0));
		test.add(new DoublePoint(1.0, 0, 0));
		test.add(new DoublePoint(0.0, 0, 0));
		test.add(new DoublePoint(0.0, 0, 0));
		test.add(new DoublePoint(0.0, 0, 0));
		test.add(new DoublePoint(0.0, 0, 0));
		PercentBool mean = new PercentBool();
		Double sol = mean.calculate(test);
		assertEquals(66.6666666666666, sol, 0.00000001);
	}

	@Test
	public void PercentBool_Null() {
		PercentBool mean = new PercentBool();

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("list is empty or null");
		mean.calculate(null);
	}

	@Test
	public void PercentBool_Empty() {
		List<DoublePoint> test = new ArrayList<DoublePoint>();
		PercentBool mean = new PercentBool();

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("list is empty or null");
		mean.calculate(test);
	}

	@Test
	public void RootMeanSquare() {
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
		RootMeanSquare mean = new RootMeanSquare();
		Double sol = mean.calculate(test);
		assertEquals(53.0871396766873173, sol, 0.00000001);
	}

	@Test
	public void RootMeanSquare_Null() {
		List<DoublePoint> test = new ArrayList<DoublePoint>();
		RootMeanSquare mean = new RootMeanSquare();

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("list is null or empty");
		mean.calculate(test);
	}

	@Test
	public void RootMeanSquare_Empty() {
		RootMeanSquare mean = new RootMeanSquare();

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("list is null or empty");
		mean.calculate(null);
	}
}
