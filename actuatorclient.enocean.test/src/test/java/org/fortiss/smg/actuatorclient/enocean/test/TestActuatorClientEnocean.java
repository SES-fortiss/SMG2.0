package org.fortiss.smg.actuatorclient.enocean.test;


import static org.junit.Assert.fail;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.actuatorclient.enocean.impl.ActuatorClientImpl;
import org.fortiss.smg.actuatorclient.enocean.impl.EnOceanLooper;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import org.fortiss.smg.actuatormaster.api.AbstractClient;
import org.fortiss.smg.actuatormaster.api.ActuatorMasterQueueNames;
import org.fortiss.smg.actuatormaster.api.IActuatorMaster;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.remoteframework.lib.DefaultProxy;


public class TestActuatorClientEnocean  extends AbstractClient{

	private static org.fortiss.smg.testing.MockOtherBundles mocker;

	//private ActuatorClientImpl impl;
	private EnOceanLooper looper;
	private static org.slf4j.Logger logger = LoggerFactory
			.getLogger(TestActuatorClientEnocean.class);
	private ActuatorClientImpl implClient;
	private IActuatorMaster broker;
	
	@BeforeClass
	public static void setUpDataBase() throws SQLException,
			ClassNotFoundException {
		ArrayList<String> bundles = new ArrayList<String>();

		bundles.add("Ambulance");
		bundles.add("InformationBroker");
		bundles.add("ActuatorMaster");
		bundles.add("ContainerManager");
		
		mocker = new org.fortiss.smg.testing.MockOtherBundles(bundles);
		
	}

	
	@Before
	public void setUp() {
		
		implClient = new ActuatorClientImpl("USB" , 3775, "enocean.wrapper",10);

		try {
			registerAsClientAtServer(implClient, "awesome-abstract-client-enocean", new IOnConnectListener() {
				
				@Override
				public void onSuccessFullConnection() {
					implClient.setMaster(master);
					implClient.setClientId(clientId);
					implClient.activate();
					implClient.connectToEncoean();
				}
			});
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//implClient.connectToEncoean();
		logger.info("ActuatorClient[enocean] is alive");		

		

	}

	@Test(timeout = 50000)
	public void testOnDeviceEventReceivedDeviceContainerString() throws TimeoutException, InterruptedException {
		
		looper = implClient.getLooperForTest();
		DeviceId devid = new DeviceId("31","office5070light");
		System.out.println(looper);
		implClient.getLooperForTest().setBoolean(true, devid.toString(), 0, "enocean", true);
	//	looper.setBoolean(true, devid.toString(), 0, "enocean", true);
		Thread.sleep(1000);
	//	looper.setBoolean(false, devid.toString(), 0, "enocean", true);
		Thread.sleep(1000);
		fail("Not yet implemented");
	}
	
}
