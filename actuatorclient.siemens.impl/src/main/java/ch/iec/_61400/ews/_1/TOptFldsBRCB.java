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
import javax.xml.bind.annotation.XmlType;


/**
 * Optional fields for BRCB
 * 
 * <p>Java class for tOptFldsBRCB complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tOptFldsBRCB">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="seqNum" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="timeStamp" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="reasonCode" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="dataSet" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="dataRef" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="bufOvfl" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="entryID" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="configRef" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tOptFldsBRCB", propOrder = {
    "seqNum",
    "timeStamp",
    "reasonCode",
    "dataSet",
    "dataRef",
    "bufOvfl",
    "entryID",
    "configRef"
})
public class TOptFldsBRCB {

    protected boolean seqNum;
    protected boolean timeStamp;
    protected boolean reasonCode;
    protected boolean dataSet;
    protected boolean dataRef;
    protected boolean bufOvfl;
    protected boolean entryID;
    protected boolean configRef;

    /**
     * Gets the value of the seqNum property.
     * 
     */
    public boolean isSeqNum() {
        return seqNum;
    }

    /**
     * Sets the value of the seqNum property.
     * 
     */
    public void setSeqNum(boolean value) {
        this.seqNum = value;
    }

    /**
     * Gets the value of the timeStamp property.
     * 
     */
    public boolean isTimeStamp() {
        return timeStamp;
    }

    /**
     * Sets the value of the timeStamp property.
     * 
     */
    public void setTimeStamp(boolean value) {
        this.timeStamp = value;
    }

    /**
     * Gets the value of the reasonCode property.
     * 
     */
    public boolean isReasonCode() {
        return reasonCode;
    }

    /**
     * Sets the value of the reasonCode property.
     * 
     */
    public void setReasonCode(boolean value) {
        this.reasonCode = value;
    }

    /**
     * Gets the value of the dataSet property.
     * 
     */
    public boolean isDataSet() {
        return dataSet;
    }

    /**
     * Sets the value of the dataSet property.
     * 
     */
    public void setDataSet(boolean value) {
        this.dataSet = value;
    }

    /**
     * Gets the value of the dataRef property.
     * 
     */
    public boolean isDataRef() {
        return dataRef;
    }

    /**
     * Sets the value of the dataRef property.
     * 
     */
    public void setDataRef(boolean value) {
        this.dataRef = value;
    }

    /**
     * Gets the value of the bufOvfl property.
     * 
     */
    public boolean isBufOvfl() {
        return bufOvfl;
    }

    /**
     * Sets the value of the bufOvfl property.
     * 
     */
    public void setBufOvfl(boolean value) {
        this.bufOvfl = value;
    }

    /**
     * Gets the value of the entryID property.
     * 
     */
    public boolean isEntryID() {
        return entryID;
    }

    /**
     * Sets the value of the entryID property.
     * 
     */
    public void setEntryID(boolean value) {
        this.entryID = value;
    }

    /**
     * Gets the value of the configRef property.
     * 
     */
    public boolean isConfigRef() {
        return configRef;
    }

    /**
     * Sets the value of the configRef property.
     * 
     */
    public void setConfigRef(boolean value) {
        this.configRef = value;
    }

}
