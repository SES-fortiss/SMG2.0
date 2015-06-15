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
 * <p>Java class for tOrcatValue.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="tOrcatValue">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="not-supported"/>
 *     &lt;enumeration value="reserved1"/>
 *     &lt;enumeration value="station-control"/>
 *     &lt;enumeration value="remote-control"/>
 *     &lt;enumeration value="reserved2"/>
 *     &lt;enumeration value="automatic-station"/>
 *     &lt;enumeration value="automatic-remote"/>
 *     &lt;enumeration value="maintenance"/>
 *     &lt;enumeration value="process"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "tOrcatValue")
@XmlEnum
public enum TOrcatValue {

    @XmlEnumValue("not-supported")
    NOT_SUPPORTED("not-supported"),
    @XmlEnumValue("reserved1")
    RESERVED_1("reserved1"),
    @XmlEnumValue("station-control")
    STATION_CONTROL("station-control"),
    @XmlEnumValue("remote-control")
    REMOTE_CONTROL("remote-control"),
    @XmlEnumValue("reserved2")
    RESERVED_2("reserved2"),
    @XmlEnumValue("automatic-station")
    AUTOMATIC_STATION("automatic-station"),
    @XmlEnumValue("automatic-remote")
    AUTOMATIC_REMOTE("automatic-remote"),
    @XmlEnumValue("maintenance")
    MAINTENANCE("maintenance"),
    @XmlEnumValue("process")
    PROCESS("process");
    private final String value;

    TOrcatValue(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TOrcatValue fromValue(String v) {
        for (TOrcatValue c: TOrcatValue.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
