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
import javax.xml.bind.annotation.XmlType;


/**
 * Container for DataAttributes
 * 
 * <p>Java class for tDataAttribute complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tDataAttribute">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DAType" type="{http://iec.ch/61400/ews/1.0/}tDAType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="FC" use="required" type="{http://iec.ch/61400/ews/1.0/}tFC" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tDataAttribute", propOrder = {
    "daType"
})
public class TDataAttribute {

    @XmlElement(name = "DAType", required = true)
    protected TDAType daType;
    @XmlAttribute(name = "FC", required = true)
    protected TFC fc;

    /**
     * Gets the value of the daType property.
     * 
     * @return
     *     possible object is
     *     {@link TDAType }
     *     
     */
    public TDAType getDAType() {
        return daType;
    }

    /**
     * Sets the value of the daType property.
     * 
     * @param value
     *     allowed object is
     *     {@link TDAType }
     *     
     */
    public void setDAType(TDAType value) {
        this.daType = value;
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

}
