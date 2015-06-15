/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.gamification.api;

import java.util.HashMap;

public class SingleGamificationUser extends GamificationParticipant {

	/**
	 * @param name
	 * @param id
	 */
	
	
	private int userManagerID;
	private String hexabusID;

	

	public SingleGamificationUser(int level, int score, String name,
			int userManagerID, String hexabusID) {
		super(level, score, name);
		this.userManagerID = userManagerID;
		this.hexabusID = hexabusID;
	}
	
	public SingleGamificationUser(int userManagerID, String name) {
		super(0, 0, name);
		this.userManagerID = userManagerID;
		this.hexabusID = "";
	}
	
	public SingleGamificationUser(SingleGamificationUser original) {
		super(original.getLevel(),original.getScore(), original.getName());
		userManagerID = original.userManagerID;
		hexabusID = original.hexabusID;
	}





	public HashMap<String, Object> serialize(){
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("level", this.level);
		map.put("score", this.score);
		map.put("name", this.name);
		map.put("hexabusid", this.hexabusID);
		map.put("usermanagerid", this.userManagerID);				
		return map;
	}

	/**
	 * @return the userManagerID
	 */
	public int getUserManagerID() {
		return userManagerID;
	}

	/**
	 * @param userManagerID the userManagerID to set
	 */
	public void setUserManagerID(int userManagerID) {
		this.userManagerID = userManagerID;
	}

	/**
	 * @return the hexabusID
	 */
	public String getHexabusID() {
		return hexabusID;
	}

	/**
	 * @param hexabusID the hexabusID to set
	 */
	public void setHexabusID(String hexabusID) {
		this.hexabusID = hexabusID;
	}
	
}
