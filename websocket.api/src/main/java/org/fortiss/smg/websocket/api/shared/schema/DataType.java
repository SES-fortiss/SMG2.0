package org.fortiss.smg.websocket.api.shared.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DataType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DataType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="double"/>
 *     &lt;enumeration value="boolean"/>
 *     &lt;enumeration value="toggle"/>
 *     &lt;enumeration value="string"/>
 *     &lt;enumeration value="stopLastCMD"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DataType")
@XmlEnum
public enum DataType {

	@XmlEnumValue("double")
	DOUBLE("double"),
	@XmlEnumValue("boolean")
	BOOLEAN("boolean"),
	@XmlEnumValue("toggle")
	TOGGLE("toggle"),
	@XmlEnumValue("string")
	STRING("string"),
	@XmlEnumValue("stopLastCMD")
	STOP_LAST_CMD("stopLastCMD");
	private final String value;

	DataType(String v) {
		value = v;
	}

	public String value() {
		return value;
	}

	public static DataType fromValue(String v) {
		for (DataType c: DataType.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}

}
