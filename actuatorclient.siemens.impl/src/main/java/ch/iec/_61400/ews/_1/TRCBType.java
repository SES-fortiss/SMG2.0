
package ch.iec._61400.ews._1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tRCBType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="tRCBType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="BRCB"/>
 *     &lt;enumeration value="URCB"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "tRCBType")
@XmlEnum
public enum TRCBType {

    BRCB,
    URCB;

    public String value() {
        return name();
    }

    public static TRCBType fromValue(String v) {
        return valueOf(v);
    }

}
