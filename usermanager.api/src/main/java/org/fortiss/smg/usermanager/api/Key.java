/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.usermanager.api;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "publicKey", "userId", "devId", "os", "appName",
	"devName", "lastSeen" })
@XmlRootElement(name = "Key")
public class Key {

    @XmlElement(required = true)
    protected String publicKey;
    protected long userId;
    @XmlElement(required = true)
    protected int devId;
    @XmlElement(required = true)
    protected String os;
    @XmlElement(required = true)
    protected String appName;
    @XmlElement(required = true)
    protected String devName;
    @XmlElement(required = true)
    protected long lastSeen;
    @XmlElement(required = true)
    protected String macAddress;

    @Override
    public Key clone() {
	try {
	    Key key = (Key) super.clone();
	    key.setDevId(devId);
	    key.setDevName(devName);
	    key.setOS(os);
	    key.setLastSeen(lastSeen);
	    key.setPublicKey(publicKey);
	    key.setUserId(userId);
	    key.setOS(os);
	    key.setMACAddress(macAddress);
	    return key;
	} catch (CloneNotSupportedException e) {
	    throw new RuntimeException("this should never happen", e);
	}

    }

    /**
     * Gets the value of the appName property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getAppName() {
	return appName;
    }

    /**
     * Gets the value of the devId property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public int getDevId() {
	return devId;
    }

    /**
     * Gets the value of the devName property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getDevName() {
	return devName;
    }

    /**
     * Gets the value of the lastSeen property.
     * 
     * @return possible object is {@link XMLGregorianCalendar }
     * 
     */
    public long getLastSeen() {
	return lastSeen;
    }

    public String getOS() {
	return os;
    }

    /**
     * Gets the value of the publicKey property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getPublicKey() {
	return publicKey;
    }

    /**
     * Gets the value of the userId property.
     * 
     */
    public long getUserId() {
	return userId;
    }
    
    /**
     * Gets the value of the macAddress property.
     * 
     */
    public String getMACAddress() {
	return macAddress;
    }

    /**
     * Sets the value of the appName property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setAppName(String value) {
	appName = value;
    }

    /**
     * Sets the value of the devId property.
     * 
     * @param deviceID
     *            allowed object is {@link String }
     * 
     */
    public void setDevId(int deviceID) {
	devId = deviceID;
    }

    /**
     * Sets the value of the devName property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setDevName(String value) {
	devName = value;
    }

    /**
     * Sets the value of the lastSeen property.
     * 
     * @param value
     *            allowed object is {@link XMLGregorianCalendar }
     * 
     */
    public void setLastSeen(long value) {
	lastSeen = value;
    }

    public void setOS(String os) {
	this.os = os;
    }

    /**
     * Sets the value of the publicKey property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setPublicKey(String value) {
	publicKey = value;
    }

    /**
     * Sets the value of the userId property.
     * 
     */
    public void setUserId(long value) {
	userId = value;
    }
    /**
     * Sets the value of the macAddress property.
     * 
     */
    public void setMACAddress(String value) {
	macAddress = value;
    }
}
