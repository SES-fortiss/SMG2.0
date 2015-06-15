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
import javax.xml.bind.annotation.XmlType;


/**
 * Container for Check Conditions
 * 
 * <p>Java class for tCheck complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tCheck">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="Syncrocheck" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="InterlockCheck" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tCheck")
public class TCheck {

    @XmlAttribute(name = "Syncrocheck")
    protected Boolean syncrocheck;
    @XmlAttribute(name = "InterlockCheck")
    protected Boolean interlockCheck;

    /**
     * Gets the value of the syncrocheck property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isSyncrocheck() {
        if (syncrocheck == null) {
            return false;
        } else {
            return syncrocheck;
        }
    }

    /**
     * Sets the value of the syncrocheck property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSyncrocheck(Boolean value) {
        this.syncrocheck = value;
    }

    /**
     * Gets the value of the interlockCheck property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isInterlockCheck() {
        if (interlockCheck == null) {
            return false;
        } else {
            return interlockCheck;
        }
    }

    /**
     * Sets the value of the interlockCheck property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setInterlockCheck(Boolean value) {
        this.interlockCheck = value;
    }

}
