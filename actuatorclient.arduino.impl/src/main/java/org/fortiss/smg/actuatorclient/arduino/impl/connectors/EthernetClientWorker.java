/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.actuatorclient.arduino.impl.connectors;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.LoggerFactory;

/**
 * Class for establishing client socket communication. Working as thread
 * @author Balsa
 *
 */
public class EthernetClientWorker implements Runnable{
	
	private static org.slf4j.Logger logger = LoggerFactory
			.getLogger(EthernetClientWorker.class);

	// Client socket for establishing communication
    private Socket clientSocket;
    // Input stream for getting data from the server side
    private InputStream input;
    // Output stream for sending data to the server side
    private OutputStream output;
    // Flag for checking if the thread should be stopped
    private boolean running;
    
    private String arduinoWrappedData;
    
    ArrayList<String> connectedSensors;
    
    // String buffer for storing chars
    StringBuilder message = new StringBuilder();
    
    // Data from that communicator
    private HashMap<String, String> data = new HashMap<String, String>();

    /**
     * Constructor for creating EthernetClientWorker
     * @param clientSocket the socket that we will use for getting and sending data
     */
    public EthernetClientWorker(Socket clientSocket, String arduinoWrappedData, ArrayList<String> connectedSensors) {
        this.clientSocket = clientSocket;
        this.running = true;
        this.arduinoWrappedData = arduinoWrappedData;
        for (String clientId: connectedSensors) {
        	data.put(clientId, "0");
        }
    }

    /**
     * Method for receiving data from the arduino and storing them in the data.
     * It is reading data and then splitting to the name of the sensor and value of the sensor.
     * After reading data from the socket it should store it in the local data
     */
    @Override
    public void run() {
        try {
            output = clientSocket.getOutputStream();
            input = clientSocket.getInputStream();
            
            output.write(arduinoWrappedData.getBytes(Charset.forName("UTF-8")));
            
            while(isRunning()) {
                try {
                    byte b = (byte) input.read();
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
				/* connection either terminated by the client or lost due to
				 * network problems*/
                } catch (IOException ioe) {
                    logger.error("Error! Connection lost!");
                    running = false;
                }
            }
        } catch (IOException ioe) {
            logger.error("Error! Connection could not be established!", ioe);
        } 
    }

    /**
     * Method for checking if the thread is running or should be terminated
     * @return
     */
    private boolean isRunning() {
        return this.running;
    }

    /**
     * Method for stopping thread and closing all streams as well as a socket
     */
    public void close() {
        running = false;
        if (clientSocket != null) {
            try {
				input.close();
				output.close();
	            clientSocket.close();
	            logger.info("Ethernet communication successfully closed");
			} catch (IOException e) {
				logger.error("Failed to close the ethernet connection");
				e.printStackTrace();
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
			System.out.println("DATA FOR SENDING: "+data);
			output.write(data.getBytes());
		} catch (IOException e) {
			logger.error("Failed to write data to the device using ethernet connector");
			e.printStackTrace();
		};
	}
}
