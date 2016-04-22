
package org.fortiss.smg.websocket.api.shared.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.fortiss.smg.containermanager.api.devices.SIUnitType;


/**
 * <p>Java class for DataTypeSpec complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DataTypeSpec">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="type" type="{http://fortiss.org/schema}DataType"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="rangeMin" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="rangeMax" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="trueSynonym" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="falseSynonym" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="toggleSynonym" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="unit" type="{http://fortiss.org/schema}SIUnitType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DataTypeSpec", propOrder = {
		"type",
		"description",
		"rangeMin",
		"rangeMax",
		"trueSynonym",
		"falseSynonym",
		"toggleSynonym",
		"unit"
})
public class DataTypeSpec {

	@XmlElement(required = true)
	private DataType type;
	private String description;
	private Double rangeMin;
	private Double rangeMax;
	private String trueSynonym;
	private String falseSynonym;
	private String toggleSynonym;
	private SIUnitType unit;

	/**
	 * Gets the value of the type property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link DataType }
	 * 
	 */
	public DataType getType() {
		return type;
	}

	/**
	 * Sets the value of the type property.
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link DataType }
	 * 
	 */
	public void setType(DataType value) {
		this.type = value;
	}

	/**
	 * Gets the value of the description property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link String }
	 * 
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the value of the description property.
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link String }
	 * 
	 */
	public void setDescription(String value) {
		this.description = value;
	}

	/**
	 * Gets the value of the rangeMin property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link Double }
	 * 
	 */
	public Double getRangeMin() {
		return rangeMin;
	}

	/**
	 * Sets the value of the rangeMin property.
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link Double }
	 * 
	 */
	public void setRangeMin(Double value) {
		this.rangeMin = value;
	}

	/**
	 * Gets the value of the rangeMax property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link Double }
	 * 
	 */
	public Double getRangeMax() {
		return rangeMax;
	}

	/**
	 * Sets the value of the rangeMax property.
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link Double }
	 * 
	 */
	public void setRangeMax(Double value) {
		this.rangeMax = value;
	}

	/**
	 * Gets the value of the trueSynonym property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link String }
	 * 
	 */
	public String getTrueSynonym() {
		return trueSynonym;
	}

	/**
	 * Sets the value of the trueSynonym property.
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link String }
	 * 
	 */
	public void setTrueSynonym(String value) {
		this.trueSynonym = value;
	}

	/**
	 * Gets the value of the falseSynonym property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link String }
	 * 
	 */
	public String getFalseSynonym() {
		return falseSynonym;
	}

	/**
	 * Sets the value of the falseSynonym property.
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link String }
	 * 
	 */
	public void setFalseSynonym(String value) {
		this.falseSynonym = value;
	}

	/**
	 * Gets the value of the toggleSynonym property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link String }
	 * 
	 */
	public String getToggleSynonym() {
		return toggleSynonym;
	}

	/**
	 * Sets the value of the toggleSynonym property.
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link String }
	 * 
	 */
	public void setToggleSynonym(String value) {
		this.toggleSynonym = value;
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

}
