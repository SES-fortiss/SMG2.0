
package ch.iec._61400.ews._1;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * <p>Java class for tOrcat complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tOrcat">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://iec.ch/61400/ews/1.0/>tOrcatValue">
 *       &lt;attribute name="ord" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tOrcat", propOrder = {
    "value"
})
public class TOrcat {

    @XmlValue
    protected TOrcatValue value;
    @XmlAttribute
    protected BigInteger ord;

    /**
     * Originator Category
     * 
     * @return
     *     possible object is
     *     {@link TOrcatValue }
     *     
     */
    public TOrcatValue getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link TOrcatValue }
     *     
     */
    public void setValue(TOrcatValue value) {
        this.value = value;
    }

    /**
     * Gets the value of the ord property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getOrd() {
        return ord;
    }

    /**
     * Sets the value of the ord property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setOrd(BigInteger value) {
        this.ord = value;
    }

}
