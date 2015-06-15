/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.actuatorclient.arduino.impl.models.beans;

public class CommunicationModel {
	// Name of the communication, for ex: serial or ethernet
    public String name;
    // Address of the device, for ex: for ethernet IP address (192.168.123.123), or for the serial serial port (COM3)
    public String address;
    // Port of the device, for ex: for ehternet port number (8080), or for serial it is null
    public String port;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
