/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.optimizer.data;

import static org.fortiss.smg.optimizer.utils.Tools.isEqual;
import static org.fortiss.smg.optimizer.utils.Tools.range;

import java.util.ArrayList;
import java.util.List;

/***
 * 
 * @author Cheng Zhang
 * @version 1.0
 * 
 */
public class Period implements Cloneable {

	private double price;

	private Interval[] intervals;

	private double[] demands;

	private double[] supplies;

	private double possibleDemand;

	private double leastSupply;

	private double maximumSupply;

	private double expectedStorage;

	private double neededStorage;

	private int first, last;

	private double exchange;

	public Period(double price, Interval[] intervals, int first, int last) {
		super();
		this.price = price;
		this.intervals = intervals;
		this.first = first;
		this.last = last;
	}

	public void update(double possibleDemand, double leastSupply,
			double maximumSupply, double neededStorage) {
		this.possibleDemand = possibleDemand;
		this.leastSupply = leastSupply;
		this.maximumSupply = maximumSupply;
		this.expectedStorage = neededStorage;
		this.neededStorage = neededStorage;
	}

	/** update battery exchange at all intervals in this price period */
	public void update(double[] batteryExchanges) {
		for (int i = 0; i < intervals.length; i++) {
			intervals[i].setBatteryExchange(batteryExchanges[i]);
			// Log
			// log.finer("[Update Interval: " + intervals[i].toString() + "]");
		}
	}

	/**
	 * Update battery exchange at all intervals in this price period with a
	 * given current capacity at the beginning of this price period
	 */
	public void update(double[] batteryExchanges, double currentCapacity) {
		for (int i = 0; i < intervals.length; i++) {
			intervals[i].setBatteryExchange(batteryExchanges[i]);
			intervals[i].setCapacity(currentCapacity);
			currentCapacity += batteryExchanges[i];
		}
	}

	public double[] getDemands() {
		if (demands == null) {
			demands = new double[intervals.length];
			for (int i = 0; i < intervals.length; i++) {
				demands[i] = intervals[i].getDemand();
			}
		}
		return demands;
	}

	public double[] getSupplies() {
		if (supplies == null) {
			supplies = new double[intervals.length];
			for (int i = 0; i < intervals.length; i++) {
				supplies[i] = intervals[i].getSupply();
			}
		}
		return supplies;
	}

	public double getPossibleDemand() {
		return possibleDemand;
	}

	public double getLeastSupply() {
		return leastSupply;
	}

	public double getMaximumSupply() {
		return maximumSupply;
	}

	public double getNeededStorage() {
		return neededStorage;
	}

	public void setNeededStorage(double neededStorage) {
		this.neededStorage = neededStorage;
	}

	public double getExpectedStorage() {
		return expectedStorage;
	}

	public double getPrice() {
		return price;
	}

	public double getExchange() {
		return exchange;
	}

	public int getFirst() {
		return first;
	}

	public int getLast() {
		return last;
	}

	public void setExchange(double exchange) {
		this.exchange = range(possibleDemand, maximumSupply, exchange);
	}

	public double getExchange(double exchange) {
		return range(possibleDemand, this.exchange < 0 ? this.exchange
				: maximumSupply, exchange);
	}

	@Override
	public String toString() {
		return "[period:" + first + " to " + last + ", price:" + price
				+ ", possibleDemand:" + possibleDemand + ", leastSupply:"
				+ leastSupply + ", maximumSupply:" + maximumSupply
				+ ", expectedStorage:" + expectedStorage + ", exchange:"
				+ exchange + ", neededStorage:" + neededStorage + "]";
	}

	@Override
	public Object clone() {
		try {
			Period period = (Period) super.clone();
			period.intervals = intervals.clone();
			for (int i = 0; i < intervals.length; i++) {
				period.intervals[i] = (Interval) intervals[i].clone();
			}
			return period;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	/** Copy price period array */
	public static Period[] copy(Period[] periods) {
		Period[] copy = periods.clone();
		for (int i = 0; i < periods.length; i++) {
			copy[i] = (Period) periods[i].clone();
		}
		return copy;
	}

	/** Transform intervals to price periods */
	public static Period[] toPeriods(Interval[] intervals) {
		List<Period> periods = new ArrayList<Period>();

		double price = intervals[0].getPrice();
		int first = 0, last = 0;
		List<Interval> periodIntervals = new ArrayList<Interval>();
		periodIntervals.add(intervals[0]);

		for (int i = 1; i < intervals.length; i++) {
			if (isEqual(price, intervals[i].getPrice())) {
				last = i;
				periodIntervals.add(intervals[i]);
			} else {
				Period period = new Period(price,
						periodIntervals.toArray(new Interval[periodIntervals
								.size()]), first, last);
				periods.add(period);

				price = intervals[i].getPrice();
				periodIntervals = new ArrayList<Interval>();
				periodIntervals.add(intervals[i]);
				first = last = i;
			}

		}

		Period period = new Period(price,
				periodIntervals.toArray(new Interval[periodIntervals.size()]),
				first, last);
		periods.add(period);

		return periods.toArray(new Period[periods.size()]);
	}

	/** Export period array with predefined format */
	public static String[] exportPeriods(Period[] periods) {
		String[] exports = new String[periods.length];
		for (int i = 0; i < periods.length; i++) {
			exports[i] = periods[i].first + " to " + periods[i].last + "	"
					+ periods[i].getPrice() + "	"
					+ periods[i].getPossibleDemand() + "	"
					+ periods[i].getLeastSupply() + "	"
					+ periods[i].getMaximumSupply() + "	"
					+ periods[i].getExpectedStorage();
		}
		return exports;
	}

	/** Export period array with predefined format */
	public static String[] exportFinalPeriods(Period[] periods) {
		String[] exports = new String[periods.length];
		for (int i = 0; i < periods.length; i++) {
			exports[i] = periods[i].first + " to " + periods[i].last + "	"
					+ periods[i].getPrice() + "	"
					+ periods[i].getPossibleDemand() + "	"
					+ periods[i].getLeastSupply() + "	"
					+ periods[i].getMaximumSupply() + "	"
					+ periods[i].getExpectedStorage() + "	"
					+ periods[i].getNeededStorage() + "	"
					+ periods[i].getExchange();
		}
		return exports;
	}
}
