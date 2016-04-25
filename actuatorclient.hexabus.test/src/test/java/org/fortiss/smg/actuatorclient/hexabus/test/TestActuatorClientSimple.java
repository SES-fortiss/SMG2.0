package org.fortiss.smg.actuatorclient.hexabus.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.actuatorclient.hexabus.impl.ActuatorClientImpl;
import org.fortiss.smg.actuatormaster.api.AbstractClient;
import org.fortiss.smg.actuatormaster.api.AbstractConnector.IOnConnectListener;
import org.fortiss.smg.containermanager.api.devices.DeviceContainer;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.testing.MockOtherBundles;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.LoggerFactory;
/*
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.OptionUtils;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)*/
public class TestActuatorClientSimple extends AbstractClient {

	private static org.fortiss.smg.testing.MockOtherBundles mocker;
	private ActuatorClientImpl impl;
	private ActuatorClientImpl implClient;
	private static org.slf4j.Logger logger = LoggerFactory
			.getLogger(ActuatorClientImpl.class);
	
/*
	@Configuration
	public Option[] config() {
		// this is used to build up a default OSGi Container and inject the SMG
		// scope
		// add here all API-libraries of the smg project on which your api &
		// impl depend on
		Option[] defaultSpace = Ops4JTestTime.getOptions();
		Option[] currentSpace = options(
				mavenBundle("org.fortiss.smartmicrogrid",
						"actuatormaster.api", "1.0-SNAPSHOT"),
				mavenBundle("org.fortiss.smartmicrogrid",
						"actuatorclient.hexabus	.impl", "1.0-SNAPSHOT")
						);

		return OptionUtils.combine(defaultSpace, currentSpace);
	}
*/
	
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
		implClient = new ActuatorClientImpl("http://192.168.21.217:8080/devicetree.json" , "8080", "hexabus.wrapper", 10, "", "");
		// Register at Actuator Master (self, human readable name for device)
		try {
			registerAsClientAtServer(implClient, "awesome-abstract-client-hexabus", new IOnConnectListener() {
				
				@Override
				public void onSuccessFullConnection() {
					implClient.setClientId(clientId);
					implClient.setMaster(master);
					implClient.activate();			
				}
			});
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@After
	public void tearDown() {
		// TODO do some cleanup
	}

	/**
	 * If it doesen't throw Error it should Work.
	 */
	
	@Test() //expected = NullPointerException.class
	public void shouldTurnDeviceOn() throws TimeoutException {
		implClient.onDoubleCommand(new org.fortiss.smg.smgschemas.commands.DoubleCommand(1), new DeviceId("fdaa:e4c:dfc5:7b09:50:c4ff:fe04:8404.2", "hexabus.wrapper")); // old dev fdfb:8f9f:4c8:bb4b:50:c4ff:fe04:83eb.2
	// To turn off all devs
	//	for(DeviceContainer dev: implClient.getDevices()){
		//	implClient.onDoubleCommand(new org.fortiss.smg.smgschemas.commands.DoubleCommand(0), dev.getDeviceId());
			
		//}
	}
	
	@Test(timeout = 5000, expected = TimeoutException.class)
	public void shouldLoadDevs() throws TimeoutException {
		implClient.loadStaticDevs("hexabus.wrapper");
	}
}
