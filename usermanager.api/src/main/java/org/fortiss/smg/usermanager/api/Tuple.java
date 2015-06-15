/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.usermanager.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Tuple", propOrder = { "tupleKey", "value" })
public class Tuple {

    @XmlElement(required = true)
    protected String tupleKey;
    @XmlElement(required = true)
    protected String value;

    public Tuple() {
        // TODO Auto-generated constructor stub
    }

    public Tuple(String publicKey, String privateKey) {
        tupleKey = publicKey;
        value = privateKey;
    }

    /**
     * Gets the value of the tupleKey property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getTupleKey() {
        return tupleKey;
    }

    /**
     * Gets the value of the value property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the tupleKey property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setTupleKey(String value) {
        tupleKey = value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setValue(String value) {
        this.value = value;
    }

}
