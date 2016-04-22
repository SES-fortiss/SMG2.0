
package org.fortiss.smg.websocket.api.shared.schema;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.fortiss.smg.containermanager.api.devices.SIUnitType;
import org.fortiss.smg.websocket.api.shared.w3._2001.xmlschema.Adapter1;



/**
 * <p>Java class for DoubleDataPoint complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DoubleDataPoint">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="timestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="unit" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DoubleDataPoint", propOrder = {
    "timestamp",
    "value",
    "unit",
    "maxAbsError"
})
public class DoubleDataPoint {

    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "dateTime")
    protected Date timestamp;
    
    protected double value;
    @XmlElement(required = true)
    protected SIUnitType unit;
    
	protected double maxAbsError;

	public DoubleDataPoint() {
	}
	
	
	
    public DoubleDataPoint(Date timestamp, double value, SIUnitType unit,
			double maxAbsError) {
		super();
		this.timestamp = timestamp;
		this.value = value;
		this.unit = unit;
		this.maxAbsError = maxAbsError;
	}



	/**
     * Gets the value of the timestamp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the value of the timestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimestamp(Date value) {
        this.timestamp = value;
    }

    /**
     * Gets the value of the value property.
     * 
     */
    public double getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     */
    public void setValue(double value) {
        this.value = value;
    }

    /**
     * Gets the value of the unit property.
     * 
     * @return
     *     possible object is
     *     {@link SIUnitType }
     *     
     */
    public SIUnitType getUnit() {
        return unit;
    }

    /**
     * Sets the value of the unit property.
     * 
     * @param value
     *     allowed object is
     *     {@link SIUnitType }
     *     
     */
    public void setUnit(SIUnitType value) {
        this.unit = value;
    }

    /**
     * Gets the value of the maxAbsError property.
     * 
     */
    public double getMaxAbsError() {
        return maxAbsError;
    }

    /**
     * Sets the value of the maxAbsError property.
     * 
     */
    public void setMaxAbsError(double value) {
        this.maxAbsError = value;
    }
}
