package org.fortiss.smg.actuatorclient.arduino.impl.connectors;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.LoggerFactory;

/**
 * EthernetCommunication responsible for communication with arduino using ethernet
 * @author Balsa
 *
 */
public class EthernetCommunication implements Communication {
	
	private static org.slf4j.Logger logger = LoggerFactory
			.getLogger(EthernetCommunication.class);

	// Port for establishing communication
    private int port;
    // Address for establishing communication
    private String address;
    // Client socket for connecting to the server
    private Socket clientSocket;
    // EthernetClientWorker responsible for creating separated thread for communication
    private EthernetClientWorker clientWorker;
    // All information that we need about the sensors wrapped into the file that will be later parsed on Arduino
    // side in order to setup all sensors and thresholds 
    private String arduinoWrappedData;

    // List of connected sensors names that are attached to that connector 
    private ArrayList<String> connectedSensors;

    /**
     * Constructor for creating EthernetCommunication
     * @param address the address of the pair
     * @param port the port of the pair
     * @param connectedSensors the list of all connected sensors 
     */
    public EthernetCommunication(String address, String port, ArrayList<String> connectedSensors, String arduinoWrappedData) {
        int port_number = Integer.parseInt(port);
        this.port = port_number;
        this.address = address;
        this.connectedSensors = connectedSensors;
        this.arduinoWrappedData = arduinoWrappedData;
    }

    /**
     * Method for for creating socket, EthernetClientWorker and starting that thread
     */
    @Override
    public void setup() {
        try {
            clientSocket = new Socket(address, port);
            System.out.println("Client is ready for listening server on address: " + address);
            clientWorker = new EthernetClientWorker(clientSocket, arduinoWrappedData, connectedSensors);
            new Thread(clientWorker).start();
            logger.info("Client is ready for listening server on address: " + address);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
        	System.out.println("The client cannot connect to the server on address: " + address);
        	logger.error("The client cannot connect to the server on address: " + address);
        }
    }
    
    /**
     * Called every second, getting data from the local connector data and storing it in the trasferData global hashmap
     * Method checks whether sensor needs to be updated. Going through the transferData and checks the signal value 
     * of all connected sensors. If the signal is 1 or 0 then we are sending it to the connector.
     * @param null
     * @return void
     */
    @Override
    public void run() {
        if (clientSocket != null) {
        	HashMap<String, String> data = clientWorker.getData();
    		for (String sensorId: connectedSensors) {
    			if (data.containsKey(sensorId)) {
    				// First check if there is any update for sensor
    				TransferData sensorUpdate = transferData.get(sensorId);
    				if (sensorUpdate != null) {
    					if (sensorUpdate.getSignal() != -1) {
    						System.out.println("SUCCESS: " + sensorUpdate.getDeviceId() + " : " + sensorUpdate.getValue() + " / " + sensorUpdate.getSignal());
    						clientWorker.writeDataToDevice(sensorUpdate.getDeviceId(), sensorUpdate.getSignal());
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
     * Method for closing the EthernetClientWorker as well as the client socket
     */
    @Override
    public void close() {
        clientWorker.close();
        try {
            clientSocket.close();
            logger.info("EthernetClientWorker is successfully closed");
        } catch (IOException e) {
            logger.error("Failed to close the EthernetClientWorker");
        }
    }
}
