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
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;sequence>
 *             &lt;element name="LogEna" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *             &lt;element name="DatSet" type="{http://iec.ch/61400/ews/1.0/}tDataSetReference"/>
 *             &lt;element name="TrgOp" type="{http://iec.ch/61400/ews/1.0/}tTrgCond"/>
 *             &lt;element name="IntgPd" type="{http://www.w3.org/2001/XMLSchema}unsignedInt"/>
 *             &lt;element name="LogRef" type="{http://iec.ch/61400/ews/1.0/}tObjectReference"/>
 *           &lt;/sequence>
 *           &lt;element name="ServiceError" type="{http://iec.ch/61400/ews/1.0/}tServiceError" minOccurs="0"/>
 *         &lt;/choice>
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
    "logEna",
    "datSet",
    "trgOp",
    "intgPd",
    "logRef",
    "serviceError"
})
@XmlRootElement(name = "GetLCBValuesResponse")
public class GetLCBValuesResponse {

    @XmlElement(name = "LogEna")
    protected Boolean logEna;
    @XmlElement(name = "DatSet")
    protected String datSet;
    @XmlElement(name = "TrgOp")
    protected TTrgCond trgOp;
    @XmlElement(name = "IntgPd")
    @XmlSchemaType(name = "unsignedInt")
    protected Long intgPd;
    @XmlElement(name = "LogRef")
    protected String logRef;
    @XmlElement(name = "ServiceError")
    protected TServiceError serviceError;
    @XmlAttribute(name = "UUID")
    protected String uuid;
    @XmlAttribute(name = "AssocID", required = true)
    protected String assocID;

    /**
     * Gets the value of the logEna property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isLogEna() {
        return logEna;
    }

    /**
     * Sets the value of the logEna property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setLogEna(Boolean value) {
        this.logEna = value;
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
     * Gets the value of the trgOp property.
     * 
     * @return
     *     possible object is
     *     {@link TTrgCond }
     *     
     */
    public TTrgCond getTrgOp() {
        return trgOp;
    }

    /**
     * Sets the value of the trgOp property.
     * 
     * @param value
     *     allowed object is
     *     {@link TTrgCond }
     *     
     */
    public void setTrgOp(TTrgCond value) {
        this.trgOp = value;
    }

    /**
     * Gets the value of the intgPd property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIntgPd() {
        return intgPd;
    }

    /**
     * Sets the value of the intgPd property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIntgPd(Long value) {
        this.intgPd = value;
    }

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
