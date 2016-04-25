package com.siemens.ctic2.iec61850;

import java.util.List;

import ch.iec._61400.ews._1.TDAType;
import ch.iec._61400.ews._1.TDataAttribute;
import ch.iec._61400.ews._1.TDataAttributeValue;
import ch.iec._61400.ews._1.TFC;

public class CMV extends Data {
	private Vector cVal;
	private Quality q;
	private Timestamp t;
	
	public CMV(String dataRef) {
		super(dataRef);
	}
	
	public static CMV extract(String dataRef, List<TDataAttributeValue> list) {
		CMV cmv = new CMV(dataRef);
		boolean found = false;
		
		for (TDataAttributeValue attrVal : list) {
			String dataAttrRef = attrVal.getDataAttrRef();
			if (dataAttrRef.equals(dataRef + ".cVal")) {
				cmv.cVal = new Vector(attrVal);
				found = true;
			}
			if (dataAttrRef.equals(dataRef + ".q")) {
				cmv.q = new Quality(attrVal);
				found = true;
			}
			if (dataAttrRef.equals(dataRef + ".t")) {
				cmv.t = new Timestamp(attrVal);
				found = true;
			}
		}
		
		return found ? cmv : null;
	}

	public Vector getCVal() {
		return cVal;
	}

	public void setCVal(Vector val) {
		cVal = val;
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

	// XXX is unnecessary, because CMV is never set! Only for settable classes!
	public void addTo(List<TDataAttributeValue> attrVal) {
		// cVal
		TDAType daType = new TDAType();
		daType.setDAName("cVal");
		cVal.addTo(daType.getDACompOrPrimComp());
		
		TDataAttribute value = new TDataAttribute();
		value.setDAType(daType);
		value.setFC(TFC.MX);
		
		TDataAttributeValue val = new TDataAttributeValue();
		val.setDataAttrRef(getDataRef());
		val.setValue(value);
		
		attrVal.add(val);
		
		// q
		daType = new TDAType();
		daType.setDAName("q");
		q.addTo(daType.getDACompOrPrimComp());
		
		value = new TDataAttribute();
		value.setDAType(daType);
		value.setFC(TFC.MX);
		
		val = new TDataAttributeValue();
		val.setDataAttrRef(getDataRef());
		val.setValue(value);
		
		attrVal.add(val);
		
		// t
		daType = new TDAType();
		daType.setDAName("t");
		t.addTo(daType.getDACompOrPrimComp());
		
		value = new TDataAttribute();
		value.setDAType(daType);
		value.setFC(TFC.MX);
		
		val = new TDataAttributeValue();
		val.setDataAttrRef(getDataRef());
		val.setValue(value);
		
		attrVal.add(val);
	}
}
