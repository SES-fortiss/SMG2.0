/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.optimizer.utils;

import static java.lang.Math.max;
import static java.lang.Math.min;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utilities for the advisor.
 * 
 * @author Cheng Zhang
 * @version 1.0
 */
public class Tools {

	public static final int Balance = 0;

	public static final int Amplitude = 1;

	public static final int NotExist = -1;

	public static final Logger log = Logger.getLogger(Tools.class.getName());

	/** Date format */
	private static final SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/** Prefix of attributes */
	private static final String[] prefixs = { "get", "is" };

	/***
	 * Compare two double value, whether they are equal
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static boolean isEqual(double d1, double d2) {
		return isEqual(d1, d2, 0.000001);
	}

	/***
	 * Compare two double value with a given accuracy, whether they are equal
	 * 
	 * @param d1
	 * @param d2
	 * @param accuracy
	 * @return
	 */
	public static boolean isEqual(double d1, double d2, double accuracy) {
		return Math.abs(d1 - d2) <= accuracy;
	}

	/***
	 * Format double value with 2 decimals
	 * 
	 * @param value
	 * @return
	 */
	public static double format(double value) {
		return ((int) (value * 100)) / 100;
	}

	/***
	 * Check whether value is in a given array
	 * 
	 * @param values
	 * @param check
	 * @return
	 */
	public static boolean isExclusive(int[] exclusives, int value) {
		if (exclusives != null)
			for (int exclusive : exclusives) {
				if (exclusive == value) {
					return true;
				}
			}
		return false;
	}

	/***
	 * Add a value to a given array
	 * 
	 * @param source
	 * @param value
	 * @return
	 */
	public static int[] add(int[] source, int value) {
		if (source == null)
			return new int[] { value };
		int[] result = new int[source.length + 1];
		for (int i = 0; i < source.length; i++) {
			result[i] = source[i];
		}
		result[source.length] = value;
		return result;

	}

	/***
	 * With a given range, return a value in this range.
	 * 
	 * @param low
	 * @param high
	 * @param value
	 * @return
	 */
	public static double range(double low, double high, double value) {
		if (low > high)
			return 0;
		return max(min(value, high), low);
	}

	/***
	 * Return a value at the same side of from or to
	 * 
	 * @param from
	 * @param to
	 * @param value
	 * @return
	 */
	public static double direction(double from, double to, double value) {
		if ((value >= to) == (from >= to)) {
			if ((value >= from) == (from >= to))
				return from;
			else
				return value;
		} else {
			return to;
		}
	}

	/***
	 * Format a array within a low value and a high value
	 * 
	 * @param low
	 * @param high
	 * @param values
	 */
	public static void range(double low, double high, double[] values) {
		if (low > high)
			return;
		for (int i = 0; i < values.length; i++) {
			values[i] = max(min(values[i], high), low);
		}
	}

