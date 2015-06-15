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
 *           &lt;element name="DATAname" type="{http://iec.ch/61400/ews/1.0/}tObjectName" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element name="DSname" type="{http://iec.ch/61400/ews/1.0/}tObjectName" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element name="BRCBname" type="{http://iec.ch/61400/ews/1.0/}tObjectName" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element name="URCBname" type="{http://iec.ch/61400/ews/1.0/}tObjectName" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element name="LCBname" type="{http://iec.ch/61400/ews/1.0/}tObjectName" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element name="LOGname" type="{http://iec.ch/61400/ews/1.0/}tObjectName" maxOccurs="unbounded" minOccurs="0"/>
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
    "datAname",
    "dSname",
    "brcBname",
    "urcBname",
    "lcBname",
    "loGname",
    "serviceError"
})
@XmlRootElement(name = "GetLogicalNodeDirectoryResponse")
public class GetLogicalNodeDirectoryResponse {

    @XmlElement(name = "DATAname")
    protected List<String> datAname;
    @XmlElement(name = "DSname")
    protected List<String> dSname;
    @XmlElement(name = "BRCBname")
    protected List<String> brcBname;
    @XmlElement(name = "URCBname")
    protected List<String> urcBname;
    @XmlElement(name = "LCBname")
    protected List<String> lcBname;
    @XmlElement(name = "LOGname")
    protected List<String> loGname;
    @XmlElement(name = "ServiceError")
    protected TServiceError serviceError;
    @XmlAttribute(name = "UUID")
    protected String uuid;
    @XmlAttribute(name = "AssocID", required = true)
    protected String assocID;

    /**
     * Gets the value of the datAname property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the datAname property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDATAname().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getDATAname() {
        if (datAname == null) {
            datAname = new ArrayList<String>();
        }
        return this.datAname;
    }

    /**
     * Gets the value of the dSname property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dSname property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDSname().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getDSname() {
        if (dSname == null) {
            dSname = new ArrayList<String>();
        }
        return this.dSname;
    }

    /**
     * Gets the value of the brcBname property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the brcBname property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBRCBname().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getBRCBname() {
        if (brcBname == null) {
            brcBname = new ArrayList<String>();
        }
        return this.brcBname;
    }

    /**
     * Gets the value of the urcBname property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the urcBname property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getURCBname().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getURCBname() {
        if (urcBname == null) {
            urcBname = new ArrayList<String>();
        }
        return this.urcBname;
    }

    /**
     * Gets the value of the lcBname property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lcBname property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLCBname().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getLCBname() {
        if (lcBname == null) {
            lcBname = new ArrayList<String>();
        }
        return this.lcBname;
    }

    /**
     * Gets the value of the loGname property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the loGname property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLOGname().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getLOGname() {
        if (loGname == null) {
            loGname = new ArrayList<String>();
        }
        return this.loGname;
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
