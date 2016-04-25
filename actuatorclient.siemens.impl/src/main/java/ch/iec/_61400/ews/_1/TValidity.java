
package ch.iec._61400.ews._1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tValidity.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="tValidity">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="good"/>
 *     &lt;enumeration value="invalid"/>
 *     &lt;enumeration value="reserved"/>
 *     &lt;enumeration value="questionable"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "tValidity")
@XmlEnum
public enum TValidity {

    @XmlEnumValue("good")
    GOOD("good"),
    @XmlEnumValue("invalid")
    INVALID("invalid"),
    @XmlEnumValue("reserved")
    RESERVED("reserved"),
    @XmlEnumValue("questionable")
    QUESTIONABLE("questionable");
    private final String value;

    TValidity(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TValidity fromValue(String v) {
        for (TValidity c: TValidity.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
