package com.siemens.ctic2.iec61850;

import java.util.List;

import ch.iec._61400.ews._1.TBasicType;
import ch.iec._61400.ews._1.TDAType;
import ch.iec._61400.ews._1.TDataAttributeValue;
import ch.iec._61400.ews._1.TDetailQual;
import ch.iec._61400.ews._1.TQuality;
import ch.iec._61400.ews._1.TSource;
import ch.iec._61400.ews._1.TValidity;

public class Quality extends NamedAttribute {
	private static TDetailQual noFail;
	
	static {
		noFail = new TDetailQual();
		noFail.setBadReference(false);
		noFail.setFailure(false);
		noFail.setInaccurate(false);
		noFail.setInconsistent(false);
		noFail.setOldData(false);
		noFail.setOscillatory(false);
		noFail.setOutOfRange(false);
		noFail.setOverflow(false);
	}
	
	private TQuality q;

	public Quality(String name) {
		this(name, TValidity.GOOD);
	}

	public Quality(String name, TValidity validity) {
		super(name);

		q = new TQuality();
		q.setOperatorBlock(false);
		q.setSource(TSource.PROCESS);
		q.setTest(false);
		q.setValidity(validity);
		q.setDetailQual(noFail);
	}

	public Quality(TDAType daType) {
		super(daType.getDAName());
		
		List<Object> comp = daType.getDACompOrPrimComp();
		
		for (Object c : comp) {
			if (c instanceof TBasicType) {
				TBasicType b = (TBasicType) c;
				q = b.getQuality();
				break;
			}
		}
	}
	
	public Quality(TDataAttributeValue attrVal) {
		this(attrVal.getValue().getDAType());
	}

	public void addTo(List<Object> list) {
		TBasicType basicType = new TBasicType();
		basicType.setQuality(q);

		list.add(basicType);
	}
}
