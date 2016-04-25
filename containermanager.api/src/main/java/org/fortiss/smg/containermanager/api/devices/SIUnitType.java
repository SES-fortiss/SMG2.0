package org.fortiss.smg.containermanager.api.devices;


import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

@XmlType(name = "SIUnitType")
@XmlEnum
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SIUnitType {

	@XmlEnumValue("Celsius")
	CELSIUS("Celsius"),
	LUX("LUX"),
	@XmlEnumValue("m")
	M("m"),
	@XmlEnumValue("kg")
	KG("kg"),
	@XmlEnumValue("s")
	S("s"),
	A("A"),
	K("K"),
	@XmlEnumValue("Percent")
	PERCENT("Percent"),
	@XmlEnumValue("Wh")
	WH("Wh"),
	W("W"),
	V("V"),
	VA("VA"),
	@XmlEnumValue("VAr")
	VAR("VAr"),
	@XmlEnumValue("VArh")
	VARH("VArh"),
	@XmlEnumValue("None")
	NONE("None"),
	@XmlEnumValue("Hz")
	HZ("Hz"), 
	@XmlEnumValue("Ah")
	AH("Ah"), 
	@XmlEnumValue("Bool")
	BOOL ("Bool"),
	@XmlEnumValue("hPa")
	HPA ("hPa"),
	@XmlEnumValue("dB")
	DB ("dB"),
	@XmlEnumValue("Switchposition")
	SWITCHPOSITION ("Switchposition"),
	@XmlEnumValue("Count")
	Count ("Count");
	private final String value;

	SIUnitType(String v) {
		value = v;
	}

	public String value() {
		return value;
	}

	public static SIUnitType fromValue(String v) {
		for (SIUnitType c: SIUnitType.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}	


	public String getType() {
		return value;
	}
	
	@JsonCreator
	  public static SIUnitType fromString(String text) {
		    if (text != null) {
		      for (SIUnitType b : SIUnitType.values()) {
		        if (text.equals(b.name())) {
		          return b;
		        }
		      }
		    }
		    return null;
		  }
	
	
}
