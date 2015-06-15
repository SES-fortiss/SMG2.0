/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */

package ch.iec._61400.ews._1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
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
 *         &lt;element name="LogRef" type="{http://iec.ch/61400/ews/1.0/}tObjectReference"/>
 *         &lt;element name="StartTime" type="{http://iec.ch/61400/ews/1.0/}tTimeStamp" minOccurs="0"/>
 *         &lt;element name="EntryID" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" minOccurs="0"/>
 *         &lt;element name="DataFilter" type="{http://iec.ch/61400/ews/1.0/}tFcdFcdaType" maxOccurs="unbounded" minOccurs="0"/>
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
    "logRef",
    "startTime",
    "entryID",
    "dataFilter"
})
@XmlRootElement(name = "QueryLogAfterRequest")
public class QueryLogAfterRequest {

    @XmlElement(name = "LogRef", required = true)
    protected String logRef;
    @XmlElement(name = "StartTime")
    protected TTimeStamp startTime;
    @XmlElement(name = "EntryID")
    @XmlSchemaType(name = "unsignedInt")
    protected Long entryID;
    @XmlElement(name = "DataFilter")
    protected List<TFcdFcdaType> dataFilter;
    @XmlAttribute(name = "UUID")
    protected String uuid;
    @XmlAttribute(name = "AssocID", required = true)
    protected String assocID;

    /**
     * Gets the value of the logRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLogRef() {
        return logRef;
    }

    /**
     * Sets the value of the logRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLogRef(String value) {
        this.logRef = value;
    }

    /**
     * Gets the value of the startTime property.
     * 
     * @return
     *     possible object is
     *     {@link TTimeStamp }
     *     
     */
    public TTimeStamp getStartTime() {
        return startTime;
    }

    /**
     * Sets the value of the startTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link TTimeStamp }
     *     
     */
    public void setStartTime(TTimeStamp value) {
        this.startTime = value;
    }

    /**
     * Gets the value of the entryID property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getEntryID() {
        return entryID;
    }

    /**
     * Sets the value of the entryID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setEntryID(Long value) {
        this.entryID = value;
    }

    /**
     * Gets the value of the dataFilter property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dataFilter property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDataFilter().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TFcdFcdaType }
     * 
     * 
     */
    public List<TFcdFcdaType> getDataFilter() {
        if (dataFilter == null) {
            dataFilter = new ArrayList<TFcdFcdaType>();
        }
        return this.dataFilter;
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
