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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tEntryData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tEntryData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DSMemberRef" type="{http://iec.ch/61400/ews/1.0/}tFcdFcdaType" minOccurs="0"/>
 *         &lt;element name="Value" type="{http://iec.ch/61400/ews/1.0/}tDataAttributeValue" maxOccurs="unbounded"/>
 *         &lt;element name="ReasonCode" type="{http://iec.ch/61400/ews/1.0/}tTrgCond" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tEntryData", propOrder = {
    "dsMemberRef",
    "value",
    "reasonCode"
})
public class TEntryData {

    @XmlElement(name = "DSMemberRef")
    protected TFcdFcdaType dsMemberRef;
    @XmlElement(name = "Value", required = true)
    protected List<TDataAttributeValue> value;
    @XmlElement(name = "ReasonCode")
    protected TTrgCond reasonCode;

    /**
     * Gets the value of the dsMemberRef property.
     * 
     * @return
     *     possible object is
     *     {@link TFcdFcdaType }
     *     
     */
    public TFcdFcdaType getDSMemberRef() {
        return dsMemberRef;
    }

    /**
     * Sets the value of the dsMemberRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link TFcdFcdaType }
     *     
     */
    public void setDSMemberRef(TFcdFcdaType value) {
        this.dsMemberRef = value;
    }

    /**
     * Gets the value of the value property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the value property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getValue().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TDataAttributeValue }
     * 
     * 
     */
    public List<TDataAttributeValue> getValue() {
        if (value == null) {
            value = new ArrayList<TDataAttributeValue>();
        }
        return this.value;
    }

    /**
     * Gets the value of the reasonCode property.
     * 
     * @return
     *     possible object is
     *     {@link TTrgCond }
     *     
     */
    public TTrgCond getReasonCode() {
        return reasonCode;
    }

    /**
     * Sets the value of the reasonCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link TTrgCond }
     *     
     */
    public void setReasonCode(TTrgCond value) {
        this.reasonCode = value;
    }

}
