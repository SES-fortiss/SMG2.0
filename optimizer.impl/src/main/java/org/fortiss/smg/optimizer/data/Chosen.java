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
public class Chosen {

	/** Chosen Types */
	public static final int Used = 1;
	public static final int Excluded = 2;

	private int chosen;

	private double exchange;

	private int lastDemand;

	private int lastSupply;

	private int type = 0;

	public Chosen(int chosen, int lastSupply, int lastDemand, double exchange) {
		super();
		this.chosen = chosen;
		this.exchange = exchange;
		this.lastSupply = lastSupply;
		this.lastDemand = lastDemand;
	}

	public Chosen(Chosen chosen, int type) {
		this.chosen = chosen.chosen;
		this.exchange = chosen.exchange;
		this.lastDemand = chosen.lastDemand;
		this.lastSupply = chosen.lastSupply;
		this.type = type;
	}

	public int getChosen() {
		return chosen;
	}

	public double getExchange() {
		return exchange;
	}

	public int getLastDemand() {
		if (exchange < 0)
			return chosen;
		return lastDemand;
	}

	public int getType() {
		return type;
	}

	public int getLastSupply() {
		if (exchange > 0)
			return chosen;
		return lastSupply;
	}

	/** Update exchange of the chosen */
	public void updateExchange(double exchange) {
		this.exchange -= exchange;
	}

	public void setExchange(double exchange) {
		this.exchange = exchange;
	}

	@Override
	public String toString() {
		return "[Chosen Period: " + chosen + ", exchange: " + exchange
				+ ", last supply: " + lastSupply + ", last demand: "
				+ lastDemand + ", type: " + type + "]";
	}

	/** Export chosen array with predefined format */
	public static String[] exportChosens(Chosen[] chosens) {
		String[] exports = new String[chosens.length];
		for (int i = 0; i < chosens.length; i++) {
			exports[i] = chosens[i].chosen + "	" + chosens[i].exchange + "	"
					+ chosens[i].lastSupply + "	" + chosens[i].lastDemand + "	"
					+ chosens[i].type;
		}
		return exports;
	}

}
