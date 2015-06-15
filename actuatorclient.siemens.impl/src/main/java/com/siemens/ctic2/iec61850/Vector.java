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

import ch.iec._61400.ews._1.TDAType;
import ch.iec._61400.ews._1.TDataAttributeValue;

public class Vector extends NamedAttribute {
	private AnalogueValue mag;
	
	public Vector(TDAType daType) {
		super(daType.getDAName());

		List<Object> comp = daType.getDACompOrPrimComp();
		
		for (Object c : comp) {
			if (c instanceof TDAType) {
				TDAType t = (TDAType) c;
				
				if (t.getDAName().equals("mag")) {
					mag = new AnalogueValue(t);
				}
			}
		}
	}
	
	public Vector(TDataAttributeValue attrVal) {
		this(attrVal.getValue().getDAType());
	}
	
	public Vector(String name) {
		super(name);
		
		mag = new AnalogueValue("mag");
	}

	public AnalogueValue getMag() {
		return mag;
	}

	public void setMag(AnalogueValue mag) {
		this.mag = mag;
	}

	public void addTo(List<Object> list) {
		TDAType daType = new TDAType();
		daType.setDAName("mag");
		mag.addTo(daType.getDACompOrPrimComp());
		list.add(daType);
	}
}
