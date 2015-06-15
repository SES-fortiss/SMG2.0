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

public class Point extends NamedAttribute {
	Float xVal;
	Float yVal;

	public Point(TDAType daType) {
		super(daType.getDAName());
		
		List<Object> comp = daType.getDACompOrPrimComp();
		
		for (Object c : comp) {
			if (c instanceof TDAType) {
				TDAType t = (TDAType) c;
				
				if (t.getDAName().equals("xVal")) {
					TBasicType p = (TBasicType) t.getDACompOrPrimComp().get(0);
					xVal = p.getFloat32();
				}
				if (t.getDAName().equals("yVal")) {
					TBasicType p = (TBasicType) t.getDACompOrPrimComp().get(0);
					yVal = p.getFloat32();
				}
			}
		}
	}
	
	public Point(TDataAttributeValue attrVal) {
		this(attrVal.getValue().getDAType());
	}
	
	public Point(String name) {
		super(name);
	}

	public void addTo(List<Object> list) {
		TBasicType basicType = new TBasicType();
		basicType.setFloat32(xVal);

		TDAType daType = new TDAType();
		daType.setDAName("xVal");
		daType.getDACompOrPrimComp().add(basicType);
		
		list.add(daType);

		basicType = new TBasicType();
		basicType.setFloat32(yVal);

		daType = new TDAType();
		daType.setDAName("yVal");
		daType.getDACompOrPrimComp().add(basicType);
		
		list.add(daType);
	}

	public Float getXVal() {
		return xVal;
	}

	public void setXVal(Float val) {
		xVal = val;
	}

	public Float getYVal() {
		return yVal;
	}

	public void setYVal(Float val) {
		yVal = val;
	}
}
