
package ch.iec._61400.ews._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * As defined in IEC 61400-25-2 clause 7.2.3 
 * 
 * <p>Java class for tQuality complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tQuality">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DetailQual" type="{http://iec.ch/61400/ews/1.0/}tDetailQual" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="Validity" use="required" type="{http://iec.ch/61400/ews/1.0/}tValidity" />
 *       &lt;attribute name="Source" use="required" type="{http://iec.ch/61400/ews/1.0/}tSource" />
 *       &lt;attribute name="test" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="operatorBlock" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tQuality", propOrder = {
    "detailQual"
})
public class TQuality {

    @XmlElement(name = "DetailQual")
    protected TDetailQual detailQual;
    @XmlAttribute(name = "Validity", required = true)
    protected TValidity validity;
    @XmlAttribute(name = "Source", required = true)
    protected TSource source;
    @XmlAttribute
    protected Boolean test;
    @XmlAttribute
    protected Boolean operatorBlock;

    /**
     * Gets the value of the detailQual property.
     * 
     * @return
     *     possible object is
     *     {@link TDetailQual }
     *     
     */
    public TDetailQual getDetailQual() {
        return detailQual;
    }

    /**
     * Sets the value of the detailQual property.
     * 
     * @param value
     *     allowed object is
     *     {@link TDetailQual }
     *     
     */
    public void setDetailQual(TDetailQual value) {
        this.detailQual = value;
    }

    /**
     * Gets the value of the validity property.
     * 
     * @return
     *     possible object is
     *     {@link TValidity }
     *     
     */
    public TValidity getValidity() {
        return validity;
    }

    /**
     * Sets the value of the validity property.
     * 
     * @param value
     *     allowed object is
     *     {@link TValidity }
     *     
     */
    public void setValidity(TValidity value) {
        this.validity = value;
    }

    /**
     * Gets the value of the source property.
     * 
     * @return
     *     possible object is
     *     {@link TSource }
     *     
     */
    public TSource getSource() {
        return source;
    }

    /**
     * Sets the value of the source property.
     * 
     * @param value
     *     allowed object is
     *     {@link TSource }
     *     
     */
    public void setSource(TSource value) {
        this.source = value;
    }

    /**
     * Gets the value of the test property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isTest() {
        if (test == null) {
            return false;
        } else {
            return test;
        }
    }

    /**
     * Sets the value of the test property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTest(Boolean value) {
        this.test = value;
    }

    /**
     * Gets the value of the operatorBlock property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isOperatorBlock() {
        if (operatorBlock == null) {
            return false;
        } else {
            return operatorBlock;
        }
    }

    /**
     * Sets the value of the operatorBlock property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setOperatorBlock(Boolean value) {
        this.operatorBlock = value;
    }

}
