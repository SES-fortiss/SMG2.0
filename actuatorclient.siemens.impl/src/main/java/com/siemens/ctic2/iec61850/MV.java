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

import ch.iec._61400.ews._1.TDataAttributeValue;

public class MV extends Data {
	private AnalogueValue mag;
	private Quality q;
	private Timestamp t;
	
	public MV(String dataRef) {
		super(dataRef);
	}
	
	public static MV extract(String dataRef, List<TDataAttributeValue> list) {
		MV mv = new MV(dataRef);
		boolean found = false;
		
		for (TDataAttributeValue attrVal : list) {
			String dataAttrRef = attrVal.getDataAttrRef();
			if (dataAttrRef.equals(dataRef + ".mag")) {
				mv.mag = new AnalogueValue(attrVal);
				found = true;
			}
			if (dataAttrRef.equals(dataRef + ".q")) {
				mv.q = new Quality(attrVal);
				found = true;
			}
			if (dataAttrRef.equals(dataRef + ".t")) {
				mv.t = new Timestamp(attrVal);
				found = true;
			}
		}
		
		return found ? mv : null;
	}

	public AnalogueValue getMag() {
		return mag;
	}

	public void setMag(AnalogueValue val) {
		mag = val;
	}
	
	public Quality getQ() {
		return q;
	}

	public void setQ(Quality q) {
		this.q = q;
	}

	public Timestamp getT() {
		return t;
	}

	public void setT(Timestamp t) {
		this.t = t;
	}
}
