
package ch.iec._61400.ews._1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for tLOGEntry complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tLOGEntry">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TimeOfEntry" type="{http://iec.ch/61400/ews/1.0/}tTimeStamp"/>
 *         &lt;element name="EntryID" type="{http://iec.ch/61400/ews/1.0/}tEntryID"/>
 *         &lt;sequence maxOccurs="unbounded">
 *           &lt;element name="EntryData" type="{http://iec.ch/61400/ews/1.0/}tEntryData"/>
 *         &lt;/sequence>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tLOGEntry", propOrder = {
    "timeOfEntry",
    "entryID",
    "entryData"
})
public class TLOGEntry {

    @XmlElement(name = "TimeOfEntry", required = true)
    protected TTimeStamp timeOfEntry;
    @XmlElement(name = "EntryID", required = true, type = String.class)
    @XmlJavaTypeAdapter(HexBinaryAdapter.class)
    protected byte[] entryID;
    @XmlElement(name = "EntryData", required = true)
    protected List<TEntryData> entryData;

    /**
     * Gets the value of the timeOfEntry property.
     * 
     * @return
     *     possible object is
     *     {@link TTimeStamp }
     *     
     */
    public TTimeStamp getTimeOfEntry() {
        return timeOfEntry;
    }

    /**
     * Sets the value of the timeOfEntry property.
     * 
     * @param value
     *     allowed object is
     *     {@link TTimeStamp }
     *     
     */
    public void setTimeOfEntry(TTimeStamp value) {
        this.timeOfEntry = value;
    }

    /**
     * Gets the value of the entryID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public byte[] getEntryID() {
        return entryID;
    }

    /**
     * Sets the value of the entryID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntryID(byte[] value) {
        this.entryID = ((byte[]) value);
    }

    /**
     * Gets the value of the entryData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the entryData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEntryData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TEntryData }
     * 
     * 
     */
    public List<TEntryData> getEntryData() {
        if (entryData == null) {
            entryData = new ArrayList<TEntryData>();
        }
        return this.entryData;
    }

}
