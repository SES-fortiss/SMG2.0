package org.fortiss.smg.websocket.api.shared.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for Device complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="Device">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="deviceId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="deviceSpec" type="{http://fortiss.org/schema}DeviceSpec"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Device", propOrder = { "deviceId", "deviceSpec" })
public class Device {

    @XmlElement(required = true)
    protected String deviceId;
    @XmlElement(required = true)
    protected DeviceSpec deviceSpec;

    public Device(String deviceId) {
        this.deviceId = deviceId;
    }

    public Device() {
    }

    /**
     * Gets the value of the deviceId property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * Sets the value of the deviceId property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setDeviceId(String value) {
        this.deviceId = value;
    }

    /**
     * Gets the value of the deviceSpec property.
     * 
     * @return possible object is {@link DeviceSpec }
     * 
     */
    public DeviceSpec getDeviceSpec() {
        return deviceSpec;
    }

    /**
     * Sets the value of the deviceSpec property.
     * 
     * @param value
     *            allowed object is {@link DeviceSpec }
     * 
     */
    public void setDeviceSpec(DeviceSpec value) {
        this.deviceSpec = value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((deviceId == null) ? 0 : deviceId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Device other = (Device) obj;
        if (deviceId == null) {
            if (other.deviceId != null)
                return false;
        } else if (!deviceId.equals(other.deviceId))
            return false;
        return true;
    }

}
