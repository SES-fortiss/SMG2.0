package org.fortiss.smg.websocket.api.shared.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for DeviceCategory.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * <p>
 * 
 * <pre>
 * &lt;simpleType name="DeviceCategory">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Light"/>
 *     &lt;enumeration value="AC"/>
 *     &lt;enumeration value="Heating"/>
 *     &lt;enumeration value="Blinds"/>
 *     &lt;enumeration value="Sensor"/>
 *     &lt;enumeration value="WindowDetector"/>
 *     &lt;enumeration value="MotionDetector"/>
 *     &lt;enumeration value="Switch"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DeviceCategory")
@XmlEnum
public enum DeviceCategory {

    @XmlEnumValue("Light")
    LIGHT("Light"), @XmlEnumValue("AC")
    AC("AC"), @XmlEnumValue("Heating")
    HEATING("Heating"), @XmlEnumValue("Blinds")
    BLINDS("Blinds"), @XmlEnumValue("Sensor")
    SENSOR("Sensor"), @XmlEnumValue("WindowDetector")
    WINDOW_DETECTOR("WindowDetector"), @XmlEnumValue("MotionDetector")
    MOTION_DETECTOR("MotionDetector"), @XmlEnumValue("Switch")
    SWITCH("Switch");
    private final String value;

    DeviceCategory(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DeviceCategory fromValue(String v) {
        for (DeviceCategory c : DeviceCategory.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
