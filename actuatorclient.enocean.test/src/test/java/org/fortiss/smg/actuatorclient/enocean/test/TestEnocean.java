package org.fortiss.smg.actuatorclient.enocean.test;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.actuatorclient.enocean.impl.ActuatorClientActivator;
import org.fortiss.smg.actuatorclient.enocean.impl.ActuatorClientImpl;
import org.fortiss.smg.actuatorclient.enocean.impl.EnOceanLooper;
import org.fortiss.smg.actuatormaster.api.ActuatorMasterQueueNames;
import org.fortiss.smg.actuatormaster.api.IActuatorMaster;
import org.fortiss.smg.actuatormaster.impl.ActuatorMasterActivator;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.containermanager.impl.ContainerManagerActivator;
import org.fortiss.smg.informationbroker.api.InformationBrokerInterface;
import org.fortiss.smg.informationbroker.api.InformationBrokerQueueNames;
import org.fortiss.smg.remoteframework.lib.DefaultProxy;
import org.fortiss.smg.sqltools.lib.utils.TestingDBUtil;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.LoggerFactory;

public class TestEnocean {

	private static MockOtherBundles mocker;

	private static org.slf4j.Logger logger = LoggerFactory
			.getLogger(TestEnocean.class);
	
	
	@BeforeClass
	public static void setUpDataBase() throws SQLException,
			ClassNotFoundException {		
		mocker = new MockOtherBundles();
	}

	
	private TestingDBUtil db;
	private ActuatorClientImpl impl;
	private ActuatorClientImpl implClient;
	private EnOceanLooper looper;

	@Before
	public void setUp() throws Exception {
		 
		 IActuatorMaster master = null;

		 DefaultProxy<IActuatorMaster> proxyMaster = new DefaultProxy<IActuatorMaster>(
					IActuatorMaster.class,
					ActuatorMasterQueueNames
							.getActuatorMasterInterfaceQueue(), 5000);
			
		 try {
				master = proxyMaster.init();
			} catch (IOException e) {
				System.out.println( ": Unable to connect to master");
			} catch (TimeoutException e) {
				System.out.println( ": Unable to connect to master (Timeout).");
			}
		
//		impl = new ActuatorClientImpl(master, "enocean-1", "USB" , 3775, "enocean.wrapper", 10);
						
		impl.activate();
				
		logger.info("ActuatorClient[enocean] is alive");
		
		impl.connectToEncoean();

		looper = impl.getLooperForTest();
		
		//ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
		//executor.scheduleAtFixedRate(looper, 0,1, TimeUnit.SECONDS);
		}

	@Test
	public void testOnDeviceEventReceivedDeviceContainerString() throws InterruptedException {
				
		DeviceId devid = new DeviceId("38","PowerPlug");
		looper.setBoolean(true, devid.toString(), 0, "enocean", true);
		Thread.sleep(1000);
		looper.setBoolean(false, devid.toString(), 0, "enocean", true);
		Thread.sleep(1000);
		fail("Not yet implemented");
	}

}