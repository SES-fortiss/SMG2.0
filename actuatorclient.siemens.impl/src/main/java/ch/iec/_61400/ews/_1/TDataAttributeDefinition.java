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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * DataAttributeDefinition
 * 
 * <p>Java class for tDataAttributeDefinition complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tDataAttributeDefinition">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DARef" type="{http://iec.ch/61400/ews/1.0/}tDAReference"/>
 *         &lt;element name="FC" type="{http://iec.ch/61400/ews/1.0/}tFC"/>
 *         &lt;element name="DAType" type="{http://iec.ch/61400/ews/1.0/}tDataAttrType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tDataAttributeDefinition", propOrder = {
    "daRef",
    "fc",
    "daType"
})
public class TDataAttributeDefinition {

    @XmlElement(name = "DARef", required = true)
    protected String daRef;
    @XmlElement(name = "FC", required = true)
    protected TFC fc;
    @XmlElement(name = "DAType", required = true)
    protected TDataAttrType daType;

    /**
     * Gets the value of the daRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDARef() {
        return daRef;
    }

    /**
     * Sets the value of the daRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDARef(String value) {
        this.daRef = value;
    }

    /**
     * Gets the value of the fc property.
     * 
     * @return
     *     possible object is
     *     {@link TFC }
     *     
     */
    public TFC getFC() {
        return fc;
    }

    /**
     * Sets the value of the fc property.
     * 
     * @param value
     *     allowed object is
     *     {@link TFC }
     *     
     */
    public void setFC(TFC value) {
        this.fc = value;
    }

    /**
     * Gets the value of the daType property.
     * 
     * @return
     *     possible object is
     *     {@link TDataAttrType }
     *     
     */
    public TDataAttrType getDAType() {
        return daType;
    }

    /**
     * Sets the value of the daType property.
     * 
     * @param value
     *     allowed object is
     *     {@link TDataAttrType }
     *     
     */
    public void setDAType(TDataAttrType value) {
        this.daType = value;
    }

}
