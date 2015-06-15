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
 * SecSE = SecondsSinceEpoch, FracOfSec = FractionsOfSecond, TA = TimeAccuracy
 * 
 * <p>Java class for tTimeStamp complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tTimeStamp">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="SecSE" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
 *       &lt;attribute name="FracOfSec" use="required" type="{http://iec.ch/61400/ews/1.0/}tInt24u" />
 *       &lt;attribute name="TA" use="required" type="{http://iec.ch/61400/ews/1.0/}tTimeAccuracy" />
 *       &lt;attribute name="LSK" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="CF" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="CNS" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tTimeStamp")
public class TTimeStamp {
	

    @XmlAttribute(name = "SecSE", required = true)
// 	@XmlSchemaType(name = "unsignedInt")
    protected long secSE;
    @XmlAttribute(name = "FracOfSec", required = true)
    protected int fracOfSec;
    @XmlAttribute(name = "TA", required = true)
    protected int ta;
    @XmlAttribute(name = "LSK")
    protected Boolean lsk;
    @XmlAttribute(name = "CF")
    protected Boolean cf;
    @XmlAttribute(name = "CNS")
    protected Boolean cns;

    /**
     * Gets the value of the secSE property.
     * 
     */
    public long getSecSE() {
        return secSE;
    }

    /**
     * Sets the value of the secSE property.
     * 
     */
    public void setSecSE(long value) {
        this.secSE = value;
    }

    /**
     * Gets the value of the fracOfSec property.
     * 
     */
    public int getFracOfSec() {
        return this.fracOfSec;
    }

    /**
     * Sets the value of the fracOfSec property.
     * 
     */
    public void setFracOfSec(int value) {
        this.fracOfSec = value;
    }

    /**
     * Gets the value of the ta property.
     * 
     */
    public int getTA() {
        return this.ta;
    }

    /**
     * Sets the value of the ta property.
     * 
     */
    public void setTA(int value) {
        this.ta = value;
    }

    /**
     * Gets the value of the lsk property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isLSK() {
        if (lsk == null) {
            return false;
        } else {
            return this.lsk;
        }
    }

    /**
     * Sets the value of the lsk property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setLSK(Boolean value) {
        this.lsk = value;
    }

    /**
     * Gets the value of the cf property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isCF() {
        if (cf == null) {
            return false;
        } else {
            return cf;
        }
    }

    /**
     * Sets the value of the cf property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCF(Boolean value) {
        this.cf = value;
    }

    /**
     * Gets the value of the cns property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isCNS() {
        if (cns == null) {
            return false;
        } else {
            return this.cns;
        }
    }

    /**
     * Sets the value of the cns property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCNS(Boolean value) {
        this.cns = value;
    }

}
