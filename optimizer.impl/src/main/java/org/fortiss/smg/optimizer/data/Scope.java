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
public class Scope {

	public static final boolean Demand = true;

	public static final boolean Supply = false;

	private boolean isDemand;

	private int from;

	private int to;

	public Scope(boolean isDemand, int from, int to) {
		super();
		this.isDemand = isDemand;
		this.from = from;
		this.to = to;
	}

	public boolean isDemand() {
		return isDemand;
	}

	public int getFrom() {
		return from;
	}

	public int getTo() {
		return to;
	}

	@Override
	public String toString() {
		return "[Scope From :" + from + ", To: " + to + ", isDemand: "
				+ isDemand + "]";
	}
}
