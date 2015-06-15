/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.optimizer.data;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static org.fortiss.smg.optimizer.data.Specification.getSpecification;
import static org.fortiss.smg.optimizer.utils.Tools.dateToString;
import static org.fortiss.smg.optimizer.utils.Tools.isEqual;
import static org.fortiss.smg.optimizer.utils.Tools.toDate;

import java.util.Date;

/***
 * 
 * @author Cheng Zhang
 * @version 1.0
 * 
 */
public class Interval {

	private Date date;

	private double duration;

	private double price;

	private double consumption;

	private double generation;

	private double charge;

	private double discharge;

	private double fromGrid;

	private double toGrid;

	private double capacity;

	/***
	 * In a interval, 'consumption', 'charge' and 'to' are positive, which mean
	 * use energy from SMG; while 'generation', 'discharge' and 'from' are
	 * negative that provide energy to SMG. The summation of 'consumption',
	 * 'generation', 'charge', 'discharge', 'from', 'to' should be lower than
	 * zero.
	 * 
	 * @param date
	 * @param duration
	 * @param price
	 * @param consumption
	 * @param generation
	 */
	public Interval(Date date, double duration, double price,
			double consumption, double generation) {
		super();
		this.date = date;
		this.duration = duration;
		this.price = price;
		this.consumption = consumption;
		this.generation = generation;
	}

	public Interval(Interval interval) {
		super();
		this.date = interval.date;
		this.duration = interval.duration;
		this.price = interval.price;
		this.consumption = interval.consumption;
		this.generation = interval.generation;
	}

	public double getPrice() {
		return price;
	}

	public String getDate() {
		return dateToString(date);
	}

	public double getCharge() {
		return charge;
	}

	public double getDischarge() {
		return discharge;
	}

	public double getConsumption() {
		return consumption;
	}

	public double getGeneration() {
		return generation;
	}

	public double getDuration() {
		return duration;
	}

	public double getFromGrid() {
		return fromGrid;
	}

	public double getToGrid() {
		return toGrid;
	}

	public double getCapacity() {
		return capacity;
	}

	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}

	public double getGridExchange() {
		return fromGrid + toGrid;
	}

	public double getCost() {
		return getGridExchange() * duration * price;
	}

	public double getDemand() {
		return getSpecification().getDemand(consumption, generation, duration);
	}

	public double getSupply() {
		return getSpecification().getSupply(consumption, duration);
	}

	/** Set from grid power and to grid power at the interval */
	private void setGridExchange() {
		double exchange = getSpecification().getGridExchange(consumption,
				generation, charge, discharge);
		if (exchange > 0) {
			this.fromGrid = 0.0;
			this.toGrid = exchange;
		} else {
			this.toGrid = 0.0;
			this.fromGrid = exchange;
		}
	}

	/**
	 * Exchange is the modified value of capacity. Negative value represents the
	 * energy provided by batteries, Positive value represents the energy stored
	 * by batteries
	 */
	public void setBatteryExchange(double exchange) {
		setChargeDischarge(getSpecification().getBatteryExchange(exchange,
				duration));
		setGridExchange();
	}

	/** Set charge power and discharge power at the interval */
	public void setChargeDischarge(double exchange) {
		if (exchange > 0) {
			this.discharge = 0.0;
			this.charge = exchange;
		} else {
			this.charge = 0.0;
			this.discharge = exchange;
		}
	}

	/** Check whether unused power exists at the interval */
	public boolean hasWastage() {
		if (!isEqual(consumption + generation + charge + discharge + fromGrid
				+ toGrid, 0, 10)
				|| consumption + generation + charge + discharge + fromGrid
						+ toGrid < 0)
			return true;
		return false;
	}

	/** Get unused power at the interval */
	public double getWastage() {
		return hasWastage() ? consumption + generation + charge + discharge
				+ fromGrid + toGrid : 0;
	}

	@Override
	public String toString() {
		return "[interval:" + dateToString(date) + " price: " + price
				+ ", duration:" + duration + ", consumption:" + consumption
				+ ", generation:" + generation + ", charge:" + charge
				+ ", discharge:" + discharge + ", from:" + fromGrid + ", to:"
				+ toGrid + "]";
	}

	@Override
	public Object clone() {
		return new Interval(this);
	}

	/** Copy interval array */
	public static Interval[] copy(Interval[] intervals) {
		Interval[] copy = intervals.clone();
		for (int i = 0; i < intervals.length; i++) {
			copy[i] = (Interval) intervals[i].clone();
		}
		return copy;
	}

	/**
	 * Construct interval array with date, duration, consumption, generation and
	 * price array
	 */
	public static Interval[] toIntervals(String[] date, double duration,
			double[] consumption, double[] generation, double[] price) {
		Interval[] intervals = new Interval[consumption.length];
		for (int i = 0; i < consumption.length; i++) {
			intervals[i] = new Interval(toDate(date[i]), duration, price[i],
					consumption[i], generation[i]);
		}
		return intervals;
	}

	/** Export interval array with predefined format */
	public static String[] exportIntervals(Interval[] intervals) {
		String[] exports = new String[intervals.length];
		for (int i = 0; i < intervals.length; i++) {
			exports[i] = min(0, intervals[i].getDemand())
					+ "	"
					+ max(0, intervals[i].getDemand())
					+ "	"
					+ (intervals[i].getSupply() - max(0,
							intervals[i].getDemand()));
		}
		return exports;
	}

	/** Export power of interval array with predefined format */
	public static String[] exportPowers(Interval[] intervals) {
		String[] exports = new String[intervals.length];
		for (int i = 0; i < intervals.length; i++) {
			exports[i] = intervals[i].getConsumption() + "	"
					+ intervals[i].getGeneration() + "	"
					+ intervals[i].fromGrid + "	" + intervals[i].toGrid + "	"
					+ intervals[i].charge + "	" + intervals[i].discharge + "	"
					+ intervals[i].getWastage();
		}
		return exports;
	}
}
