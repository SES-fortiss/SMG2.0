
package ch.iec._61400.ews._1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

import org.fortiss.smg.containermanager.api.devices.SIUnitType;


/**
 * <p>Java class for tFC.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="tFC">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ST"/>
 *     &lt;enumeration value="MX"/>
 *     &lt;enumeration value="CO"/>
 *     &lt;enumeration value="SP"/>
 *     &lt;enumeration value="CF"/>
 *     &lt;enumeration value="DC"/>
 *     &lt;enumeration value="EX"/>
 *     &lt;enumeration value="BR"/>
 *     &lt;enumeration value="RP"/>
 *     &lt;enumeration value="LG"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "tFC")
@XmlEnum
public enum TFC {
	@XmlEnumValue("ST")
    ST,
    @XmlEnumValue("MX")
    MX,
    @XmlEnumValue("CO")
    CO,
    @XmlEnumValue("SP")
    SP,
    @XmlEnumValue("CF")
    CF,
    @XmlEnumValue("DC")
    DC,
    @XmlEnumValue("EX")
    EX,
    @XmlEnumValue("BR")
    BR,
    @XmlEnumValue("RP")
    RP,
    @XmlEnumValue("LG")
    LG;

    public String value() {
        return name();
    }

    public static TFC fromValue(String v) {
		for (TFC c: TFC.values()) {
			if (c.value().equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}

}
