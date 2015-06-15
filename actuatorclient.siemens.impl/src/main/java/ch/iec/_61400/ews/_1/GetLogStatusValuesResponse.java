/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */

package ch.iec._61400.ews._1;

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
 *       &lt;choice>
 *         &lt;sequence>
 *           &lt;element name="OldEntrTm" type="{http://iec.ch/61400/ews/1.0/}tTimeStamp"/>
 *           &lt;element name="NewEntrTm" type="{http://iec.ch/61400/ews/1.0/}tTimeStamp"/>
 *           &lt;element name="OldEntr" type="{http://www.w3.org/2001/XMLSchema}unsignedInt"/>
 *           &lt;element name="NewEntr" type="{http://www.w3.org/2001/XMLSchema}unsignedInt"/>
 *         &lt;/sequence>
 *         &lt;element name="ServiceError" type="{http://iec.ch/61400/ews/1.0/}tServiceError" minOccurs="0"/>
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
    "oldEntrTm",
    "newEntrTm",
    "oldEntr",
    "newEntr",
    "serviceError"
})
@XmlRootElement(name = "GetLogStatusValuesResponse")
public class GetLogStatusValuesResponse {

    @XmlElement(name = "OldEntrTm")
    protected TTimeStamp oldEntrTm;
    @XmlElement(name = "NewEntrTm")
    protected TTimeStamp newEntrTm;
    @XmlElement(name = "OldEntr")
    @XmlSchemaType(name = "unsignedInt")
    protected Long oldEntr;
    @XmlElement(name = "NewEntr")
    @XmlSchemaType(name = "unsignedInt")
    protected Long newEntr;
    @XmlElement(name = "ServiceError")
    protected TServiceError serviceError;
    @XmlAttribute(name = "UUID")
    protected String uuid;
    @XmlAttribute(name = "AssocID", required = true)
    protected String assocID;

    /**
     * Gets the value of the oldEntrTm property.
     * 
     * @return
     *     possible object is
     *     {@link TTimeStamp }
     *     
     */
    public TTimeStamp getOldEntrTm() {
        return oldEntrTm;
    }

    /**
     * Sets the value of the oldEntrTm property.
     * 
     * @param value
     *     allowed object is
     *     {@link TTimeStamp }
     *     
     */
    public void setOldEntrTm(TTimeStamp value) {
        this.oldEntrTm = value;
    }

    /**
     * Gets the value of the newEntrTm property.
     * 
     * @return
     *     possible object is
     *     {@link TTimeStamp }
     *     
     */
    public TTimeStamp getNewEntrTm() {
        return newEntrTm;
    }

    /**
     * Sets the value of the newEntrTm property.
     * 
     * @param value
     *     allowed object is
     *     {@link TTimeStamp }
     *     
     */
    public void setNewEntrTm(TTimeStamp value) {
        this.newEntrTm = value;
    }

    /**
     * Gets the value of the oldEntr property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getOldEntr() {
        return oldEntr;
    }

    /**
     * Sets the value of the oldEntr property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setOldEntr(Long value) {
        this.oldEntr = value;
    }

    /**
     * Gets the value of the newEntr property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getNewEntr() {
        return newEntr;
    }

    /**
     * Sets the value of the newEntr property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setNewEntr(Long value) {
        this.newEntr = value;
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

}
