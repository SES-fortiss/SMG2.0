package org.fortiss.smg.analyzer.impl.calculations.dispersion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.fortiss.smg.informationbroker.api.DoublePoint;

/**
 * @author Ann Katrin Gibtner (annkatrin.gibtner@tum.de)
 *
 */
public class BlockPlotter {

	/**
	 * given list
	 */
	private List<DoublePoint> mylist;
	/**
	 * element with the smallest value of the list
	 */
	private DoublePoint min;
	/**
	 * element with the lagest value of the list
	 */
	private DoublePoint max;
	/**
	 * first quartile of the list
	 * 
	 * @see org.fortiss.smg.analyzer.impl.calculations.dispersion.Quantile#quantiles(List,
	 *      int)
	 */
	private Double quartile1;
	/**
	 * third quartile of the list
	 * 
	 * @see org.fortiss.smg.analyzer.impl.calculations.dispersion.Quantile#quantiles(List,
	 *      int)
	 */
	private Double quartile3;
	/**
	 * median of the list
	 * 
	 * @see org.fortiss.smg.analyzer.impl.calculations.centralTendency.Median#calculate(List)
	 */
	private Double median;
	/**
	 * element with the lowest value within 1.5 * interquartile range below the
	 * first quartile
	 * 
	 * @see org.fortiss.smg.analyzer.impl.calculations.dispersion.Quantile#interquartileRange(List)
	 */
	private DoublePoint whiskerLow = new DoublePoint(Double.MAX_VALUE, 0, 0);
	/**
	 * element with the highest value within 1.5 * interquartile range above the
	 * first quartile
	 * 
	 * @see org.fortiss.smg.analyzer.impl.calculations.dispersion.Quantile#interquartileRange(List)
	 */
	private DoublePoint whiskerHigh = new DoublePoint(Double.MIN_VALUE, 0, 0);
	/**
	 * list of all elements whose values are below the value of
	 * {@code whiskerLow} or above the value of {@code whiskerHigh}
	 * 
	 * @see #whiskerHigh
	 * @see #whiskerLow
	 */
	private List<DoublePoint> outliers = new ArrayList<DoublePoint>();

	public BlockPlotter(List<DoublePoint> mylist)
			throws IllegalArgumentException {
		if (mylist == null || mylist.isEmpty()) {
			throw new IllegalArgumentException("list is empty or null");
		}
		this.mylist = mylist;
		// won't cause exceptions - have been caught earlier
		this.min = PeakValues.minimum(mylist);
		this.max = PeakValues.maximum(mylist);
		List<Double> quart = new ArrayList<Double>();
		try {
			quart = Quantile.quantiles(mylist, 4);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw e;
		}
		if (quart == null || quart.isEmpty()) {
			this.quartile1 = null;
			this.median = null;
			this.quartile3 = null;
		} else {
			this.quartile1 = quart.get(0);
			this.quartile3 = quart.get(2);
			this.median = quart.get(1);
		}
		Double range;
		try {
			range = Quantile.interquartileRange(mylist) * 1.5;
		} catch (IllegalArgumentException e) {
			range = null;
		}
		if (mylist == null || mylist.isEmpty()) {
		} else {
			for (Iterator<DoublePoint> iterator = mylist.iterator(); iterator
					.hasNext();) {
				DoublePoint point = (DoublePoint) iterator.next();
				if (point.getValue() < quartile1 - range
						|| point.getValue() > quartile3 + range) {
					this.outliers.add(point);
				} else if (Math
						.abs((quartile1 - range) - whiskerLow.getValue()) > Math
						.abs((quartile1 - range) - point.getValue())) {
					whiskerLow = point;
				} else if (Math.abs((quartile3 + range)
						- whiskerHigh.getValue()) > Math
						.abs((quartile3 + range) - point.getValue())) {
					whiskerHigh = point;
				}
			}
		}
	}

	public List<DoublePoint> getMylist() {
		return mylist;
	}

	public void setMylist(List<DoublePoint> mylist) {
		this.mylist = mylist;
	}

	public DoublePoint getMin() {
		return min;
	}

	public void setMin(DoublePoint min) {
		this.min = min;
	}

	public DoublePoint getMax() {
		return max;
	}

	public void setMax(DoublePoint max) {
		this.max = max;
	}

	public Double getQuartile1() {
		return quartile1;
	}

	public void setQuartile1(Double quartile1) {
		this.quartile1 = quartile1;
	}

	public Double getQuartile3() {
		return quartile3;
	}

	public void setQuartile3(Double quartile3) {
		this.quartile3 = quartile3;
	}

	public Double getMedian() {
		return median;
	}

	public void setMedian(Double median) {
		this.median = median;
	}

	public DoublePoint getWhiskerLow() {
		return whiskerLow;
	}

	public void setWhiskerLow(DoublePoint whiskerLow) {
		this.whiskerLow = whiskerLow;
	}

	public DoublePoint getWhiskerHigh() {
		return whiskerHigh;
	}

	public void setWhiskerHigh(DoublePoint whiskerHigh) {
		this.whiskerHigh = whiskerHigh;
	}

	public List<DoublePoint> getOutliers() {
		return outliers;
	}

	public void setOutliers(List<DoublePoint> outliers) {
		this.outliers = outliers;
	}

	/**
	 * Creates a list which can be used for google candle stick charts
	 * 
	 * @return a list with min, max, quartile1 & quartile3
	 */
	public List<Double> candleStickMinMax() {
		List<Double> sol = new ArrayList<Double>();
		sol.add(min.getValue());
		sol.add(quartile1);
		sol.add(quartile3);
		sol.add(max.getValue());
		return sol;
	}
}
