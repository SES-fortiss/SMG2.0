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
import ch.iec._61400.ews._1.TDataAttribute;
import ch.iec._61400.ews._1.TDataAttributeValue;
import ch.iec._61400.ews._1.TEnumerated;
import ch.iec._61400.ews._1.TFC;

public class CSG extends Data {
	private int numPts;
	private List<Point> crvPts;
	private TEnumerated xUnit;
	private TEnumerated yUnit;
	private String xD;
	private String yD;
	private long maxPts;
	
	public CSG(String dataRef) {
		super(dataRef);
	}
	
	public static CSG extract(String dataRef, List<TDataAttributeValue> list) {
		CSG asg = new CSG(dataRef);
		boolean found = false;
		
		for (TDataAttributeValue attrVal : list) {
			String dataAttrRef = attrVal.getDataAttrRef();
			if (dataAttrRef.equals(dataRef + ".numPts")) {
				TBasicType p = (TBasicType) attrVal.getValue().getDAType().getDACompOrPrimComp().get(0);
				asg.numPts = p.getInt16U();
				found = true;
			}
		}
		// TODO add all the other attributes
		
		return found ? asg : null;
	}


	public void addTo(String dataref, List<TDataAttributeValue> data) {
		TDAType daType = new TDAType();
		daType.setDAName("setVal");
//		setVal.addTo(daType.getDACompOrPrimComp());
		
		TDataAttribute value = new TDataAttribute();
		value.setDAType(daType);
		value.setFC(TFC.SP);
		
		TDataAttributeValue val = new TDataAttributeValue();
		val.setDataAttrRef(getDataRef());
		val.setValue(value);
		
		data.add(val);
	}

}
