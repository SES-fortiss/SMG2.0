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
 * DATA-SET Member reference type
 * 
 * <p>Java class for tFcdFcdaType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tFcdFcdaType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Ref" type="{http://iec.ch/61400/ews/1.0/}tDAReference" minOccurs="0"/>
 *         &lt;element name="FC" type="{http://iec.ch/61400/ews/1.0/}tFC"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tFcdFcdaType", propOrder = {
    "ref",
    "fc"
})
public class TFcdFcdaType {

    @XmlElement(name = "Ref")
    protected String ref;
    @XmlElement(name = "FC", required = true)
    protected TFC fc;

    /**
     * Gets the value of the ref property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRef() {
        return ref;
    }

    /**
     * Sets the value of the ref property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRef(String value) {
        this.ref = value;
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
