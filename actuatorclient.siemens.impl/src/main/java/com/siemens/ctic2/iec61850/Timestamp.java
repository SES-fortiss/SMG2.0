package com.siemens.ctic2.iec61850;

import java.util.Date;
import java.util.List;

import ch.iec._61400.ews._1.TBasicType;
import ch.iec._61400.ews._1.TDAType;
import ch.iec._61400.ews._1.TDataAttributeValue;
import ch.iec._61400.ews._1.TTimeStamp;

public class Timestamp extends NamedAttribute {
	
	private TTimeStamp t;

	public Timestamp(String name) {
		this(name, System.currentTimeMillis());
	}

	public Timestamp(String name, Date date) {
		this(name, date.getTime());
	}

	public Timestamp(String name, long time) {
		super(name);
		
		setTime(time);

		t.setCF(false);
		t.setCNS(false);
		t.setLSK(false);
		t.setTA(7);
	}
	
	public Timestamp(TDAType daType) {
		super(daType.getDAName());
		
		List<Object> comp = daType.getDACompOrPrimComp();
		
		for (Object c : comp) {
			if (c instanceof TBasicType) {
				TBasicType b = (TBasicType) c;
				t = b.getTimeStamp();
				break;
			}
		}
	}
	
	public Timestamp(TDataAttributeValue attrVal) {
		this(attrVal.getValue().getDAType());
	}

	public long getTime() {
		return t.getSecSE() * 1000 + t.getFracOfSec() * 1000L / 0x1000000L;
	}
	
	public void setTime(long time) {
		t.setSecSE(time / 1000);
		t.setFracOfSec((int)(0x1000000L*(time % 1000)/1000));
	}
	
	public void addTo(List<Object> list) {
		TBasicType basicType = new TBasicType();
		basicType.setTimeStamp(t);

		list.add(basicType);
	}
}
