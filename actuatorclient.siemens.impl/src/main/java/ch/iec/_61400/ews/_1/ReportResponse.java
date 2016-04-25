
package ch.iec._61400.ews._1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.Duration;


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
 *         &lt;sequence>
 *           &lt;element name="ReportFormat" type="{http://iec.ch/61400/ews/1.0/}tReportFormat" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element name="MaxRequestTime" type="{http://www.w3.org/2001/XMLSchema}duration"/>
 *           &lt;element name="MinRequestTime" type="{http://www.w3.org/2001/XMLSchema}duration" minOccurs="0"/>
 *         &lt;/sequence>
 *         &lt;element name="ServiceError" type="{http://iec.ch/61400/ews/1.0/}tServiceError" minOccurs="0"/>
 *       &lt;/choice>
 *       &lt;attribute name="UUID" type="{http://iec.ch/61400/ews/1.0/}tstring36" />
 *       &lt;attribute name="AssocID" use="required" type="{http://iec.ch/61400/ews/1.0/}tAssocID" />
 *       &lt;attribute name="moreFollows" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "reportFormat",
    "maxRequestTime",
    "minRequestTime",
    "serviceError"
})
@XmlRootElement(name = "ReportResponse")
public class ReportResponse {

    @XmlElement(name = "ReportFormat")
    protected List<TReportFormat> reportFormat;
    @XmlElement(name = "MaxRequestTime")
    protected Duration maxRequestTime;
    @XmlElement(name = "MinRequestTime")
    protected Duration minRequestTime;
    @XmlElement(name = "ServiceError")
    protected TServiceError serviceError;
    @XmlAttribute(name = "UUID")
    protected String uuid;
    @XmlAttribute(name = "AssocID", required = true)
    protected String assocID;
    @XmlAttribute
    protected Boolean moreFollows;

    /**
     * Gets the value of the reportFormat property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the reportFormat property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReportFormat().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TReportFormat }
     * 
     * 
     */
    public List<TReportFormat> getReportFormat() {
        if (reportFormat == null) {
            reportFormat = new ArrayList<TReportFormat>();
        }
        return this.reportFormat;
    }

    /**
     * Gets the value of the maxRequestTime property.
     * 
     * @return
     *     possible object is
     *     {@link Duration }
     *     
     */
    public Duration getMaxRequestTime() {
        return maxRequestTime;
    }

    /**
     * Sets the value of the maxRequestTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link Duration }
     *     
     */
    public void setMaxRequestTime(Duration value) {
        this.maxRequestTime = value;
    }

    /**
     * Gets the value of the minRequestTime property.
     * 
     * @return
     *     possible object is
     *     {@link Duration }
     *     
     */
    public Duration getMinRequestTime() {
        return minRequestTime;
    }

    /**
     * Sets the value of the minRequestTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link Duration }
     *     
     */
    public void setMinRequestTime(Duration value) {
        this.minRequestTime = value;
    }

    /**
     * Gets the value of the serviceError property.
     * 
     * @return
     *     possible object is
     *     {@link TServiceError }
     *     
     */
    public TServiceError getServiceError() {
        return serviceError;
    }

    /**
     * Sets the value of the serviceError property.
     * 
     * @param value
     *     allowed object is
     *     {@link TServiceError }
     *     
     */
    public void setServiceError(TServiceError value) {
        this.serviceError = value;
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

    /**
     * Gets the value of the moreFollows property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isMoreFollows() {
        if (moreFollows == null) {
            return false;
        } else {
            return moreFollows;
        }
    }

    /**
     * Sets the value of the moreFollows property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setMoreFollows(Boolean value) {
        this.moreFollows = value;
    }

}
