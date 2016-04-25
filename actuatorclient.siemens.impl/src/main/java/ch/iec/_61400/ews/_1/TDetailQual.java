
package ch.iec._61400.ews._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tDetailQual complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tDetailQual">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="overflow" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="outOfRange" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="badReference" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="oscillatory" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="failure" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="oldData" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="inconsistent" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="inaccurate" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tDetailQual")
public class TDetailQual {

    @XmlAttribute
    protected Boolean overflow;
    @XmlAttribute
    protected Boolean outOfRange;
    @XmlAttribute
    protected Boolean badReference;
    @XmlAttribute
    protected Boolean oscillatory;
    @XmlAttribute
    protected Boolean failure;
    @XmlAttribute
    protected Boolean oldData;
    @XmlAttribute
    protected Boolean inconsistent;
    @XmlAttribute
    protected Boolean inaccurate;

    /**
     * Gets the value of the overflow property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isOverflow() {
        if (overflow == null) {
            return false;
        } else {
            return overflow;
        }
    }

    /**
     * Sets the value of the overflow property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setOverflow(Boolean value) {
        this.overflow = value;
    }

    /**
     * Gets the value of the outOfRange property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isOutOfRange() {
        if (outOfRange == null) {
            return false;
        } else {
            return outOfRange;
        }
    }

    /**
     * Sets the value of the outOfRange property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setOutOfRange(Boolean value) {
        this.outOfRange = value;
    }

    /**
     * Gets the value of the badReference property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isBadReference() {
        if (badReference == null) {
            return false;
        } else {
            return badReference;
        }
    }

    /**
     * Sets the value of the badReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setBadReference(Boolean value) {
        this.badReference = value;
    }

    /**
     * Gets the value of the oscillatory property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isOscillatory() {
        if (oscillatory == null) {
            return false;
        } else {
            return oscillatory;
        }
    }

    /**
     * Sets the value of the oscillatory property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setOscillatory(Boolean value) {
        this.oscillatory = value;
    }

    /**
     * Gets the value of the failure property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isFailure() {
        if (failure == null) {
            return false;
        } else {
            return failure;
        }
    }

    /**
     * Sets the value of the failure property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setFailure(Boolean value) {
        this.failure = value;
    }

    /**
     * Gets the value of the oldData property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isOldData() {
        if (oldData == null) {
            return false;
        } else {
            return oldData;
        }
    }

    /**
     * Sets the value of the oldData property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setOldData(Boolean value) {
        this.oldData = value;
    }

    /**
     * Gets the value of the inconsistent property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isInconsistent() {
        if (inconsistent == null) {
            return false;
        } else {
            return inconsistent;
        }
    }

    /**
     * Sets the value of the inconsistent property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setInconsistent(Boolean value) {
        this.inconsistent = value;
    }

    /**
     * Gets the value of the inaccurate property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isInaccurate() {
        if (inaccurate == null) {
            return false;
        } else {
            return inaccurate;
        }
    }

    /**
     * Sets the value of the inaccurate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setInaccurate(Boolean value) {
        this.inaccurate = value;
    }

}
