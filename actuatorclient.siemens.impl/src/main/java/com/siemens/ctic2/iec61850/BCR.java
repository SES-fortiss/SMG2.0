package com.siemens.ctic2.iec61850;

import java.math.BigInteger;
import java.util.List;

import ch.iec._61400.ews._1.TBasicType;
import ch.iec._61400.ews._1.TDataAttributeValue;

public class BCR extends Data {
	private BigInteger actVal;
	private Quality q;
	private Timestamp t;
	private float pulsQty;
	
	public BCR(String dataRef) {
		super(dataRef);
	}
	
	public static BCR extract(String dataRef, List<TDataAttributeValue> list) {
		BCR bcr = new BCR(dataRef);
		boolean found = false;
		
		for (TDataAttributeValue attrVal : list) {
			String dataAttrRef = attrVal.getDataAttrRef();
			if (dataAttrRef.equals(dataRef + ".actVal")) {
				List<Object> compOrPrimComp = attrVal.getValue().getDAType().getDACompOrPrimComp();
				if (compOrPrimComp.size() == 1) {
					Object o = compOrPrimComp.get(0);
					if (o instanceof TBasicType) {
						TBasicType basicType = (TBasicType) o;
						bcr.actVal = basicType.getInt128();
						found = true;
					}
				}
			}
			if (dataAttrRef.equals(dataRef + ".q")) {
				bcr.q = new Quality(attrVal);
				found = true;
			}
			if (dataAttrRef.equals(dataRef + ".t")) {
				bcr.t = new Timestamp(attrVal);
				found = true;
			}
			if (dataAttrRef.equals(dataRef + ".pulsQty")) {
				List<Object> compOrPrimComp = attrVal.getValue().getDAType().getDACompOrPrimComp();
				if (compOrPrimComp.size() == 1) {
					Object o = compOrPrimComp.get(0);
					if (o instanceof TBasicType) {
						TBasicType basicType = (TBasicType) o;
						bcr.pulsQty = basicType.getFloat32();
						found = true;
					}
				}
			}
		}
		
		return found ? bcr : null;
	}

	public BigInteger getActVal() {
		return actVal;
	}

	public void setActVal(BigInteger val) {
		actVal = val;
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

	public float getPulsQty() {
		return pulsQty;
	}

	public void setPulsQty(float pulsQty) {
		this.pulsQty = pulsQty;
	}
}