	/***
	 * Parse a string to a date
	 * 
	 * @param date
	 * @return
	 */
	public static Date toDate(String date) {
		try {
			return formatter.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}

	/***
	 * Format a date to a string
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToString(Date date) {
		return formatter.format(date);
	}

	/***
	 * With a given factor, amplify a given array.
	 * 
	 * @param values
	 * @param factor
	 */
	public static void amplify(double[] values, int factor) {
		for (int i = 0; i < values.length; i++) {
			values[i] = values[i] * factor;
		}
	}

	/***
	 * With a given power during a elapsed time, calculate the capacity exchange
	 * from capacity
	 * 
	 * @param power
	 *            W
	 * @param elapedTime
	 *            H
	 * @param chargeEfficiency
	 *            %
	 * @param dischargeEfficiency
	 *            %
	 * @return Wh
	 */
	public static double capacity(double power, double elapsedTime,
			double chargeEfficiency, double dischargeEfficiency) {
		if (power > 0) {
			return power * elapsedTime * chargeEfficiency;
		} else {
			return power * elapsedTime / dischargeEfficiency;
		}
	}

	/***
	 * Calculate power with a given changed value of capacity. Positive
	 * represent a value of the stored energy by batteries, Negative represent a
	 * value of the provided energy by batteries
	 * 
	 * @param capacity
	 * @param elapsedTime
	 * @param chargeEfficiency
	 * @param dischargeEfficiency
	 * @return
	 */
	public static double power(double capacity, double elapsedTime,
			double chargeEfficiency, double dischargeEfficiency) {
		if (capacity < 0) {
			return capacity * dischargeEfficiency / elapsedTime;
		} else {
			return capacity / chargeEfficiency / elapsedTime;
		}
	}

	/***
	 * Reverse an array
	 * 
	 * @param values
	 * @return
	 */
	public static double[] reverse(double[] values) {
		double[] reverse = new double[values.length];
		for (int i = 0; i < values.length; i++) {
			reverse[i] = values[values.length - 1 - i];
		}
		return reverse;
	}

	/***
	 * Sum values in an array
	 * 
	 * @param values
	 * @return
	 */
	public static double sum(double[] values) {
		double sum = 0.0;
		for (double value : values) {
			sum += value;
		}
		return sum;
	}

	/***
	 * Sum values in an array based on the sign of value
	 * 
	 * @param values
	 * @param isPositive
	 * @return
	 */
	public static double sum(double[] values, boolean isPositive) {
		double sum = 0.0;
		for (double value : values) {
			if (value > 0 == isPositive)
				sum += value;
		}
		return sum;
	}

	/***
	 * Cumulatively sum value in an array
	 * 
	 * @param values
	 * @return
	 */
	public static double[] cumulative(double[] values) {
		double[] cumulatives = new double[values.length];
		for (int i = 0; i < values.length; i++) {
			cumulatives[i] = i == 0 ? values[i] : cumulatives[i - 1]
					+ values[i];
		}
		return cumulatives;
	}

	/***
	 * Find a bound of a cumulative array
	 * 
	 * @param values
	 * @param maximumCapacity
	 * @return [Upper, Lower]
	 */
	public static double[] bound(double[] cumulatives, double balance,
			double amplitude) {
		double[] bound = { balance, amplitude };
		for (int i = 0; i < cumulatives.length; i++) {
			if (isEqual(bound[Balance], bound[Amplitude]))
				break;
			// positive direction
			if ((bound[Amplitude] >= bound[Balance]) == (cumulatives[i] >= bound[Balance])) {
				bound[Balance] = direction(bound[Balance], bound[Amplitude],
						cumulatives[i]);
			}
			// negative direction
			else {
				bound[Amplitude] = direction(bound[Balance], bound[Amplitude],
						amplitude + cumulatives[i] - balance);
			}
		}
		return bound;
	}

	/***
	 * Check whether there is benefit with a given source price and destination
	 * price considering efficiency
	 * 
	 * @param sourcePrice
	 * @param destPrice
	 * @param chargeEfficiency
	 * @param dischargeEfficiency
	 * @return
	 */
	public static boolean hasBenefit(double sourcePrice, double destPrice,
			double chargeEfficiency, double dischargeEfficiency) {
		if (destPrice * chargeEfficiency * dischargeEfficiency / sourcePrice > 1)
			return true;
		return false;
	}

	/***
	 * Set log level
	 * 
	 * @param level
	 */
	public static void setLoggerLevel(Level level) {
		log.setUseParentHandlers(false);
		log.setLevel(Level.ALL);
		ConsoleHandler consoleHandler = null;
		Handler[] handlers = log.getHandlers();
		for (Handler handler : handlers) {
			if (handler instanceof ConsoleHandler) {
				consoleHandler = (ConsoleHandler) handler;
			}
		}
		if (consoleHandler == null) {
			consoleHandler = new ConsoleHandler();
			consoleHandler.setLevel(level);
			log.addHandler(consoleHandler);
		}
	}

	/***
	 * Get attribute of an object by a given field name
	 * 
	 * @param aClass
	 * @param aField
	 * @param instance
	 * @return
	 */
	public static Object getAttribute(Class<?> aClass, String aField,
			Object instance) {
		for (String prefix : prefixs) {
			try {
				Method aMethod = aClass.getMethod(
						prefix + Character.toUpperCase(aField.charAt(0))
								+ aField.substring(1), new Class[0]);
				return aMethod.invoke(instance, new Object[0]);
			} catch (NoSuchMethodException e) {
				continue;
			} catch (Exception e) {
				// Nothing
				return null;
			}
		}
		return null;
	}

}
