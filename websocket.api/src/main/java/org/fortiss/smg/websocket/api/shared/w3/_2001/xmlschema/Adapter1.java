
package org.fortiss.smg.websocket.api.shared.w3._2001.xmlschema;

import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class Adapter1
    extends XmlAdapter<String, Date>
{


    public Date unmarshal(String value) {
        return null;//TODO (org.apache.cxf.tools.common.DataTypeAdapter.parseDateTime(value));
    }

    public String marshal(Date value) {
        return null;//TODO (org.apache.cxf.tools.common.DataTypeAdapter.printDateTime(value));
    }

}
