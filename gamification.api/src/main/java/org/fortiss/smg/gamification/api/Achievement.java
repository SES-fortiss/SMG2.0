/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
/**
 * 
 */
package org.fortiss.smg.gamification.api;

/**
 * @author Pahlke
 *
 */
public abstract class Achievement {
	final int id;
	final String title;
	final String description;
	final String imageFile;
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @return the imageFile
	 */
	public String getImageFile() {
		return imageFile;
	}
	public Achievement(int id, String title, String description,
			String imageFile) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.imageFile = imageFile;
	}
	
}
