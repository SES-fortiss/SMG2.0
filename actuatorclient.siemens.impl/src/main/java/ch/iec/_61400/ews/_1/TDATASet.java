
package ch.iec._61400.ews._1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Container for DATA-SET object
 * 
 * <p>Java class for tDATASet complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tDATASet">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DSName" type="{http://iec.ch/61400/ews/1.0/}tObjectName"/>
 *         &lt;element name="DSMemberRef" type="{http://iec.ch/61400/ews/1.0/}tFcdFcdaType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tDATASet", propOrder = {
    "dsName",
    "dsMemberRef"
})
public class TDATASet {

    @XmlElement(name = "DSName", required = true)
    protected String dsName;
    @XmlElement(name = "DSMemberRef", required = true)
    protected List<TFcdFcdaType> dsMemberRef;

    /**
     * Gets the value of the dsName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDSName() {
        return dsName;
    }

    /**
     * Sets the value of the dsName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDSName(String value) {
        this.dsName = value;
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

}
