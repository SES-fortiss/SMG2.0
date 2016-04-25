package com.siemens.ctic2.iec61850;

import java.util.List;

import ch.iec._61400.ews._1.TBasicType;
import ch.iec._61400.ews._1.TDAType;
import ch.iec._61400.ews._1.TDataAttribute;
import ch.iec._61400.ews._1.TDataAttributeValue;
import ch.iec._61400.ews._1.TFC;

public class ING extends Data {
	private long setVal;
	
	public ING(String dataRef) {
		super(dataRef);
	}
	
	public static ING extract(String dataRef, List<TDataAttributeValue> list) {
		ING ing = new ING(dataRef);
		boolean found = false;
		
		for (TDataAttributeValue attrVal : list) {
			String dataAttrRef = attrVal.getDataAttrRef();
			if (dataAttrRef.equals(dataRef + ".setVal")) {
				TBasicType p = (TBasicType) attrVal.getValue().getDAType().getDACompOrPrimComp().get(0);
				ing.setVal = p.getInt32();
				found = true;
			}
		}
		
		return found ? ing : null;
	}

	public long getSetVal() {
		return setVal;
	}

	public void addTo(String dataref, List<TDataAttributeValue> data) {
		TDataAttributeValue attrval = new TDataAttributeValue();
		attrval.setDataAttrRef(dataref + ".setVal");
		data.add(attrval);
		
		TDataAttribute attr = new TDataAttribute();
		attrval.setValue(attr);
		
		TBasicType basicType = new TBasicType();
		basicType.setInt32(setVal);

		TDAType daType = new TDAType();
		daType.setDAName("setVal");
		daType.getDACompOrPrimComp().add(basicType);
		
		attr.setDAType(daType);
		attr.setFC(TFC.SP);
	}

	public void setSetVal(long setVal) {
		this.setVal = setVal;
	}

}
