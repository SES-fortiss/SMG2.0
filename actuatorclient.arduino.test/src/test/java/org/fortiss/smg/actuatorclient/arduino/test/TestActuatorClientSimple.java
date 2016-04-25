package org.fortiss.smg.actuatorclient.arduino.test;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.fortiss.smg.actuatorclient.arduino.impl.ActuatorClientImpl;
import org.fortiss.smg.actuatorclient.arduino.impl.ArduinoConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
//import org.slf4j.LoggerFactory;

public class TestActuatorClientSimple{

	// Logger from sl4j
//    private static org.slf4j.Logger logger = LoggerFactory.getLogger(TestActuatorClientSimple.class);
	
//	private ActuatorClientImpl impl;
	ArduinoConfig arduinoConfig;

	@Before
	public void setUp() {
		
		
//		impl = new ActuatorClientImpl(arduinoConfig);
    }
	
	@Test
	public void testModel() {
		arduinoConfig = new ArduinoConfig();
		arduinoConfig.setHost("192.168.0.2");
		arduinoConfig.setPort("80");
		arduinoConfig.setPolling_frequency(10);
		arduinoConfig.setProtocol("ethernet");
		arduinoConfig.setWrapper_id("adroino_nano.wrapper");
		arduinoConfig.setWrapper_name("smg-client-adroino_nano");
		ArrayList<Integer> subdevices = new ArrayList<Integer>();
		subdevices.add(new Integer(12));
		
//		ActuatorClientImpl impl = new ActuatorClientImpl(arduinoConfig);
		
		assertEquals("ethernet", arduinoConfig.getProtocol());
		
		
		
//		impl = new ActuatorClientImpl(arduinoConfig);
		// Register at Actuator Master (self, human readable name for device)
//		try {
//			registerAsClientAtServer(impl, "awesome-abstract-client-hexabus", new IOnConnectListener() {
//				
//				@Override
//				public void onSuccessFullConnection() {
//					implClient.setClientId(clientId);
//					implClient.setMaster(master);
//					implClient.activate();			
//				}
//			});
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	@After
	public void tearDown(){
            // TODO do some cleanup
    }
}
