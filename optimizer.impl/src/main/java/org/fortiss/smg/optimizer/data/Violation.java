/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.optimizer.data;

/***
 * 
 * @author Cheng Zhang
 * @version 1.0
 * 
 */
public class Violation {

	/** Violation Types */
	public static int OverSupply = 1;
	public static int ExtraSupply = 2;
	public static int ImpossibleDemand = 3;

	private int type;

	private double possible;

	private int from = -1;

	private int to = -1;

	public double getPossible() {
		return possible;
	}

	public Violation(int type, double possible, int from, int to) {
		super();
		this.type = type;
		this.possible = possible;
		this.from = from;
		this.to = to;
	}

	public int getFrom() {
		return from;
	}

	public int getTo() {
		return to;
	}

	public int getType() {
		return type;
	}

	@Override
	public String toString() {
		return "[Violation Type:" + type + " From:" + from + ", to:" + to
				+ ", Possible:" + possible + "]";
	}
}
