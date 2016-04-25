package org.fortiss.smg.actuatorclient.froeschl.test;

import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.actuatorclient.froeschl.impl.ActuatorClientImpl;
import org.fortiss.smg.actuatorclient.froeschl.impl.JSONReaderFroeschl;
import org.fortiss.smg.actuatormaster.api.AbstractClient;
import org.junit.After;
import org.junit.Assert;
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

	private static org.fortiss.smg.actuatorclient.froeschl.test.MockOtherBundles mocker;
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
		mocker = new MockOtherBundles();
	}

	//For Fail: http://192.168.21.20/
	@Before
	public void setUp() {
		implClient = new ActuatorClientImpl("http://192.168.21.214/GetMeasuredValue.cgi" , "8080", "hexabus.wrapper", 10, "", "");
		// Register at Actuator Master (self, human readable name for device)
		try {
			registerAsClientAtServer(impl, "awesome-abstract-client-hexabus", new IOnConnectListener() {
				
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

	@Test(timeout = 500000)
	public void testYourMethod() throws TimeoutException {
		JSONReaderFroeschl jReader = new JSONReaderFroeschl();
		//URL working Test
		Assert.assertNotNull(jReader.readJsonFroeschl(implClient.getHost()));
			
	}
}
