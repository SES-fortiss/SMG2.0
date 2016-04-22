
package org.fortiss.smg.websocket.api.shared.schema;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DeviceSpec complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DeviceSpec">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="specId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="deviceCategory" type="{http://fortiss.org/schema}DeviceCategory" maxOccurs="unbounded"/>
 *         &lt;element name="supportedEvents" type="{http://fortiss.org/schema}DataTypeSpec" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="supportedCommands" type="{http://fortiss.org/schema}DataTypeSpec" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DeviceSpec", propOrder = {
    "specId",
    "deviceCategory",
    "supportedEvents",
    "supportedCommands"
})
public class DeviceSpec {

    @XmlElement(required = true)
    protected String specId;
    @XmlElement(required = true)
    protected List<DeviceCategory> deviceCategory;
    protected List<DataTypeSpec> supportedEvents;
    protected List<DataTypeSpec> supportedCommands;

    /**
     * Gets the value of the specId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpecId() {
        return specId;
    }

    /**
     * Sets the value of the specId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpecId(String value) {
        this.specId = value;
    }

    /**
     * Gets the value of the deviceCategory property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the deviceCategory property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDeviceCategory().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DeviceCategory }
     * 
     * 
     */
    public List<DeviceCategory> getDeviceCategory() {
        if (deviceCategory == null) {
            deviceCategory = new ArrayList<DeviceCategory>();
        }
        return this.deviceCategory;
    }

    /**
     * Gets the value of the supportedEvents property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the supportedEvents property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSupportedEvents().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DataTypeSpec }
     * 
     * 
     */
    public List<DataTypeSpec> getSupportedEvents() {
        if (supportedEvents == null) {
            supportedEvents = new ArrayList<DataTypeSpec>();
        }
        return this.supportedEvents;
    }

    /**
     * Gets the value of the supportedCommands property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the supportedCommands property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSupportedCommands().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DataTypeSpec }
     * 
     * 
     */
    public List<DataTypeSpec> getSupportedCommands() {
        if (supportedCommands == null) {
            supportedCommands = new ArrayList<DataTypeSpec>();
        }
        return this.supportedCommands;
    }

}
