/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */

package ch.iec._61400.ews._1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tTimOperRsp.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="tTimOperRsp">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="timerActivated"/>
 *     &lt;enumeration value="commandExecuted"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "tTimOperRsp")
@XmlEnum
public enum TTimOperRsp {

    @XmlEnumValue("timerActivated")
    TIMER_ACTIVATED("timerActivated"),
    @XmlEnumValue("commandExecuted")
    COMMAND_EXECUTED("commandExecuted");
    private final String value;

    TTimOperRsp(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TTimOperRsp fromValue(String v) {
        for (TTimOperRsp c: TTimOperRsp.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
