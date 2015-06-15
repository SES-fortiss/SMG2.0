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
 *         &lt;element name="ControlObjectRef" type="{http://iec.ch/61400/ews/1.0/}tDAReference"/>
 *         &lt;element name="T" type="{http://iec.ch/61400/ews/1.0/}tTimeStamp"/>
 *         &lt;element name="Test" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
    "controlObjectRef",
    "t",
    "test"
})
@XmlRootElement(name = "CommandTerminationRequest")
public class CommandTerminationRequest {

    @XmlElement(name = "ControlObjectRef", required = true)
    protected String controlObjectRef;
    @XmlElement(name = "T", required = true)
    protected TTimeStamp t;
    @XmlElement(name = "Test")
    protected boolean test;
    @XmlAttribute(name = "UUID")
    protected String uuid;
    @XmlAttribute(name = "AssocID", required = true)
    protected String assocID;

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
     */
    public boolean isTest() {
        return test;
    }

    /**
     * Sets the value of the test property.
     * 
     */
    public void setTest(boolean value) {
        this.test = value;
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
