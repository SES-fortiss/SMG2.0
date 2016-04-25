
package ch.iec._61400.ews._1;

import java.util.ArrayList;
import java.util.List;
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
 *       &lt;sequence>
 *         &lt;element name="DSRef" type="{http://iec.ch/61400/ews/1.0/}tDataSetReference"/>
 *         &lt;element name="DSMemberRef" type="{http://iec.ch/61400/ews/1.0/}tFcdFcdaType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
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
    "dsRef",
    "dsMemberRef"
})
@XmlRootElement(name = "CreateDataSetRequest")
public class CreateDataSetRequest {

    @XmlElement(name = "DSRef", required = true)
    protected String dsRef;
    @XmlElement(name = "DSMemberRef", required = true)
    protected List<TFcdFcdaType> dsMemberRef;
    @XmlAttribute(name = "UUID")
    protected String uuid;
    @XmlAttribute(name = "AssocID", required = true)
    protected String assocID;

    /**
     * Gets the value of the dsRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDSRef() {
        return dsRef;
    }

    /**
     * Sets the value of the dsRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDSRef(String value) {
        this.dsRef = value;
    }

    /**
     * Gets the value of the dsMemberRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dsMemberRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDSMemberRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TFcdFcdaType }
     * 
     * 
     */
    public List<TFcdFcdaType> getDSMemberRef() {
        if (dsMemberRef == null) {
            dsMemberRef = new ArrayList<TFcdFcdaType>();
        }
        return this.dsMemberRef;
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
