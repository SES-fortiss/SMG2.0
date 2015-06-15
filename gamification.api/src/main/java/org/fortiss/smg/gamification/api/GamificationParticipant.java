/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.gamification.api;


public abstract class GamificationParticipant {

	protected int level;
	protected int score;
	protected String name;

	public GamificationParticipant(int level, int score, String name) {
		super();
		this.level = level;
		this.score = score;
		this.name = name;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @param level
	 *            the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * @param score
	 *            the score to set
	 */
	public void setScore(int score) {
		this.score = score;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	

	// protected abstract Map<Integer,List<Map<Integer,Double>>>
	// getWeekConsumption(long week);
	//
	// protected abstract Map<Integer,List<Map<Integer,Double>>>
	// getConsumptionForPeriod(long startDay, long endDay);

}
