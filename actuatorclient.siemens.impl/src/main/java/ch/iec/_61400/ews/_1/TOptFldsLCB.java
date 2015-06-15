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
 * Optional fields for LCB
 * 
 * <p>Java class for tOptFldsLCB complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tOptFldsLCB">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ReasFI" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tOptFldsLCB", propOrder = {
    "reasFI"
})
public class TOptFldsLCB {

    @XmlElement(name = "ReasFI")
    protected boolean reasFI;

    /**
     * Gets the value of the reasFI property.
     * 
     */
    public boolean isReasFI() {
        return reasFI;
    }

    /**
     * Sets the value of the reasFI property.
     * 
     */
    public void setReasFI(boolean value) {
        this.reasFI = value;
    }

}
