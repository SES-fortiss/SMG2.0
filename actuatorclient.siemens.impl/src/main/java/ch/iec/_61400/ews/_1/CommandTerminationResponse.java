
package ch.iec._61400.ews._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="notFinished" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;sequence>
 *           &lt;element name="ControlObjectRef" type="{http://iec.ch/61400/ews/1.0/}tDAReference"/>
 *           &lt;element name="T" type="{http://iec.ch/61400/ews/1.0/}tTimeStamp"/>
 *           &lt;element name="Test" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *           &lt;element name="AddCause" type="{http://iec.ch/61400/ews/1.0/}tAddCause" minOccurs="0"/>
 *         &lt;/sequence>
 *       &lt;/choice>
 *       &lt;attribute name="UUID" type="{http://iec.ch/61400/ews/1.0/}tstring36" />
 *       &lt;attribute name="AssocID" use="required" type="{http://iec.ch/61400/ews/1.0/}tAssocID" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "notFinished",
    "controlObjectRef",
    "t",
    "test",
    "addCause"
})
@XmlRootElement(name = "CommandTerminationResponse")
public class CommandTerminationResponse {

    protected Boolean notFinished;
    @XmlElement(name = "ControlObjectRef")
    protected String controlObjectRef;
    @XmlElement(name = "T")
    protected TTimeStamp t;
    @XmlElement(name = "Test")
    protected Boolean test;
    @XmlElement(name = "AddCause")
    protected String addCause;
    @XmlAttribute(name = "UUID")
    protected String uuid;
    @XmlAttribute(name = "AssocID", required = true)
    protected String assocID;

    /**
     * Gets the value of the notFinished property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isNotFinished() {
        return notFinished;
    }

    /**
     * Sets the value of the notFinished property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setNotFinished(Boolean value) {
        this.notFinished = value;
    }

    /**
     * Gets the value of the controlObjectRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getControlObjectRef() {
        return controlObjectRef;
    }

    /**
     * Sets the value of the controlObjectRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setControlObjectRef(String value) {
        this.controlObjectRef = value;
    }

    /**
     * Gets the value of the t property.
     * 
     * @return
     *     possible object is
     *     {@link TTimeStamp }
     *     
     */
    public TTimeStamp getT() {
        return t;
    }

    /**
     * Sets the value of the t property.
     * 
     * @param value
     *     allowed object is
     *     {@link TTimeStamp }
     *     
     */
    public void setT(TTimeStamp value) {
        this.t = value;
    }

    /**
     * Gets the value of the test property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTest() {
        return test;
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
     * Gets the value of the addCause property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddCause() {
        return addCause;
    }

    /**
     * Sets the value of the addCause property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddCause(String value) {
        this.addCause = value;
    }

    /**
     * Gets the value of the uuid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUUID() {
        return uuid;
    }

    /**
     * Sets the value of the uuid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUUID(String value) {
        this.uuid = value;
    }

    /**
     * Gets the value of the assocID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssocID() {
        return assocID;
    }

    /**
     * Sets the value of the assocID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssocID(String value) {
        this.assocID = value;
    }

}
