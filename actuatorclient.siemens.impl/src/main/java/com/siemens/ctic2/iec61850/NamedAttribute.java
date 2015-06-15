/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package com.siemens.ctic2.iec61850;

import java.util.List;

public abstract class NamedAttribute {
	private String name;
	
	public NamedAttribute(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public abstract void addTo(List<Object> list);
}