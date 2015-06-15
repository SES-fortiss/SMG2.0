/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.actuatorclient.arduino.impl.connectors;

import java.util.HashMap;

import org.slf4j.LoggerFactory;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

/**
 * SerialCommunicationListener responsible for listening serial port and getting data
 * from the serial bus
 * @author Balsa
 *
 */
public class SerialCommunicationListener implements SerialPortEventListener {
	
	private static org.slf4j.Logger logger = LoggerFactory
			.getLogger(SerialCommunicationListener.class);

	// SerialPort used for establishing connection and getting data from
    private SerialPort serialPort;
    // String buffer for storing chars 
    private StringBuilder message = new StringBuilder();

    // Data from that communicator
    private HashMap<String, String> data = new HashMap<String, String>();

    /**
     * Constructor for creating SerialCommunicationListener (Works as a Thread)
     * @param serialPort the port for connecting
     */
    public SerialCommunicationListener(SerialPort serialPort) {
        this.serialPort = serialPort;
    }

    /**
     * Method responsible for getting data.
     * Behave as a run method in thread
     * @param serialPortEvent the event that is occurred
     */
    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        if(serialPortEvent.isRXCHAR() && serialPortEvent.getEventValue() > 0){
            try {
                byte buffer[] = serialPort.readBytes();
                for (byte b: buffer) {
                    if ((b == '\r' || b == '\n') && message.length() > 0) {
                    	String result = message.toString();
                        String[] parts = result.split("=");
                        String key = parts[0]; 
                        String value = parts[1]; 
                        data.put(key, value);
                        message.setLength(0);
                    }
                    else {
                        if (b != '\r' && b != '\n') {
                            message.append((char) b);
                        }
                    }
                }
            }
            catch (SerialPortException ex) {
                System.out.println(ex);
                System.out.println("serialEvent");
            }
        }
    }

    /**
     * Method for getting all sensors and values attached to the communicator
     * @return
     */
	public HashMap<String, String> getData() {
		return data;
	}
	
	/**
	 * Method for sending updates to the arduino device
	 * @param key the unique identifier for the sensor (same name should be on the arduino board)
	 * @param value the value that should be updated (0 - to turn off, 1 - to turn on)
	 */
	public void writeDataToDevice(String key, int value) {
		try {
			String data = key + "=" + value;
			System.out.println("DATA FOR SENDING: " + data);
			logger.info("Data for sending to the device: " + data);
			serialPort.writeString(data);
		} catch (SerialPortException e) {
			logger.error("Error sending data to the device");
			e.printStackTrace();
		}
	}
}
