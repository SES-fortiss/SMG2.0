
package ch.iec._61400.ews._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Container for a DataAttribute 
 * 
 * <p>Java class for tDataAttributeValue complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tDataAttributeValue">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DataAttrRef" type="{http://iec.ch/61400/ews/1.0/}tDAReference"/>
 *         &lt;element name="Value" type="{http://iec.ch/61400/ews/1.0/}tDataAttribute"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tDataAttributeValue", propOrder = {
    "dataAttrRef",
    "value"
})
public class TDataAttributeValue {

    @XmlElement(name = "DataAttrRef", required = true)
    protected String dataAttrRef;
    @XmlElement(name = "Value", required = true)
    protected TDataAttribute value;

    /**
     * Gets the value of the dataAttrRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataAttrRef() {
        return dataAttrRef;
    }

    /**
     * Sets the value of the dataAttrRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataAttrRef(String value) {
        this.dataAttrRef = value;
    }

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link TDataAttribute }
     *     
     */
    public TDataAttribute getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link TDataAttribute }
     *     
     */
    public void setValue(TDataAttribute value) {
        this.value = value;
    }

}
