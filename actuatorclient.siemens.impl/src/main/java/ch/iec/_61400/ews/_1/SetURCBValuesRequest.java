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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="URCBRef" type="{http://iec.ch/61400/ews/1.0/}tControlBlockReference"/>
 *         &lt;element name="RptID" type="{http://iec.ch/61400/ews/1.0/}tstring65" minOccurs="0"/>
 *         &lt;element name="RptEna" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="Resv" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="DatSet" type="{http://iec.ch/61400/ews/1.0/}tDataSetReference" minOccurs="0"/>
 *         &lt;element name="OptFlds" type="{http://iec.ch/61400/ews/1.0/}tOptFldsBRCB" minOccurs="0"/>
 *         &lt;element name="BufTm" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" minOccurs="0"/>
 *         &lt;element name="TrgOp" type="{http://iec.ch/61400/ews/1.0/}tTrgCond" minOccurs="0"/>
 *         &lt;element name="IntgPd" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" minOccurs="0"/>
 *         &lt;element name="GI" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="UUID" type="{http://iec.ch/61400/ews/1.0/}tstring36" />
 *       &lt;attribute name="AssocID" use="required" type="{http://iec.ch/61400/ews/1.0/}tAssocID" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "urcbRef",
    "rptID",
    "rptEna",
    "resv",
    "datSet",
    "optFlds",
    "bufTm",
    "trgOp",
    "intgPd",
    "gi"
})
@XmlRootElement(name = "SetURCBValuesRequest")
public class SetURCBValuesRequest {

    @XmlElement(name = "URCBRef", required = true)
    protected String urcbRef;
    @XmlElement(name = "RptID")
    protected String rptID;
    @XmlElement(name = "RptEna")
    protected Boolean rptEna;
    @XmlElement(name = "Resv")
    protected Boolean resv;
    @XmlElement(name = "DatSet")
    protected String datSet;
    @XmlElement(name = "OptFlds")
    protected TOptFldsBRCB optFlds;
    @XmlElement(name = "BufTm")
    @XmlSchemaType(name = "unsignedInt")
    protected Long bufTm;
    @XmlElement(name = "TrgOp")
    protected TTrgCond trgOp;
    @XmlElement(name = "IntgPd")
    @XmlSchemaType(name = "unsignedInt")
    protected Long intgPd;
    @XmlElement(name = "GI")
    protected Boolean gi;
    @XmlAttribute(name = "UUID")
    protected String uuid;
    @XmlAttribute(name = "AssocID", required = true)
    protected String assocID;

    /**
     * Gets the value of the urcbRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getURCBRef() {
        return urcbRef;
    }

    /**
     * Sets the value of the urcbRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setURCBRef(String value) {
        this.urcbRef = value;
    }

    /**
     * Gets the value of the rptID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRptID() {
        return rptID;
    }

    /**
     * Sets the value of the rptID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRptID(String value) {
        this.rptID = value;
    }

    /**
     * Gets the value of the rptEna property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRptEna() {
        return rptEna;
    }

    /**
     * Sets the value of the rptEna property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRptEna(Boolean value) {
        this.rptEna = value;
    }

    /**
     * Gets the value of the resv property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isResv() {
        return resv;
    }

    /**
     * Sets the value of the resv property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setResv(Boolean value) {
        this.resv = value;
    }

    /**
     * Gets the value of the datSet property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatSet() {
        return datSet;
    }

    /**
     * Sets the value of the datSet property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatSet(String value) {
        this.datSet = value;
    }

    /**
     * Gets the value of the optFlds property.
     * 
     * @return
     *     possible object is
     *     {@link TOptFldsBRCB }
     *     
     */
    public TOptFldsBRCB getOptFlds() {
        return optFlds;
    }

    /**
     * Sets the value of the optFlds property.
     * 
     * @param value
     *     allowed object is
     *     {@link TOptFldsBRCB }
     *     
     */
    public void setOptFlds(TOptFldsBRCB value) {
        this.optFlds = value;
    }

    /**
     * Gets the value of the bufTm property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getBufTm() {
        return bufTm;
    }

    /**
     * Sets the value of the bufTm property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setBufTm(Long value) {
        this.bufTm = value;
    }

    /**
     * Gets the value of the trgOp property.
     * 
     * @return
     *     possible object is
     *     {@link TTrgCond }
     *     
     */
    public TTrgCond getTrgOp() {
        return trgOp;
    }

    /**
     * Sets the value of the trgOp property.
     * 
     * @param value
     *     allowed object is
     *     {@link TTrgCond }
     *     
     */
    public void setTrgOp(TTrgCond value) {
        this.trgOp = value;
    }

    /**
     * Gets the value of the intgPd property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIntgPd() {
        return intgPd;
    }

    /**
     * Sets the value of the intgPd property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIntgPd(Long value) {
        this.intgPd = value;
    }

    /**
     * Gets the value of the gi property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isGI() {
        return gi;
    }

    /**
     * Sets the value of the gi property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setGI(Boolean value) {
        this.gi = value;
    }

    /**
     * Gets the value of the uuid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUUID() {
        return uuid;
    }

    /**
     * Sets the value of the uuid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUUID(String value) {
        this.uuid = value;
    }

    /**
     * Gets the value of the assocID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssocID() {
        return assocID;
    }

    /**
     * Sets the value of the assocID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssocID(String value) {
        this.assocID = value;
    }

}
