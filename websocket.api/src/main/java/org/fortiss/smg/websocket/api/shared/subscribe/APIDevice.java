package org.fortiss.smg.websocket.api.shared.subscribe;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import org.fortiss.smg.websocket.api.shared.schema.BoolDataPoint;
import org.fortiss.smg.websocket.api.shared.schema.DeviceSpec;
import org.fortiss.smg.websocket.api.shared.schema.DoubleDataPoint;
import org.fortiss.smg.websocket.api.shared.schema.StringDataPoint;
import org.fortiss.smg.websocket.api.shared.schema.ToggleDataPoint;

@XmlRootElement
public class APIDevice {
    String devid;
    String name;
    DeviceSpec deviceSpec;
    BoolDataPoint boolvalue;
    ToggleDataPoint togglevalue;
    ArrayList<DoubleDataPoint> doublevalues;
    StringDataPoint stringvalue;

    public APIDevice() {
    }

    public APIDevice(String id) {
        devid = id;
        doublevalues = new ArrayList<DoubleDataPoint>();
    }

    public BoolDataPoint getBoolvalue() {
        return boolvalue;
    }

    public DeviceSpec getDeviceSpec() {
        return deviceSpec;
    }

    public ArrayList<DoubleDataPoint> getDoublevalues() {
        return doublevalues;
    }

    public String getId() {
        return devid;
    }

    public String getName() {
        return name;
    }

    public StringDataPoint getStringvalue() {
        return stringvalue;
    }

    public ToggleDataPoint getTogglevalue() {
        return togglevalue;
    }

    public void setBoolvalue(BoolDataPoint boolvalue) {
        this.boolvalue = boolvalue;
    }

    public void setDeviceSpec(DeviceSpec deviceSpec) {
        this.deviceSpec = deviceSpec;
    }

    public void setDoublevalues(ArrayList<DoubleDataPoint> doublevalues) {
        this.doublevalues = doublevalues;
    }

    public void setId(String id) {
        devid = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStringvalue(StringDataPoint stringvalue) {
        this.stringvalue = stringvalue;
    }

    public void setTogglevalue(ToggleDataPoint togglevalue) {
        this.togglevalue = togglevalue;
    }

}
