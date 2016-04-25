
package ch.iec._61400.ews._1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tIEMcls.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="tIEMcls">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="DATA"/>
 *     &lt;enumeration value="DATASET"/>
 *     &lt;enumeration value="BRCB"/>
 *     &lt;enumeration value="URCB"/>
 *     &lt;enumeration value="LCB"/>
 *     &lt;enumeration value="LOG"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "tIEMcls")
@XmlEnum
public enum TIEMcls {

    DATA,
    DATASET,
    BRCB,
    URCB,
    LCB,
    LOG;

    public String value() {
        return name();
    }

    public static TIEMcls fromValue(String v) {
        return valueOf(v);
    }

}
