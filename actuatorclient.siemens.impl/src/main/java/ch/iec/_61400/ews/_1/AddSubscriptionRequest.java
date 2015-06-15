/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */

package ch.iec._61400.ews._1;

import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element name="RCBRef" type="{http://iec.ch/61400/ews/1.0/}tControlBlockReference"/>
 *         &lt;element name="RCBType" type="{http://iec.ch/61400/ews/1.0/}tRCBType"/>
 *         &lt;element name="RptID" type="{http://iec.ch/61400/ews/1.0/}tstring65" minOccurs="0"/>
 *         &lt;element name="RptEna" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="DatSet" type="{http://iec.ch/61400/ews/1.0/}tDataSetReference" minOccurs="0"/>
 *         &lt;element name="OptFlds" type="{http://iec.ch/61400/ews/1.0/}tOptFldsBRCB" minOccurs="0"/>
 *         &lt;element name="BufTm" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" minOccurs="0"/>
 *         &lt;element name="TrgOp" type="{http://iec.ch/61400/ews/1.0/}tTrgCond" minOccurs="0"/>
 *         &lt;element name="IntgPd" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" minOccurs="0"/>
 *         &lt;element name="DSMbrRef" type="{http://iec.ch/61400/ews/1.0/}tFcdFcdaType" maxOccurs="unbounded" minOccurs="0"/>
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
    "rcbRef",
    "rcbType",
    "rptID",
    "rptEna",
    "datSet",
    "optFlds",
    "bufTm",
    "trgOp",
    "intgPd",
    "dsMbrRef"
})
@XmlRootElement(name = "AddSubscriptionRequest")
public class AddSubscriptionRequest {

    @XmlElement(name = "RCBRef", required = true)
    protected String rcbRef;
    @XmlElement(name = "RCBType", required = true)
    protected TRCBType rcbType;
    @XmlElement(name = "RptID")
    protected String rptID;
    @XmlElement(name = "RptEna")
    protected Boolean rptEna;
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
    @XmlElement(name = "DSMbrRef")
    protected List<TFcdFcdaType> dsMbrRef;
    @XmlAttribute(name = "UUID")
    protected String uuid;
    @XmlAttribute(name = "AssocID", required = true)
    protected String assocID;

    /**
     * Gets the value of the rcbRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRCBRef() {
        return rcbRef;
    }

    /**
     * Sets the value of the rcbRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRCBRef(String value) {
        this.rcbRef = value;
    }

    /**
     * Gets the value of the rcbType property.
     * 
     * @return
     *     possible object is
     *     {@link TRCBType }
     *     
     */
    public TRCBType getRCBType() {
        return rcbType;
    }

    /**
     * Sets the value of the rcbType property.
     * 
     * @param value
     *     allowed object is
     *     {@link TRCBType }
     *     
     */
    public void setRCBType(TRCBType value) {
        this.rcbType = value;
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
     * Gets the value of the dsMbrRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dsMbrRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDSMbrRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TFcdFcdaType }
     * 
     * 
     */
    public List<TFcdFcdaType> getDSMbrRef() {
        if (dsMbrRef == null) {
            dsMbrRef = new ArrayList<TFcdFcdaType>();
        }
        return this.dsMbrRef;
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
