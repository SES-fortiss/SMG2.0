
package ch.iec._61400.ews._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * Optional fields for URCB
 * 
 * <p>Java class for tOptFldsURCB complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tOptFldsURCB">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="seqNum" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="timeStamp" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="reasonCode" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="dataSet" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="dataRef" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="reserved" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="configRev" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tOptFldsURCB", propOrder = {
    "seqNum",
    "timeStamp",
    "reasonCode",
    "dataSet",
    "dataRef",
    "reserved",
    "configRev"
})
public class TOptFldsURCB {

    protected boolean seqNum;
    protected boolean timeStamp;
    protected boolean reasonCode;
    protected boolean dataSet;
    protected boolean dataRef;
    protected boolean reserved;
    protected boolean configRev;

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
     * Gets the value of the reserved property.
     * 
     */
    public boolean isReserved() {
        return reserved;
    }

    /**
     * Sets the value of the reserved property.
     * 
     */
    public void setReserved(boolean value) {
        this.reserved = value;
    }

    /**
     * Gets the value of the configRev property.
     * 
     */
    public boolean isConfigRev() {
        return configRev;
    }

    /**
     * Sets the value of the configRev property.
     * 
     */
    public void setConfigRev(boolean value) {
        this.configRev = value;
    }

}
