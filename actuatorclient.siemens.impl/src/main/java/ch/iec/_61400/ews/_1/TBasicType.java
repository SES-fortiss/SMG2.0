
package ch.iec._61400.ews._1;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for tBasicType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tBasicType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="Boolean" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="int8" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *         &lt;element name="int16" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *         &lt;element name="int24" type="{http://iec.ch/61400/ews/1.0/}tInt24"/>
 *         &lt;element name="int32" type="{http://iec.ch/61400/ews/1.0/}tInt32"/>
 *         &lt;element name="int128" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="int8u" type="{http://www.w3.org/2001/XMLSchema}unsignedByte"/>
 *         &lt;element name="int16u" type="{http://www.w3.org/2001/XMLSchema}unsignedShort"/>
 *         &lt;element name="int24u" type="{http://iec.ch/61400/ews/1.0/}tInt24u"/>
 *         &lt;element name="int32u" type="{http://iec.ch/61400/ews/1.0/}tInt32u"/>
 *         &lt;element name="float32" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="float64" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="enum" type="{http://iec.ch/61400/ews/1.0/}tEnumerated"/>
 *         &lt;element name="codedEnum" type="{http://iec.ch/61400/ews/1.0/}tCodedEnum"/>
 *         &lt;element name="octetString" type="{http://www.w3.org/2001/XMLSchema}hexBinary"/>
 *         &lt;element name="visibleString" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="unicodeString" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="quality" type="{http://iec.ch/61400/ews/1.0/}tQuality"/>
 *         &lt;element name="timeStamp" type="{http://iec.ch/61400/ews/1.0/}tTimeStamp"/>
 *         &lt;element name="array" type="{http://iec.ch/61400/ews/1.0/}tArray"/>
 *       &lt;/choice>
 *       &lt;attribute name="pos" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tBasicType", propOrder = {
    "_boolean",
    "int8",
    "int16",
    "int24",
    "int32",
    "int128",
    "int8U",
    "int16U",
    "int24U",
    "int32U",
    "float32",
    "float64",
    "_enum",
    "codedEnum",
    "octetString",
    "visibleString",
    "unicodeString",
    "quality",
    "timeStamp",
    "array"
})
public class TBasicType {

    @XmlElement(name = "Boolean")
    protected Boolean _boolean;
    protected Byte int8;
    protected Short int16;
    protected Integer int24;
    protected Long int32;
    protected BigInteger int128;
    @XmlElement(name = "int8u")
    @XmlSchemaType(name = "unsignedByte")
    protected Short int8U;
    @XmlElement(name = "int16u")
    @XmlSchemaType(name = "unsignedShort")
    protected Integer int16U;
    @XmlElement(name = "int24u")
    protected Integer int24U;
    @XmlElement(name = "int32u")
    protected Long int32U;
    protected Float float32;
    protected Double float64;
    @XmlElement(name = "enum")
    protected TEnumerated _enum;
    protected TCodedEnum codedEnum;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(HexBinaryAdapter.class)
    @XmlSchemaType(name = "hexBinary")
    protected byte[] octetString;
    protected String visibleString;
    protected String unicodeString;
    protected TQuality quality;
    protected TTimeStamp timeStamp;
    protected TArray array;
    @XmlAttribute
    protected BigInteger pos;

    /**
     * Gets the value of the boolean property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isBoolean() {
        return _boolean;
    }

    /**
     * Sets the value of the boolean property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setBoolean(Boolean value) {
        this._boolean = value;
    }

    /**
     * Gets the value of the int8 property.
     * 
     * @return
     *     possible object is
     *     {@link Byte }
     *     
     */
    public Byte getInt8() {
        return int8;
    }

    /**
     * Sets the value of the int8 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Byte }
     *     
     */
    public void setInt8(Byte value) {
        this.int8 = value;
    }

    /**
     * Gets the value of the int16 property.
     * 
     * @return
     *     possible object is
     *     {@link Short }
     *     
     */
    public Short getInt16() {
        return int16;
    }

    /**
     * Sets the value of the int16 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Short }
     *     
     */
    public void setInt16(Short value) {
        this.int16 = value;
    }

    /**
     * Gets the value of the int24 property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getInt24() {
        return int24;
    }

    /**
     * Sets the value of the int24 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setInt24(Integer value) {
        this.int24 = value;
    }

    /**
     * Gets the value of the int32 property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getInt32() {
        return int32;
    }

    /**
     * Sets the value of the int32 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setInt32(Long value) {
        this.int32 = value;
    }

    /**
     * Gets the value of the int128 property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getInt128() {
        return int128;
    }

    /**
     * Sets the value of the int128 property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setInt128(BigInteger value) {
        this.int128 = value;
    }

    /**
     * Gets the value of the int8U property.
     * 
     * @return
     *     possible object is
     *     {@link Short }
     *     
     */
    public Short getInt8U() {
        return int8U;
    }

