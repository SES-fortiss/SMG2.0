package org.fortiss.smg.actuatorclient.arduino.impl.connectors;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Interface for all kinds of communication  possible to be established with Arduino
 * Extends Runnable in order to call them by ScheduledExecutorService every interval
 * @author Balsa
 *
 */
public interface Communication extends Runnable {
	
	// ConcurrentHashMap for all devices and their values that are read from the sensors as well as signals if sensor needs update
	// This hash map is thread safe
    ConcurrentHashMap<String, TransferData> transferData = new ConcurrentHashMap<String, TransferData>();

	/**
	 * Setting up all necessary information and establish communication
	 */
    public abstract void setup();

    /**
     * Start reading data from the device
     * Called by ScheduledExecutorService every interval
     */
    public abstract void run();

    /**
     * Close the connection to the device
     */
    public abstract void close();
}
