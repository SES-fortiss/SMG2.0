package org.fortiss.smg.webrest.impl.types;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "customer")
public class TestXMLObject {

    String name;
    int pin;

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute
    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TestXMLObject)) {
            return false;
        }
        TestXMLObject cust2 = (TestXMLObject) obj;
        return pin == cust2.getPin() && cust2.getName().equals(name);
    }
}