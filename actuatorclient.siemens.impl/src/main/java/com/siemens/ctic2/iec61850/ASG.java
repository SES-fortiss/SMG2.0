package com.siemens.ctic2.iec61850;

import java.util.List;

import ch.iec._61400.ews._1.TDAType;
import ch.iec._61400.ews._1.TDataAttribute;
import ch.iec._61400.ews._1.TDataAttributeValue;
import ch.iec._61400.ews._1.TFC;

public class ASG extends Data {
	private AnalogueValue setVal;
	
	public ASG(String dataRef) {
		super(dataRef);
	}
	
	public static ASG extract(String dataRef, List<TDataAttributeValue> list) {
		ASG asg = new ASG(dataRef);
		boolean found = false;
		
		for (TDataAttributeValue attrVal : list) {
			String dataAttrRef = attrVal.getDataAttrRef();
			if (dataAttrRef.equals(dataRef + ".setVal")) {
				asg.setVal = new AnalogueValue(attrVal);
				found = true;
			}
		}
		
		return found ? asg : null;
	}

	public AnalogueValue getSetVal() {
		return setVal;
	}

	public void addTo(String dataref, List<TDataAttributeValue> data) {
		TDAType daType = new TDAType();
		daType.setDAName("setVal");
		setVal.addTo(daType.getDACompOrPrimComp());
		
		TDataAttribute value = new TDataAttribute();
		value.setDAType(daType);
		value.setFC(TFC.SP);
		
		TDataAttributeValue val = new TDataAttributeValue();
		val.setDataAttrRef(getDataRef());
		val.setValue(value);
		
		data.add(val);
	}

	public void setSetVal(AnalogueValue setVal) {
		this.setVal = setVal;
	}

}
