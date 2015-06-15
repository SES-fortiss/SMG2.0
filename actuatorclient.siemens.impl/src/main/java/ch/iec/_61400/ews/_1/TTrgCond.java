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
 * <p>Java class for tTrgCond complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tTrgCond">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="dchg" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="qchg" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="dupd" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="integrity" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="genrInterg" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tTrgCond")
public class TTrgCond {

    @XmlAttribute
    protected Boolean dchg;
    @XmlAttribute
    protected Boolean qchg;
    @XmlAttribute
    protected Boolean dupd;
    @XmlAttribute
    protected Boolean integrity;
    @XmlAttribute
    protected Boolean genrInterg;

    /**
     * Gets the value of the dchg property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isDchg() {
        if (dchg == null) {
            return false;
        } else {
            return dchg;
        }
    }

    /**
     * Sets the value of the dchg property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDchg(Boolean value) {
        this.dchg = value;
    }

    /**
     * Gets the value of the qchg property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isQchg() {
        if (qchg == null) {
            return false;
        } else {
            return qchg;
        }
    }

    /**
     * Sets the value of the qchg property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setQchg(Boolean value) {
        this.qchg = value;
    }

    /**
     * Gets the value of the dupd property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isDupd() {
        if (dupd == null) {
            return false;
        } else {
            return dupd;
        }
    }

    /**
     * Sets the value of the dupd property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDupd(Boolean value) {
        this.dupd = value;
    }

    /**
     * Gets the value of the integrity property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isIntegrity() {
        if (integrity == null) {
            return false;
        } else {
            return integrity;
        }
    }

    /**
     * Sets the value of the integrity property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIntegrity(Boolean value) {
        this.integrity = value;
    }

    /**
     * Gets the value of the genrInterg property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isGenrInterg() {
        if (genrInterg == null) {
            return false;
        } else {
            return genrInterg;
        }
    }

    /**
     * Sets the value of the genrInterg property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setGenrInterg(Boolean value) {
        this.genrInterg = value;
    }

}
