package org.fortiss.smg.actuatorclient.arduino.impl.connectors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.LoggerFactory;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

/**
 * SerialCommunication the communication using the serial bus (USB)
 * @author Balsa
 *
 */
public class SerialCommunication implements Communication {
	
	private static org.slf4j.Logger logger = LoggerFactory
			.getLogger(SerialCommunication.class);
	
	// Special mask for reading characters
    private static final int mask = SerialPort.MASK_RXCHAR;
    // Serial port for communication with the device
    private SerialPort serialPort;
    // The name of the port where the device is attached (for example: COM3)
    private String port_name;
    // Flag for detecting whether the device 
    private boolean device_connected = false;
    // SerialCommunicationListener listen for a new events
    SerialCommunicationListener communicationListener;
    
    // List of connected sensors names that are attached to that connector
    ArrayList<String> connectedSensors = new ArrayList<String>();
    
    // Our global hash map for storing all sensor names, values and flags if they need to be updated
    ConcurrentHashMap<String, TransferData> transferData;
    
    /**
     * Constructor for creating the SerialCommunication
     * @param address the address of the device
     * @param connectedSensors the list of connected sensors to the connector
     * @param transferData the global data
     */
    public SerialCommunication(String address, ArrayList<String> connectedSensors, ConcurrentHashMap<String, TransferData> transferData) {
        this.port_name = address;
        this.connectedSensors = connectedSensors;
        this.transferData = transferData;
        this.serialPort = new SerialPort(address);
    }

    /**
     * Method for setting up serial communication 
     * It opens the communication on the given port, and then set the parameters for package size and the data mask
     * @param null
     * @return void
     */
    @Override
    public void setup() {
        try {
            if (isDeviceConnected()) {
                System.out.println("Device is successfully connected on port: " + port_name);
                serialPort.openPort();
                serialPort.setParams(9600, 8, 1, 0);
                serialPort.setEventsMask(mask);
                communicationListener = new SerialCommunicationListener(serialPort);
                serialPort.addEventListener(communicationListener);
                System.out.println("The serial communication is ready");
                logger.info("The serial communication is ready");
            }
            else {
                System.out.println("Unfortunately device is not connected on the port: " + port_name);
                logger.info("Unfortunately device is not connected on the port: " + port_name);
            }
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Called every second, getting data from the local connector data and storing it in the trasferData global hash-map
     * Method checks whether sensor needs to be updated. Going through the transferData and checks the signal value 
     * of all connected sensors. If the signal is 1 or 0 then we are sending it to the connector.
     * @param null
     * @return void
     */
    @Override
    public void run() {
    	// Every second gets values form the listener and add to the global transferData
    	if (isDeviceConnected()) {
    		HashMap<String, String> data = communicationListener.getData();
    		for (String sensorId: connectedSensors) {
    			if (data.containsKey(sensorId)) {
    				// First check if there is any update for sensor
    				TransferData sensorUpdate = transferData.get(sensorId);
    				if (sensorUpdate != null) {
    					if (sensorUpdate.getSignal() != -1) {
    						System.out.println("SUCCESS: " + sensorUpdate.getDeviceId() + " : " + sensorUpdate.getValue() + " / " + sensorUpdate.getSignal());
    						communicationListener.writeDataToDevice(sensorUpdate.getDeviceId(), sensorUpdate.getSignal());
    						sensorUpdate.setSignal(-1);
    						transferData.put(sensorId, sensorUpdate);
    					}
    				}
    				
    				// First check if the value is same as previous, if yes, then we don't need to update ActuatorMaster
    				int update;
    				if (sensorUpdate.getValue().equals(data.get(sensorId))) {
    					update = 0;
    				}
    				else {
    					update = 1;
    				}
    				// Then update values from sensor
    				TransferData sensorData = new TransferData(sensorId, data.get(sensorId), -1, update);
    				transferData.put(sensorId, sensorData);
    			}
    		}
        }
    }
    
    /**
     * Close communication between pairs
     * @param null
     * @return void
     */
    @Override
    public void close() {
        try {
            serialPort.closePort();
            logger.info("Serial communication is successfully closed");
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Method for checking whether the device is connected to the serial port
     * @param null
     * @return device_connected the flag if the device is connected
     */
    public boolean isDeviceConnected() {
        String portNames[] = SerialPortList.getPortNames();
        for (int i = 0; i < portNames.length; i++) {
            if (portNames[i].equals(port_name)) {
                device_connected = true;
            }
        }
        return device_connected;
    }
}
