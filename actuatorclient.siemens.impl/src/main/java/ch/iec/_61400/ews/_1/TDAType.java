/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */

package ch.iec._61400.ews._1;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 * Container for DAType
 * 
 * <p>Java class for tDAType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tDAType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice maxOccurs="unbounded">
 *           &lt;element name="DAComp" type="{http://iec.ch/61400/ews/1.0/}tDAType" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element name="PrimComp" type="{http://iec.ch/61400/ews/1.0/}tBasicType"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *       &lt;attribute name="DAName" use="required" type="{http://iec.ch/61400/ews/1.0/}tObjectName" />
 *       &lt;attribute name="ix" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tDAType", propOrder = {
    "daCompOrPrimComp"
})
public class TDAType {

    @XmlElements({
        @XmlElement(name = "PrimComp", type = TBasicType.class),
        @XmlElement(name = "DAComp", type = TDAType.class)
    })
    protected List<Object> daCompOrPrimComp;
    @XmlAttribute(name = "DAName", required = true)
    protected String daName;
    @XmlAttribute
    protected BigInteger ix;

    /**
     * Gets the value of the daCompOrPrimComp property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the daCompOrPrimComp property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDACompOrPrimComp().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TBasicType }
     * {@link TDAType }
     * 
     * 
     */
    public List<Object> getDACompOrPrimComp() {
        if (daCompOrPrimComp == null) {
            daCompOrPrimComp = new ArrayList<Object>();
        }
        return this.daCompOrPrimComp;
    }

    /**
     * Gets the value of the daName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDAName() {
        return daName;
    }

    /**
     * Sets the value of the daName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDAName(String value) {
        this.daName = value;
    }

    /**
     * Gets the value of the ix property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getIx() {
        return ix;
    }

    /**
     * Sets the value of the ix property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setIx(BigInteger value) {
        this.ix = value;
    }

}
