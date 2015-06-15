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
 * <p>Java class for tServiceError.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="tServiceError">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="instance-not-available"/>
 *     &lt;enumeration value="instance-in-use"/>
 *     &lt;enumeration value="access-violation"/>
 *     &lt;enumeration value="access-not-allowed-in-current-state"/>
 *     &lt;enumeration value="parameter-value-inappropriate"/>
 *     &lt;enumeration value="parameter-value-inconsistent"/>
 *     &lt;enumeration value="class-not-supported"/>
 *     &lt;enumeration value="instance-locked-by-other-client"/>
 *     &lt;enumeration value="control-must-be-selected"/>
 *     &lt;enumeration value="type-conflict"/>
 *     &lt;enumeration value="failed-due-to-communications-constraint"/>
 *     &lt;enumeration value="failed-due-to-server-constraint"/>
 *     &lt;enumeration value="application-unreachable"/>
 *     &lt;enumeration value="connection-lost"/>
 *     &lt;enumeration value="memory-unavailable"/>
 *     &lt;enumeration value="processor-resource-unavailable"/>
 *     &lt;enumeration value="connection-lost"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "tServiceError")
@XmlEnum
public enum TServiceError {

    @XmlEnumValue("instance-not-available")
    INSTANCE_NOT_AVAILABLE("instance-not-available"),
    @XmlEnumValue("instance-in-use")
    INSTANCE_IN_USE("instance-in-use"),
    @XmlEnumValue("access-violation")
    ACCESS_VIOLATION("access-violation"),
    @XmlEnumValue("access-not-allowed-in-current-state")
    ACCESS_NOT_ALLOWED_IN_CURRENT_STATE("access-not-allowed-in-current-state"),
    @XmlEnumValue("parameter-value-inappropriate")
    PARAMETER_VALUE_INAPPROPRIATE("parameter-value-inappropriate"),
    @XmlEnumValue("parameter-value-inconsistent")
    PARAMETER_VALUE_INCONSISTENT("parameter-value-inconsistent"),
    @XmlEnumValue("class-not-supported")
    CLASS_NOT_SUPPORTED("class-not-supported"),
    @XmlEnumValue("instance-locked-by-other-client")
    INSTANCE_LOCKED_BY_OTHER_CLIENT("instance-locked-by-other-client"),
    @XmlEnumValue("control-must-be-selected")
    CONTROL_MUST_BE_SELECTED("control-must-be-selected"),
    @XmlEnumValue("type-conflict")
    TYPE_CONFLICT("type-conflict"),
    @XmlEnumValue("failed-due-to-communications-constraint")
    FAILED_DUE_TO_COMMUNICATIONS_CONSTRAINT("failed-due-to-communications-constraint"),
    @XmlEnumValue("failed-due-to-server-constraint")
    FAILED_DUE_TO_SERVER_CONSTRAINT("failed-due-to-server-constraint"),
    @XmlEnumValue("application-unreachable")
    APPLICATION_UNREACHABLE("application-unreachable"),
    @XmlEnumValue("connection-lost")
    CONNECTION_LOST("connection-lost"),
    @XmlEnumValue("memory-unavailable")
    MEMORY_UNAVAILABLE("memory-unavailable"),
    @XmlEnumValue("processor-resource-unavailable")
    PROCESSOR_RESOURCE_UNAVAILABLE("processor-resource-unavailable");
    private final String value;

    TServiceError(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TServiceError fromValue(String v) {
        for (TServiceError c: TServiceError.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
