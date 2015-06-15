/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package teachintelegraminfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TeachInTelegramInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TeachInTelegramInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="teachInTelegramString" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="TeachInTelegramDescription" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TeachInTelegramInfo", propOrder = {
    "teachInTelegramString",
    "teachInTelegramDescription"
})
public class TeachInTelegramInfo {

    @XmlElement(required = true)
    protected String teachInTelegramString;
    @XmlElement(name = "TeachInTelegramDescription", required = true)
    protected String teachInTelegramDescription;

    /**
     * Gets the value of the teachInTelegramString property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTeachInTelegramString() {
        return teachInTelegramString;
    }

    /**
     * Sets the value of the teachInTelegramString property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTeachInTelegramString(String value) {
        this.teachInTelegramString = value;
    }

    /**
     * Gets the value of the teachInTelegramDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTeachInTelegramDescription() {
        return teachInTelegramDescription;
    }

    /**
     * Sets the value of the teachInTelegramDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTeachInTelegramDescription(String value) {
        this.teachInTelegramDescription = value;
    }

}
