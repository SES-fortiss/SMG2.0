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

import ch.iec._61400.ews._1.TBasicType;
import ch.iec._61400.ews._1.TDAType;
import ch.iec._61400.ews._1.TDataAttributeValue;

public class AnalogueValue extends NamedAttribute {
	Float f;

	public AnalogueValue(TDAType daType) {
		super(daType.getDAName());
		
		List<Object> comp = daType.getDACompOrPrimComp();
		
		for (Object c : comp) {
			if (c instanceof TDAType) {
				TDAType t = (TDAType) c;
				
				if (t.getDAName().equals("f")) {
					TBasicType p = (TBasicType) t.getDACompOrPrimComp().get(0);
					f = p.getFloat32();
				}
			}
		}
	}
	
	public AnalogueValue(TDataAttributeValue attrVal) {
		this(attrVal.getValue().getDAType());
	}
	
	public AnalogueValue(String name) {
		super(name);
	}

	public Float getF() {
		return f;
	}

	public void setF(Float f) {
		this.f = f;
	}

	public void addTo(List<Object> list) {
		TBasicType basicType = new TBasicType();
		basicType.setFloat32(f);

		TDAType daType = new TDAType();
		daType.setDAName("f");
		daType.getDACompOrPrimComp().add(basicType);
		
		list.add(daType);
	}
}
