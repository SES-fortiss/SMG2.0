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
 * Control value
 * 
 * <p>Java class for tControlValue complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tControlValue">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ctlVal" type="{http://iec.ch/61400/ews/1.0/}tDAType"/>
 *         &lt;element name="origin" type="{http://iec.ch/61400/ews/1.0/}tOrigin"/>
 *         &lt;element name="operTm" type="{http://iec.ch/61400/ews/1.0/}tTimeStamp" minOccurs="0"/>
 *         &lt;element name="ctlNum" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tControlValue", propOrder = {
    "ctlVal",
    "origin",
    "operTm",
    "ctlNum"
})
public class TControlValue {

    @XmlElement(required = true)
    protected TDAType ctlVal;
    @XmlElement(required = true)
    protected TOrigin origin;
    protected TTimeStamp operTm;
    protected int ctlNum;

    /**
     * Gets the value of the ctlVal property.
     * 
     * @return
     *     possible object is
     *     {@link TDAType }
     *     
     */
    public TDAType getCtlVal() {
        return ctlVal;
    }

    /**
     * Sets the value of the ctlVal property.
     * 
     * @param value
     *     allowed object is
     *     {@link TDAType }
     *     
     */
    public void setCtlVal(TDAType value) {
        this.ctlVal = value;
    }

    /**
     * Gets the value of the origin property.
     * 
     * @return
     *     possible object is
     *     {@link TOrigin }
     *     
     */
    public TOrigin getOrigin() {
        return origin;
    }

    /**
     * Sets the value of the origin property.
     * 
     * @param value
     *     allowed object is
     *     {@link TOrigin }
     *     
     */
    public void setOrigin(TOrigin value) {
        this.origin = value;
    }

    /**
     * Gets the value of the operTm property.
     * 
     * @return
     *     possible object is
     *     {@link TTimeStamp }
     *     
     */
    public TTimeStamp getOperTm() {
        return operTm;
    }

    /**
     * Sets the value of the operTm property.
     * 
     * @param value
     *     allowed object is
     *     {@link TTimeStamp }
     *     
     */
    public void setOperTm(TTimeStamp value) {
        this.operTm = value;
    }

    /**
     * Gets the value of the ctlNum property.
     * 
     */
    public int getCtlNum() {
        return ctlNum;
    }

    /**
     * Sets the value of the ctlNum property.
     * 
     */
    public void setCtlNum(int value) {
        this.ctlNum = value;
    }

}
