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
 * DataAttrType
 * 
 * <p>Java class for tDataAttrType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tDataAttrType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BasicType" type="{http://iec.ch/61400/ews/1.0/}tDataAttrBasicType"/>
 *         &lt;element name="Len" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tDataAttrType", propOrder = {
    "basicType",
    "len"
})
public class TDataAttrType {

    @XmlElement(name = "BasicType", required = true)
    protected TDataAttrBasicType basicType;
    @XmlElement(name = "Len")
    protected Integer len;

    /**
     * Gets the value of the basicType property.
     * 
     * @return
     *     possible object is
     *     {@link TDataAttrBasicType }
     *     
     */
    public TDataAttrBasicType getBasicType() {
        return basicType;
    }

    /**
     * Sets the value of the basicType property.
     * 
     * @param value
     *     allowed object is
     *     {@link TDataAttrBasicType }
     *     
     */
    public void setBasicType(TDataAttrBasicType value) {
        this.basicType = value;
    }

    /**
     * Gets the value of the len property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getLen() {
        return len;
    }

    /**
     * Sets the value of the len property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setLen(Integer value) {
        this.len = value;
    }

}
