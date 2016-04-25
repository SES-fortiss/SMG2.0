
package ch.iec._61400.ews._1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for tReportFormat complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tReportFormat">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RptID" type="{http://iec.ch/61400/ews/1.0/}tstring65"/>
 *         &lt;element name="OptFlds" type="{http://iec.ch/61400/ews/1.0/}tOptFldsBRCB"/>
 *         &lt;element name="SqNum" type="{http://www.w3.org/2001/XMLSchema}unsignedShort" minOccurs="0"/>
 *         &lt;element name="SubSqNum" type="{http://www.w3.org/2001/XMLSchema}unsignedShort" minOccurs="0"/>
 *         &lt;element name="MoreSegFlw" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="DatSet" type="{http://iec.ch/61400/ews/1.0/}tDataSetReference" minOccurs="0"/>
 *         &lt;element name="BufOvfl" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="ConfRev" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" minOccurs="0"/>
 *         &lt;element name="TimeOfEntry" type="{http://iec.ch/61400/ews/1.0/}tTimeStamp" minOccurs="0"/>
 *         &lt;element name="EntryID" type="{http://iec.ch/61400/ews/1.0/}tEntryID" minOccurs="0"/>
 *         &lt;sequence>
 *           &lt;element name="EntryData" type="{http://iec.ch/61400/ews/1.0/}tEntryData" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "tReportFormat", propOrder = {
    "rptID",
    "optFlds",
    "sqNum",
    "subSqNum",
    "moreSegFlw",
    "datSet",
    "bufOvfl",
    "confRev",
    "timeOfEntry",
    "entryID",
    "entryData"
})
public class TReportFormat {

    @XmlElement(name = "RptID", required = true)
    protected String rptID;
    @XmlElement(name = "OptFlds", required = true)
    protected TOptFldsBRCB optFlds;
    @XmlElement(name = "SqNum")
    @XmlSchemaType(name = "unsignedShort")
    protected Integer sqNum;
    @XmlElement(name = "SubSqNum")
    @XmlSchemaType(name = "unsignedShort")
    protected Integer subSqNum;
    @XmlElement(name = "MoreSegFlw")
    protected boolean moreSegFlw;
    @XmlElement(name = "DatSet")
    protected String datSet;
    @XmlElement(name = "BufOvfl")
    protected Boolean bufOvfl;
    @XmlElement(name = "ConfRev")
    @XmlSchemaType(name = "unsignedInt")
    protected Long confRev;
    @XmlElement(name = "TimeOfEntry")
    protected TTimeStamp timeOfEntry;
    @XmlElement(name = "EntryID", type = String.class)
    @XmlJavaTypeAdapter(HexBinaryAdapter.class)
    protected byte[] entryID;
    @XmlElement(name = "EntryData")
    protected List<TEntryData> entryData;

    /**
     * Gets the value of the rptID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRptID() {
        return rptID;
    }

    /**
     * Sets the value of the rptID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRptID(String value) {
        this.rptID = value;
    }

    /**
     * Gets the value of the optFlds property.
     * 
     * @return
     *     possible object is
     *     {@link TOptFldsBRCB }
     *     
     */
    public TOptFldsBRCB getOptFlds() {
        return optFlds;
    }

    /**
     * Sets the value of the optFlds property.
     * 
     * @param value
     *     allowed object is
     *     {@link TOptFldsBRCB }
     *     
     */
    public void setOptFlds(TOptFldsBRCB value) {
        this.optFlds = value;
    }

    /**
     * Gets the value of the sqNum property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSqNum() {
        return sqNum;
    }

    /**
     * Sets the value of the sqNum property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSqNum(Integer value) {
        this.sqNum = value;
    }

    /**
     * Gets the value of the subSqNum property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSubSqNum() {
        return subSqNum;
    }

    /**
     * Sets the value of the subSqNum property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSubSqNum(Integer value) {
        this.subSqNum = value;
    }

    /**
     * Gets the value of the moreSegFlw property.
     * 
     */
    public boolean isMoreSegFlw() {
        return moreSegFlw;
    }

    /**
     * Sets the value of the moreSegFlw property.
     * 
     */
    public void setMoreSegFlw(boolean value) {
        this.moreSegFlw = value;
    }

    /**
     * Gets the value of the datSet property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatSet() {
        return datSet;
    }

    /**
     * Sets the value of the datSet property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatSet(String value) {
        this.datSet = value;
    }

    /**
     * Gets the value of the bufOvfl property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isBufOvfl() {
        return bufOvfl;
    }

    /**
     * Sets the value of the bufOvfl property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setBufOvfl(Boolean value) {
        this.bufOvfl = value;
    }

    /**
     * Gets the value of the confRev property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getConfRev() {
        return confRev;
    }

    /**
     * Sets the value of the confRev property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setConfRev(Long value) {
        this.confRev = value;
    }

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
