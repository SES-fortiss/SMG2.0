/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.analyzer.api;

/**
 * exception which is caused, if no data was found in the database
 * 
 * @author Ann Katrin Gibtner (annkatrin.gibtner@tum.de)
 *
 */
public class NoDataFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6607594016300161957L;

	public NoDataFoundException(String string) {
		super(string);
	}
}
