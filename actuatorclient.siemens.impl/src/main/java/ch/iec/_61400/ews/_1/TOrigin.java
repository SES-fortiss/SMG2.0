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
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * Control originator
 * 
 * <p>Java class for tOrigin complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tOrigin">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="orIdent" type="{http://iec.ch/61400/ews/1.0/}tOctetString64"/>
 *         &lt;element name="orCat" type="{http://iec.ch/61400/ews/1.0/}tOrcat"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tOrigin", propOrder = {
    "orIdent",
    "orCat"
})
public class TOrigin {

    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(HexBinaryAdapter.class)
    protected byte[] orIdent;
    @XmlElement(required = true)
    protected TOrcat orCat;

    /**
     * Gets the value of the orIdent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public byte[] getOrIdent() {
        return orIdent;
    }

    /**
     * Sets the value of the orIdent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrIdent(byte[] value) {
        this.orIdent = ((byte[]) value);
    }

    /**
     * Gets the value of the orCat property.
     * 
     * @return
     *     possible object is
     *     {@link TOrcat }
     *     
     */
    public TOrcat getOrCat() {
        return orCat;
    }

    /**
     * Sets the value of the orCat property.
     * 
     * @param value
     *     allowed object is
     *     {@link TOrcat }
     *     
     */
    public void setOrCat(TOrcat value) {
        this.orCat = value;
    }

}
