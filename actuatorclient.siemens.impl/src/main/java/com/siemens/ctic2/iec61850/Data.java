/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package com.siemens.ctic2.iec61850;

public class Data {
	private String dataName;
	private String dataRef;
	
	protected Data(String dataRef) {
		setDataRef(dataRef);
	}
	
	public String getDataRef() {
		return dataRef;
	}
	
	public void setDataRef(String dataRef) {
		this.dataRef = dataRef;
		
		int lastDot = dataRef.lastIndexOf('.');
		
		if (lastDot < 0) {
			throw new IllegalArgumentException("Wrong data reference format: " + dataRef);
		}
		
		dataName = dataRef.substring(lastDot + 1);
	}
	
	public String getDataName() {
		return dataName;
	}
}
