
package ch.iec._61400.ews._1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tDataAttrBasicType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="tDataAttrBasicType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Boolean"/>
 *     &lt;enumeration value="Int8"/>
 *     &lt;enumeration value="Int16"/>
 *     &lt;enumeration value="Int24"/>
 *     &lt;enumeration value="Int32"/>
 *     &lt;enumeration value="Int128"/>
 *     &lt;enumeration value="Int8U"/>
 *     &lt;enumeration value="Int16U"/>
 *     &lt;enumeration value="Int24U"/>
 *     &lt;enumeration value="Int32U"/>
 *     &lt;enumeration value="Float32"/>
 *     &lt;enumeration value="Float64"/>
 *     &lt;enumeration value="Enumerated"/>
 *     &lt;enumeration value="CodedEnum"/>
 *     &lt;enumeration value="OctetString"/>
 *     &lt;enumeration value="VisibleString"/>
 *     &lt;enumeration value="UnicodeString"/>
 *     &lt;enumeration value="Quality"/>
 *     &lt;enumeration value="TimeStamp"/>
 *     &lt;enumeration value="ArrayOfBoolean"/>
 *     &lt;enumeration value="ArrayOfInt8"/>
 *     &lt;enumeration value="ArrayOfInt16"/>
 *     &lt;enumeration value="ArrayOfInt24"/>
 *     &lt;enumeration value="ArrayOfInt32"/>
 *     &lt;enumeration value="ArrayOfInt128"/>
 *     &lt;enumeration value="ArrayOfInt8U"/>
 *     &lt;enumeration value="ArrayOfInt16U"/>
 *     &lt;enumeration value="ArrayOfInt24U"/>
 *     &lt;enumeration value="ArrayOfInt32U"/>
 *     &lt;enumeration value="ArrayOfFloat32"/>
 *     &lt;enumeration value="ArrayOfFloat64"/>
 *     &lt;enumeration value="ArrayOfEnum"/>
 *     &lt;enumeration value="ArrayOfCodedEnum"/>
 *     &lt;enumeration value="ArrayOfOctetString"/>
 *     &lt;enumeration value="ArrayOfVisibleString"/>
 *     &lt;enumeration value="ArrayOfUnicodeString"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "tDataAttrBasicType")
@XmlEnum
public enum TDataAttrBasicType {

    @XmlEnumValue("Boolean")
    BOOLEAN("Boolean"),
    @XmlEnumValue("Int8")
    INT_8("Int8"),
    @XmlEnumValue("Int16")
    INT_16("Int16"),
    @XmlEnumValue("Int24")
    INT_24("Int24"),
    @XmlEnumValue("Int32")
    INT_32("Int32"),
    @XmlEnumValue("Int128")
    INT_128("Int128"),
    @XmlEnumValue("Int8U")
    INT_8_U("Int8U"),
    @XmlEnumValue("Int16U")
    INT_16_U("Int16U"),
    @XmlEnumValue("Int24U")
    INT_24_U("Int24U"),
    @XmlEnumValue("Int32U")
    INT_32_U("Int32U"),
    @XmlEnumValue("Float32")
    FLOAT_32("Float32"),
    @XmlEnumValue("Float64")
    FLOAT_64("Float64"),
    @XmlEnumValue("Enumerated")
    ENUMERATED("Enumerated"),
    @XmlEnumValue("CodedEnum")
    CODED_ENUM("CodedEnum"),
    @XmlEnumValue("OctetString")
    OCTET_STRING("OctetString"),
    @XmlEnumValue("VisibleString")
    VISIBLE_STRING("VisibleString"),
    @XmlEnumValue("UnicodeString")
    UNICODE_STRING("UnicodeString"),
    @XmlEnumValue("Quality")
    QUALITY("Quality"),
    @XmlEnumValue("TimeStamp")
    TIME_STAMP("TimeStamp"),
    @XmlEnumValue("ArrayOfBoolean")
    ARRAY_OF_BOOLEAN("ArrayOfBoolean"),
    @XmlEnumValue("ArrayOfInt8")
    ARRAY_OF_INT_8("ArrayOfInt8"),
    @XmlEnumValue("ArrayOfInt16")
    ARRAY_OF_INT_16("ArrayOfInt16"),
    @XmlEnumValue("ArrayOfInt24")
    ARRAY_OF_INT_24("ArrayOfInt24"),
    @XmlEnumValue("ArrayOfInt32")
    ARRAY_OF_INT_32("ArrayOfInt32"),
    @XmlEnumValue("ArrayOfInt128")
    ARRAY_OF_INT_128("ArrayOfInt128"),
    @XmlEnumValue("ArrayOfInt8U")
    ARRAY_OF_INT_8_U("ArrayOfInt8U"),
    @XmlEnumValue("ArrayOfInt16U")
    ARRAY_OF_INT_16_U("ArrayOfInt16U"),
    @XmlEnumValue("ArrayOfInt24U")
    ARRAY_OF_INT_24_U("ArrayOfInt24U"),
    @XmlEnumValue("ArrayOfInt32U")
    ARRAY_OF_INT_32_U("ArrayOfInt32U"),
    @XmlEnumValue("ArrayOfFloat32")
    ARRAY_OF_FLOAT_32("ArrayOfFloat32"),
    @XmlEnumValue("ArrayOfFloat64")
    ARRAY_OF_FLOAT_64("ArrayOfFloat64"),
    @XmlEnumValue("ArrayOfEnum")
    ARRAY_OF_ENUM("ArrayOfEnum"),
    @XmlEnumValue("ArrayOfCodedEnum")
    ARRAY_OF_CODED_ENUM("ArrayOfCodedEnum"),
    @XmlEnumValue("ArrayOfOctetString")
    ARRAY_OF_OCTET_STRING("ArrayOfOctetString"),
    @XmlEnumValue("ArrayOfVisibleString")
    ARRAY_OF_VISIBLE_STRING("ArrayOfVisibleString"),
    @XmlEnumValue("ArrayOfUnicodeString")
    ARRAY_OF_UNICODE_STRING("ArrayOfUnicodeString");
    private final String value;

    TDataAttrBasicType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TDataAttrBasicType fromValue(String v) {
        for (TDataAttrBasicType c: TDataAttrBasicType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