    /**
     * Sets the value of the int8U property.
     * 
     * @param value
     *     allowed object is
     *     {@link Short }
     *     
     */
    public void setInt8U(Short value) {
        this.int8U = value;
    }

    /**
     * Gets the value of the int16U property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getInt16U() {
        return int16U;
    }

    /**
     * Sets the value of the int16U property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setInt16U(Integer value) {
        this.int16U = value;
    }

    /**
     * Gets the value of the int24U property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getInt24U() {
        return int24U;
    }

    /**
     * Sets the value of the int24U property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setInt24U(Integer value) {
        this.int24U = value;
    }

    /**
     * Gets the value of the int32U property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getInt32U() {
        return int32U;
    }

    /**
     * Sets the value of the int32U property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setInt32U(Long value) {
        this.int32U = value;
    }

    /**
     * Gets the value of the float32 property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getFloat32() {
        return float32;
    }

    /**
     * Sets the value of the float32 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setFloat32(Float value) {
        this.float32 = value;
    }

    /**
     * Gets the value of the float64 property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getFloat64() {
        return float64;
    }

    /**
     * Sets the value of the float64 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setFloat64(Double value) {
        this.float64 = value;
    }

    /**
     * Gets the value of the enum property.
     * 
     * @return
     *     possible object is
     *     {@link TEnumerated }
     *     
     */
    public TEnumerated getEnum() {
        return _enum;
    }

    /**
     * Sets the value of the enum property.
     * 
     * @param value
     *     allowed object is
     *     {@link TEnumerated }
     *     
     */
    public void setEnum(TEnumerated value) {
        this._enum = value;
    }

    /**
     * Gets the value of the codedEnum property.
     * 
     * @return
     *     possible object is
     *     {@link TCodedEnum }
     *     
     */
    public TCodedEnum getCodedEnum() {
        return codedEnum;
    }

    /**
     * Sets the value of the codedEnum property.
     * 
     * @param value
     *     allowed object is
     *     {@link TCodedEnum }
     *     
     */
    public void setCodedEnum(TCodedEnum value) {
        this.codedEnum = value;
    }

    /**
     * Gets the value of the octetString property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public byte[] getOctetString() {
        return octetString;
    }

    /**
     * Sets the value of the octetString property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOctetString(byte[] value) {
        this.octetString = ((byte[]) value);
    }

    /**
     * Gets the value of the visibleString property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVisibleString() {
        return visibleString;
    }

    /**
     * Sets the value of the visibleString property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVisibleString(String value) {
        this.visibleString = value;
    }

    /**
     * Gets the value of the unicodeString property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnicodeString() {
        return unicodeString;
    }

    /**
     * Sets the value of the unicodeString property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnicodeString(String value) {
        this.unicodeString = value;
    }

    /**
     * Gets the value of the quality property.
     * 
     * @return
     *     possible object is
     *     {@link TQuality }
     *     
     */
    public TQuality getQuality() {
        return quality;
    }

    /**
     * Sets the value of the quality property.
     * 
     * @param value
     *     allowed object is
     *     {@link TQuality }
     *     
     */
    public void setQuality(TQuality value) {
        this.quality = value;
    }

    /**
     * Gets the value of the timeStamp property.
     * 
     * @return
     *     possible object is
     *     {@link TTimeStamp }
     *     
     */
    public TTimeStamp getTimeStamp() {
        return timeStamp;
    }

    /**
     * Sets the value of the timeStamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link TTimeStamp }
     *     
     */
    public void setTimeStamp(TTimeStamp value) {
        this.timeStamp = value;
    }

    /**
     * Gets the value of the array property.
     * 
     * @return
     *     possible object is
     *     {@link TArray }
     *     
     */
    public TArray getArray() {
        return array;
    }

    /**
     * Sets the value of the array property.
     * 
     * @param value
     *     allowed object is
     *     {@link TArray }
     *     
     */
    public void setArray(TArray value) {
        this.array = value;
    }

    /**
     * Gets the value of the pos property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getPos() {
        return pos;
    }

    /**
     * Sets the value of the pos property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setPos(BigInteger value) {
        this.pos = value;
    }

}
